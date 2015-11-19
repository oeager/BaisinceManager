package com.bison.app.components.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by oeager on 2015/6/10.
 * email: oeager@foxmail.com
 */
public class IntentUtils {

    private IntentUtils() {

    }

    public static void launchMarketApp(Context context, String pck) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if (IntentHelper.verifyIntentReceiveApp(context, intent)) {
            context.startActivity(intent);
        }

    }

    public static void launchMarketToShowSelf(Context context){
        launchMarketApp(context, context.getPackageName());
    }

    public static void launchGoogleMarket(Context activity, String pck) {
        Intent intent = new Intent();
        intent.setPackage("com.android.vending");
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if(IntentHelper.verifyIntentReceiveApp(activity,intent)){
            activity.startActivity(intent);
        }
    }

    /**
     * jump to the browser from user's current application with a linked url;
     *
     * @param cx
     * @param url the url
     */
    public static void launchBrowserApp(Context cx, String url) {

        boolean hasBrowser = IntentHelper.existBrowserApp(cx);
        if (hasBrowser) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            cx.startActivity(intent);
        }
    }

    public static void launchDefaultBrowser(Context cx, String url) {

        boolean hasBrowser = IntentHelper.existBrowserApp(cx);
        if (hasBrowser) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            cx.startActivity(intent);
        }
    }


    public static void launchTelDialApp(Context context, String number) {
        if(IntentHelper.existTelApp(context)){
            Uri uri = Uri.parse("tel:" + number);
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(it);
        }
    }

    public static void launchTelDialApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if(IntentHelper.verifyIntentReceiveApp(context, intent)){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
    public static void launchSmsApp(Context context, String smsBody, String tel) {
      if(IntentHelper.existSmsApp(context)){
          Uri uri = Uri.parse("smsto:" + tel);
          Intent it = new Intent(Intent.ACTION_SENDTO, uri);
          it.putExtra("sms_body", smsBody);
          context.startActivity(it);
      }
    }

    public static void launchSmsApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        if(IntentHelper.verifyIntentReceiveApp(context, intent)){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }


    public static void launchEmailApp(Context context, String subject,String content,Uri attachment, String... emails) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if(IntentHelper.verifyIntentReceiveApp(context,intent)){
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            if(attachment!=null){
                intent.putExtra(Intent.EXTRA_STREAM, attachment);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void showSystemShareOption(Activity context,final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }


    public static void launchCameraAppForImage(Activity context,int requestCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (IntentHelper.verifyIntentReceiveApp(context,intent)) {
            context.startActivityForResult(intent, requestCode);
        }
    }



    /**on result ,do like this:
     *  Uri videoUri = intent.getData();
     mVideoView.setVideoURI(videoUri);

     * @param context
     * @param requestCode
     */
    public static void launchCameraAppForVideo(Activity context,int requestCode){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(IntentHelper.verifyIntentReceiveApp(context, takeVideoIntent)){
            context.startActivityForResult(takeVideoIntent,requestCode);
        }
    }

    public static void launchCameraAppStillImage(Activity context,int requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if(IntentHelper.verifyIntentReceiveApp(context, intent)){
            context.startActivityForResult(intent, requestCode);
        }
    }

    public static void launchCameraAppStillVideo(Activity context,int requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if(IntentHelper.verifyIntentReceiveApp(context, intent)){
            context.startActivityForResult(intent, requestCode);
        }
    }

    public static void launchContactByUri(Context context,Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (IntentHelper.verifyIntentReceiveApp(context,intent)) {
            context.startActivity(intent);
        }

    }





}
