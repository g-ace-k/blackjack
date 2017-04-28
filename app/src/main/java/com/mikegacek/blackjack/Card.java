package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.TextureRegion;

import java.io.Serializable;

/**
 * Created by Michael on 12/13/2016.
 */
// 1  2  3  4  5  6  7  8  9  10 11 12 13
// 14 15 16 17 18 19 20 21 22 23 24 25 26
// 27 28 29 30 31 32 33 34 35 36 37 38 39
// 40 41 42 43 44 45 46 47 48 49 50 51 52

public class Card implements Serializable {

    private int value,suit;
    private float oldX,oldY,currentX,currentY,newX,newY;
    private transient TextureRegion texture;
    private float rotation;
    private float oldScaleX,oldScaleY,scaleX,scaleY,newScaleX,newScaleY;
    private boolean visable;
    private float speed;

    public Card(int value) {
        value=(int)(Math.random()*2)+13;
        this.suit=(value-1)/13;
        this.value=value%13;
        if(this.value==0)
            this.value=13;
        this.texture= new TextureRegion(Assets.cards,(this.value-1)*150,(this.suit)*210,150,210);
        this.rotation=0;
        this.oldScaleX=4f/3f;
        this.oldScaleY=4f/3f;
        this.scaleX=4f/3f;
        this.scaleY=4f/3f;
        this.visable=true;
        this.currentX=1280;
        this.currentY=2120;
        this.oldX=1280;
        this.oldY=2120;
        this.newX=1280;
        this.newY=2120;
        this.newScaleX=this.scaleX;
        this.newScaleY=this.scaleY;
        this.speed=.4f;
    }

    public void invertVisable() {
        visable=!visable;
    }

    public void setVisable(boolean visable) {
        this.visable=visable;
    }

    public void setRotation(float rotation) {
        this.rotation=rotation;
    }

    public void setScaleX(float scaleX) {
        this.scaleX=scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY=scaleY;
    }

    public void setScale(float scaleX,float scaleY) {
        this.scaleX=scaleX;
        this.scaleY=scaleY;
    }

    public void setCurrentPos(int x, int y) {
        currentX=x;
        currentY=y;
    }

    public void setNewPos(int x, int y) {
        if(currentX!=newX) {
            oldX=currentX;
        }
        if(currentY!=newY) {
            oldY=currentY;
        }
        newX=x;
        newY=y;
    }

    public void setNewScaleX(float x) {
        if(scaleX!=newScaleX)
            oldScaleX=scaleX;
        if(scaleY!=newScaleY)
            oldScaleY=scaleY;
        newScaleX=x;
    }

    public void setNewScaleY(float y) {
        newScaleY=y;
    }

    public void setSpeed(float s) {
        speed=s;
    }

    public float getSpeed() {
        return speed;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getRotation() {
        return rotation;
    }

    public TextureRegion getTexture() {
        if(visable)
            return texture;
        else
            return Assets.back;
    }

    public void loadTexture() {
        this.texture= new TextureRegion(Assets.cards,(this.value-1)*150,(this.suit)*210,150,210);
    }

    public boolean getVisable() {
        return visable;
    }

    public int getValue() {
        return value;
    }

    public int getSuit() {return suit;}

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public float getNewX() {
        return newX;
    }

    public float getNewY() {
        return newY;
    }

    public void move(float rate) {
        float tempX=currentX,tempY=currentY;
        if(!(currentX==newX && currentY==newY)) {
            double distance = distance(currentX - newX, currentY - newY);
                currentX -= rate*Math.sqrt(distance*10) * (currentX - newX) /  distance;
                currentY -= rate*Math.sqrt(distance*10) * (currentY - newY) /  distance;
            if((tempX-newX<=0 && currentX-newX>=0) || (tempX-newX>=0 && currentX-newX<=0))
                currentX=newX;
            if((tempY-newY<=0 && currentY-newY>=0) || (tempY-newY>=0 && currentY-newY<=0))
                currentY=newY;
        }
        if(oldX!=currentX && oldY!=currentY && currentX==newX && currentY==newY) {
            oldX=currentX;
            oldY=currentY;
        }
    }

    public void moveAndScale(float rate) {
        float tempX=currentX,tempY=currentY;
        if(!(currentX==newX && currentY==newY)) {
            double distance = distance(currentX - newX, currentY - newY);
            currentX -= rate*Math.sqrt(distance*10) * (currentX - newX) /  distance;
            currentY -= rate*Math.sqrt(distance*10) * (currentY - newY) /  distance;
            if((tempX-newX<=0 && currentX-newX>=0) || (tempX-newX>=0 && currentX-newX<=0)) {
                currentX = newX;
                oldX=currentX;
            }
            if((tempY-newY<=0 && currentY-newY>=0) || (tempY-newY>=0 && currentY-newY<=0)) {
                currentY = newY;
                oldY=currentY;
            }
        }
        else if(currentX==newX && currentY==newY) {
            oldX=currentX;
            oldY=currentY;
        }
        adjustScale();

    }

    public void adjustScale() {
        float tempX=scaleX,tempY=scaleY;
        if(!(scaleX==newScaleX && scaleY==newScaleY)) {
            float dScaleX,dScaleY,dX,dY;
            dScaleX=newScaleX-oldScaleX;
            dScaleY=newScaleY-oldScaleY;

            if(oldX!=newX) {
                dX = Math.abs(currentX - oldX) / Math.abs(newX - oldX);
                scaleX = dScaleX * dX + oldScaleX;
            }
            else
                scaleX=newScaleX;
            if(oldY!=newY) {
                dY = Math.abs(currentY - oldY) / Math.abs(newY - oldY);
                scaleY=dScaleY*dY+oldScaleY;
            }
            else
                scaleY=newScaleY;

            if((tempX-newScaleX<=0 && scaleX-newScaleX>=0) || (tempX-newScaleX>=0 && scaleX-newScaleX<=0)) {
                scaleX = newScaleX;
                oldScaleX=scaleX;
            }
            if((tempY-newScaleY<=0 && scaleY-newScaleY>=0) || (tempY-newScaleY>=0 && scaleY-newScaleY<=0)) {
                scaleY = newScaleY;
                oldScaleY=scaleY;
            }
        }
        else if(scaleX==newScaleX && scaleY==newScaleY) {
            oldScaleX=scaleX;
            oldScaleY=scaleY;
        }

    }


    public void adjustScale(float rate) {
        float tempX=scaleX,tempY=scaleY;
        if(!(scaleX==newScaleX && scaleY==newScaleY)) {
            if(scaleX<newScaleX)
                scaleX+=rate;
            else
                scaleX-=rate;
            if(scaleY<newScaleY)
                scaleY+=rate;
            else
                scaleY-=rate;

            if((tempX-newScaleX<=0 && scaleX-newScaleX>=0) || (tempX-newScaleX>=0 && scaleX-newScaleX<=0))
                scaleX=newScaleX;
            if((tempY-newScaleY<=0 && scaleY-newScaleY>=0) || (tempY-newScaleY>=0 && scaleY-newScaleY<=0))
                scaleY=newScaleY;
        }
        if(oldScaleX!=scaleX && oldScaleY!=scaleY && scaleX==newScaleX && scaleY==newScaleY) {
            oldScaleX=scaleX;
            oldScaleY=scaleY;
        }
    }
    //returns whether it is done flipping or not
    public boolean flipCard() {
        if(visable==false) {
            scaleX-=.1f;
            if(scaleX<=0){
                scaleX=0;
                visable=true;
            }
        }
        else {
            scaleX+=.1f;
            if(scaleX>=4f/3f) {
                scaleX = 4f/3f;
                return true;
            }
        }
        return false;
    }

    private double distance(float x,float y) {
        return Math.sqrt(x*x+y*y);
    }

    public void reset() {
        rotation=0;
        oldScaleX=4f/3f;
        oldScaleY=4f/3f;
        scaleX=1;
        scaleY=1;
        visable=true;
        oldX=1280;
        oldY=2120;
        currentX=1280;
        currentY=2120;
        newX=1280;
        newY=2120;
        newScaleX=1;
        newScaleY=1;
    }

}
