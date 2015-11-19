package com.bison;

import com.bison.app.components.BaseApp;
import com.bison.crash.catcher.CrashCatcher;
import com.bison.crash.catcher.ReportFunc;

/**
 * Created by oeager on 2015/11/18.
 * email:oeager@foxmail.com
 */
public class AppContext extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashCatcher crashCatcher = new CrashCatcher();
        crashCatcher.init(this);
        crashCatcher.restoreDefaultSettings();
        crashCatcher.registerReportData(ReportFunc.EmailReporter.SUBJECT, "程序崩溃日志");
        crashCatcher.registerReportData(ReportFunc.EmailReporter.EMAILS, new String[]{"oeager@foxmail.com"});
        crashCatcher.registerReporter(ReportFunc.EmailReporter.class);
//        crashCatcher.registerReportData(ReportFunc.HttpReporter.URL,"http://www.baidu.com");

    }

    @Override
    protected void onAppExit() {

    }
}
    