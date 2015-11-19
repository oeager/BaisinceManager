package com.bison.app.components.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by oeager on 2015/6/5.
 * email: oeager@foxmail.com
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifecycleCallbacksWrapper implements Application.ActivityLifecycleCallbacks {

    private final ActivityLifecycleCallbacksCompat callbacks;
    public ActivityLifecycleCallbacksWrapper(ActivityLifecycleCallbacksCompat callback){
        this.callbacks = callback;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        callbacks.onActivityCreated(activity,savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        callbacks.onActivityStarted(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        callbacks.onActivityResumed(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        callbacks.onActivityPaused(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        callbacks.onActivityStopped(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        callbacks.onActivitySaveInstanceState(activity, outState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        callbacks.onActivityDestroyed(activity);
    }

    @Override
    public boolean equals(Object object) {
        if( !(object instanceof ActivityLifecycleCallbacksWrapper) ) {
            return false;
        }
        final ActivityLifecycleCallbacksWrapper that = (ActivityLifecycleCallbacksWrapper) object;
        return null == callbacks ? null == that.callbacks : callbacks.equals( that.callbacks );
    }

    /**
     *
     * return wrapped callback object hashCode
     */
    @Override
    public int hashCode() {
        return null != callbacks ? callbacks.hashCode() : 0;
    }
}
