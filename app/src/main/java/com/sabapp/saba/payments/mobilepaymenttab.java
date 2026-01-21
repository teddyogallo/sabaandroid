package com.sabapp.saba.payments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.sabapp.saba.application.sabaapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.sabapp.saba.R;
import com.sabapp.saba.sabaDrawerActivity;

public class mobilepaymenttab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppCompatSpinner spinner;
    private ArrayList<String> systemmenulist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MaterialButton sharedbutton;
    Context mContext;
    String recipient_number;
    String customer_joincode;
    String system_selected;
    String recipientGlob;
    String amountrequestedGlob;
    String payment_typeGlob;
    TextView tabtitle;
    ArrayList<String> productnameList;
    ArrayList<String> productpriceList;

    ArrayList<String> customerfullnameList;
    ArrayList<String> cutomerphonenumberList;
    String globalusername;
    String globalapi_password;
    String globaluser_token;

    private TextView businessnumber;
    //private TextView businessjoincode;
    private EditText customerphonenumber;
    //private EditText saledescription;
    private EditText amountrequested;
    private MaterialButton requestPaymentButton;

    sabaapp app;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }
    public mobilepaymenttab() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (sabaapp) mContext.getApplicationContext();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView=inflater.inflate(R.layout.mobilepaymenttab, container, false);

        String recipient_idthis= app.getRecipientid();
        tabtitle = rootView.findViewById(R.id.tabtitle);

        tabtitle.setText("Request instant payment");
        globalusername = app.getApiusername();
        globalapi_password = app.getApipassword();
        globaluser_token =  app.getApipassword();



        if(recipient_idthis.equals("")){
            recipient_idthis = "254797318700";


        }

        productnameList  = new ArrayList<String>();
        productpriceList  = new ArrayList<String>();
        customerfullnameList  = new ArrayList<String>();
        cutomerphonenumberList  = new ArrayList<String>();

        recipient_number = recipient_idthis;
        customerphonenumber = rootView.findViewById(R.id.paymentnumbertextautocomplete);
        //businessjoincode =  rootView.findViewById(R.id.joincodetext);
        //customerphonenumber =  rootView.findViewById(R.id.paymentnumbertext);
        //saledescription =  rootView.findViewById(R.id.paymentdescriptiontext);
        amountrequested = rootView.findViewById(R.id.amountrequestedtext);


        requestPaymentButton = rootView.findViewById(R.id.requestinstantpaymentbutton);


        //fill for customer autocompleted






        //end of fill for customer autocompleted



        rootView.findViewById(R.id.requestinstantpaymentbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openWhatsAppConversationUsingUri(mContext,customer_joincode,recipient_number);
                if (system_selected == null || system_selected.isEmpty() || system_selected.trim().isEmpty()) {

                    Toast.makeText(mContext, "Please select option to Continue", Toast.LENGTH_LONG).show();

                }else if (amountrequested.getText().toString() == null || amountrequested.getText().toString().isEmpty() || amountrequested.getText().toString().trim().isEmpty() ||amountrequested.getText().toString().matches("")) {

                    Toast.makeText(mContext, "Please enter the payment amount to continue", Toast.LENGTH_LONG).show();

                }else if (customerphonenumber.getText().toString() == null || customerphonenumber.getText().toString().isEmpty() || customerphonenumber.getText().toString().trim().isEmpty() ||customerphonenumber.getText().toString().matches("")) {

                    Toast.makeText(mContext, "Please enter the payment amount to continue", Toast.LENGTH_LONG).show();

                }else{

                    postToAPI();
                }


            }
        });
        // action if delete menu option is selected


        spinner = rootView.findViewById(R.id.paymentsystem_menuspinner);



        //getProductlist();

        systemmenulist= new ArrayList<String>();
        //populate system menu
        systemmenulist.add("M-PESA");
        systemmenulist.add("MTN MOMO - Uganda");
        systemmenulist.add("Airtel Money- Uganda");
        systemmenulist.add("USSD Banking- Nigeria");


        ArrayAdapter<String> adaptertwo = new ArrayAdapter<String>(mContext,
                R.layout.spinner_item, systemmenulist);
        adaptertwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptertwo);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    Log.e("System Menu Selected", item.toString());
                    system_selected=item.toString();
                }


            } @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        return rootView;

    }


    private void postToAPI()
    {

        requestPaymentButton.setEnabled(false);
        requestPaymentButton.setText("Processing request...");

        String phonenum;
        String paymentdescription;
        String amountrequestedtext;


        //paymentdescription = saledescription.getText().toString();





        amountrequestedtext = amountrequested.getText().toString();

        amountrequestedGlob = amountrequestedtext;
        payment_typeGlob = system_selected;

        if(customerphonenumber.getText().toString().startsWith("+"))
        {
            phonenum=customerphonenumber.getText().toString();
            recipientGlob= phonenum;
        }
        else
        {
            phonenum=customerphonenumber.getText().toString();
            recipientGlob =phonenum;


        }





        Log.e("Submitted User ID", globalusername);
        Log.e("Payment option selected", system_selected);
        Log.e("Submitted Phone number", phonenum);

        Log.e("Amount requested", amountrequestedtext);


        HashMap<String, String> paramsotpu = new HashMap<String, String>();
        String paymentsendpoint = "https://api.wayawaya.com/v1/payments/request/mpesastk";
        if(system_selected.equals("M-PESA")){


            paramsotpu.put("username", globalusername);
            paramsotpu.put("amount", amountrequestedtext);

            paramsotpu.put("charge_phone", phonenum);

            paymentsendpoint = "https://api.wayawaya.com/v1/payments/request/mpesastk";

        }else if(system_selected.equals("MTN MOMO - Uganda")){
            paramsotpu.put("username", globalusername);
            paramsotpu.put("amount", amountrequestedtext);

            paramsotpu.put("charge_phone", phonenum);
            paramsotpu.put("charge_type", "mtn_uganda");

            paymentsendpoint = "https://api.wayawaya.com/v1/payments/request/debitugwallet";

        }else if(system_selected.equals("Airtel Money- Uganda")){
            paramsotpu.put("username", globalusername);
            paramsotpu.put("amount", amountrequestedtext);

            paramsotpu.put("charge_phone", phonenum);
            paramsotpu.put("charge_type", "airtel_uganda");

            paymentsendpoint = "https://api.wayawaya.com/v1/payments/request/debitugwallet";

        }else if(system_selected.equals("USSD Banking- Nigeria")){
            paramsotpu.put("username", globalusername);
            paramsotpu.put("amount", amountrequestedtext);

            paramsotpu.put("charge_phone", phonenum);

            paymentsendpoint = "https://api.wayawaya.com/v1/payments/request/mpesastk";

        }else {

            paramsotpu.put("username", globalusername);
            paramsotpu.put("amount", amountrequestedtext);

            paramsotpu.put("charge_phone", phonenum);

            paymentsendpoint = "https://api.wayawaya.com/v1/payments/request/mpesastk";

        }




        Log.e("response", "response "+phonenum);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response decoded", "response "+response.toString());
                        //hideProgressBar();
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
                                Integer response_code = jsonObj.getInt("CODE");
                                if(message.toLowerCase().matches("success"))
                                {

                                    requestPaymentButton.setEnabled(true);
                                    requestPaymentButton.setText("Request started");

                                    String wayawayarequest_reference =jsonObj.getString("REFERENCE");
                                    String providerrequest_id =jsonObj.getString("REQUEST_ID");
                                    //Toast.makeText(mContext, "Failed :"+messagedetails, Toast.LENGTH_LONG).show();

                                    AlertDialog ad = new AlertDialog.Builder(mContext)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request processed");
                                    ad.setMessage("We have sent a "+amountrequestedGlob+" payment request to your customers' "+system_selected+" number "+recipientGlob+", you will receive a notification once the customer completes payment");
                                    ad.setButton(mContext.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();


                                        }
                                    });
                                    ad.show();

                                }else if(response_code==2){

                                    requestPaymentButton.setEnabled(true);
                                    requestPaymentButton.setText("Request Failed");


                                    AlertDialog ad = new AlertDialog.Builder(mContext)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(mContext.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            //dialog.dismiss();

                                            Intent intent =  new Intent(mContext, sabaDrawerActivity.class);
                                            mContext.startActivity(intent);


                                        }
                                    });
                                    ad.show();

                                }else if(response_code==3){

                                    requestPaymentButton.setEnabled(true);
                                    requestPaymentButton.setText("Request Failed");


                                    AlertDialog ad = new AlertDialog.Builder(mContext)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(mContext.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            //dialog.dismiss();

                                            Intent intent =  new Intent(mContext, sabaDrawerActivity.class);
                                            mContext.startActivity(intent);


                                        }
                                    });
                                    ad.show();

                                }
                                else
                                {

                                    requestPaymentButton.setEnabled(true);
                                    requestPaymentButton.setText("Request Failed");

                                    Toast.makeText(mContext, "Failed :"+messagedetails, Toast.LENGTH_LONG).show();

                                    AlertDialog ad = new AlertDialog.Builder(mContext)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(mContext.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

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
                            //hideProgressBar();

                        } else {
                            // hideProgressBar();
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                requestPaymentButton.setEnabled(true);
                requestPaymentButton.setText("Request Error..");
                //hideProgressBar();
                //continuetonextbutton.setText("Upload to your store");
                Log.e("error is ", "" + error);
                Toast.makeText(mContext, "Their was an application network error, please try again", Toast.LENGTH_LONG).show();

                /*SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(mContext);
                sharedPrefsXtreme.saveData("janjaprofileskip","true");
                Intent intent =  new Intent(mContext, wayawayaDrawerActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);*/

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
                String apiusername = globalusername;
                String apipassword = globalapi_password;

                String creds = String.format("%s:%s",apiusername,apipassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);


    }




}
