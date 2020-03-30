package com.andrewlevada.certus;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class lpDebugActivity extends AppCompatActivity {

    static int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lp_debug);

        // Setup database connection
        String sessionName = "testing";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sessions/" + sessionName);

        findViewById(R.id.student_button).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.teacher_button_1).setOnClickListener(v -> {
            ref.child("document").child("0").child("v").child("3").setValue("тема " + counter);
            counter++;
        });

        findViewById(R.id.teacher_button_2).setOnClickListener(v -> {
            ref.child("document").child("1").child("v").child("2").child("v").child("0").child("v").setValue("А вот и нет!");
        });

        findViewById(R.id.teacher_button_3).setOnClickListener(v -> {
            counter = 0;
            ref.child("document").child("0").child("v").child("3").setValue("тема");
            ref.child("document").child("1").child("v").child("2").child("v").child("0").child("v").setValue("Хорошее настроение - правило");
        });
    }
}
