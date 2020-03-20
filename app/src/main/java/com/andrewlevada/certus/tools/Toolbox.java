package com.andrewlevada.certus.tools;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Toolbox {
    @Nullable
    public static View getLastChild(@NonNull ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);

            if (child != null) return child;
        }
        return null;
    }
}
