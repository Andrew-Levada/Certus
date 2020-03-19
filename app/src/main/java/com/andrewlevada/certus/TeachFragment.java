package com.andrewlevada.certus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.andrewlevada.certus.logic.lessons.FakeLesson;
import com.andrewlevada.certus.logic.lessons.LessonStatus;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.logic.storageunits.Grade;
import com.andrewlevada.certus.logic.storageunits.Subject;
import com.andrewlevada.certus.tools.SimpleInflater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeachFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeachFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclableLesson> list;

    private MainActivity hostActivity;

    private Handler backdropFillHandler;
    private Runnable backdropFillRunnable;

    public TeachFragment() {
        hostActivity = (MainActivity) getActivity();
        list = new ArrayList<>();
    }

    public TeachFragment(MainActivity hostActivity) {
        this.hostActivity = hostActivity;
        list = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Learn.
     */
    public static TeachFragment newInstance(MainActivity hostActivity) {
        TeachFragment fragment = new TeachFragment(hostActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_teach, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.teach_home_recycler);
        setupRecyclerView();

        fillFakeDataset();
        //TODO: Request to server to update list array

        backdropFillRunnable = new Runnable() {
            @Override
            public void run() {
                if (hostActivity != null)
                    hostActivity.requestFAB(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hostActivity.updateBackdrop(true);
                        }
                    });

                    hostActivity.fillBackdrop(R.layout.backdrop_content_learn, new SimpleInflater.OnViewInflated() {
                        @Override
                        public void inflated(View view) {
                            fillBackdrop((ViewGroup) view);
                        }
                    });
            }
        };

        backdropFillHandler = new Handler();
        backdropFillHandler.postDelayed(backdropFillRunnable, 250);

        return layout;
    }

    @Override
    public void onDestroy() {
        backdropFillHandler.removeCallbacks(backdropFillRunnable);
        super.onDestroy();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerBasicAdapter adapter = new RecyclerBasicAdapter(recyclerView, list);
        recyclerView.setAdapter(adapter);
    }

    private void fillFakeDataset() {
        LessonStatus status = new LessonStatus();
        status.setStatus(LessonStatus.STATUS_CHAT);
        list.add(new FakeLesson(getContext(), "Водородная связь", status, Subject.CODE_CHEMISTRY));
        list.add(new FakeLesson(getContext(), "Котология по лапкам", status, Subject.CODE_ASTRONOMY));
    }

    private void fillBackdrop(ViewGroup backdrop) {
        if (getContext() == null
                || backdrop.findViewById(R.id.teach_dropdown_subjects) == null
                || backdrop.findViewById(R.id.teach_dropdown_grades) == null) return;

        ArrayAdapter subjectsAdapter = new ArrayAdapter(
                getContext(), R.layout.dropdown_menu_popup_item, Subject.getStringArray(getContext()));

        AutoCompleteTextView subjects = backdrop.findViewById(R.id.teach_dropdown_subjects);
        subjects.setHint("Предмет");
        subjects.setAdapter(subjectsAdapter);

        ArrayAdapter gradesAdapter = new ArrayAdapter(
                getContext(), R.layout.dropdown_menu_popup_item, Grade.getStringArray(getContext()));

        AutoCompleteTextView grades = backdrop.findViewById(R.id.teach_dropdown_grades);
        grades.setHint("Классы");
        grades.setAdapter(gradesAdapter);
    }
}
