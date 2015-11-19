package com.bison.support.v4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bison.support.R;

import static com.bison.support.v4.ViewTool.*;

/**
 * Created by oeager on 2015/11/10.
 * email:oeager@foxmail.com
 */
public abstract class SupportAppFragmentWrapper<T extends Activity> extends Fragment {

    private T context;

    private View loadView;

    private FrameLayout failView;

    private View emptyView;

    private View contentFrame;

    private boolean hasCalledFirst = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (T) getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!supportExtraWrapper()) {
            return onCreateContentView(inflater, container, savedInstanceState);
        }
        ViewGroup root = onCreateRootView(inflater, container, savedInstanceState);
        //----add loading view
        loadView = onCreateLoadingView(inflater, container, savedInstanceState);
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
            FrameLayout.LayoutParams loadParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            loadParam.gravity = Gravity.CENTER;

            root.addView(loadView, loadParam);
        }
        //----add errorView
        failView = new FrameLayout(getContext());
        failView.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        root.addView(failView, params);
        //-----add contentView
        contentFrame = onCreateContentView(inflater, container, savedInstanceState);
        if (contentFrame != null) {
            root.addView(contentFrame, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        //----add emptyView
        emptyView = onCreateEmptyView(inflater, container, savedInstanceState);
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            root.addView(emptyView, layoutParams);
        }
        return root;
    }


    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view, savedInstanceState);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponentsData(savedInstanceState);
        if (!hasCalledFirst) {
            hasCalledFirst = true;
            onShowToUserFirst();
        }
    }

    @Override
    public void onDestroyView() {
        contentFrame = null;
        failView = null;
        loadView = emptyView = null;
        super.onDestroyView();
    }

    protected ViewGroup onCreateRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout root = new FrameLayout(getContext());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return root;
    }

    protected View onCreateLoadingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(false);
        return progressBar;
    }

    protected View onCreateEmptyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText(R.string.empty_tip);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowToUserFirst();
            }
        });
        return textView;
    }

    protected View onCreateErrorView(LayoutInflater inflater, ViewGroup container, int errorType) {
        TextView textView = new TextView(getContext());
        textView.setText(R.string.error_happened);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowToUserFirst();
            }
        });
        return textView;

    }

    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public T getContext() {
        return context;
    }

    protected void onShowToUserFirst() {

    }

    protected boolean supportExtraWrapper() {
        return true;
    }

    public void showOnLoadFail(int errorType, boolean animate) {
        if (failView == null) {
            return;
        }
        hideView(contentFrame);
        hideView(emptyView);

        if (!equalIntTag(failView, errorType)) {
            failView.removeAllViews();
            View errorView = onCreateErrorView(getContext().getLayoutInflater(), failView, errorType);
            failView.addView(errorView);
            failView.setTag(errorType);
        }

        if (failView.getVisibility() == View.VISIBLE) {
            hideView(loadView);
            return;
        }

        if (animate) {
            showViewAnimated(failView);
            hideViewAnimated(loadView);

        } else {
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(failView);
            safeClearViewAnimation(contentFrame);
            safeClearViewAnimation(emptyView);
        }
        showView(failView);
        hideView(loadView);
    }

    public void showOnBindData(boolean animate) {
        if (contentFrame == null) {
            return;
        }
        hideView(failView);
        hideView(emptyView);
        if (contentFrame.getVisibility() == View.VISIBLE) {
            hideView(loadView);
            return;
        }
        if (animate) {
            showViewAnimated(contentFrame);
            hideViewAnimated(loadView);

        } else {
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(failView);
            safeClearViewAnimation(contentFrame);
            safeClearViewAnimation(emptyView);
        }
        showView(contentFrame);
        hideView(loadView);


    }

    public void showOnEmpty(boolean animate) {
        if (emptyView == null) {
            return;
        }
        hideView(failView);
        hideView(contentFrame);
        if (emptyView.getVisibility() == View.VISIBLE) {
            hideView(loadView);
            return;
        }
        if (animate) {
            showViewAnimated(emptyView);
            hideViewAnimated(loadView);

        } else {
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(failView);
            safeClearViewAnimation(contentFrame);
            safeClearViewAnimation(emptyView);
        }
        showView(emptyView);
        hideView(loadView);

    }


    public void showOnLoading(boolean animate) {
        if (loadView == null) {
            return;
        }
        hideView(failView);
        hideView(emptyView);
        if (loadView.getVisibility() == View.VISIBLE) {
            hideView(contentFrame);
            return;
        }
        if (animate) {
            showViewAnimated(loadView);
            hideViewAnimated(contentFrame);

        } else {
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(failView);
            safeClearViewAnimation(contentFrame);
            safeClearViewAnimation(emptyView);
        }
        showView(loadView);
        hideView(contentFrame);
    }

    protected abstract void initComponents(View createView, Bundle savedInstanceState);

    protected abstract void initComponentsData(Bundle savedInstanceState);
}
    