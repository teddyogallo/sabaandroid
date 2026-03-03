package com.sabapp.saba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.onboarding.loginoptionchoose;

public class sabaDrawerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    sabaapp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabadashboardactivity);
        app = (sabaapp) getApplicationContext();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment firstFragment = new homeclientFragment();
        Fragment secondFragment = new SecondFragment();
        Fragment vendorFragment = new vendorlistFragment();
        Fragment eventlistFragment = new eventslistFragment();
        Fragment messageFragment = new messageFragment();

        String fragmentToOpen = getIntent().getStringExtra("open_fragment");

        if ("messages".equalsIgnoreCase(fragmentToOpen)) {
            setCurrentFragment(messageFragment);
            bottomNavigationView.setSelectedItemId(R.id.messages);
        }else if ("payment".equalsIgnoreCase(fragmentToOpen)) {
            setCurrentFragment(secondFragment);
            bottomNavigationView.setSelectedItemId(R.id.businesschatbot);
        }else {
            setCurrentFragment(firstFragment); // default
        }

        /*MaterialToolbar toolbar = findViewById(R.id.main_toolbar);
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
        });*/


        MaterialToolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide default title

// Set toolbar background
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_primary));

// Inflate custom layout into toolbar
        View customToolbar = LayoutInflater.from(this).inflate(R.layout.customtopactionbarmaterial, toolbar, false);
        toolbar.addView(customToolbar);

// Find icons
        ImageView userIcon = customToolbar.findViewById(R.id.user_icon);
        ImageView notificationIcon = customToolbar.findViewById(R.id.notification_icon);

// Click listeners
        userIcon.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();

                if (id == R.id.dashboard_menu_settings) {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (id == R.id.dashboard_menu_logout) {
                    Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
                    app.logOutPreliminaries(); // clear session, prefs, tokens, etc.

                    Intent intent = new Intent(this, loginoptionchoose.class);
                    intent.putExtra("createadrad", "gotowhatsappbotmaker");

                    // CLEAR entire activity stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    finish(); // optional but safe

                    return true;

                }

                return false;

            });

            popupMenu.show();
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
                    setCurrentFragment(vendorFragment);
                } else if (id == R.id.requestpay) {
                    setCurrentFragment(eventlistFragment);
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
