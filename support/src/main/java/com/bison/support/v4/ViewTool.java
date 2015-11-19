package com.bison.support.v4;

import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public final class ViewTool {

    public static boolean equalIntTag(View v, int t) {
        Object o = v.getTag();
        if (o == null) {
            return false;
        }
        try {
            int value = (int) o;
            return value == t;
        } catch (Exception e) {
            return false;
        }

    }

    public static void hideView(View v) {
        if (v != null && v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.GONE);
        }
    }

    public static void showView(View v) {
        if (v != null && v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViewAnimated(View v) {
        if (v != null && v.getVisibility() == View.VISIBLE) {
            v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_out));
        }
    }

    public static void showViewAnimated(View v) {
        if (v != null && v.getVisibility() != View.VISIBLE) {
            v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in));
        }
    }

    public static void safeClearViewAnimation(View v) {
        if (v != null) {
            v.clearAnimation();
        }
    }

    public static boolean isViewVisible(View v){
        if(v==null){
            return false;
        }
        return v.getVisibility()==View.VISIBLE;
    }
}
    