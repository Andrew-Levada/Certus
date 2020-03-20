package com.andrewlevada.certus.logic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private boolean isAuthed;
    private FirebaseUser firebaseUser;

    private static User instance;

    public boolean isAuthed() {
        return isAuthed;
    }

    public void auth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (isAuthed || user == null) return;

        firebaseUser = user;
        isAuthed = true;
    }

    public void signOut() {
        firebaseUser.delete(); // TODO: Process exceptions
        firebaseUser = null;
        isAuthed = false;
    }

    private User() {
        // TODO: Load user data from db or fb
    }

    @NonNull
    public static User getInstance() {
        if (instance == null) instance = new User();
        return instance;
    }
}
