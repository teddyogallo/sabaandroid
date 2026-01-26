package com.sabapp.saba;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sabapp.saba.adapters.recentactivitiesRecyclerAdapter;
import com.sabapp.saba.adapters.sabaeventlistclientHomeRecyclerAdapter;
import com.sabapp.saba.adapters.servicesOfferedRecyclerAdapter;
import com.sabapp.saba.adapters.vendorassignmentsRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.messaging.messagestartactivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeclientFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class homeclientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout createEventLayout, messagesquicklink, paymentsquicklink, fevoritesquicklink;

    AVLoadingIndicatorView progressBar;

    sabaapp app;

    JSONArray dataobj;
    //eventlist values


    ArrayList<String> user_idlist = new ArrayList<String>();
    ArrayList<String> event_namelist;
    ArrayList<String> event_timelist,event_timeUnixlist;
    ArrayList<String> event_locationlist;
    ArrayList<String> event_budgetlist;
    ArrayList<String> budget_spentlist;
    ArrayList<String> setup_statuslist;
    ArrayList<String> event_statuslist;
    ArrayList<String> planner_idlist;
    ArrayList<String> event_image_idlist;
    ArrayList<String> event_image_locationlist;
    ArrayList<String> image_encodedlist;
    ArrayList<String> time_setuplist;
    ArrayList<String> event_idlist;

    String globalapiusername, globalapipassword;

    ArrayList<sabaEventItem> eventwholearray;

    RecyclerView eventlistrecyclerview,servicesofferedrecycler;

    int loggedinnumberGlobal =0;

    sabaeventlistclientHomeRecyclerAdapter sabaeventsadapter;



    //for services
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


    ArrayList<sabaEventItem> serviceswholearray;


    recentactivitiesRecyclerAdapter servicesofferAdapter;

    Context context;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeclientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeclientFragment newInstance(String param1, String param2) {
        homeclientFragment fragment = new homeclientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public homeclientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // now you can safely use it
    }



    public void showProgressBar()
    {
        progressBar.smoothToShow();
    }
    public void hideProgressBar()
    {
        progressBar.smoothToHide();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (sabaapp) requireActivity().getApplication();



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onResume()
    {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

        //Objects.requireNonNull(((sabaDrawerActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        //Objects.requireNonNull(((sabaDrawerActivity) requireActivity()).getSupportActionBar()).setHomeButtonEnabled(false);
        setUpRecycler();
        getEventslist();
        getServicesList();
        //loadTemplateImagesBottom();

        //save number of times user has logged in
        loggedinnumberGlobal = app.getLoggeddashboardtime();
        loggedinnumberGlobal +=1;
        SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);
        sharedPrefsXtreme.saveIntData("loggeddashtime", loggedinnumberGlobal);


        /*LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("CHAT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("PAYMENT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("ORDER_BROADCAST"));
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("NOTIFICATION_LOCAL_BROADCAST"));*/

    }

    private void loadTemplateImagesBottom()
    {
        eventwholearray.clear();
        for(Integer i=0; i<event_namelist.size(); i++)
        {
            sabaEventItem item=new sabaEventItem();

            item.seteventName(event_namelist.get(i));

            item.seteventUserid(user_idlist.get(i));

            item.seteventTime(event_timelist.get(i));
            item.seteventTimeUnix(event_timeUnixlist.get(i));
            item.seteventLocation(event_locationlist.get(i));
            item.seteventBudget(event_budgetlist.get(i));
            item.seteventBudget(budget_spentlist.get(i));
            item.seteventStatus(setup_statuslist.get(i));
            item.seteventStatus(event_statuslist.get(i));
            item.setplannerId(planner_idlist.get(i));
            item.seteventimageId(event_image_idlist.get(i));
            item.seteventimageLocation(event_image_locationlist.get(i));
            item.seteventimageEncoded(image_encodedlist.get(i));
            item.settime_Setup(time_setuplist.get(i));
            item.setevent_Id(event_idlist.get(i));


            //setImagebitmap
            eventwholearray.add(item);

        }
        sabaeventsadapter.notifyDataSetChanged();
    }

    private void setUpRecycler()
    {



        eventlistrecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        sabaeventsadapter=new sabaeventlistclientHomeRecyclerAdapter(eventwholearray,context, homeclientFragment.this, app);
        eventlistrecyclerview.setAdapter(sabaeventsadapter);


        servicesofferedrecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        servicesofferAdapter=new recentactivitiesRecyclerAdapter(serviceswholearray,context, homeclientFragment.this, app);
        servicesofferedrecycler.setAdapter(servicesofferAdapter);
        /*
        janjarecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new wwdashboardAdapter(templateArrays,context, dashboardFragment.this);
        janjarecycler.setAdapter(adapter);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_homeclient, container, false);
        View view = inflater.inflate(R.layout.fragment_homeclient, container, false);


        createEventLayout = view.findViewById(R.id.createeventlayout);
        messagesquicklink = view.findViewById(R.id.messagequicklinkbutton);
        paymentsquicklink = view.findViewById(R.id.paymentsquicklinkbutton);
        fevoritesquicklink = view.findViewById(R.id.fevoritesquicklinkbutton);
        eventlistrecyclerview= view.findViewById(R.id.eventslistrecycler);
        servicesofferedrecycler = view.findViewById(R.id.recentactivitiesrecycler);

        progressBar = view.findViewById(R.id.progressbar);
        hideProgressBar();

        eventwholearray = new ArrayList<sabaEventItem>();
        serviceswholearray = new ArrayList<sabaEventItem>();

        user_idlist = new ArrayList<String>();
        event_namelist  = new ArrayList<String>();
        event_timelist  = new ArrayList<String>();
        event_timeUnixlist = new ArrayList<String>();
        event_locationlist = new ArrayList<String>();
        event_budgetlist  = new ArrayList<String>();
        budget_spentlist = new ArrayList<String>();
        setup_statuslist= new ArrayList<String>();
        event_statuslist = new ArrayList<String>();
        planner_idlist = new ArrayList<String>();
        event_image_idlist = new ArrayList<String>();
        event_image_locationlist = new ArrayList<String>();
        image_encodedlist = new ArrayList<String>();
        time_setuplist = new ArrayList<String>();
        event_idlist  = new ArrayList<String>();


        base_pricelist = new ArrayList<String>();
        capability_detailslist = new ArrayList<JSONObject>();
        capability_idlist = new ArrayList<String>();
        service_image_locationlist = new ArrayList<String>();
        eventwholearray = new ArrayList<sabaEventItem>();

        vendorserviceimage_idList = new ArrayList<String>();
        vendorserviceimagelocationList = new ArrayList<String>();
        vendoridList = new ArrayList<String>();

        vendornameList = new ArrayList<String>();
        vendorcapabilitynameList = new ArrayList<String>();
        vendorlocationList = new ArrayList<String>();




        // Set click listener
        createEventLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(getContext(), "Create New Event clicked", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), createevent.class);
            intent.putExtra("createadrad", "gotowhatsappbotmaker");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);


        });

        messagesquicklink.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            Toast.makeText(getContext(), "Messages link clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), messagestartactivity.class);
            intent.putExtra("createadrad", "gotowhatsappbotmaker");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);


        });

        paymentsquicklink.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            Toast.makeText(getContext(), "Payments quick link clicked", Toast.LENGTH_SHORT).show();


        });

        fevoritesquicklink.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            Toast.makeText(getContext(), "Fevorites quicklink Selected", Toast.LENGTH_SHORT).show();


        });





    return view;
    }



    private void getEventslist()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());


        String paymentsendpoint="https://api.sabaapp.co/v0/events";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
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

                            user_idlist.clear();
                            event_namelist.clear();
                            event_timelist.clear();
                            event_timeUnixlist.clear();
                            event_locationlist.clear();
                            event_budgetlist.clear();
                            budget_spentlist.clear();
                            setup_statuslist.clear();
                            event_statuslist.clear();
                            planner_idlist.clear();
                            event_image_idlist.clear();
                            event_image_locationlist.clear();
                            image_encodedlist.clear();
                            time_setuplist.clear();
                            event_idlist.clear();

                            eventwholearray.clear();

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

                                        user_idlist.add(app.getApiusername());
                                        event_namelist.add("Add Event");
                                        event_timelist.add(null);
                                        event_timeUnixlist.add(null);
                                        event_locationlist.add("Tap to add a new event");
                                        event_budgetlist.add(null);
                                        budget_spentlist.add(null);
                                        setup_statuslist.add(null);
                                        event_statuslist.add("draft");
                                        planner_idlist.add(null);
                                        event_image_idlist.add(null);
                                        event_image_locationlist.add(null);
                                        image_encodedlist.add(null);
                                        time_setuplist.add(null);
                                        event_idlist.add(null);

                                        //eventwholearray.clear();

                                        for(Integer i=0; i<event_namelist.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.seteventName(event_namelist.get(i));

                                            item.seteventUserid(user_idlist.get(i));

                                            item.seteventTime(event_timelist.get(i));
                                            item.seteventTimeUnix(event_timeUnixlist.get(i));
                                            item.seteventLocation(event_locationlist.get(i));
                                            item.seteventBudget(event_budgetlist.get(i));
                                            item.setbudgetSpent(budget_spentlist.get(i));
                                            item.seteventStatus(setup_statuslist.get(i));
                                            item.seteventStatus(event_statuslist.get(i));
                                            item.setplannerId(planner_idlist.get(i));
                                            item.seteventimageId(event_image_idlist.get(i));
                                            item.seteventimageLocation(event_image_locationlist.get(i));
                                            item.seteventimageEncoded(image_encodedlist.get(i));
                                            item.settime_Setup(time_setuplist.get(i));
                                            item.setevent_Id(event_idlist.get(i));


                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();



                                        //end of their are no values to add
                                    }else{

                                        for (int i = 0; i < dataobj.length(); i++) {
                                            jsonObj = dataobj.getJSONObject(i);

                                            String eventuserid = jsonObj.getString("user_id");
                                            String eventname = jsonObj.getString("event_name");
                                            String eventtime= jsonObj.getString("event_time");
                                            String formattedEventTime = null;

                                            if (isValidUnixTimestamp(eventtime)) {
                                                formattedEventTime = convertUnixToDateTime(eventtime);
                                            } else {
                                                Log.w("EVENT_TIME", "Invalid Unix timestamp: " + eventtime);
                                            }
                                            String eventlocation = jsonObj.getString("event_location");

                                            String eventbudget =jsonObj.getString("event_budget");
                                            String budgetspent =jsonObj.getString("budget_spent");

                                            String setupstatus =jsonObj.getString("setup_status");
                                            String eventstatus =jsonObj.getString("event_status");
                                            String plannerid =jsonObj.getString("planner_id");

                                            String eventimageid =jsonObj.getString("event_image_id");
                                            String eventimagelocation=jsonObj.getString("event_image_location");
                                            String eventimageencoded =jsonObj.getString("image_encoded");
                                            String time_setup =jsonObj.getString("time_setup");
                                            String event_id =jsonObj.getString("event_id");


                                            user_idlist.add(eventuserid);
                                            event_namelist.add(eventname);
                                            event_timelist.add(formattedEventTime);
                                            event_timeUnixlist.add(eventtime);
                                            event_locationlist.add(eventlocation);
                                            event_budgetlist.add(eventbudget);
                                            budget_spentlist.add(budgetspent);
                                            setup_statuslist.add(setupstatus);
                                            event_statuslist.add(eventstatus);
                                            planner_idlist.add(plannerid);
                                            event_image_idlist.add(eventimageid);
                                            event_image_locationlist.add(eventimagelocation);
                                            image_encodedlist.add(eventimageencoded);
                                            time_setuplist.add(time_setup);
                                            event_idlist.add(event_id);



                                            //for

                                            Log.d("Added Item", eventname + " To products array list");
                                        }



                                        eventwholearray.clear();


                                        //Integer i=0; i<event_namelist.size(); i++

                                        for(int i = event_namelist.size() - 1; i >= 0; i--)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.seteventName(event_namelist.get(i));

                                            item.seteventUserid(user_idlist.get(i));

                                            item.seteventTime(event_timelist.get(i));
                                            item.seteventTimeUnix(event_timeUnixlist.get(i));
                                            item.seteventLocation(event_locationlist.get(i));
                                            item.seteventBudget(event_budgetlist.get(i));
                                            item.setbudgetSpent(budget_spentlist.get(i));
                                            item.seteventStatus(setup_statuslist.get(i));
                                            item.seteventStatus(event_statuslist.get(i));
                                            item.setplannerId(planner_idlist.get(i));
                                            item.seteventimageId(event_image_idlist.get(i));
                                            item.seteventimageLocation(event_image_locationlist.get(i));
                                            item.seteventimageEncoded(image_encodedlist.get(i));
                                            item.settime_Setup(time_setuplist.get(i));
                                            item.setevent_Id(event_idlist.get(i));


                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();

                                        app.setSabaEventlist( eventwholearray);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : event_namelist)
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
                String apiusername = globalapiusername;
                String apipassword = globalapipassword;


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

    private void getServicesList()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());


        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/capabilities";



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
                                        eventwholearray.add(null);

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

    private boolean isValidUnixTimestamp(String value) {
        if (value == null) return false;
        if (!value.matches("\\d+")) return false;

        return value.length() == 10 || value.length() == 13;
    }

    private String convertUnixToDateTime(String timestamp) {
        try {
            long time = Long.parseLong(timestamp);

            // If seconds → convert to milliseconds
            if (timestamp.length() == 10) {
                time *= 1000;
            }

            Date date = new Date(time);

            SimpleDateFormat sdf =
                    new SimpleDateFormat("HH:mm dd-MM-yyyy ", Locale.getDefault());

            return sdf.format(date);

        } catch (Exception e) {
            return null;
        }
    }

}