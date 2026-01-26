package com.sabapp.saba.vendors;


import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.events.chooseservices;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.events.eventdashboard;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;


public class availabilitycalender extends AppCompatActivity {

    Set<CalendarDay> availableDays;
    Set<CalendarDay> busyDays;

    private CalendarDay selectedDay;

    Calendar startCal;
    Calendar endCal;

    sabaapp app;


    EditText etStartTime,etEndTime;

    AVLoadingIndicatorView progressindicator;

    LinearLayout addavailabilitylayoutbutton, removeavailabilitylayoutbutton;


    MaterialCalendarView calendarView;


    Map<CalendarDay, List<String>> availableTimes;


    private Calendar calendarFromDay(@Nullable CalendarDay date, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.set(date.getYear(), date.getMonth() - 1, date.getDay(), hour, minute);
        } else {
            LocalDate today = LocalDate.now();
            cal.set(today.getYear(), today.getMonthValue() - 1, today.getDayOfMonth(), hour, minute);
        }
        return cal;
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availabilitycalender);
        app = (sabaapp) getApplicationContext();

        calendarView = findViewById(R.id.calendarView);
        progressindicator = findViewById(R.id.progressbar);
        addavailabilitylayoutbutton = findViewById(R.id.addavailabilitylayoutbutton);
        removeavailabilitylayoutbutton = findViewById(R.id.removeavailabilitylayoutbutton);




        // ✅ Initialize sets INSIDE a method
        availableDays = new HashSet<>();
        busyDays = new HashSet<>();
        availableTimes = new HashMap<>();


        // 2️⃣ Define start and end dates
        int year = 2026;
        int month = 1; // January (1-indexed)
        int startDay = 20;
        int endDay = 27;

        // 3️⃣ Loop from start to end day and add each date
        for (int day = startDay; day <= endDay; day++) {
            availableDays.add(CalendarDay.from(year, month, day));
        }


        for (CalendarDay day : availableDays) {
            List<String> times = new ArrayList<>();
            // Example: available every hour from 9 AM to 5 PM
            for (int hour = 9; hour <= 17; hour++) {
                times.add(hour + ":00");
                times.add(hour + ":30"); // half-hour slots
            }
            availableTimes.put(day, times);
        }


        busyDays.add(CalendarDay.from(2026, 1, 27));
        busyDays.add(CalendarDay.from(2026, 2, 14));

        calendarView.addDecorators(
                new AvailableDayDecorator(availableDays),
                new BusyDayDecorator(busyDays)
        );

        loadAvailabilityFromServer(calendarView);

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDay = date;

            String availabledates = "";

            if (availableDays.contains(date)) {
                List<String> times = availableTimes.get(date);
                // Show times in a RecyclerView, Dialog, or Spinner
                // Example: Toast
                availabledates = "Available slots: " + times;

                        //Toast.makeText(this, "Available slots: " + times, Toast.LENGTH_LONG).show();
            } else if (busyDays.contains(date)) {
                Toast.makeText(this, "Day is fully booked", Toast.LENGTH_SHORT).show();

                availabledates = "Day is fully booked";
            }
            showAddAvailabilityDialog(date,availabledates);




        });


        addavailabilitylayoutbutton.setOnClickListener(v -> {

            showAddAvailabilityDialog(null,null);

        });

        removeavailabilitylayoutbutton.setOnClickListener(v -> {

            showDeleteAvailabilityDialog(null,null);

        });




    }


    private void showAddAvailabilityDialog(CalendarDay date, String availabledescription) {
        View view = getLayoutInflater()
                .inflate(R.layout.dialogue_add_availability, null);

        TextView tvDate = view.findViewById(R.id.tvDate);

        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        TextView availabledatestext = view.findViewById(R.id.availabledatesvalues);

        if(availabledescription!=null && !availabledescription.isEmpty()){

            availabledatestext.setText(availabledescription);
        }else{

            availabledatestext.setText("");
        }

        // Format selected date
        LocalDate localDate;

        if (date != null) {
            // Use CalendarDay if provided
            localDate = LocalDate.of(
                    date.getYear(),
                    date.getMonth(),
                    date.getDay()
            );
            tvDate.setText("Date: " + localDate.toString());
        }else{

            tvDate.setText("");
        }


        // Defaults
        //LocalTime startTime = LocalTime.of(7, 0);
        //LocalTime endTime = LocalTime.of(8, 0);

        startCal = calendarFromDay(date, 7, 0);
        endCal = calendarFromDay(date, 8, 0);

        SimpleDateFormat dateTimeFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        etStartTime = view.findViewById(R.id.etStartTime);
        etEndTime = view.findViewById(R.id.etEndTime);

        etStartTime.setText(dateTimeFormat.format(startCal.getTime()));
        etEndTime.setText(dateTimeFormat.format(endCal.getTime()));

        // Availability type
        String[] types = {"hour", "day", "week", "month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );
        spinnerType.setAdapter(adapter);

        etStartTime.setOnClickListener(v -> {
            showDateTimePicker(etStartTime, startCal);
        });

        etEndTime.setOnClickListener(v -> {
            showDateTimePicker(etEndTime, endCal);
        });


        new AlertDialog.Builder(this)
                .setTitle("Add Availability")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {

                    LocalDate localDateToUse;
                    if (date != null) {
                        // Use CalendarDay if provided
                        localDateToUse = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
                    } else {
                        // User picked a date/time manually in etStartTime
                        String startTimeStr = etStartTime.getText().toString(); // "yyyy-MM-dd HH:mm"
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
                        try {
                            LocalDateTime startDateTime = LocalDateTime.parse(startTimeStr, formatter);
                            localDateToUse = startDateTime.toLocalDate();
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                            // fallback
                            localDateToUse = LocalDate.now();
                        }
                    }

                    // 1. Calculate the difference in milliseconds
                    long diffInMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();

                    // 2. Define constants for easier reading
                    long hourInMillis = 3600000L;
                    long dayInMillis = 86400000L;
                    long weekInMillis = 604800000L;
                    long monthInMillis = 2592000000L; // Approximate (30 days)

                    String calculatedType;

                    // 3. Logic: Categorize based on the difference
                    if (diffInMillis <= dayInMillis) {
                        // If 24 hours or less
                        calculatedType = "hour";
                    } else if (diffInMillis <= weekInMillis) {
                        // If more than a day but up to a week
                        calculatedType = "day";
                    } else if (diffInMillis <= monthInMillis) {
                        // If more than a week but up to a month
                        calculatedType = "week";
                    } else {
                        // If more than a month (includes years)
                        calculatedType = "month";
                    }

                    // 4. Submit using the calculated string instead of the spinner
                    submitAvailability(
                            localDateToUse,
                            etStartTime.getText().toString(),
                            etEndTime.getText().toString(),
                            calculatedType
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();

    }

    private void showDeleteAvailabilityDialog(CalendarDay date, String availabledescription) {
        View view = getLayoutInflater()
                .inflate(R.layout.dialogue_delete_availability, null);

        TextView tvDate = view.findViewById(R.id.tvDate);

        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        TextView availabledatestext = view.findViewById(R.id.availabledatesvalues);

        if(availabledescription!=null && !availabledescription.isEmpty()){

            availabledatestext.setText(availabledescription);
        }else{

            availabledatestext.setText("");
        }

        // Format selected date
        LocalDate localDate;

        if (date != null) {
            // Use CalendarDay if provided
            localDate = LocalDate.of(
                    date.getYear(),
                    date.getMonth(),
                    date.getDay()
            );
            tvDate.setText("Date: " + localDate.toString());
        }else{

            tvDate.setText("");
        }


        // Defaults
        //LocalTime startTime = LocalTime.of(7, 0);
        //LocalTime endTime = LocalTime.of(8, 0);

        startCal = calendarFromDay(date, 7, 0);
        endCal = calendarFromDay(date, 8, 0);

        SimpleDateFormat dateTimeFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        etStartTime = view.findViewById(R.id.etStartTime);
        etEndTime = view.findViewById(R.id.etEndTime);

        etStartTime.setText(dateTimeFormat.format(startCal.getTime()));
        etEndTime.setText(dateTimeFormat.format(endCal.getTime()));

        // Availability type
        String[] types = {"hour", "day", "week", "month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );
        spinnerType.setAdapter(adapter);

        etStartTime.setOnClickListener(v -> {
            showDateTimePicker(etStartTime, startCal);
        });

        etEndTime.setOnClickListener(v -> {
            showDateTimePicker(etEndTime, endCal);
        });


        new AlertDialog.Builder(this)
                .setTitle("Delete Availability")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {

                    LocalDate localDateToUse;
                    if (date != null) {
                        // Use CalendarDay if provided
                        localDateToUse = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
                    } else {
                        // User picked a date/time manually in etStartTime
                        String startTimeStr = etStartTime.getText().toString(); // "yyyy-MM-dd HH:mm"
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
                        try {
                            LocalDateTime startDateTime = LocalDateTime.parse(startTimeStr, formatter);
                            localDateToUse = startDateTime.toLocalDate();
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                            // fallback
                            localDateToUse = LocalDate.now();
                        }
                    }

                    // 1. Calculate the difference in milliseconds
                    long diffInMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();

                    // 2. Define constants for easier reading
                    long hourInMillis = 3600000L;
                    long dayInMillis = 86400000L;
                    long weekInMillis = 604800000L;
                    long monthInMillis = 2592000000L; // Approximate (30 days)

                    String calculatedType;

                    // 3. Logic: Categorize based on the difference
                    if (diffInMillis <= dayInMillis) {
                        // If 24 hours or less
                        calculatedType = "hour";
                    } else if (diffInMillis <= weekInMillis) {
                        // If more than a day but up to a week
                        calculatedType = "day";
                    } else if (diffInMillis <= monthInMillis) {
                        // If more than a week but up to a month
                        calculatedType = "week";
                    } else {
                        // If more than a month (includes years)
                        calculatedType = "month";
                    }

                    // 4. Submit using the calculated string instead of the spinner
                    deleteAvailability(
                            localDateToUse,
                            etStartTime.getText().toString(),
                            etEndTime.getText().toString(),
                            calculatedType
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();

    }



    private void showDateTimePicker(EditText target, Calendar calendar) {

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, day) -> {

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    TimePickerDialog timePicker = new TimePickerDialog(
                            this,
                            (tp, hour, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);

                                endCal.setTime(calendar.getTime());
                                endCal.add(Calendar.HOUR_OF_DAY, 1);

                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm", Locale.getDefault()
                                );
                                target.setText(sdf.format(calendar.getTime()));
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
    }


    private void submitAvailability( LocalDate date,
                                        String startTime,
                                        String endTime,
                                        String type)
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        JSONObject json = new JSONObject();

        try {
            // =============================
            // ROOT PAYLOAD
            // =============================
            //payload.put("event_amountspent", "0");

            json.put("type", type);
            json.put("start_time", startTime.replace("Start: ", ""));
            json.put("end_time", endTime.replace("End: ", ""));
            json.put("start_date", date.toString());
            json.put("end_date", date.toString());
            json.put("day_of_week", date.getDayOfWeek().getValue() % 7);
            //payload.put("event_budget", event_budget);


            Log.d("SENDING WHOLE availability PAYLOAD", "JSON: "+json);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/availability/add";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "Submit budget response : "+response.toString());
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





                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(availabilitycalender.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Completed");
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
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(availabilitycalender.this)
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


    private void deleteAvailability( LocalDate date,
                                     String startTime,
                                     String endTime,
                                     String type)
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        JSONObject json = new JSONObject();

        try {
            // =============================
            // ROOT PAYLOAD
            // =============================
            //payload.put("event_amountspent", "0");

            json.put("type", type);
            json.put("start_time", startTime.replace("Start: ", ""));
            json.put("end_time", endTime.replace("End: ", ""));
            json.put("start_date", date.toString());
            json.put("end_date", date.toString());
            json.put("day_of_week", date.getDayOfWeek().getValue() % 7);
            //payload.put("event_budget", event_budget);


            Log.d("SENDING WHOLE availability PAYLOAD", "JSON: "+json);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/availability/delete";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "Submit budget response : "+response.toString());
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





                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(availabilitycalender.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Completed");
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
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(availabilitycalender.this)
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


    private void loadAvailabilityFromServer(MaterialCalendarView calendarView)
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();





        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/availability";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "Submit budget response : "+response.toString());
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


                                    availableDays.clear();
                                    busyDays.clear();
                                    availableTimes.clear();


                                    try {
                                        JSONArray data = response.getJSONArray("DATA");

                                        for (int i = 0; i < data.length(); i++) {
                                            JSONObject obj = data.getJSONObject(i);

                                            String startDateStr = obj.getString("start_date"); // yyyy-MM-dd
                                            String endDateStr = obj.getString("end_date");
                                            String startTime = obj.getString("start_time");     // HH:mm:ss
                                            String endTime = obj.getString("end_time");

                                            LocalDate startDate = LocalDate.parse(startDateStr);
                                            LocalDate endDate = LocalDate.parse(endDateStr);

                                            // 🔁 Handle date ranges
                                            for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {

                                                CalendarDay calDay = CalendarDay.from(
                                                        d.getYear(),
                                                        d.getMonthValue(),
                                                        d.getDayOfMonth()
                                                );

                                                availableDays.add(calDay);

                                                List<String> times =
                                                        availableTimes.getOrDefault(calDay, new ArrayList<>());

                                                // Nice readable slot
                                                times.add(
                                                        startTime.substring(0,5) + " - " +
                                                                endTime.substring(0,5)
                                                );

                                                availableTimes.put(calDay, times);
                                            }
                                        }

                                        // 🔹 Refresh decorators
                                        calendarView.removeDecorators();
                                        calendarView.addDecorators(
                                                new AvailableDayDecorator(availableDays),
                                                new BusyDayDecorator(busyDays)
                                        );

                                        //refreshCalendarDecorators();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }







                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(availabilitycalender.this)
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

    private void refreshCalendarDecorators() {
        calendarView.removeDecorators();

        calendarView.addDecorators(
                new AvailableDayDecorator(availableDays)
        );
    }




}

