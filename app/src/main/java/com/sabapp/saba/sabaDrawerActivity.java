package com.sabapp.saba;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

        setCurrentFragment(firstFragment);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.dashboard) {
                    setCurrentFragment(firstFragment);
                } else if (id == R.id.events) {
                    setCurrentFragment(secondFragment);
                } else if (id == R.id.requestpay) {
                    setCurrentFragment(thirdFragment);
                } else if (id == R.id.messages) {
                    setCurrentFragment(thirdFragment);
                } else if (id == R.id.businesschatbot) {
                    setCurrentFragment(thirdFragment);
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
