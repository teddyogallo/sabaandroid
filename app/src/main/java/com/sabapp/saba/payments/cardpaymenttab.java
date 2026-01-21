package com.sabapp.saba.payments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.paystack.android.ui.paymentsheet.PaymentSheetResult;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.paystack.android.core.Paystack;

import com.paystack.android.ui.paymentsheet.PaymentSheet;
import com.wang.avi.AVLoadingIndicatorView;


public class cardpaymenttab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;
    sabaapp app;

    TextView tabtitle;
    private EditText amountrequested;

    String globalusername;
    String globalapi_password;
    String globaluser_token;

    AVLoadingIndicatorView progressindicator;

    private PaymentSheet paymentSheet;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    public void showProgressBar()
    {
        progressindicator.smoothToShow();
    }
    public void hideProgressBar()
    {
        progressindicator.smoothToHide();
    }
    public cardpaymenttab() {
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
        View rootView= inflater.inflate(R.layout.cardpaymenttab, container, false);

        tabtitle = rootView.findViewById(R.id.tabtitle);

        progressindicator = rootView.findViewById(R.id.signinprogress);

        tabtitle.setText("Top up with card");

        Paystack.builder()
                .setPublicKey("pk_live_31efdd78d25ccb1fac9a2d2beb2e207d83a86b76")
                .setLoggingEnabled(true)
                .build();

        paymentSheet = new PaymentSheet(this, this::paymentComplete);


        amountrequested = rootView.findViewById(R.id.amountrequestedtext);

        globalusername = app.getApiusername();
        globalapi_password = app.getApipassword();
        globaluser_token =  app.getApipassword();




        rootView.findViewById(R.id.generatelinkbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PRESSING CARD", "onClick: ");

                String amount = amountrequested.getText().toString().trim();

                if (amount.isEmpty()) {
                    Toast.makeText(mContext, "Please enter the payment amount to continue", Toast.LENGTH_LONG).show();
                } else {
                    postToAPI();
                }
            }
        });



        return rootView;
    }




    private void postToAPI()
    {

        showProgressBar();




        String phonenum;
        String paymentdescription;
        String amountrequestedtext;



        amountrequestedtext = amountrequested.getText().toString();



        HashMap<String, String> paramsotpu = new HashMap<String, String>();


        paramsotpu.put("amount", amountrequestedtext);



        Log.e("Submitted User ID", globalusername);

        Log.e("Submitted request amount", amountrequestedtext);



        String paymentsendpoint = "https://api.sabaapp.co/v0/payment/paystack/initiate";

        Log.e("response", "response "+amountrequestedtext);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "response "+response.toString());
                        hideProgressBar();
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());

                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");
                                String messagedetails =jsonObj.getString("MESSAGE");
                                Integer response_code = jsonObj.getInt("CODE");
                                if(message.toLowerCase().matches("success"))
                                {



                                    String accesslink = jsonObj.getString("access_code");

                                    makePayment(accesslink);







                                }
                                else
                                {

                                    Toast.makeText(mContext, "Failed :"+messagedetails, Toast.LENGTH_LONG).show();

                                    AlertDialog ad = new AlertDialog.Builder(mContext)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Payment request Failed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(mContext.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                           /* dialog.dismiss();
                                            SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(mContext);
                                            sharedPrefsXtreme.saveData("template_submited", "TRUE");
                                            sharedPrefsXtreme.saveData("janjaprofileskip","true");
                                            Intent intent =  new Intent(mContext, wayawayaDrawerActivity.class);
                                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            mContext.startActivity(intent);*/
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
                Log.e("error is ", "" + error);
                Toast.makeText(mContext, "Their was an application network error, please try again", Toast.LENGTH_LONG).show();
                /*SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(mContext);
                sharedPrefsXtreme.saveData("template_submited", "TRUE");
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

    private void makePayment(String accesscode) {
        // Pass access_code from transaction initialize call on the server
        String accessvalue = "br6cgmvflhn3qtd";
        if(accesscode==null){

            accessvalue = accesscode;

        }
        paymentSheet.launch(accessvalue);
    }

    private void paymentComplete(PaymentSheetResult paymentSheetResult) {
        String message;

        if (paymentSheetResult instanceof PaymentSheetResult.Cancelled) {
            message = "Cancelled";
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e("Payment failed",
                    failedResult.getError().getMessage() != null ? failedResult.getError().getMessage() : "Failed",
                    failedResult.getError());
            message = failedResult.getError().getMessage() != null ? failedResult.getError().getMessage() : "Failed";
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d("Payment successful",
                    ((PaymentSheetResult.Completed) paymentSheetResult).getPaymentCompletionDetails().toString());
            message = "Successful";
        } else {
            message = "You shouldn't be here";
        }

        Toast.makeText(getContext(), "Payment " + message, Toast.LENGTH_SHORT).show();
    }


}