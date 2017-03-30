package com.mikegacek.blackjack.framework.gl;

/**
 * Created by Michael on 11/12/2015.
 */
public class Animation {
    public static final int ANIMATION_LOOPING =0;
    public static final int ANIMATION_NONLOOPING=1;

    final TextureRegion[] keyFrames;
    final float frameDuration;

    public Animation(float frameDuration, TextureRegion ... keyFrames) {
        this.frameDuration = frameDuration;
        this.keyFrames=keyFrames;
    }

    public TextureRegion getKeyFrame(float stateTime, int mode) {
        int frameNumber = (int)(stateTime/frameDuration);
        if(mode == ANIMATION_LOOPING) {
            frameNumber= Math.min(keyFrames.length - 1, frameNumber);
        } else {
            frameNumber=frameNumber%keyFrames.length;
        }
        return keyFrames[frameNumber];
    }
}
