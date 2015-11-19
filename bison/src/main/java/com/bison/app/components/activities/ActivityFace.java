package com.bison.app.components.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.StringRes;


/**
 * Created by oeager on 2015/5/28.
 * email: oeager@foxmail.com
 */
public interface ActivityFace {

    /**
     * @return true, if the context is recovering from in interruption (i.e.
     *         onRestoreInstanceState was called.
     */
    public boolean isRestoring();

    /**
     * @return true, if the context is "soft-resuming", i.e. onResume has been
     *         called without a prior call to onCreate
     */
    public boolean isResuming();

    /**
     * @return true, if the context is launching, i.e. is going through
     *         onCreate but is not restoring.
     */
    public boolean isLaunching();

    /**
     * Android doesn't distinguish between your Activity being paused by another
     * Activity of your own application, or by an Activity of an entirely
     * different application. This function only returns true, if your Activity
     * is being paused by an Activity of another app, thus hiding yours.
     *
     * @return true, if the Activity is being paused because an Activity of
     *         another application received focus.
     */
    public boolean isActivityFont();

    /**
     * Retrieves the current intent that was used to create or resume this
     * context. If the context received a call to onNewIntent (e.g. because it
     * was launched in singleTop mode), then the Intent passed to that method is
     * returned. Otherwise the returned Intent is the intent returned by
     * getIntent (which is the Intent which was used to initially launch this
     * context).
     *
     * @return the current {@link Intent}
     */
    public Intent getCurrentIntent();

    public boolean isLandscapeMode();

    public void showToast(String msg);

    public void showToast(@StringRes int id);

    public void showProgressDialog(@StringRes int id,boolean cancelable);

    public void showProgressDialog(String msg,boolean cancelable);

    public void dismissProgressDialog();

    void initTitleBar();

    void initComponents();

    void registerComponentListeners();

    void supportSwipeBack(int position, boolean isCaptureFullScreen);

}
