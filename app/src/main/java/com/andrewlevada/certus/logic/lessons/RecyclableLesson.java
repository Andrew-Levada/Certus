package com.andrewlevada.certus.logic.lessons;

import com.andrewlevada.certus.logic.lessons.storageunits.Grade;
import com.andrewlevada.certus.logic.lessons.storageunits.LessonStatus;
import com.andrewlevada.certus.logic.lessons.storageunits.Subject;

public interface RecyclableLesson {
    String getTitle();

    LessonStatus getStatus();

    Subject getSubject();

    Grade getGrade();
}
