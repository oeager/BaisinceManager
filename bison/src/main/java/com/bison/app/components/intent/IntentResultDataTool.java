package com.bison.app.components.intent;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public final class IntentResultDataTool {

    public static Bitmap getImageDataReturnedByCameraApp(Intent data){
        Bundle extras = data.getExtras();
        return (Bitmap) extras.get("data");

    }

    public static Uri getVideoUriReturnedByCameraApp(Intent data){
        return data.getData();
    }

    public static String[] getSelectedContactData(Context context, Intent data) {
        Uri contactUri = data.getData();
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1
        };
        Cursor cursor = context.getContentResolver().query(contactUri, projection,
                null, null, null);
        // If the cursor returned is valid, get the phone number
        if (cursor != null && cursor.moveToFirst()) {
            String[] results = new String[2];
            results[0] = cursor.getString(0);
            results[1] = cursor.getString(1);
            return results;
        }

        return null;
    }
}
    