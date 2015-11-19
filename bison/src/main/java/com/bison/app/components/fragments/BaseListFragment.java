package com.bison.app.components.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bison.app.R;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public abstract class BaseListFragment <T extends Activity> extends Fragment implements OnErrorEvent{

    private final static String EXTRA_TAG = "extra_tag";

    private T context;

    private boolean hasCalledFirst = false;

    private final static String ERROR_UNIQUE = "error";

    private final static String LOAD_UNIQUE = "load";

    private final static int FADE_DURATION = 220;

    final private Handler mHandler = new Handler();

    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    final private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };

    ListAdapter mAdapter;

    FrameLayout contentView;

    ListView mList;

    View mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (T) getActivity();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mListContainer = onCreateListView(inflater, container, savedInstanceState);
        if (mListContainer == null) {
            throw  new IllegalArgumentException("ListFragment view missing");
        }
        mList = (ListView) mListContainer.findViewById(android.R.id.list);
        if(mList==null){
            throw  new IllegalArgumentException("onCreateListView must have a ListView with id android.R.id.list");
        }
        contentView = new FrameLayout(getContext());
        ViewGroup root = onCreateRootView(inflater, container, savedInstanceState);
        root.setId(R.id.child_container);
        root.addView(contentView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mEmptyView = onCreateEmptyView(inflater, container, savedInstanceState);
        if (mEmptyView != null) {
            mList.setEmptyView(mEmptyView);
            mEmptyView.setVisibility(View.GONE);
            contentView.addView(mEmptyView);
        }
        contentView.addView(mListContainer);
        return root;
    }

    protected abstract View onCreateListView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList.setOnItemClickListener(mOnClickListener);
        mHandler.post(mRequestFocus);
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
        mHandler.removeCallbacks(mRequestFocus);
        mList = null;
        contentView = null;
        mEmptyView = null;
        super.onDestroyView();
    }

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the data
     * associated with the selected item.
     *
     * @param l        The ListView where the click happened
     * @param v        The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
    }

    /**
     * Provide the cursor for the list view.
     */
    public void setListAdapter(ListAdapter adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mList != null) {
            mList.setAdapter(adapter);
            if (!isViewVisible(contentView) && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter. It is now time to show it.
                showOnBindData(getView().getWindowToken() != null);
            }
        }
    }

    /**
     * Set the currently selected list item to the specified position with the
     * adapter's data
     *
     * @param position
     */
    public void setSelection(int position) {
        mList.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        return mList.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        return mList.getSelectedItemId();
    }

    /**
     * Get the context's list view widget.
     */
    public ListView getListView() {
        return mList;
    }

    /**
     * The default content for a ListFragment has a TextView that can be shown
     * when the list is empty. If you would like to have it shown, call this
     * method to supply the text it should use.
     */
    public void setEmptyText(CharSequence text) {
        if (mEmptyView != null) {
            TextView emptyText = (TextView) mEmptyView
                    .findViewById(android.R.id.empty);
            if (emptyText != null) {
                emptyText.setText(text);
            }
        }
    }


    /**
     * Get the ListAdapter associated with this context's ListView.
     */
    public ListAdapter getListAdapter() {
        return mAdapter;
    }


    static boolean isViewVisible(View v){
        if(v==null){
            return false;
        }
        return v.getVisibility()==View.VISIBLE;
    }

    public T getContext() {
        return context;
    }

    protected void onShowToUserFirst() {

    }

    protected Fragment onCreateLoadingView(Context context) {

        return DefaultLoadingFragment.newInstance(getString(R.string.loading));
    }

    protected View onCreateEmptyView(LayoutInflater inflater,ViewGroup container,Bundle saveInstance) {
        TextView textView = new TextView(getContext());
        textView.setText(R.string.empty_tip);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetEmptyEvent();
            }
        });
        return textView;
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
    public void onGetEmptyEvent() {

    }
    @Override
    public void onGetErrorEvent(int errorType) {

    }
}
    