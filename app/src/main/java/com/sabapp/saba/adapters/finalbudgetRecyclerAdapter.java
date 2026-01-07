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

public class finalbudgetRecyclerAdapter extends RecyclerView.Adapter<finalbudgetRecyclerAdapter.ViewHolder> {

    private List<SelectedCapabilityItem> itemList;
    private Context context;

    public finalbudgetRecyclerAdapter(List<SelectedCapabilityItem> itemList, Context context) {
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
    public finalbudgetRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.budgetitemcell, parent, false);
        return new finalbudgetRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(finalbudgetRecyclerAdapter.ViewHolder holder, int position) {
        SelectedCapabilityItem item = itemList.get(position);
        Log.d("Adding Budget Capability on finali screen", item.getName());
        holder.nameText.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
