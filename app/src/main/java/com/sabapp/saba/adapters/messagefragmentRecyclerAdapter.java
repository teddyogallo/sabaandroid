package com.sabapp.saba.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.sabapp.saba.R;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.sabaEventItem;
import com.sabapp.saba.messageFragment;
import com.sabapp.saba.messaging.conversationactivity;
import com.sabapp.saba.messaging.messagestartactivity;
import com.sabapp.saba.sabaDrawerActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagefragmentRecyclerAdapter extends RecyclerView.Adapter<messagefragmentRecyclerAdapter.MyViewHolder>{
    int selected_position = 0;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<sabaEventItem> bitmapList;
    sabaapp app;
    private List<Boolean> selected;
    private Context context;
    private messageFragment pulse;

    private sabaEventItem wayaWayaItem;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView thumbnail;

        public TextView title;
        public TextView messagemain;
        public TextView timedetails;
        public LinearLayout mainlayout;
        public MaterialCardView cardView;


        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardView=view.findViewById(R.id.container);
            thumbnail= view.findViewById(R.id.advertimage);
            title= view.findViewById(R.id.txtname);
            messagemain= view.findViewById(R.id.messagecontent);
            mainlayout= view.findViewById(R.id.content);
            timedetails= view.findViewById(R.id.messagetimestamp);


        }


        @Override
        public void onClick(View v) {

           /* final Intent intent;
            String useridactive =  wayaWayaItem.getStatus();
            intent =  new Intent(context, conversationactivity.class);
            intent.putExtra("createadrad", "gotowholechatpref");
            intent.putExtra("activeuser", useridactive);
            intent.putExtra("chatname", sabaEventItem.getBusinessname());
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);*/


            // Do your another stuff for your onClick
        }
    }

    public messagefragmentRecyclerAdapter(List<sabaEventItem> bitmapList, Context context, messageFragment pulse) {
        this.bitmapList = bitmapList;
        this.context=context;
        this.pulse=pulse;

    }



    public void updateList(List<sabaEventItem> newList) {
        this.bitmapList = newList; // Update the list
        notifyDataSetChanged();    // Notify the adapter about data changes
    }

    @Override
    public messagefragmentRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chathead, parent, false);
        app = (sabaapp) context.getApplicationContext();

        return new messagefragmentRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(messagefragmentRecyclerAdapter.MyViewHolder holder, int position) {

        // holder.cardView.setStrokeColor(selected_position == position ? Color.YELLOW : Color.TRANSPARENT);
        //
        final sabaEventItem wayaWayaItem = bitmapList.get(position);
        //wayaWayaItem = bitmapList.get(position);

        /*
        Log.e("n","n"+wayaWayaItem.getBusinessname());
        Glide.with(context).load(wayaWayaItem.getTemplateImg()).into(holder.thumbnail);
        */
        if(wayaWayaItem.getBytes()!=null)
        {
            Glide.with(context).load(wayaWayaItem.getBytes()).into(holder.thumbnail);
        }
        else
        {
            Glide.with(context).load(R.drawable.defaultimage).into(holder.thumbnail);
        }

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String productname = wayaWayaItem.getBusinessname();
                String useridactive = wayaWayaItem.getStatus();
                final Intent intent;

                if(productname==null || productname.equals(""))
                {
                    // redirect to add new product



                    //end of redirect to new product

                }
                else if(productname.toLowerCase().matches("Start new chat")||productname.equals("No new messages"))
                {
                    //start of redirect to new product
                    intent =  new Intent(context, sabaDrawerActivity.class);

                    context.startActivity(intent);


                    //end of redirect to new product

                }
                else{
                    // redirect to exact product or whole Shop

                    intent =  new Intent(context, conversationactivity.class);
                    intent.putExtra("createadrad", "gotowholechatpref");
                    intent.putExtra("activeuser", useridactive);
                    intent.putExtra("chatname", wayaWayaItem.getBusinessname());
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);

                    //End of redirect to exact product or whole Shop
                }

            }
        });


        //action for delete button


        if(wayaWayaItem.getBusinessname()==null || wayaWayaItem.getBusinessname().equals(""))
        {
            holder.title.setVisibility(View.GONE);
        }
        else
        {
            holder.title.setText(wayaWayaItem.getBusinessname());
        }



        if(wayaWayaItem.getProductprice()==null || wayaWayaItem.getProductprice().equals(""))
        {
            holder.messagemain.setVisibility(View.GONE);
        }
        else
        {
            holder.messagemain.setText(wayaWayaItem.getProductprice());
        }


        if(wayaWayaItem.getDetails()==null ||wayaWayaItem.getDetails().equals("") )
        {
            holder.timedetails.setVisibility(View.GONE);
        }
        else
        {
            holder.timedetails.setText(wayaWayaItem.getDetails());
        }



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String useridactive =  wayaWayaItem.getStatus();
                final Intent intent;

                if(useridactive==null || useridactive.equals(""))
                {
                    // redirect to add new product
                    intent =  new Intent(context, sabaDrawerActivity.class);

                    context.startActivity(intent);

                    //end of redirect to new product

                }
                else if(useridactive.toLowerCase().matches("no_val")||useridactive.equals("No_Val"))
                {
                    //start of redirect to new product
                    intent =  new Intent(context, sabaDrawerActivity.class);
                    context.startActivity(intent);

                    //end of redirect to new product

                }
                else{
                    // redirect to exact product or whole Shop


                    intent =  new Intent(context, conversationactivity.class);
                    intent.putExtra("createadrad", "gotowholechatpref");
                    intent.putExtra("activeuser", useridactive);
                    intent.putExtra("chatname", wayaWayaItem.getBusinessname());
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);

                    //End of redirect to exact product or whole Shop
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }







}
