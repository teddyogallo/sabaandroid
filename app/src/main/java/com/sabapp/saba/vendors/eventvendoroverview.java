package com.sabapp.saba.vendors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.eventdetailsinfoRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.chooseservices;
import com.sabapp.saba.events.setupbudgets;
import com.sabapp.saba.messaging.conversationactivity;
import com.sabapp.saba.messaging.messagestartactivity;
import com.sabapp.saba.sabaVendorDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.checkerframework.common.value.qual.IntVal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class eventvendoroverview extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;


    RecyclerView servicesselected;

    ImageView backbuttonImage, eventimage;

    LinearLayout nextbuttonLayout, messagesbuttonlayout, rejectproposallayout, userratinglayout;



    TextView eventtitletext, eventusernametext, eventlocationtext, userratingtext, eventdateviewtext, eventbudgetpercenttext;


    String eventimageurl;

    RecyclerView budgetrecyclerview, eventservicesrecycler;

    eventdetailsinfoRecyclerAdapter eventdetailsadapter;


    //intent values here

    String event_idIntent;
    String vendor_idIntent;
    String image_locationIntent;
    String capability_idIntent;
    String event_statusIntent;
    String agreed_priceIntent;
    String assigned_byIntent;
    String time_assignedIntent;
    String event_nameIntent;

    String event_locationIntent;
    String event_cityIntent;
    String event_stateIntent;
    String postal_codeIntent;
    String event_countryIntent;
    String longitudeIntent;
    String latitudeIntent;
    String deposit_percentageIntent;
    String deposit_balanceIntent;
    String eventcapability_nameIntent;
    String event_vibeIntent;
    String event_typeIntent;
    String event_vendor_nameIntent;

    //for array population

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

    ArrayList<sabaEventItem> eventwholearray;



    private int GALLERY = 2, CAMERA = 1;

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
        startActivity(new Intent(getApplicationContext(), sabaVendorDrawerActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventvendoroverview);


        app = (sabaapp) getApplicationContext();
        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressbar);
        hideProgressBar();

        backbuttonImage = (ImageView) findViewById(R.id.backbuttonimage);
        eventimage = (ImageView) findViewById(R.id.eventimagedisplay);


        nextbuttonLayout = (LinearLayout) findViewById(R.id.acceptproposallayoutbutton);
        messagesbuttonlayout = (LinearLayout) findViewById(R.id.sendmessagelayoutbutton);
        rejectproposallayout = (LinearLayout) findViewById(R.id.rejectproposallayoutbutton);
        userratinglayout = (LinearLayout) findViewById(R.id.userratinglayout);

        eventtitletext = (TextView) findViewById(R.id.eventtitletext);
        eventusernametext = (TextView) findViewById(R.id.clientname);
        eventlocationtext = (TextView) findViewById(R.id.eventlocationlabel);
        userratingtext  = (TextView) findViewById(R.id.userratingtext);

        eventdateviewtext = (TextView)findViewById(R.id.eventdatelabel) ;
        eventbudgetpercenttext = (TextView)findViewById(R.id.eventbudgettextlabel);

        eventservicesrecycler = (RecyclerView) findViewById(R.id.eventdetailsrecycler);
        budgetrecyclerview = (RecyclerView) findViewById(R.id.budgetrecycler);

        //get intent values here

        Intent intent = getIntent();



        event_idIntent = intent.getStringExtra("event_id");
        vendor_idIntent = intent.getStringExtra("vendor_id");
        image_locationIntent = intent.getStringExtra("image_location");
        capability_idIntent = intent.getStringExtra("capability_id");
        event_statusIntent = intent.getStringExtra("event_status");
        agreed_priceIntent = intent.getStringExtra("agreed_price");
        assigned_byIntent = intent.getStringExtra("assigned_by");
        time_assignedIntent = intent.getStringExtra("time_assigned");
        event_nameIntent = intent.getStringExtra("event_name");

        event_locationIntent = intent.getStringExtra("event_location");
        event_cityIntent = intent.getStringExtra("event_city");
        event_stateIntent = intent.getStringExtra("event_state");
        postal_codeIntent = intent.getStringExtra("postal_code");
        event_countryIntent = intent.getStringExtra("event_country");
        longitudeIntent = intent.getStringExtra("longitude");
        latitudeIntent = intent.getStringExtra("latitude");
        deposit_percentageIntent = intent.getStringExtra("deposit_percentage");
        deposit_balanceIntent = intent.getStringExtra("deposit_balance");
        eventcapability_nameIntent = intent.getStringExtra("eventcapability_name");
        event_vibeIntent = intent.getStringExtra("event_vibe");
        event_typeIntent = intent.getStringExtra("event_type");
        event_vendor_nameIntent = intent.getStringExtra("event_vendor_name");

        //end of get intent values

        //setup the recycler values


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
        eventwholearray = new ArrayList<sabaEventItem>();


        eventservicesrecycler.setLayoutManager(new LinearLayoutManager(eventvendoroverview.this, LinearLayoutManager.VERTICAL, false));

        eventdetailsadapter=new eventdetailsinfoRecyclerAdapter(eventwholearray,eventvendoroverview.this, eventvendoroverview.this, app);
        eventservicesrecycler.setAdapter(eventdetailsadapter);


        //start of values

        //set for event name

        eventtitletext.setText(event_nameIntent);

        //load event image
        int radius = 15; // corner radius in pixels

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new RoundedCorners(radius));
        try{

            Glide.with(eventvendoroverview.this)
                    .load(image_locationIntent)
                    .placeholder(R.drawable.defaultimage)
                    .apply(requestOptions)
                    .into(eventimage);

        } catch (Exception e) {

            Log.e("LOAD event assigned IMAGE ERROR: ",""+e);

            Glide.with(eventvendoroverview.this).load(R.drawable.defaultimage).apply(requestOptions).into(eventimage);

        }


        eventusernametext.setText(event_vendor_nameIntent);
        eventlocationtext.setText(event_locationIntent);

        eventdateviewtext = (TextView)findViewById(R.id.eventdatelabel) ;
        if(agreed_priceIntent!=null && agreed_priceIntent.isEmpty() && deposit_percentageIntent!=null && !deposit_percentageIntent.isEmpty()){
            double valuethis = 0;
            try{
                valuethis = Double.parseDouble(agreed_priceIntent);
            } catch (Exception e) {

            }
            double depositpercentage = 0;

            if(valuethis >0){

                try{
                    depositpercentage = Double.parseDouble(deposit_percentageIntent);
                } catch (Exception e) {

                }
                if(depositpercentage>0){
                    depositpercentage = depositpercentage/100;
                }
            }

            double valuefinal = 0;

            if (depositpercentage>0){

                valuefinal = depositpercentage* valuethis;

            }

            eventbudgetpercenttext.setText("$"+agreed_priceIntent+" / $"+valuefinal);
        }


        eventwholearray.clear();

        sabaEventItem item3=new sabaEventItem();
        item3.setevent_nameAssigned("Event Name");
        item3.setevent_imagelocationAssigned("storefront");
        item3.setevent_locationAssigned(event_nameIntent);
        eventwholearray.add(item3);

        //add for event id
        sabaEventItem item4=new sabaEventItem();
        item4.setevent_nameAssigned("Event Status");
        item4.setevent_imagelocationAssigned("success_icon");
        item4.setevent_locationAssigned(event_statusIntent);
        eventwholearray.add(item4);

        //add for event status
        sabaEventItem item=new sabaEventItem();
        item.setevent_nameAssigned("Event ID");
        item.setevent_imagelocationAssigned("showcalendar");
        item.setevent_locationAssigned(event_idIntent);
        eventwholearray.add(item);
        //add for vendor id
        sabaEventItem item2=new sabaEventItem();
        item2.setevent_nameAssigned("Vendor ID");
        item2.setevent_imagelocationAssigned("shop_vector_icon_small");
        item2.setevent_locationAssigned(vendor_idIntent);
        //setImagebitmap
        eventwholearray.add(item2);

        sabaEventItem item13=new sabaEventItem();
        item13.setevent_nameAssigned("Budget Proposal");
        item13.setevent_imagelocationAssigned("writing");
        item13.setevent_locationAssigned("$ "+agreed_priceIntent);
        eventwholearray.add(item13);


        sabaEventItem item5=new sabaEventItem();
        item5.setevent_nameAssigned("Event Location");
        item5.setevent_imagelocationAssigned("place");
        item5.setevent_locationAssigned(event_locationIntent);
        eventwholearray.add(item5);

        sabaEventItem item6=new sabaEventItem();
        item6.setevent_nameAssigned("City");
        item6.setevent_imagelocationAssigned("place");
        item6.setevent_locationAssigned(event_cityIntent);
        eventwholearray.add(item6);

        sabaEventItem item7=new sabaEventItem();
        item7.setevent_nameAssigned("State");
        item7.setevent_imagelocationAssigned("place");
        item7.setevent_locationAssigned(event_stateIntent);
        eventwholearray.add(item7);

        sabaEventItem item8=new sabaEventItem();
        item8.setevent_nameAssigned("Postal code/Zip Code");
        item8.setevent_imagelocationAssigned("place");
        item8.setevent_locationAssigned(postal_codeIntent);
        eventwholearray.add(item8);

        sabaEventItem item9=new sabaEventItem();
        item9.setevent_nameAssigned("Country");
        item9.setevent_imagelocationAssigned("locationsmall");
        item9.setevent_locationAssigned(event_countryIntent);
        eventwholearray.add(item9);

        sabaEventItem item10=new sabaEventItem();
        item10.setevent_nameAssigned("Service Requested");
        item10.setevent_imagelocationAssigned("imgcompleteprof");
        item10.setevent_locationAssigned(eventcapability_nameIntent);
        eventwholearray.add(item10);

        sabaEventItem item11=new sabaEventItem();
        item11.setevent_nameAssigned("Vibe");
        item11.setevent_imagelocationAssigned("eventsplaceholder");
        item11.setevent_locationAssigned(event_vibeIntent);
        eventwholearray.add(item11);

        sabaEventItem item12=new sabaEventItem();
        item12.setevent_nameAssigned("Type");
        item12.setevent_imagelocationAssigned("campaign");
        item12.setevent_locationAssigned(event_vibeIntent);
        eventwholearray.add(item12);

        sabaEventItem item14=new sabaEventItem();
        item14.setevent_nameAssigned("Assigned By");
        item14.setevent_imagelocationAssigned("useravatar");
        item14.setevent_locationAssigned(assigned_byIntent);
        eventwholearray.add(item14);


        eventdetailsadapter.notifyDataSetChanged();

        //end of setup of recycler values





        backbuttonImage.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), sabaVendorDrawerActivity.class));


        });

        messagesbuttonlayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();




        });

        rejectproposallayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();




        });


        nextbuttonLayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(chooseservices.this, "Messages link clicked", Toast.LENGTH_SHORT).show();




        });


        messagesbuttonlayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            /*ArrayList<String> selectedCapabilities =
                    sabaeventsadapter.getSelectedCapabilityNames();*/






            Intent intent2 = new Intent(eventvendoroverview.this, conversationactivity.class);
            intent2.putExtra("chatname", event_nameIntent);
            intent2.putExtra("activeuser", assigned_byIntent);
            startActivity(intent2);


        });







    }



}
