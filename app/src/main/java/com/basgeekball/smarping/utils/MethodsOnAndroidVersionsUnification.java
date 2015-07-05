package com.basgeekball.smarping.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.ViewPropertyAnimator;

public class MethodsOnAndroidVersionsUnification {

    public static void setEndActionAfterAnimation(ViewPropertyAnimator viewPropertyAnimator, final SimpleCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    callback.execute();
                }
            });
        } else {
            withEndAction(viewPropertyAnimator, callback);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void withEndAction(ViewPropertyAnimator viewPropertyAnimator, final SimpleCallback callback) {
        viewPropertyAnimator.withEndAction(new Runnable() {
            @Override
            public void run() {
                callback.execute();
            }
        });
    }

}