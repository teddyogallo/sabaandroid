package com.sabapp.saba.vendors;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.sabapp.saba.R;
import com.sabapp.saba.VendorCatalogueFragment;
import com.sabapp.saba.adapters.capabilityRecyclerAdapter;
import com.sabapp.saba.adapters.servicesOfferedRecyclerAdapter;
import com.sabapp.saba.adapters.vendorservicesListRecycler;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.eventdashboard;
import com.sabapp.saba.messaging.conversationactivity;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class vendorproposrequest extends AppCompatActivity {


    Set<CalendarDay> availableDays;
    Set<CalendarDay> busyDays;

    Calendar startCal;
    Calendar endCal;

    MaterialCalendarView calendarView;


    Map<CalendarDay, List<String>> availableTimes;


    String vendor_id;

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;



    RecyclerView servicesselected;

    ImageView backbuttonImage;

    LinearLayout nextbuttonLayout;

    String EventId;

    RecyclerView  servicesofferedrecycler;
    AVLoadingIndicatorView progressBar;


    ArrayList<sabaEventItem> serviceswholearray;

    ArrayList<String> base_pricelist;
    ArrayList<JSONObject> capability_detailslist;
    ArrayList<String> capability_idlist;
    ArrayList<String> service_image_locationlist;
    ArrayList<String> vendorserviceimage_idList;
    ArrayList<String> vendorserviceimagelocationList;
    ArrayList<String> vendoridList;

    ArrayList<String> vendornameList;
    ArrayList<String> vendorcapabilitynameList;
    ArrayList<String> vendorlocationList;

    vendorservicesListRecycler servicesofferAdapter;

    String event_name, budget_amount, budget_spent, vendorname, vendorlocation,vendoravailability,vendorbaseprice, vendorimagestore;
    int vendorrating;
    int vendorchemistryscore;

    ImageView vendorImage,availablestatusimage;

    TextView eventnamelabel, vendornamelabel, chemistryscorelabel, budgetspentlabel,availablestatuslabel;

    Button chemistryscorebutton;
    LinearLayout sendmessagebuttonlayout, sendproposalbuttonlayout, approvepaymentbuttonlayout, withdrawproposalbuttonlayout;

    private CalendarDay selectedDay;

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
        setContentView(R.layout.vendorproposrequest);

        calendarView =(MaterialCalendarView)findViewById(R.id.calendarView);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressbar);

        servicesofferedrecycler = (RecyclerView) findViewById(R.id.vendorservicesrecycler);

        vendorImage = (ImageView) findViewById(R.id.vendor_image);
        backbuttonImage = (ImageView)findViewById(R.id.backbuttonimage);
        availablestatusimage = (ImageView) findViewById(R.id.availability_check);

        eventnamelabel = (TextView) findViewById(R.id.eventtitletext);
        vendornamelabel = (TextView) findViewById(R.id.vendor_name);
        chemistryscorebutton = (Button) findViewById(R.id.chemistry_badge);
        budgetspentlabel = (TextView) findViewById(R.id.eventbudgettextlabel);
        availablestatuslabel = (TextView) findViewById(R.id.availability_text);

        sendmessagebuttonlayout = (LinearLayout) findViewById(R.id.sendmessagelayoutbutton);
        sendproposalbuttonlayout = (LinearLayout) findViewById(R.id.acceptproposallayoutbutton);
        approvepaymentbuttonlayout = (LinearLayout) findViewById(R.id.approvepaymentlayoutbutton);
        withdrawproposalbuttonlayout = (LinearLayout) findViewById(R.id.rejectproposallayoutbutton);


        app = (sabaapp) getApplicationContext();

        // ✅ Initialize sets INSIDE a method
        availableDays = new HashSet<>();
        busyDays = new HashSet<>();
        availableTimes = new HashMap<>();

        Intent intent = getIntent();

        EventId = intent.getStringExtra("event_id");
        vendor_id = intent.getStringExtra("vendor_id");
        event_name = intent.getStringExtra("event_name");
        eventnamelabel.setText(event_name);
        budget_amount = intent.getStringExtra("budget_amount");
        budget_spent = intent.getStringExtra("budget_spent");
        vendorname = intent.getStringExtra("vendor_name");
        vendornamelabel.setText(vendorname);
        vendorlocation = intent.getStringExtra("vendor_location");
        vendoravailability = intent.getStringExtra("availability");
        vendorbaseprice = intent.getStringExtra("base_price");
        vendorrating = intent.getIntExtra("rating",1);
        vendorchemistryscore = intent.getIntExtra("chemistry_score",0);
        vendorimagestore = intent.getStringExtra("vendor_image");

        if(vendorimagestore!=null){

            try{

                Glide.with(context)
                        .load(vendorimagestore)
                        .placeholder(R.drawable.defaultimage)
                        .into(vendorImage);

            } catch (Exception e) {

                Log.e("LOAD event assigned IMAGE ERROR: ",""+e);

                Glide.with(context).load(R.drawable.defaultimage).into(vendorImage);

            }
        }else{

            Glide.with(context).load(R.drawable.defaultimage).into(vendorImage);
        }

        serviceswholearray = new ArrayList<sabaEventItem>();

        //FOR SERVICES

        base_pricelist = new ArrayList<String>();
        capability_detailslist = new ArrayList<JSONObject>();
        capability_idlist = new ArrayList<String>();
        service_image_locationlist = new ArrayList<String>();
        vendorserviceimage_idList = new ArrayList<String>();
        vendorserviceimagelocationList = new ArrayList<String>();
        vendoridList = new ArrayList<String>();

        vendornameList = new ArrayList<String>();
        vendorcapabilitynameList = new ArrayList<String>();
        vendorlocationList = new ArrayList<String>();

        servicesofferedrecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        servicesofferAdapter=new vendorservicesListRecycler(serviceswholearray,context, vendorproposrequest.this, app);
        servicesofferedrecycler.setAdapter(servicesofferAdapter);


        getServicesList();



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


                Toast.makeText(this, "Available slots: " + times, Toast.LENGTH_LONG).show();
            } else if (busyDays.contains(date)) {
                Toast.makeText(this, "Day is fully booked", Toast.LENGTH_SHORT).show();


            }





        });



        backbuttonImage.setOnClickListener(v -> {
            //start of budget next button

            Intent intent2 = new Intent(getApplicationContext(), sabaDrawerActivity.class);
            intent2.putExtra("createadrad", "gotowhatsappbotmaker");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);

            //end of budget next button
        });


        sendmessagebuttonlayout.setOnClickListener(v -> {
            //start of budget next button

            Intent intent2 = new Intent(getApplicationContext(),  conversationactivity.class);
            intent2.putExtra("createadrad", "gotowholechatpref");
            intent2.putExtra("activeuser", vendor_id);
            intent2.putExtra("chatname", vendorname);
            intent2.setFlags(intent2.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);



            //end of budget next button
        });

        sendproposalbuttonlayout.setOnClickListener(v -> {
            //start of budget next button

            Intent intent2 = new Intent(getApplicationContext(), eventdashboard.class);
            intent2.putExtra("createadrad", "gotowhatsappbotmaker");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);

            //end of budget next button
        });

        approvepaymentbuttonlayout.setOnClickListener(v -> {
            //start of budget next button

            Intent intent2 = new Intent(getApplicationContext(), eventdashboard.class);
            intent2.putExtra("createadrad", "gotowhatsappbotmaker");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);

            //end of budget next button
        });

        withdrawproposalbuttonlayout.setOnClickListener(v -> {
            //start of budget next button

            Intent intent2 = new Intent(getApplicationContext(), eventdashboard.class);
            intent2.putExtra("createadrad", "gotowhatsappbotmaker");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);

            //end of budget next button
        });




    }






    private void getServicesList()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();





        String paymentsendpoint="https://api.getabirio.com/v0/vendor/capabilities/"+vendor_id;



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL SERVICES: "+response.toString());
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
                                    dataobj = jsonObj.getJSONArray("DATA");


                                    if(dataobj.length()==0){
                                        // their are not values to add
                                        String messageerror="There was Zero products retrieved";
                                        Log.d("Msg:",messageerror);



                                        base_pricelist.add(null);
                                        capability_detailslist.add(null);
                                        capability_idlist.add(null);
                                        service_image_locationlist.add(null);

                                        vendorserviceimage_idList.add(null);
                                        vendorserviceimagelocationList.add(null);
                                        vendoridList.add(null);

                                        vendornameList.add("No Capability loaded");
                                        vendorcapabilitynameList.add(null);
                                        vendorlocationList.add("Please try later");


                                        serviceswholearray.clear();

                                        sabaEventItem item=new sabaEventItem();
                                        item.setcapability_code(null);
                                        item.setcapability_name("No services");
                                        item.setcapability_category(null);
                                        item.setcapability_imageid(null);
                                        item.setcapability_image_location(null);

                                        serviceswholearray.add(item);

                                        servicesofferAdapter.notifyDataSetChanged();


                                        //end of their are no values to add
                                    }else{

                                        for (int i = 0; i < dataobj.length(); i++) {

                                            jsonObj = dataobj.getJSONObject(i);

                                            String base_price = jsonObj.optString("base_price", null);
                                            String capability_id = jsonObj.optString("capability_id", null);
                                            String service_image_location = jsonObj.optString("service_image_location", null);
                                            String capabilitname = jsonObj.optString("capability_name", null);
                                            String vendorname = jsonObj.optString("vendor_name", null);
                                            String vendorlocation = jsonObj.optString("location", null);
                                            String vendorserviceimageid = jsonObj.optString("service_image_id", null);
                                            String vendorid = jsonObj.optString("vendor_id", null);

                                            JSONObject capability_details = jsonObj.optJSONObject("capability_details");

                                            base_pricelist.add(base_price);
                                            capability_idlist.add(capability_id);
                                            service_image_locationlist.add(service_image_location);
                                            vendorserviceimage_idList.add(vendorserviceimageid);
                                            vendoridList.add(vendorid);
                                            vendornameList.add(vendorname);
                                            vendorcapabilitynameList.add(capabilitname);
                                            vendorlocationList.add(vendorlocation);


                                            capability_detailslist.add(capability_details);


                                            Log.d("Added Capability", vendorname + " added");
                                        }




                                        serviceswholearray.clear();

                                        for(Integer i=0; i<capability_idlist.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();


                                            item.setvendorbase_price(base_pricelist.get(i));
                                            item.setvendorcapability_details(capability_detailslist.get(i));
                                            item.setvendorcapability_id(capability_idlist.get(i));
                                            item.setvendorserviceimage_id(vendorserviceimage_idList.get(i));
                                            item.setvendorserviceimagelocation(service_image_locationlist.get(i));
                                            item.setvendorid(vendoridList.get(i));

                                            item.setvendorname(vendornameList.get(i));
                                            item.setvendorcapabilityname(vendorcapabilitynameList.get(i));
                                            item.setvendorlocation(vendorlocationList.get(i));




                                            //setImagebitmap
                                            serviceswholearray.add(item);

                                        }
                                        servicesofferAdapter.notifyDataSetChanged();

                                        app.setCapabilitylist( serviceswholearray);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : capability_idlist)
                                        {
                                            Log.d("Event Name value--", singleRecord.toString());
                                        }


                                        //adaptertwo.notifyDataSetChanged();

                                    }

                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

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





        String paymentsendpoint="https://api.getabirio.com/v0/vendor/availability/"+vendor_id;



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

                                    android.app.AlertDialog ad = new android.app.AlertDialog.Builder(vendorproposrequest.this)
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
