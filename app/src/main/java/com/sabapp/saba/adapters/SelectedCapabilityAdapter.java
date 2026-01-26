package com.sabapp.saba.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sabapp.saba.R;
import com.sabapp.saba.data.model.SelectedCapabilityItem;

import java.util.List;

public class SelectedCapabilityAdapter extends RecyclerView.Adapter<SelectedCapabilityAdapter.ViewHolder> {

    private List<SelectedCapabilityItem> itemList;
    private Context context;

    private double availableBudget;

    public SelectedCapabilityAdapter(List<SelectedCapabilityItem> itemList, Context context,double availableBudget) {
        this.itemList = itemList;
        this.context = context;
        this.availableBudget = availableBudget;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText,valueText,lineitemamount;
        SeekBar seekBar;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.capabilityNameText);

            valueText = itemView.findViewById(R.id.spenttrackertext);
            seekBar = itemView.findViewById(R.id.budgetamountvalue);

            lineitemamount = itemView.findViewById(R.id.capabilitybudgettextamount);

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

        holder.seekBar.setProgress(item.getSelectedValue());
        String capabilityamount = String.valueOf(item.getSelectedValue());

        try {
            double percentage = (double) item.getSelectedValue() /100;
            double doubleamountallocated = availableBudget * percentage;

            holder.valueText.setText("Allocate $"+doubleamountallocated+"/ $ "+availableBudget+" available");

            holder.lineitemamount.setText("$"+doubleamountallocated);


        }catch(Exception e){

            holder.valueText.setText("Allocate $ 0/ $ "+availableBudget+" available");
            holder.lineitemamount.setText("$ 0.00");

        }



        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                item.setSelectedValue(progress);     // store value
                holder.valueText.setText(String.valueOf(progress)); // update UI

                try {
                    double percentage = (double) progress / 100;
                    double doubleamountallocated = availableBudget * percentage;

                    item.setAllocatedAmount(doubleamountallocated);

                    holder.valueText.setText("Allocate $"+doubleamountallocated+"/ $ "+availableBudget+" available");

                    holder.lineitemamount.setText("$"+doubleamountallocated);


                }catch(Exception e){

                    holder.valueText.setText("Allocate $ 0/ $ "+availableBudget+" available");
                    holder.lineitemamount.setText("$ 0.00");

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

