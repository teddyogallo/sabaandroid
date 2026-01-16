package com.sabapp.saba.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.sabapp.saba.R;
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.sabaDrawerActivity;
import com.sabapp.saba.sabaVendorDrawerActivity;
import com.sabapp.saba.sabaplannerDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class loginoptionchoose extends AppCompatActivity {


    sabaapp app;



    private ArrayList<String> setuptypeList;

    String accounttype;

    String businessnamevalue = null;


    SharedPrefsXtreme sharedPrefsXtreme;


    LinearLayout chooseemaillogin,choosegooglelogin,choosefacebooklogin;

    @Override
    public void onBackPressed(){

        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), onboardone.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginoptionchoose);

        app = (sabaapp) getApplicationContext();


        sharedPrefsXtreme = SharedPrefsXtreme.getInstance(getApplicationContext());

        businessnamevalue = sharedPrefsXtreme.getData("business_name");
        String businessdescription = sharedPrefsXtreme.getData("business_description");

        Log.d("BUSINESS NAME",businessnamevalue );


        chooseemaillogin =   (LinearLayout) findViewById(R.id.loginlayoutbutton);

        choosegooglelogin =   (LinearLayout) findViewById(R.id.loginwithgooglelayout);

        choosefacebooklogin =   (LinearLayout) findViewById(R.id.loginwithfacebooklayoutbutton);


        chooseemaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),
                        login.class));


            }
        });


        choosegooglelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*if(businessnamevalue == null || businessnamevalue.isEmpty() || businessnamevalue.equalsIgnoreCase("null")){

                    startActivity(new Intent(getApplicationContext(),
                            chooselogintype.class));


                }else{




                }*/

            }
        });


        choosefacebooklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*if(businessnamevalue == null || businessnamevalue.isEmpty() || businessnamevalue.equalsIgnoreCase("null")){




                }else{




                }*/

            }
        });







    }
}
