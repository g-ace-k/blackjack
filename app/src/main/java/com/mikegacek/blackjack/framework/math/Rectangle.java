package com.mikegacek.blackjack.framework.math;

import java.io.Serializable;

/**
 * Created by Michael on 10/27/2015.
 */
public class Rectangle  implements Serializable {
    public Vector2 lowerLeft;
    public float width,height;

    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2(x,y);
        this.width=width;
        this.height=height;
    }

    public void setRectPos(float x, float y) {
        this.lowerLeft = new Vector2(x,y);
    }

}
