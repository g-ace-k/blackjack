package com.mikegacek.blackjack.framework;

import com.mikegacek.blackjack.framework.math.Rectangle;
import com.mikegacek.blackjack.framework.math.Vector2;

/**
 * Created by Michael on 10/27/2015.
 */
public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;

    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x-width/2,y-height/2,width,height);
    }
}
