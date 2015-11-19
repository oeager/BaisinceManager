package com.bison.app.components.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.bison.app.R;
import com.bison.app.components.BaseApp;
import com.bison.app.components.utils.AppUtils;


/**
 * Created by oeager on 2015/5/28.
 * email: oeager@foxmail.com
 */
public class UpgradeManager {

    volatile static UpgradeManager INSTANCE;

    private final DownloadMangerProxy downloadMangerProxy;

    private long downloadId;

    private UpgradeManager(final Context mContext) {
        downloadMangerProxy = DownloadMangerProxy.getDefault(mContext);
        downloadMangerProxy.addDownloadCompleteListener(new DownloadMangerProxy.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete(long id, Uri localUri) {
                if (id == downloadId) {

                    downloadMangerProxy.release();
                    AppUtils.installApk(mContext, localUri);
                    BaseApp.getApp().appExit();
                }
            }
        });
    }

    public static UpgradeManager getDefault(Context mContext) {

        if (INSTANCE == null) {
            synchronized (UpgradeManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UpgradeManager(mContext);
                }
            }
        }
        return INSTANCE;
    }

    public void upgrade(UpgradeInfo info) {

        if (info.newVersionCode > info.oldVersionCode) {
            Activity activity = BaseApp.getApp().currentActivity();
            if (activity == null) {
                throw new IllegalArgumentException("is there any context active?or you not use AppManager?");
            }
            showUpgradeInfoDialog(activity, info);
            //
        }


    }

    private void showUpgradeInfoDialog(Activity activity, final UpgradeInfo info) {

        PackageInfo pi = AppUtils.getPackageInfo(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String message = activity.getString(R.string.app_upgrade_tips);
        String upgradeWay = info.forceUpgrade ? activity.getString(R.string.force_upgrade) : activity.getString(R.string.suggest_upgrade);
        builder.setMessage(String.format(message, info.newVersionName, info.releaseNote, upgradeWay));
        builder.setTitle(String.format(activity.getString(R.string.app_version_upgrade), activity.getString(pi.applicationInfo.labelRes)));

        if (info.forceUpgrade) {
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            downloadId = downloadMangerProxy.enqueue(info);
        } else {
            builder.setPositiveButton(R.string.upgrade, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downloadId = downloadMangerProxy.enqueue(info);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.after, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

    }


}
