package com.andrewlevada.certus.logic.storageunits;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.andrewlevada.certus.R;

public class Subject {
    public static final int CODE_MATH = 0;
    public static final int CODE_ALGEBRA = 1;
    public static final int CODE_GEOMETRY = 2;
    public static final int CODE_IT = 3;

    public static final int CODE_RUSSIAN = 4;
    public static final int CODE_LITERATURE = 5;
    public static final int CODE_LANGUAGE = 6;
    public static final int CODE_HISTORY = 7;
    public static final int CODE_SOCIAL = 8;

    public static final int CODE_PHYSICS = 9;
    public static final int CODE_CHEMISTRY = 10;
    public static final int CODE_BIOLOGY = 11;
    public static final int CODE_GEOGRAPHY = 12;
    public static final int CODE_ASTRONOMY = 13;

    public static final int CODE_OTHER = 14;

    public static final int AMOUNT = 15;

    private int code;
    @ColorInt
    private int color;
    private String name;

    public int getCode() {
        return code;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Subject(@NonNull Context context, int code) {
        this.code = code;
        color = context.getResources().getIntArray(R.array.subjectColors)[code];
        name = context.getResources().getStringArray(R.array.subjectNames)[code];
    }

    @NonNull
    public static String[] getStringArray(@NonNull Context context) {
        return context.getResources().getStringArray(R.array.subjectNames);
    }
}
