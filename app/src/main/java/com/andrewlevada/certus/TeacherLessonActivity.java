package com.andrewlevada.certus;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.andrewlevada.certus.logic.livepapers.LivePaper;
import com.andrewlevada.certus.tools.SimpleInflater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class TeacherLessonActivity extends AppCompatActivity {
    private LivePaper livePaper;

    private ConstraintLayout layout;
    private LinearLayout livePaperView;
    private LinearLayout focusWrapper;
    private ScrollView focusLayout;

    private ConstraintSet defaultConstraint;
    private ConstraintSet focusConstraint;

    private OnBackPressedCallback backFocusCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_teacher);

        livePaper = new LivePaper(getApplicationContext(), this::onViewClick);

        // Find views by ids
        layout = findViewById(R.id.parent_layout);
        focusWrapper = findViewById(R.id.lp_focus_wrapper);
        focusLayout = findViewById(R.id.lp_focus_layout);
        livePaperView = findViewById(R.id.live_paper);

        // Setup focus
        Point display = new Point();
        getWindowManager().getDefaultDisplay().getSize(display);
        focusWrapper.getLayoutParams().height = display.y;

        // Setup ConstraintSets for focus animations
        defaultConstraint = new ConstraintSet();
        defaultConstraint.clone(layout);
        focusConstraint = new ConstraintSet();
        focusConstraint.load(getApplicationContext(), R.layout.activity_lesson_teacher_focus);

        // Setup database connection
        String sessionName = "testing";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sessions/" + sessionName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> root;
                List<Map<String, Object>> document;

                try {
                    root = (Map<String, Object>) dataSnapshot.getValue();
                    if (root == null) return;
                    document = (List<Map<String, Object>>) root.get("document");
                    if (document == null) return;
                } catch (Exception ex) {
                    return;
                }

                // TODO: Process session options

                int i = 0;
                for (Map<String, Object> node : document) {
                    livePaper.process(livePaperView, node, i);
                    i++;
                }

                for (int j = livePaperView.getChildCount() - 1; j >= document.size(); j--) {
                    livePaperView.removeViewAt(j);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // On add button clicked
        findViewById(R.id.lp_add_button).setOnClickListener(v -> {
            // TODO: Add item addition
        });
    }

    private void updateFocus(boolean extend) {
        ConstraintSet constraintSet;

        // Load needed layout
        if (extend) constraintSet = focusConstraint;
        else constraintSet = defaultConstraint;

        // Setup transition
        Transition transition = new AutoTransition();
        transition.setDuration(300);
        if (extend) transition.setInterpolator(new FastOutSlowInInterpolator());
        else transition.setInterpolator(new FastOutLinearInInterpolator());

        // Make transition
        TransitionManager.beginDelayedTransition(layout, transition);
        constraintSet.applyTo(layout);

        // Process back button
        if (extend) {
            backFocusCallback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    updateFocus(false);
                    remove();

                    // TODO: Send data to server
                }
            };

            getOnBackPressedDispatcher().addCallback(this, backFocusCallback);
        } else {
            backFocusCallback.remove();
            backFocusCallback = null;
        }
    }

    private View fillFocus(@LayoutRes int layout) {
        focusLayout.removeAllViews();
        View view = SimpleInflater.inflate(focusLayout, layout);
        return view;
    }

    private void onViewClick(View view) {
        String tag = view.getTag().toString();

        if (tag.equals("text") || tag.equals("header")) {
            View v = fillFocus(R.layout.lp_f_text);
            ((EditText) v.findViewById(R.id.lp_f_text_field)).setText(((TextView) view).getText());

            // Show keyboard
            v.findViewById(R.id.lp_f_text_field).requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else if (tag.equals("block")) {
            // Do not process
            return;
        } else if (tag.equals("list")) {

        } else if (tag.equals("rule")) {

        }

        updateFocus(true);
    }
}
