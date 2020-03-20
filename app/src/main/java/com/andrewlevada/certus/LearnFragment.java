package com.andrewlevada.certus;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.certus.logic.User;
import com.andrewlevada.certus.logic.lessons.FakeLesson;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.logic.lessons.storageunits.Grade;
import com.andrewlevada.certus.logic.lessons.storageunits.LessonStatus;
import com.andrewlevada.certus.logic.lessons.storageunits.Subject;
import com.andrewlevada.certus.tools.SimpleInflater;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearnFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclableLesson> list;

    private HomeActivity hostActivity;

    private Handler afterAnimHandler;
    private Runnable afterAnimRunnable;

    public LearnFragment() {
        this.hostActivity = (HomeActivity) getActivity();
        list = new ArrayList<>();
    }

    public LearnFragment(HomeActivity hostActivity) {
        this.hostActivity = hostActivity;
        list = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Learn.
     */
    public static LearnFragment newInstance(HomeActivity hostActivity) {
        LearnFragment fragment = new LearnFragment(hostActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate fragment view
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_learn, container, false);

        // Setup recycler view
        recyclerView = (RecyclerView) layout.findViewById(R.id.learn_home_recycler);
        setupRecyclerView(new RecyclerBasicAdapter.OnChatOpenRequest() {
            @Override
            public void request(int index) {
                openChatRequest(index);
            }
        });

        // Get data
        fillFakeDataset();
        // TODO: Request to server to update list array

        // Add actions after fragment switching is finished
        afterAnimRunnable = new Runnable() {
            @Override
            public void run() {
                if (hostActivity == null) return;

                // Request fab from activity
                hostActivity.requestFAB(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hostActivity.updateBackdrop(true);
                    }
                });

                // Request to fill backdrop
                hostActivity.fillBackdrop(R.layout.backdrop_content_learn, new SimpleInflater.OnViewInflated() {
                    @Override
                    public void inflated(View view) {
                        fillBackdrop((ViewGroup) view);
                    }
                });
            }
        };

        // Setup actions after fragment switching is finished
        afterAnimHandler = new Handler();
        afterAnimHandler.postDelayed(afterAnimRunnable, 250);

        return layout;
    }

    @Override
    public void onDestroy() {
        // If anim is not finished cancel after actions
        afterAnimHandler.removeCallbacks(afterAnimRunnable);

        super.onDestroy();
    }

    private void setupRecyclerView(RecyclerBasicAdapter.OnChatOpenRequest chatRequest) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerBasicAdapter adapter = new RecyclerBasicAdapter(recyclerView, list, chatRequest);
        recyclerView.setAdapter(adapter);
    }

    @Deprecated
    private void fillFakeDataset() {
        LessonStatus status = new LessonStatus();
        status.setStatus(LessonStatus.STATUS_CHAT);
        list.add(new FakeLesson(getContext(), "Водородная связь", status, Subject.CODE_CHEMISTRY, Grade.MIDDLE_CODE));

        status = new LessonStatus();
        status.setStatus(LessonStatus.STATUS_SEARCH);
        list.add(new FakeLesson(getContext(), "Формулы сокращённого умножения", status, Subject.CODE_ALGEBRA, Grade.BEGINNER_CODE));
        list.add(new FakeLesson(getContext(), "Теория вероятностей", status, Subject.CODE_ALGEBRA, Grade.HIGH_CODE));
    }

    private void fillBackdrop(ViewGroup backdrop) {
        if (getContext() == null) return;

        ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<>(
                getContext(), R.layout.dropdown_menu_popup_item, Subject.getStringArray(getContext()));

        AutoCompleteTextView subjects = backdrop.findViewById(R.id.learn_new_dropdown_subjects);
        subjects.setHint("Предмет");
        subjects.setAdapter(subjectsAdapter);

        ArrayAdapter<String> gradesAdapter = new ArrayAdapter<>(
                getContext(), R.layout.dropdown_menu_popup_item, Grade.getStringArray(getContext()));

        AutoCompleteTextView grades = backdrop.findViewById(R.id.learn_new_dropdown_grades);
        grades.setHint("Классы");
        grades.setAdapter(gradesAdapter);
    }

    private void openChatRequest(int index) {
        User user = User.getInstance();

        // Require auth
        if (user == null || !user.isAuthed()) {
            if (hostActivity == null) return;
            hostActivity.requestAuth();
        }

        // TODO: Open chat
    }
}
