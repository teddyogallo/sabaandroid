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
import com.sabapp.saba.vendorFragment;

import java.util.List;

public class servicesOfferedRecyclerAdapter extends RecyclerView.Adapter<servicesOfferedRecyclerAdapter.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private vendorFragment pulse;
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

    public servicesOfferedRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, vendorFragment pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public servicesOfferedRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicesofferedcell, parent, false);

        return new servicesOfferedRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(servicesOfferedRecyclerAdapter.MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        final sabaEventItem sabaItem = bitmapList.get(position);

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
                String productname = sabaItem.getevent_nameAssigned();
                final Intent intent;

                if(productname==null ||productname.equals(""))
                {
                    // redirect to add new product

                    /*intent =  new Intent(context, createevent.class);
                    context.startActivity(intent);*/

                    //end of redirect to new product

                }else if(productname.toLowerCase().matches("no event assigned"))
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
