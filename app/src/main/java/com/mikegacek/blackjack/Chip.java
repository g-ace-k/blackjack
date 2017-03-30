package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.TextureRegion;

import java.io.Serializable;

/**
 * Created by Michael on 12/26/2016.
 */
public class Chip  implements Serializable {

    private int value,textureValue,id;
    private boolean moneyChanged;
    private float currentX,currentY,newX,newY;
    private transient TextureRegion texture;
    private float scaleX,scaleY,newScaleX,newScaleY,rotation;

    public Chip (int id,float xPos, float yPos,float newXPos, float newYPos){
        this.id=id;
        currentX=xPos;
        currentY=yPos;
        newX=newXPos;
        newY=newYPos;
        scaleX=.5f;
        scaleY=.5f;
        newScaleX=.5f;
        newScaleY=.5f;
        rotation=0;
        moneyChanged=false;
        switch(id) {
            case 0:
                value=1;
                texture=Assets.chip1;
                break;
            case 1:
                value=5;
                texture=Assets.chip5;
                break;
            case 2:
                value=25;
                texture=Assets.chip25;
                break;
            case 3:
                value=100;
                texture=Assets.chip100;
                break;
            case 4:
                value=500;
                texture=Assets.chip500;
                break;
            case 5:
                value=1000;
                texture=Assets.chip1000;
                break;
            case 6:
                value=10000;
                texture=Assets.chip10000;
                break;
        }
        //---------- 180 210 240 270 300 330 360
    }

    public int getValue() {
        return value;
    }

    public int getId() { return id;}

    public void setValue(int value) {
        this.value = value;
    }

    public void setId(int id) { this.id=id;}

    public int getTextureValue() {
        return textureValue;
    }

    public void setTextureValue(int textureValue) {
        this.textureValue = textureValue;
    }

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    public float getNewX() {
        return newX;
    }

    public void setNewX(float newX) {
        this.newX = newX;
    }

    public float getNewY() {
        return newY;
    }

    public void setNewY(float newY) {
        this.newY = newY;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void loadTexture() {
        switch(id) {
            case 0:
                value=1;
                texture=Assets.chip1;
                break;
            case 1:
                value=5;
                texture=Assets.chip5;
                break;
            case 2:
                value=25;
                texture=Assets.chip25;
                break;
            case 3:
                value=100;
                texture=Assets.chip100;
                break;
            case 4:
                value=500;
                texture=Assets.chip500;
                break;
            case 5:
                value=1000;
                texture=Assets.chip1000;
                break;
            case 6:
                value=10000;
                texture=Assets.chip10000;
                break;
        }
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getNewScaleX() {
        return newScaleX;
    }

    public void setNewScaleX(float newScaleX) {
        this.newScaleX = newScaleX;
    }

    public float getNewScaleY() {
        return newScaleY;
    }

    public void setNewScaleY(float newScaleY) {
        this.newScaleY = newScaleY;
    }

    public void setNewPos(float x, float y) {
        newX=x;
        newY=y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean getMoneyChanged() { return moneyChanged;}

    public void setMoneyChanged(boolean b) { moneyChanged=b;}

    private double distance(float x,float y) {
        return Math.sqrt(x*x+y*y);
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
    }
}
