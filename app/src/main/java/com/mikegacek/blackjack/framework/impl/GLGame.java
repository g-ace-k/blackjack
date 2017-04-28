package com.mikegacek.blackjack.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.mikegacek.blackjack.framework.Audio;
import com.mikegacek.blackjack.framework.FileIO;
import com.mikegacek.blackjack.framework.Game;
import com.mikegacek.blackjack.framework.Graphics;
import com.mikegacek.blackjack.framework.Input;
import com.mikegacek.blackjack.framework.Screen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by laptop on 3/20/2015.
 */
public abstract class GLGame extends Activity implements Game, Renderer {
    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }

    GLSurfaceView glView;
    GLGraphics glGraphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    GLGameState state = GLGameState.Initialized;
    Object stateChanged = new Object();
    long startTime = System.nanoTime();
    WakeLock wakeLock;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        glView= new GLSurfaceView(this);
        glView.setEGLConfigChooser(8,8,8,8,16,0);
        glView.setRenderer(this);
        setContentView(glView);


        glGraphics = new GLGraphics(glView);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this,glView,1,1);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock=powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"GLGame");

        sharedPreferences = this.getBaseContext().getSharedPreferences("com.blackjack",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
        wakeLock.acquire();
    }

    @Override
    public void onBackPressed() {
        //If a disabled back button is pressed
        //0=false 1=true 2=changing
        if(sharedPreferences.getInt("disableBack",0)==1) {
            editor.putInt("disableBack",2);
            editor.commit();
        }
        else {
            super.onBackPressed();
        }
    }



    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glGraphics.setGL(gl);

        synchronized (stateChanged) {
            if(state== GLGameState.Initialized)
                screen=getStartScreen();
            state= GLGameState.Running;
            screen.resume();
            startTime= System.nanoTime();
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    public void onDrawFrame(GL10 gl) {
        GLGameState state = null;

        synchronized (stateChanged) {
            state = this.state;
        }

        if(state == GLGameState.Running) {
            float deltaTime = (System.nanoTime()-startTime)/1000000000.0f;
            startTime= System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        }

        if(state == GLGameState.Paused) {
            screen.pause();
            synchronized (stateChanged) {
                this.state= GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }

        if(state== GLGameState.Finished) {
            screen.pause();
            screen.dispose();
            synchronized (stateChanged) {
                this.state= GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }
    }

    @Override
    public void onPause() {
        synchronized (stateChanged) {
            if(isFinishing())
                state= GLGameState.Finished;
            else
                state= GLGameState.Paused;
            while(true) {
                try {
                    stateChanged.wait();
                    break;
                } catch(InterruptedException e) {
                }
            }
        }
        wakeLock.release();
        glView.onPause();
        super.onPause();
    }

    public GLGraphics getGlGraphics() {
        return glGraphics;
    }

    public Input getInput() {
       return input;
    }

    public FileIO getFileIO() {
        return fileIO;
    }

    public Graphics getGraphics() {
        throw new IllegalStateException("We are using OpenGL");
    }

    public Audio getAudio() {
        return audio;
    }

    public void setScreen(Screen newScreen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        newScreen.resume();
        newScreen.update(0);
        this.screen = newScreen;
    }

    public Screen getCurrentScreen() {
        return screen;
    }
}
