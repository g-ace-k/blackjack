package com.mikegacek.blackjack.framework.impl;


import android.opengl.GLSurfaceView;

import java.io.Serializable;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by laptop on 3/20/2015.
 */
public class GLGraphics implements Serializable {
    GLSurfaceView glView;
    GL10 gl;

    public GLGraphics(GLSurfaceView glView) {
        this.glView = glView;
    }

    public GL10 getGl() {
        return gl;
    }

    void setGL(GL10 gl) {
        this.gl = gl;
    }

    public int getWidth() {
        return glView.getWidth();
    }

    public int getHeight() {
        return glView.getHeight();
    }
}
