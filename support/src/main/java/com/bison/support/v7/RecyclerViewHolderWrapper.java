package com.bison.support.v7;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class RecyclerViewHolderWrapper extends RecyclerView.ViewHolder {


    public RecyclerViewHolderWrapper(View itemView) {
        super(itemView);
    }

    public <T extends View> T find(@IdRes int id){
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
    