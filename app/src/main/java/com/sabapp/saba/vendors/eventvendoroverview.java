package com.sabapp.saba.vendors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.sabaVendorDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

public class eventvendoroverview extends AppCompatActivity {

    AVLoadingIndicatorView progressindicator;

    sabaapp app;

    String fcm_token = null;

    AppCompatSpinner setup_type_spinner;

    JSONArray dataobj;


    RecyclerView servicesselected;

    ImageView backbuttonImage, eventimage;

    LinearLayout nextbuttonLayout, messagesbuttonlayout, rejectproposallayout, userratinglayout;



    TextView eventtitletext, eventusernametext, eventlocationtext, userratingtext;


    String eventimageurl;

    RecyclerView budgetrecyclerview, eventservicesrecycler;


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

        eventservicesrecycler = (RecyclerView) findViewById(R.id.eventdetailsrecycler);
        budgetrecyclerview = (RecyclerView) findViewById(R.id.budgetrecycler);




        if(eventimageurl!=null && eventimageurl.isEmpty())
        {


            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));
            try{

                Glide.with(eventvendoroverview.this)
                        .load(eventimageurl)
                        .placeholder(R.drawable.defaultimage)
                        .apply(requestOptions)
                        .into(eventimage);

            } catch (Exception e) {

                Log.e("LOAD event assigned IMAGE ERROR: ",""+e);

                Glide.with(eventvendoroverview.this).load(R.drawable.defaultimage).apply(requestOptions).into(eventimage);

            }




        }
        else
        {

            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));

            Glide.with(eventvendoroverview.this).load(R.drawable.defaultimage).apply(requestOptions).into(eventimage);
        }



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







    }



}
