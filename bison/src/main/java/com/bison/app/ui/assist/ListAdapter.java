package com.bison.app.ui.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bison.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oeager on 2015/11/20.
 * email:oeager@foxmail.com
 */
public abstract class ListAdapter<VH extends ListAdapter.ViewHolder,T> extends BaseAdapter {

    private Context context;

    private LayoutInflater mInflater;

    private final List<T> mList = new ArrayList<>();

    public ListAdapter(Context context){
        this(context,(List)null);
    }

    public ListAdapter(Context context, T[] mList){
        this(context,Arrays.asList(mList));
    }

    public ListAdapter(Context context, List<T> mList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        if(mList!=null){
            this.mList.addAll(mList);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if(convertView==null){
            holder = onCreateViewHolder(mInflater,parent);
            convertView = holder.itemView;
            convertView.setTag(R.id.item_view_tag_id,holder);
        }else{
            holder = (VH) convertView.getTag(R.id.item_view_tag_id);
        }
        holder.setPosition(position);
        onBindViewHolder(holder,position);
        return holder.itemView;
    }

    //----define--
    protected abstract VH onCreateViewHolder(LayoutInflater inflater,ViewGroup parent) ;

    protected abstract void onBindViewHolder(VH holder, int position) ;

    public Context getContext(){
        return context;
    }

    public List<T> getListData(){
        return mList;
    }

    public void notifyDataSetChanged(boolean keepOld,List<T> newData) {
        if(!keepOld){
            mList.clear();
        }
        if(newData!=null){
            mList.addAll(newData);
        }
        notifyDataSetChanged();
    }
    public void notifyDataSetChanged(boolean keepOld,T... newData) {
        if(!keepOld){
            mList.clear();
        }
        if(newData!=null){
            mList.addAll(Arrays.asList(newData));
        }
        notifyDataSetChanged();
    }
    public void clearListData() {
       mList.clear();
    }

    public static class ViewHolder{

        public final View itemView;

        private int position;

        public ViewHolder(View itemView){

            this.itemView = itemView;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
    