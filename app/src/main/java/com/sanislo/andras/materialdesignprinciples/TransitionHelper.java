package com.sanislo.andras.materialdesignprinciples;

import android.content.Context;
import android.graphics.Path;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.PathMotion;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.animation.AnimationUtils;

/**
 * Created by root on 08.02.17.
 */

public class TransitionHelper {

    public static Transition getDetailActivityEnterTransition(Context context) {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimationUtils.loadInterpolator(context,
                android.R.interpolator.linear_out_slow_in));
        slide.setDuration(context.getResources().getInteger(android.R.integer.config_longAnimTime));
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        slide.excludeTarget(R.id.tv_description_title, true);
        slide.excludeTarget(R.id.rl_detail_comments, true);
        return slide;
    }

    /*public static Transition getDetailActivityEnterSharedElementTransition(Context context) {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeImageTransform());
        transitionSet.addTransition(new ChangeClipBounds());
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTarget(R.id.iv_photo);
        Transition arcMotionTransition = TransitionInflater.from(context).inflateTransition(R.transition.transition_arc_motion);
        transitionSet.addTransition(arcMotionTransition);
        return transitionSet;
    }*/

    /*public static Transition getDetailActivityReturnTransition() {
        Fade fade = new Fade();
        fade.addTarget(R.id.iv_photo);
        fade.addTarget(R.id.tv_description_title);
        fade.addTarget(R.id.tv_description_text);
        return fade;
    }*/

    /*public static Transition getDetailActivityReenterTransition() {
        Fade fade = new Fade();
        fade.addTarget(R.id.iv_photo);
        fade.addTarget(R.id.tv_description_title);
        fade.addTarget(R.id.tv_description_text);
        return fade;
    }*/

    public static Transition getMainActivityExitTransition(Context context) {
        Explode explode = new Explode();
        explode.excludeTarget(R.id.toolbar, true);
        explode.excludeTarget(android.R.id.statusBarBackground, true);
        explode.excludeTarget(android.R.id.navigationBarBackground, true);
        explode.setInterpolator(AnimationUtils.loadInterpolator(context,
                android.R.interpolator.linear_out_slow_in));
        explode.setDuration(2000);
        return explode;
    }

    public static Transition getMainActivityReenterTransition(Context context) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.TOP);
        slide.excludeTarget(R.id.toolbar, true);
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        slide.setInterpolator(AnimationUtils.loadInterpolator(context,
                android.R.interpolator.linear_out_slow_in));
        slide.setDuration(2000);
        return slide;
    }
}
