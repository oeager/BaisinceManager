package com.bison.app.components.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class DialogFragmentWrapper extends DialogFragment {

    public final static String DIALOG_FRAGMENT_TAG = "dialog_fragment_tag";

    private final static String STYLE_KEY = "style";

    private final static String THEME_KEY = "theme";


    protected static Bundle generateStyleBundle(int style, int theme) {
        Bundle bundle = new Bundle();
        bundle.putInt(STYLE_KEY, style);
        bundle.putInt(THEME_KEY, theme);
        return bundle;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            int style = bundle.getInt(STYLE_KEY);
            int theme = bundle.getInt(THEME_KEY);
            setStyle(style,theme);
        }

    }
}
    