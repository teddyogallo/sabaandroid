package com.sabapp.saba.events;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class setupbudgets extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String EventId;

    EditText budgetamountEdit;

    TextView amountspent, valuededicatedversusspent;

    RecyclerView budgetlistRecycler;
    ImageView backbuttonImage;

    LinearLayout nextbuttonLayoutButton;

    List<SelectedCapabilityItem> capabilityList;

    String eventBudgetvalue;


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
        Intent intent2 =  new Intent(setupbudgets.this, sabaDrawerActivity.class);
        intent2.putExtra("event_id", EventId);
        intent2.setFlags(intent2.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setbudgetlayout);

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressindicator);
        app = (sabaapp) getApplicationContext();

        Intent intent = getIntent();

        EventId = intent.getStringExtra("event_id");

        budgetamountEdit = (EditText)findViewById(R.id.budgetamounttext);

        budgetlistRecycler = (RecyclerView)findViewById(R.id.selectableoptions) ;

        budgetamountEdit = (EditText) findViewById(R.id.budgetamounttext);

        amountspent = (TextView)findViewById(R.id.budgetspentText);
        valuededicatedversusspent = (TextView)findViewById(R.id.totalbudgettext);



        backbuttonImage = (ImageView) findViewById(R.id.backbuttonimage);

        nextbuttonLayoutButton = (LinearLayout)findViewById(R.id.nextbutton_layout);


        Bundle extras = intent.getExtras();
        if (extras != null) {
            Serializable serializable = extras.getSerializable("selected_services"); // make sure the key matches what you sent
            if (serializable instanceof HashMap) {
                HashMap<String, String> selected = (HashMap<String, String>) serializable;
                Log.d("SELECTED_CAPS", selected.toString());

                // Convert to a List of SelectedCapabilityItem
                capabilityList = new ArrayList<>();
                for (Map.Entry<String, String> entry : selected.entrySet()) {
                    capabilityList.add(new SelectedCapabilityItem(entry.getKey(), entry.getValue()));
                }

                // Set up RecyclerView
                budgetlistRecycler.setLayoutManager(new LinearLayoutManager(this));
                SelectedCapabilityAdapter adapter = new SelectedCapabilityAdapter(capabilityList, this);
                budgetlistRecycler.setAdapter(adapter);


            }
        }






        backbuttonImage.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(setupbudgets.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            Intent intent2 =  new Intent(setupbudgets.this, sabaDrawerActivity.class);
            intent2.putExtra("event_id", EventId);
            intent2.setFlags(intent2.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent2);


        });

        budgetamountEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {

                String typedValue = s.toString().trim();

                if (typedValue.isEmpty()) {
                    valuededicatedversusspent.setText("$0/ $0 Spent");
                } else {
                    valuededicatedversusspent.setText("$"+typedValue + "/ $0 ");
                }
            }
        });



        nextbuttonLayoutButton.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(setupbudgets.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            if(budgetamountEdit.getText().toString().matches(""))
            {
                //bizdesclay.setError("Business Description cannot be empty");

                Toast.makeText(setupbudgets.this, "Budget amount should not be empty", Toast.LENGTH_SHORT).show();

            }

            else {

                eventBudgetvalue = budgetamountEdit.getText().toString();


                sendpostRequest();


            }




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
            JSONArray budgetItemsArray = new JSONArray();

            JSONObject budgetItem = new JSONObject();
            budgetItem.put("capability_id", null);   // replace dynamically
            budgetItem.put("allocated_amount", null);
            budgetItem.put("amount_paid", null);
            budgetItem.put("currency", null);
            budgetItem.put("payment_status", null);
            budgetItem.put("last_payment_date", null);
            budgetItem.put("notes", null);

            budgetItemsArray.put(budgetItem);

            payload.put("event_budgetitems", budgetItemsArray);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/events/budget";



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




                                    Log.d("EVENT ID", EventId);


                                    Intent intent = new Intent(setupbudgets.this, vendormatching.class);
                                    intent.putExtra("event_id", EventId);
                                    intent.putExtra("budget", eventBudgetvalue);
                                    intent.putExtra("selected_services", (Serializable) capabilityList);
                                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    setupbudgets.this.startActivity(intent);




                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    AlertDialog ad = new AlertDialog.Builder(setupbudgets.this)
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
