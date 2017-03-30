package com.mikegacek.blackjack.framework.impl;

import android.graphics.Bitmap;

import com.mikegacek.blackjack.framework.Graphics.PixmapFormat;
import com.mikegacek.blackjack.framework.Pixmap;

/**
 * Created by laptop on 3/4/2015.
 */
public class AndroidPixmap implements Pixmap{
    Bitmap bitmap;
    PixmapFormat format;

    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        this.bitmap=bitmap;
        this.format=format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
