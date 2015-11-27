package com.bison.app.ui.assist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by oeager on 2015/11/20.
 * email:oeager@foxmail.com
 */
public abstract class SimpleListAdapter<T> extends ListAdapter<ListViewHolderFactory.LayoutViewHolder,T> {

    public SimpleListAdapter(Context context) {
        super(context);
    }

    public SimpleListAdapter(Context context, T[] mList) {
        super(context, mList);
    }

    public SimpleListAdapter(Context context, List<T> mList) {
        super(context, mList);
    }

    @Override
    protected ListViewHolderFactory.LayoutViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new ListViewHolderFactory.LayoutViewHolder(inflater,parent,getItemLayout());
    }

    protected abstract  @LayoutRes int getItemLayout();

    protected abstract void onBindViewHolder(ListViewHolderFactory.LayoutViewHolder holder,T t);

    @Override
    protected final void onBindViewHolder(ListViewHolderFactory.LayoutViewHolder holder, int position) {
        T t = getItem(position);
        onBindViewHolder(holder,t);
    }
}
    