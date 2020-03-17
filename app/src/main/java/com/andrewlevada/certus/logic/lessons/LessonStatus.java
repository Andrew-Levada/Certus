package com.andrewlevada.certus.logic.lessons;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LessonStatus {
    public static final int STATUS_REPORTED = 0;
    public static final int STATUS_SEARCH = 1;
    public static final int STATUS_CHAT = 2;
    public static final int STATUS_TIMED = 3;

    private int status;
    private Calendar lessonDate;

    public int getStatus() {
        return status;
    }

    public Calendar getLessonDate() {
        return lessonDate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatus(int status, Calendar lessonDate) {
        this.status = status;
        this.lessonDate = lessonDate;
    }

    public String getStatusString() {
        String string = "";

        switch (status) {
            case STATUS_REPORTED:
            case STATUS_SEARCH:
                string = "В процессе поиска...";
                break;

            case STATUS_CHAT:
                string = "Время не определено";
                break;

            case STATUS_TIMED:
                string = "Урок ";

                Calendar now = Calendar.getInstance();
                SimpleDateFormat formater = new SimpleDateFormat();

                if (lessonDate.before(now)) {
                    string += "уже прошёл";
                    break;
                }

                if (lessonDate.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    int difference = lessonDate.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
                    if (difference == 0) string += "сегодня в ";
                    else if (difference == 1) string += "завтра в ";
                    else {
                        formater.applyPattern("dd.MM");
                        string += formater.format(lessonDate) + " в ";
                    }
                } else {
                    formater.applyPattern("dd.MM.yyyy");
                    string += formater.format(lessonDate) + " в ";
                }

                formater.applyPattern("HH:mm");
                string += formater.format(lessonDate);
                break;
        }

        return string;
    }

    public boolean isChatable() {
        return (status == STATUS_CHAT || status == STATUS_TIMED);
    }

    public LessonStatus() {
        status = STATUS_SEARCH;
    }
}
