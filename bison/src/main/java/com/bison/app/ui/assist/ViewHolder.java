package com.bison.app.ui.assist;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	private final SparseArray<View> mViews;
	private View mConvertView;

	private ViewHolder(LayoutInflater inflater,ViewGroup parent, int layoutId) {
		this.mViews = new SparseArray<View>();
		mConvertView = inflater.inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}

	private ViewHolder (View initView){
		this.mViews = new SparseArray<View>();
		mConvertView =initView;
		mConvertView.setTag(this);
	}
	/**
	 * 拿到一个ViewHolder对象
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @return
	 */
	public static ViewHolder get(LayoutInflater inflater, View convertView,ViewGroup parent, int layoutId) {
		if (convertView == null) {
			return new ViewHolder(inflater, parent, layoutId);
		}
		return (ViewHolder) convertView.getTag();
	}
	public static ViewHolder get(View convertView,View initView) {
		if (convertView == null) {
			return new ViewHolder(initView);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对应的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}
	
	public ViewHolder setTextSize(int viewId,float size ){
		TextView view = getView(viewId);
		view.setTextSize(size);
		return this;
	}
	
	public ViewHolder setTextColor(int viewId,int color){
		TextView view = getView(viewId);
		view.setTextColor(color);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}
	public ViewHolder setImageViewDrawable(int viewId, Drawable d) {
		ImageView view = getView(viewId);
		view.setImageDrawable(d);
		return this;
	}
	public ViewHolder setBackground(int viewId,Drawable d){
		getView(viewId).setBackgroundDrawable(d);
		return this;
	}
	public ViewHolder setBackgroundColor(int viewId,int color){
		getView(viewId).setBackgroundColor(color);
		return this;
	}
	public ViewHolder setBackground(int viewId,int res){
		getView(viewId).setBackgroundResource(res);
		return this;
	}

	public ViewHolder setVisibility(int viewId,int visibilty){
		getView(viewId).setVisibility(visibilty);
		return this;
	}
	public ViewHolder setEnabled(int viewId,boolean isEnable){
		getView(viewId).setEnabled(isEnable);
		return this;
	}

}
