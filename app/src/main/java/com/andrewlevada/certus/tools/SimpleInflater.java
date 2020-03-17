package com.andrewlevada.certus.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * Simple wrapper class for {@link LayoutInflater}.
 * Useful for multiple inflations from same parent.
 * For single inflation use static implementation.
 */
public class SimpleInflater {
    private Context context;
    private ViewGroup parent;

    public SimpleInflater(Context context, ViewGroup parent) {
        this.context = context;
        this.parent = parent;
    }

    public SimpleInflater(Activity activity, View parent) {
        this(activity.getApplicationContext(), (ViewGroup) parent);
    }

    public SimpleInflater(ViewGroup parent) {
        this(parent.getContext(), parent);
    }

    @SuppressLint("ResourceType")
    public View inflate(@LayoutRes int id, boolean attachToRoot) {
        View view = LayoutInflater.from(context)
                .inflate(id, parent, attachToRoot);

        if (attachToRoot) return Toolbox.getLastChild(parent);
        else return view;
    }

    @SuppressLint("ResourceType")
    public View inflate(@LayoutRes int id) {
        return this.inflate(id, true);
    }

    public static View inflate(@NonNull ViewGroup parent, @LayoutRes int id, boolean attachToRoot) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(id, parent, attachToRoot);

        if (attachToRoot) return Toolbox.getLastChild(parent);
        else return view;
    }

    public static View inflate(@NonNull ViewGroup parent, @LayoutRes int id) {
        return SimpleInflater.inflate(parent, id, true);
    }
}
