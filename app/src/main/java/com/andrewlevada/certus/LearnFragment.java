package com.andrewlevada.certus;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.andrewlevada.certus.logic.lessons.FakeLesson;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.logic.lessons.storageunits.Grade;
import com.andrewlevada.certus.logic.lessons.storageunits.LessonStatus;
import com.andrewlevada.certus.logic.lessons.storageunits.Subject;

import java.util.ArrayList;


/**
 * A simple {@link RecyclerFragment} subclass.
 */
public class LearnFragment extends RecyclerFragment {
    public LearnFragment() {
        super();
    }

    public LearnFragment(HomeActivity hostActivity) {
        super(hostActivity, R.layout.backdrop_content_learn);
    }

    @Override
    ArrayList<RecyclableLesson> getFakeDataset() {
        ArrayList<RecyclableLesson> list = new ArrayList<>();

        LessonStatus status = new LessonStatus();
        status.setStatus(LessonStatus.STATUS_CHAT);
        list.add(new FakeLesson(getContext(), "Водородная связь", status, Subject.CODE_CHEMISTRY, Grade.MIDDLE_CODE));

        status = new LessonStatus();
        status.setStatus(LessonStatus.STATUS_SEARCH);
        list.add(new FakeLesson(getContext(), "Формулы сокращённого умножения", status, Subject.CODE_ALGEBRA, Grade.BEGINNER_CODE));
        list.add(new FakeLesson(getContext(), "Теория вероятностей", status, Subject.CODE_ALGEBRA, Grade.HIGH_CODE));

        return list;
    }

    @Override
    void fillBackdrop(ViewGroup backdrop) {
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
}
