package com.sureone;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class TransAlphaAnimation extends TranslateAnimation {
    private float mFromAlpha;
    private float mToAlpha;

    public TransAlphaAnimation(float fromXDelta, float toXDelta,
                               float fromYDelta, float toYDelta,float fromAlpha,float toAlpha) {
        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
        mFromAlpha=fromAlpha;
        mToAlpha=toAlpha;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void applyTransformation(float interpolatedTime,
                                       Transformation t) {

        super.applyTransformation(interpolatedTime,t);
        final float alpha = mFromAlpha;
        t.setAlpha(alpha + ((mToAlpha - alpha) * interpolatedTime));
    }



}
