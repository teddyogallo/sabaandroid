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
        sharedPrefsXtreme.saveData("loginaccounttype", "");
        sharedPrefsXtreme.saveData("fcm_token", "");
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

    public int getChatNumber()
    {
        return sharedPrefsXtreme.getIntData("chat_notifications_num");

    }

    public int getPaymentNumber()
    {
        return sharedPrefsXtreme.getIntData("payment_notifications_num");

    }


    public int getLoginNumber()
    {
        return sharedPrefsXtreme.getIntData("login_number_times");

    }



    public int getOrderNumber()
    {
        return sharedPrefsXtreme.getIntData("order_notifications_num");

    }

    public int getGeneralNotificationsNumber()
    {
        return sharedPrefsXtreme.getIntData("general_notifications_num");

    }

    public String getSetMenulevel()
    {
        return sharedPrefsXtreme.getData("set_menulevel");

    }

    public String getCurrentNotificationtitle()
    {
        return sharedPrefsXtreme.getData("current_notificationtitle");

    }

    public String getCurrentNotificationmessage()
    {
        return sharedPrefsXtreme.getData("current_notification_message");

    }

    public String getCurrentNotificationtype()
    {
        return sharedPrefsXtreme.getData("current_notification_type");

    }

    public String getCurrentNotificationid()
    {
        return sharedPrefsXtreme.getData("current_notification_id");

    }

    public String getCurrentNotificationname()
    {
        return sharedPrefsXtreme.getData("current_notification_name");

    }

    public String getSetmenuActionid()
    {
        return sharedPrefsXtreme.getData("set_menuactionid");

    }

    public String getActiveChatuser()
    {
        return sharedPrefsXtreme.getData("activechatuser");

    }


    public String getActiveChatfullname()
    {
        return sharedPrefsXtreme.getData("activechatname");

    }

    public String getRecipientid()
    {
        return sharedPrefsXtreme.getData("recipient_id");

    }


    public String getLoginAccounttype()
    {
        return sharedPrefsXtreme.getData("loginaccounttype");

    }

    public String getFCMToken()
    {
        return sharedPrefsXtreme.getData("fcm_token");

    }



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