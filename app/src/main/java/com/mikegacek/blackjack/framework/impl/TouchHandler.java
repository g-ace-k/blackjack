package com.mikegacek.blackjack.framework.impl;

import com.mikegacek.blackjack.framework.Input.TouchEvent;

import java.util.List;

/**
 * Created by laptop on 2/25/2015.
 */
public interface TouchHandler {
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();
}

