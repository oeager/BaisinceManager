package com.bison.support.v7;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public abstract class RecycleViewAdapterWrapper <VH extends RecyclerView.ViewHolder,T>  extends RecyclerView.Adapter<VH> {

    private final List<T> mListData = new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    private Context context;


    public RecycleViewAdapterWrapper(Context context) {
        this(context, null);
    }

    public RecycleViewAdapterWrapper(Context context, List<T> list) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        if (list != null) {
            mListData.addAll(list);
        }
    }
    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecycleViewType.VIEW_TYPE_HEADER) {
            return onCreateRecyclerHeader(mLayoutInflater, parent);
        }
        if (viewType == RecycleViewType.VIEW_TYPE_FOOTER) {
            return onCreateRecyclerFooter(mLayoutInflater, parent);
        }
        return onCreateRecycleItemView(mLayoutInflater, parent, viewType);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        int realPosition = calcDataPosition(position);//realPosition小于0时，为非content数据,Don't Mind it
        if(realPosition<0){
            return;
        }
        T t = mListData.get(realPosition);
        onBindItemViewData(holder, t,realPosition);
    }

    @Override
    public final int getItemViewType(int position) {
        final boolean hasHeader = hasHeader();
        final boolean hasFooter = hasFooter();
        if (position == 0 && hasHeader) {
            return RecycleViewType.VIEW_TYPE_HEADER;
        }
        if (hasFooter && position == getItemCount() - 1) {
            return RecycleViewType.VIEW_TYPE_FOOTER;
        }
        int realPosition = hasHeader ? position - 1 : position;

        return generateViewType(position, realPosition, mListData.get(realPosition));
    }

    @Override
    public int getItemCount() {
        int itemCount = mListData.size();
        if (hasHeader()) {
            itemCount++;
        }
        if (hasFooter()) {
            itemCount++;
        }
        return itemCount;
    }

    private int calcDataPosition(int itemPosition) {
        final boolean hasHeader = hasHeader();
        int realPosition = hasHeader ? itemPosition - 1 : itemPosition;
        if (realPosition > mListData.size() - 1) {
            return -1;
        }
        return realPosition;

    }

    public void notifyDataSetChanged(boolean keepOld,T...newData){
        notifyDataSetChanged(keepOld,Arrays.asList(newData));
    }

    public void notifyDataSetChanged(boolean keepOld,List<T> newData){
        if(!keepOld){
            mListData.clear();
        }
        if(newData!=null){
            mListData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    public void clearRecyclerData(){
        mListData.clear();
    }

    protected Context getContext(){
        return context;
    }

    public List<T> getListData(){
        return mListData;
    }

    protected VH onCreateRecyclerHeader(LayoutInflater layoutInflater, ViewGroup parent) {
        return null;
    }

    protected VH onCreateRecyclerFooter(LayoutInflater layoutInflater, ViewGroup parent) {
        return null;
    }

    protected int generateViewType(int itemPosition,int dataPosition,T t) {
        return RecycleViewType.VIEW_TYPE_CONTENT;
    }


    protected abstract boolean hasHeader();

    protected abstract boolean hasFooter();

    protected abstract VH onCreateRecycleItemView(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    protected abstract void onBindItemViewData(VH holder, T data,int dataPosition);


}
    