package com.andrewlevada.certus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewlevada.certus.logic.lessons.FakeLesson;
import com.andrewlevada.certus.logic.lessons.LessonStatus;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.logic.subjects.Subject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeachFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeachFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclableLesson> list;

    public TeachFragment() {
        list = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Learn.
     */
    public static TeachFragment newInstance() {
        TeachFragment fragment = new TeachFragment();
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

        return layout;
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
}
