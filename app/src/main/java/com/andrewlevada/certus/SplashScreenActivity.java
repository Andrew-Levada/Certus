package com.andrewlevada.certus;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long delay = 0;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        intent = new Intent(getApplicationContext(), LessonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final ImageView imageView = (ImageView) findViewById(R.id.splash_icon);
        AnimatedVectorDrawable vector = (AnimatedVectorDrawable) imageView.getDrawable();
        vector.start();

        Handler loadHandler = new Handler();
        loadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startHomeActivity();
            }
        }, delay);
    }

    private void startHomeActivity() {
        startActivity(intent);
        finish();
    }
}
