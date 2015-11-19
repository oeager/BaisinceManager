package com.bison.support.v4;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bison.support.R;

import static com.bison.support.v4.ViewTool.*;

public abstract class SupportAppListFragmentWrapper<T extends Activity> extends Fragment {
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

    FrameLayout mContentView;

    ListView mList;

    View loadView;

    View mEmptyView;

    FrameLayout mErrorView;

    public T context;

    private boolean hasCalledFirst = false;


    public SupportAppListFragmentWrapper() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (T) getActivity();
    }

    /**
     * Provide default implementation to return a simple list view. Subclasses
     * can override to replace with their own layout. If doing so, the returned
     * view hierarchy <em>must</em> have a ListView whose id is
     * {@link android.R.id#list android.R.id.list} and can optionally have a
     * sibling view id {@link android.R.id#empty android.R.id.empty} that is to
     * be shown when the list is empty.
     * <p>
     * <p>
     * If you are overriding this method with your own custom content, consider
     * including the standard layout {@link android.R.layout#list_content} in
     * your layout file, so that you continue to retain all of the standard
     * behavior of ListFragment. In particular, this is currently the only way
     * to have the built-in indeterminant progress state be shown.
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {

        if (!supportExtraWrapper()) {
            View listViewContainer = onCreateListView(inflater, container, savedInstanceState);
            mList = (ListView) listViewContainer.findViewById(android.R.id.list);
            mEmptyView = listViewContainer.findViewById(android.R.id.empty);
            if (mEmptyView != null) {
                mList.setEmptyView(mEmptyView);
            }
            return listViewContainer;
        }

        ViewGroup root = onCreateRootView(inflater, container, savedInstanceState);

        // ------loadview------------------------------------------------------------

        loadView = onCreateLoadingView(inflater, container, savedInstanceState);
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
            FrameLayout.LayoutParams loadParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            loadParam.gravity = Gravity.CENTER;

            root.addView(loadView, loadParam);
        }

        // ----------errorView--------------------------------------------------------
        mErrorView = new FrameLayout(context);
        mErrorView.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        root.addView(mErrorView, params);
        //-----------
        mContentView = new FrameLayout(context);
        root.addView(mContentView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        //--------emptyView-----------------------------------------------------------


        mEmptyView = onCreateEmptyView(inflater, container, savedInstanceState);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
            FrameLayout.LayoutParams emptyParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            emptyParam.gravity = Gravity.CENTER;
            mContentView.addView(mEmptyView, emptyParam);
        }

        //------ListView--------
        View listViewContainer = onCreateListView(inflater, container, savedInstanceState);
        mList = (ListView) listViewContainer.findViewById(android.R.id.list);
        if (mEmptyView != null) {
            mList.setEmptyView(mEmptyView);
        }
        mContentView.addView(listViewContainer, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }


    /**
     * Attach to list view once the view hierarchy has been created.
     */
    @Override
    public final void onViewCreated(View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        mList.setOnItemClickListener(mOnClickListener);
        mHandler.post(mRequestFocus);
        initComponents(root,savedInstanceState);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponentsData(savedInstanceState);
        if(!hasCalledFirst){
            hasCalledFirst = true;
            onShowToUserFirst();
        }
    }

    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mRequestFocus);
        mList = null;
        mErrorView = null;
        mContentView = null;
        loadView = mEmptyView = null;
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
            if (!isViewVisible(mContentView) && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter. It is now time to show it.
                setListShown(getView().getWindowToken() != null);
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


    //--------------------------our self----
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

    protected View onCreateListView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView lv = new ListView(context);
        lv.setCacheColorHint(Color.TRANSPARENT);
        lv.setId(android.R.id.list);
        lv.setDrawSelectorOnTop(false);
        return lv;
    }

    protected void setListShown(boolean animate) {
        if (mContentView == null) {
            return;
        }
        hideView(mErrorView);
        if (isViewVisible(mContentView)) {
            hideView(loadView);
            return;
        }
        if (animate) {
            showViewAnimated(mContentView);
            hideView(loadView);
        } else {
            safeClearViewAnimation(mContentView);
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(mErrorView);
        }
        showView(mContentView);
        hideView(loadView);
    }

    public void showOnLoading(boolean animate) {
        if (loadView == null) {
            return;
        }
        hideView(mContentView);
        if (isViewVisible(loadView)) {
            hideView(mErrorView);
            return;
        }
        if (animate) {
            showViewAnimated(loadView);
            hideViewAnimated(mErrorView);

        } else {
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(mErrorView);
            safeClearViewAnimation(mContentView);
        }
        showView(loadView);
        hideView(mErrorView);
    }

    public void showOnLoadFail(int errorType, boolean animate) {
        if (mErrorView == null) {
            return;
        }
        hideView(mContentView);

        if (!equalIntTag(mErrorView, errorType)) {
            mErrorView.removeAllViews();
            View errorView = onCreateErrorView(getContext().getLayoutInflater(), mErrorView, errorType);
            mErrorView.addView(errorView);
            mErrorView.setTag(errorType);
        }

        if (isViewVisible(mErrorView)) {
            hideView(loadView);
            return;
        }

        if (animate) {
            showViewAnimated(mErrorView);
            hideViewAnimated(loadView);

        } else {
            safeClearViewAnimation(loadView);
            safeClearViewAnimation(mErrorView);
            safeClearViewAnimation(mContentView);
        }
        showView(mErrorView);
        hideView(loadView);
    }


    public T getContext() {
        return context;
    }

    protected void onShowToUserFirst() {

    }

    protected boolean supportExtraWrapper() {
        return true;
    }

    protected abstract void initComponents(View createView, Bundle savedInstanceState);

    protected abstract void initComponentsData(Bundle savedInstanceState);
}
