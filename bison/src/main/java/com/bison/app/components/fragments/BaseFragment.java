package com.bison.app.components.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bison.app.R;
import com.developer.bsince.log.GOL;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public abstract class BaseFragment<T extends Activity> extends Fragment implements OnEmptyEvent, OnErrorEvent {

    private final static String EXTRA_TAG = "extra_tag";

    private T context;

    private boolean hasCalledFirst = false;

    private final static String ERROR_UNIQUE = "error";

    private final static String EMPTY_UNIQUE = "empty";

    private final static String LOAD_UNIQUE = "load";

    private final static int FADE_DURATION = 220;

    private View contentView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (T) getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = onCreateContentView(inflater, container, savedInstanceState);
        if (contentView == null) {
            return null;
        }
        ViewGroup root = onCreateRootView(inflater, container, savedInstanceState);
        root.setId(R.id.child_container);
        root.addView(contentView);
        return root;
    }

    protected abstract View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


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
        FragmentManager fm = getChildFragmentManager();
        Fragment f = fm.findFragmentByTag(EXTRA_TAG);
        if (f != null) {
            fm.beginTransaction().remove(f).commitAllowingStateLoss();
        }
        super.onDestroyView();
    }

    public T getContext() {
        return context;
    }

    protected void onShowToUserFirst() {

    }

    protected Fragment onCreateLoadingView(Context context) {

        return DefaultLoadingFragment.newInstance(getString(R.string.loading));
    }

    protected Fragment onCreateEmptyView(Context context) {
        return DefaultEmptyFragment.newInstance(this, getString(R.string.empty_tip));
    }

    protected Fragment onCreateErrorView(Context context, int errorType) {
        return DefaultErrorFragment.newInstance(this, getString(R.string.error_happened), errorType);
    }

    public final void showOnLoadFail(int errorType, boolean animate) {
        String unique = ERROR_UNIQUE + errorType;
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment preFragment = fragmentManager.findFragmentByTag(EXTRA_TAG);

        if (equalFragmentUniqueId(preFragment, unique)){
//            GOL.e("当前已是相同错误的页面");
            return;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (preFragment != null){
            ft.remove(preFragment);
//            GOL.e("Show Fail 时发现不同的tag页卡,移除");
        }

        Fragment newFragment = onCreateErrorView(getContext(), errorType);
        setFragmentUniqueId(newFragment, unique);
        ft.add(R.id.child_container, newFragment, EXTRA_TAG);
        hideView(contentView,animate);
        if (animate)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commitAllowingStateLoss();
    }

    public final void showOnBindData(boolean animate) {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment preFragment = fragmentManager.findFragmentByTag(EXTRA_TAG);
        if (preFragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(preFragment);
//            GOL.e("Show Data 时发现不同的tag页卡,移除");
            showView(contentView,animate);
            if (animate)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commitAllowingStateLoss();
        }
    }

    public final void showOnEmpty(boolean animate) {
        FragmentManager fragmentManager =getChildFragmentManager();
        Fragment preFragment = fragmentManager.findFragmentByTag(EXTRA_TAG);
        if (equalFragmentUniqueId(preFragment, EMPTY_UNIQUE)){
//            GOL.e("当前已是相同的空页面");
            return;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (preFragment != null){
            ft.remove(preFragment);
//            GOL.e("Show Empty 时发现不同的tag页卡,移除");
        }

        Fragment newFragment = onCreateEmptyView(getContext());
        setFragmentUniqueId(newFragment, EMPTY_UNIQUE);
        ft.add(R.id.child_container, newFragment, EXTRA_TAG);
        hideView(contentView,animate);
        if (animate)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commitAllowingStateLoss();
    }

    public final void showOnLoading(boolean animate) {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment preFragment = fragmentManager.findFragmentByTag(EXTRA_TAG);
        if (equalFragmentUniqueId(preFragment, LOAD_UNIQUE)){
//            GOL.e("当前已是相同的加载页面");
            return;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (preFragment != null){
            ft.remove(preFragment);
//            GOL.e("Show Load 时发现不同的tag页卡,移除");
        }

        Fragment newFragment = onCreateLoadingView(getContext());
        setFragmentUniqueId(newFragment, LOAD_UNIQUE);
        ft.add(R.id.child_container, newFragment, EXTRA_TAG);
        hideView(contentView,animate);
        if (animate)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commitAllowingStateLoss();
    }

    protected abstract void initComponents(View createView, Bundle savedInstanceState);

    protected abstract void initComponentsData(Bundle savedInstanceState);

    private static void setFragmentUniqueId(Fragment f, String uniqueId) {
        View v = f.getView();
        if (v != null) {
            v.setTag(R.id.fragment_uniqueId, uniqueId);
        }

    }

    private static boolean equalFragmentUniqueId(Fragment f, String uniqueId) {
        if (f == null) {
            return false;
        }
        View v = f.getView();
        if (v == null) {
            return false;
        }
        Object o = v.getTag(R.id.fragment_uniqueId);
        if (o == null) {
            return false;
        }
        try {
            String oldId = (String) o;
            return uniqueId.equals(oldId);
        } catch (Exception e) {
            return false;
        }
    }


    protected ViewGroup onCreateRootView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstance) {
        FrameLayout f = new FrameLayout(getContext());
        f.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return f;
    }

    private void hideView(final View view, boolean animate) {
        if (view != null) {
            if (animate) {
                view.animate().alpha(0f).setDuration(FADE_DURATION).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(view.getVisibility()!=View.GONE){
                            view.setVisibility(View.GONE);
                        }

                    }
                }).start();
            } else {
                if(view.getAlpha()!=0f){
                    view.setAlpha(0f);
                }
                if(view.getVisibility()!=View.GONE){
                    view.setVisibility(View.GONE);
                }
            }

        }
    }

    private void showView(final View view, boolean animate) {
        if (view != null) {
            if (animate) {
                view.animate().alpha(1f).setDuration(FADE_DURATION).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (view.getVisibility() != View.VISIBLE) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                }).start();
            } else {
                if (view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
                if(view.getAlpha()!=1f){
                    view.setAlpha(1f);
                }

            }

        }
    }


    @Override
    public void onGetEmptyEvent() {

    }

    @Override
    public void onGetErrorEvent(int errorType) {

    }
}
    