package com.andrewlevada.certus;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andrewlevada.certus.logic.livepapers.LivePaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class StudentLessonActivity extends AppCompatActivity {
    private LivePaper livePaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_student);

        livePaper = new LivePaper(getApplicationContext());

        // Find views by ids
        final ViewGroup lpView = findViewById(R.id.live_paper);

        // Setup database connection
        String sessionName = "testing";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sessions/" + sessionName);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> root;
                List<Map<String, Object>> document;

                try {
                    root = (Map<String, Object>) dataSnapshot.getValue();
                    if (root == null) return;
                    document = (List<Map<String, Object>>) root.get("document");
                    if (document == null) return;
                } catch (Exception ex) {
                    return;
                }

                // TODO: Process session options

                int i = 0;
                for (Map<String, Object> node : document) {
                    livePaper.process(lpView, node, i);
                    i++;
                }

                for (int j = lpView.getChildCount() - 1; j >= document.size(); j--) {
                    lpView.removeViewAt(j);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FB_E", "onCancelled");
            }
        });
    }
}
