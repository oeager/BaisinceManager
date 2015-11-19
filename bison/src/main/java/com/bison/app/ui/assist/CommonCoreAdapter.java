package com.bison.app.ui.assist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class CommonCoreAdapter<T> extends BaseAdapter {

	private List<T> mData;

	protected Context mContext;


	public CommonCoreAdapter(Context context, List<T> mList) {
		mData = mList;
		mContext = context;
	}

	public CommonCoreAdapter(Context context, T[] mList) {
		if (mList != null) {
			mData = Arrays.asList(mList);
		}
		mContext = context;
	}

	public CommonCoreAdapter(Context context) {
		mContext = context;
	}


	public void notifyDataSetChanged(T[] data) {
		if (data != null) {
			mData = Arrays.asList(data);
		} else {
			mData = null;
		}
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged(List<T> data) {
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
		return getView(position,getItem(position),convertView);

	}
	public abstract View getView(int position,T data,View convertView);
	
//	private CommonCoreAdapter<String> demo = new CommonCoreAdapter<String>(mContext) {
//
//		@Override
//		public View getView(int position, String t, View convertView) {
//			TextView textview = new TextView(mContext);
//			textview.setId(1);
//			ViewHolder holder = ViewHolder.get(convertView, textview);
//			holder.setText(1, t);
//			return holder.getConvertView();
//		}
//	};
}
