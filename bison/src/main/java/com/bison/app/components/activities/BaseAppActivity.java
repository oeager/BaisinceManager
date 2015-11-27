package com.bison.app.components.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bison.app.components.BaseApp;
import com.bison.app.components.fragments.ProgressDialogFragment;
import com.bison.app.ui.swipeback.SwipeConfiguration;
import com.bison.app.ui.swipeback.SwipePanelLayout;


public abstract class BaseAppActivity extends AppCompatActivity implements ActivityFace {

    private Toast toast = null;

    private boolean wasCreated, wasInterrupted;

    private boolean isFocus;

    private Intent currentIntent;

    private DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.wasCreated = true;
        this.currentIntent = getIntent();
        BaseApp.joinActivity(this);
        LifecycleCompatDispatcher.getDefault().onActivityCreated(this, savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LifecycleCompatDispatcher.getDefault().onPostCreate(this, savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initTitleBar();
        initComponents();
        registerComponentListeners();
    }



    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initTitleBar();
        initComponents();
        registerComponentListeners();
    }



    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initTitleBar();
        initComponents();
        registerComponentListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LifecycleCompatDispatcher.getDefault().onActivityStarted(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFocus = true;
        LifecycleCompatDispatcher.getDefault().onActivityResumed(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isFocus = false;
        wasCreated = wasInterrupted = false;
        LifecycleCompatDispatcher.getDefault().onActivityPaused(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LifecycleCompatDispatcher.getDefault().onActivityStopped(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApp.quitActivity(this);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        LifecycleCompatDispatcher.getDefault().onActivityDestroyed(this);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LifecycleCompatDispatcher.getDefault().onActivitySaveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wasInterrupted = true;
    }

    @Override
    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();

    }

    @Override
    public void showToast(@StringRes int resId) {
        if (toast == null) {
            toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
        toast.show();
    }


    @Override
    public void showProgressDialog(String msg,boolean cancelable) {
        dialogFragment =ProgressDialogFragment.newInstance(msg);
        dialogFragment.setCancelable(cancelable);
        ((ProgressDialogFragment)dialogFragment).show(this);
    }


    @Override
    public void showProgressDialog(@StringRes int id,boolean cancelable) {
        showProgressDialog(getString(id),cancelable);
    }

    @Override
    public void dismissProgressDialog() {
        if(dialogFragment!=null){
            dialogFragment.dismiss();
            dialogFragment=null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.currentIntent = intent;
    }

    @Override
    public void supportSwipeBack(@SwipeConfiguration.SwipePosition int position,boolean isCaptureFullScreen) {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SwipeConfiguration configuration = new SwipeConfiguration.Builder()
                .position(position)
                .edgeSize(isCaptureFullScreen?1f:0.18f)
                .build();
        SwipePanelLayout.attachToActivity(this, configuration);
    }

    @Override
    public boolean isRestoring() {
        return wasInterrupted;
    }

    @Override
    public boolean isResuming() {
        return !wasCreated;
    }

    @Override
    public boolean isLaunching() {
        return !wasInterrupted && wasCreated;
    }

    @Override
    public boolean isActivityFont() {
        return isFocus;
    }

    @Override
    public Intent getCurrentIntent() {
        return currentIntent;
    }

    @Override
    public boolean isLandscapeMode() {
        return getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90;
    }


}
