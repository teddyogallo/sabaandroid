package com.sabapp.saba.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.sabapp.saba.R;
import com.sabapp.saba.data.model.EventItem;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private final List<EventItem> eventList;
    private final Context context;

    private final ViewHolder.OnTimelineActionListener listener;

    public TimelineAdapter(Context context, List<EventItem> eventList, ViewHolder.OnTimelineActionListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventItem item = eventList.get(position);

        holder.timeLabel.setText(item.time);
        holder.title.setText(item.title);
        holder.vendorName.setText(item.vendorName);
        holder.statusBadge.setText(item.status);

        // Handle UI states based on booking status
        if (item.isBooked) {
            setupBookedState(holder);
        } else if (item.status.equals("Action Required")) {
            setupActionRequiredState(holder);
        } else {
            setupPendingState(holder);
        }

        holder.btnCall.setOnClickListener(v->{
            if(listener!=null){
                listener.onCallVendor(item.vendorName);

            }


        });

        holder.btnAddVendor.setOnClickListener(v->{

            if(listener!=null){
                if(item.status.equalsIgnoreCase("action required")){
                    listener.onAddVendor(item.title);
                }else{
                    listener.onViewInquiry(item.vendorName);
                }
            }


        });
    }

    private void setupBookedState(ViewHolder holder) {
        holder.statusBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.greenthemelight));
        holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.green_active));
        holder.btnCall.setVisibility(View.VISIBLE);
        holder.btnAddVendor.setVisibility(View.GONE);
    }

    private void setupActionRequiredState(ViewHolder holder) {
        holder.statusBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.chemistry_red));
        holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.deepredsaba));
        holder.btnCall.setVisibility(View.GONE);
        holder.btnAddVendor.setVisibility(View.VISIBLE);
    }

    private void setupPendingState(ViewHolder holder) {
        holder.statusBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.yellow));
        holder.statusBadge.setTextColor(ContextCompat.getColor(context, R.color.wayayellow));
        holder.btnCall.setVisibility(View.GONE);
        holder.btnAddVendor.setVisibility(View.VISIBLE);
        holder.btnAddVendor.setText("View Inquiry");
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeLabel, title, vendorName, statusBadge;
        Button btnCall, btnAddVendor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeLabel = itemView.findViewById(R.id.timeLabel);
            title = itemView.findViewById(R.id.eventTitleInCard); // Ensure IDs match your XML
            vendorName = itemView.findViewById(R.id.vendorName);
            statusBadge = itemView.findViewById(R.id.statusBadge);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnAddVendor = itemView.findViewById(R.id.btnAddVendor);
        }


        public interface OnTimelineActionListener {
            void onCallVendor(String vendorName);
            void onAddVendor(String category);
            void onViewInquiry(String vendorName);
        }


    }
}

