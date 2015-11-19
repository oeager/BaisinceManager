package com.bison.app.components.fragments;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class DefaultEmptyFragment extends Fragment {

    private final static int LAYOUT_DEFAULT = 0;

    private final static int LAYOUT_DEFINE = 1;

    private final static String LAYOUT_RES = "layRes";

    protected final static String SHOWN_TEXT = "shownTex";

    protected final static String LAYOUT_MODE = "layoutMode";

    public static DefaultEmptyFragment newInstance(OnEmptyEvent event,@LayoutRes int layoutRes){

        DefaultEmptyFragment fragment = new DefaultEmptyFragment();
        Bundle b = new Bundle();
        b.putInt(LAYOUT_RES, layoutRes);
        b.putInt(LAYOUT_MODE,LAYOUT_DEFINE);
        fragment.setArguments(b);
        fragment.registerEmptyEvent(event);
        return fragment;
    }

    public static DefaultEmptyFragment newInstance(OnEmptyEvent event,String shownText){

        DefaultEmptyFragment fragment = new DefaultEmptyFragment();
        Bundle b = new Bundle();
        b.putString(SHOWN_TEXT, shownText);
        b.putInt(LAYOUT_MODE, LAYOUT_DEFAULT);
        fragment.setArguments(b);
        fragment.registerEmptyEvent(event);
        return fragment;

    }

    private TextView textView;

    private OnEmptyEvent event;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle b = getArguments();
        int layoutMode = b.getInt(LAYOUT_MODE);
        if(layoutMode==LAYOUT_DEFAULT){
            String shownText = b.getString(SHOWN_TEXT);
            return createDefaultView(inflater, container, shownText);
        }else{
            int layoutRes = b.getInt(LAYOUT_RES);
            View emptyView = inflater.inflate(layoutRes, container, false);
            View emptyAction = emptyView.findViewById(android.R.id.empty);
            if(emptyAction!=null){
                emptyAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performEmptyEvent();
                    }
                });
            }
            return emptyView;
        }

    }

    protected View createDefaultView(LayoutInflater inflater,ViewGroup container,String shownText){
        textView = new TextView(getActivity());
        textView.setTextSize(18);
        textView.setText(shownText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performEmptyEvent();
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        if (container!=null&&container instanceof FrameLayout) {
            textView.setLayoutParams(params);
            return textView;
        } else {
            FrameLayout frameLayout = new FrameLayout(getActivity());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayout.addView(textView, params);
            return frameLayout;
        }
    }

    public void registerEmptyEvent(OnEmptyEvent event){
        this.event = event;
    }

    public void unregisterEmptyEvent(){
        this.event = null;
    }

    @Override
    public void onDestroyView() {
        unregisterEmptyEvent();
        super.onDestroyView();
    }

    protected void performEmptyEvent(){
        if(this.event!=null){
            this.event.onGetEmptyEvent();
        }
    }

    public TextView getTextView(){
        return textView;
    }
}
    