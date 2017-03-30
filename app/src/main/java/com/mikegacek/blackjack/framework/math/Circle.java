package com.mikegacek.blackjack.framework.math;

import java.io.Serializable;

/**
 * Created by Michael on 10/27/2015.
 */
public class Circle  implements Serializable {
    public Vector2 center = new Vector2();
    public float radius;

    public Circle(float x,float y, float radius) {
        this.center.set(x,y);
        this.radius = radius;
    }

    public void setCirclePos(float x, float y) {
        this.center.set(x,y);
    }
}
