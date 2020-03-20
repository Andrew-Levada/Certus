package com.andrewlevada.certus;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long delay = 5000;

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
        Intent intent = new Intent(getApplicationContext(),
                HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
