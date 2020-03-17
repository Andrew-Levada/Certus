package com.andrewlevada.certus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private int currentHomeFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loading default learn fragment screen
        loadHomeFragment(LearnFragment.newInstance(), R.id.navigation_button_learn);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);

        // Process bottom navigation buttons clicks
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if (currentHomeFragmentId == item.getItemId()) return false;

                switch (item.getItemId()) {
                    case R.id.navigation_button_learn:
                        fragment = LearnFragment.newInstance();
                        break;

                    case R.id.navigation_button_teach:
                        fragment = TeachFragment.newInstance();
                        break;

                    case R.id.navigation_button_account:
                        fragment = AccountFragment.newInstance();
                        break;
                }

                return loadHomeFragment(fragment, item.getItemId());
            }
        });
    }

    private boolean loadHomeFragment(Fragment fragment, int id) {
        if (fragment != null) {
            currentHomeFragmentId = id;

            FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
            transition.setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_close_exit);
            transition.replace(R.id.home_fragment_container, fragment);
            transition.commit();
            return true;
        }
        return false;
    }
}
