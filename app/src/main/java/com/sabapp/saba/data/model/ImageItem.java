package com.sabapp.saba.data.model;

import android.graphics.Bitmap;

public class ImageItem {
    public int imageRes = -1;
    public Bitmap bitmap = null;
    public boolean isLocked = false;

    public ImageItem(int res) { this.imageRes = res; }
    public ImageItem(Bitmap bmp) { this.bitmap = bmp; }
}
