package com.sabapp.saba.payments;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;

public class requestpaymentpopup extends DialogFragment {
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
    private  TextView businessnumber;
    private TextView businessjoincode;
    private ViewPager viewPager;
    TabLayout tabs;




    public requestpaymentpopup() {
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
            params.height = 1600;
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        }




    }
    Context context;




    public static requestpaymentpopup newInstance() {
        return new requestpaymentpopup();
    }



    private void initializePlayer() {

    }
    private static requestpaymentpopup instance;
    public static requestpaymentpopup getInstance()
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



        View view=inflater.inflate(R.layout.requestpaymentpopup, container, false);
        view.findViewById(R.id.requestpaymentbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsAppConversationUsingUri(mContext); }
        });


        viewPager = view.findViewById(R.id.view_pager);
        tabs = view.findViewById(R.id.tabs);
        //businessnumber = view.findViewById(R.id.numbertosavetext);
        //businessjoincode =  view.findViewById(R.id.joincodetext);
        //businessnumber.setText("Ask customers to save WhatsApp number +254756 773379");
        //businessjoincode.setText("Ask your customers to Start by sending WhatsApp Message “Join wayawayatest” to +254797318700");

        requestpaymentpopupPagerAdapterOld pageAdapter = new requestpaymentpopupPagerAdapterOld(getChildFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tabs.setupWithViewPager(viewPager);

//        viewPager = view.findViewById(R.id.view_pager);
//        WWTemplateAdapter sectionsPagerAdapter = new WWTemplateAdapter(mContext, getChildFragmentManager());
//
//        viewPager.setAdapter(sectionsPagerAdapter);
//        tabs.setupWithViewPager(viewPager);
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



    public static void openWhatsAppConversationUsingUri(Context context) {
        //get the Join code and dedicated number
        Uri uri = Uri.parse("https://wa.me/254797318700?text=join wayawayatest");

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

        context.startActivity(sendIntent);
    }

    public static void openJanjawebsite(Context context) {
        //get the Join code and dedicated number
        Uri uri = Uri.parse("https://www.janja.me/getbranded.php");

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

        context.startActivity(sendIntent);
    }






    static class requestpaymentpopupPagerAdapterOld extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        public requestpaymentpopupPagerAdapterOld(FragmentManager fm) {
            super(fm);

            // Price
            mobilepaymenttab onePriceFragment = new mobilepaymenttab();
            fragments.add(onePriceFragment);
            titles.add("Mobile Money Top-Up");
            // auction
            cardpaymenttab auctionFragment = new cardpaymenttab();
            fragments.add(auctionFragment);
            titles.add("Debit/Credit card top up");

            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }





}

