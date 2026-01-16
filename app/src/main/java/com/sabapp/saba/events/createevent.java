package com.sabapp.saba.events;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.onboarding.chooselogintype;
import com.sabapp.saba.onboarding.login;
import com.sabapp.saba.onboarding.signupaccount;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class createevent extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    private ArrayList<String> setuptypeList, eventvibelist;

    EditText eventname, eventlocation, eventdate, guestcount;
    AppCompatSpinner eventtype, eventyvibe;

    String eventtypeValue,eventVibeString;

    LinearLayout nextbuttonlayout;
    TextView nextbuttontext;

    ImageView backarrowimagebutton;

    JSONArray dataobj;


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
        startActivity(new Intent(getApplicationContext(), sabaDrawerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupevent);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressindicator);
        app = (sabaapp) getApplicationContext();


        eventname = (EditText) findViewById(R.id.eventname);
        eventlocation = (EditText)findViewById(R.id.eventlocation);
        eventdate = (EditText)findViewById(R.id.eventdate);
        guestcount = (EditText) findViewById(R.id.guestnumber);


        eventtype = (AppCompatSpinner)findViewById(R.id.eventtypespinner);
        eventyvibe = (AppCompatSpinner)findViewById(R.id.eventvibe);

        nextbuttonlayout = (LinearLayout)findViewById(R.id.nextbuttonlayout);

        nextbuttontext = (TextView) findViewById(R.id.nextbuttontext);

        backarrowimagebutton = (ImageView)findViewById(R.id.backbuttonimage);




        setuptypeList= new ArrayList<String>();
        eventvibelist = new ArrayList<String>();



        //setup for account type

        setuptypeList.add("Birthday");
        setuptypeList.add("Wedding");
        setuptypeList.add("Corporate Event");
        setuptypeList.add("Conference");
        setuptypeList.add("Concert");
        setuptypeList.add("Anniversary Celebration");
        setuptypeList.add("Baby Shower");
        setuptypeList.add("Graduation Party");
        setuptypeList.add("Engagement Party");
        setuptypeList.add("Charity Event");
        setuptypeList.add("Product Launch");
        setuptypeList.add("Holiday Party");
        setuptypeList.add("Sports Event");
        setuptypeList.add("Networking Event");


        eventvibelist.add("Casual");
        eventvibelist.add("Formal");
        eventvibelist.add("Fun");
        eventvibelist.add("Romantic");
        eventvibelist.add("Energetic");
        eventvibelist.add("Elegant");
        eventvibelist.add("Chill");
        eventvibelist.add("Festive");
        eventvibelist.add("Luxury");
        eventvibelist.add("Creative");
        eventvibelist.add("Intimate");
        eventvibelist.add("Vibrant");
        eventvibelist.add("Relaxed");
        eventvibelist.add("Sophisticated");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(createevent.this,
                R.layout.spinner_item, setuptypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventtype.setAdapter(adapter);
        eventtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Log.e("itemSelected", item.toString());
                    eventtypeValue=item.toString();




                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                eventtypeValue="Birthday";

            }

        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(createevent.this,
                R.layout.spinner_item, eventvibelist);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventyvibe.setAdapter(adapter2);
        eventyvibe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Log.e("Vibe itemSelected", item.toString());
                    eventVibeString=item.toString();




                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                eventVibeString="Casual";

            }

        });
        eventdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    createevent.this,
                    (view, year, month, dayOfMonth) -> {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog timePicker = new TimePickerDialog(
                                createevent.this,
                                (timeView, hourOfDay, minute) -> {

                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    calendar.set(Calendar.SECOND, 0);

                                    SimpleDateFormat sdf =
                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                                    String formatted = sdf.format(calendar.getTime());
                                    eventdate.setText(formatted);

                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );

                        timePicker.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePicker.show();
        });

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        sdf.setLenient(false); // 🔥 CRITICAL
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        eventdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 19) {
                    try {
                        sdf.parse(s.toString());
                        eventdate.setError(null);
                    } catch (ParseException e) {
                        eventdate.setError("Format: yyyy-MM-dd HH:mm:ss");
                    }
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });




        nextbuttonlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(eventname.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(createevent.this, "Event name cannot be empty", Toast.LENGTH_SHORT).show();

                }else if(eventlocation.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(createevent.this, "Event location cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else if(eventdate.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(createevent.this, "Event location cannot be empty ", Toast.LENGTH_SHORT).show();

                }
                else if(guestcount.getText().toString().matches(""))
                {
                    //bizdesclay.setError("Business Description cannot be empty");

                    Toast.makeText(createevent.this, "Guest count should not be empty", Toast.LENGTH_SHORT).show();

                }

                else
                {


                    progressindicator.setVisibility(View.VISIBLE);
                                /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/



                    sendpostRequest();




                }


            }
        });


        backarrowimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(createevent.this, "Redirecting to login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(createevent.this, sabaDrawerActivity.class));


            }
        });



        //end of oncreate

    }


    private void sendpostRequest()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();

        // Get the text from EditText
        String dateInput = eventdate.getText().toString().trim();
        String unixTimestampString = null;

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = sdf.parse(dateInput);
            long unixSeconds = date.getTime() / 1000;
            unixTimestampString = String.valueOf(unixSeconds);
        } catch (ParseException e) {
            Log.e("DateParseError", "Invalid datetime. Expected yyyy-MM-dd HH:mm:ss");
        }


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());
        paramsotpu.put("event_name", eventname.getText().toString());
        paramsotpu.put("event_time", unixTimestampString);
        paramsotpu.put("event_location", eventlocation.getText().toString());
        paramsotpu.put("event_type", eventtypeValue);
        paramsotpu.put("event_vibe", eventVibeString);




        String paymentsendpoint="https://api.sabaapp.co/v0/events";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL Products: "+response.toString());
                        hideProgressBar();
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");
                                String messagedetails =jsonObj.getString("MESSAGE");
                                if(message.toLowerCase().matches("success"))
                                {


                                    String datavalue =jsonObj.getString("DATA");

                                    Log.d("EVENT ID", datavalue);


                                    Intent intent = new Intent(createevent.this, chooseservices.class);
                                    intent.putExtra("event_id", datavalue);
                                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    createevent.this.startActivity(intent);




                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    AlertDialog ad = new AlertDialog.Builder(createevent.this)
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
                            hideProgressBar();

                        } else {
                            hideProgressBar();
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                //continuetonextbutton.setText("Upload to your store");
                Log.e("Menu list error is ", "" + error);

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


                String creds = String.format("%s:%s",app.getApiusername(),app.getApipassword());
                Log.e("Login with API username", "" + app.getApiusername()+" , And API passwrod"+app.getApipassword());
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }

}
