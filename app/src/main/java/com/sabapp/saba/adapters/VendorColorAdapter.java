package com.sabapp.saba.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.sabapp.saba.R;
import com.sabapp.saba.data.model.VendorMatch;

public class VendorColorAdapter extends RecyclerView.Adapter<VendorColorAdapter.ViewHolder> {

    private final List<VendorMatch> vendorList;
    private final OnVendorActionListener listener;

    public interface OnVendorActionListener {
        void onRequestSample(VendorMatch vendor);
    }

    public VendorColorAdapter(List<VendorMatch> vendorList, OnVendorActionListener listener) {
        this.vendorList = vendorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vendor_color_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VendorMatch vendor = vendorList.get(position);

        holder.vendorName.setText(vendor.name);
        holder.matchBadge.setText(vendor.status);

        // Styling the badge based on vendor's availability color
        try {
            holder.matchBadge.setBackgroundColor(Color.parseColor(vendor.badgeColor));
        } catch (Exception e) {
            holder.matchBadge.setBackgroundColor(Color.GRAY);
        }

        holder.btnRequest.setOnClickListener(v -> listener.onRequestSample(vendor));
    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vendorName, matchBadge;
        Button btnRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vendorName = itemView.findViewById(R.id.vendorNameText);
            matchBadge = itemView.findViewById(R.id.matchStatusBadge);
            btnRequest = itemView.findViewById(R.id.btnRequestSample);
        }
    }
}

