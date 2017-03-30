package com.mikegacek.blackjack.framework.impl;

import android.media.SoundPool;

import com.mikegacek.blackjack.framework.Sound;

/**
 * Created by laptop on 2/12/2015.
 */
public class AndroidSound implements Sound{
    int soundId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool,int soundId) {
        this.soundId=soundId;
        this.soundPool=soundPool;
    }
    @Override
    public void play(float volume) {
        soundPool.play(soundId,volume,volume,0,0,1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}
