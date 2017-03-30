package com.mikegacek.blackjack.framework.gl;

import java.io.Serializable;

/**
 * Created by Michael on 7/15/2015.
 */
public class TextureRegion  implements Serializable {
    public float u1, v1;
    public float u2, v2;
    public float width,height;
    public final Texture texture;

    public TextureRegion(Texture texture, float x, float y, float width, float height) {
        this.width=width;
        this.height=height;
        this.u1 = x/ texture.width;
        this.v1 = y/texture.height;
        this.u2 = this.u1 + width/texture.width;
        this.v2 = this.v1 + height/texture.height;
        this.texture = texture;
    }

    public void changeValues(float x,float y,float width,float height) {
        this.width=width;
        this.height=height;
        this.u1 = x/ texture.width;
        this.v1 = y/texture.height;
        this.u2 = this.u1 + width/texture.width;
        this.v2 = this.v1 + height/texture.height;
    }

}
