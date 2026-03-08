package com.sabapp.saba.onboarding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.exceptions.NoCredentialException;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sabapp.saba.R;
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.sabaDrawerActivity;
import com.sabapp.saba.sabaVendorDrawerActivity;
import com.sabapp.saba.sabaplannerDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class loginoptionchoose extends AppCompatActivity {


    sabaapp app;



    private ArrayList<String> setuptypeList;

    String accounttype;

    String businessnamevalue = null;


    SharedPrefsXtreme sharedPrefsXtreme;


    LinearLayout chooseemaillogin,choosegooglelogin,choosefacebooklogin;

    private CredentialManager credentialManager;
    private Executor executor;

    String fcm_token = null;

    String logged_api_username, loggedapi_password, loggin_country, loggin_currency, loginkeytime, bearerToken;

    AVLoadingIndicatorView progressindicator;


    String usernameString, passwordString;

    @Override
    public void onBackPressed(){

        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), onboardone.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginoptionchoose);
        credentialManager = CredentialManager.create(this);
        executor = ContextCompat.getMainExecutor(this);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.signinprogress);
        app = (sabaapp)getApplicationContext();
        app.logOutPreliminaries();


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

                            startGoogleSignIn();


                        });





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

    private void startGoogleSignIn() {

        String nonce = UUID.randomUUID().toString();

        GetGoogleIdOption googleIdOption =
                new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false) // first login
                        .setServerClientId("584250703624-3askntni1cj5vbhe9v0u9bv9abkhiiqg.apps.googleusercontent.com")
                        .setAutoSelectEnabled(false)
                        .setNonce(nonce)
                        .build();

        GetCredentialRequest request =
                new GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build();

        credentialManager.getCredentialAsync(
                this,
                request,
                null,
                executor,
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {

                    @Override
                    public void onResult(GetCredentialResponse result) {

                        Credential credential = result.getCredential();

                        if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                .equals(credential.getType())) {

                            GoogleIdTokenCredential googleCredential =
                                    GoogleIdTokenCredential.createFrom(credential.getData());

                            String idToken = googleCredential.getIdToken();

                            Log.d("GOOGLE_LOGIN", "ID Token: " + idToken);

                            volleyGet(idToken);
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {

                        choosegooglelogin.setVisibility(View.GONE);
                        progressindicator.setVisibility(View.GONE);

                        if (e instanceof NoCredentialException) {
                            Log.e("GOOGLE_LOGIN", "No saved credentials. Showing account chooser.");
                        } else {
                            Log.e("GOOGLE_LOGIN", "Other sign-in error", e);
                        }
                    }
                }
        );


    }


    public void volleyGet(String id_token) {
        choosegooglelogin.setEnabled(false);
        progressindicator.setVisibility(View.VISIBLE);


        HashMap<String, String> paramsotpu = new HashMap<>();
        paramsotpu.put("id_token", id_token);
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

        String loginuserendpoint = "https://api.getabirio.com/v0/account/logingoogle";

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


                                Toast.makeText(getApplicationContext(),
                                        "Login to account started",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),
                                        choosesaba.class));
                                finish();

                            } else {
                                choosegooglelogin.setEnabled(true);


                                AlertDialog ad = new AlertDialog.Builder(loginoptionchoose.this)
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

                            AlertDialog ad = new AlertDialog.Builder(loginoptionchoose.this)
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

                        choosegooglelogin.setEnabled(true);

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


    private void showError(String message) {
        new AlertDialog.Builder(loginoptionchoose.this)
                .setTitle("Login Failed")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Okay", (dialog, which) -> dialog.dismiss())
                .show();
    }





}
