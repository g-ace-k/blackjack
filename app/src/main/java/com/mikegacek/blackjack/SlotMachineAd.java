package com.mikegacek.blackjack;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.chartboost.sdk.Model.CBError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.ChartboostDelegate;

/**
 * Created by Michael on 2/10/2017.
 */

public class SlotMachineAd extends Activity {
    InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    boolean adFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.blackjack",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        editor.putBoolean("adFinished",false);
        editor.commit();

        Chartboost.startWithAppId(this, "59d56248d7b73c0c133bcb4f", "faf369fa7e9d07eb69a23b10ba44c430c41d8700");
        Chartboost.setDelegate(chartBoostDelegate);
        Chartboost.onCreate(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
        Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return true;
    }

    private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() {


        public void didCloseInterstitial(String location) {
            closeAd();
        }

        public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
            admobAd();
        }

    };

    @Override
    public void onBackPressed() {

        if(sharedPreferences.getBoolean("adFinished",true)==true)
            this.finish();

        if (Chartboost.onBackPressed())
            return;
        else
            super.onBackPressed();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void closeAd() {
        editor.putBoolean("adFinished",true);
        editor.commit();
        this.finish();
    }

    private void admobAd() {

        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build();
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("EC0BF4F45703476285EA00FD292318F8")


        //AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);


        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            public void onAdFailedToLoad(int e) {
                closeAd();
            }

            public void onAdClosed() {
                closeAd();
            }
        });
    }
}
