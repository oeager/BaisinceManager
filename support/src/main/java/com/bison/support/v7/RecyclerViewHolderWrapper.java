package com.bison.support.v7;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

    public void setText(@IdRes int id,CharSequence s){
        TextView textView = find(id);
        textView.setText(s);
    }

    public void setText(@IdRes int id,@StringRes int  s){
        TextView textView = find(id);
        textView.setText(s);
    }

    public void setBg(@IdRes int id,int color){
        find(id).setBackgroundColor(color);
    }

    public void setBg(@IdRes int id,Drawable d){
        find(id).setBackgroundDrawable(d);
    }

    public void setBgRes(@IdRes int id,int res){
        find(id).setBackgroundResource(res);
    }

    public void setSrc(@IdRes int id,int res){
        ImageView imageView = find(id);
        imageView.setImageResource(res);
    }
    public void setSrc(@IdRes int id,Bitmap bm){
        ImageView imageView = find(id);
        imageView.setImageBitmap(bm);
    }
    public void setSrc(@IdRes int id,Drawable res){
        ImageView imageView = find(id);
        imageView.setImageDrawable(res);
    }
    public void setImageLevel(@IdRes int id,int level){
        ImageView imageView = find(id);
        imageView.setImageLevel(level);
    }

    public void setEnable(@IdRes int id,boolean enable){
        find(id).setEnabled(enable);
    }

    public void setClickable(@IdRes int id,boolean clickable){
        find(id).setClickable(clickable);
    }

    public void setChecked(@IdRes int id,boolean checked){
        View v =find(id);
        if(v instanceof CheckBox){
            ((CheckBox)v).setChecked(checked);
        }
        if(v instanceof RadioButton){
            ((RadioButton)v).setChecked(checked);
        }
    }


}
    