package com.andrewlevada.certus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class lpDebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lp_debug);

        // Setup database connection
        String sessionName = "testing";
        String exampleName = "-M3ytZT3sNoWBbM9Azng";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sessions/" + sessionName);

        findViewById(R.id.student_button).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StudentLessonActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.teacher_button_1).setOnClickListener(v -> {
            // Nothing -_-
        });

        findViewById(R.id.teacher_button_2).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TeacherLessonActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.teacher_button_3).setOnClickListener(v -> {
            DatabaseReference fullRef = database.getReference("sessions");
            fullRef.child(exampleName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    fullRef.child(sessionName).setValue(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }
}
