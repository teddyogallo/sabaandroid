package com.sabapp.saba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;
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
import com.sabapp.saba.adapters.fragmentEventlistRecycler;
import com.sabapp.saba.adapters.messagefragmentRecyclerAdapter;
import com.sabapp.saba.adapters.paymentlistAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.databinding.FragmentSecondBinding;
import com.sabapp.saba.messaging.messagestartactivity;
import com.sabapp.saba.payments.requestpaymentpopup;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SecondFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSecondBinding binding;

    private String activeUser;
    private String chatName;
    private String createAdrAd;

    TextView balanceamounttext;

    Button withdrawtopupbutton;

    private RecyclerView paymentlistRecycler;

    paymentlistAdapter paymentsAdapter;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    ArrayList<sabaEventItem> pymentsvalueArraystwo;

    sabaapp app;
    AVLoadingIndicatorView progressBar;

    String accountlogintype;

    Context context;
    ArrayList<sabaEventItem> paymentList;

    int chat_number = 0;
    int allnotifications_number = 0;

    public static SecondFragment newInstance(
            String activeUser,
            String chatName,
            String createAdrAd
    ) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("activeuser", activeUser);
        args.putString("chatname", chatName);
        args.putString("createadrad", createAdrAd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // now you can safely use it
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
        getPaymentslist();


        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("CHAT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("PAYMENT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("ORDER_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("NOTIFICATION_LOCAL_BROADCAST"));





    }

    private void setUpRecycler()
    {



        //mMessageAdapter=new conversationactivityRecycler(templateArraystwo,getApplicationContext(), this);
        /*adaptertwo = new messagefragmentRecyclerAdapter(paymentlistRecycler, context, SecondFragment.this);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(context));
        mMessageRecycler.setAdapter(adaptertwo);*/

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
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        View view =  inflater.inflate(R.layout.fragment_second, container, false);
        context = requireContext();

        paymentlistRecycler = (RecyclerView) view.findViewById(R.id.paymentlistrecycler);

        balanceamounttext = (TextView) view.findViewById(R.id.accountbalancetextlabel);
        withdrawtopupbutton = (Button)view.findViewById(R.id.withdrawbutton);


        paymentList  = new ArrayList<sabaEventItem>();



        progressBar = view.findViewById(R.id.progressBar);
        hideProgressBar();

        accountlogintype = app.getLoginAccounttype();


        if(accountlogintype!=null && accountlogintype.equalsIgnoreCase("vendor")){


            withdrawtopupbutton.setBackground(ContextCompat.getDrawable(    context, R.drawable.cr8lr270a7be2e80d7f40a80));
            withdrawtopupbutton.setText("Withdraw");


        }else{

            withdrawtopupbutton.setBackground(ContextCompat.getDrawable(    context, R.drawable.cr10lr270cd01a2891161));

            withdrawtopupbutton.setText("Top Up");
        }


        withdrawtopupbutton.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            if(accountlogintype!=null && !accountlogintype.equalsIgnoreCase("vendor")){


                showRequestPaymentpopup();


            }


        });

        paymentlistRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        paymentsAdapter=new paymentlistAdapter(paymentList,context, SecondFragment.this, app);
        paymentlistRecycler.setAdapter(paymentsAdapter);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals("NOTIFICATION_LOCAL_BROADCAST")) {
                    // new push notification is received
                    int payment_numberlocal = app.getPaymentNumber();
                    int ordernumber_local = app.getOrderNumber();
                    int generalnumber_local = app.getGeneralNotificationsNumber();


                    int currentgeneral_notifications = payment_numberlocal + ordernumber_local + generalnumber_local;

                    allnotifications_number =1 + currentgeneral_notifications;

                    generalnumber_local = generalnumber_local + 1;

                    String notification_title = app.getCurrentNotificationtitle();

                    String notification_message = app.getCurrentNotificationmessage();

                    String notification_type = app.getCurrentNotificationtype();

                    String notification_id = app.getCurrentNotificationid();

                    //save to shared preferences here
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

                    sharedPrefsXtreme.saveIntData("general_notifications_num", generalnumber_local);
                    //sharedPrefsXtreme.saveData("current_notificationtitle", notification_title);

                    //sharedPrefsXtreme.saveData("current_notification_message", notification_message);

                    //sharedPrefsXtreme.saveData("current_notification_type", notification_type);

                    //sharedPrefsXtreme.saveData("current_notification_id", notification_id);



                    //end of save to shared preferences here

                    /*AppCompatActivity activity = (AppCompatActivity) messagestartactivity.this;
                    popupnotification bottomSheetFragment = new popupnotification();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());*/



                }
                else if (intent.getAction().equals("CHAT_BROADCAST")) {
                    // new push notification is received

                    /*chat_number = app.getChatNumber();

                    chat_number =1 + chat_number;

                    //save to shared preferences here
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

                    sharedPrefsXtreme.saveIntData("chat_notifications_num", chat_number);*/

                    String social_id = intent.getStringExtra("social_id");
                    if(TextUtils.isEmpty(social_id)||social_id.equals("null")||social_id==null){
                        social_id = "testuserid";

                    }

                    String sendernames =  intent.getStringExtra("firstname");
                    if(TextUtils.isEmpty(sendernames)||sendernames.equals("null")||sendernames==null){
                        sendernames = "TEST User";

                    }

                    String social_details = sendernames;
                    String message_type =  intent.getStringExtra("type");
                    if(TextUtils.isEmpty(message_type)||message_type.equals("null")||message_type==null){
                        message_type = "INCOMING";

                    }

                    String messagecontent =  intent.getStringExtra("chat_message");
                    if(TextUtils.isEmpty(messagecontent)||messagecontent.equals("null")||messagecontent==null){
                        messagecontent = "Message does not have content";

                    }

                    String timestamp =  intent.getStringExtra("time");
                    if(TextUtils.isEmpty(timestamp)||timestamp.equals("null")||timestamp==null){
                        //timestamp = "Message does not have content";
                        Long tsLong = System.currentTimeMillis()/1000;
                        timestamp = tsLong.toString();

                    }

                    if(sendernames.equals("")){

                        social_details= social_id;
                    }

                    String notification_title = intent.getStringExtra("title");

                    String notification_message = intent.getStringExtra("body");

                    String notification_type = intent.getStringExtra("type");

                    String notification_id = intent.getStringExtra("social_id");





                    //getFevorites();

                    //end of save to shared preferences here
                    /*AppCompatActivity activity = (AppCompatActivity) messagestartactivity.this;
                    popupnotification bottomSheetFragment = new popupnotification();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());*/

                }

                else if (intent.getAction().equals("PAYMENT_BROADCAST")) {
                    // new push notification is received
                    int payment_numberlocal = app.getPaymentNumber();
                    int ordernumber_local = app.getOrderNumber();
                    int generalnumber_local = app.getGeneralNotificationsNumber();


                    int currentgeneral_notifications = payment_numberlocal + ordernumber_local + generalnumber_local;

                    allnotifications_number =1 + currentgeneral_notifications;

                    String notification_title = intent.getStringExtra("title");

                    String notification_message = intent.getStringExtra("body");

                    String notification_type = intent.getStringExtra("type");

                    String notification_id = intent.getStringExtra("social_id");

                    //save to shared preferences here
                    payment_numberlocal = payment_numberlocal + 1;
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

                    sharedPrefsXtreme.saveIntData("payment_notifications_num", payment_numberlocal);


                    //end of save to shared preferences here

                    /*AppCompatActivity activity = (AppCompatActivity) messagestartactivity.this;
                    popupnotification bottomSheetFragment = new popupnotification();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());*/

                }

            }
        };





        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    public void showRequestPaymentpopup()
    {


        /*requestpaymentpopup bottomSheetFragment = new requestpaymentpopup();
        Bundle args = new Bundle();
        args.putString("VALUE_ARRAY", dataobj.toString());
        bottomSheetFragment.setArguments(args);

        bottomSheetFragment.show((this).getSupportFragmentManager(), bottomSheetFragment.getTag());*/

        AppCompatActivity activity = (AppCompatActivity) context;
        requestpaymentpopup bottomSheetFragment = new requestpaymentpopup();
        bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());


    }


    private void getPaymentslist()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        String request_username = app.getApiusername();


        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());


        String paymentsendpoint="https://api.sabaapp.co/v0/payments";



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



                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");
                                String messagedetails =jsonObj.getString("MESSAGE");
                                if(message.toLowerCase().matches("success"))
                                {
                                    JSONArray paymentsArray  = jsonObj.getJSONArray("DATA");



                                    if(paymentsArray.length()==0){
                                        // their are not values to add
                                        String messageerror="There was Zero products retrieved";
                                        Log.d("Msg:",messageerror);

                                        sabaEventItem item = new sabaEventItem();

                                        item.setpaymentAmount(null);
                                        item.setpaymentCurrency(null);
                                        item.setpaymentDescription("No payment listed");
                                        item.setpaymentFees(null);
                                        item.setPayingUserId(null);
                                        item.setPaymentStatus(null);
                                        item.setPaymentType(null);
                                        item.setProviderReference(null);
                                        item.setpaymentRecNum(null);
                                        item.setpaymentStatusDescription("No payment value retrieved");
                                        item.setpaymentTaxAmount(null);
                                        item.setpaymentTimePaid(null);
                                        item.setpaymentTimestamp(null);
                                        item.setpaymentTransactionNumber(null);
                                        item.setpaymentUserId(null);

                                        paymentList.add(item);

                                        paymentsAdapter.notifyDataSetChanged();


                                        //end of their are no values to add
                                    }else{

                                        paymentList.clear();

                                        for (int i = 0; i < paymentsArray.length(); i++) {

                                            JSONObject paymentObj = paymentsArray.getJSONObject(i);

                                            String amount = paymentObj.optString("amount");
                                            String currency = paymentObj.optString("currency");
                                            String description = paymentObj.optString("description");
                                            String fees = paymentObj.optString("fees");
                                            String payingUserId = paymentObj.optString("payinguserid");
                                            String paymentStatus = paymentObj.optString("payment_status");
                                            String paymentType = paymentObj.optString("payment_type");
                                            String providerReference = paymentObj.optString("provider_reference");
                                            String recNum = paymentObj.optString("rec_num");
                                            String statusDescription = paymentObj.optString("status_description");
                                            String taxAmount = paymentObj.optString("taxamount");
                                            String timePaid = paymentObj.optString("timepaid");
                                            String timestamp = paymentObj.optString("timestamp");
                                            String transactionNumber = paymentObj.optString("transaction_number");
                                            String userId = paymentObj.optString("userid");

                                            // Create payment model object
                                            sabaEventItem item = new sabaEventItem();

                                            item.setpaymentAmount(amount);
                                            item.setpaymentCurrency(currency);
                                            item.setpaymentDescription(description);
                                            item.setpaymentFees(fees);
                                            item.setPayingUserId(payingUserId);
                                            item.setPaymentStatus(paymentStatus);
                                            item.setPaymentType(paymentType);
                                            item.setProviderReference(providerReference);
                                            item.setpaymentRecNum(recNum);
                                            item.setpaymentStatusDescription(statusDescription);
                                            item.setpaymentTaxAmount(taxAmount);
                                            item.setpaymentTimePaid(timePaid);
                                            item.setpaymentTimestamp(timestamp);
                                            item.setpaymentTransactionNumber(transactionNumber);
                                            item.setpaymentUserId(userId);

                                            paymentList.add(item);

                                            Log.d("Added Payment", transactionNumber);
                                        }

                                        Collections.reverse(paymentList);

                                        paymentsAdapter.notifyDataSetChanged();


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


    public void showProgressBar()
    {
        progressBar.smoothToShow();
    }
    public void hideProgressBar()
    {
        progressBar.smoothToHide();
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