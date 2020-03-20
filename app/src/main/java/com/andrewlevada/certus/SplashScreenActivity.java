package com.andrewlevada.certus;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long delay = 2400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView imageView = (ImageView) findViewById(R.id.splash_icon);
        AnimatedVectorDrawable vector = (AnimatedVectorDrawable) imageView.getDrawable();
        vector.start();

        Handler loadHandler = new Handler();
        loadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadHomeActivity();
            }
        }, delay);
    }

    private void loadHomeActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
