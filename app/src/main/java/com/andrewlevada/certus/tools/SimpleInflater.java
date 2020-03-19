package com.andrewlevada.certus.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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

    public static void inflateSmooth(OnViewInflated callback, @NonNull ViewGroup parent, @LayoutRes int id, boolean attachToRoot) {
        class InflatingThread extends Thread {
            private Handler handler;
            private ViewGroup parent;
            private int id;

            @Override
            public void run() {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(id, parent, false);

                Message message = new Message();
                message.obj = view;
                handler.sendMessage(message);
            }

            public InflatingThread(Handler handler, ViewGroup parent, int id) {
                this.handler = handler;
                this.parent = parent;
                this.id = id;
            }
        }

        class InflatedHandler extends Handler {
            private OnViewInflated callback;
            private ViewGroup parent;
            private boolean attachToRoot;

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                ViewGroup view = (ViewGroup) msg.obj;
                if (attachToRoot) parent.addView(view);

                callback.inflated(view);
            }

            public InflatedHandler(OnViewInflated callback, ViewGroup parent, boolean attachToRoot) {
                this.callback = callback;
                this.parent = parent;
                this.attachToRoot = attachToRoot;
            }
        }

        Thread thread = new InflatingThread(new InflatedHandler(callback, parent, attachToRoot), parent, id);
        thread.start();
    }

    public static void inflateSmooth(OnViewInflated callback, @NonNull ViewGroup parent, @LayoutRes int id) {
        inflateSmooth(callback, parent, id, true);
    }

    public interface OnViewInflated {
        void inflated(View view);
    }
}
