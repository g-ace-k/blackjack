package com.mikegacek.blackjack.framework;

import com.mikegacek.blackjack.framework.Graphics.PixmapFormat;

/**
 * Created by laptop on 2/12/2015.
 */
public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
