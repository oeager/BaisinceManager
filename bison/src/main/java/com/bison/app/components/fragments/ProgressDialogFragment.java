package com.bison.app.components.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bison.app.R;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class ProgressDialogFragment extends DialogFragmentWrapper {


    private ProgressBar progressBar;

    private TextView loadMessageTex;

    private final static String LOAD_MESSAGE = "load_message";

    private final static String LOAD_LAYOUT = "load_layout";


    public static ProgressDialogFragment newInstance(String loadMessage){
        return newInstance(loadMessage,-1);
    }

    public static ProgressDialogFragment newInstance(String loadMessage, @LayoutRes int layout) {
        Bundle bundle = generateStyleBundle(DialogFragment.STYLE_NORMAL, R.style.Bison_Progress_Dialog);
        bundle.putString(LOAD_MESSAGE, loadMessage);
        bundle.putInt(LOAD_LAYOUT,layout);
        ProgressDialogFragment p = new ProgressDialogFragment();
        p.setArguments(bundle);
        return p;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout = getArguments().getInt(LOAD_LAYOUT, -1);
        if(layout!=-1){
            return inflater.inflate(layout,container,false);
        }
        View view = inflater.inflate(R.layout.dialog_progress_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadMessageTex = (TextView) view.findViewById(R.id.load_message);
        String loadMessage = getArguments().getString(LOAD_MESSAGE);
        if (!TextUtils.isEmpty(loadMessage)) {
            loadMessageTex.setText(loadMessage);
        }
    }

    public void show(FragmentActivity context) {
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
    