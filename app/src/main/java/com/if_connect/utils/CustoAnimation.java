package com.if_connect.utils;

import android.view.View;
import android.view.animation.Animation;

public class CustoAnimation {

    public static void startAndEndAnimation(View view, Animation start, Animation end) {
        view.startAnimation(start);
        start.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(end);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
