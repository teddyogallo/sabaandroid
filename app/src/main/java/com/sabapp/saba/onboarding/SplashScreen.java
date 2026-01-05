package com.sabapp.saba.onboarding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.R;
import android.os.Handler;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


@SuppressLint("CustomSplashScreen")
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    String userprefGlob="";
    sabaapp app;


    public static final String MyPREFERENCES = "ww_prefs" ;
    public static final String Username = "usernamepref";

    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app = (sabaapp)getApplicationContext();





        SharedPreferences  mySharedPreferences ;
        mySharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Retrieve the saved values.

        String usernamepref = mySharedPreferences.getString("usernamepref", "");


        String showonboarding = app.getShowonboard();;



        userprefGlob=usernamepref;


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

            	/*
                Intent id = new Intent(SplashScreen.this, first_onboard.class);
                startActivity(id);

                // close this activity
                finish();

                */



                /*if(userprefGlob.length() == 0)
                {
                    Intent i = new Intent(SplashScreen.this, first_onboard.class);
                    //i.putExtra("useridsaved", userprefGlob);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);
                }else */

                String showonboarding = app.getShowonboard();
                if(showonboarding.equals("YES")){

                    Intent intent = new Intent(SplashScreen.this,login.class);
                    // intent.putExtra("useridsaved", userprefGlob);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);

                }else
                {
                    Intent intent = new Intent(SplashScreen.this,onboardone.class);
                    // intent.putExtra("useridsaved", userprefGlob);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }




            }
        }, SPLASH_TIME_OUT);
    }

}
