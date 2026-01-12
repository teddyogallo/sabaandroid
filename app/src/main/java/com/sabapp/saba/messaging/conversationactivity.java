package com.sabapp.saba.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.adapters.conversationactivityRecycler;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.sabapp.saba.R;


public class conversationactivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private conversationactivityRecycler mMessageAdapter;
    ArrayList<Integer> images;
    ArrayList<String> janjalisttwo;
    ArrayList<String> janjalistthree;
    ArrayList<String> timelist;
    ArrayList<String> socialidlist;
    ArrayList<String> messagetypelist;
    ArrayList<String> datedmessagelist;
    ArrayList<String> receivedtitlelist;
    ArrayList<String> senttitlelist;
    ArrayList<String> productImagelist;
    conversationactivityRecycler adaptertwo;
    ArrayList<sabaEventItem> templateArraystwo;
    RecyclerView janjarecyclertwo;
    Context context;
    sabaapp app;
    AVLoadingIndicatorView progressBar;
    String recipient_id;
    String responsemessage;
    EditText replymessage;
    Button sendmessagebutton, viewmorebutton;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int chat_number = 0;
    int allnotifications_number = 0;
    String activechatname;


    ImageView backbuttonimage;

    @Override
    public void onBackPressed(){

        startActivity(new Intent(getApplicationContext(), messagestartactivity.class));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converstionactivity);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        progressBar=(AVLoadingIndicatorView)findViewById(R.id.progressBar);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_gchannel);
        // add toolbar back button
        setSupportActionBar(toolbarTop);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);  // Disable the default title
        }
        toolbarTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                startActivity(new Intent(getApplicationContext(), messagestartactivity.class));

            }
        });
        //end of add toolbar back button
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);




        //end of add toolbar back button

        backbuttonimage = (ImageView)findViewById(R.id.backarrowimage);
        replymessage=(EditText) findViewById(R.id.edit_gchat_message);
        sendmessagebutton=(Button) findViewById(R.id.button_gchat_send);
        viewmorebutton = (Button) findViewById(R.id.morebutton);
        context = getApplicationContext();

        Intent intent=getIntent();

        String chatname=intent.getStringExtra("chatname");
        activechatname= intent.getStringExtra("chatname");
        String activechat=intent.getStringExtra("activeuser");
        if(!chatname.equals("")){

            mTitle.setText("Chat with: "+chatname);
        }

        if(!activechat.equals("")){

            SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(getApplicationContext());
            sharedPrefsXtreme.saveData("activechatuser", activechat);
        }


        if(!chatname.equals("")){

            SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(getApplicationContext());
            sharedPrefsXtreme.saveData("activechatname", chatname);

        }

        templateArraystwo= new ArrayList<sabaEventItem>();
        templateArraystwo= new ArrayList<sabaEventItem>();
        janjalisttwo= new ArrayList<String>();
        janjalistthree= new ArrayList<String>();
        timelist=new ArrayList<String>();
        socialidlist=new ArrayList<String>();
        messagetypelist=new ArrayList<String>();
        datedmessagelist=new ArrayList<String>();
        senttitlelist=new ArrayList<String>();
        receivedtitlelist=new ArrayList<String>();
        productImagelist = new ArrayList<String>();
        images= new ArrayList<Integer>();


        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        //mMessageAdapter=new conversationactivityRecycler(templateArraystwo,getApplicationContext(), this);
        adaptertwo = new conversationactivityRecycler(this, templateArraystwo);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(adaptertwo);
        progressBar=findViewById(R.id.progressBar);
        app = (sabaapp) this.getApplicationContext();
        getFevorites();

        String current_status = "LOGIN";

        sendagentstatus(current_status);

        chat_number = app.getChatNumber();
        int payment_numberlocal = app.getPaymentNumber();
        int ordernumber_local = app.getOrderNumber();
        int generalnumber_local = app.getGeneralNotificationsNumber();


        allnotifications_number = payment_numberlocal + ordernumber_local + generalnumber_local;


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
                    String notification_title = intent.getStringExtra("title");

                    String notification_message = intent.getStringExtra("body");

                    String notification_type = intent.getStringExtra("type");

                    String notification_id = intent.getStringExtra("social_id");

                    //save to shared preferences here
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

                    sharedPrefsXtreme.saveIntData("general_notifications_num", generalnumber_local);
                    sharedPrefsXtreme.saveData("current_notificationtitle", notification_title);

                    sharedPrefsXtreme.saveData("current_notification_message", notification_message);

                    sharedPrefsXtreme.saveData("current_notification_type", notification_type);

                    sharedPrefsXtreme.saveData("current_notification_id", notification_id);



                    //end of save to shared preferences here

                    AppCompatActivity activity = (AppCompatActivity) conversationactivity.this;
                    popupnotification bottomSheetFragment = new popupnotification();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());



                }
                else if (intent.getAction().equals("CHAT_BROADCAST")) {
                    // new push notification is received

                    String social_id = intent.getStringExtra("social_id");
                    if(TextUtils.isEmpty(social_id)||social_id.equals("null")||social_id==null){
                        social_id = "254724044116";

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
                        messagecontent = "Empty system message";

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

                    if(activechat.equals(social_id)){

                        if (message_type.equals("INCOMING")){
                            //start of incoming message

                            Log.d("New user being added : ", social_details);

                            Long Timestamp;
                            try{

                                Timestamp = Long.parseLong(timestamp);

                            }catch ( NumberFormatException e) {

                                Timestamp = (long)Double.parseDouble(timestamp);
                            }

                            Long oneweekago = Timestamp - 1000 * 60 * 60 * 24 * 7;
                            Date timeD = new Date(Timestamp * 1000);
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
                            SimpleDateFormat plaindate = new SimpleDateFormat("EEE, MMM d, ''yy");

                            String Time = sdf.format(timeD);
                            String date_started = plaindate.format(timeD);

                            socialidlist.add(social_id);
                            janjalisttwo.add(social_details);
                            janjalistthree.add(messagecontent);
                            messagetypelist.add(message_type);
                            receivedtitlelist.add(social_details);
                            timelist.add(Time);
                            productImagelist.add(imagetobase64(R.drawable.profileresized));
                            images.add((R.drawable.profileresized));
                            //Log.d("Added Item", janjalisttwo + "");



                            sabaEventItem item = new sabaEventItem();
                            item.setBusinessname(social_details);

                            item.setReceivedmessage(messagecontent);
                            item.setStatus(social_id);
                            item.setReceivedmessagetime(Time);
                            item.setChatusertype(message_type);

                            if(!datedmessagelist.contains(date_started)){
                                datedmessagelist.add(date_started);
                                item.setReceivedmessagedate(date_started);
                            }else{
                                datedmessagelist.add("");
                                item.setReceivedmessagedate("");
                            }

                            if(!receivedtitlelist.contains(social_details)){
                                item.setReceivedmessagetitle(social_details);

                            }else{

                                item.setReceivedmessagetitle("");
                            }

                            item.setReceivedmessagesender("");


                            byte[] decodedString = Base64.decode(imagetobase64(R.drawable.profileresized), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            item.setBytes(decodedString);

                            item.setImagebitmap(decodedByte);

                            item.setTemplateImg(R.drawable.profileresized);
                            //setImagebitmap
                            templateArraystwo.add(item);

                            adaptertwo.notifyDataSetChanged();

                            mMessageRecycler.scrollToPosition(templateArraystwo.size() - 1);

                            //end of incoming message
                        }


                    }
                    else{
                        //add notifications here and show notifications
                        chat_number = app.getChatNumber();

                        chat_number =1 + chat_number;

                        String notification_title = intent.getStringExtra("title");

                        String notification_message = intent.getStringExtra("body");

                        String notification_type = intent.getStringExtra("type");

                        String notification_id = intent.getStringExtra("social_id");

                        //save to shared preferences here
                        SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

                        sharedPrefsXtreme.saveIntData("chat_notifications_num", chat_number);
                        sharedPrefsXtreme.saveData("current_notificationtitle", notification_title);

                        sharedPrefsXtreme.saveData("current_notification_message", notification_message);

                        sharedPrefsXtreme.saveData("current_notification_type", notification_type);

                        sharedPrefsXtreme.saveData("current_notification_id", notification_id);

                        AppCompatActivity activity = (AppCompatActivity) conversationactivity.this;
                        popupnotification bottomSheetFragment = new popupnotification();
                        bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());


                    }


                    //getFevorites();

                    //end of save to shared preferences here



                }
                else if (intent.getAction().equals("ORDER_BROADCAST")) {
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
                    ordernumber_local = ordernumber_local + 1;
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

                    sharedPrefsXtreme.saveIntData("order_notifications_num", ordernumber_local);
                    sharedPrefsXtreme.saveData("current_notificationtitle", notification_title);

                    sharedPrefsXtreme.saveData("current_notification_message", notification_message);

                    sharedPrefsXtreme.saveData("current_notification_type", notification_type);

                    sharedPrefsXtreme.saveData("current_notification_id", notification_id);

                    AppCompatActivity activity = (AppCompatActivity) conversationactivity.this;
                    popupnotification bottomSheetFragment = new popupnotification();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());

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
                    sharedPrefsXtreme.saveData("current_notificationtitle", notification_title);

                    sharedPrefsXtreme.saveData("current_notification_message", notification_message);

                    sharedPrefsXtreme.saveData("current_notification_type", notification_type);

                    sharedPrefsXtreme.saveData("current_notification_id", notification_id);
                    //end of save to shared preferences here

                    AppCompatActivity activity = (AppCompatActivity) conversationactivity.this;
                    popupnotification bottomSheetFragment = new popupnotification();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());



                }

            }
        };


        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start login operations

                if(replymessage.getText().toString().matches(""))
                {
                    //show alert here
                    Toast.makeText(getApplicationContext(), "Message cannot be empty, Try again", Toast.LENGTH_LONG).show();


                }else{
                    responsemessage = replymessage.getText().toString();

                    sendMessageResponse();

                }



                //end of start login operations
            }
        });


        viewmorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start login operations
                messageoptionspopups bottomSheetFragment = new messageoptionspopups();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());


                //end of start login operations
            }
        });



    }


    @Override
    public void onResume()
    {
        super.onResume();

        loadTemplateImagesBottom();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("CHAT_BROADCAST"));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("PAYMENT_BROADCAST"));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("ORDER_BROADCAST"));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("NOTIFICATION_LOCAL_BROADCAST"));

        String current_status = "LOGIN";

        sendagentstatus(current_status);

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

        String current_status = "LOGOUT";

        sendagentstatus(current_status);
    }

    private void loadTemplateImagesBottom()
    {
        templateArraystwo.clear();

        for(Integer i=0; i<janjalisttwo.size(); i++)
        {
            sabaEventItem item=new sabaEventItem();
            item.setBusinessname(janjalisttwo.get(i));
            item.setPrice_tier(janjalistthree.get(i));
            item.setStatus(socialidlist.get(i));
            item.setSentmessagetitle(senttitlelist.get(i));
            item.setReceivedmessagetitle(receivedtitlelist.get(i));
            item.setChatusertype(messagetypelist.get(i));
            item.setReceivedmessagedate(datedmessagelist.get(i));
            item.setDetails(timelist.get(i));
            item.setEncodedstring(productImagelist.get(i));
            item.setTemplateImg(images.get(i));
            templateArraystwo.add(item);

        }
        Log.e("adaptercall", "adaptercall");
        adaptertwo.notifyDataSetChanged();
        mMessageRecycler.scrollToPosition(templateArraystwo.size() - 1);
    }

    private void getFevorites()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        String activechatuser = app.getActiveChatuser();

        showProgressBar();
        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());
        paramsotpu.put("active_user", app.getActiveChatuser());

        String loginuserendpoint="https://api.sabaapp.co/v0/messages/retrieveusermessages?active_user="+app.getActiveChatuser();

        Log.d("GET all chat Active user", app.getActiveChatuser() + "");

        Log.d("GET all chat API User Name", app.getApiusername() + "");
        Log.d("Request endpoint ", loginuserendpoint);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, loginuserendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressBar();
                        Log.e("response", "response "+response.toString());


                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("status");
                                String messagedetails =jsonObj.getString("message");
                                if(message.toLowerCase().matches("success"))
                                {
                                    JSONArray dataobj = jsonObj.getJSONArray("data");

                                    if(dataobj.length()==0){
                                        //add place holder here
                                        janjalisttwo.add("Start chatting to see more");
                                        janjalistthree.add("No Message");
                                        timelist.add("Time not updated");
                                        socialidlist.add("No_Val");
                                        messagetypelist.add("OUTGOING");
                                        datedmessagelist.add("");
                                        senttitlelist.add("");
                                        receivedtitlelist.add("");
                                        productImagelist.add(imagetobase64(R.drawable.profileresized));
                                        images.add((R.drawable.profileresized));

                                        //end of add placeholder here
                                    }else{
                                        //no need to add place holder
                                        templateArraystwo.clear();

                                        for (int i = 0; i < dataobj.length() ; i++) {
                                            jsonObj = dataobj.getJSONObject(i);

                                            String social_id = jsonObj.getString("social_id");

                                            String sendernames = jsonObj.getString("firstname");
                                            String social_details = sendernames;
                                            String message_type = jsonObj.getString("type");

                                            String messagecontent =jsonObj.getString("message");
                                            String timestamp =jsonObj.getString("time");
                                            if(sendernames.equals("")){

                                                social_details= social_id;
                                            }


                                            if (message_type.equals("INCOMING")){
                                                //start of incoming message

                                                Log.d("User already added : ", social_details + " is too old");

                                                Long Timestamp;
                                                try{

                                                    Timestamp = Long.parseLong(timestamp);

                                                }catch ( NumberFormatException e) {

                                                    Timestamp = (long)Double.parseDouble(timestamp);
                                                }

                                                Long oneweekago = Timestamp - 1000 * 60 * 60 * 24 * 7;
                                                Date timeD = new Date(Timestamp * 1000);
                                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
                                                SimpleDateFormat plaindate = new SimpleDateFormat("EEE, MMM d, ''yy");

                                                String Time = sdf.format(timeD);
                                                String date_started = plaindate.format(timeD);

                                                socialidlist.add(social_id);
                                                janjalisttwo.add(social_details);
                                                janjalistthree.add(messagecontent);
                                                messagetypelist.add(message_type);
                                                receivedtitlelist.add(social_details);
                                                timelist.add(Time);
                                                productImagelist.add(imagetobase64(R.drawable.profileresized));
                                                images.add((R.drawable.profileresized));
                                                //Log.d("Added Item", janjalisttwo + "");



                                                sabaEventItem item = new sabaEventItem();
                                                item.setBusinessname(social_details);

                                                item.setReceivedmessage(messagecontent);
                                                item.setStatus(social_id);
                                                item.setReceivedmessagetime(Time);
                                                item.setChatusertype(message_type);

                                                if(!datedmessagelist.contains(date_started)){
                                                    datedmessagelist.add(date_started);
                                                    item.setReceivedmessagedate(date_started);
                                                }else{
                                                    datedmessagelist.add("");
                                                    item.setReceivedmessagedate("");
                                                }

                                                if(!receivedtitlelist.contains(social_details)){
                                                    item.setReceivedmessagetitle(social_details);

                                                }else{

                                                    item.setReceivedmessagetitle("");
                                                }

                                                item.setReceivedmessagesender("");


                                                byte[] decodedString = Base64.decode(imagetobase64(R.drawable.profileresized), Base64.DEFAULT);
                                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                item.setBytes(decodedString);

                                                item.setImagebitmap(decodedByte);

                                                item.setTemplateImg(R.drawable.profileresized);
                                                //setImagebitmap
                                                templateArraystwo.add(item);

                                                //end of incoming message
                                            }else if (message_type.equals("OUTGOING")){
                                                //start of incoming message

                                                Log.d("User already added : ", social_details + " is too old");

                                                Long Timestamp;
                                                try{

                                                    Timestamp = Long.parseLong(timestamp);

                                                }catch ( NumberFormatException e) {

                                                    Timestamp = (long)Double.parseDouble(timestamp);
                                                }
                                                Long oneweekago = Timestamp - 1000 * 60 * 60 * 24 * 7;
                                                Date timeD = new Date(Timestamp * 1000);
                                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
                                                SimpleDateFormat plaindate = new SimpleDateFormat("EEE, MMM d, ''yy");

                                                String Time = sdf.format(timeD);
                                                String date_started = plaindate.format(timeD);

                                                socialidlist.add(social_id);
                                                janjalisttwo.add(social_details);
                                                janjalistthree.add(messagecontent);
                                                messagetypelist.add(message_type);
                                                senttitlelist.add(social_details);
                                                timelist.add(Time);
                                                productImagelist.add(imagetobase64(R.drawable.profileresized));
                                                images.add((R.drawable.janjaitemrecyclerbottom));
                                                //Log.d("Added Item", janjalisttwo + "");



                                                sabaEventItem item = new sabaEventItem();
                                                item.setBusinessname(social_details);

                                                item.setSentmessage(messagecontent);
                                                item.setStatus(social_id);
                                                item.setSentmessagetime(Time);
                                                item.setChatusertype(message_type);

                                                if(!datedmessagelist.contains(date_started)){
                                                    datedmessagelist.add(date_started);
                                                    item.setReceivedmessagedate(date_started);
                                                }else{
                                                    datedmessagelist.add("");
                                                    item.setReceivedmessagedate("");
                                                }

                                                if(!senttitlelist.contains(social_details)){
                                                    item.setSentmessagetitle(social_details);

                                                }else{

                                                    item.setSentmessagetitle("");
                                                }

                                                byte[] decodedString = Base64.decode(imagetobase64(R.drawable.profileresized), Base64.DEFAULT);
                                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                item.setBytes(decodedString);

                                                item.setImagebitmap(decodedByte);

                                                item.setTemplateImg(R.drawable.profileresized);
                                                //setImagebitmap
                                                templateArraystwo.add(item);

                                                //end of incoming message
                                            }


                                        }
                                        //no need to add placeholder
                                        adaptertwo.notifyDataSetChanged();

                                        mMessageRecycler.scrollToPosition(templateArraystwo.size() - 1);
                                        //mMessageRecycler.smoothScrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);

                                    }


                                }
                                else
                                {

                                    janjalisttwo.add("No new message");
                                    janjalistthree.add("Wait for customers to send message");
                                    timelist.add("Time not added");
                                    socialidlist.add("No_Val");

                                    productImagelist.add(imagetobase64(R.drawable.profileresized));
                                    images.add((R.drawable.profileresized));

                                    templateArraystwo.clear();
                                    for(Integer i=0; i<janjalisttwo.size(); i++)
                                    {
                                        sabaEventItem item=new sabaEventItem();
                                        item.setBusinessname(janjalisttwo.get(i));
                                        item.setSentmessage(janjalistthree.get(i));
                                        item.setStatus(socialidlist.get(i));
                                        item.setDetails(timelist.get(i));
                                        byte[] decodedString = Base64.decode(productImagelist.get(i), Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                                        item.setBytes(decodedString);

                                        item.setTemplateImg(images.get(i));
                                        templateArraystwo.add(item);

                                    }
                                    adaptertwo.notifyDataSetChanged();

                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }


                        } else {
                            hideProgressBar();
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();

                Log.e("error is ", "" + error);

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
                String apiusername = app.getApiusername();
                String apipassword = app.getApipassword();

                String creds = String.format("%s:%s",apiusername,apipassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private void sendMessageResponse()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        String activechatuser = app.getActiveChatuser();

        showProgressBar();
        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());
        paramsotpu.put("recipient_id", app.getActiveChatuser());
        paramsotpu.put("message", responsemessage);

        String loginuserendpoint="https://api.sabaapp.co/v0/messages/sendsupportmsg";

        Log.d("GET all chat Active user", app.getActiveChatuser() + "");
        Log.d("SENDING MESSAGE: ", responsemessage + "");

        Log.d("GET all chat API User Name", app.getApiusername() + "");
        Log.d("Get all chat  Password", app.getApipassword() + "");
        Log.d("Sender ID", app.getRecipientid() + "");


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginuserendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressBar();
                        Log.e("response", "response "+response.toString());


                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("status");
                                Integer responsecode = jsonObj.getInt("success_code");

                                if (responsecode == 2){
                                    //show popup

                                    messageoptionspopups bottomSheetFragment = new messageoptionspopups();
                                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

                                    //end of show popup
                                }else if(message.toLowerCase().matches("success"))
                                {
                                    replymessage.getText().clear();
                                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                                    //JSONArray dataobj = jsonObj.getJSONArray("data");



                                    String social_id = app.getActiveChatuser();

                                    String sendernames = activechatname;
                                    String social_details = sendernames;
                                    String message_type = "OUTGOING";

                                    String messagecontent =responsemessage;
                                    Long tsLong = System.currentTimeMillis()/1000;
                                    String timestamp = tsLong.toString();

                                    if(sendernames.equals("")){

                                        social_details= social_id;
                                    }


                                    Log.d("User already added : ", social_details + " is too old");

                                    Long Timestamp;
                                    try{

                                        Timestamp = Long.parseLong(timestamp);

                                    }catch ( NumberFormatException e) {

                                        Timestamp = (long)Double.parseDouble(timestamp);
                                    }
                                    Long oneweekago = Timestamp - 1000 * 60 * 60 * 24 * 7;
                                    Date timeD = new Date(Timestamp * 1000);
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
                                    SimpleDateFormat plaindate = new SimpleDateFormat("EEE, MMM d, ''yy");

                                    String Time = sdf.format(timeD);
                                    String date_started = plaindate.format(timeD);

                                    socialidlist.add(social_id);
                                    janjalisttwo.add(social_details);
                                    janjalistthree.add(messagecontent);
                                    messagetypelist.add(message_type);
                                    senttitlelist.add(social_details);
                                    timelist.add(Time);
                                    productImagelist.add(imagetobase64(R.drawable.profileresized));
                                    images.add((R.drawable.janjaitemrecyclerbottom));
                                    //Log.d("Added Item", janjalisttwo + "");



                                    sabaEventItem item = new sabaEventItem();
                                    item.setBusinessname(social_details);

                                    item.setSentmessage(messagecontent);
                                    item.setStatus(social_id);
                                    item.setSentmessagetime(Time);
                                    item.setChatusertype(message_type);

                                    if(!datedmessagelist.contains(date_started)){
                                        datedmessagelist.add(date_started);
                                        item.setReceivedmessagedate(date_started);
                                    }else{
                                        datedmessagelist.add("");
                                        item.setReceivedmessagedate("");
                                    }

                                    if(!senttitlelist.contains(social_details)){
                                        item.setSentmessagetitle(social_details);

                                    }else{

                                        item.setSentmessagetitle("");
                                    }

                                    byte[] decodedString = Base64.decode(imagetobase64(R.drawable.profileresized), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    item.setBytes(decodedString);

                                    item.setImagebitmap(decodedByte);

                                    item.setTemplateImg(R.drawable.profileresized);
                                    //setImagebitmap
                                    templateArraystwo.add(item);

                                    adaptertwo.notifyDataSetChanged();

                                    mMessageRecycler.scrollToPosition(templateArraystwo.size() - 1);


                                }
                                else
                                {
                                    String messagedetails =jsonObj.getString("message");
                                    Toast.makeText(getApplicationContext(), "Not Sent :"+messagedetails, Toast.LENGTH_LONG).show();



                                    String messageerror="There was an Error : "+messagedetails;
                                    Log.d("Msg:",messageerror);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                                Toast.makeText(getApplicationContext(), "Error Parsing", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            hideProgressBar();
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();

                Log.e("error is ", "" + error);
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();

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
                String apiusername = app.getApiusername();
                String apipassword = app.getApipassword();

                String creds = String.format("%s:%s",apiusername,apipassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization",auth);
                return params;
            }


        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private void sendagentstatus(String statusactivity){

        HashMap<String, String> paramsotpu = new HashMap<String, String>();
        paramsotpu.put("username", app.getApiusername());
        paramsotpu.put("active_status", statusactivity);
        paramsotpu.put("active_user", app.getActiveChatuser());




        String loginuserendpoint="https://messaging.wayawaya.com/v0/notifyagent";


        Log.e("Waiting list API Username", "response "+app.getApiusername());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loginuserendpoint, new JSONObject(paramsotpu),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "response "+response.toString());

                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("status");
                                String messagedetails =jsonObj.getString("message");
                                if(message.toLowerCase().matches("success"))

                                {




                                }
                                else
                                {



                                    Log.e("Register waiting list Send failed ", "error"+messagedetails);


                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }


                        } else {




                            Log.e("Update token failed", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {





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
                String apiusername = app.getApiusername();
                String apipassword = app.getApipassword();
                String creds = String.format("%s:%s",apiusername,apipassword);
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

    public String imagetobase64(int drawableimg){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableimg);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray,Base64.DEFAULT);

        return baseString;
    }

    public void showProgressBar()
    {
        progressBar.smoothToShow();
    }
    public void hideProgressBar()
    {
        progressBar.smoothToHide();
    }
}

