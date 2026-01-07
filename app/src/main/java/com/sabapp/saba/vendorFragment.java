package com.sabapp.saba;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.Intent;
import android.os.Bundle;

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
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.vendors.addcatalogue;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public vendorFragment() {
        // Required empty public constructor
    }


    ArrayList<String> base_pricelist;
    ArrayList<JSONObject> capability_detailslist;
    ArrayList<String> capability_idlist;
    ArrayList<String> service_image_locationlist;

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


    //for services
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

    ArrayList<sabaEventItem> serviceswholearray;

    vendorassignmentsRecyclerAdapter sabaeventsadapter;

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



    private void setUpRecycler()
    {



        eventlistrecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        sabaeventsadapter=new vendorassignmentsRecyclerAdapter(eventwholearray,context, vendorFragment.this, app);
        eventlistrecyclerview.setAdapter(sabaeventsadapter);


        //setup services whole array


        servicesofferedrecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        servicesofferAdapter=new servicesOfferedRecyclerAdapter(serviceswholearray,context, vendorFragment.this, app);
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

        //FOR CAPABILITIES

        base_pricelist = new ArrayList<String>();
        capability_detailslist = new ArrayList<JSONObject>();
        capability_idlist = new ArrayList<String>();
        service_image_locationlist = new ArrayList<String>();

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



        //FOR SERVICES

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

        //for services offered recycler



        createEventLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(getContext(), "Create New Event clicked", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), addcatalogue.class);
            intent.putExtra("createadrad", "gotowhatsappbotmaker");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            getActivity().startActivity(intent);





        });

        messagesquicklink.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            Toast.makeText(getContext(), "Messages link clicked", Toast.LENGTH_SHORT).show();


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


                                            event_id.add(vendor_idvalue);
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

                                        for(Integer i=0; i<event_id.size(); i++)
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
}