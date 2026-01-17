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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.eventdashboard;

import java.util.ArrayList;
import java.util.List;

public class vendorlisteventRecyclerAdapter extends RecyclerView.Adapter<vendorlisteventRecyclerAdapter.ViewHolder> {
    //private List<sabaEventItem> items = new ArrayList<>();
    private List<sabaEventItem> items;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sabaEventItem item = items.get(position);


        if(item.getvendorname()==null || item.getvendorname().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("Not Valid");
        }
        else
        {
            holder.title.setText(item.getvendorname());
        }


        if(item.getvendorserviceimagelocation()!=null)
        {


            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));
            try{

                Glide.with(context)
                        .load(item.getvendorserviceimagelocation())
                        .placeholder(R.drawable.defaultimage)
                        .apply(requestOptions)
                        .into(holder.vendor_image);

            } catch (Exception e) {

                Log.e("LOAD IMAGE ERROR: ",""+e);

                Glide.with(context).load(R.drawable.defaultimage).apply(requestOptions).into(holder.vendor_image);

            }






        }
        else
        {

            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));

            Glide.with(context).load(R.drawable.defaultimage).apply(requestOptions).into(holder.vendor_image);
        }



        holder.vendoritem.setOnClickListener(new View.OnClickListener() {
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
        TextView title, eventalertvalue,availabilitytext;
        ImageView vendor_image, availability_checkimage, workswith1,workswith2,workswith3;
        Button chemistry_badgebutton;

        RatingBar vendorrating;

        LinearLayout vendoritem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vendor_name);
            eventalertvalue = itemView.findViewById(R.id.eventtwincelltextvalue);

            vendor_image = itemView.findViewById(R.id.vendor_image);
            availability_checkimage  = itemView.findViewById(R.id.availability_check);

            workswith1  = itemView.findViewById(R.id.partner_avatar_1);
            workswith2  = itemView.findViewById(R.id.partner_avatar_2);
            workswith3  = itemView.findViewById(R.id.partner_avatar_3);

            vendoritem = itemView.findViewById(R.id.vendor_itemlayout);
            chemistry_badgebutton = itemView.findViewById(R.id.chemistry_badge);
            vendorrating = itemView.findViewById(R.id.vendor_rating);

            availabilitytext = itemView.findViewById(R.id.availability_text);


        }
    }

    public vendorlisteventRecyclerAdapter(List<sabaEventItem> items, Context context, eventdashboard pulse, sabaapp app) {
        this.items = items;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }
}