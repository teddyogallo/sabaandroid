package com.sabapp.saba;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sabapp.saba.adapters.messagefragmentRecyclerAdapter;
import com.sabapp.saba.adapters.messagestartactivityRecycler;
import com.sabapp.saba.adapters.recentactivitiesRecyclerAdapter;
import com.sabapp.saba.adapters.sabaeventlistclientHomeRecyclerAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mMessageRecycler;

    ArrayList<Integer> images;

    ArrayList<String> janjalisttwo;
    ArrayList<String> janjalistthree;
    ArrayList<String> timelist;
    ArrayList<String> socialidlist;
    ArrayList<String> senderidlist;
    ArrayList<String> productImagelist;
    messagefragmentRecyclerAdapter adaptertwo;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int chat_number = 0;
    int allnotifications_number = 0;
    // private FragmentGalleryBinding binding;
    RecyclerView janjarecyclertwo;
    ArrayList<sabaEventItem> templateArraystwo;
    Context context;
    sabaapp app;
    AVLoadingIndicatorView progressBar;
    String recipient_id;
    RelativeLayout taptotest;

    TextInputEditText customersearch;

    ImageButton addContactbutton;


    public messageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment messageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static messageFragment newInstance(String param1, String param2) {
        messageFragment fragment = new messageFragment();
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
        getFevorites();


        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("CHAT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("PAYMENT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("ORDER_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("NOTIFICATION_LOCAL_BROADCAST"));



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



        //mMessageAdapter=new conversationactivityRecycler(templateArraystwo,getApplicationContext(), this);
        adaptertwo = new messagefragmentRecyclerAdapter(templateArraystwo, context, messageFragment.this);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(context));
        mMessageRecycler.setAdapter(adaptertwo);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_message, container, false);

        mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);

        progressBar = view.findViewById(R.id.progressBar);
        hideProgressBar();

        templateArraystwo= new ArrayList<sabaEventItem>();
        janjalisttwo= new ArrayList<String>();
        janjalistthree= new ArrayList<String>();
        timelist=new ArrayList<String>();
        socialidlist=new ArrayList<String>();
        senderidlist=new ArrayList<String>();
        productImagelist = new ArrayList<String>();
        images= new ArrayList<Integer>();



        chat_number = app.getChatNumber();
        int payment_numberlocal = app.getPaymentNumber();
        int ordernumber_local = app.getOrderNumber();
        int generalnumber_local = app.getGeneralNotificationsNumber();


        allnotifications_number = payment_numberlocal + ordernumber_local + generalnumber_local;

        int localchat_number = 0;

        //save to shared preferences here
        SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(context);

        sharedPrefsXtreme.saveIntData("chat_notifications_num", localchat_number);


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




                    if (message_type.equals("INCOMING")){
                        //start of incoming message
                        if(!socialidlist.contains(social_id)) {

                            Long Timestamp;
                            try{

                                Timestamp = Long.parseLong(timestamp);

                            }catch ( NumberFormatException e) {

                                Timestamp = (long)Double.parseDouble(timestamp);
                            }

                            Long oneweekago = Timestamp - 1000 * 60 * 60 * 24 * 7;
                            Date timeD = new Date(Timestamp * 1000);
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy 'at' hh:mm aaa");

                            //Log.d("Added Item", janjalisttwo + "");
                            if (socialidlist.size() >= 5 && oneweekago > Timestamp) {
                                //messages sent over a week ago processed here
                                Log.d("Message from : ", social_details + " is too old");
                                //end of messages sent over a week ago
                            } else if (socialidlist.size() <= 15) {

                                String Time = sdf.format(timeD);
                                socialidlist.add(social_id);
                                Log.d("User being added: ", social_details + "");
                                senderidlist.add(social_id);
                                janjalisttwo.add(social_details);
                                janjalistthree.add(messagecontent);
                                timelist.add(Time);
                                productImagelist.add(imagetobase64(R.drawable.personplaceholder));
                                images.add((R.drawable.janjaitemrecyclerbottom));

                                sabaEventItem item = new sabaEventItem();
                                item.setBusinessname(social_details);

                                item.setProductprice(messagecontent);
                                item.setStatus(social_id);
                                item.setDetails(Time);

                                byte[] decodedString = Base64.decode(imagetobase64(R.drawable.personplaceholder), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                item.setBytes(decodedString);

                                item.setImagebitmap(decodedByte);

                                item.setTemplateImg(R.drawable.janjaitemrecyclerbottom);
                                //setImagebitmap
                                templateArraystwo.add(item);
                                adaptertwo.notifyDataSetChanged();

                                //messages not older than a week
                            }

//end of exception if user ID is already added
                        }else{

                            Log.d("Value for Social ID Exists : ", social_details );

                        }

                        //end of incoming message


                    }

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



    private void getFevorites()
    {
        //continuetonextbutton.setText("Uploading to your store...");

        showProgressBar();
        HashMap<String, String> paramsotpu = new HashMap<String, String>();

        paramsotpu.put("username", app.getApiusername());

        String loginuserendpoint="https://api.getabirio.com/v0/messages/retrieveconversations";


        Log.d("GET Favs API User Name", app.getApiusername() + "");
        Log.d("Get Favs API Password", app.getApipassword() + "");


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

                                        janjalisttwo.add("No new messages");
                                        janjalistthree.add("Start chatting to create new messages");
                                        timelist.add("Time not updated");
                                        socialidlist.add("No_Val");
                                        senderidlist.add("No_val");
                                        productImagelist.add(imagetobase64(R.drawable.profileplaceholder));
                                        images.add((R.drawable.addproduct_icon));


                                        //end of add placeholder here
                                    }
                                    else{
                                        //no need to add place holder
                                        templateArraystwo.clear();

                                        for (int i = 0; i < dataobj.length(); i++) {
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
                                                if(!socialidlist.contains(social_id)) {

                                                    Long Timestamp;
                                                    try{

                                                        Timestamp = Long.parseLong(timestamp);

                                                    }catch ( NumberFormatException e) {

                                                        Timestamp = (long)Double.parseDouble(timestamp);
                                                    }

                                                    Long oneweekago = Timestamp - 1000 * 60 * 60 * 24 * 30;
                                                    Date timeD = new Date(Timestamp * 1000);
                                                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy 'at' hh:mm aaa");

                                                    //Log.d("Added Item", janjalisttwo + "");
                                                    if (socialidlist.size() >= 30 && oneweekago > Timestamp) {
                                                        //messages sent over a week ago processed here
                                                        Log.d("Message from : ", social_details + " is too old");
                                                        //end of messages sent over a week ago
                                                    } else if (i <= 100) {

                                                        String Time = sdf.format(timeD);
                                                        socialidlist.add(social_id);
                                                        Log.d("User being added: ", social_details + "");
                                                        senderidlist.add(social_id);
                                                        janjalisttwo.add(social_details);
                                                        janjalistthree.add(messagecontent);
                                                        timelist.add(Time);
                                                        productImagelist.add(imagetobase64(R.drawable.personplaceholder));
                                                        images.add((R.drawable.janjaitemrecyclerbottom));

                                                        sabaEventItem item = new sabaEventItem();
                                                        item.setBusinessname(social_details);

                                                        item.setProductprice(messagecontent);
                                                        item.setStatus(social_id);
                                                        item.setDetails(Time);

                                                        byte[] decodedString = Base64.decode(imagetobase64(R.drawable.personplaceholder), Base64.DEFAULT);
                                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                        item.setBytes(decodedString);

                                                        item.setImagebitmap(decodedByte);

                                                        item.setTemplateImg(R.drawable.janjaitemrecyclerbottom);
                                                        //setImagebitmap
                                                        templateArraystwo.add(item);

                                                        //messages not older than a week
                                                    }

//end of exception if user ID is already added
                                                }else{

                                                    Log.d("Value for Social ID Exists : ", social_details );

                                                }

                                                //end of incoming message


                                            }else{


                                                /*janjalisttwo.add(null);
                                                janjalistthree.add(null);
                                                timelist.add(null);
                                                socialidlist.add(null);
                                                senderidlist.add(null);
                                                productImagelist.add(null);
                                                images.add(null);*/
                                            }


                                        }
                                        //no need to add placeholder
                                        adaptertwo.notifyDataSetChanged();

                                    }


                                }
                                else
                                {

                                    /*janjalisttwo.add("No new messages");
                                    janjalistthree.add("Start chatting to create messages");
                                    timelist.add("Time not updated");
                                    socialidlist.add("No_Val");
                                    senderidlist.add("No_val");
                                    productImagelist.add(imagetobase64(R.drawable.profileplaceholder));
                                    images.add((R.drawable.profileplaceholder));

                                    templateArraystwo.clear();
                                    for(Integer i=0; i<janjalisttwo.size(); i++)
                                    {
                                        sabaEventItem item=new sabaEventItem();
                                        item.setBusinessname(janjalisttwo.get(i));
                                        item.setProductprice(janjalistthree.get(i));
                                        item.setStatus(senderidlist.get(i));
                                        item.setDetails(timelist.get(i));
                                        byte[] decodedString = Base64.decode(productImagelist.get(i), Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                                        item.setBytes(decodedString);

                                        item.setTemplateImg(images.get(i));
                                        templateArraystwo.add(item);

                                    }
                                    adaptertwo.notifyDataSetChanged();

                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);*/

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