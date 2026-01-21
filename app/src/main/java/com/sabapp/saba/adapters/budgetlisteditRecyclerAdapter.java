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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.events.eventdashboard;
import com.sabapp.saba.homeclientFragment;

import java.util.List;

public class budgetlisteditRecyclerAdapter  extends RecyclerView.Adapter<budgetlisteditRecyclerAdapter.MyViewHolder>{
    sabaapp app;
    int selected_position = 0;
    private List<sabaEventItem> bitmapList;
    private List<Boolean> selected;
    private Context context;

    private Activity activity;
    private eventdashboard pulse;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public TextView title,capabilitybudgettextamountvalue,spenttrackertextvalue;

        public SeekBar budgetamountvalueseek;


        public LinearLayout cardView;



        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardView=view.findViewById(R.id.eventlayoutvalues);

            title= view.findViewById(R.id.capabilityNameText);
            capabilitybudgettextamountvalue= view.findViewById(R.id.capabilitybudgettextamount);
            budgetamountvalueseek = view.findViewById(R.id.budgetamountvalue);

            spenttrackertextvalue= view.findViewById(R.id.spenttrackertext);



        }


        @Override
        public void onClick(View v) {


            // Do your another stuff for your onClick
        }
    }

    public budgetlisteditRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, eventdashboard pulse, sabaapp app) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;
        this.app = app;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetitemcell, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        final sabaEventItem sabaItem = bitmapList.get(position);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
            holder.capabilitybudgettextamountvalue.setText("$0");
        }
        else
        {
            holder.capabilitybudgettextamountvalue.setText(sabaItem.getvendorlocation());
        }


        if(sabaItem.getvendorcapabilityname()==null || sabaItem.getvendorcapabilityname().equals(""))
        {
            //holder.eventtitle.setVisibility(View.GONE);
            holder.spenttrackertextvalue.setText("Paid $ 0/ Remaining $ 0");


        }
        else
        {

            holder.spenttrackertextvalue.setText(sabaItem.getvendorcapabilityname());
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