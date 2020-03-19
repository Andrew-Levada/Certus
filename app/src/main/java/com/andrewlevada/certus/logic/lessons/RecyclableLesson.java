package com.andrewlevada.certus.logic.lessons;

import com.andrewlevada.certus.logic.storageunits.Subject;

public interface RecyclableLesson {
    String getTitle();
    LessonStatus getStatus();
    Subject getSubject();
}
