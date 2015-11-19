package com.bison.support.v7;

import android.content.Context;

import java.util.List;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public abstract class BaseRecyclerViewAdapter <T> extends RecycleViewAdapterWrapper<RecyclerViewHolderWrapper,T> {

    public BaseRecyclerViewAdapter(Context context) {
        super(context);
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        super(context, list);
    }
}
    