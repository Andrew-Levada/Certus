package com.andrewlevada.certus;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;

import com.andrewlevada.certus.logic.lessons.FakeLesson;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.logic.lessons.storageunits.Grade;
import com.andrewlevada.certus.logic.lessons.storageunits.LessonStatus;
import com.andrewlevada.certus.logic.lessons.storageunits.Subject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeachFragment extends RecyclerFragment {
    public TeachFragment() {
        super();
    }

    public TeachFragment(HomeActivity hostActivity) {
        super(hostActivity, R.layout.backdrop_content_teach);
    }

    @Override
    ArrayList<RecyclableLesson> getFakeDataset() {
        ArrayList<RecyclableLesson> list = new ArrayList<>();

        LessonStatus status = new LessonStatus();
        status.setStatus(LessonStatus.STATUS_CHAT);
        list.add(new FakeLesson(getContext(), "Водородная связь", status, Subject.CODE_CHEMISTRY, Grade.HIGH_CODE));
        list.add(new FakeLesson(getContext(), "Котология по лапкам", status, Subject.CODE_ASTRONOMY, Grade.HIGH_CODE));

        return list;
    }

    @Override
    void fillBackdrop(ViewGroup backdrop) {
        if (getContext() == null) return;

        ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<>(
                getContext(), R.layout.dropdown_menu_popup_item, Subject.getStringArray(getContext()));

        AutoCompleteTextView subjects = backdrop.findViewById(R.id.teach_dropdown_subjects);
        subjects.setHint("Предмет");
        subjects.setAdapter(subjectsAdapter);

        ArrayAdapter<String> gradesAdapter = new ArrayAdapter<>(
                getContext(), R.layout.dropdown_menu_popup_item, Grade.getStringArray(getContext()));

        AutoCompleteTextView grades = backdrop.findViewById(R.id.teach_dropdown_grades);
        grades.setHint("Классы");
        grades.setAdapter(gradesAdapter);
    }
}
