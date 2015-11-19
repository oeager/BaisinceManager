package com.bison.app.components.intent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public final class IntentHelper {

    private IntentHelper() {
    }


    public static boolean verifyIntentReceiveApp(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = intent.resolveActivity(pm);
        return componentName != null;
    }

    public static int getIntentReceiveAppCount(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        return pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size();
    }

    public static List<ResolveInfo> getIntentReceiveApps(Context context, Intent intent){
        PackageManager pm = context.getPackageManager();
        return pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    public static boolean existMarketApp(Context context){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        return verifyIntentReceiveApp(context, intent);
    }

    public static boolean existBrowserApp(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://"));
        return verifyIntentReceiveApp(context,intent);
    }

    public static boolean existTelApp(Context context){
        Intent it = new Intent(Intent.ACTION_DIAL);
        return verifyIntentReceiveApp(context,it);
    }

    public static boolean existSmsApp(Context context){
        Intent it = new Intent(Intent.ACTION_SENDTO);
        return verifyIntentReceiveApp(context,it);
    }

    public static boolean existEmailApp(Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        return verifyIntentReceiveApp(context,intent);
    }
}
    