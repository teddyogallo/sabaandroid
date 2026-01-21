package com.sabapp.saba.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.chooseservices;
import com.sabapp.saba.events.eventdashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class dashboardclientcapabilityselectRecycler extends RecyclerView.Adapter<dashboardclientcapabilityselectRecycler.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;
    private eventdashboard pulse;

    private Set<Integer> selectedPositions = new HashSet<>();
    private ArrayList<String> selectedCapabilityNames = new ArrayList<>();
    private ArrayList<String> selectedCapabilityCodes = new ArrayList<>();

    private HashMap<String, String> selectedCapabilities = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        public TextView title;
        public TextView locationaddress, datevalues, eventstatus;

        public RelativeLayout cardView;

        public RelativeLayout selectedlayout;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardView=view.findViewById(R.id.serviceslayoutfull);
            thumbnail= view.findViewById(R.id.serviceimage);
            title= view.findViewById(R.id.servicetextlabel);
            selectedlayout = view.findViewById(R.id.serviceselectedlayout);



        }

        @Override
        public void onClick(View v) {


            // Do your another stuff for your onClick
        }
    }

    public dashboardclientcapabilityselectRecycler(List<sabaEventItem> bitmapList, Context context, eventdashboard pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chooseservicecell, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        final sabaEventItem sabaItem = bitmapList.get(position);

        if (selectedPositions.contains(position)) {


            holder.selectedlayout.setBackground(
                    ContextCompat.getDrawable(context, R.drawable.circlebordercolourselected)
            );

            ImageView card = holder.selectedlayout.findViewById(R.id.roundedimag);
            card.setBackgroundColor(ContextCompat.getColor(context, R.color.wayaBlue));


        } else {



            holder.selectedlayout.setBackground(
                    ContextCompat.getDrawable(context, R.drawable.circlebordercolour)
            );

            ImageView card = holder.selectedlayout.findViewById(R.id.roundedimag);
            card.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        if(sabaItem.getcapability_image_location()!=null)
        {


            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));
            try{

                Glide.with(context)
                        .load(sabaItem.getcapability_image_location())
                        .placeholder(R.drawable.defaultimage)
                        .apply(requestOptions)
                        .into(holder.thumbnail);

            } catch (Exception e) {

                Log.e("LOAD IMAGE ERROR: ",""+e);

                Glide.with(context).load(R.drawable.defaultimage).apply(requestOptions).into(holder.thumbnail);

            }






        }
        else
        {

            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));

            Glide.with(context).load(R.drawable.defaultimage).apply(requestOptions).into(holder.thumbnail);
        }


        holder.cardView.setOnClickListener(v -> {

            int position1 = holder.getAdapterPosition();
            String capabilityName = sabaItem.getcapability_name();
            String capabilitycode = sabaItem.getcapability_code();

            if (selectedPositions.contains(position1)) {
                // UNSELECT
                selectedPositions.remove(position1);
                //selectedCapabilityNames.remove(capabilityName);
                //selectedCapabilityCodes.remove(capabilitycode);
                selectedCapabilities.remove(capabilitycode);

                //holder.selectedlayout.setVisibility(View.GONE);
                holder.selectedlayout.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.circlebordercolour)
                );

                ImageView card = holder.selectedlayout.findViewById(R.id.roundedimag);
                card.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

            }
            else {
                // SELECT
                selectedPositions.add(position1);
                //selectedCapabilityNames.add(capabilityName);
                //selectedCapabilityCodes.remove(capabilitycode);
                selectedCapabilities.put(capabilitycode, capabilityName);

                //holder.selectedlayout.setVisibility(View.VISIBLE);
                holder.selectedlayout.setBackground(
                        ContextCompat.getDrawable(context, R.drawable.circlebordercolourselected)
                );

                ImageView card = holder.selectedlayout.findViewById(R.id.roundedimag);
                card.setBackgroundColor(ContextCompat.getColor(context, R.color.wayaBlue));


            }
        });



        if(sabaItem.getcapability_name()==null || sabaItem.getcapability_name().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("General");
        }
        else
        {
            holder.title.setText(sabaItem.getcapability_name());
        }



    }
    /*public ArrayList<String> getSelectedCapabilityNames() {
        return new ArrayList<>(selectedCapabilityNames);
    }*/

    public HashMap<String, String> getSelectedCapabilities() {
        return new HashMap<>(selectedCapabilities);
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }




}