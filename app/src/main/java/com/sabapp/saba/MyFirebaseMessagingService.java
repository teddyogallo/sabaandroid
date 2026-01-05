package com.sabapp.saba;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.content.SharedPreferences;
import com.sabapp.saba.SharedPrefsXtreme;
import com.sabapp.saba.R;

import com.sabapp.saba.sabaDrawerActivity;


import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("FireBase Token", s);
        sendRegistrationToServer(s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Context ctx = this;
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            //handleNotification(remoteMessage.getNotification().getBody());

            Intent intent = new Intent(this, sabaDrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //PendingIntent pendingIntent;
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            }
            else
            {

                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            }

            String channelId = "Default";
            String notification_title = remoteMessage.getNotification().getTitle();
            String notification_body =  remoteMessage.getNotification().getBody();
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(notification_title);
            bigText.setBigContentTitle(notification_body);
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.saba_micro_icon)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setStyle(bigText)
                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());


            if (!isAppIsInBackground(ctx)) {
                // app is in foreground, broadcast the push message
                if(remoteMessage.getData().get("type").equals("CHAT")){
                    // Check if message contains a data payload.
                    if (remoteMessage.getData().size() > 0) {
                        //notification has a data payload
                        Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                            Log.e("Key Test", "Key = " + entry.getKey() + ", Value = " + entry.getValue() );
                        }
                        //emd of notification has a data payload
                    }
                    //save to shared preferences here
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(this);

                    sharedPrefsXtreme.saveData("current_notificationtitle", remoteMessage.getNotification().getTitle());

                    sharedPrefsXtreme.saveData("current_notification_message", remoteMessage.getNotification().getBody());

                    sharedPrefsXtreme.saveData("current_notification_type", remoteMessage.getData().get("type"));

                    sharedPrefsXtreme.saveData("current_notification_id",  remoteMessage.getData().get("social_id"));

                    sharedPrefsXtreme.saveData("current_notification_name",  remoteMessage.getData().get("full_names"));

                    Log.e(TAG, "CHAT Notification: " + remoteMessage.getNotification().getBody());
                    Intent intent2 = new Intent("CHAT_BROADCAST");
                    intent2.putExtra("title", remoteMessage.getNotification().getTitle());
                    intent2.putExtra("body", remoteMessage.getNotification().getBody());
                    intent2.putExtra("message",  remoteMessage.getData().get("chat_message"));
                    intent2.putExtra("social_id", remoteMessage.getData().get("social_id"));
                    intent2.putExtra("firstname", remoteMessage.getData().get("full_names"));
                    intent2.putExtra("type", remoteMessage.getData().get("messagetype"));
                    intent2.putExtra("chat_message", remoteMessage.getData().get("chat_message"));
                    intent2.putExtra("time", remoteMessage.getData().get("time"));
                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent2);


                }
                else if(remoteMessage.getData().get("type").equals("PAYMENT")){

                    // Check if message contains a data payload.
                    if (remoteMessage.getData().size() > 0) {
                        //notification has a data payload
                        Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                            Log.e("Key Test", "Key = " + entry.getKey() + ", Value = " + entry.getValue() );
                        }
                        //emd of notification has a data payload
                    }

                    Log.e(TAG, "PAYMENT Notification: " + remoteMessage.getNotification().getBody());

                    //save to shared preferences here
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(this);

                    sharedPrefsXtreme.saveData("current_notificationtitle", remoteMessage.getNotification().getTitle());

                    sharedPrefsXtreme.saveData("current_notification_message", remoteMessage.getNotification().getBody());

                    sharedPrefsXtreme.saveData("current_notification_type", remoteMessage.getData().get("type"));

                    sharedPrefsXtreme.saveData("current_notification_id",  remoteMessage.getData().get("payment_phone"));
                    sharedPrefsXtreme.saveData("current_notification_name",  remoteMessage.getData().get("full_names"));

                    Intent intent2 = new Intent("PAYMENT_BROADCAST");
                    intent2.putExtra("title", remoteMessage.getNotification().getTitle());
                    intent2.putExtra("body", remoteMessage.getNotification().getBody());
                    intent2.putExtra("message",  remoteMessage.getData().get("narration"));

                    intent2.putExtra("payment_phone",  remoteMessage.getData().get("payment_phone"));
                    intent2.putExtra("payment_amount",  remoteMessage.getData().get("payment_amount"));
                    intent2.putExtra("transaction_ref",  remoteMessage.getData().get("transaction_ref"));
                    intent2.putExtra("payment_type",  remoteMessage.getData().get("payment_type"));
                    intent2.putExtra("currency",  remoteMessage.getData().get("currency"));
                    intent2.putExtra("payment_name",  remoteMessage.getData().get("payment_name"));
                    intent2.putExtra("status",  remoteMessage.getData().get("status"));
                    intent2.putExtra("narration",  remoteMessage.getData().get("narration"));
                    intent2.putExtra("start_timestamp",  remoteMessage.getData().get("start_timestamp"));
                    intent2.putExtra("provider_account_id",  remoteMessage.getData().get("provider_account_id"));
                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent2);


                }
                else if(remoteMessage.getData().get("type").equals("ORDER")){
                    // Check if message contains a data payload.
                    if (remoteMessage.getData().size() > 0) {
                        //notification has a data payload
                        Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                            Log.e("Key Test", "Key = " + entry.getKey() + ", Value = " + entry.getValue() );
                        }
                        //emd of notification has a data payload
                    }

                    Log.e(TAG, "ORDER Notification: " + remoteMessage.getNotification().getBody());

                    //save to shared preferences here
                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(this);

                    sharedPrefsXtreme.saveData("current_notificationtitle", remoteMessage.getNotification().getTitle());

                    sharedPrefsXtreme.saveData("current_notification_message", remoteMessage.getNotification().getBody());

                    sharedPrefsXtreme.saveData("current_notification_type", remoteMessage.getData().get("type"));

                    sharedPrefsXtreme.saveData("current_notification_id", remoteMessage.getData().get("customer_id"));
                    sharedPrefsXtreme.saveData("current_notification_name",  remoteMessage.getData().get("full_names"));

                    Intent intent2 = new Intent("ORDER_BROADCAST");
                    intent2.putExtra("title", remoteMessage.getNotification().getTitle());
                    intent2.putExtra("body", remoteMessage.getNotification().getBody());
                    intent2.putExtra("message", remoteMessage.getData().get("narration"));

                    intent2.putExtra("customer_id", remoteMessage.getData().get("customer_id"));
                    intent2.putExtra("total_price", remoteMessage.getData().get("total_price"));
                    intent2.putExtra("product_quantity", remoteMessage.getData().get("product_quantity"));
                    intent2.putExtra("unit_price", remoteMessage.getData().get("unit_price"));
                    intent2.putExtra("payment_mode", remoteMessage.getData().get("payment_mode"));
                    intent2.putExtra("payment_id", remoteMessage.getData().get("payment_id"));
                    intent2.putExtra("currency", remoteMessage.getData().get("currency"));
                    intent2.putExtra("product_name", remoteMessage.getData().get("product_name"));
                    intent2.putExtra("delivery_address", remoteMessage.getData().get("delivery_address"));
                    intent2.putExtra("customer_phone", remoteMessage.getData().get("customer_phone"));
                    intent2.putExtra("customer_email", remoteMessage.getData().get("customer_email"));
                    intent2.putExtra("customer_name", remoteMessage.getData().get("customer_name"));
                    intent2.putExtra("time_requested", remoteMessage.getData().get("time_requested"));
                    intent2.putExtra("delivery_status", remoteMessage.getData().get("delivery_status"));
                    intent2.putExtra("payment_status", remoteMessage.getData().get("payment_status"));



                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent2);


                }
                else{
                    // Check if message contains a data payload.
                    if (remoteMessage.getData().size() > 0) {
                        //notification has a data payload
                        Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                            Log.e("Key Test", "Key = " + entry.getKey() + ", Value = " + entry.getValue() );
                        }
                        //emd of notification has a data payload
                    }

                    SharedPrefsXtreme sharedPrefsXtreme = SharedPrefsXtreme.getInstance(this);

                    sharedPrefsXtreme.saveData("current_notificationtitle", remoteMessage.getNotification().getTitle());

                    sharedPrefsXtreme.saveData("current_notification_message", remoteMessage.getNotification().getBody());

                    sharedPrefsXtreme.saveData("current_notification_type", remoteMessage.getData().get("type"));

                    sharedPrefsXtreme.saveData("current_notification_id", remoteMessage.getData().get("customer_id"));
                    sharedPrefsXtreme.saveData("current_notification_name",  remoteMessage.getData().get("full_names"));

                    Log.e(TAG, "Other Notification: " + remoteMessage.getNotification().getBody());
                    Intent intent2 = new Intent("NOTIFICATION_LOCAL_BROADCAST");
                    intent2.putExtra("title", remoteMessage.getNotification().getTitle());
                    intent2.putExtra("body", remoteMessage.getNotification().getBody());
                    intent2.putExtra("message",  remoteMessage.getData().get("chat_message"));
                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent2);

                }


            }


        }




    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }



    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }


    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, sabaDrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    public static class MyWorker extends Worker {

        public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            // TODO(developer): add long running task here.
            return Result.success();
        }
    }
}
