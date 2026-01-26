package com.sabapp.saba.events;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.EventTwinBottomAdapter;
import com.sabapp.saba.adapters.SelectedCapabilityAdapter;
import com.sabapp.saba.adapters.budgetlisteditRecyclerAdapter;
import com.sabapp.saba.adapters.capabilityRecyclerAdapter;
import com.sabapp.saba.adapters.dashboardclientcapabilityselectRecycler;
import com.sabapp.saba.adapters.eventoverviewalertsRecyclerAdapter;
import com.sabapp.saba.adapters.vendorlisteventRecyclerAdapter;
import com.sabapp.saba.adapters.vendormatchingRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.SelectedCapabilityItem;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.sabaDrawerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class eventdashboard extends AppCompatActivity implements OnOverviewAlertClickListener {

    // Top tabs
    private Button tabEventOverview,tabEventTwin, tabMoodBoard, tabVendors, tabTimeline, tabBudget;

    // Containers for different screens
    private ViewGroup eventOverviewContainer,  eventTwinContainer, moodBoardContainer, vendorsContainer, timelineContainer, budgetContainer;

    private Button tabActionFeed, tabAlerts, tabSuggestions, tabDrafts;

    private Button vendortab_vendor_list, ventortab_date, ventortab_budget,vendortab_style, vendortab_related,vendortab_chat;

    private ArrayList<sabaEventItem> eventTwinArray = new ArrayList<>();
    private EventTwinBottomAdapter eventTwinAdapter;

    private RecyclerView eventTwinRecyclerView;

    AVLoadingIndicatorView progressindicator;

    LinearLayout backbuttontitlelayout;

    sabaapp app;

    String EventId,event_name,event_time,eventtimeunix, event_location,event_budget,event_budgetspent,event_setupstatus,event_eventstatus,event_plannerid,image_location,time_setup,user_id;



    //for eventalerts
    ArrayList<String> eventoverview_alertname;

    ArrayList<String> eventoverview_capabilityid;
    ArrayList<String> eventoverview_alertid;

    ArrayList<String> eventoverview_alerttype;

    ArrayList<String> eventoverview_alertcontent;


    ArrayList<String> eventoverview_alertimagelocation;

    //for eventtiwn alerts

    ArrayList<String> eventoverview_alertnameTwin;

    ArrayList<String> eventoverview_capabilityidTwin;
    ArrayList<String> eventoverview_alertidTwin;

    ArrayList<String> eventoverview_alerttypeTwin;

    ArrayList<String> eventoverview_alertcontentTwin;


    ArrayList<String> eventoverview_alertimagelocationTwin;


    //for budget list

    ArrayList<String> budget_event_id;
    ArrayList<String> budget_capability_id;
    ArrayList<String> budget_allocated_amount;
    ArrayList<String> budget_amount_paid;
    ArrayList<String> budget_currency;
    ArrayList<String> budget_payment_status;
    ArrayList<String> budget_last_payment_date ;
    ArrayList<String> budget_planner_id;

    //for vendor list

    ArrayList<String> capability_codeList;

    ArrayList<String> base_pricelist;
    ArrayList<JSONObject> capability_detailslist;
    ArrayList<String> capability_idlist;
    ArrayList<String> service_image_locationlist;


    ArrayList<String> vendorserviceimage_idList;
    ArrayList<String> vendorserviceimagelocationList;
    ArrayList<String> vendoridList;

    ArrayList<String> vendornameList;
    ArrayList<String> vendorcapabilitynameList;
    ArrayList<String> vendorlocationList;

    ArrayList<sabaEventItem> eventwholearray;

    ArrayList<sabaEventItem> eventalertsholearrey;

    vendorlisteventRecyclerAdapter sabaeventsadapter;

    eventoverviewalertsRecyclerAdapter eventalertadapter;

    RecyclerView servicesselected,capabilitiesselected,budgetselectedrecycler;

    RecyclerView eventoverviewalertrecycler;

    ArrayList<SelectedCapabilityItem> capabilityList;

    JSONArray dataobj;

    //get event time

    int daysDifference;
    int hoursDifference;
    int minutesDifference;
    int secondsDifference;


    //eventoverviewvalues

    int numberofservices = 0;
    int numberofvendors = 0;
    int bookedvendorsnumber =0;
    int numberbookedservices =0;




    TextView budgetamountTextoverview, servicesbookedoverviewtotaltextoverview,servicestotaloverviewtextoverview, vendorscontractedctedtotaltextoverview, vendorstotaltextoverview, budgetamountpercentagetextoverview;


    //for service budgets


    ArrayList<String> event_capability_codeList;
    ArrayList<String> event_capability_nameList;
    ArrayList<String> event_categoryList;
    ArrayList<String> event_capability_imageidList;
    ArrayList<String> event_capability_image_locationList;

    ArrayList<sabaEventItem> capabilitywholearray;

    dashboardclientcapabilityselectRecycler capabilityadapter;

    ArrayList<sabaEventItem> budgetwholearray;

    budgetlisteditRecyclerAdapter budgetadapter;

    LinearLayout capabilitylistlayout, capabilitynextbutton, updatebudgetnextbutton;

    List<SelectedCapabilityItem> draftbudgetcapabilityList;

    double budgetamountvaluedouble;

    @Override
    public void onAlertClicked(String alertType) {

        if ("service_budget".equalsIgnoreCase(alertType)) {
            showTab("budget");
        } else if ("add_vendor".equalsIgnoreCase(alertType)) {
            showTab("vendors");

        } else if ("service_add".equalsIgnoreCase(alertType)) {
            showTab("vendors");

        }else if("send_message".equalsIgnoreCase(alertType)) {

            Intent intent = new Intent(this, sabaDrawerActivity.class);
            intent.putExtra("open_fragment", "messages");
            startActivity(intent);

        }else if("approve_payment".equalsIgnoreCase(alertType)) {

            Intent intent = new Intent(this, sabaDrawerActivity.class);
            intent.putExtra("open_fragment", "payment");
            startActivity(intent);

        }else if("add_payment".equalsIgnoreCase(alertType)) {

            Intent intent = new Intent(this, sabaDrawerActivity.class);
            intent.putExtra("open_fragment", "payment");
            startActivity(intent);
        }

        //end of on alert clicked
    }

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
        eventtimeunix=intent.getStringExtra("event_timeunix");

        if(isValidUnixTimestamp(eventtimeunix)){

            TimeDiff diff = getSignedTimeDifference(eventtimeunix);


            daysDifference = diff.days;
            hoursDifference = diff.hours;
            minutesDifference = diff.minutes;
            secondsDifference = diff.seconds;


        }

        Serializable serializable = getIntent().getSerializableExtra("selected_services");
        if (serializable instanceof ArrayList<?>) {
            capabilityList =
                    (ArrayList<SelectedCapabilityItem>) serializable;

            // Now you can use this list to populate a RecyclerView
                /*budgetlistRecycler.setLayoutManager(new LinearLayoutManager(this));
                SelectedCapabilityAdapter adapter = new SelectedCapabilityAdapter(capabilityList, this);
                budgetlistRecycler.setAdapter(adapter);*/

            // Optional: Log to check
            for (SelectedCapabilityItem item : capabilityList) {
                Log.d("CAPABILITY_ITEM", item.getCode() + " - " + item.getName());
            }
        }

        if (capabilityList == null) {
            capabilityList = new ArrayList<>();
        }


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
        tabEventOverview = findViewById(R.id.tab_event_overview);

        //buttons
        capabilitynextbutton = findViewById(R.id.nextbuttonlayout);

        // Containers for each tab content
        eventTwinContainer = findViewById(R.id.container_event_twin);
        moodBoardContainer = findViewById(R.id.container_mood_board);
        vendorsContainer = findViewById(R.id.container_vendors);
        timelineContainer = findViewById(R.id.container_timeline);
        budgetContainer = findViewById(R.id.container_budget);
        eventOverviewContainer =findViewById(R.id.container_event_overview);

        //initiate event twin controller

        setupEventoverviewAlertListRecycler();
        setupEventTwinRecycler();

        setupVendorListRecycler();

        setupBudgetListRecycler();

        setupBudgetCapabilityListRecycler();

        // Default: show Event Twin
        showTab("overview");

        // Set click listeners
        tabEventOverview.setOnClickListener(v -> showTab("overview"));
        tabEventTwin.setOnClickListener(v -> showTab("event"));
        tabMoodBoard.setOnClickListener(v -> showTab("mood"));
        tabVendors.setOnClickListener(v -> showTab("vendors"));
        tabTimeline.setOnClickListener(v -> showTab("timeline"));
        tabBudget.setOnClickListener(v -> showTab("budget"));


        capabilitynextbutton.setOnClickListener(v -> {


            HashMap<String, String> selected =
                    capabilityadapter.getSelectedCapabilities();

            if (selected.isEmpty()) {
                Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show();
                return;
            }else{
                //valid selection has been made

                // Convert to a List of SelectedCapabilityItem
                draftbudgetcapabilityList = new ArrayList<>();
                for (Map.Entry<String, String> entry : selected.entrySet()) {
                    draftbudgetcapabilityList.add(new SelectedCapabilityItem(entry.getKey(), entry.getValue()));
                }

                budgetselectedrecycler.setVisibility(View.VISIBLE);
                updatebudgetnextbutton.setVisibility(View.VISIBLE);
                capabilitylistlayout.setVisibility(View.GONE);

                capabilitiesselected.setVisibility(View.GONE);


                // Set up RecyclerView
                budgetselectedrecycler.setLayoutManager(new LinearLayoutManager(this));
                SelectedCapabilityAdapter adapter = new SelectedCapabilityAdapter(draftbudgetcapabilityList, this, budgetamountvaluedouble);
                budgetselectedrecycler.setAdapter(adapter);


              //end of valid selection has been made
            }

            Log.d("SELECTED SERVICES CHOOSE", String.valueOf(selected));



            /*Intent intent2 = new Intent(chooseservices.this, setupbudgets.class);
            //intent2.putStringArrayListExtra("selected_services", selectedCapabilities);
            intent2.putExtra("selected_services", selected);
            intent2.putExtra("event_id", EventId);
            startActivity(intent2);*/


        });


        updatebudgetnextbutton.setOnClickListener(v -> {
            //start of budget next button

            sendupdatedbudgetPost();


         //end of budget next button
        });


    }

    private void showTab(String tab) {
        // Reset all backgrounds
        resetTopTabStyles();

        // Hide all containers
        eventOverviewContainer.setVisibility(View.GONE);
        eventTwinContainer.setVisibility(View.GONE);
        moodBoardContainer.setVisibility(View.GONE);
        vendorsContainer.setVisibility(View.GONE);
        timelineContainer.setVisibility(View.GONE);
        budgetContainer.setVisibility(View.GONE);

        // Show selected
        switch(tab) {
            case "overview":
                eventOverviewContainer.setVisibility(View.VISIBLE);
                tabEventOverview.setBackgroundResource(R.drawable.bg_pill_button_primary);
                tabEventOverview.setTextColor(getResources().getColor(R.color.text_light));
                initOverviewContainer();
                break;
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
                initVendorContainer();
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
                initBudgetContainer();
                break;
        }
    }

    private void resetTopTabStyles() {
        Button[] tabs = {tabEventOverview,tabEventTwin, tabMoodBoard, tabVendors, tabTimeline, tabBudget};
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
         // make sure ID exists

        // Default data
        showEventTwinTab("action");

        tabActionFeed.setOnClickListener(v -> showEventTwinTab("action"));
        tabAlerts.setOnClickListener(v -> showEventTwinTab("alerts"));
        tabSuggestions.setOnClickListener(v -> showEventTwinTab("suggestions"));
        tabDrafts.setOnClickListener(v -> showEventTwinTab("drafts"));
    }

    private void initVendortopTabs() {

        vendortab_vendor_list = findViewById(R.id.tab_vendor_list);
        ventortab_date = findViewById(R.id.tab_date);
        ventortab_budget = findViewById(R.id.tab_budget);
        vendortab_style = findViewById(R.id.tab_style);
        vendortab_related = findViewById(R.id.tab_related);
        vendortab_chat = findViewById(R.id.tab_chat);


        // Default data
        showVendorbottomTab("action");

        vendortab_vendor_list.setOnClickListener(v -> showVendorbottomTab("vendor"));
        ventortab_date.setOnClickListener(v -> showVendorbottomTab("date"));
        ventortab_budget.setOnClickListener(v -> showVendorbottomTab("budget"));
        vendortab_style.setOnClickListener(v -> showVendorbottomTab("style"));


        vendortab_related.setOnClickListener(v -> showVendorbottomTab("related"));
        vendortab_chat.setOnClickListener(v -> showVendorbottomTab("chat"));
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

    private void showVendorbottomTab(String tab) {
        // Reset styles

        Button[] subTabs = {vendortab_vendor_list, ventortab_date, ventortab_budget, vendortab_style,vendortab_related,vendortab_chat};
        for (Button b : subTabs) {
            if (b == null) continue;

            b.setBackgroundColor(Color.TRANSPARENT);
            b.setTextColor(getResources().getColor(R.color.text_primary));
        }


        switch(tab) {
            case "vendor":

                vendortab_vendor_list.setBackgroundResource(R.drawable.bg_pill_button_primary);
                vendortab_vendor_list.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("vendor");
                break;
            case "date":

                ventortab_date.setBackgroundResource(R.drawable.bg_pill_button_primary);
                ventortab_date.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("alerts");
                break;
            case "budget":

                ventortab_budget.setBackgroundResource(R.drawable.bg_pill_button_primary);
                ventortab_budget.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("suggestions");
                break;
            case "style":

                vendortab_style.setBackgroundResource(R.drawable.bg_pill_button_primary);
                vendortab_style.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("drafts");
                break;
            case "related":

                vendortab_related.setBackgroundResource(R.drawable.bg_pill_button_primary);
                vendortab_related.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("suggestions");
                break;
            case "chat":

                vendortab_chat.setBackgroundResource(R.drawable.bg_pill_button_primary);
                vendortab_chat.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("drafts");
                break;
            default:
                vendortab_vendor_list.setBackgroundResource(R.drawable.bg_pill_button_primary);
                vendortab_vendor_list.setTextColor(getResources().getColor(R.color.text_light));
                getVendorbottomtabList("action");
                break;
        }


    }

    private void setupEventTwinRecycler() {

        eventoverview_alertnameTwin = new ArrayList<String>();
        eventoverview_capabilityidTwin = new ArrayList<String>();
        eventoverview_alertidTwin = new ArrayList<String>();
        eventoverview_alerttypeTwin= new ArrayList<String>();
        eventoverview_alertcontentTwin = new ArrayList<String>();
        eventoverview_alertimagelocationTwin = new ArrayList<String>();
        eventTwinArray = new ArrayList<sabaEventItem>();

        eventTwinRecyclerView = findViewById(R.id.event_twin_recycler);


        eventTwinAdapter = new EventTwinBottomAdapter(eventTwinArray,this, eventdashboard.this,this, app);
        eventTwinRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventTwinRecyclerView.setAdapter(eventTwinAdapter);
    }


    private void setupVendorListRecycler() {

        base_pricelist = new ArrayList<String>();
        capability_detailslist = new ArrayList<JSONObject>();
        capability_idlist = new ArrayList<String>();
        service_image_locationlist = new ArrayList<String>();
        eventwholearray = new ArrayList<sabaEventItem>();


        vendorserviceimage_idList = new ArrayList<String>();
        vendorserviceimagelocationList = new ArrayList<String>();
        vendoridList = new ArrayList<String>();

        vendornameList = new ArrayList<String>();
        vendorcapabilitynameList = new ArrayList<String>();
        vendorlocationList = new ArrayList<String>();

        servicesselected = findViewById(R.id.recyclerView_vendors);


        servicesselected.setLayoutManager(new LinearLayoutManager(eventdashboard.this, LinearLayoutManager.VERTICAL, false));

        sabaeventsadapter=new vendorlisteventRecyclerAdapter(eventwholearray,context, eventdashboard.this, app);
        servicesselected.setAdapter(sabaeventsadapter);



    }

    private void setupBudgetListRecycler() {

        budget_event_id = new ArrayList<String>();
        budget_capability_id = new ArrayList<String>();
        budget_allocated_amount = new ArrayList<String>();
        budget_amount_paid = new ArrayList<String>();
        budget_currency  = new ArrayList<String>();
        budget_payment_status = new ArrayList<String>();
        budget_last_payment_date = new ArrayList<String>();
        budget_planner_id = new ArrayList<String>();


        budgetselectedrecycler = findViewById(R.id.budgetoptionsrecycler);

        updatebudgetnextbutton = (LinearLayout)findViewById(R.id.budgetnextbuttonlayout);

        budgetwholearray = new ArrayList<sabaEventItem>();

        budgetselectedrecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        budgetadapter=new budgetlisteditRecyclerAdapter(eventwholearray,context, eventdashboard.this, app);
        budgetselectedrecycler.setAdapter(budgetadapter);



    }

    private void setupBudgetCapabilityListRecycler() {



        capabilitiesselected = findViewById(R.id.selectableoptions);


        capabilitywholearray = new ArrayList<sabaEventItem>();

        event_capability_codeList = new ArrayList<String>();
        event_capability_nameList = new ArrayList<String>();
        event_categoryList = new ArrayList<String>();
        event_capability_imageidList = new ArrayList<String>();
        event_capability_image_locationList = new ArrayList<String>();

        capabilitiesselected.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        capabilityadapter=new dashboardclientcapabilityselectRecycler(capabilitywholearray,context, eventdashboard.this, app);
        capabilitiesselected.setAdapter(capabilityadapter);



    }

    private void setupEventoverviewAlertListRecycler() {

        eventoverview_alertname = new ArrayList<String>();
        eventoverview_capabilityid = new ArrayList<String>();
        eventoverview_alertid = new ArrayList<String>();
        eventoverview_alerttype = new ArrayList<String>();
        eventoverview_alertcontent = new ArrayList<String>();
        eventoverview_alertimagelocation = new ArrayList<String>();
        eventalertsholearrey = new ArrayList<sabaEventItem>();


        eventoverviewalertrecycler = findViewById(R.id.eventoverviewrecycleralerts);


        eventoverviewalertrecycler.setLayoutManager(new LinearLayoutManager(eventdashboard.this, LinearLayoutManager.VERTICAL, false));

        eventalertadapter=new eventoverviewalertsRecyclerAdapter(eventalertsholearrey,context, eventdashboard.this,this, app);
        eventoverviewalertrecycler.setAdapter(eventalertadapter);



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
                    JSONObject jsonObj = null;

                    eventoverview_alertname.clear();
                    eventoverview_capabilityid.clear();
                    eventoverview_alertid.clear();
                    eventoverview_alerttype.clear();
                    eventoverview_alertcontent.clear();
                    eventoverview_alertimagelocation.clear();

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


                            eventoverview_alertname.add("No new notification");
                            eventoverview_capabilityid.add(null);
                            eventoverview_alertid.add(null);
                            eventoverview_alerttype.add(null);
                            eventoverview_alertcontent.add("No alerts");
                            eventoverview_alertimagelocation.add(null);



                            sabaEventItem item=new sabaEventItem();
                            item.seteventoverviewalertid(null);
                            item.seteventoverviewalertname("No new notification");
                            item.seteventoverviewalertdescription("No alerts");
                            item.seteventoverviewalerttype(null);
                            item.seteventoverview_capabilityid(null);
                            item.seteventoverviewalertimagelocation(null);

                            eventTwinArray.add(item);

                            eventalertadapter.notifyDataSetChanged();


                        } else {


                            for (int i = 0; i < dataArray.length(); i++) {

                                jsonObj = dataArray.getJSONObject(i);


                                String capability_id = jsonObj.optString("id", null);
                                String alerttype = jsonObj.optString("type", null);


                                String alertname = jsonObj.optString("name", null);
                                String alertmessage = jsonObj.optString("message", null);

                                String alertid = jsonObj.optString("event_id", null);



                                /*eventoverview_alertnameTwin.add(alertname);
                                eventoverview_capabilityidTwin.add(capability_id);
                                eventoverview_alertidTwin.add(alertid);
                                eventoverview_alerttypeTwin.add(alerttype);
                                eventoverview_alertcontentTwin.add(alertmessage);
                                eventoverview_alertimagelocationTwin.add(null);*/

                                sabaEventItem item=new sabaEventItem();


                                item.seteventoverviewalertid(alertid);
                                item.seteventoverviewalertname(alertname);
                                item.seteventoverview_capabilityid(capability_id);
                                item.seteventoverviewalertdescription(alertmessage);
                                item.seteventoverviewalerttype(alerttype);
                                item.seteventoverviewalertimagelocation(null);



                                //setImagebitmap
                                eventTwinArray.add(item);


                                Log.d("Added the alert", alertname + " added");
                            }

                            // Notify adapter ONCE
                            eventTwinAdapter.notifyDataSetChanged();



                            /*for(Integer i=0; i<eventoverview_alertnameTwin.size(); i++)
                            {
                                sabaEventItem item=new sabaEventItem();


                                item.seteventoverviewalertid(eventoverview_alertidTwin.get(i));
                                item.seteventoverviewalertname(eventoverview_alertnameTwin.get(i));
                                item.seteventoverview_capabilityid(eventoverview_capabilityidTwin.get(i));
                                item.seteventoverviewalertdescription(eventoverview_alertcontentTwin.get(i));
                                item.seteventoverviewalerttype(eventoverview_alerttypeTwin.get(i));
                                item.seteventoverviewalertimagelocation(eventoverview_alertimagelocationTwin.get(i));



                                //setImagebitmap
                                eventTwinArray.add(item);

                            }*/



                        }



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

    private void getVendorbottomtabList(String type) {

        showProgressBar();

        String endpoint = "https://api.sabaapp.co/v0/event_twin?type=" + type;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                endpoint,
                null,
                response -> {

                    hideProgressBar();
                    Log.d("EVENT_TWIN_RESPONSE", response.toString());
                    JSONObject jsonObj = null;

                    eventoverview_alertname.clear();
                    eventoverview_capabilityid.clear();
                    eventoverview_alertid.clear();
                    eventoverview_alerttype.clear();
                    eventoverview_alertcontent.clear();
                    eventoverview_alertimagelocation.clear();

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


                            eventoverview_alertname.add("No new notification");
                            eventoverview_capabilityid.add(null);
                            eventoverview_alertid.add(null);
                            eventoverview_alerttype.add(null);
                            eventoverview_alertcontent.add("No alerts");
                            eventoverview_alertimagelocation.add(null);



                            sabaEventItem item=new sabaEventItem();
                            item.seteventoverviewalertid(null);
                            item.seteventoverviewalertname("No new notification");
                            item.seteventoverviewalertdescription("No alerts");
                            item.seteventoverviewalerttype(null);
                            item.seteventoverview_capabilityid(null);
                            item.seteventoverviewalertimagelocation(null);

                            eventTwinArray.add(item);

                            eventalertadapter.notifyDataSetChanged();


                        } else {


                            for (int i = 0; i < dataArray.length(); i++) {

                                jsonObj = dataArray.getJSONObject(i);


                                String capability_id = jsonObj.optString("id", null);
                                String alerttype = jsonObj.optString("type", null);


                                String alertname = jsonObj.optString("name", null);
                                String alertmessage = jsonObj.optString("message", null);

                                String alertid = jsonObj.optString("event_id", null);



                                eventoverview_alertnameTwin.add(alertname);
                                eventoverview_capabilityidTwin.add(capability_id);
                                eventoverview_alertidTwin.add(alertid);
                                eventoverview_alerttypeTwin.add(alerttype);
                                eventoverview_alertcontentTwin.add(alertmessage);
                                eventoverview_alertimagelocationTwin.add(null);


                                Log.d("Added the alert", alertname + " added");
                            }



                            for(Integer i=0; i<eventoverview_alertnameTwin.size(); i++)
                            {
                                sabaEventItem item=new sabaEventItem();


                                item.seteventoverviewalertid(eventoverview_alertidTwin.get(i));
                                item.seteventoverviewalertname(eventoverview_alertnameTwin.get(i));
                                item.seteventoverview_capabilityid(eventoverview_capabilityidTwin.get(i));
                                item.seteventoverviewalertdescription(eventoverview_alertcontentTwin.get(i));
                                item.seteventoverviewalerttype(eventoverview_alerttypeTwin.get(i));
                                item.seteventoverviewalertimagelocation(eventoverview_alertimagelocationTwin.get(i));



                                //setImagebitmap
                                eventTwinArray.add(item);

                            }

                            /*for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject obj = dataArray.getJSONObject(i);

                                sabaEventItem item = new sabaEventItem();

                                item.seteventName(obj.optString("title"));
                                item.seteventLocation(obj.optString("description"));
                                item.seteventStatus(obj.optString("status"));
                                item.seteventTime(obj.optString("created_at"));
                                item.setevent_Id(obj.optString("id"));

                                // Add to list
                                eventTwinArray.add(item);
                            }*/


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


    private void getOverview_alerts()
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
            payload.put("event_budget", event_budget);

            // =============================
            // BUILD event_budgetitems ARRAY
            // =============================
            JSONArray capabilityCodesArray = new JSONArray();
            for (SelectedCapabilityItem item : capabilityList) {
                capabilityCodesArray.put(item.getCode());
            }

            payload.put("event_capabilities", capabilityCodesArray);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/events/overview/"+EventId;

        Log.d("SENDING MATCH PAYLOAD", String.valueOf(payload));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL Vendors: "+response.toString());
                        hideProgressBar();
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            eventoverview_alertname.clear();
                            eventoverview_capabilityid.clear();
                            eventoverview_alertid.clear();
                            eventoverview_alerttype.clear();
                            eventoverview_alertcontent.clear();
                            eventoverview_alertimagelocation.clear();

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");
                                String messagedetails =jsonObj.getString("MESSAGE");



                                if(message.toLowerCase().matches("success"))
                                {

                                    numberofservices = jsonObj.optInt("ALL_SERVICES_COUNT", 0);
                                    numberofvendors = jsonObj.optInt("ALL_VENDORS_NUMBER", 0);
                                    bookedvendorsnumber = jsonObj.optInt("NUMBER_BOOKED_VENDORS", 0);
                                    numberbookedservices =jsonObj.optInt("SERVICES_BOOKED", 0);


                                    servicestotaloverviewtextoverview.setText(numberofservices+ " Pending");
                                    int servicesremaining = numberofservices -numberbookedservices;

                                    int remainingvendors = numberofvendors - bookedvendorsnumber;
                                    vendorstotaltextoverview.setText(numberofvendors+" Pending");
                                    vendorscontractedctedtotaltextoverview.setText(remainingvendors+"");

                                    servicesbookedoverviewtotaltextoverview.setText(servicesremaining+"");


                                    JSONObject eventdata = jsonObj.optJSONObject("DETAILS");

                                    if (eventdata != null) {

                                        String budget = eventdata.optString("BUDGET", null);
                                        String budgetSpent = eventdata.optString("BUDGET_SPENT", null);
                                        String location = eventdata.optString("LOCATION", null);
                                        String setupStatus = eventdata.optString("SETUP_STATUS", null);
                                        String status = eventdata.optString("STATUS", null);
                                        String type = eventdata.optString("TYPE", null);
                                        String vibe = eventdata.optString("VIBE", null);

                                        // Nullable fields
                                        String image = eventdata.isNull("IMAGE") ? null : eventdata.optString("IMAGE");
                                        String plannerId = eventdata.isNull("PLANNER_ID") ? null : eventdata.optString("PLANNER_ID");


                                        if(budget!=null){

                                            try{
                                                budgetamountvaluedouble = Double.parseDouble(budget);
                                            } catch (Exception e) {

                                                budgetamountvaluedouble = 0.0;
                                            }


                                            if(budgetSpent!=null){

                                                budgetamountTextoverview.setText("$"+budgetSpent+"/ $"+budget);
                                                double budgetValue = 0.0;
                                                double budgetSpentValue = 0.0;

                                                try {
                                                    budgetValue = Double.parseDouble(budget);
                                                    budgetSpentValue = Double.parseDouble(budgetSpent);
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }

                                                int percentage = 0;

                                                if (budgetValue > 0) {
                                                    percentage = (int) Math.round((budgetSpentValue / budgetValue) * 100);
                                                }


                                                budgetamountpercentagetextoverview.setText(percentage+"%");




                                            }else{

                                                budgetamountTextoverview.setText("$ 0/ $ "+budget);

                                                budgetamountpercentagetextoverview.setText("0%");


                                            }
                                        }

                                    }









                                    dataobj = jsonObj.getJSONArray("ALL_RECOMENDATIONS");

                                    Log.d("Receive RECOMENDATIONS DATA", String.valueOf(dataobj));

                                    if(dataobj.length()==0){
                                        // their are not values to add
                                        String messageerror="There was Zero products retrieved";
                                        Log.d("NO RECOMENDATION:",messageerror);

                                        eventoverview_alertname.add("No new notification");
                                        eventoverview_capabilityid.add(null);
                                        eventoverview_alertid.add(null);
                                        eventoverview_alerttype.add(null);
                                        eventoverview_alertcontent.add("No alerts");
                                        eventoverview_alertimagelocation.add(null);



                                        eventalertsholearrey.clear();

                                        sabaEventItem item=new sabaEventItem();
                                        item.seteventoverviewalertid(null);
                                        item.seteventoverviewalertname("No new notification");
                                        item.seteventoverviewalertdescription("No alerts");
                                        item.seteventoverviewalerttype(null);
                                        item.seteventoverview_capabilityid(null);
                                        item.seteventoverviewalertimagelocation(null);

                                        eventalertsholearrey.add(item);

                                        eventalertadapter.notifyDataSetChanged();


                                        //end of their are no values to add
                                    }else{

                                        for (int i = 0; i < dataobj.length(); i++) {

                                            jsonObj = dataobj.getJSONObject(i);


                                            String capability_id = jsonObj.optString("id", null);
                                            String alerttype = jsonObj.optString("type", null);


                                            String alertname = jsonObj.optString("name", null);
                                            String alertmessage = jsonObj.optString("message", null);

                                            String alertid = jsonObj.optString("event_id", null);



                                            eventoverview_alertname.add(alertname);
                                            eventoverview_capabilityid.add(capability_id);
                                            eventoverview_alertid.add(alertid);
                                            eventoverview_alerttype.add(alerttype);
                                            eventoverview_alertcontent.add(alertmessage);
                                            eventoverview_alertimagelocation.add(null);


                                            Log.d("Added the alert", alertname + " added");
                                        }




                                        eventalertsholearrey.clear();

                                        for(Integer i=0; i<eventoverview_alertname.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();


                                            item.seteventoverviewalertid(eventoverview_alertid.get(i));
                                            item.seteventoverviewalertname(eventoverview_alertname.get(i));
                                            item.seteventoverview_capabilityid(eventoverview_capabilityid.get(i));
                                            item.seteventoverviewalertdescription(eventoverview_alertcontent.get(i));
                                            item.seteventoverviewalerttype(eventoverview_alerttype.get(i));
                                            item.seteventoverviewalertimagelocation(eventoverview_alertimagelocation.get(i));



                                            //setImagebitmap
                                            eventalertsholearrey.add(item);

                                        }
                                        eventalertadapter.notifyDataSetChanged();

                                        //app.setCapabilitylist( eventalertsholearrey);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : eventoverview_alertname)
                                        {
                                            Log.d("Alert Name value--", singleRecord.toString());
                                        }


                                        //adaptertwo.notifyDataSetChanged();

                                    }




                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    AlertDialog ad = new AlertDialog.Builder(eventdashboard.this)
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
    private void getVendorList()
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
            payload.put("event_budget", event_budget);

            // =============================
            // BUILD event_budgetitems ARRAY
            // =============================
            JSONArray capabilityCodesArray = new JSONArray();
            for (SelectedCapabilityItem item : capabilityList) {
                capabilityCodesArray.put(item.getCode());
            }

            payload.put("event_capabilities", capabilityCodesArray);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/events/vendors";

        Log.d("SENDING MATCH PAYLOAD", String.valueOf(payload));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "ALL Vendors: "+response.toString());
                        hideProgressBar();



                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            base_pricelist.clear();
                            capability_detailslist.clear();
                            capability_idlist.clear();
                            eventwholearray.clear();

                            vendorserviceimage_idList.clear();
                            vendorserviceimagelocationList.clear();
                            vendoridList.clear();

                            vendornameList.clear();
                            vendorcapabilitynameList.clear();
                            vendorlocationList.clear();



                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");
                                String messagedetails =jsonObj.getString("MESSAGE");
                                if(message.toLowerCase().matches("success"))
                                {




                                    dataobj = jsonObj.getJSONArray("DATA");

                                    Log.d("Receive VENDOR DATA", String.valueOf(dataobj));

                                    if(dataobj.length()==0){
                                        // their are not values to add
                                        String messageerror="There was Zero products retrieved";
                                        Log.d("Msg:",messageerror);



                                        base_pricelist.add(null);
                                        capability_detailslist.add(null);
                                        capability_idlist.add(null);
                                        service_image_locationlist.add(null);
                                        eventwholearray.add(null);

                                        vendorserviceimage_idList.add(null);
                                        vendorserviceimagelocationList.add(null);
                                        vendoridList.add(null);

                                        vendornameList.add("No vendor loaded");
                                        vendorcapabilitynameList.add(null);
                                        vendorlocationList.add("Please try later");


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

                                            String base_price = jsonObj.optString("base_price", null);
                                            String capability_id = jsonObj.optString("capability_id", null);
                                            String service_image_location = jsonObj.optString("service_image_location", null);
                                            String capabilitname = jsonObj.optString("capability_name", null);
                                            String vendorname = jsonObj.optString("vendor_name", null);
                                            String vendorlocation = jsonObj.optString("location", null);
                                            String vendorserviceimageid = jsonObj.optString("service_image_id", null);
                                            String vendorid = jsonObj.optString("vendor_id", null);

                                            JSONObject capability_details = jsonObj.optJSONObject("capability_details");

                                            base_pricelist.add(base_price);
                                            capability_idlist.add(capability_id);
                                            service_image_locationlist.add(service_image_location);
                                            vendorserviceimage_idList.add(vendorserviceimageid);
                                            vendoridList.add(vendorid);
                                            vendornameList.add(vendorname);
                                            vendorcapabilitynameList.add(capabilitname);
                                            vendorlocationList.add(vendorlocation);


                                            capability_detailslist.add(capability_details);


                                            Log.d("Added Capability", vendorname + " added");
                                        }




                                        eventwholearray.clear();

                                        for(Integer i=0; i<capability_idlist.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();


                                            item.setvendorbase_price(base_pricelist.get(i));
                                            item.setvendorcapability_details(capability_detailslist.get(i));
                                            item.setvendorcapability_id(capability_idlist.get(i));
                                            item.setvendorserviceimage_id(vendorserviceimage_idList.get(i));
                                            item.setvendorserviceimagelocation(service_image_locationlist.get(i));
                                            item.setvendorid(vendoridList.get(i));

                                            item.setvendorname(vendornameList.get(i));
                                            item.setvendorcapabilityname(vendorcapabilitynameList.get(i));
                                            item.setvendorlocation(vendorlocationList.get(i));




                                            //setImagebitmap
                                            eventwholearray.add(item);

                                        }
                                        sabaeventsadapter.notifyDataSetChanged();

                                        app.setCapabilitylist( eventwholearray);


                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : capability_idlist)
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

                                    AlertDialog ad = new AlertDialog.Builder(eventdashboard.this)
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

    private void getBudgetvalue()
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
            payload.put("event_budget", event_budget);

            // =============================
            // BUILD event_budgetitems ARRAY
            // =============================
            JSONArray capabilityCodesArray = new JSONArray();
            for (SelectedCapabilityItem item : capabilityList) {
                capabilityCodesArray.put(item.getCode());
            }

            payload.put("event_capabilities", capabilityCodesArray);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/events/"+EventId;

        Log.d("SENDING MATCH PAYLOAD", String.valueOf(payload));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paymentsendpoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", "GET SINGLE EVENT: "+response.toString());
                        hideProgressBar();
                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            budget_event_id.clear();
                            budget_capability_id.clear();
                            budget_allocated_amount.clear();
                            budget_amount_paid.clear();
                            budget_currency.clear();
                            budget_payment_status.clear();
                            budget_last_payment_date.clear();
                            budget_planner_id.clear();

                            try {
                                jsonObj = new JSONObject(response.toString());
                                String message =jsonObj.getString("STATUS");

                                if(message.toLowerCase().matches("success"))
                                {


                                    dataobj = jsonObj.optJSONArray("capabilities");

                                    Log.d("Receive Event data", String.valueOf(dataobj));

                                    if (dataobj==null){

                                        budgetselectedrecycler.setVisibility(View.GONE);
                                        updatebudgetnextbutton.setVisibility(View.GONE);

                                        capabilitylistlayout.setVisibility(View.VISIBLE);
                                        capabilitiesselected.setVisibility(View.VISIBLE);


                                        getCapabilitylist();

                                    }
                                    else if(dataobj.length()==0){
                                        // their are not values to add
                                        String messageerror="There was Zerobudget events retrieved";
                                        Log.d("Msg:",messageerror);

                                        budgetselectedrecycler.setVisibility(View.GONE);
                                        updatebudgetnextbutton.setVisibility(View.GONE);

                                        capabilitylistlayout.setVisibility(View.VISIBLE);
                                        capabilitiesselected.setVisibility(View.VISIBLE);


                                        getCapabilitylist();




                                        //end of their are no values to add
                                    }
                                    else{

                                        for (int i = 0; i < dataobj.length(); i++) {

                                            jsonObj = dataobj.getJSONObject(i);


                                            String event_id = jsonObj.optString("event_id", null);
                                            String capability_id = jsonObj.optString("capability_id", null);
                                            String allocated_amount = jsonObj.optString("allocated_amount", null);
                                            String amount_paid = jsonObj.optString("amount_paid", null);
                                            String currency = jsonObj.optString("currency", null);
                                            String payment_status = jsonObj.optString("payment_status", null);
                                            String last_payment_date = jsonObj.optString("last_payment_date", null);
                                            String planner_id= jsonObj.optString("planner_id", null);

                                            //"notes"

                                            double allocationvalue = 0;

                                            if (allocated_amount!=null &&  !allocated_amount.equals("")){

                                                try{
                                                    allocationvalue = Double.parseDouble(allocated_amount);

                                                } catch (Exception e) {

                                                }
                                            }

                                            double amountpaidvalue = 0;

                                            try{

                                                amountpaidvalue = Double.parseDouble(amount_paid);

                                            } catch (Exception e) {

                                            }


                                            budget_event_id.add(event_id);
                                            budget_capability_id.add(capability_id);
                                            budget_allocated_amount.add(allocated_amount);
                                            budget_amount_paid.add(amount_paid);
                                            budget_currency.add(currency);
                                            budget_payment_status.add(payment_status);
                                            budget_last_payment_date.add(last_payment_date);
                                            budget_planner_id.add(planner_id);



                                            Log.d("Added Capability", capability_id+ " added");
                                        }


                                        budgetwholearray.clear();

                                        for(Integer i=0; i<budget_event_id.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();

                                            item.setbudget_event_id(budget_event_id.get(i));
                                            item.setbudget_capability_id(budget_capability_id.get(i));
                                            item.setbudget_allocated_amount(budget_allocated_amount.get(i));
                                            item.setbudget_amount_paid(budget_amount_paid.get(i));
                                            item.setbudget_currency(budget_currency.get(i));
                                            item.setbudget_payment_status(budget_payment_status.get(i));
                                            item.setbudget_last_payment_date(budget_last_payment_date.get(i));
                                            item.setbudget_planner_id(budget_planner_id.get(i));


                                            //setImagebitmap
                                            budgetwholearray.add(item);

                                        }

                                        budgetselectedrecycler.setVisibility(View.VISIBLE);
                                        updatebudgetnextbutton.setVisibility(View.VISIBLE);
                                        capabilitylistlayout.setVisibility(View.GONE);

                                        capabilitiesselected.setVisibility(View.GONE);

                                        budgetadapter.notifyDataSetChanged();


                                        for ( String singleRecord : budget_capability_id)
                                        {
                                            Log.d("Budget Name value--", singleRecord.toString());
                                        }


                                        //adaptertwo.notifyDataSetChanged();

                                    }




                                }
                                else
                                {

                                    budgetselectedrecycler.setVisibility(View.GONE);
                                    updatebudgetnextbutton.setVisibility(View.GONE);

                                    capabilitylistlayout.setVisibility(View.VISIBLE);

                                    capabilitiesselected.setVisibility(View.VISIBLE);
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    AlertDialog ad = new AlertDialog.Builder(eventdashboard.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Failed");
                                    ad.setMessage("Could not retrieve budget items");
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    ad.show();

                                }




                            } catch (JSONException e) {

                                budgetselectedrecycler.setVisibility(View.GONE);
                                updatebudgetnextbutton.setVisibility(View.GONE);
                                capabilitylistlayout.setVisibility(View.VISIBLE);

                                capabilitiesselected.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                                Log.e("errorIs", "error"+e.getMessage());
                            }
                            hideProgressBar();

                        } else {
                            hideProgressBar();

                            budgetselectedrecycler.setVisibility(View.GONE);
                            updatebudgetnextbutton.setVisibility(View.GONE);
                            capabilitylistlayout.setVisibility(View.VISIBLE);

                            capabilitiesselected.setVisibility(View.VISIBLE);
                            Log.e("Your Array Response", "Data Null");
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();

                budgetselectedrecycler.setVisibility(View.GONE);
                updatebudgetnextbutton.setVisibility(View.GONE);

                capabilitiesselected.setVisibility(View.VISIBLE);

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

                        if (!response.equals(null)) {
                            Log.e("response", "response "+response.toString());
                            JSONObject merchant_profile = null;
                            JSONObject accountdetails = null;
                            JSONObject paymentinfo = null;
                            JSONObject jsonObj = null;

                            event_capability_codeList.clear();
                            event_capability_nameList.clear();
                            event_categoryList.clear();
                            event_capability_imageidList.clear();
                            event_capability_image_locationList.clear();

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

                                        event_capability_codeList.add(null);
                                        event_capability_nameList.add("No services");;
                                        event_categoryList.add("No services loaded");
                                        event_capability_imageidList.add(null);
                                        event_capability_image_locationList.add(null);

                                        capabilitywholearray.clear();

                                        sabaEventItem item=new sabaEventItem();
                                        item.setcapability_code(null);
                                        item.setcapability_name("No services");
                                        item.setcapability_category(null);
                                        item.setcapability_imageid(null);
                                        item.setcapability_image_location(null);

                                        capabilitywholearray.add(item);

                                        capabilityadapter.notifyDataSetChanged();


                                        //end of their are no values to add
                                    }else{

                                        for (int i = 0; i < dataobj.length(); i++) {
                                            jsonObj = dataobj.getJSONObject(i);


                                            String capability_code = jsonObj.getString("capability_code");
                                            String capability_name = jsonObj.getString("capability_name");
                                            String category= jsonObj.getString("category");
                                            String capability_imageid = jsonObj.getString("capability_imageid");

                                            String capability_image_location =jsonObj.getString("capability_image_location");


                                            event_capability_codeList.add(capability_code);
                                            event_capability_nameList.add(capability_name);
                                            event_categoryList.add(category);
                                            event_capability_imageidList.add(capability_imageid);
                                            event_capability_image_locationList.add(capability_image_location);

                                            //for

                                            Log.d("Added Capability", capability_name + " To products array list");
                                        }



                                        capabilitywholearray.clear();

                                        for(Integer i=0; i<event_capability_nameList.size(); i++)
                                        {
                                            sabaEventItem item=new sabaEventItem();


                                            item.setcapability_code(event_capability_codeList.get(i));
                                            item.setcapability_name(event_capability_nameList.get(i));
                                            item.setcapability_category(event_categoryList.get(i));
                                            item.setcapability_imageid(event_capability_imageidList.get(i));
                                            item.setcapability_image_location(event_capability_image_locationList.get(i));


                                            //setImagebitmap
                                            capabilitywholearray.add(item);

                                        }
                                        capabilityadapter.notifyDataSetChanged();



                                        //WayaWayaItem item = new WayaWayaItem();
                                        //item.setproductListMain(productnameList);

                                        for ( String singleRecord : event_capability_nameList)
                                        {
                                            Log.d("Event Name value--", singleRecord.toString());
                                        }




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

        if(daysDifference!=0 || hoursDifference!=0|| minutesDifference!=0 || secondsDifference!=0){

            if(daysDifference!=0){

                countdowncardtext.setText(daysDifference+" Days to Go");

            }else if(hoursDifference!=0){

                countdowncardtext.setText(hoursDifference+" Hours to Go");

            }else if(minutesDifference!=0){

                countdowncardtext.setText(minutesDifference+" Minutes to Go");

            }else {

                countdowncardtext.setText(secondsDifference+" Seconds to Go");

            }


        }else{

            if(event_time!=null){

                countdowncardtext.setText(event_time+" Days to Go");
            }else{

                countdowncardtext.setText("No Days to Go");
            }

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


    private void initOverviewContainer() {

        //EventId,event_name,event_time,event_location,event_budget,event_budgetspent,event_setupstatus,event_eventstatus,event_plannerid,image_location,time_setup,user_id



        TextView eventname = findViewById(R.id.eventnameoveralllabel);
        TextView countdowncardtext = findViewById(R.id.eventcountdowndays_text);

        TextView dayscounttext = findViewById(R.id.dayscounttextlabel);
        TextView hourscounttext = findViewById(R.id.hourcountlabel);
        TextView minutescounttext = findViewById(R.id.minutescountlabel);
        TextView secondscounttext = findViewById(R.id.secondscountlabel);


        budgetamountTextoverview = findViewById(R.id.budgetamounttext);
        servicesbookedoverviewtotaltextoverview = findViewById(R.id.servicesbookedoverviewtotaltext);
        servicestotaloverviewtextoverview = findViewById(R.id.servicestotaloverviewtext);
        vendorscontractedctedtotaltextoverview = findViewById(R.id.vendorscontractedctedtotaltext);
        vendorstotaltextoverview = findViewById(R.id.vendorstotaltext);
        budgetamountpercentagetextoverview = findViewById(R.id. budgetamountpercentagetext);




        if(event_budget!=null){


            if(event_budgetspent!=null){

                budgetamountTextoverview.setText("$"+event_budget+"/ $"+event_budgetspent);


            }else{

                budgetamountTextoverview.setText("$"+event_budget+"/ $ 0");


            }
        }else{

            budgetamountTextoverview.setText("$0 / $ 0");
        }

        //component status



        if(event_name!=null){
            eventname.setText(event_name);
        }else{

            eventname.setText("Event title");
        }

        if(daysDifference!=0 || hoursDifference!=0|| minutesDifference!=0 || secondsDifference!=0){

            if(daysDifference!=0){

                countdowncardtext.setText("In "+daysDifference+" Days");

            }else if(hoursDifference!=0){

                countdowncardtext.setText("In "+hoursDifference+" Hours");

            }else if(minutesDifference!=0){

                countdowncardtext.setText("In "+minutesDifference+" Minutes");

            }else {

                countdowncardtext.setText("In "+secondsDifference+" Seconds");

            }

            dayscounttext.setText(""+daysDifference);
            hourscounttext.setText(""+hoursDifference);
            minutescounttext.setText(""+minutesDifference);
            secondscounttext.setText(""+secondsDifference);




        }else{

            if(event_time!=null){

                countdowncardtext.setText(event_time+" Days to Go");
            }else{

                countdowncardtext.setText("No Days to Go");
            }

        }




        // Load event alerts
        getOverview_alerts();


    }


    private void initVendorContainer() {





        TextView filtersummarytext = findViewById(R.id.filter_summary_text);

        //Showing vendors matching: June 12, Budget: $15,000, Style: Elegant, Related: Any

        String valuetext = "";


        if(event_name!=null){

            valuetext = "Showing Vendors matching : "+event_name+", On";


            if(event_time!=null){

                valuetext = valuetext+","+event_time;
            }

            if(event_budget!=null){

                valuetext = valuetext+", Budget : "+event_budget;

            }

            filtersummarytext.setText(valuetext);
        }else{

            filtersummarytext.setText("Event title");
        }





        // Load default tab
        getVendorList();
        initVendortopTabs();


    }


    private void initBudgetContainer() {


        capabilitylistlayout = findViewById(R.id.capabilitylistlayout);





        // Load default tab
        getBudgetvalue();


    }


    private void sendupdatedbudgetPost()
    {
        //continuetonextbutton.setText("Uploading to your store...");
        showProgressBar();


        JSONObject payload = new JSONObject();

        try {
            // =============================
            // ROOT PAYLOAD
            // =============================
            //payload.put("event_amountspent", "0");
            payload.put("eventid", EventId);
            //payload.put("event_budget", event_budget);

            // =============================
            // BUILD event_budgetitems ARRAY
            // =============================
            JSONArray budgetItemsArray = new JSONArray();

            for (SelectedCapabilityItem item : draftbudgetcapabilityList) {

                // ❗ Skip items with zero allocation if desired
                //if (item.getAllocatedAmount() <= 0) continue;

                JSONObject budgetItem = new JSONObject();
                budgetItem.put("capability_id", item.getCode());
                budgetItem.put("allocated_amount", item.getAllocatedAmount());
                budgetItem.put("amount_paid", 0);
                budgetItem.put("currency", "USD");
                budgetItem.put("payment_status", "pending");
                budgetItem.put("last_payment_date", null);
                budgetItem.put("notes", JSONObject.NULL);

                budgetItemsArray.put(budgetItem);
            }

            payload.put("items", budgetItemsArray);
            Log.d("SENDING WHOLE BUDGET PAYLOAD", "JSON: "+payload);

        } catch (JSONException e) {
            hideProgressBar();
            e.printStackTrace();
            return;
        }



        String paymentsendpoint="https://api.sabaapp.co/v0/events/budget/items";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, paymentsendpoint, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "Submit budget response : "+response.toString());
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
                                String messagedetails =jsonObj.getString("message");
                                if(message.toLowerCase().matches("success"))
                                {


                                    Log.d("EVENT ID", EventId);


                                    AlertDialog ad = new AlertDialog.Builder(eventdashboard.this)
                                            .create();
                                    ad.setCancelable(true);
                                    ad.setTitle("Request Completed");
                                    ad.setMessage(messagedetails);
                                    ad.setButton(getApplicationContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            getBudgetvalue();
                                        }
                                    });
                                    ad.show();




                                }
                                else
                                {
                                    String messageerror="There was an Error";
                                    Log.d("Msg:",messageerror);

                                    AlertDialog ad = new AlertDialog.Builder(eventdashboard.this)
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


    //calculate the time difference

    public static class TimeDiff {
        public int days;
        public int hours;
        public int minutes;
        public int seconds;
    }
    public static TimeDiff getSignedTimeDifference(String unixTimestampStr) {
        TimeDiff result = new TimeDiff();

        if (unixTimestampStr == null || unixTimestampStr.isEmpty()) {
            return result; // all zeros
        }

        long inputTime;
        try {
            inputTime = Long.parseLong(unixTimestampStr);
        } catch (NumberFormatException e) {
            return result;
        }

        // Detect seconds vs milliseconds
        if (unixTimestampStr.length() <= 10) {
            inputTime *= 1000;
        }

        long now = System.currentTimeMillis();
        long diffMillis = inputTime - now;

        int sign = Long.compare(diffMillis, 0); // -1, 0, or 1
        long diff = Math.abs(diffMillis);

        long days = diff / (24 * 60 * 60 * 1000);
        diff %= (24 * 60 * 60 * 1000);

        long hours = diff / (60 * 60 * 1000);
        diff %= (60 * 60 * 1000);

        long minutes = diff / (60 * 1000);
        diff %= (60 * 1000);

        long seconds = diff / 1000;

        result.days = (int) days * sign;
        result.hours = (int) hours * sign;
        result.minutes = (int) minutes * sign;
        result.seconds = (int) seconds * sign;

        return result;
    }





}

