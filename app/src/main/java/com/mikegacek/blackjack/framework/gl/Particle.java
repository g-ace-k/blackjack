package com.mikegacek.blackjack.framework.gl;

import java.io.Serializable;

/**
 * Created by Michael on 2/6/2017.
 */

// NEEDS WORK

public class Particle implements Serializable{

    float r,g,b,a;
    transient TextureRegion texture;
    float xPos,yPos,width,height,angle;

    public Particle(TextureRegion textureRegion, float xPos, float yPos, float width, float height,float r, float g, float b, float a, float angle) {
        texture=textureRegion;
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
        this.xPos=xPos;
        this.yPos=yPos;
        this.width=width;
        this.height=height;
        this.angle=angle;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getAngle() { return angle;}

    public TextureRegion getTextureRegion() {
        return texture;
    }

    public void setTextureRegion(TextureRegion t) { texture=t;}
}
