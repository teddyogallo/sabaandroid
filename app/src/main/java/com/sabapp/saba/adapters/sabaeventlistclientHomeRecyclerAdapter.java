package com.sabapp.saba.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.createevent;
import com.sabapp.saba.events.eventdashboard;
import com.sabapp.saba.homeclientFragment;

public class sabaeventlistclientHomeRecyclerAdapter  extends RecyclerView.Adapter<sabaeventlistclientHomeRecyclerAdapter.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private homeclientFragment pulse;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        public TextView title;
        public TextView locationaddress, datevalues, eventstatus;

        public LinearLayout cardView,eventlayout;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            eventlayout = view.findViewById(R.id.eventlayoutvalues);
            cardView=view.findViewById(R.id.eventstatuslayout);
            thumbnail= view.findViewById(R.id.serviceimage);
            title= view.findViewById(R.id.eventtitle);
            locationaddress= view.findViewById(R.id.eventlocation);
            datevalues= view.findViewById(R.id.eventdate);
            eventstatus= view.findViewById(R.id.eventstatustext);


        }


        @Override
        public void onClick(View v) {


            // Do your another stuff for your onClick
        }
    }

    public sabaeventlistclientHomeRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, homeclientFragment pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventshorizontalcell, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        final sabaEventItem sabaItem = bitmapList.get(position);

        if(sabaItem.geteventimageLocation()!=null)
        {


            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));
            try{

                Glide.with(context)
                        .load(sabaItem.geteventimageLocation())
                        .placeholder(R.drawable.defaultimage)
                        .apply(requestOptions)
                        .into(holder.thumbnail);

            } catch (Exception e) {

               Log.e("LOAD IMAGE ERROR: ",""+e);

                Glide.with(context).load(R.drawable.defaultimage).apply(requestOptions).into(holder.thumbnail);

            }



            /*Glide.with(context)
                    .load(sabaItem.geteventimageEncoded())
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.thumbnail);*/


        }
        else
        {

            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));

            Glide.with(context).load(R.drawable.defaultimage).apply(requestOptions).into(holder.thumbnail);
        }
//  Glide.with(context).load(sabaItem.getTemplateImg()).into(holder.thumbnail);

        //for loading images
        //Glide.with(context).load(decodedByte).apply(RequestOptions.skipMemoryCacheOf(true)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)).into(adimage);



        holder.eventlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productname = sabaItem.geteventName();
                final Intent intent;

                if(productname==null ||productname.equals(""))
                {
                    // redirect to add new product

                    intent =  new Intent(context, createevent.class);
                    context.startActivity(intent);

                    //end of redirect to new product

                }else if(productname.toLowerCase().matches("No Name")||productname.equals("no name"))
                {
                    //start of redirect to new product
                    intent =  new Intent(context, createevent.class);
                    context.startActivity(intent);

                    //end of redirect to new product

                }else if(productname.toLowerCase().matches("Add Event")||productname.equals("add event"))
                {
                    //start of redirect to new product
                    intent =  new Intent(context, createevent.class);
                    context.startActivity(intent);

                    //end of redirect to new product

                }
                else{
                    // redirect to payment popup
                    intent =  new Intent(context, eventdashboard.class);
                    intent.putExtra("event_name", productname);
                    intent.putExtra("event_id",sabaItem.getevent_Id ());
                    intent.putExtra("event_time",sabaItem.geteventTime());
                    intent.putExtra("event_location",sabaItem.geteventLocation());
                    intent.putExtra("event_budget",sabaItem.geteventBudget());
                    intent.putExtra("event_budgetspent",sabaItem.getbudgetSpent());
                    intent.putExtra("event_setupstatus",sabaItem.getsetupStatus());
                    intent.putExtra("event_eventstatus",sabaItem.geteventStatus());
                    intent.putExtra("event_plannerid",sabaItem.getplannerId());
                    intent.putExtra("image_location",sabaItem.geteventimageLocation());
                    intent.putExtra("time_setup",sabaItem.gettime_Setup());
                    intent.putExtra("user_id",sabaItem.geteventUserid());
                    intent.putExtra("event_timeunix",sabaItem.geteventTimeUnix());

                    context.startActivity(intent);
                    //End of redirect to payment popup
                }


            }
        });





        if(sabaItem.geteventName()==null || sabaItem.geteventName().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("No Name");
        }
        else
        {
            holder.title.setText(sabaItem.geteventName());
        }



        if(sabaItem.geteventLocation()==null || sabaItem.geteventLocation().equals(""))
        {
            holder.locationaddress.setVisibility(View.GONE);
        }
        else
        {
            holder.locationaddress.setText(sabaItem.geteventLocation());
        }

        if(sabaItem.geteventTime()==null || sabaItem.geteventTime().equals(""))
        {
            holder.datevalues.setVisibility(View.GONE);
        }
        else
        {
            holder.datevalues.setText(sabaItem.geteventTime());
        }


        if(sabaItem.geteventStatus()==null || sabaItem.geteventStatus().equals(""))
        {


            holder.cardView.setBackground(
                    ContextCompat.getDrawable(holder.cardView.getContext(),
                            R.drawable.home_background_ovalnew)
            );

        }
        else
        {

            if(sabaItem.geteventStatus().equalsIgnoreCase("pending")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.orangesquere)
                );

            }else if(sabaItem.geteventStatus().equalsIgnoreCase("upcoming")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.s8b868833sw1cr101)
                );

            }else if(sabaItem.geteventStatus().equalsIgnoreCase("completed")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.cr8lr270a7be2e80d7f40a80)
                );

            }
            else if(sabaItem.geteventStatus().equalsIgnoreCase("cancelled")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.cr5bfb01600d)
                );

            }
            else if(sabaItem.geteventStatus().equalsIgnoreCase("draft")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.greysquere)
                );

            }else{

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.cr10lr270fb0160f703d0)
                );

            }

        }


        if(sabaItem.geteventStatus()==null || sabaItem.geteventStatus().equals(""))
        {
            holder.eventstatus.setText(sabaItem.geteventStatus());
        }
        else
        {
            holder.eventstatus.setText(sabaItem.geteventStatus());
        }







//


    }
    public Bitmap convert_image(String base64imagestring) {
        byte[] decodedString = Base64.decode(base64imagestring, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //item.setBytes(decodedString);

        return decodedByte;

    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }
}