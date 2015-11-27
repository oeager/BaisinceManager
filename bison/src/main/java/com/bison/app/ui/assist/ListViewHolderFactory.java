package com.bison.app.ui.assist;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by oeager on 2015/11/20.
 * email:oeager@foxmail.com
 */
public final class ListViewHolderFactory {

    private ListViewHolderFactory(){

    }

    public static class SimpleViewHolder extends ListAdapter.ViewHolder{

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }

        public <T> T find(@IdRes int id){
            return (T) itemView.findViewById(id);
        }

        public void setLabel(@IdRes int id,CharSequence c){
            TextView textView = find(id);
            textView.setText(c);
        }

        public void setLabel(@IdRes int id,@StringRes int res){
            TextView textView = find(id);
            textView.setText(res);
        }

        public void setImage(@IdRes int id,@DrawableRes int res){
            ImageView i = find(id);
            i.setImageResource(res);
        }

        public TextView castTextView(@IdRes int id){
            return find(id);
        }
        public ImageView castImageView(@IdRes int id){
            return find(id);
        }
    }

    public static class LayoutViewHolder extends SimpleViewHolder{

        public LayoutViewHolder(LayoutInflater mInflater,ViewGroup parent,@LayoutRes int layout) {
            super(mInflater.inflate(layout,parent,false));
        }
    }
}
    