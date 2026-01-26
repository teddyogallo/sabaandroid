package com.sabapp.saba.events;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

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
import com.sabapp.saba.adapters.SelectedCapabilityAdapter;
import com.sabapp.saba.adapters.capabilityRecyclerAdapter;
import com.sabapp.saba.adapters.sabaeventlistclientHomeRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.homeclientFragment;
import com.sabapp.saba.onboarding.chooselogintype;
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

public class chooseservices extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;



    RecyclerView servicesselected;

    ImageView backbuttonImage;

    LinearLayout nextbuttonLayout;

    String EventId;

    ArrayList<String> capability_codeList;
    ArrayList<String> capability_nameList;
    ArrayList<String> categoryList;
    ArrayList<String> capability_imageidList;
    ArrayList<String> capability_image_locationList;

    ArrayList<sabaEventItem> eventwholearray;

    capabilityRecyclerAdapter sabaeventsadapter;

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
        setContentView(R.layout.chooseservices);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressindicator);
        app = (sabaapp) getApplicationContext();

        Intent intent=getIntent();

        EventId=intent.getStringExtra("event_id");

        servicesselected = (RecyclerView)findViewById(R.id.selectableoptions);

        backbuttonImage = (ImageView) findViewById(R.id.backbuttonimage);


        nextbuttonLayout = (LinearLayout)findViewById(R.id.nextbuttonlayout);

        eventwholearray = new ArrayList<sabaEventItem>();


        capability_codeList = new ArrayList<String>();
        capability_nameList = new ArrayList<String>();
        categoryList = new ArrayList<String>();
        capability_imageidList = new ArrayList<String>();
        capability_image_locationList = new ArrayList<String>();

        servicesselected.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        sabaeventsadapter=new capabilityRecyclerAdapter(eventwholearray,context, chooseservices.this, app);
        servicesselected.setAdapter(sabaeventsadapter);

        backbuttonImage.setEnabled(false);
        nextbuttonLayout.setEnabled(false);



        getCapabilitylist();



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

            Log.d("SELECTED SERVICES CHOOSE", String.valueOf(selected));



            Intent intent2 = new Intent(chooseservices.this, setupbudgets.class);
            //intent2.putStringArrayListExtra("selected_services", selectedCapabilities);
            intent2.putExtra("selected_services", selected);
            intent2.putExtra("event_id", EventId);
            startActivity(intent2);


        });




        //end of oncreate
    }


    private void getCapabilitylist()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());


        String paymentsendpoint="https://api.sabaapp.co/v0/vendors/capabilities";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL Products: "+response.toString());
                        hideProgressBar();
                        backbuttonImage.setEnabled(true);
                        nextbuttonLayout.setEnabled(true);
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;


                            capability_codeList.clear();
                            capability_nameList.clear();
                            categoryList.clear();
                            capability_imageidList.clear();
                            capability_image_locationList.clear();
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

                                        capability_codeList.add(null);
                                        capability_nameList.add("No services");;
                                        categoryList.add("No services loaded");
                                        capability_imageidList.add(null);
                                        capability_image_locationList.add(null);

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


                                            String capability_code = jsonObj.getString("capability_code");
                                            String capability_name = jsonObj.getString("capability_name");
                                            String category= jsonObj.getString("category");
                                            String capability_imageid = jsonObj.getString("capability_imageid");

                                            String capability_image_location =jsonObj.getString("capability_image_location");


                                            capability_codeList.add(capability_code);
                                            capability_nameList.add(capability_name);
                                            categoryList.add(category);
                                            capability_imageidList.add(capability_imageid);
                                            capability_image_locationList.add(capability_image_location);

                                            //for

                                            Log.d("Added Capability", capability_name + " To products array list");
                                        }



                                        eventwholearray.clear();

                                        for(Integer i=0; i<capability_nameList.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();


                                            item.setcapability_code(capability_codeList.get(i));
                                            item.setcapability_name(capability_nameList.get(i));
                                            item.setcapability_category(categoryList.get(i));
                                            item.setcapability_imageid(capability_imageidList.get(i));
                                            item.setcapability_image_location(capability_image_locationList.get(i));


                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();

                                        app.setCapabilitylist( eventwholearray);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : capability_nameList)
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
                            backbuttonImage.setEnabled(true);
                            nextbuttonLayout.setEnabled(true);

                        } else {
                            hideProgressBar();
                            backbuttonImage.setEnabled(true);
                            nextbuttonLayout.setEnabled(true);
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                backbuttonImage.setEnabled(true);
                nextbuttonLayout.setEnabled(true);
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
