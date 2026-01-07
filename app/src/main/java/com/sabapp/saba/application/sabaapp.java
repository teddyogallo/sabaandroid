package com.sabapp.saba.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.data.model.sabaEventItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class sabaapp extends Application {

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    SharedPrefsXtreme sharedPrefsXtreme;

    public Boolean appLaunched=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    private ArrayList<sabaEventItem> sabaeventlist=new ArrayList<sabaEventItem>();;

    private ArrayList<sabaEventItem> capabilitylist=new ArrayList<sabaEventItem>();;

    public String getDevversion() {
        String currentversion = "1";

        return currentversion;
    }

    public void setShowonboard(String readableTime)
    {
        sharedPrefsXtreme.saveData("onboardseen", readableTime);
    }

    public String getShowonboard()
    {
        return sharedPrefsXtreme.getData("onboardseen");
    }

    public void setAppLaunched(Boolean appLaunched)
    {
        this.appLaunched=appLaunched;
    }
    public Boolean getAppLaunched()
    {
        return appLaunched;
    }

    public void setReadableTimeStart(String readableTime)
    {
        sharedPrefsXtreme.saveData("readabletime", readableTime);
    }


    public void logOutPreliminaries()
    {
        sharedPrefsXtreme.deleteAllData(getApplicationContext());

        sharedPrefsXtreme.saveData("isLoggedin", "");
        sharedPrefsXtreme.saveData("phonenum", "");
        sharedPrefsXtreme.saveData("token", "");
        setAppLaunched(false);

    }

    public String getApiusername()
    {
        return sharedPrefsXtreme.getData("api_username");

    }

    public String getApipassword()
    {
        return sharedPrefsXtreme.getData("api_password");

    }

    public String getBearertoken()
    {
        return sharedPrefsXtreme.getData("bearer_token");

    }

    public void setReadableTimeEnd(String readableTimeEnd)
    {
        sharedPrefsXtreme.saveData("readabletimeend", readableTimeEnd);
    }

    public void setStartTimeStamp(String timeStamp)
    {
        sharedPrefsXtreme.saveData("starttimestamp", timeStamp);
    }

    public void setEndtimestamp(String endtimestamp)
    {
        sharedPrefsXtreme.saveData("endtimestamp", endtimestamp);
    }


    public void setSabaEventlist(ArrayList<sabaEventItem> eventslist)
    {



        this.sabaeventlist=eventslist;

    }
    public ArrayList<sabaEventItem> getSabaEventlist()
    {

        return sabaeventlist;
    }


    public void setCapabilitylist(ArrayList<sabaEventItem> eventslist)
    {



        this.capabilitylist=eventslist;

    }
    public ArrayList<sabaEventItem> getCapabilitylist()
    {

        return capabilitylist;
    }

    public int getLoggeddashboardtime()
    {
        return sharedPrefsXtreme.getIntData("loggeddashtime");
    }
    public void setCreateAdImage(String createAdImage)
    {
        sharedPrefsXtreme.saveData("createadimg", createAdImage);
    }

    public String getCreateAdImage()
    {
        return sharedPrefsXtreme.getData("createadimg");}

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPrefsXtreme = SharedPrefsXtreme.getInstance(getApplicationContext());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseApp.initializeApp(this);
        final SimpleDateFormat myDateFormat =new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        setReadableTimeStart(myDateFormat.format(c.getTime()));
        c.add(Calendar.DATE, 30);
        Date expDate = c.getTime();

        Long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        setReadableTimeEnd(myDateFormat.format(c.getTime()));
        setStartTimeStamp(String.valueOf(timestamp));
        setEndtimestamp(String.valueOf(expDate));

    }

}