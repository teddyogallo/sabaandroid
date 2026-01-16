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
import com.sabapp.saba.adapters.sabaeventlistclientHomeRecyclerAdapter;
import com.sabapp.saba.adapters.servicesOfferedRecyclerAdapter;
import com.sabapp.saba.adapters.vendorassignmentsRecyclerAdapter;
import com.sabapp.saba.adapters.vendorproposalsRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.messaging.messagestartactivity;
import com.sabapp.saba.vendors.addcatalogue;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vendorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vendorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;

    public vendorFragment() {
        // Required empty public constructor
    }




    ArrayList<String> event_id;
    ArrayList<String> vendor_id;
    ArrayList<String>  capability_id;
    ArrayList<String> status;
    ArrayList<String> agreed_price;
    ArrayList<JSONObject> contract_terms;
    ArrayList<String> assigned_by;
    ArrayList<String> time_assigned;
    ArrayList<String> event_name;
    ArrayList<String> eventimagelocationlist;


    //for proposals

    ArrayList<String> base_priceProposallist;
    ArrayList<JSONObject> capability_detailsProposallist;
    ArrayList<String> capability_idProposallist;
    ArrayList<String> service_image_locationProposallist;

    ArrayList<String> event_idProposal;
    ArrayList<String> vendor_idProposal;
    ArrayList<String>  capability_idProposal;
    ArrayList<String> statusProposal;
    ArrayList<String> agreed_priceProposal;
    ArrayList<JSONObject> contract_termsProposal;
    ArrayList<String> assigned_byProposal;
    ArrayList<String> time_assignedProposal;
    ArrayList<String> event_nameProposal;
    ArrayList<String> eventimagelocationlistProposal;


    ArrayList<String> eventcapability_nameProposal;
    ArrayList<String> depositbalance_percentageProposal;
    ArrayList<String> deposit_percentageProposal;
    ArrayList<String> event_allocatedtimeProposal;
    ArrayList<String> event_locationProposal;
    ArrayList<String> event_typeProposal;
    ArrayList<String> event_vibeProposal;
    ArrayList<String> eventcountryProposal;
    ArrayList<String> event_vendorbase_priceProposal;
    ArrayList<String> event_vendor_nameProposal;

    //end of for proposals


    //for services
    ArrayList<String> eventcapability_name;
    ArrayList<String> depositbalance_percentage;
    ArrayList<String> deposit_percentage;
    ArrayList<String> event_allocatedtime;
    ArrayList<String> event_location;
    ArrayList<String> event_type;
    ArrayList<String> event_vibe;
    ArrayList<String> eventcountry;
    ArrayList<String> event_vendorbase_price;
    ArrayList<String> event_vendor_name;

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

    LinearLayout createEventLayout ,messagesquicklink, paymentsquicklink, fevoritesquicklink;

    RecyclerView eventlistrecyclerview, servicesofferedrecycler;
    AVLoadingIndicatorView progressBar;

    ArrayList<sabaEventItem> eventwholearray;

    ArrayList<sabaEventItem> proposalswholearray;

    ArrayList<sabaEventItem> serviceswholearray;

    vendorassignmentsRecyclerAdapter sabaeventsadapter;

    vendorproposalsRecyclerAdapter proposalsAdapter;

    servicesOfferedRecyclerAdapter servicesofferAdapter;



    sabaapp app;

    JSONArray dataobj;

    int loggedinnumberGlobal =0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vendorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static vendorFragment newInstance(String param1, String param2) {
        vendorFragment fragment = new vendorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        getEventProposals();
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



    private void setUpRecycler()
    {



        eventlistrecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        sabaeventsadapter=new vendorassignmentsRecyclerAdapter(eventwholearray,context, vendorFragment.this, app);
        eventlistrecyclerview.setAdapter(sabaeventsadapter);


        //setup services whole array


        servicesofferedrecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        proposalsAdapter=new vendorproposalsRecyclerAdapter(proposalswholearray,context, vendorFragment.this, app);
        servicesofferedrecycler.setAdapter(proposalsAdapter);



        /*
        janjarecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new wwdashboardAdapter(templateArrays,context, dashboardFragment.this);
        janjarecycler.setAdapter(adapter);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor, container, false);

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
        proposalswholearray = new ArrayList<sabaEventItem>();

        //FOR Active events
        event_id = new ArrayList<String>();
        vendor_id = new ArrayList<String>();
        capability_id = new ArrayList<String>();
        status = new ArrayList<String>();
        agreed_price = new ArrayList<String>();
        contract_terms = new ArrayList<JSONObject>();
        assigned_by = new ArrayList<String>();
        time_assigned = new ArrayList<String>();
        event_name = new ArrayList<String>();
        eventimagelocationlist = new ArrayList<String>();


        eventcapability_name = new ArrayList<String>();
        depositbalance_percentage = new ArrayList<String>();
        deposit_percentage = new ArrayList<String>();
        event_allocatedtime = new ArrayList<String>();
        event_location = new ArrayList<String>();
        event_type = new ArrayList<String>();
        event_vibe = new ArrayList<String>();
        eventcountry = new ArrayList<String>();
        event_vendorbase_price = new ArrayList<String>();
        event_vendor_name = new ArrayList<String>();

        //for proposals
        base_priceProposallist = new ArrayList<String>();
        capability_detailsProposallist = new ArrayList<JSONObject>();
        capability_idProposallist = new ArrayList<String>();
        service_image_locationProposallist = new ArrayList<String>();

        event_idProposal = new ArrayList<String>();
        vendor_idProposal = new ArrayList<String>();
        capability_idProposal = new ArrayList<String>();
        statusProposal = new ArrayList<String>();
        agreed_priceProposal = new ArrayList<String>();
        contract_termsProposal = new ArrayList<JSONObject>();
        assigned_byProposal = new ArrayList<String>();
        time_assignedProposal = new ArrayList<String>();
        event_nameProposal = new ArrayList<String>();
        eventimagelocationlistProposal = new ArrayList<String>();

        eventcapability_nameProposal = new ArrayList<String>();
        depositbalance_percentageProposal = new ArrayList<String>();
        deposit_percentageProposal = new ArrayList<String>();
        event_allocatedtimeProposal = new ArrayList<String>();
        event_locationProposal = new ArrayList<String>();
        event_typeProposal = new ArrayList<String>();
        event_vibeProposal = new ArrayList<String>();
        eventcountryProposal = new ArrayList<String>();
        event_vendorbase_priceProposal = new ArrayList<String>();
        event_vendor_nameProposal = new ArrayList<String>();

        //end of for proposals



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

        //for services offered recycler



        createEventLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(getContext(), "Create New Event clicked", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), addcatalogue.class);
            intent.putExtra("createadrad", "gotowhatsappbotmaker");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);





        });

        messagesquicklink.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            Toast.makeText(getContext(), "Messages link clicked", Toast.LENGTH_SHORT).show();

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


        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/assignments";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL ASSIGMENTS: "+response.toString());
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

                                        event_id.add(null);
                                        vendor_id.add(null);
                                        capability_id.add(null);
                                        status.add(null);
                                        agreed_price.add(null);
                                        contract_terms.add(null);
                                        assigned_by.add(null);
                                        time_assigned.add(null);
                                        event_name.add("No event assigned");
                                        eventimagelocationlist.add(null);

                                        //new values
                                        eventcapability_name.add(null);
                                        depositbalance_percentage.add(null);
                                        deposit_percentage.add(null);
                                        event_allocatedtime.add(null);
                                        event_location.add(null);
                                        event_type.add(null);
                                        event_vibe.add(null);
                                        eventcountry.add(null);
                                        event_vendorbase_price.add(null);
                                        event_vendor_name.add(null);

                                        eventwholearray.clear();

                                        for(Integer i=0; i<event_name.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.setevent_idAssigned(event_id.get(i));

                                            item.setvendor_idAssigned(vendor_id.get(i));

                                            item.setcapability_idAssigned(capability_id.get(i));
                                            item.setstatusAssigned(status.get(i));
                                            item.setagreed_priceAssigned(agreed_price.get(i));
                                            item.setcontract_termsAssigned(contract_terms.get(i));
                                            item.setassigned_byAssigned(assigned_by.get(i));
                                            item.settime_assignedAssigned(time_assigned.get(i));
                                            item.setevent_nameAssigned(event_name.get(i));
                                            item.setevent_imagelocationAssigned(eventimagelocationlist.get(i));


                                            item.seteventcapability_nameAssigned(eventcapability_name.get(i));
                                            item.setdeposit_balance_percentageAssigned(depositbalance_percentage.get(i));
                                            item.setdeposit_percentageAssigned(deposit_percentage.get(i));
                                            item.setevent_allocated_timeAssigned(event_allocatedtime.get(i));
                                            item.setevent_locationAssigned(event_location.get(i));
                                            item.setevent_typeAssigned(event_type.get(i));
                                            item.setevent_vibeAssigned(event_vibe.get(i));
                                            item.setevent_countryAssigned(eventcountry.get(i));
                                            item.setevent_vendor_base_priceAssigned(event_vendorbase_price.get(i));
                                            item.setevent_vendor_nameAssigned(event_vendor_name.get(i));



                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();



                                        //end of their are no values to add
                                    }else{

                                        for (int i = 0; i < dataobj.length(); i++) {
                                            jsonObj = dataobj.getJSONObject(i);

                                            String event_idvalue = jsonObj.optString("event_id");
                                            String vendor_idvalue =jsonObj.optString("vendor_id");
                                            String capability_idvalue = jsonObj.optString("capability_id");
                                            String statusvalue = jsonObj.getString("status");
                                            String agreed_pricevalue = jsonObj.getString("agreed_price");
                                            JSONObject contract_termsvalue = jsonObj.optJSONObject("contract_terms");
                                            String assigned_byvalue = jsonObj.getString("assigned_by");
                                            String time_assignedvalue = jsonObj.getString("time_assigned");
                                            String event_namevalue = jsonObj.getString("event_name");
                                            String eventimagelocation = jsonObj.getString("event_image_location");

                                            String eventcapability_namevalue = jsonObj.getString("capability_name");
                                            String depositbalance_percentagevalue = jsonObj.getString("deposit_balance_percentage");
                                            String deposit_percentagevalue = jsonObj.getString("deposit_percentage");
                                            String event_allocatedtimevalue = jsonObj.getString("event_allocation_time");
                                            String event_cityvalue = jsonObj.getString("event_city");
                                            String eventcountryvalue = jsonObj.getString("event_country");
                                            String event_locationvalue = jsonObj.getString("event_location");
                                            String event_statevalue = jsonObj.getString("event_state");
                                            String event_typevalue = jsonObj.getString("event_type");
                                            String event_vibevalue = jsonObj.getString("event_vibe");
                                            String event_latitudevalue = jsonObj.getString("latitude");
                                            String event_longitudevalue = jsonObj.getString("longitude");
                                            String event_postal_codevalue =  jsonObj.getString("postal_code");
                                            String event_vendorbase_pricevalue = jsonObj.getString("vendor_base_price");
                                            String event_vendor_namevalue = jsonObj.getString("vendor_name");

                                            String formattedEventTime = null;

                                            if (isValidUnixTimestamp(event_allocatedtimevalue)) {
                                                formattedEventTime = convertUnixToDateTime(event_allocatedtimevalue);
                                            } else {
                                                Log.w("EVENT_TIME", "Invalid Unix timestamp: " + event_allocatedtimevalue);
                                            }


                                            eventcapability_name.add(eventcapability_namevalue);
                                            depositbalance_percentage.add(depositbalance_percentagevalue);
                                            deposit_percentage.add(deposit_percentagevalue);
                                            event_allocatedtime.add(formattedEventTime);
                                            event_location.add(event_locationvalue);
                                            event_type.add(event_typevalue);
                                            event_vibe.add(event_vibevalue);
                                            eventcountry.add(eventcountryvalue);
                                            event_vendorbase_price.add(event_vendorbase_pricevalue);
                                            event_vendor_name.add(event_vendor_namevalue);




                                            event_id.add(event_idvalue);
                                            vendor_id.add(vendor_idvalue);
                                            capability_id.add(capability_idvalue);
                                            status.add(statusvalue);
                                            agreed_price.add(agreed_pricevalue);
                                            contract_terms.add(contract_termsvalue);
                                            assigned_by.add(assigned_byvalue);
                                            time_assigned.add(time_assignedvalue);
                                            event_name.add(event_namevalue);
                                            eventimagelocationlist.add(eventimagelocation);







                                            //for

                                            Log.d("Added Item", event_id + " To events array list");
                                        }



                                        eventwholearray.clear();


                                        //Integer i=0; i<event_id.size(); i++

                                        for(int i = event_id.size() - 1; i >= 0; i--)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.setevent_idAssigned(event_id.get(i));

                                            item.setvendor_idAssigned(vendor_id.get(i));

                                            item.setcapability_idAssigned(capability_id.get(i));
                                            item.setstatusAssigned(status.get(i));
                                            item.setagreed_priceAssigned(agreed_price.get(i));
                                            item.setcontract_termsAssigned(contract_terms.get(i));
                                            item.setassigned_byAssigned(assigned_by.get(i));
                                            item.settime_assignedAssigned(time_assigned.get(i));
                                            item.setevent_nameAssigned(event_name.get(i));
                                            item.setevent_imagelocationAssigned(eventimagelocationlist.get(i));

                                            item.seteventcapability_nameAssigned(eventcapability_name.get(i));
                                            item.setdeposit_balance_percentageAssigned(depositbalance_percentage.get(i));
                                            item.setdeposit_percentageAssigned(deposit_percentage.get(i));
                                            item.setevent_allocated_timeAssigned(event_allocatedtime.get(i));
                                            item.setevent_locationAssigned(event_location.get(i));
                                            item.setevent_typeAssigned(event_type.get(i));
                                            item.setevent_vibeAssigned(event_vibe.get(i));
                                            item.setevent_countryAssigned(eventcountry.get(i));
                                            item.setevent_vendor_base_priceAssigned(event_vendorbase_price.get(i));
                                            item.setevent_vendor_nameAssigned(event_vendor_name.get(i));



                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();

                                        app.setSabaEventlist( eventwholearray);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : event_id)
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



                                        event_id.add(null);
                                        vendor_id.add(null);
                                        capability_id.add(null);
                                        status.add(null);
                                        agreed_price.add(null);
                                        contract_terms.add(null);
                                        assigned_by.add(null);
                                        time_assigned.add(null);
                                        event_name.add("No event found");
                                        eventimagelocationlist.add(null);

                                        eventcapability_name.add(null);
                                        depositbalance_percentage.add(null);
                                        deposit_percentage.add(null);
                                        event_allocatedtime.add(null);
                                        event_location.add(null);
                                        event_type.add(null);
                                        event_vibe.add(null);
                                        eventcountry.add(null);
                                        event_vendorbase_price.add(null);
                                        event_vendor_name.add(null);

                                        eventwholearray.clear();



                                        for(Integer i=0; i<event_name.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.setevent_idAssigned(event_id.get(i));

                                            item.setvendor_idAssigned(vendor_id.get(i));

                                            item.setcapability_idAssigned(capability_id.get(i));
                                            item.setstatusAssigned(status.get(i));
                                            item.setagreed_priceAssigned(agreed_price.get(i));
                                            item.setcontract_termsAssigned(contract_terms.get(i));
                                            item.setassigned_byAssigned(assigned_by.get(i));
                                            item.settime_assignedAssigned(time_assigned.get(i));
                                            item.setevent_nameAssigned(event_name.get(i));
                                            item.setevent_imagelocationAssigned(eventimagelocationlist.get(i));

                                            item.seteventcapability_nameAssigned(eventcapability_name.get(i));
                                            item.setdeposit_balance_percentageAssigned(depositbalance_percentage.get(i));
                                            item.setdeposit_percentageAssigned(deposit_percentage.get(i));
                                            item.setevent_allocated_timeAssigned(event_allocatedtime.get(i));
                                            item.setevent_locationAssigned(event_location.get(i));
                                            item.setevent_typeAssigned(event_type.get(i));
                                            item.setevent_vibeAssigned(event_vibe.get(i));
                                            item.setevent_countryAssigned(eventcountry.get(i));
                                            item.setevent_vendor_base_priceAssigned(event_vendorbase_price.get(i));
                                            item.setevent_vendor_nameAssigned(event_vendor_name.get(i));



                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();


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

    private void getEventProposals()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());


        String paymentsendpoint="https://api.sabaapp.co/v0/vendor/proposals";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL ASSIGMENTS: "+response.toString());
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

                                        event_idProposal.add(null);
                                        vendor_idProposal.add(null);
                                        capability_idProposal.add(null);
                                        statusProposal.add(null);
                                        agreed_priceProposal.add(null);
                                        contract_termsProposal.add(null);
                                        assigned_byProposal.add(null);
                                        time_assignedProposal.add(null);
                                        event_nameProposal.add("No event assigned");
                                        eventimagelocationlistProposal.add(null);

                                        eventcapability_nameProposal.add(null);
                                        depositbalance_percentageProposal.add(null);
                                        deposit_percentageProposal.add(null);
                                        event_allocatedtimeProposal.add(null);
                                        event_locationProposal.add(null);
                                        event_typeProposal.add(null);
                                        event_vibeProposal.add(null);
                                        eventcountryProposal.add(null);
                                        event_vendorbase_priceProposal.add(null);
                                        event_vendor_nameProposal.add(null);

                                        proposalswholearray.clear();

                                        for(Integer i=0; i<event_nameProposal.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.setevent_idAssignedProposal(event_idProposal.get(i));

                                            item.setvendor_idAssignedProposal(vendor_idProposal.get(i));

                                            item.setcapability_idAssignedProposal(capability_idProposal.get(i));
                                            item.setstatusAssignedProposal(statusProposal.get(i));
                                            item.setagreed_priceAssignedProposal(agreed_priceProposal.get(i));
                                            item.setcontract_termsAssignedProposal(contract_termsProposal.get(i));
                                            item.setassigned_byAssignedProposal(assigned_byProposal.get(i));
                                            item.settime_assignedAssignedProposal(time_assignedProposal.get(i));
                                            item.setevent_nameAssignedProposal(event_nameProposal.get(i));
                                            item.setevent_imagelocationAssignedProposal(eventimagelocationlistProposal.get(i));


                                            item.seteventcapability_nameAssignedProposal(eventcapability_nameProposal.get(i));
                                            item.setdeposit_balance_percentageAssignedProposal(depositbalance_percentageProposal.get(i));
                                            item.setdeposit_percentageAssignedProposal(deposit_percentageProposal.get(i));
                                            item.setevent_allocated_timeAssignedProposal(event_allocatedtimeProposal.get(i));
                                            item.setevent_locationAssignedProposal(event_locationProposal.get(i));
                                            item.setevent_typeAssignedProposal(event_typeProposal.get(i));
                                            item.setevent_vibeAssignedProposal(event_vibeProposal.get(i));
                                            item.setevent_countryAssignedProposal(eventcountryProposal.get(i));
                                            item.setevent_vendor_base_priceAssignedProposal(event_vendorbase_priceProposal.get(i));
                                            item.setevent_vendor_nameAssignedProposal(event_vendor_nameProposal.get(i));



                                            //setImagebitmap
                                            proposalswholearray.add(item);

                                        }
                                        proposalsAdapter.notifyDataSetChanged();



                                        //end of their are no values to add
                                    }else{

                                        for (int i = 0; i < dataobj.length(); i++) {
                                            jsonObj = dataobj.getJSONObject(i);

                                            String event_idvalue = jsonObj.optString("event_id");
                                            String vendor_idvalue =jsonObj.optString("vendor_id");
                                            String capability_idvalue = jsonObj.optString("capability_id");
                                            String statusvalue = jsonObj.getString("status");
                                            String agreed_pricevalue = jsonObj.getString("agreed_price");
                                            JSONObject contract_termsvalue = jsonObj.optJSONObject("contract_terms");
                                            String assigned_byvalue = jsonObj.getString("assigned_by");
                                            String time_assignedvalue = jsonObj.getString("time_assigned");
                                            String event_namevalue = jsonObj.getString("event_name");
                                            String eventimagelocation = jsonObj.getString("event_image_location");


                                            String eventcapability_namevalue = jsonObj.getString("capability_name");
                                            String depositbalance_percentagevalue = jsonObj.getString("deposit_balance_percentage");
                                            String deposit_percentagevalue = jsonObj.getString("deposit_percentage");
                                            String event_allocatedtimevalue = jsonObj.getString("event_allocation_time");
                                            String event_cityvalue = jsonObj.getString("event_city");
                                            String eventcountryvalue = jsonObj.getString("event_country");
                                            String event_locationvalue = jsonObj.getString("event_location");
                                            String event_statevalue = jsonObj.getString("event_state");
                                            String event_typevalue = jsonObj.getString("event_type");
                                            String event_vibevalue = jsonObj.getString("event_vibe");
                                            String event_latitudevalue = jsonObj.getString("latitude");
                                            String event_longitudevalue = jsonObj.getString("longitude");
                                            String event_postal_codevalue =  jsonObj.getString("postal_code");
                                            String event_vendorbase_pricevalue = jsonObj.getString("vendor_base_price");
                                            String event_vendor_namevalue = jsonObj.getString("vendor_name");


                                            String formattedEventTime = null;

                                            if (isValidUnixTimestamp(event_allocatedtimevalue)) {
                                                formattedEventTime = convertUnixToDateTime(event_allocatedtimevalue);
                                            } else {
                                                Log.w("EVENT_TIME", "Invalid Unix timestamp: " + event_allocatedtimevalue);
                                            }


                                            event_idProposal.add(event_idvalue);
                                            vendor_idProposal.add(vendor_idvalue);
                                            capability_idProposal.add(capability_idvalue);
                                            statusProposal.add(statusvalue);
                                            agreed_priceProposal.add(agreed_pricevalue);
                                            contract_termsProposal.add(contract_termsvalue);
                                            assigned_byProposal.add(assigned_byvalue);
                                            time_assignedProposal.add(time_assignedvalue);
                                            event_nameProposal.add(event_namevalue);
                                            eventimagelocationlistProposal.add(eventimagelocation);


                                            eventcapability_nameProposal.add(eventcapability_namevalue);
                                            depositbalance_percentageProposal.add(depositbalance_percentagevalue);
                                            deposit_percentageProposal.add(deposit_percentagevalue);
                                            event_allocatedtimeProposal.add(formattedEventTime);
                                            event_locationProposal.add(event_locationvalue);
                                            event_typeProposal.add(event_typevalue);
                                            event_vibeProposal.add(event_vibevalue);
                                            eventcountryProposal.add(eventcountryvalue);
                                            event_vendorbase_priceProposal.add(event_vendorbase_pricevalue);
                                            event_vendor_nameProposal.add(event_vendor_namevalue);




                                            //for

                                            Log.d("Added Item", event_id + " To proposals array list");
                                        }



                                        proposalswholearray.clear();



                                        //Integer i=0; i<event_idProposal.size(); i++

                                        for(int i = event_idProposal.size() - 1; i >= 0; i--)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.setevent_idAssignedProposal(event_idProposal.get(i));

                                            item.setvendor_idAssignedProposal(vendor_idProposal.get(i));

                                            item.setcapability_idAssignedProposal(capability_idProposal.get(i));
                                            item.setstatusAssignedProposal(statusProposal.get(i));
                                            item.setagreed_priceAssignedProposal(agreed_priceProposal.get(i));
                                            item.setcontract_termsAssignedProposal(contract_termsProposal.get(i));
                                            item.setassigned_byAssignedProposal(assigned_byProposal.get(i));
                                            item.settime_assignedAssignedProposal(time_assignedProposal.get(i));
                                            item.setevent_nameAssignedProposal(event_nameProposal.get(i));
                                            item.setevent_imagelocationAssignedProposal(eventimagelocationlistProposal.get(i));


                                            item.seteventcapability_nameAssignedProposal(eventcapability_nameProposal.get(i));
                                            item.setdeposit_balance_percentageAssignedProposal(depositbalance_percentageProposal.get(i));
                                            item.setdeposit_percentageAssignedProposal(deposit_percentageProposal.get(i));
                                            item.setevent_allocated_timeAssignedProposal(event_allocatedtimeProposal.get(i));
                                            item.setevent_locationAssignedProposal(event_locationProposal.get(i));
                                            item.setevent_typeAssignedProposal(event_typeProposal.get(i));
                                            item.setevent_vibeAssignedProposal(event_vibeProposal.get(i));
                                            item.setevent_countryAssignedProposal(eventcountryProposal.get(i));
                                            item.setevent_vendor_base_priceAssignedProposal(event_vendorbase_priceProposal.get(i));
                                            item.setevent_vendor_nameAssignedProposal(event_vendor_nameProposal.get(i));


                                            //setImagebitmap
                                            proposalswholearray.add(item);

                                        }
                                        proposalsAdapter.notifyDataSetChanged();

                                        app.setSabaEventlist( proposalswholearray);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : event_idProposal)
                                        {
                                            Log.d("Event Proposal Name value--", singleRecord.toString());
                                        }


                                        //adaptertwo.notifyDataSetChanged();

                                    }

                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    event_idProposal.add(null);
                                    vendor_idProposal.add(null);
                                    capability_idProposal.add(null);
                                    statusProposal.add(null);
                                    agreed_priceProposal.add(null);
                                    contract_termsProposal.add(null);
                                    assigned_byProposal.add(null);
                                    time_assignedProposal.add(null);
                                    event_nameProposal.add("No Proposals listed");
                                    eventimagelocationlistProposal.add(null);

                                    eventcapability_nameProposal.add(null);
                                    depositbalance_percentageProposal.add(null);
                                    deposit_percentageProposal.add(null);
                                    event_allocatedtimeProposal.add(null);
                                    event_locationProposal.add(null);
                                    event_typeProposal.add(null);
                                    event_vibeProposal.add(null);
                                    eventcountryProposal.add(null);
                                    event_vendorbase_priceProposal.add(null);
                                    event_vendor_nameProposal.add(null);

                                    proposalswholearray.clear();

                                    for(Integer i=0; i<event_nameProposal.size(); i++)
                                    {
                                        sabaEventItem item=new sabaEventItem();

                                        item.setevent_idAssignedProposal(event_idProposal.get(i));

                                        item.setvendor_idAssignedProposal(vendor_idProposal.get(i));

                                        item.setcapability_idAssignedProposal(capability_idProposal.get(i));
                                        item.setstatusAssignedProposal(statusProposal.get(i));
                                        item.setagreed_priceAssignedProposal(agreed_priceProposal.get(i));
                                        item.setcontract_termsAssignedProposal(contract_termsProposal.get(i));
                                        item.setassigned_byAssignedProposal(assigned_byProposal.get(i));
                                        item.settime_assignedAssignedProposal(time_assignedProposal.get(i));
                                        item.setevent_nameAssignedProposal(event_nameProposal.get(i));
                                        item.setevent_imagelocationAssignedProposal(eventimagelocationlistProposal.get(i));


                                        item.seteventcapability_nameAssignedProposal(eventcapability_nameProposal.get(i));
                                        item.setdeposit_balance_percentageAssignedProposal(depositbalance_percentageProposal.get(i));
                                        item.setdeposit_percentageAssignedProposal(deposit_percentageProposal.get(i));
                                        item.setevent_allocated_timeAssignedProposal(event_allocatedtimeProposal.get(i));
                                        item.setevent_locationAssignedProposal(event_locationProposal.get(i));
                                        item.setevent_typeAssignedProposal(event_typeProposal.get(i));
                                        item.setevent_vibeAssignedProposal(event_vibeProposal.get(i));
                                        item.setevent_countryAssignedProposal(eventcountryProposal.get(i));
                                        item.setevent_vendor_base_priceAssignedProposal(event_vendorbase_priceProposal.get(i));
                                        item.setevent_vendor_nameAssignedProposal(event_vendor_nameProposal.get(i));



                                        //setImagebitmap
                                        proposalswholearray.add(item);

                                    }
                                    proposalsAdapter.notifyDataSetChanged();

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