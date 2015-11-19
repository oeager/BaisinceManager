package com.bison.app.components.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bison.app.R;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class DefaultErrorFragment extends Fragment {

    private final static int LAYOUT_DEFAULT = 0;

    private final static int LAYOUT_DEFINE = 1;

    private final static String LAYOUT_RES = "layRes";

    private final static String ERROR_TYPE = "error_type";

    protected final static String SHOWN_TEXT = "shownTex";

    protected final static String LAYOUT_MODE = "layoutMode";

    public static DefaultErrorFragment newInstance(OnErrorEvent event,@LayoutRes int layoutRes,int errorType){

        DefaultErrorFragment fragment = new DefaultErrorFragment();
        Bundle b = new Bundle();
        b.putInt(LAYOUT_RES, layoutRes);
        b.putInt(LAYOUT_MODE,LAYOUT_DEFINE);
        b.putInt(ERROR_TYPE,errorType);
        fragment.setArguments(b);
        fragment.registerErrorEvent(event);
        return fragment;

    }
    public static DefaultErrorFragment newInstance(OnErrorEvent event,String shownText,int errorType){

        DefaultErrorFragment fragment = new DefaultErrorFragment();
        Bundle b = new Bundle();
        b.putString(SHOWN_TEXT, shownText);
        b.putInt(LAYOUT_MODE, LAYOUT_DEFAULT);
        b.putInt(ERROR_TYPE,errorType);
        fragment.setArguments(b);
        fragment.registerErrorEvent(event);
        return fragment;

    }

    private OnErrorEvent event;

    private TextView textView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        int layoutMode = b.getInt(LAYOUT_MODE);
        final int errorType = b.getInt(ERROR_TYPE);
        if(layoutMode==LAYOUT_DEFAULT){
            String errorText = b.getString(SHOWN_TEXT);
            return createDefaultView(inflater, container, errorText,errorType);
        }else{
            int layoutRes = b.getInt(LAYOUT_RES);
            View errorView = inflater.inflate(layoutRes, container, false);
            View errorAction = errorView.findViewById(R.id.action_error);
            if(errorAction!=null){
                errorAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performErrorEvent(errorType);

                    }
                });
            }
            return errorView;
        }

    }

    protected View createDefaultView(LayoutInflater inflater,ViewGroup container,String shownText, final int errorType){
        textView = new TextView(getActivity());
        textView.setTextSize(18);
        textView.setText(shownText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performErrorEvent(errorType);
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

    public void registerErrorEvent(OnErrorEvent event){
        this.event = event;
    }

    public void unregisterErrorEvent(){
        this.event = null;
    }

    @Override
    public void onDestroyView() {
        unregisterErrorEvent();
        super.onDestroyView();
    }

    protected void performErrorEvent(int errorType){
        if(this.event!=null){
            this.event.onGetErrorEvent(errorType);
        }
    }

    public TextView getTextView(){
        return textView;
    }
}
    