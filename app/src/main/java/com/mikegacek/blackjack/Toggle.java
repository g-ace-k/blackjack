package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.math.Rectangle;

/**
 * Created by Michael on 9/27/2017.
 */

public class Toggle extends Rectangle {

    private int width,height;
    private float xPos,yPos,circleXPos,circleYPos,red,green,blue,alpha;
    private boolean on;

    public Toggle(int w, int h,float posX, float posY) {
        super(posX-w/2,posY-h/2,w,h);
        this.width=w;
        this.height=h;
        xPos=posX;
        yPos=posY;
        circleXPos=xPos-33;
        circleYPos=yPos;
        red=.25f;
        green=.25f;
        blue=.25f;
        alpha=1;
        on=false;
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

    public float getCircleXPos() {
        return circleXPos;
    }

    public void setCircleXPos(float circleXPos) {
        this.circleXPos = circleXPos;
    }

    public float getCircleYPos() {
        return circleYPos;
    }

    public void setCircleYPos(float circleYPos) {
        this.circleYPos = circleYPos;
    }

    public int getRedi() {return (int) (red*255);}

    public float getRedf() {return red;}

    public int getGreeni() {return (int) (green*255);}

    public float getGreenf() {return green;}

    public int getBluei() {return (int) (blue*255);}

    public float getBluef() {return blue;}

    public void setRGBi(int r, int g, int b) {
        red=(r/255f);
        green = (g/255f);
        blue = (b/255f);
    }

    public void setRGBf(float r, float g, float b) {
        red=r;
        green=g;
        blue=b;
    };

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean getOn() {
        return on;
    }

    public void setOn(boolean b) {
        on = b;
    }
}
