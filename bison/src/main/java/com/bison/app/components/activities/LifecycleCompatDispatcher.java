package com.bison.app.components.activities;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by oeager on 2015/6/5.
 * email: oeager@foxmail.com
 */
public class LifecycleCompatDispatcher implements ActivityLifecycleCallbacksCompat {

    private final static LifecycleCompatDispatcher mDispatcher = new LifecycleCompatDispatcher();

    private final ArrayList<ActivityLifecycleCallbacksCompat> mActivityLifecycleCallbacksCompats = new ArrayList<>();

    private LifecycleCompatDispatcher(){}


    public void registerActivityLifecycle(ActivityLifecycleCallbacksCompat callbacksCompat){
        synchronized (mActivityLifecycleCallbacksCompats){
            mActivityLifecycleCallbacksCompats.add(callbacksCompat);
        }
    }

    public void unregisterActivityLifecycle(ActivityLifecycleCallbacksCompat callbacksCompat){
        synchronized (mActivityLifecycleCallbacksCompats){
            mActivityLifecycleCallbacksCompats.remove(callbacksCompat);
        }
    }

    public static LifecycleCompatDispatcher getDefault(){
        return mDispatcher;
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (mActivityLifecycleCallbacksCompats) {
            if (mActivityLifecycleCallbacksCompats.size() > 0) {
                callbacks = mActivityLifecycleCallbacksCompats.toArray();
            }
        }
        return callbacks;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivityCreated(activity,
                        savedInstanceState);
            }
        }
    }

    @Override
    public void onPostCreate(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onPostCreate(activity,
                        savedInstanceState);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivityStarted(activity);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivityResumed(activity);
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivityPaused(activity);
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivityStopped(activity);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivitySaveInstanceState(activity,
                        outState);
            }
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i=0; i<callbacks.length; i++) {
                ((ActivityLifecycleCallbacksCompat)callbacks[i]).onActivityDestroyed(activity);
            }
        }
    }
}
