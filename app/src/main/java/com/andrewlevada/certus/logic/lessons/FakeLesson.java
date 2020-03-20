package com.andrewlevada.certus.logic.lessons;

import android.content.Context;

import com.andrewlevada.certus.logic.lessons.storageunits.Grade;
import com.andrewlevada.certus.logic.lessons.storageunits.LessonStatus;
import com.andrewlevada.certus.logic.lessons.storageunits.Subject;

public class FakeLesson implements RecyclableLesson {
    private String title;
    private LessonStatus status;
    private Subject subject;
    private Grade grade;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public LessonStatus getStatus() {
        return status;
    }

    @Override
    public Subject getSubject() {
        return subject;
    }

    @Override
    public Grade getGrade() {
        return grade;
    }

    public FakeLesson(Context context, String title, LessonStatus status, int subject, int grade) {
        this.title = title;
        this.status = status;
        this.subject = new Subject(context, subject);
        this.grade = new Grade(context, grade);
    }
}
