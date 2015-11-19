package com.bison.app.components;

import android.app.Activity;
import android.app.Application;
import android.os.Build;

import com.bison.app.components.activities.ActivityLifecycleCallbacksCompat;
import com.bison.app.components.activities.ActivityLifecycleCallbacksWrapper;
import com.bison.app.components.activities.LifecycleCompatDispatcher;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by oeager on 2015/6/5.
 * email: oeager@foxmail.com
 */
public abstract class BaseApp extends Application {


    private static BaseApp INSTANCE;

    private final Stack<Activity> activityStack = new Stack<>();

    public BaseApp(){
        INSTANCE = this;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {

        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.peek();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.pop();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if(activity != null ) {
            if(activityStack.contains(activity)){
                this.activityStack.remove(activity);
            }
            if(!activity.isFinishing()){
                activity.finish();
            }

        }
    }

    public void popActivity(Activity activity){
        if(activity != null ) {
            if(activityStack.contains(activity)){
                this.activityStack.remove(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<? extends Activity> cls) {
        Iterator<Activity> iterator =activityStack.iterator();
        while (iterator.hasNext()){
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while(!activityStack.isEmpty()){
            Activity activity = activityStack.pop();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<? extends Activity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            onAppExit();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    protected abstract void onAppExit();

    public static <App extends BaseApp> App getApp() {
        App app = (App) INSTANCE;
        return app;
    }

    public static void joinActivity(Activity activity){

        if(activity.getApplication() instanceof BaseApp){
            BaseApp.getApp().addActivity(activity);
        }
    }

    public static void quitActivity(Activity activity){
        if(activity.getApplication() instanceof BaseApp){
            BaseApp.getApp().popActivity(activity);
        }
    }

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksWrapper(callback));
        } else {
            LifecycleCompatDispatcher.getDefault().registerActivityLifecycle(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            unregisterActivityLifecycleCallbacks(new ActivityLifecycleCallbacksWrapper(callback));
        } else {
            LifecycleCompatDispatcher.getDefault().unregisterActivityLifecycle(callback);
        }
    }
}
