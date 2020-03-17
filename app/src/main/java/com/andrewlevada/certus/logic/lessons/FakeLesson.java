package com.andrewlevada.certus.logic.lessons;

import android.content.Context;

import com.andrewlevada.certus.logic.subjects.Subject;

public class FakeLesson implements RecyclableLesson {
    private String title;
    private LessonStatus status;
    private Subject subject;

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

    public FakeLesson(Context context, String title, LessonStatus status, int subject) {
        this.title = title;
        this.status = status;
        this.subject = new Subject(context, subject);
    }
}
