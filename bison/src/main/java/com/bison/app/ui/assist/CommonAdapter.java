package com.bison.app.ui.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

	private List<T> mData;

	private LayoutInflater mInflater;

	private int mAdapterLayoutId;

	public CommonAdapter(Context context, List<T> mList, int layoutId) {
		mData = mList;
		mInflater = LayoutInflater.from(context);
		mAdapterLayoutId = layoutId;
	}

	public CommonAdapter(Context context, T[] mList, int layoutId) {
		if (mList != null) {
			mData = Arrays.asList(mList);
		}
		mInflater = LayoutInflater.from(context);
		mAdapterLayoutId = layoutId;
	}

	public CommonAdapter(Context context, int layoutId) {
		mInflater = LayoutInflater.from(context);
		mAdapterLayoutId = layoutId;
	}


	public final void notifyDataSetChanged(T[] data) {
		if (data != null) {
			mData = Arrays.asList(data);
		} else {
			mData = null;
		}
		notifyDataSetChanged();

	}

	public final void notifyDataSetChanged(List<T> data) {
		mData = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mInflater, convertView, parent,
					mAdapterLayoutId);
		convert(viewHolder, getItem(position),position);
		return viewHolder.getConvertView();

	}

	public abstract void convert(ViewHolder helper, T data,int position);

}
