package com.mikegacek.blackjack;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.google.android.gms.ads.MobileAds;
import com.mikegacek.blackjack.framework.Screen;
import com.mikegacek.blackjack.framework.impl.GLGame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Michael on 11/12/2015.
 */
public class Blackjack extends GLGame {
    boolean firstTimeCreate=true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Screen getStartScreen() {
        Assets.load(this);
        return new MainScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl,config);
        sharedPreferences = getSharedPreferences("com.blackjack", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3350697134197623~3373329190");
        Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);

        if(firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate=false;
        }
        else {
            Assets.reload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
