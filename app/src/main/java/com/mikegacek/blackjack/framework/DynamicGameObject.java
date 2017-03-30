package com.mikegacek.blackjack.framework;

import com.mikegacek.blackjack.framework.math.Vector2;

/**
 * Created by Michael on 10/27/2015.
 */
public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 accel;

    public DynamicGameObject(float x, float y, float width, float height) {
        super(x,y,width,height);
        velocity= new Vector2();
        accel = new Vector2();
    }
}
