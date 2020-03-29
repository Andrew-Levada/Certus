package com.andrewlevada.certus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andrewlevada.certus.tools.SimpleInflater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // Find views by ids
        final ViewGroup lpView = findViewById(R.id.live_paper);

        // Setup database connection
        String sessionName = "testing";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sessions/" + sessionName);

        ref.addValueEventListener(new ValueEventListener() {
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

                for (Map<String, Object> node : document) {
                    process(lpView, node);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FB_E", "onCancelled");
            }
        });
    }

    private void process(ViewGroup parent, Map<String, Object> node) {
        String type = (String) node.get("t");
        if (type == null) return;

        if (type.equals("text")) {
            addText(parent, node, false);
        } else if (type.equals("block")) {
            addBlock(parent, node);
        } else if (type.equals("header")) {
            addText(parent, node, true);
        } else if (type.equals("list")) {
            addList(parent, node);
        } else if (type.equals("rule")) {
            addRule(parent, node);
        }
    }

    private void addText(ViewGroup parent, Map<String, Object> node, boolean isHeader) {
        Object text = node.get("v");
        SpannableStringBuilder stringBuilder = getStringBuilder(text);

        TextView view;
        if (isHeader) view = (TextView) SimpleInflater.inflate(parent, R.layout.lp_header);
        else view = (TextView) SimpleInflater.inflate(parent, R.layout.lp_text);

        view.setText(stringBuilder);
    }

    private void addBlock(ViewGroup parent, Map<String, Object> node) {
        ViewGroup view = (ViewGroup) SimpleInflater.inflate(parent, R.layout.lp_block);

        for (Map<String, Object> o : (List<Map<String, Object>>) node.get("v")) process(view, o);
    }

    private void addList(ViewGroup parent, Map<String, Object> node) {
        ViewGroup view = (ViewGroup) SimpleInflater.inflate(parent, R.layout.lp_block);
        String style = (String) node.get("s");
        if (style == null) style = "bullet";

        int i = 1;
        for (Object o : (List) node.get("v")) {
            View v = null;
            if (style.equals("bullet")) v = SimpleInflater.inflate(view, R.layout.lp_list_bullet);
            else if (style.equals("num")) v = SimpleInflater.inflate(view, R.layout.lp_list_num);
            if (v == null) return;

            SpannableStringBuilder stringBuilder = getStringBuilder(o);
            ((TextView) v.findViewById(R.id.lp_list_text)).setText(stringBuilder);

            if (style.equals("num")) ((TextView) v.findViewById(R.id.lp_list_num)).setText(i + ".");

            i++;
        }
    }

    private void addRule(ViewGroup parent, Map<String, Object> node) {
        ViewGroup view = (ViewGroup) SimpleInflater.inflate(parent, R.layout.lp_rule_box);

        if (node.get("v") instanceof Map) process(view, (Map<String, Object>) node.get("v"));
        else for (Map<String, Object> o : (List<Map<String, Object>>) node.get("v"))
            process(view, o);
    }

    private SpannableStringBuilder getStringBuilder(Object text) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        if (text instanceof String) stringBuilder.append((String) text);
        else if (text instanceof List) {
            for (Object o : (List<Object>) text) {
                if (o instanceof String) {
                    stringBuilder.append(o + " ");
                    continue;
                }

                Map m = (Map) o;
                String style = (String) m.get("s");
                if (style == null) {
                    stringBuilder.append((String) m.get("v"));
                } else if (style.equals("bold")) {
                    stringBuilder.append((String) m.get("v"),
                            new StyleSpan(Typeface.BOLD), Spanned.SPAN_MARK_MARK);
                } else if (style.equals("italic")) {
                    stringBuilder.append((String) m.get("v"),
                            new StyleSpan(Typeface.ITALIC), Spanned.SPAN_MARK_MARK);
                } else if (style.equals("highlight")) {
                    stringBuilder.append((String) m.get("v"),
                            new BackgroundColorSpan(getResources().getColor(R.color.colorSecondaryLight)), Spanned.SPAN_MARK_MARK);
                }

                stringBuilder.append(" ");
            }
        }

        return stringBuilder;
    }
}
