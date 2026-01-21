package com.sabapp.saba.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;

import java.util.ArrayList;
import java.util.List;
import com.sabapp.saba.R;
import com.sabapp.saba.events.OnOverviewAlertClickListener;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.events.eventdashboard;
import com.sabapp.saba.homeclientFragment;

public class EventTwinBottomAdapter extends RecyclerView.Adapter<EventTwinBottomAdapter.ViewHolder> {
    private List<sabaEventItem> items = new ArrayList<>();

    sabaapp app;
    int selected_position = 0;

    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private eventdashboard pulse;

    OnOverviewAlertClickListener listener;

    /*public void setData(List<sabaEventItem> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventtwintimelinecell, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sabaEventItem item = items.get(position);


        if(item.geteventoverviewalertname()==null || item.geteventoverviewalertname().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("No Value ");




        }
        else
        {
            holder.title.setText(item.geteventoverviewalertname());
        }


        if(item.geteventoverviewalertdescription()==null || item.geteventoverviewalertdescription().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.eventalertvalue.setText("No alert detail has been set");


        }
        else
        {
            holder.eventalertvalue.setText(item.geteventoverviewalertdescription());
        }

        if(item.geteventoverviewalerttype()==null || item.geteventoverviewalerttype().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.eventtwincellbutton.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.saba_micro_icon).into(holder.alerttypeimage);

            holder.eventtwincellbutton.setText("No Action");

            holder.eventtwincellbutton.setTextColor(
                    holder.itemView.getContext().getResources()
                            .getColor(R.color.text_light)
            );

            // background drawable
            holder.eventtwincellbutton.setBackground(
                    holder.itemView.getContext().getResources()
                            .getDrawable(R.drawable.bg_pill_button_primary)
            );

            holder.eventtwincellbutton.setVisibility(View.GONE);


        }else if(item.geteventoverviewalerttype()!=null ){

            if(item.geteventoverviewalerttype().equalsIgnoreCase("no_alert"))
            {
                //holder.eventtitle.setVisibility(View.GONE);
                holder.eventtwincellbutton.setVisibility(View.GONE);

                Glide.with(context).load(R.drawable.notificationbell).into(holder.alerttypeimage);

                holder.eventtwincellbutton.setText("No Action");

                holder.eventtwincellbutton.setTextColor(
                        holder.itemView.getContext().getResources()
                                .getColor(R.color.text_light)
                );

                // background drawable
                holder.eventtwincellbutton.setBackground(
                        holder.itemView.getContext().getResources()
                                .getDrawable(R.drawable.bg_pill_button_primary)
                );

                holder.eventtwincellbutton.setVisibility(View.GONE);
            }
            if(item.geteventoverviewalerttype().equalsIgnoreCase("alerts"))
            {
                //holder.eventtitle.setVisibility(View.GONE);
                holder.eventtwincellbutton.setVisibility(View.GONE);

                Glide.with(context).load(R.drawable.ic_warning).into(holder.alerttypeimage);

                holder.eventtwincellbutton.setText("Fix now");

                holder.eventtwincellbutton.setTextColor(
                        holder.itemView.getContext().getResources()
                                .getColor(R.color.text_light)
                );

                // background drawable
                holder.eventtwincellbutton.setBackground(
                        holder.itemView.getContext().getResources()
                                .getDrawable(R.drawable.bg_pill_button_primary)
                );

                holder.eventtwincellbutton.setVisibility(View.VISIBLE);

            }
            else if( item.geteventoverviewalerttype().equalsIgnoreCase("send_message"))
            {
                //holder.eventtitle.setVisibility(View.GONE);
                holder.eventtwincellbutton.setVisibility(View.VISIBLE);

                Glide.with(context).load(R.drawable.message).into(holder.alerttypeimage);

                holder.eventtwincellbutton.setText("Send a Message");

                holder.eventtwincellbutton.setTextColor(
                        holder.itemView.getContext().getResources()
                                .getColor(R.color.text_light)
                );

                // background drawable
                holder.eventtwincellbutton.setBackground(
                        holder.itemView.getContext().getResources()
                                .getDrawable(R.drawable.bg_pill_button_secondary)
                );

                holder.eventtwincellbutton.setVisibility(View.VISIBLE);


            }
            else if( item.geteventoverviewalerttype().equalsIgnoreCase("service_budget"))
            {
                //holder.eventtitle.setVisibility(View.GONE);
                holder.eventtwincellbutton.setVisibility(View.VISIBLE);

                Glide.with(context).load(R.drawable.addbasket_icon).into(holder.alerttypeimage);

                holder.eventtwincellbutton.setText("Setup Budget");

                holder.eventtwincellbutton.setTextColor(
                        holder.itemView.getContext().getResources()
                                .getColor(R.color.text_light)
                );

                // background drawable
                holder.eventtwincellbutton.setBackground(
                        holder.itemView.getContext().getResources()
                                .getDrawable(R.drawable.bg_pill_button_primary)
                );

                holder.eventtwincellbutton.setVisibility(View.VISIBLE);
            }
            else if(item.geteventoverviewalerttype().equalsIgnoreCase("add_vendor"))
            {
                //holder.eventtitle.setVisibility(View.GONE);
                holder.eventtwincellbutton.setVisibility(View.VISIBLE);

                Glide.with(context).load(R.drawable.janja_icons_storelocator).into(holder.alerttypeimage);

                holder.eventtwincellbutton.setText("Add Vendor");

                holder.eventtwincellbutton.setTextColor(
                        holder.itemView.getContext().getResources()
                                .getColor(R.color.text_light)
                );

                // background drawable
                holder.eventtwincellbutton.setBackground(
                        holder.itemView.getContext().getResources()
                                .getDrawable(R.drawable.bg_pill_button_primary)
                );

                holder.eventtwincellbutton.setVisibility(View.VISIBLE);

            }else{

                holder.eventtwincellbutton.setVisibility(View.VISIBLE);

                Glide.with(context).load(R.drawable.iconbutton).into(holder.alerttypeimage);

                holder.eventtwincellbutton.setText("Continue");

                holder.eventtwincellbutton.setTextColor(
                        holder.itemView.getContext().getResources()
                                .getColor(R.color.text_light)
                );

                // background drawable
                holder.eventtwincellbutton.setBackground(
                        holder.itemView.getContext().getResources()
                                .getDrawable(R.drawable.bg_pill_button_primary)
                );

                holder.eventtwincellbutton.setVisibility(View.VISIBLE);
            }



        }
        else
        {

            Glide.with(context).load(R.drawable.saba_micro_icon).into(holder.alerttypeimage);
            holder.eventtwincellbutton.setVisibility(View.GONE);

        }



        holder.eventtwincellbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String productname = sabaItem.geteventName();
                final Intent intent;

                /*intent =  new Intent(context, createevent.class);
                context.startActivity(intent);*/


            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, eventalertvalue;
        ImageView alerttypeimage, alertdetailsimage;
        Button eventtwincellbutton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventtwincelltitletextvalue);
            eventalertvalue = itemView.findViewById(R.id.eventtwincelltextvalue);

            alerttypeimage = itemView.findViewById(R.id.eventtwincelltypeimage);
            alertdetailsimage  = itemView.findViewById(R.id.eventtwincellmoreimage);
            eventtwincellbutton = itemView.findViewById(R.id.eventtwincellbtn);


        }
    }

    public EventTwinBottomAdapter(List<sabaEventItem> bitmapList, Context context, eventdashboard pulse,OnOverviewAlertClickListener listener, sabaapp app) {
        this.items = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.listener=listener;
        this.app = app;
    }
}

