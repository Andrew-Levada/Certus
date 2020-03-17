package com.andrewlevada.certus.tools;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class Toolbox {
    @Nullable
    public static View getLastChild(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);

            if (child != null) return child;
        }
        return null;
    }

    @Nullable
    public static View getLastChild(View parent) {
        if (!(parent instanceof ViewGroup)) return null;
        return getLastChild((ViewGroup) parent);
    }
}
