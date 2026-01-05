package com.sabapp.saba.onboarding;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class signupaccount extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    LinearLayout signupbutton, businesstypeentries;

    AppCompatSpinner setup_type_spinner, currency_spinner, country_spinner;

    String accounttype, countryname, currencyname;

    private ArrayList<String> setuptypeList, countrylist, currencylist;

    EditText firstnameText, lastnameText, phonenumberText, emailaddressText, passwordText;

    EditText addressText, cityText, stateText, postalcodeText, businessnameText, businessdescriptionText;

    ImageView backarrowimagebutton;

    TextView registerbuttontextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.signinprogress);
        app = (sabaapp) getApplicationContext();
        app.logOutPreliminaries();

        progressindicator.setVisibility(View.GONE);

        signupbutton = (LinearLayout) findViewById(R.id.createaccountlayoutbutton);

        businesstypeentries = (LinearLayout)findViewById(R.id.businessoptionalfields);

        businesstypeentries.setVisibility(View.GONE);


        setup_type_spinner = (AppCompatSpinner) findViewById(R.id.businesstypespinner);
        currency_spinner = (AppCompatSpinner) findViewById(R.id.currencytypespinner);
        country_spinner = (AppCompatSpinner) findViewById(R.id.countrytypespinner);

        backarrowimagebutton = (ImageView)findViewById(R.id.backarrowimage);
        registerbuttontextview = (TextView)findViewById(R.id.registerbuttontext);

        //setup mandatory personal edit texts
        firstnameText = (EditText)findViewById(R.id.firstnameEdit);
        lastnameText = (EditText)findViewById(R.id.lastnameEdit);
        phonenumberText = (EditText)findViewById(R.id.phonenumberEdit);
        emailaddressText = (EditText)findViewById(R.id.emailEdit);
        passwordText = (EditText)findViewById(R.id.passwordEdit);

        //setup business edit text
        addressText = (EditText) findViewById(R.id.addressEdit);
        cityText = (EditText)findViewById(R.id.cityEdit);
        stateText = (EditText)findViewById(R.id.stateEdit);
        postalcodeText = (EditText)findViewById(R.id.postalcodeEdit);
        businessnameText = (EditText)findViewById(R.id.businessnameEdit);
        businessdescriptionText = (EditText)findViewById(R.id.businessdescriptionEdit);



        setuptypeList= new ArrayList<String>();
        countrylist = new ArrayList<String>();
        currencylist = new ArrayList<String>();

        //setup for account type

        setuptypeList.add("Personal Account setup");
        setuptypeList.add("Planner Account setup");
        setuptypeList.add("Vendor Account Setup");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(signupaccount.this,
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

                        businesstypeentries.setVisibility(View.GONE);
                        accounttype = "standard";

                    }else{

                        businesstypeentries.setVisibility(View.VISIBLE);

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


        //setup for country spinner

        countrylist.add("USA");
        countrylist.add("UK");
        countrylist.add("Kenya");
        countrylist.add("Nigeria");
        countrylist.add("Uganda");
        countrylist.add("South Africa");
        countrylist.add("Tanzania");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(signupaccount.this,
                R.layout.spinner_item, countrylist);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(adapter2);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Log.e("Country itemSelected", item.toString());
                    countryname=item.toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                countryname="USA";

            }

        });


        //end of setup for country spinner

        //setup for currency spinner

        currencylist.add("USD");
        currencylist.add("GBP");
        currencylist.add("KES");
        currencylist.add("NGN");
        currencylist.add("UGX");
        currencylist.add("ZAR");
        currencylist.add("TZS");


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(signupaccount.this,
                R.layout.spinner_item, currencylist);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency_spinner.setAdapter(adapter3);
        currency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Log.e("Currency itemSelected", item.toString());
                    currencyname=item.toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                currencyname="USD";

            }

        });


        //end of setup for currency spinner

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(firstnameText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "First name cannot be empty", Toast.LENGTH_SHORT).show();

                }else if(lastnameText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Last name cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(phonenumberText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(emailaddressText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "E-Mail cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(passwordText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(!accounttype.equalsIgnoreCase("standard") && businessnameText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Business name cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(!accounttype.equalsIgnoreCase("standard") && addressText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Business address cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(!accounttype.equalsIgnoreCase("standard") && cityText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Business address city cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(!accounttype.equalsIgnoreCase("standard") && postalcodeText.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(signupaccount.this, "Business address postal code cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else
                {


                                progressindicator.setVisibility(View.VISIBLE);
                                /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/


                                String notification_token = retrieve_firebaseToken();


                                volleyGet();




                }


            }
        });


        backarrowimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(signupaccount.this, "Redirecting to login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(signupaccount.this, login.class));


            }
        });

    }


    public void volleyGet(){

       signupbutton.setEnabled(false);
       registerbuttontextview.setText("Processing Registration");
        // request login here


        HashMap<String, String> paramsotpu = new HashMap<String, String>();
        paramsotpu.put("firstname", firstnameText.getText().toString());
        paramsotpu.put("lastname", lastnameText.getText().toString());
        paramsotpu.put("phonenumber", phonenumberText.getText().toString());
        paramsotpu.put("email",  emailaddressText.getText().toString());
        paramsotpu.put("password", passwordText.getText().toString());
        paramsotpu.put("country", countryname);
        paramsotpu.put("currency",currencyname);
        paramsotpu.put("registrationtype", accounttype);

        if(!accounttype.equalsIgnoreCase("standard")){

            paramsotpu.put("businessname",  businessnameText.getText().toString());
            paramsotpu.put("businessdescription", businessdescriptionText.getText().toString());
            paramsotpu.put("address", addressText.getText().toString());
            paramsotpu.put("postal_code", postalcodeText.getText().toString());
            paramsotpu.put("city",  cityText.getText().toString());
            paramsotpu.put("state", stateText.getText().toString());
            paramsotpu.put("adminlevel", "level1");



        }

        //start new request que
        RequestQueue queue = Volley.newRequestQueue(signupaccount.this);

        String registeruserendpoint="https://api.sabaapp.co/v0/registeraccount/createenduser";

        Log.e("SIGNUP EMAIL", emailaddressText.getText().toString());



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, registeruserendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressindicator.setVisibility(View.GONE);
                        /* getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
                        Log.e("response", "response "+response.toString());

                       registerbuttontextview.setText("Continuing Registration..");
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("status");
                                String messagedetails =jsonObj.getString("DETAILS");
                                if(message.toLowerCase().matches("success"))

                                {
                                    registerbuttontextview.setEnabled(true);
                                   registerbuttontextview.setText("Registration Succesfull");
                                    Log.e("Registration Completed", "Registration to account started");

                                    Toast.makeText(signupaccount.this, "Redirecting to login", Toast.LENGTH_SHORT).show();

                                    messagedetails = "Your account has been succesfully setup, we are now redirecting you to the login page";

                                    AlertDialog ad = new AlertDialog.Builder(signupaccount.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            startActivity(new Intent(signupaccount.this, login.class));
                                            //Start loggin to Login page
                                        }
                                    });
                                    ad.show();





                                }else if(message.toLowerCase().matches("failed"))
                                {
                                   signupbutton.setEnabled(true);
                                   registerbuttontextview.setText("Retry Registration");
                                    Log.e("Registration Failed", "Failed with Error :"+messagedetails);

                                    //Toast.makeText(signupsingle.this, "Failed :"+messagedetails, Toast.LENGTH_LONG).show();

                                    /*Toast toast = Toast.makeText(signupsingle.this, "Failed :"+messagedetails, Toast.LENGTH_LONG);
                                    View view = toast.getView();
                                    view.setBackgroundResource(R.drawable.successdraw);
                                    TextView text = (TextView) view.findViewById(android.R.id.message);
                                    text.setTextColor(Color.parseColor("#FFFFFF"));
                                    toast.show();*/
                                    AlertDialog ad = new AlertDialog.Builder(signupaccount.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    ad.show();

                                }
                                else
                                {
                                   signupbutton.setEnabled(true);
                                   registerbuttontextview.setText("Retry Registration");
                                    //Toast.makeText(signupsingle.this, "Failed :"+messagedetails, Toast.LENGTH_LONG).show();
                                    /*Toast toast = Toast.makeText(signupsingle.this, "Failed :"+messagedetails, Toast.LENGTH_LONG);
                                    View view = toast.getView();
                                    view.setBackgroundResource(R.drawable.successdraw);
                                    TextView text = (TextView) view.findViewById(android.R.id.message);
                                    text.setTextColor(Color.parseColor("#FFFFFF"));
                                    toast.show();*/
                                    AlertDialog ad = new AlertDialog.Builder(signupaccount.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
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

               signupbutton.setEnabled(true);
               registerbuttontextview.setText("Retry Registration");

                progressindicator.setVisibility(View.GONE);
                /* getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
                Log.e("error is ", "" + error);
                if (error == null || error.networkResponse == null) {
                    return;
                }

                Log.e("error is ", "" + error);
                final String statusCode = String.valueOf(error.networkResponse.statusCode);

                Log.e("Received Error status code ", "" + statusCode);

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        if (obj.has("message")) {

                            String messagedetails =obj.getString("message");

                            AlertDialog ad = new AlertDialog.Builder(signupaccount.this)
                                    .create();
                            ad.setCancelable(true);
                            ad.setTitle("Registration Failed");
                            ad.setMessage(messagedetails);
                            ad.setButton("Okay", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    startActivity(new Intent(signupaccount.this, login.class));
                                }
                            });
                            ad.show();

                            //Toast.makeText(getApplicationContext(), "Error when updating : "+messagedetails, Toast.LENGTH_SHORT).show();

                            Log.e("PARSED ERROR ", "" + messagedetails);

                        }

                        if(obj.has("status")){

                            String message =obj.getString("status");

                        }



                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();

                        String body;
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                            Log.e("Error response isnt JSON and is ", "" + body);
                        } catch (UnsupportedEncodingException e) {
                            Log.e("encoding is ", "" + e.getMessage());
                            // exception
                        }



                    }
                }

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization","Basic MjU0NTQ3MTMxMjA5MjQ3OjJaZ2dKbUxH");
                return params;
            }


        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }

    public String retrieve_firebaseToken(){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        fcm_token = token;

                        // Log and toast
                        String msg = "FCM TOKEN : " + token;
                        Log.d(TAG, msg);
                        Log.e("FCM TOKEN ", "" + token);
                        //Toast.makeText(signupsingle.this msg, Toast.LENGTH_SHORT).show();
                    }
                });
        return fcm_token;
    }


}
