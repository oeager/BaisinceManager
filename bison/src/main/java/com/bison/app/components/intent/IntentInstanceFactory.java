package com.bison.app.components.intent;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import java.util.Calendar;

/**
 * Created by oeager on 2015/11/11.
 * email:oeager@foxmail.com
 */
public final class IntentInstanceFactory {


    /**
     * Note :In order to invoke the ACTION_SET_ALARM intent, your app must have the SET_ALARM permission:
     * com.android.alarm.permission.SET_ALARM
     * <p/>
     * intentFilter:action android:name="android.intent.action.SET_ALARM" ;
     * category android:name="android.intent.category.DEFAULT"
     *
     * @param context
     * @param message
     * @param hour
     * @param minutes
     */
    public static void createAlarmInstance(Context context, String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivity(intent);
        }
    }

    /**
     * Note :In order to invoke the ACTION_SET_ALARM intent, your app must have the SET_ALARM permission:
     * com.android.alarm.permission.SET_ALARM
     * <p/>
     * intentFilter:action android:name="android.intent.action.SET_ALARM" ;
     * category android:name="android.intent.category.DEFAULT"
     *
     * @param context
     * @param message A custom message to identify the timer.
     * @param seconds The length of the timer in seconds.
     * @param skipUi  A boolean specifying whether the responding app should skip its UI
     *                when setting the timer. If true, the app should bypass
     *                any confirmation UI and simply start the specified timer.
     */
    public static void createTimerInstance(Context context, String message, int seconds, boolean skipUi) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivity(intent);
        }

    }

    /**
     * @param context
     * @param title    The event title.
     * @param location The event location.
     * @param begin
     * @param end
     */
    public static void createCalendarEventInstance(Context context, String title, String location, Calendar begin, Calendar end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivity(intent);
        }

    }

    /**
     * @param context
     * @param mLocationForPhotos save folder path
     * @param targetFilename     fileName
     * @param requestCode        the request code
     */
    public static void createCaptureImageInstance(Activity context, Uri mLocationForPhotos, String targetFilename, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    public static void createCaptureVideoInstance(Activity context, Uri mLocationForPhotos,
                                                  String targetFilename, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivityForResult(intent, requestCode);
        }
    }
    /**
     * @param context
     * @param mLocationForPhotos
     * @param targetFilename
     * @param requestCode
     * @param quality
     * @param sizeLimit
     * @param durationLimit      Specify the maximum allowed recording duration in seconds.
     */
    public static void createCaptureVideoInstance(Activity context, Uri mLocationForPhotos,
                                                  String targetFilename, int requestCode,
                                                  int quality, long sizeLimit, int durationLimit
    ) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit);

        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivityForResult(intent, requestCode);

        }
    }

    public static void createContactsSelectInstance(Activity context,int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    /**
     *
     * @param context
     * @param mimeType CommonDataKinds.Phone.CONTENT_TYPE;CommonDataKinds.Email.CONTENT_TYPE;CommonDataKinds.StructuredPostal.CONTENT_TYPE
     * @param requestCode
     */
    public static void createContactsSelectInstance(Activity context,String mimeType,int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(mimeType);
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    /**
     *
     * @param context
     * @param contactUri
     * @param editKey just like Intents.Insert.EMAIL
     * @param editValue
     */
    public static void createContactEditInstance(Context context,Uri contactUri,String editKey,String editValue){
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(contactUri);
        intent.putExtra(editKey, editValue);
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivity(intent);
        }

    }

    public static void createImagePickInstance(Activity context,int requestCode){
        createFilePickInstance(context, "image/*", requestCode);
    }
    public static void createImageOpenInstance(Activity context,int requestCode){
        createFilePickInstance(context, "image/*", requestCode);

    }
    public static void createFilePickInstance(Activity context,String mimeType,int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        if(IntentHelper.verifyIntentReceiveApp(context,intent)){
            context.startActivityForResult(intent,requestCode);
        }
    }
    public static void createFileOpenInstance(Activity context,String mimeType,int requestCode){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if(IntentHelper.verifyIntentReceiveApp(context,intent)){
            context.startActivityForResult(intent, requestCode);
        }
    }
    public static void createMediaPlayInstance(Context context,Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (IntentHelper.verifyIntentReceiveApp(context,intent)) {
            context.startActivity(intent);
        }
    }

    public static void createSearchWebInstance(Context context,String query) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (IntentHelper.verifyIntentReceiveApp(context,intent)) {
            context.startActivity(intent);
        }
    }

}
    