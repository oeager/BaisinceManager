package com.bison.crash.catcher;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by oeager on 2015/11/17.
 * email:oeager@foxmail.com
 */
public class LogReportService extends IntentService {

    public LogReportService(){
        this("logReportService");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LogReportService(String name) {
        super(name);
    }


    public static void start(Context context,Bundle bundle) {
        Intent starter = new Intent(context, LogReportService.class);
        starter.putExtras(bundle);
        context.startService(starter);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle =intent.getExtras();
        Object o = bundle.getSerializable(CrashCatcher.REPORTER);
        String crashData = bundle.getString(CrashCatcher.CRASH_DATA);
        if(o==null){
            return;
        }

        Class<?> cls = (Class<?>) o;

        try {
            ReportFunc.IReporter reporter = (ReportFunc.IReporter) cls.newInstance();
            String content = reporter.generateBody(this,crashData);
            bundle.putString(CrashCatcher.CONTENT,content);
            reporter.report(this,bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Error e){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
    