package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.math.Rectangle;

/**
 * Created by Michael on 12/15/2016.
 */
public class Button extends Rectangle{

    private int width,height,rotation;
    private boolean on;
    private float xPos,yPos,newX,newY,alpha,scaleX,scaleY,newScaleX,newScaleY,red,green,blue;
    private transient TextureRegion textureRegion;
    private boolean pressed;

    public Button(int w, int h,float posX, float posY) {
        super(posX-w/2,posY-h/2,w,h);
        this.width=w;
        this.height=h;
        xPos=posX;
        yPos=posY;
        newX=xPos;
        newY=yPos;
        red=.25f;
        green=.25f;
        blue=.25f;
        alpha=1;
        on=false;
        scaleX=1;
        scaleY=1;
        newScaleX=1;
        newScaleY=1;
        rotation=0;
        pressed=false;
    }

    public Button(int w, int h,float posX, float posY,TextureRegion tr) {
        super(posX-w/2,posY-h/2,w,h);
        this.width=w;
        this.height=h;
        xPos=posX;
        yPos=posY;
        newX=xPos;
        newY=yPos;
        red=.25f;
        green=.25f;
        blue=.25f;
        alpha=1;
        on=false;
        scaleX=1;
        scaleY=1;
        newScaleX=1;
        newScaleY=1;
        textureRegion=tr;
        pressed=false;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public float getNewX() { return newX;}

    public float getNewY() { return newY;}

    public int getRedi() {return (int) (red*255);}

    public float getRedf() {return red;}

    public int getGreeni() {return (int) (green*255);}

    public float getGreenf() {return green;}

    public int getBluei() {return (int) (blue*255);}

    public float getBluef() {return blue;}

    public float getAlpha() {
        return alpha;
    }

    public int getRotation() {return rotation;}

    public TextureRegion getTextureRegion() { return textureRegion;}

    public void loadTexture(int id) {
        switch(id) {
            case 1:
                textureRegion=Assets.settingsButton;
                break;
            case 2:
                textureRegion=Assets.rulesButton;
                break;
            case 3:
                textureRegion=Assets.freeChipsButton;
                break;
            case 4:
                textureRegion=Assets.soundOnButton;
                break;
        }
    }

    public boolean getOn() { return on;}

    public void setPos(float x, float y) {
        this.setRectPos(x-this.width/2,y-this.height/2);
        xPos=x;
        yPos=y;
    }

    public void setNewPos(float x, float y) {
        newX=x;
        newY=y;
    }

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

    public void setAlpha(float a) {
        alpha=a;
    }

    public void setOn(boolean b) {
        on=b;
    }

    public void setTextureRegion(TextureRegion tr) {
        textureRegion=tr;
    }

    public float getNewScaleY() {
        return newScaleY;
    }

    public void setNewScaleY(float newScaleY) {
        this.newScaleY = newScaleY;
    }

    public float getNewScaleX() {
        return newScaleX;
    }

    public boolean getPressed() {
        return pressed;
    }

    public void setNewScaleX(float newScaleX) {
        this.newScaleX = newScaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setPressed(boolean b) {pressed=b;}

    public void setRotation(int r) { rotation=r;}

    public void move(float rate) {
        float tempX=xPos,tempY=yPos;
        if(xPos!=newX || yPos!=newY) {
            double distance = distance(xPos - newX, yPos - newY);
            xPos -= rate*Math.sqrt(distance*10) *(xPos - newX) /  distance;
            yPos -= rate*Math.sqrt(distance*10) *(yPos - newY) /  distance;
            if((tempX-newX<=0 && xPos-newX>=0) || (tempX-newX>=0 && xPos-newX<=0))
                xPos=newX;
            if((tempY-newY<=0 && yPos-newY>=0) || (tempY-newY>=0 && yPos-newY<=0))
                yPos=newY;
        }
        this.setPos(xPos,yPos);
    }

    private double distance(float x,float y) {
        return Math.sqrt(x*x+y*y);
    }
}
