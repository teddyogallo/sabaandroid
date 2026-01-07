package com.sabapp.saba.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sabapp.saba.R;
import com.sabapp.saba.data.model.SelectedCapabilityItem;

import java.util.List;

public class SelectedCapabilityAdapter extends RecyclerView.Adapter<SelectedCapabilityAdapter.ViewHolder> {

    private List<SelectedCapabilityItem> itemList;
    private Context context;

    public SelectedCapabilityAdapter(List<SelectedCapabilityItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.capabilityNameText);
        }
    }

    @Override
    public SelectedCapabilityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.budgetitemcell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedCapabilityAdapter.ViewHolder holder, int position) {
        SelectedCapabilityItem item = itemList.get(position);
        Log.d("Adding Budget Capability", item.getName());
        holder.nameText.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

