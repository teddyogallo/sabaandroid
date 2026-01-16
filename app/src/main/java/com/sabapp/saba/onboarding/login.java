package com.sabapp.saba.onboarding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sabapp.saba.MyFirebaseMessagingService;
import com.sabapp.saba.R;
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import android.util.Base64;


public class login extends AppCompatActivity {


    LinearLayout loginbutton;
    EditText usernameEditText, passwordEditText;

    TextView signuptextviewButton, loginbuttontext;
    ImageView revealpasswordImageButton;

    String usernameString, passwordString;

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    SharedPrefsXtreme sharedPrefsXtreme;

    String logged_api_username, loggedapi_password, loggin_country, loggin_currency, loginkeytime, bearerToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.signinprogress);
        app = (sabaapp)getApplicationContext();
        app.logOutPreliminaries();

        progressindicator.setVisibility(View.GONE);

        loginbutton  = (LinearLayout) findViewById(R.id.loginlayoutbutton);


        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);


        signuptextviewButton = (TextView) findViewById(R.id.signuptextview);
        loginbuttontext = (TextView)findViewById(R.id.loginbuttontext);

        revealpasswordImageButton = (ImageView) findViewById(R.id.revealpasswordimageviewbutton);

        sharedPrefsXtreme =
                SharedPrefsXtreme.getInstance(login.this);

        loginbutton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        passwordString = passwordEditText.getText().toString();
                        usernameString = usernameEditText.getText().toString();



                        if(usernameString .matches(""))
                        {
                            Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();


                        }else if(passwordString.matches("")) {

                            Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();


                        } else
                        {



                            //String notification_token = retrieve_firebaseToken();

                            /*fcm_token = MyFirebaseMessagingService.getToken(login.this);
                            Log.e("New FCM Token", " "+ fcm_token);*/

                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(task -> {
                                        if (!task.isSuccessful()) {
                                            Log.e("FCM", "Fetching FCM token failed", task.getException());
                                            return;
                                        }

                                        fcm_token = task.getResult();
                                        Log.e("New FCM Token", fcm_token);

                                        sharedPrefsXtreme.saveData("fcm_token", fcm_token);

                                        // Save it
                                        getSharedPreferences("_", MODE_PRIVATE)
                                                .edit()
                                                .putString("fb", fcm_token)
                                                .apply();

                                        // Send to backend here

                                        progressindicator.setVisibility(View.VISIBLE);
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        volleyGet();


                                    });




                        }



                    }
                }

        );




        signuptextviewButton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //signup button on press



                        Intent intent =  new Intent(getApplicationContext(), signupaccount.class);
                        startActivity(intent);



                    }
                }

        );

        revealpasswordImageButton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    //function for revealing password





                    }
                }

        );





    }


    public void volleyGet() {
        loginbutton.setEnabled(false);
        loginbuttontext.setText("Processing Login");

        HashMap<String, String> paramsotpu = new HashMap<>();
        paramsotpu.put("email", usernameString);
        paramsotpu.put("password", passwordString);
        paramsotpu.put("request_domain", null);
        paramsotpu.put("request_state", null);
        paramsotpu.put("platform", "android");
        paramsotpu.put("version", app.getDevversion());
        paramsotpu.put("telco", null);
        paramsotpu.put("devicemanufacturer", null);
        paramsotpu.put("devicemodel", null);
        paramsotpu.put("sim_serial_no", null);
        paramsotpu.put("sim_number", null);
        paramsotpu.put("notification_token", fcm_token);

        String loginuserendpoint = "https://api.sabaapp.co/v0/account/loginuser";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                loginuserendpoint,
                new JSONObject(paramsotpu),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressindicator.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        try {
                            String message = response.getString("status");
                            String messagedetails = response.getString("DETAILS");

                            if ("success".equalsIgnoreCase(message)) {
                                // JSON fields
                                logged_api_username = response.getString("API_USERNAME");
                                loginkeytime = response.getString("KEY_TIME");
                                loggin_country = response.optString("COUNTRY", "USA");
                                loggin_currency = response.optString("CURRENCY", "USD");
                                String loginkey = response.getString("KEY");
                                String businessname = response.getString("BUSINESS_NAME");
                                String businessdescription = response.getString("BUSINESS_DESCRIPTION");

                                /*sharedPrefsXtreme =
                                        SharedPrefsXtreme.getInstance(login.this);*/

                                sharedPrefsXtreme.saveData("api_username", logged_api_username);
                                sharedPrefsXtreme.saveData("api_password", loggedapi_password); // <-- FROM HEADER
                                sharedPrefsXtreme.saveData("bearer_token", bearerToken); // <-- Bearer token from header
                                sharedPrefsXtreme.saveData("login_token", loginkey);
                                sharedPrefsXtreme.saveData("loginuser_country", loggin_country);
                                sharedPrefsXtreme.saveData("loginuser_currency", loggin_currency);
                                sharedPrefsXtreme.saveData("business_name", businessname);
                                sharedPrefsXtreme.saveData("business_description", businessdescription);
                                Log.d("API_PASSWORD LOGGED", loggedapi_password);

                                // Other shared prefs
                                sharedPrefsXtreme.saveData("stringnumstaff", "0");
                                sharedPrefsXtreme.saveData("emailcollab", " ");
                                sharedPrefsXtreme.saveData("isLoggedin", "verifprocess");
                                sharedPrefsXtreme.saveData("phonenum", usernameString);
                                sharedPrefsXtreme.saveData("fromRegist", "truth");

                                loginbuttontext.setText("Login Successful");
                                Toast.makeText(getApplicationContext(),
                                        "Login to account started",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),
                                        choosesaba.class));

                            } else {
                                loginbutton.setEnabled(true);
                                loginbuttontext.setText("Retry Login");

                                AlertDialog ad = new AlertDialog.Builder(login.this)
                                        .create();
                                ad.setCancelable(true);
                                ad.setTitle("Login Failed");
                                ad.setMessage(messagedetails);
                                ad.setButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                ad.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("VolleyGetError", "JSON parsing error: " + e.getMessage());

                            AlertDialog ad = new AlertDialog.Builder(login.this)
                                    .create();
                            ad.setCancelable(true);
                            ad.setTitle("Login Failed");
                            ad.setMessage("Cannot process server request");
                            ad.setButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            ad.show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loginbutton.setEnabled(true);
                        loginbuttontext.setText("Retry Login");
                        progressindicator.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        // 1️⃣ No internet / unreachable host
                        if (error instanceof NoConnectionError || error instanceof NetworkError) {
                            Log.e("VolleyError", "No internet or network unreachable", error);
                            showError("No internet connection. Please check your network.");
                            return;
                        }

                        // 2️⃣ Timeout
                        if (error instanceof TimeoutError) {
                            Log.e("VolleyError", "Request timed out", error);
                            showError("Request timed out. Please try again.");
                            return;
                        }

                        // 3️⃣ HTTP errors (400 / 401 / 500 etc)
                        NetworkResponse response = error.networkResponse;

                        if (response != null) {
                            int statusCode = response.statusCode;

                            Log.e("VolleyHTTP",
                                    "HTTP " + statusCode + " | " +
                                            new String(response.data));

                            String serverMessage = "Server error occurred";

                            try {
                                String body = new String(
                                        response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8")
                                );

                                JSONObject obj = new JSONObject(body);
                                serverMessage = obj.optString("message", serverMessage);

                            } catch (Exception e) {
                                Log.e("VolleyParse", "Failed to parse error body", e);
                            }

                            if (statusCode == 400 || statusCode == 401) {
                                showError(serverMessage);
                            } else if (statusCode >= 500) {
                                showError("Server error. Please try again later.");
                            } else {
                                showError(serverMessage);
                            }
                            return;
                        }

                        // 4️⃣ Fallback
                        Log.e("VolleyError", "Unknown Volley error", error);
                        showError("Unexpected error occurred.");
                    }
                }

        ) {
            /** 🔑 Extract Verification header and decode base64 **/
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String verificationHeader = response.headers.get("Verification");


                    if (verificationHeader != null) {
                        byte[] decodedBytes = Base64.decode(verificationHeader, Base64.DEFAULT);
                        String decodedValue = new String(decodedBytes, "UTF-8");

                        // Format: APIUSERNAME:API_PASSWORD
                        String[] parts = decodedValue.split(":");
                        if (parts.length == 2) {
                            String apiUsernameFromHeader = parts[0];
                            loggedapi_password = parts[1]; // ✅ store API_PASSWORD
                            //Log.d("API_PASSWORD", loggedapi_password);
                        }
                    }

                    // Extract Authorization header (Bearer)
                    String authHeader = response.headers.get("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        bearerToken = authHeader.substring(7); // remove "Bearer " prefix
                        //Log.d("Bearer_Token", bearerToken);
                    }

                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    return Response.success(
                            new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );

                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }

            /** Headers for request **/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    // Inside login Activity (same class as volleyGet)
    private void showError(String message) {
        new AlertDialog.Builder(login.this)
                .setTitle("Login Failed")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Okay", (dialog, which) -> dialog.dismiss())
                .show();
    }


}
