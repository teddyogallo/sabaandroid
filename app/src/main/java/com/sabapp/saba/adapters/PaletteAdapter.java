package com.sabapp.saba.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.sabapp.saba.R;

public class PaletteAdapter extends RecyclerView.Adapter<PaletteAdapter.ViewHolder> {

    private final List<Integer> colors;
    private final OnPaletteClickListener listener;

    public interface OnPaletteClickListener {
        void onColorSelected(int color);
    }

    public PaletteAdapter(List<Integer> colors, OnPaletteClickListener listener) {
        this.colors = colors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palette_swatch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color = colors.get(position);

        // Create Circular Swatch
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
        holder.swatchView.setBackground(shape);

        holder.itemView.setOnClickListener(v -> listener.onColorSelected(color));
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View swatchView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swatchView = itemView.findViewById(R.id.swatchView);
        }
    }
}
