package com.sabapp.saba.messaging;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.sabapp.saba.SecondFragment;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.R;

public class popupnotification extends DialogFragment {
    sabaapp app;
    LinearLayout replyreviewcont;
    LinearLayout myreviewcont;

    AppCompatImageView imageView;

    private AppCompatImageView cancel;
    Context mContext;
    byte[] decodedString;
    String businessName;
    TextView customerName;
    String ussdCode;
    String offerdetails;
    String phonenumber;
    RatingBar ratingBar;
    String ratingval;
    String reviewtrans;
    String reviewReply;
    String oid;
    Boolean hasReply;
    private TextView businessNa;
    private TextView reviews;
    private TextView ussdCo;
    private MaterialButton ussdDial;
    private AppCompatImageView advimg;
    private AppCompatImageView whatsapp;
    private AppCompatImageView message;
    private AppCompatImageView calls;

    private LinearLayout ussdCont;
    private LinearLayout offerCont;
    private LinearLayout phoneNumCont;
    private TextView edit;
    private MaterialButton submitReply;
    private TextInputEditText replyTextEdit;
    private TextView replyText;
    private TextView replyTexttwo;
    private ViewPager viewPager;
    TextView popuptitle;
    TextView notificationmessage;
    String notification_type;
    String notification_id;
    String notification_messagetext;
    String notification_titletext;
    String mainnotification_type;
    String mainnotification_id;
    TextView nameofcustomer;
    TextView nameofproduct;
    TextView receiptnumber;
    MaterialCardView transactionstatus;
    TextView transaction_statuslabel;
    ImageView transaction_typeimage;
    TextView transaction_amountlabel;
    TextView customer_phonenumber;
    TextView customer_address;
    TabLayout tabs;
    RelativeLayout popuplayout;




    public popupnotification() {
        // Required empty public constructor
    }
    public Boolean ischeck=getIscheckedw();
    public Boolean ischeckedn=getIschecked();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (sabaapp) mContext.getApplicationContext();
        instance= this;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
//        for(int i=0;i<=photoFiles.size()-1;i++){
//            photoFiles.remove(photoFiles.get(i));
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 360;
            getDialog().getWindow().setGravity(Gravity.TOP);

            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        }




    }
    Context context;




    public static popupnotification newInstance() {
        return new popupnotification();
    }



    private void initializePlayer() {

    }
    private static popupnotification instance;
    public static popupnotification getInstance()
    {
        return instance;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null && getDialog().getWindow() != null) {
            //      getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().setCanceledOnTouchOutside(true);
//            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//            int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//            getDialog().getWindow().setLayout(width, height);

        }


        View view = inflater.inflate(R.layout.popupnotification, container, false);


        popuptitle = view.findViewById(R.id.notification_title);
        notificationmessage = view.findViewById(R.id.notificationmessage);
        popuplayout  = view.findViewById(R.id.popuprelativelayout);

        mainnotification_type = app.getCurrentNotificationtype();
        mainnotification_id = app.getCurrentNotificationid();


        notification_messagetext = app.getCurrentNotificationmessage();

        if (notification_messagetext == null || TextUtils.isEmpty(notification_messagetext) || notification_messagetext.trim().isEmpty()){
            notificationmessage.setText("Detail message is not available");
        }else{

            notificationmessage.setText(notification_messagetext);
        }


        notification_titletext = app.getCurrentNotificationtitle();

        if (notification_titletext == null || TextUtils.isEmpty(notification_titletext) || notification_titletext.trim().isEmpty()){
            popuptitle.setText("Janja Notification");
        }else{

            popuptitle.setText(notification_titletext);
        }



        notification_type = app.getCurrentNotificationtype();
        notification_id= app.getCurrentNotificationid();

        popuplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainnotification_type.equals("PAYMENT")){
                    //GO TO PAYMENTS ACTIVITY

                    /*Intent intent =  new Intent(mContext, paymentshistory_activity.class);
                    intent.putExtra("createadrad", "gotowholechatpref");
                    intent.putExtra("activeuser", notification_id);
                    intent.putExtra("chatname", app.getCurrentNotificationname());
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(intent);*/
                    SecondFragment fragment = SecondFragment.newInstance(
                            notification_id,
                            app.getCurrentNotificationname(),
                            "gotowholechatpref"
                    );

                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, fragment) // IMPORTANT
                            .addToBackStack("SecondFragment")
                            .commit();

                    dismiss(); // close popup


                    //END OF GO TO PAYMENTS ACTIVITY
                }else if (mainnotification_type.equals("CHAT")){
                    //GO TO PAYMENTS ACTIVITY

                    Intent intent =  new Intent(mContext, conversationactivity.class);
                    intent.putExtra("createadrad", "gotowholechatpref");
                    intent.putExtra("activeuser", notification_id);
                    intent.putExtra("chatname", app.getCurrentNotificationname());
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(intent);



                    //END OF GO TO PAYMENTS ACTIVITY
                }else{


                    Toast.makeText(mContext, "No option for button press for :"+mainnotification_type, Toast.LENGTH_LONG).show();
                }


                // Toast.makeText(mContext, "Popup selected", Toast.LENGTH_LONG).show();


            }
        });



       /* String provider_type= app.getHistorylisttype();
       if(provider_type.equals("M-PESA")){

            transaction_typeimage.setImageResource(R.drawable.mpesa_logo);

        }*/


        return view;




    }

    public void setIschecked(String ischeckedn)
    {

    }

    public Boolean getIschecked()
    {

        return false;
    }
    public Boolean getIscheckedw()
    {

        return true;
    }

    public void closeAutomoton()
    {

        getDialog().dismiss();
    }

    public static void openWhatsAppConversation(Context context, String number, String message) {

        number = number.replace(" ", "").replace("+", "");

        Intent sendIntent = new Intent("android.intent.action.MAIN");

        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");

        context.startActivity(sendIntent);
    }


    public static void openWhatsAppConversationUsingUri(Context context) {

        Uri uri = Uri.parse("https://wa.me/254797318700?text=join wayawayatest");

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

        context.startActivity(sendIntent);
    }





}

