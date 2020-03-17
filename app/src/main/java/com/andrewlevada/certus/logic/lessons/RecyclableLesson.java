package com.andrewlevada.certus.logic.lessons;

import com.andrewlevada.certus.logic.subjects.Subject;

public interface RecyclableLesson {
    String getTitle();
    LessonStatus getStatus();
    Subject getSubject();
}
