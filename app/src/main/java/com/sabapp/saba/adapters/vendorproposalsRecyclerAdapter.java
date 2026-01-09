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

import java.util.List;

public class vendorproposalsRecyclerAdapter extends RecyclerView.Adapter<vendorproposalsRecyclerAdapter.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private vendorFragment pulse;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        public TextView title;
        public TextView locationaddress, datevalues, eventstatus;

        public LinearLayout cardView;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
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

    public vendorproposalsRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, vendorFragment pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public vendorproposalsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposal_listcell, parent, false);

        return new vendorproposalsRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(vendorproposalsRecyclerAdapter.MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        final sabaEventItem sabaItem = bitmapList.get(position);

        if(sabaItem.getevent_imagelocationAssignedProposal()!=null)
        {


            int radius = 15; // corner radius in pixels

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new RoundedCorners(radius));
            try{

                Glide.with(context)
                        .load(sabaItem.getevent_imagelocationAssignedProposal())
                        .placeholder(R.drawable.defaultimage)
                        .apply(requestOptions)
                        .into(holder.thumbnail);

            } catch (Exception e) {

                Log.e("LOAD event assigned IMAGE ERROR: ",""+e);

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



        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
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





        if(sabaItem.getevent_nameAssignedProposal()==null || sabaItem.getevent_nameAssignedProposal().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.title.setText("No Proposal");
        }
        else
        {
            holder.title.setText(sabaItem.getevent_nameAssignedProposal());
        }



        if(sabaItem.getevent_locationAssignedProposal()==null || sabaItem.getevent_locationAssignedProposal().equals(""))
        {
            holder.locationaddress.setVisibility(View.GONE);
        }
        else
        {
            holder.locationaddress.setText(sabaItem.getevent_locationAssignedProposal());
        }

        if(sabaItem.gettime_assignedAssignedProposal()==null || sabaItem.gettime_assignedAssignedProposal().equals(""))
        {
            holder.datevalues.setVisibility(View.GONE);
        }
        else
        {
            holder.datevalues.setText(sabaItem.gettime_assignedAssignedProposal());
        }


        if(sabaItem.getstatusAssignedProposal()==null || sabaItem.getstatusAssignedProposal().equals(""))
        {


            holder.cardView.setBackground(
                    ContextCompat.getDrawable(holder.cardView.getContext(),
                            R.drawable.home_background_ovalnew)
            );

        }
        else
        {

            if(sabaItem.getstatusAssignedProposal().equalsIgnoreCase("pending")|| sabaItem.getstatusAssignedProposal().equalsIgnoreCase("proposal")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.orangesquere)
                );

            }else if(sabaItem.getstatusAssignedProposal().equalsIgnoreCase("active")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.s8b868833sw1cr101)
                );

            }else if(sabaItem.getstatusAssignedProposal().equalsIgnoreCase("completed")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.cr8lr270a7be2e80d7f40a80)
                );

            }
            else if(sabaItem.getstatusAssignedProposal().equalsIgnoreCase("cancelled")){

                holder.cardView.setBackground(
                        ContextCompat.getDrawable(holder.cardView.getContext(),
                                R.drawable.cr5bfb01600d)
                );

            }
            else if(sabaItem.getstatusAssignedProposal().equalsIgnoreCase("draft")){

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


        if(sabaItem.getagreed_priceAssignedProposal()==null || sabaItem.getagreed_priceAssignedProposal().equals(""))
        {

            holder.eventstatus.setVisibility(View.GONE);
        }
        else
        {
            holder.eventstatus.setText(sabaItem.getagreed_priceAssignedProposal());
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