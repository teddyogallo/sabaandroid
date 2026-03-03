package com.sabapp.saba.events;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sabapp.saba.R;

public class TimelineItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint linePaint;
    private final int lineOffset; // Distance from left edge
    private final int lineWidth;

    public TimelineItemDecoration(int color, int width, int offsetDp) {
        linePaint = new Paint();
        linePaint.setColor(color);
        linePaint.setStrokeWidth(width);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        this.lineWidth = width;
        this.lineOffset = offsetDp;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            // Get the center of the vertical line based on the 'timeNode' view position
            float centerX = child.getLeft() + lineOffset;

            float startY = child.getTop();
            float stopY = child.getBottom();

            // Special handling for the first and last items to prevent line overflow
            int position = parent.getChildAdapterPosition(child);
            if (position == 0) {
                // Start the line at the middle of the first node
                View node = child.findViewById(R.id.timeNode);
                if (node != null) startY = child.getTop() + node.getTop() + (node.getHeight() / 2f);
            }

            if (position == state.getItemCount() - 1) {
                // End the line at the middle of the last node
                View node = child.findViewById(R.id.timeNode);
                if (node != null) stopY = child.getTop() + node.getTop() + (node.getHeight() / 2f);
            }

            c.drawLine(centerX, startY, centerX, stopY, linePaint);
        }
    }
}

