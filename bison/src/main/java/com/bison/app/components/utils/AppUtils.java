package com.bison.app.components.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.developer.bsince.log.GOL;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * <p>This is an auxiliary class associated with the app operation</p>
 * @author oeager
 * @since  1.0
 */
public class AppUtils
{
	private AppUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * get the name of application
	 *
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get the versionName of the application
	 *
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get the packageInfos of the application
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		
		return info;
	}
	
	/**
	 * install an app on user's mobile phone by the path of the apk.
	 * @param apkFilePath the path of the apk file;
	 */
	public static void installApk(Context context,String apkFilePath) {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			Toast.makeText(context, "apk file is not exist", Toast.LENGTH_SHORT).show();
			return;
		}
		Uri uri = Uri.fromFile(apkfile);
		installApk(context, uri);
	}

	public static void installApk(Context context,Uri uri){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(i);
	}
	/**
	 * uninstall the application by packageName
	 * @param packageName packageName
	 */
	public static void uninstallApk(Context cx,String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		Uri packageURI = Uri.parse("package:" + packageName);
		intent.setData(packageURI);
		cx.startActivity(intent);
	}
	
	/**
	 * This method is used to determine whether the service is running
	 * @param ctx  Interface to global information about an application environment
	 * @param className  name of the service
	 * @return true if the service is running
	 *
	 * @see android.app.Service
	 */
	public static boolean isServiceRunning(Context ctx, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			if (className.equals(si.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	public static boolean hasPermission(Context context,String permission) {
		final PackageManager pm = context.getPackageManager();
		if (pm == null) {
			return false;
		}

		try {
			return pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
		} catch (RuntimeException e) {
			// To catch RuntimeException("Package manager has died") that can occur on some version of Android,
			// when the remote PackageManager is unavailable. I suspect this sometimes occurs when the App is being reinstalled.
			return false;
		}
	}

    public static boolean isApplicationBroughtToBackground(Context mContext){

        ActivityManager am = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

	public static boolean isPackageExist(Context context,String pckName) {
		try {
			PackageInfo pckInfo = context.getPackageManager()
					.getPackageInfo(pckName, 0);
			if (pckInfo != null)
				return true;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String getIMEI(Context mContext) {
		TelephonyManager tel = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tel.getDeviceId();
	}

	public static String getPhoneType() {
		return android.os.Build.MODEL;
	}

	public static void openApp(Context context, String packageName) {
		Intent mainIntent = context.getPackageManager()
				.getLaunchIntentForPackage(packageName);
		if (mainIntent == null) {
			mainIntent = new Intent(packageName);
		} else {
			GOL.d("Action:" + mainIntent.getAction());
		}
		context.startActivity(mainIntent);
	}

	public static boolean openAppActivity(Context context, String packageName,
										  String activityName) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cn = new ComponentName(packageName, activityName);
		intent.setComponent(cn);
		try {
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

