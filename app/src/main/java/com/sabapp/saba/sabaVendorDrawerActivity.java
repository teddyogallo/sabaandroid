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

public class sabaVendorDrawerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sabavendordashboardactivity);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment firstFragment = new vendorFragment();
        Fragment secondFragment = new VendorCatalogueFragment();
        Fragment alleventsFragment = new alleventsVendorFragment();
        Fragment thirdFragment = new SecondFragment();
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
                } else if (id == R.id.catalogue) {
                    setCurrentFragment(secondFragment);
                } else if (id == R.id.allevents) {
                    setCurrentFragment(alleventsFragment);
                } else if (id == R.id.payments) {
                    setCurrentFragment(thirdFragment);
                } else if (id == R.id.businesschatbot) {
                    setCurrentFragment(messageFragment);
                }
                return true;
            }
        });


    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }


}
