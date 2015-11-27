package com.bison.crash.catcher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by oeager on 2015/11/17.
 * email:oeager@foxmail.com
 */
public final class CrashCatcher implements Thread.UncaughtExceptionHandler {

    public static final String INTENT_ERROR_HANDLE = "android.bison.action.APP_ERROR";

    final static String CRASH_DATA = "crash_data";

    final static String LOG_VIEW = "log_view";

    final static String REPORTER = "reporter";

    public final static String CONTENT = "content";

    private boolean forceReportMode = false;

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    private Class<? extends Activity> launcherCls;

    private Application mApp;

    private final Bundle bundle = new Bundle();

    public void init(Application application) {
        mApp = application;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void restoreDefaultSettings() {
        registerErrorHandleActivity(AppReportActivity.class);
        registerErrorDetailActivity(LogViewActivity.class);
        forceReportMode = false;
        String title = String.format(mApp.getString(R.string.default_report_title), getApplicationName(mApp));
        registerReportData(AppReportActivity.TITLE, title);
        registerReportData(AppReportActivity.DESCRIPTION, mApp.getString(R.string.default_report_description));
    }

    public void registerErrorHandleActivity(Class<? extends Activity> cls) {
        this.launcherCls = cls;
    }

    public void registerErrorDetailActivity(Class<? extends Activity> cls) {
        bundle.putSerializable(LOG_VIEW, cls);
    }

    public void registerReporter(Class<? extends ReportFunc.IReporter> cls) {
        bundle.putSerializable(REPORTER, cls);
    }

    public void registerReportData(String key, Object value) {
        if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else {
            bundle.putString(key, value.toString());
        }
    }

    public void registerReportData(String key, String[] arrayValue) {
        bundle.putStringArray(key, arrayValue);
    }

    public void registerReportData(String key, int[] arrayValue) {
        bundle.putIntArray(key, arrayValue);
    }

    public void registerReportData(String key, Bundle allValue) {
        bundle.putBundle(key, allValue);
    }

    public void registerReportData(String key, Parcelable pValue) {
        bundle.putParcelable(key, pValue);
    }

    public void openForceReportMode() {
        forceReportMode = true;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultExceptionHandler != null) {
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null || mApp == null) {
            return false;
        }
        bundle.putString(CRASH_DATA, catchLogs(ex));
        if (forceReportMode) {
            LogReportService.start(mApp, bundle);
            return true;
        }
        try {
            Intent intent = new Intent(mApp, launcherCls);
            intent.setAction(INTENT_ERROR_HANDLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            mApp.startActivity(intent);
            System.exit(0);
            return true;
        } catch (Exception e) {
            return false;
        } catch (Error e) {
            return false;
        }
    }

    public static String getApplicationName(Context mApp) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = mApp.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mApp.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    private String catchLogs(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

}
