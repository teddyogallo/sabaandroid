package com.sabapp.saba.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.sabapp.saba.R;
import com.sabapp.saba.data.model.ImageItem;

public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.ViewHolder> {

    private final List<ImageItem> items;
    private final OnImageInteractionListener listener;

    public interface OnImageInteractionListener {
        void onImageTouch(ImageView view, MotionEvent event, ImageItem item);
        void onImageLocked(ImageItem item);
    }

    public MasonryAdapter(List<ImageItem> items, OnImageInteractionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageItem item = items.get(position);

        // Handle both Resource IDs and Bitmaps
        if (item.bitmap != null) {
            holder.img.setImageBitmap(item.bitmap);
        } else {
            holder.img.setImageResource(item.imageRes);
        }


        // Lock Visibility
        holder.lockIcon.setVisibility(item.isLocked ? View.VISIBLE : View.GONE);

        // Color Dropper Touch Listener
        holder.img.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                listener.onImageTouch(holder.img, event, item);
            }
            return false; // Allow click/long-click to propagate
        });

        // Long Press to Lock
        holder.img.setOnLongClickListener(v -> {
            item.isLocked = !item.isLocked;
            notifyItemChanged(position);
            listener.onImageLocked(item);
            return true;
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, lockIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.inspirationImage);
            lockIcon = itemView.findViewById(R.id.lockIcon);
        }
    }
}


