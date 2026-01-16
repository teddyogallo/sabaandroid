package com.sabapp.saba.onboarding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sabapp.saba.R;
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.sabaDrawerActivity;
import com.sabapp.saba.sabaVendorDrawerActivity;
import com.sabapp.saba.sabaplannerDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chooselogintype extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    private ArrayList<String> setuptypeList;

    String accounttype;

    String businessnamevalue = null;
    LinearLayout setupupbutton, hiddenlayer, chooseaccounttypelayout;

    EditText addressText, cityText, stateText, postalcodeText, businessnameText, businessdescriptionText;

    SharedPrefsXtreme sharedPrefsXtreme;


    public void showProgressBar()
    {

        progressindicator.smoothToShow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideProgressBar()
    {

        progressindicator.smoothToHide();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onBackPressed(){

        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), loginoptionchoose.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooselogintype);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.signinprogress);
        app = (sabaapp) getApplicationContext();


        sharedPrefsXtreme = SharedPrefsXtreme.getInstance(getApplicationContext());

        businessnamevalue = sharedPrefsXtreme.getData("business_name");
        String businessdescription = sharedPrefsXtreme.getData("business_description");

        Log.d("BUSINESS NAME",businessnamevalue );



        progressindicator.setVisibility(View.GONE);


        setupupbutton = (LinearLayout) findViewById(R.id.loginaccountlayoutbutton);
        hiddenlayer = (LinearLayout) findViewById(R.id.businessoptionalfields);
        chooseaccounttypelayout = (LinearLayout)findViewById(R.id.chooseaccounttypelayout);

        hiddenlayer.setVisibility(View.GONE);

        setup_type_spinner = (AppCompatSpinner) findViewById(R.id.businesstypespinner);

        //setup business edit text
        addressText = (EditText) findViewById(R.id.addressEdit);
        cityText = (EditText)findViewById(R.id.cityEdit);
        stateText = (EditText)findViewById(R.id.stateEdit);
        postalcodeText = (EditText)findViewById(R.id.postalcodeEdit);
        businessnameText = (EditText)findViewById(R.id.businessnameEdit);
        businessdescriptionText = (EditText)findViewById(R.id.businessdescriptionEdit);




        setuptypeList= new ArrayList<String>();
        accounttype = app.getLoginAccounttype();


        if(accounttype!=null && !accounttype.equalsIgnoreCase("standard") && (businessnamevalue == null || businessnamevalue.isEmpty() || businessnamevalue.equalsIgnoreCase("null"))){

            hiddenlayer.setVisibility(View.VISIBLE);

        }

        setup_type_spinner.setVisibility(View.GONE);
        chooseaccounttypelayout.setVisibility(View.GONE);

        //setup for account type

        setuptypeList.add("Personal Account setup");
        setuptypeList.add("Planner Account setup");
        setuptypeList.add("Vendor Account Setup");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(chooselogintype.this,
                R.layout.spinner_item, setuptypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setup_type_spinner.setAdapter(adapter);
        setup_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Log.e("itemSelected", item.toString());
                    String setupaccountype=item.toString();


                    if(setupaccountype.equalsIgnoreCase("personal account setup")){

                        hiddenlayer.setVisibility(View.GONE);
                        accounttype = "standard";


                    }else {

                        if(businessnamevalue == null || businessnamevalue.isEmpty()|| businessnamevalue.equalsIgnoreCase("null")){

                            hiddenlayer.setVisibility(View.VISIBLE);

                        }




                        if(setupaccountype.equalsIgnoreCase("planner account setup")){


                            accounttype = "planner";

                        }else{

                            accounttype = "vendor";
                        }



                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        setupupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPrefsXtreme.saveData("loginaccounttype", accounttype);
                 if(accounttype.equalsIgnoreCase("standard")){



                     startActivity(new Intent(getApplicationContext(),
                             sabaDrawerActivity.class));

                 }
                 else if(businessnamevalue == null || businessnamevalue.isEmpty() || businessnamevalue.equalsIgnoreCase("null")){
                     //continue here if account has not been setup


                     if(!accounttype.equalsIgnoreCase("standard") && businessnameText.getText().toString().matches(""))
                     {
                         //bizdesclay.setError("Business Description cannot be empty");

                         Toast.makeText(chooselogintype.this, "Business name cannot be empty", Toast.LENGTH_SHORT).show();

                     }
                     else if(!accounttype.equalsIgnoreCase("standard") && addressText.getText().toString().matches(""))
                     {
                         //bizdesclay.setError("Business Description cannot be empty");

                         Toast.makeText(chooselogintype.this, "Business address cannot be empty", Toast.LENGTH_SHORT).show();

                     }
                     else if(!accounttype.equalsIgnoreCase("standard") && cityText.getText().toString().matches(""))
                     {
                         //bizdesclay.setError("Business Description cannot be empty");

                         Toast.makeText(chooselogintype.this, "Business address city cannot be empty", Toast.LENGTH_SHORT).show();

                     }
                     else if(!accounttype.equalsIgnoreCase("standard") && postalcodeText.getText().toString().matches(""))
                     {
                         //bizdesclay.setError("Business Description cannot be empty");

                         Toast.makeText(chooselogintype.this, "Business address postal code cannot be empty", Toast.LENGTH_SHORT).show();

                     }
                     else
                     {


                         progressindicator.setVisibility(View.VISIBLE);
                                /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/


                         volleyGet();

                     }



                  //end of continue if account has not been setup
                 }
                 else if(accounttype.equalsIgnoreCase("planner")){
                     //go  to planner dashboard

                     startActivity(new Intent(getApplicationContext(),
                             sabaplannerDrawerActivity.class));



                     //end of go to planner dashboard

                }
                 else{
                     //go to vendor dashboard

                     startActivity(new Intent(getApplicationContext(),
                             sabaVendorDrawerActivity.class));

                  //end of go to vendor dashboard
                 }

            }
        });


    //end of oncreate
    }


    public void volleyGet()
    {
        setupupbutton.setEnabled(false);

        showProgressBar();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();
        paramsotpu.put("businessname", businessnameText.getText().toString());
        paramsotpu.put("business_description", businessdescriptionText.getText().toString());
        paramsotpu.put("business_address",  addressText.getText().toString());

        paramsotpu.put("city", cityText.getText().toString());
        paramsotpu.put("state", stateText.getText().toString());
        paramsotpu.put("zipcode",  postalcodeText.getText().toString());


        String loginuserendpoint="https://api.sabaapp.co/v0/account/updateuser";

        Log.e("Request Payload", String.valueOf(new JSONObject(paramsotpu)));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginuserendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Add response", "Response : "+response.toString());
                        hideProgressBar();


                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("status");
                                String messagedetails =jsonObj.getString("MESSAGE");
                                if(message.toLowerCase().matches("success"))
                                {
                                    setupupbutton.setEnabled(true);

                                    if(accounttype.equalsIgnoreCase("standard")){

                                        startActivity(new Intent(getApplicationContext(),
                                                sabaDrawerActivity.class));

                                    }else if(accounttype.equalsIgnoreCase("planner")){
                                        //go to planner dashboard

                                        startActivity(new Intent(getApplicationContext(),
                                                sabaplannerDrawerActivity.class));

                                    }else{
                                        //go to vendor dashboard
                                        startActivity(new Intent(getApplicationContext(),
                                                sabaVendorDrawerActivity.class));



                                    }

                                }
                                else
                                {
                                    setupupbutton.setEnabled(true);

                                    Toast.makeText(getApplicationContext(), messagedetails, Toast.LENGTH_LONG).show();

                                    AlertDialog ad = new AlertDialog.Builder(chooselogintype.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            startActivity(new Intent(getApplicationContext(),
                                                    sabaDrawerActivity.class));

                                        }
                                    });
                                    ad.show();



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }


                        } else {

                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                setupupbutton.setEnabled(true);

                hideProgressBar();

                Toast.makeText(getApplicationContext(), "Connection Error :Fix your connection & try again", Toast.LENGTH_LONG).show();

                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //Log.e("error st_code ", "" + statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //Log.e("encoding is ", "" + e.getMessage());
                    // exception
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String apiusername = app.getApiusername();
                String apipassword = app.getApipassword();

                String creds = String.format("%s:%s",apiusername,apipassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);



    }



}
