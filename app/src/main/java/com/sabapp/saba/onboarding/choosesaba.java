package com.sabapp.saba.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class choosesaba extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    private ArrayList<String> setuptypeList;

    String accounttype;

    String businessnamevalue = null;
    LinearLayout setupupbutton, hiddenlayer;

    EditText addressText, cityText, stateText, postalcodeText, businessnameText, businessdescriptionText;

    SharedPrefsXtreme sharedPrefsXtreme;


    RelativeLayout chooseclient,choosevendor,chooseplanner;

    @Override
    public void onBackPressed(){

        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), loginoptionchoose.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosesaba);

        app = (sabaapp) getApplicationContext();


        sharedPrefsXtreme = SharedPrefsXtreme.getInstance(getApplicationContext());

        businessnamevalue = sharedPrefsXtreme.getData("business_name");
        String businessdescription = sharedPrefsXtreme.getData("business_description");

        Log.d("BUSINESS NAME",businessnamevalue );


        chooseclient =   (RelativeLayout) findViewById(R.id.clientselect);

        chooseplanner =   (RelativeLayout) findViewById(R.id.plannerselect);

        choosevendor =   (RelativeLayout) findViewById(R.id.vendorselect);


        chooseclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accounttype = "standard";

                sharedPrefsXtreme.saveData("loginaccounttype", accounttype);

                startActivity(new Intent(getApplicationContext(),
                        sabaDrawerActivity.class));


            }
        });


        chooseplanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accounttype = "planner";
                sharedPrefsXtreme.saveData("loginaccounttype", accounttype);

                if(businessnamevalue == null || businessnamevalue.isEmpty() || businessnamevalue.equalsIgnoreCase("null")){

                    startActivity(new Intent(getApplicationContext(),
                            chooselogintype.class));


                }else{

                    startActivity(new Intent(getApplicationContext(),
                            sabaplannerDrawerActivity.class));


                }

            }
        });


        choosevendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accounttype = "vendor";
                sharedPrefsXtreme.saveData("loginaccounttype", accounttype);

                if(businessnamevalue == null || businessnamevalue.isEmpty() || businessnamevalue.equalsIgnoreCase("null")){

                    startActivity(new Intent(getApplicationContext(),
                            chooselogintype.class));


                }else{

                    startActivity(new Intent(getApplicationContext(),
                            sabaVendorDrawerActivity.class));


                }

            }
        });







    }
}
