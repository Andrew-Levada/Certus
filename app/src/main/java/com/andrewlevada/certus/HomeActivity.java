package com.andrewlevada.certus;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.andrewlevada.certus.logic.User;
import com.andrewlevada.certus.tools.SimpleInflater;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

/**
 * Main ond first activity which appears to user.
 * Contains {@link LearnFragment}, {@link TeachFragment} and
 * {@link AccountFragment} tabs. Implements backdrop.
 */
public class HomeActivity extends AppCompatActivity {
    private int currentHomeFragmentId;

    private Point display;

    private ConstraintLayout layout;
    private ViewGroup backdrop;

    private ConstraintSet defaultConstraint;
    private ConstraintSet backdropConstraint;

    private FloatingActionButton fabView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get screen size
        display = new Point();
        getWindowManager().getDefaultDisplay().getSize(display);

        // Find views by ids
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        layout = (ConstraintLayout) findViewById(R.id.home_layout);
        fabView = (FloatingActionButton) findViewById(R.id.home_fab);

        // Setup backdrop
        backdrop = (ViewGroup) findViewById(R.id.backdrop);
        backdrop.getLayoutParams().height = display.y;

        // Setup ConstraintSets for backdrop animations
        defaultConstraint = new ConstraintSet();
        defaultConstraint.clone(layout);
        backdropConstraint = new ConstraintSet();
        backdropConstraint.load(getApplicationContext(), R.layout.activity_home_backdrop);

        // Setup backdrop toolbar
        MaterialToolbar backdropToolbar = findViewById(R.id.backdrop_toolbar);
        backdropToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBackdrop(false);
            }
        });

        // Loading default learn fragment screen
        loadHomeFragment(new LearnFragment(this), R.id.navigation_button_learn);

        // Setup user
        user = User.getInstance();

        // Process bottom navigation buttons clicks
        final HomeActivity itself = this;
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if (currentHomeFragmentId == item.getItemId()) return false;

                switch (item.getItemId()) {
                    case R.id.navigation_button_learn:
                        fragment = new LearnFragment(itself);
                        break;

                    case R.id.navigation_button_teach:
                        fragment = new TeachFragment(itself);
                        break;

                    case R.id.navigation_button_account:
                        fragment = new AccountFragment();
                        break;
                }

                return loadHomeFragment(fragment, item.getItemId());
            }
        });
    }

    private boolean loadHomeFragment(Fragment fragment, int id) {
        if (fragment != null) {
            // Remember switching fragment
            currentHomeFragmentId = id;

            // Hide fab. If fragment needs it, it can request it
            fabView.hide();

            // Make transition between fragments
            FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
            transition.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
            transition.replace(R.id.home_fragment_container, fragment);
            transition.commit();

            return true;
        }
        return false;
    }

    public void updateBackdrop(boolean extend) {
        ConstraintSet constraintSet;

        // Load needed layout
        if (extend) constraintSet = backdropConstraint;
        else constraintSet = defaultConstraint;

        // Setup transition
        Transition transition = new AutoTransition();
        transition.setDuration(600);
        if (extend) transition.setInterpolator(new FastOutSlowInInterpolator());
        else transition.setInterpolator(new FastOutLinearInInterpolator());

        // Make transition
        TransitionManager.beginDelayedTransition(layout, transition);
        constraintSet.applyTo(layout);
    }

    public void fillBackdrop(@LayoutRes int layout, SimpleInflater.OnViewInflated callback) {
        ViewGroup backdrop = findViewById(R.id.backdrop);
        backdrop.removeAllViews();
        SimpleInflater.inflateSmooth(callback, (ViewGroup) findViewById(R.id.backdrop), layout);
    }

    public void requestFAB(@Nullable View.OnClickListener onClickListener) {
        fabView.show();
        fabView.setOnClickListener(onClickListener);
    }

    public void requestAuth() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // TODO: Add auth by email and fb

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user.auth();
            } else {
                // TODO: Error process
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
            }
        }
    }
}
