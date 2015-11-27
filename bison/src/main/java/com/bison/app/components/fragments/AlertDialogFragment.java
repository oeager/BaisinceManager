package com.bison.app.components.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * Created by oeager on 2015/11/23.
 * email:oeager@foxmail.com
 */
public class AlertDialogFragment extends DialogFragment {

    private final static String DIALOG_TITLE = "dialog_title";
    private final static String DIALOG_ICON = "dialog_icon";
    private final static String DIALOG_MESSAGE = "dialog_message";
    private final static String DIALOG_POSITIVE_TEXT = "positive_text";
    private final static String DIALOG_NEGATIVE_TEXT = "negative_text";

    private final static String DIALOG_FRAGMENT_TAG = "alert_dialog_fragment_tag";

    public static AlertDialogFragment newInstance(String title,String message,String pos,String neg){
        return newInstance(-1,title,message,pos,neg);
    }

    public static AlertDialogFragment newInstance(int icon,String title,String message,String pos,String neg){
        Bundle bundle = new Bundle();
        bundle.putInt(DIALOG_ICON, icon);
        bundle.putString(DIALOG_TITLE, title);
        bundle.putString(DIALOG_MESSAGE, message);
        bundle.putString(DIALOG_POSITIVE_TEXT, pos);
        bundle.putString(DIALOG_NEGATIVE_TEXT, neg);
        AlertDialogFragment  f= new AlertDialogFragment();
        f.setArguments(bundle);
        return f;
    }

    private DialogInterface.OnClickListener onDialogClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String title = bundle.getString(DIALOG_TITLE);
        int icon = bundle.getInt(DIALOG_ICON);
        String message = bundle.getString(DIALOG_MESSAGE);
        String positiveTex = bundle.getString(DIALOG_POSITIVE_TEXT);
        String negativeTex = bundle.getString(DIALOG_NEGATIVE_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(!TextUtils.isEmpty(title)){
            builder.setTitle(title);
        }
        if(icon!=-1){
            builder.setIcon(icon);
        }
        if(!TextUtils.isEmpty(message)){
            builder.setMessage(message);
        }
        if(!TextUtils.isEmpty(positiveTex)){
            builder.setPositiveButton(positiveTex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onDialogClickListener != null) {
                        onDialogClickListener.onClick(dialog, Dialog.BUTTON_POSITIVE);
                    }
                }
            });
        }

        if(!TextUtils.isEmpty(negativeTex)){
            builder.setNegativeButton(negativeTex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onDialogClickListener != null) {
                        onDialogClickListener.onClick(dialog, Dialog.BUTTON_NEGATIVE);
                    }
                }
            });
        }
        return builder.create();

    }

    public void show(FragmentActivity context,DialogInterface.OnClickListener clickListener) {
        this.onDialogClickListener = clickListener;
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_FRAGMENT_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_FRAGMENT_TAG);
    }


}
    