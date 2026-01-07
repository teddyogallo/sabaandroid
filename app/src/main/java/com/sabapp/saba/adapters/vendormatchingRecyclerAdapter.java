package com.sabapp.saba.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sabapp.saba.events.vendormatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class vendormatchingRecyclerAdapter extends RecyclerView.Adapter<vendormatchingRecyclerAdapter.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;
    private vendormatching pulse;

    private Set<Integer> selectedPositions = new HashSet<>();


    private HashMap<String, String> selectedCapabilities = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        public TextView title,locationdetails,servicessupported,minimumbudgettextview;
        public TextView locationaddress, datevalues, eventstatus;

        public LinearLayout cardView, amountlayout;

        public RelativeLayout selectedlayout;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardView=view.findViewById(R.id.eventlayoutvalues);
            thumbnail= view.findViewById(R.id.serviceimage);
            title= view.findViewById(R.id.eventtitle);
            locationdetails= view.findViewById(R.id.eventlocation);
            servicessupported= view.findViewById(R.id.eventdate);
            selectedlayout = view.findViewById(R.id.serviceselectedlayout);
            amountlayout = view.findViewById(R.id.eventstatuslayout);
            minimumbudgettextview = view.findViewById(R.id.eventstatustext);



        }

        @Override
        public void onClick(View v) {


            // Do your another stuff for your onClick
        }
    }


    public vendormatchingRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, vendormatching pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public vendormatchingRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendorhorizontalcellselect, parent, false);

        return new vendormatchingRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(vendormatchingRecyclerAdapter.MyViewHolder holder, int position) {

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

        if(sabaItem.getvendorserviceimagelocation()!=null)
        {


            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));
            try{

                Glide.with(context)
                        .load(sabaItem.getvendorserviceimagelocation())
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
            String capabilityName = sabaItem.getvendorname();
            String capabilitycode = sabaItem.getvendorid();

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



        if(sabaItem.getvendorname()==null || sabaItem.getvendorname().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("Not Valid");
        }
        else
        {
            holder.title.setText(sabaItem.getvendorname());
        }


        if(sabaItem.getvendorlocation()==null || sabaItem.getvendorlocation().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.locationdetails.setText("Not information");
        }
        else
        {
            holder.locationdetails.setText(sabaItem.getvendorlocation());
        }


        if(sabaItem.getvendorcapabilityname()==null || sabaItem.getvendorcapabilityname().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.servicessupported.setText("No information");

            holder.servicessupported.setVisibility(View.GONE);
        }
        else
        {
            holder.servicessupported.setVisibility(View.VISIBLE);
            holder.servicessupported.setText(sabaItem.getvendorcapabilityname());
        }


        if(sabaItem.getvendorbase_price()==null || sabaItem.getvendorbase_price().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);

            holder.amountlayout.setVisibility(View.GONE);
        }
        else
        {
            holder.amountlayout.setVisibility(View.VISIBLE);
            holder.minimumbudgettextview.setText(sabaItem.getvendorbase_price());
        }



    }

    public HashMap<String, String> getSelectedCapabilities() {
        return new HashMap<>(selectedCapabilities);
    }


    @Override
    public int getItemCount() {
        return bitmapList.size();
    }


}
