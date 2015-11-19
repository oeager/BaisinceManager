package com.bison.app.media_camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by oeager on 2015/6/10.
 * email: oeager@foxmail.com
 */
public class MediaUtils {

    private static Boolean _hasCamera = null;
    public static final boolean hasCamera(Context mContext) {
        if (_hasCamera == null) {
            PackageManager pckMgr = mContext
                    .getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            if (flag || flag1)
                flag2 = true;
            else
                flag2 = false;
            _hasCamera = Boolean.valueOf(flag2);
        }
        return _hasCamera.booleanValue();
    }

    public static void openCamera(Context context) {
        Intent intent = new Intent(); // 调用照相机
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setFlags(0x34c40000);
        context.startActivity(intent);
    }
}
