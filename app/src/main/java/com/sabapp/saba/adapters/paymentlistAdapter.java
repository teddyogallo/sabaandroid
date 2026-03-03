package com.sabapp.saba.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.SecondFragment;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.eventdashboard;
import com.sabapp.saba.vendors.vendorproposrequest;

import java.util.List;

public class paymentlistAdapter  extends RecyclerView.Adapter<paymentlistAdapter.ViewHolder> {
    //private List<sabaEventItem> items = new ArrayList<>();
    private List<sabaEventItem> items;



    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private SecondFragment pulse;

    /*public void setData(List<sabaEventItem> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentitemcell, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sabaEventItem item = items.get(position);

        String paymentcurrency = "$";


        if(item.getpaymentDescription()==null || item.getpaymentDescription().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("Not Valid");
        }
        else
        {
            holder.title.setText(item.getpaymentDescription());
        }

        if(item.getpaymentCurrency()!=null || !item.getpaymentCurrency().equals("")){

            paymentcurrency = item.getpaymentCurrency();
        }


        if(item.getpaymentAmount()==null || item.getpaymentAmount().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.paymentstatuslayouttext.setText(paymentcurrency+" 0.00");
        }
        else
        {
            holder.paymentstatuslayouttext.setText(paymentcurrency+" "+item.getpaymentAmount());
        }




        if(item.getpaymentTimePaid()==null || item.getpaymentTimePaid().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);

            holder.datetext.setText("");
        }
        else
        {
            holder.datetext.setText(item.getpaymentTimePaid());
        }


        String paymentstatus = item.getPaymentStatus();

        if (paymentstatus == null || paymentstatus.equals("")) {

            holder.statustitle.setText("F");
            holder.statustitle.setTextColor(
                    ContextCompat.getColor(context, R.color.gray_dark)
            );

        } else if (paymentstatus.equalsIgnoreCase("pending")) {

            holder.statustitle.setText("P");
            holder.statustitle.setTextColor(
                    ContextCompat.getColor(context, R.color.orangeDefualt)
            );

        } else if (paymentstatus.equalsIgnoreCase("success")
                || paymentstatus.equalsIgnoreCase("successful")
                || paymentstatus.equalsIgnoreCase("completed")) {

            holder.statustitle.setText("S");
            holder.statustitle.setTextColor(
                    ContextCompat.getColor(context, R.color.green_active)
            );

        } else if (paymentstatus.equalsIgnoreCase("failed")) {

            holder.statustitle.setText("F");
            holder.statustitle.setTextColor(
                    ContextCompat.getColor(context, R.color.wayaRed)
            );

        } else {

            holder.statustitle.setText("E");
            holder.statustitle.setTextColor(
                    ContextCompat.getColor(context, R.color.grey)
            );
        }

        if(item.getpaymentTransactionNumber()==null || item.getpaymentTransactionNumber().equals("")){
            holder.detailstext.setText("");

        }else{

            holder.detailstext.setText(item.getpaymentTransactionNumber());

        }





    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView statustitle, title, detailstext,datetext, paymentstatuslayouttext;

        LinearLayout  paymentstatuslayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventtitle);
            statustitle = itemView.findViewById(R.id.paymentlabel);
            detailstext= itemView.findViewById(R.id.eventlocation);
            datetext= itemView.findViewById(R.id.eventdate);
            paymentstatuslayouttext= itemView.findViewById(R.id.eventstatustext);
            paymentstatuslayout= itemView.findViewById(R.id.eventstatuslayout);

        }
    }

    public paymentlistAdapter(List<sabaEventItem> items, Context context, SecondFragment pulse, sabaapp app) {

        this.items = items;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }
}