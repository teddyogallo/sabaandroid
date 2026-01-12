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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.vendorFragment;
import com.sabapp.saba.vendors.eventvendoroverview;

import java.util.List;

public class eventdetailsinfoRecyclerAdapter  extends RecyclerView.Adapter<eventdetailsinfoRecyclerAdapter.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private eventvendoroverview pulse;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        public TextView title;
        public TextView locationaddress, datevalues, eventstatus;

        public LinearLayout cardView, wholelayout;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardView=view.findViewById(R.id.eventlayoutvalues);
            thumbnail = view.findViewById(R.id.celltypeicon);
            title= view.findViewById(R.id.eventtitle);
            locationaddress= view.findViewById(R.id.eventlocation);
            datevalues= view.findViewById(R.id.eventdate);
            wholelayout = view.findViewById(R.id.eventlayoutvalues);


        }


        @Override
        public void onClick(View v) {


            // Do your another stuff for your onClick
        }
    }

    public eventdetailsinfoRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, eventvendoroverview pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public eventdetailsinfoRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventdetailcell, parent, false);

        return new eventdetailsinfoRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(eventdetailsinfoRecyclerAdapter.MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        final sabaEventItem sabaItem = bitmapList.get(position);

        if(sabaItem.getevent_imagelocationAssigned()==null || sabaItem.getevent_imagelocationAssigned().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.thumbnail.setBackground(
                    ContextCompat.getDrawable(holder.thumbnail.getContext(),
                            R.drawable.home_background_ovalnew)
            );

        }
        else
        {
            Context context = holder.thumbnail.getContext();

            int drawableId = context.getResources()
                    .getIdentifier(sabaItem.getevent_imagelocationAssigned(), "drawable", context.getPackageName());

            if (drawableId != 0) {
                holder.thumbnail.setBackground(
                        ContextCompat.getDrawable(context, drawableId)
                );
            }
        }




        holder.wholelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productname = sabaItem.getevent_nameAssignedProposal();
                final Intent intent;

                if(productname==null ||productname.equals(""))
                {
                    // redirect to add new product

                    /*intent =  new Intent(context, createevent.class);
                    context.startActivity(intent);*/

                    //end of redirect to new product

                }else if(productname.toLowerCase().matches("No Proposal"))
                {
                    //start of redirect to new product
                    /*intent =  new Intent(context, wwProductActivity.class);
                    context.startActivity(intent);*/

                    //end of redirect to new product

                }
                else{
                    // redirect to payment popup
                    /*app.setProductselectedprice(sabaItem.getProductprice());
                    app.setProductselectedname(productname);
                    app.setProductimgbyte(context, sabaItem.getBytes());

                    AppCompatActivity activity = (AppCompatActivity) context;
                    requestpaymentprefilled bottomSheetFragment = new requestpaymentprefilled();
                    bottomSheetFragment.show((activity).getSupportFragmentManager(), bottomSheetFragment.getTag());
                    */
                    //End of redirect to payment popup



                }


            }
        });





        if(sabaItem.getevent_nameAssigned()==null || sabaItem.getevent_nameAssigned().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("No Value");
        }
        else
        {
            holder.title.setText(sabaItem.getevent_nameAssigned());
        }



        if(sabaItem.getevent_locationAssigned()==null || sabaItem.getevent_locationAssigned().equals("") || sabaItem.getevent_locationAssigned().equals("null"))
        {
            holder.locationaddress.setVisibility(View.GONE);
        }
        else
        {
            holder.locationaddress.setText(sabaItem.getevent_locationAssigned());
        }

        if(sabaItem.getevent_allocated_timeAssignedProposal()==null || sabaItem.getevent_allocated_timeAssignedProposal().equals(""))
        {
            holder.datevalues.setVisibility(View.GONE);
        }
        else
        {
            holder.datevalues.setText(sabaItem.getevent_allocated_timeAssignedProposal());
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
