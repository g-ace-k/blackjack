package com.mikegacek.blackjack.framework.math;

import java.io.Serializable;

/**
 * Created by Michael on 7/21/2015.
 */
public class Vector2  implements Serializable {
    public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI;
    public static float TO_DEGREES = (1/ (float) Math.PI)*180;
    public float x,y;

    public Vector2(){
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2 cpy() {
        return new Vector2(x,y);
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 sub(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 mult(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2 nor() {
        float len = len();
        if(len!=0) {
            this.x /=len;
            this.y /=len;
        }
        return this;
    }

    public float angle() {
        float angle = (float) Math.atan2(y, x) * TO_DEGREES;
        if(angle<0)
            angle +=360;
        return angle;
    }

    public Vector2 rotate(float angle) {
        float rad = angle*TO_RADIANS;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;

        return this;
    }

    public float dist(Vector2 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return (float)Math.sqrt(distX * distX + distY * distY);
    }

    public float dist(float x, float y) {
        float distX = this.x - x;
        float distY = this.y - y;
        return (float) Math.sqrt(distX * distX + distY * distY);
    }

    public boolean overlapCircles(Circle c1, Circle c2) {
        float distance = c1.center.distSquared(c2.center);
        float radiusSum=c1.radius+c2.radius;
        return distance <=radiusSum*radiusSum;
    }

    public float distSquared(Vector2 other) {
        float distX = this.x - other.x;
        float distY = this.y-other.y;
        return distX*distX+distY*distY;
    }

    public float distSquared(float x, float y) {
        float distX=this.x-x;
        float distY=this.y-y;
        return distX*distX+distY*distY;
    }

    public boolean overlapRectangles(Rectangle r1, Rectangle r2) {
        if(r1.lowerLeft.x<r2.lowerLeft.x + r2.width &&
           r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&
           r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&
           r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
            return true;
        else
            return false;
    }

    public boolean overlapCircleRectangle(Circle c, Rectangle r) {
        float closestX=c.center.x;
        float closestY=c.center.y;

        if(c.center.x < r.lowerLeft.x) {
            closestX=r.lowerLeft.x;
        }
        else if(c.center.x > r.lowerLeft.x+r.width) {
            closestX=r.lowerLeft.x + r.width;
        }

        if(c.center.y<r.lowerLeft.y) {
            closestY = r.lowerLeft.y;
        }
        else if(c.center.y>r.lowerLeft.y + r.height) {
            closestY = r.lowerLeft.y + r.height;
        }

        return c.center.distSquared(closestX,closestY) < c.radius*c.radius;
    }

}
