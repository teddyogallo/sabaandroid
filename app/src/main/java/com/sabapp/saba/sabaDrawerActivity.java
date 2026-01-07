package com.sabapp.saba;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sabapp.saba.R;

public class sabaDrawerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabadashboardactivity);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment firstFragment = new homeclientFragment();
        Fragment secondFragment = new SecondFragment();
        Fragment thirdFragment = new FirstFragment();
        Fragment messageFragment = new messageFragment();

        setCurrentFragment(firstFragment);

        MaterialToolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide default title

        View customToolbar = LayoutInflater.from(this).inflate(R.layout.customtopactionbar, toolbar, false);
        toolbar.addView(customToolbar);

        ImageView userIcon = customToolbar.findViewById(R.id.user_icon);
        ImageView notificationIcon = customToolbar.findViewById(R.id.notification_icon);

        userIcon.setOnClickListener(v -> {
            Toast.makeText(this, "User clicked", Toast.LENGTH_SHORT).show();
        });

        notificationIcon.setOnClickListener(v -> {
            Toast.makeText(this, "Notification clicked", Toast.LENGTH_SHORT).show();
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.dashboard) {
                    setCurrentFragment(firstFragment);
                } else if (id == R.id.events) {
                    setCurrentFragment(thirdFragment);
                } else if (id == R.id.requestpay) {
                    setCurrentFragment(thirdFragment);
                } else if (id == R.id.messages) {
                    setCurrentFragment(messageFragment);
                } else if (id == R.id.businesschatbot) {
                    setCurrentFragment(secondFragment);
                }
                return true;
            }
        });

        /*bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dashboard:
                    setCurrentFragment(firstFragment);
                    break;
                case R.id.events:
                    setCurrentFragment(secondFragment);
                    break;
                case R.id.requestpay:
                    setCurrentFragment(thirdFragment);
                    break;
                case R.id.messages:
                    setCurrentFragment(thirdFragment);
                    break;

                case R.id.businesschatbot:
                    setCurrentFragment(thirdFragment);
                    break;
            }
            return true;
        });*/
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }

}
