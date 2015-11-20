package com.bison.crash.catcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by oeager on 2015/11/19.
 * email:oeager@foxmail.com
 */
public final class ReportFunc {

    private final static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String formatDate(long time){
        Date date = new Date(time);
        return formatter.format(date);
    }

    private final static String TAG = "ReportFunc";

    public interface IReporter {

        String generateBody(Context context, String crashData);

        void report(Context context, Bundle data);
    }

    public static class EmailReporter implements IReporter {

        public final static String EMAILS = "emails";

        public final static String SUBJECT = "subject";

        @Override
        public String generateBody(Context context,String crashData) {
            return StringWrapper(context, crashData);
        }

        @Override
        public void report(Context context, Bundle data) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            PackageManager pm = context.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                intent.putExtra(Intent.EXTRA_EMAIL, data.getStringArray(EMAILS));
                intent.putExtra(Intent.EXTRA_SUBJECT, data.getString(SUBJECT));
                intent.putExtra(Intent.EXTRA_TEXT, data.getString(CrashCatcher.CONTENT));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else {
                Log.e(TAG,context.getString(R.string.no_emai_app));
            }
        }

    }

    public static class HttpReporter implements IReporter {

        public final static String HEADERS = "headers";

        public final static String URL = "url";
        @Override
        public String generateBody(Context context,String crashData) {
            String body = StringWrapper(context,crashData);
//            Log.e(TAG,body);
            return body;
        }

        @Override
        public void report(Context context, Bundle data) {
            String url = data.getString(URL);
            if (TextUtils.isEmpty(url)) {
                Log.e(TAG, "you should set your report url first");
                return;
            }

            try {
                URL reportUrl = new URL(url);
                HttpURLConnection urlConn = (HttpURLConnection) reportUrl.openConnection();
                urlConn.setConnectTimeout(30 * 1000);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("POST");
                urlConn.setInstanceFollowRedirects(true);
                Bundle bundle = data.getBundle(HEADERS);
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        urlConn.addRequestProperty(key, bundle.getString(key));
                    }
                }
                String body = data.getString(CrashCatcher.CONTENT);
                GZIPOutputStream os = new GZIPOutputStream(
                        urlConn.getOutputStream());
                os.write(body.getBytes());
                os.flush();
                os.close();
                int statusCode = urlConn.getResponseCode();
                // 判断是否请求成功(状态码200表示成功)
                if (statusCode != HttpsURLConnection.HTTP_OK) {
                    Log.e(TAG, "statusCode = " + statusCode + ",report fail");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e) {
                e.printStackTrace();
            }
        }
    }

    static void collectAppInfo(Context mContext,StringBuffer buffer) {
        PackageManager pm = mContext.getPackageManager();
        try {
            buffer.append("app_info:\n");
            buffer.append("app_name:");
            buffer.append(CrashCatcher.getApplicationName(mContext));
            buffer.append("\n");

            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            buffer.append("app_version:");
            buffer.append(pi.versionName);
            buffer.append("\n");
            buffer.append("app_versionCode:");
            buffer.append(pi.versionCode);
            buffer.append("\n");

            buffer.append("crash_date:");
            buffer.append(formatDate(System.currentTimeMillis()));
            buffer.append("\n");

        } catch (Exception e) {
            e.printStackTrace();
        }catch (Error error){
            error.printStackTrace();
        }

    }

    static String StringWrapper(Context context,String crashData ){
        StringBuffer buffer = new StringBuffer();
        collectAppInfo(context, buffer);
        collectMobileInfo(context, buffer);
        buffer.append("Exception_:\n");
        buffer.append(crashData);
        return buffer.toString();
    }

    static void collectMobileInfo(Context context,StringBuffer buffer) {

        try {
            buffer.append("MobileInfo:");
            buffer.append("\n");
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field: fields){
                //暴力反射 ,获取私有的信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                buffer.append(name+":"+value);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Error e){

        }
    }
}
    