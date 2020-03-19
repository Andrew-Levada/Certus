package com.andrewlevada.certus.logic.storageunits;

import android.content.Context;

import androidx.annotation.NonNull;

import com.andrewlevada.certus.R;

public class Grade {
    public static final int ELEMENTARY_CODE = 0;
    public static final int BEGINNER_CODE = 1;
    public static final int MIDDLE_CODE = 2;
    public static final int HIGH_CODE = 3;

    public static final int AMOUNT = 4;

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Grade(@NonNull Context context, int code) {
        this.code = code;
        name = context.getResources().getStringArray(R.array.gradeNames)[code];
    }

    @NonNull
    public static String[] getStringArray(@NonNull Context context) {
        return context.getResources().getStringArray(R.array.gradeNames);
    }
}
