package com.sabapp.saba;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.databinding.ClientdashboardfragmentBinding;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class dashboardfragment extends Fragment {

    private ClientdashboardfragmentBinding binding;



    sabaapp app;

    String token;
    String recipient_id;
    String accountjoincode;
    public Boolean isCamera=false;
    public Boolean getIsCamera() {
        return isCamera;
    }

    private DashboardViewModal homeViewModel;
    PermissionListener permissionlistener;

    String currency_symbol = "$";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    AVLoadingIndicatorView progressBar;

    public void showProgressBar()
    {
        progressBar.smoothToShow();
    }
    public void hideProgressBar()
    {
        progressBar.smoothToHide();
    }


    RecyclerView eventsrecycler;
    RecyclerView activitiesrecycler;

    ArrayList<String> eventnamelist;
    ArrayList<String> eevnttimelist;

    ArrayList<String> eventcurrencylist;
    ArrayList<String> eventamountlist;

    ArrayList<String> eventlocationlist;
    ArrayList<String> eventdatelocation;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(DashboardViewModal.class);

        binding = ClientdashboardfragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }


}
