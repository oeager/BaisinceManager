package com.bison.app.components.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class DefaultLoadingFragment extends Fragment {

    private final static int LAYOUT_DEFAULT = 0;

    private final static int LAYOUT_DEFINE = 1;

    private final static String LAYOUT_RES = "layRes";

    protected final static String SHOWN_TEXT = "shownTex";

    protected final static String LAYOUT_MODE = "layoutMode";

    public static DefaultLoadingFragment newInstance(@LayoutRes int layoutRes) {

        DefaultLoadingFragment fragment = new DefaultLoadingFragment();
        Bundle b = new Bundle();
        b.putInt(LAYOUT_RES, layoutRes);
        b.putInt(LAYOUT_MODE, LAYOUT_DEFINE);
        fragment.setArguments(b);
        return fragment;
    }

    public static DefaultLoadingFragment newInstance(String shownText) {

        DefaultLoadingFragment fragment = new DefaultLoadingFragment();
        Bundle b = new Bundle();
        b.putString(SHOWN_TEXT, shownText);
        b.putInt(LAYOUT_MODE, LAYOUT_DEFAULT);
        fragment.setArguments(b);
        return fragment;

    }

    private ProgressBar progressBar;

    private TextView textView;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle b = getArguments();
        int layoutMode = b.getInt(LAYOUT_MODE);
        if (layoutMode == LAYOUT_DEFAULT) {
            String shownText = b.getString(SHOWN_TEXT);
            return createDefaultView(inflater, container, shownText);
        } else {
            int layoutRes = b.getInt(LAYOUT_RES);
            return inflater.inflate(layoutRes, container, false);

        }

    }

    protected View createDefaultView(LayoutInflater inflater, ViewGroup container, String shownText) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        progressBar = new ProgressBar(getActivity());
        if (TextUtils.isEmpty(shownText)) {
            if (container != null && container instanceof FrameLayout) {
                progressBar.setLayoutParams(params);
                return progressBar;
            } else {
                FrameLayout frameLayout = new FrameLayout(getActivity());
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                frameLayout.addView(progressBar, params);
                return frameLayout;
            }
        } else {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView = new TextView(getActivity());
            textView.setText(shownText);
            textView.setTextSize(18);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 20;
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            layout.addView(progressBar);
            layout.addView(textView, lp);
            return layout;
        }
    }

    public TextView getTextView() {
        return textView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
    