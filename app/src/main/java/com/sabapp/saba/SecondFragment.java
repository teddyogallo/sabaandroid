package com.sabapp.saba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabapp.saba.adapters.messagefragmentRecyclerAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.databinding.FragmentSecondBinding;
import com.sabapp.saba.messaging.messagestartactivity;
import com.sabapp.saba.payments.requestpaymentpopup;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSecondBinding binding;

    private String activeUser;
    private String chatName;
    private String createAdrAd;

    TextView balanceamounttext;

    Button withdrawtopupbutton;

    private RecyclerView paymentlistRecycler;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    ArrayList<sabaEventItem> pymentsvalueArraystwo;

    sabaapp app;
    AVLoadingIndicatorView progressBar;

    String accountlogintype;

    Context context;

    public static SecondFragment newInstance(
            String activeUser,
            String chatName,
            String createAdrAd
    ) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("activeuser", activeUser);
        args.putString("chatname", chatName);
        args.putString("createadrad", createAdrAd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // now you can safely use it
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
        //getFevorites();


        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("CHAT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("PAYMENT_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("ORDER_BROADCAST"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("NOTIFICATION_LOCAL_BROADCAST"));





    }

    private void setUpRecycler()
    {



        //mMessageAdapter=new conversationactivityRecycler(templateArraystwo,getApplicationContext(), this);
        /*adaptertwo = new messagefragmentRecyclerAdapter(paymentlistRecycler, context, SecondFragment.this);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(context));
        mMessageRecycler.setAdapter(adaptertwo);*/

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
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        View view =  inflater.inflate(R.layout.fragment_second, container, false);
        context = requireContext();

        paymentlistRecycler = (RecyclerView) view.findViewById(R.id.paymentlistrecycler);

        balanceamounttext = (TextView) view.findViewById(R.id.accountbalancetextlabel);
        withdrawtopupbutton = (Button)view.findViewById(R.id.withdrawbutton);



        progressBar = view.findViewById(R.id.progressBar);
        hideProgressBar();

        accountlogintype = app.getLoginAccounttype();


        if(accountlogintype!=null && accountlogintype.equalsIgnoreCase("vendor")){


            withdrawtopupbutton.setBackground(ContextCompat.getDrawable(    context, R.drawable.cr8lr270a7be2e80d7f40a80));
            withdrawtopupbutton.setText("Withdraw");


        }else{

            withdrawtopupbutton.setBackground(ContextCompat.getDrawable(    context, R.drawable.cr10lr270cd01a2891161));

            withdrawtopupbutton.setText("Top Up");
        }


        withdrawtopupbutton.setOnClickListener(v -> {
            // Action when layout is clicked
            // For example, open a new fragment or show a toast
            if(accountlogintype!=null && !accountlogintype.equalsIgnoreCase("vendor")){


                showRequestPaymentpopup();


            }


        });





        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showProgressBar()
    {
        progressBar.smoothToShow();
    }
    public void hideProgressBar()
    {
        progressBar.smoothToHide();
    }


    public void showRequestPaymentpopup()
    {


        /*requestpaymentpopup bottomSheetFragment = new requestpaymentpopup();
        Bundle args = new Bundle();
        args.putString("VALUE_ARRAY", dataobj.toString());
        bottomSheetFragment.setArguments(args);

        bottomSheetFragment.show((this).getSupportFragmentManager(), bottomSheetFragment.getTag());*/

        AppCompatActivity activity = (AppCompatActivity) context;
        requestpaymentpopup bottomSheetFragment = new requestpaymentpopup();
        bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());


    }

}