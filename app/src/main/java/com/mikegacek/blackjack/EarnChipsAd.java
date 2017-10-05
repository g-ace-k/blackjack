package com.mikegacek.blackjack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by Michael on 10/4/2017.
 */

public class EarnChipsAd extends Activity implements RewardedVideoAdListener {

    private RewardedVideoAd mAd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        sharedPreferences = getSharedPreferences("com.blackjack",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        loadRewardedVideoAd();
        mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                if(mAd.isLoaded())
                    mAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                closeAd();
            }

            @Override
            public void onRewarded(RewardItem reward) {


                reward(reward.getAmount());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                closeAd();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                closeAd();
            }
        });
    }

    private void loadRewardedVideoAd() {
        mAd.loadAd("ca-app-pub-3350697134197623/3162711700", new AdRequest.Builder().build());
    }


    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        this.finish();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        this.finish();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        this.finish();
    }

    public void reward(int reward) {
        int money = sharedPreferences.getInt("com.blackjack.money",5000);
        money+=5000;
        editor.putBoolean("com.blackjack.reward",true);
        editor.commit();
        editor.putInt("com.blackjack.money",money);
        editor.commit();
    }

    public void closeAd() {
        this.finish();
    }
}
