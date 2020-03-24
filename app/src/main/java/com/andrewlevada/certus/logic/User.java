package com.andrewlevada.certus.logic;

import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private boolean isAuthed;
    private FirebaseUser firebaseUser;

    private static User instance;

    public boolean isAuthed() {
        return isAuthed;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void auth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (isAuthed || user == null) return;

        firebaseUser = user;
        isAuthed = true;
    }

    public void signOut(Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // TODO: Give some feedback
                    }
                });

        firebaseUser = null;
        isAuthed = false;
    }

    private User() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            firebaseUser = user;
            isAuthed = true;
        }
    }

    @NonNull
    public static User getInstance() {
        if (instance == null) instance = new User();
        return instance;
    }
}
