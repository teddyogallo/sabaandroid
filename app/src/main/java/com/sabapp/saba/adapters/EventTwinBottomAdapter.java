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

import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;

import java.util.ArrayList;
import java.util.List;
import com.sabapp.saba.R;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.events.eventdashboard;
import com.sabapp.saba.homeclientFragment;

public class EventTwinBottomAdapter extends RecyclerView.Adapter<EventTwinBottomAdapter.ViewHolder> {
    private List<sabaEventItem> items = new ArrayList<>();

    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private eventdashboard pulse;

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


        if(item.geteventtwin_notificationtitle()==null || item.geteventtwin_notificationtitle().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("No Value ");
        }
        else
        {
            holder.title.setText(item.geteventtwin_notificationtitle());
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

    public EventTwinBottomAdapter(List<sabaEventItem> bitmapList, Context context, eventdashboard pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }
}

