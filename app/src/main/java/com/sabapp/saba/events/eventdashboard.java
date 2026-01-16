package com.sabapp.saba.events;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.EventTwinBottomAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class eventdashboard extends AppCompatActivity {

    // Top tabs
    private Button tabEventTwin, tabMoodBoard, tabVendors, tabTimeline, tabBudget;

    // Containers for different screens
    private ViewGroup eventTwinContainer, moodBoardContainer, vendorsContainer, timelineContainer, budgetContainer;

    private Button tabActionFeed, tabAlerts, tabSuggestions, tabDrafts;

    private ArrayList<sabaEventItem> eventTwinArray = new ArrayList<>();
    private EventTwinBottomAdapter eventTwinAdapter;

    private RecyclerView eventTwinRecyclerView;

    AVLoadingIndicatorView progressindicator;

    LinearLayout backbuttontitlelayout;

    sabaapp app;

    String EventId,event_name,event_time,event_location,event_budget,event_budgetspent,event_setupstatus,event_eventstatus,event_plannerid,image_location,time_setup,user_id;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventdashboard); // your XML root
        app = (sabaapp) getApplicationContext();

        progressindicator = (AVLoadingIndicatorView) findViewById(R.id.progressbar);

        backbuttontitlelayout = (LinearLayout)findViewById(R.id.btn_back);

        Intent intent=getIntent();

        EventId=intent.getStringExtra("event_id");

        event_name=intent.getStringExtra("event_name");
        event_time=intent.getStringExtra("event_time");
        event_location=intent.getStringExtra("event_location");
        event_budget=intent.getStringExtra("event_budget");
        event_budgetspent=intent.getStringExtra("event_budgetspent");
        event_setupstatus=intent.getStringExtra("event_setupstatus");
        event_eventstatus=intent.getStringExtra("event_eventstatus");
        event_plannerid=intent.getStringExtra("event_plannerid");
        image_location=intent.getStringExtra("image_location");
        time_setup=intent.getStringExtra("time_setup");
        user_id=intent.getStringExtra("user_id");


        backbuttontitlelayout.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            //Toast.makeText(getContext(), "Create New Event clicked", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(getApplicationContext(), sabaDrawerActivity.class);
            intent2.putExtra("createadrad", "gotowhatsappbotmaker");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);


        });

        // Top Tabs
        tabEventTwin = findViewById(R.id.tab_event_twin);
        tabMoodBoard = findViewById(R.id.tab_mood_board);
        tabVendors = findViewById(R.id.tab_vendors);
        tabTimeline = findViewById(R.id.tab_timeline);
        tabBudget = findViewById(R.id.tab_budget);

        // Containers for each tab content
        eventTwinContainer = findViewById(R.id.container_event_twin);
        moodBoardContainer = findViewById(R.id.container_mood_board);
        vendorsContainer = findViewById(R.id.container_vendors);
        timelineContainer = findViewById(R.id.container_timeline);
        budgetContainer = findViewById(R.id.container_budget);

        // Default: show Event Twin
        showTab("event");

        // Set click listeners
        tabEventTwin.setOnClickListener(v -> showTab("event"));
        tabMoodBoard.setOnClickListener(v -> showTab("mood"));
        tabVendors.setOnClickListener(v -> showTab("vendors"));
        tabTimeline.setOnClickListener(v -> showTab("timeline"));
        tabBudget.setOnClickListener(v -> showTab("budget"));



        //initiate event twin controller
        setupEventTwinRecycler();






    }

    private void showTab(String tab) {
        // Reset all backgrounds
        resetTopTabStyles();

        // Hide all containers
        eventTwinContainer.setVisibility(View.GONE);
        moodBoardContainer.setVisibility(View.GONE);
        vendorsContainer.setVisibility(View.GONE);
        timelineContainer.setVisibility(View.GONE);
        budgetContainer.setVisibility(View.GONE);

        // Show selected
        switch(tab) {
            case "event":
                eventTwinContainer.setVisibility(View.VISIBLE);
                tabEventTwin.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabEventTwin.setTextColor(getResources().getColor(R.color.text_light));
                initEventTwinContainer();
                break;
            case "mood":
                moodBoardContainer.setVisibility(View.VISIBLE);
                tabMoodBoard.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabMoodBoard.setTextColor(getResources().getColor(R.color.text_light));
                break;
            case "vendors":
                vendorsContainer.setVisibility(View.VISIBLE);
                tabVendors.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabVendors.setTextColor(getResources().getColor(R.color.text_light));
                break;
            case "timeline":
                timelineContainer.setVisibility(View.VISIBLE);
                tabTimeline.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabTimeline.setTextColor(getResources().getColor(R.color.text_light));
                break;
            case "budget":
                budgetContainer.setVisibility(View.VISIBLE);
                tabBudget.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabBudget.setTextColor(getResources().getColor(R.color.text_light));
                break;
        }
    }

    private void resetTopTabStyles() {
        Button[] tabs = {tabEventTwin, tabMoodBoard, tabVendors, tabTimeline, tabBudget};
        for (Button tab : tabs) {
            tab.setBackgroundColor(Color.TRANSPARENT);
            tab.setTextColor(getResources().getColor(R.color.text_primary));
        }
    }

    //initialize event twin services
    //function for event twin

    private void initEventTwinTabs() {
        tabActionFeed = findViewById(R.id.tab_action_feed);
        tabAlerts = findViewById(R.id.tab_alerts);
        tabSuggestions = findViewById(R.id.tab_suggestions);
        tabDrafts = findViewById(R.id.tab_drafts);
        eventTwinRecyclerView = findViewById(R.id.event_twin_recycler); // make sure ID exists

        // Default data
        showEventTwinTab("action");

        tabActionFeed.setOnClickListener(v -> showEventTwinTab("action"));
        tabAlerts.setOnClickListener(v -> showEventTwinTab("alerts"));
        tabSuggestions.setOnClickListener(v -> showEventTwinTab("suggestions"));
        tabDrafts.setOnClickListener(v -> showEventTwinTab("drafts"));
    }

    private void showEventTwinTab(String tab) {
        // Reset styles
        Button[] subTabs = {tabActionFeed, tabAlerts, tabSuggestions, tabDrafts};
        for (Button b : subTabs) {
            b.setBackgroundColor(Color.TRANSPARENT);
            b.setTextColor(getResources().getColor(R.color.text_primary));
        }


        switch(tab) {
            case "action":

                tabActionFeed.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabActionFeed.setTextColor(getResources().getColor(R.color.text_light));
                getEventTwinList("action");
                break;
            case "alerts":

                tabAlerts.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabAlerts.setTextColor(getResources().getColor(R.color.text_light));
                getEventTwinList("alerts");
                break;
            case "suggestions":

                tabSuggestions.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabSuggestions.setTextColor(getResources().getColor(R.color.text_light));
                getEventTwinList("suggestions");
                break;
            case "drafts":

                tabDrafts.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabDrafts.setTextColor(getResources().getColor(R.color.text_light));
                getEventTwinList("drafts");
                break;
            default:
                tabActionFeed.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabActionFeed.setTextColor(getResources().getColor(R.color.text_light));
                getEventTwinList("action");
                break;
        }


    }

    private void setupEventTwinRecycler() {


        eventTwinAdapter = new EventTwinBottomAdapter(eventTwinArray,this, eventdashboard.this, app);
        eventTwinRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventTwinRecyclerView.setAdapter(eventTwinAdapter);
    }

    private void getEventTwinList(String type) {

        showProgressBar();

        String endpoint = "https://api.sabaapp.co/v0/event_twin?type=" + type;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                endpoint,
                null,
                response -> {

                    hideProgressBar();
                    Log.d("EVENT_TWIN_RESPONSE", response.toString());

                    try {
                        String status = response.getString("STATUS");

                        if (!status.equalsIgnoreCase("success")) {
                            Log.e("EVENT_TWIN", "API returned non-success");
                            return;
                        }

                        JSONArray dataArray = response.getJSONArray("DATA");

                        // Clear existing data BEFORE adding new
                        eventTwinArray.clear();

                        if (dataArray.length() == 0) {
                            // Optional: Empty-state placeholder
                            sabaEventItem emptyItem = new sabaEventItem();
                            emptyItem.seteventName("No items available");
                            emptyItem.seteventStatus("empty");
                            eventTwinArray.add(emptyItem);

                        } else {

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject obj = dataArray.getJSONObject(i);

                                sabaEventItem item = new sabaEventItem();

                                item.seteventName(obj.optString("title"));
                                item.seteventLocation(obj.optString("description"));
                                item.seteventStatus(obj.optString("status"));
                                item.seteventTime(obj.optString("created_at"));
                                item.setevent_Id(obj.optString("id"));

                                // Add to list
                                eventTwinArray.add(item);
                            }
                        }

                        // Notify adapter ONCE
                        eventTwinAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("EVENT_TWIN_PARSE", e.getMessage());
                    }

                },
                error -> {
                    hideProgressBar();
                    Log.e("EVENT_TWIN_ERROR", error.toString());
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String creds = String.format("%s:%s",
                        app.getApiusername(),
                        app.getApipassword()
                );

                String auth = "Basic " +
                        Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(this).add(request);
    }


    private void initEventTwinContainer() {

        //EventId,event_name,event_time,event_location,event_budget,event_budgetspent,event_setupstatus,event_eventstatus,event_plannerid,image_location,time_setup,user_id

        LinearLayout buttonsContainer = findViewById(R.id.event_twin_buttons_container);

        TextView eventname = findViewById(R.id.eventnamelabel);
        TextView countdowncardtext = findViewById(R.id.eventcountdown_text);

        //component status

        LinearLayout statuslayout = findViewById(R.id.eventstatusbadge_layout);
        ImageView statusbarimage = findViewById(R.id.eventstatusbadge_icon);
        TextView statusbartext = findViewById(R.id.eventstatusbadge_text);
        ImageView statusbarmoreicon = findViewById(R.id.eventstatusbadge_moreicon);


        //timelinehealth status indicators

        LinearLayout timelinehealthlayout = findViewById(R.id.timelinehealthvaluelayout);
        TextView timelinehealth = findViewById(R.id.timelinehealthtext);

        TextView vendorraedinesscountertext = findViewById(R.id.vendorreadinesscardvalue);
        TextView budgethealthcountertext = findViewById(R.id.budgethealthtextvalue);

        if(event_name!=null){
            eventname.setText(event_name);
        }else{

            eventname.setText("Event title");
        }


        if(event_time!=null){

            countdowncardtext.setText(event_time+" Days to Go");
        }else{

            countdowncardtext.setText("No Days to Go");
        }






        Button tabActionFeed = findViewById(R.id.tab_action_feed);
        Button tabAlerts = findViewById(R.id.tab_alerts);
        Button tabSuggestions = findViewById(R.id.tab_suggestions);
        Button tabDrafts = findViewById(R.id.tab_drafts);

        tabActionFeed.setOnClickListener(v -> getEventTwinList("action"));
        tabAlerts.setOnClickListener(v -> getEventTwinList("alerts"));
        tabSuggestions.setOnClickListener(v -> getEventTwinList("suggestions"));
        tabDrafts.setOnClickListener(v -> getEventTwinList("drafts"));

        // Load default tab
        initEventTwinTabs();


    }

    //volleyfor feticng bottom tab data

    //for fetching vendors
    /*private void initVendorsContainer() {
        RecyclerView recyclerView = findViewById(R.id.vendors_recycler);
        VendorsAdapter adapter = new VendorsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchVendorsData(adapter);
    }

    private void fetchVendorsData(VendorsAdapter adapter) {
        String url = "https://your-api.com/vendors";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Vendor> items = parseVendors(response);
                    adapter.setData(items);
                },
                error -> {
                    Toast.makeText(this, "Error loading vendors", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }*/

    //end for fetching vendors


}

