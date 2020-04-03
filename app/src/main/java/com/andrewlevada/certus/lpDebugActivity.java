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

import java.util.HashMap;
import java.util.Map;

public class lpDebugActivity extends AppCompatActivity {

    static int c1 = 0;
    static int c2 = 4;

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
            Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.teacher_button_1).setOnClickListener(v -> {
            ref.child("document").child("0").child("v").child("3").setValue("тема " + c1);
            c1++;
        });

        findViewById(R.id.teacher_button_2).setOnClickListener(v -> {
            Map<String, Object> content = new HashMap<>();
            content.put("t", "text");
            content.put("v", "Новый текст... " + c2);

            Map<String, Object> map = new HashMap<>();
            map.put(String.valueOf(c2), content);

            ref.child("document").child("1").child("v").updateChildren(map);
            c2++;
        });

        findViewById(R.id.teacher_button_3).setOnClickListener(v -> {
            c1 = 0;
            c2 = 4;

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
