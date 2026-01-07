package com.sabapp.saba.events;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.capabilityRecyclerAdapter;
import com.sabapp.saba.adapters.vendormatchingRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.data.model.SelectedVendorItem;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class vendormatching extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;



    RecyclerView servicesselected;

    ImageView backbuttonImage;

    LinearLayout nextbuttonLayout;

    String EventId,eventBudgetvalue;

    ArrayList<String> capability_codeList;

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

    ArrayList<sabaEventItem> eventwholearray;

    vendormatchingRecyclerAdapter sabaeventsadapter;

    ArrayList<SelectedCapabilityItem> capabilityList;

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
        Intent intent2 = new Intent(vendormatching.this, sabaDrawerActivity.class);
        //intent2.putStringArrayListExtra("selected_services", selectedCapabilities);
        //intent2.putExtra("selected_services", selected);
        intent2.putExtra("event_id", EventId);
        startActivity(intent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendormatchingevent);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressindicator);
        app = (sabaapp) getApplicationContext();

        servicesselected = (RecyclerView)findViewById(R.id.selectableoptions);

        backbuttonImage = (ImageView)findViewById(R.id.backbuttonimage);

        nextbuttonLayout = (LinearLayout)findViewById(R.id.nextbuttonlayout);

        Intent intent = getIntent();

        EventId = intent.getStringExtra("event_id");

        eventBudgetvalue = intent.getStringExtra("budget");

        Serializable serializable = getIntent().getSerializableExtra("selected_services");
        if (serializable instanceof ArrayList<?>) {
            capabilityList =
                    (ArrayList<SelectedCapabilityItem>) serializable;

            // Now you can use this list to populate a RecyclerView
                /*budgetlistRecycler.setLayoutManager(new LinearLayoutManager(this));
                SelectedCapabilityAdapter adapter = new SelectedCapabilityAdapter(capabilityList, this);
                budgetlistRecycler.setAdapter(adapter);*/

            // Optional: Log to check
            for (SelectedCapabilityItem item : capabilityList) {
                Log.d("CAPABILITY_ITEM", item.getCode() + " - " + item.getName());
            }
        }

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


        servicesselected.setLayoutManager(new LinearLayoutManager(vendormatching.this, LinearLayoutManager.VERTICAL, false));

        sabaeventsadapter=new vendormatchingRecyclerAdapter(eventwholearray,context, vendormatching.this, app);
        servicesselected.setAdapter(sabaeventsadapter);

        sendpostRequest();

        backbuttonImage.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), sabaDrawerActivity.class));


        });



        nextbuttonLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            /*ArrayList<String> selectedCapabilities =
                    sabaeventsadapter.getSelectedCapabilityNames();*/


            HashMap<String, String> selected =
                    sabaeventsadapter.getSelectedCapabilities();

            if (selected.isEmpty()) {
                Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("SELECTED VENDORS CHOOSE", String.valueOf(selected));

            ArrayList<SelectedVendorItem> vendorList = new ArrayList<>();

            for (Map.Entry<String, String> entry : selected.entrySet()) {
                SelectedVendorItem item =
                        new SelectedVendorItem(entry.getKey(), entry.getValue());
                vendorList.add(item);
            }




            Intent intent2 = new Intent(vendormatching.this, finalizesetup.class);
            //intent2.putStringArrayListExtra("selected_services", selectedCapabilities);
            intent2.putExtra("event_id", EventId);
            intent2.putExtra("budget", eventBudgetvalue);
            intent2.putExtra("selected_services", (Serializable) capabilityList);
            intent2.putExtra("selected_vendors", vendorList);

            startActivity(intent2);



        });








    //end of oncreate
    }


    private void sendpostRequest()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        JSONObject payload = new JSONObject();

        try {
            // =============================
            // ROOT PAYLOAD
            // =============================
            payload.put("event_amountspent", "0");
            payload.put("event_id", EventId);
            payload.put("event_budget", eventBudgetvalue);

            // =============================
            // BUILD event_budgetitems ARRAY
            // =============================
            JSONArray capabilityCodesArray = new JSONArray();
            for (SelectedCapabilityItem item : capabilityList) {
                capabilityCodesArray.put(item.getCode());
            }

            payload.put("event_capabilities", capabilityCodesArray);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/events/vendors";

        Log.d("SENDING MATCH PAYLOAD", String.valueOf(payload));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, payload,
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




                                    dataobj = jsonObj.getJSONArray("DATA");

                                    Log.d("Receive VENDOR DATA", String.valueOf(dataobj));

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

                                        vendornameList.add("No vendor loaded");
                                        vendorcapabilitynameList.add(null);
                                        vendorlocationList.add("Please try later");


                                        eventwholearray.clear();

                                        sabaEventItem item=new sabaEventItem();
                                        item.setcapability_code(null);
                                        item.setcapability_name("No services");
                                        item.setcapability_category(null);
                                        item.setcapability_imageid(null);
                                        item.setcapability_image_location(null);

                                        eventwholearray.add(item);

                                        sabaeventsadapter.notifyDataSetChanged();


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




                                        eventwholearray.clear();

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
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();

                                        app.setCapabilitylist( eventwholearray);


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

                                    AlertDialog ad = new AlertDialog.Builder(vendormatching.this)
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
