package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.math.Rectangle;

/**
 * Created by Michael on 9/27/2017.
 */

public class Slider extends Rectangle {

    private int width,height;
    private float xPos,yPos,alpha,barXStart,barXEnd;

    public Slider(int w, int h,float posX, float posY,float barXStart,float barXEnd) {
        super(barXStart-100,posY-h/2,barXEnd-barXStart+200,h);
        this.width=w;
        this.height=h;
        this.barXStart=barXStart;
        this.barXEnd=barXEnd;
        xPos=posX;
        yPos=posY;
        alpha=1;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBarXStart() {
        return barXStart;
    }

    public void setBarXStart(float barXStart) {
        this.barXStart = barXStart;
    }

    public float getBarXEnd() {
        return barXEnd;
    }

    public void setBarXEnd(float barXEnd) {
        this.barXEnd = barXEnd;
    }

    //start=200 x=800 end=1000
    public float getSliderPlacement() {
        return (xPos-barXStart)/(barXEnd-barXStart);
    }
}
