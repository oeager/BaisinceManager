/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bison.app.ui.assist;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class ViewPagerAdapter<T> extends PagerAdapter {

    private Context context;

    private LayoutInflater mInflater;

    private final List<T> mList = new ArrayList<>();

    public ViewPagerAdapter(Context context) {
        this(context,(List)null);
    }

    public ViewPagerAdapter(Context context, T... mListData) {
        this(context, Arrays.asList(mListData));
    }

    public ViewPagerAdapter(Context context, List<T> mListData) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        if(mListData!=null){
            mList.addAll(mListData);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = onCreatePagerView(mInflater,container,position);
        container.addView(itemView);
        return itemView;
    }

    protected abstract View onCreatePagerView(LayoutInflater inflater,ViewGroup container,int position);


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public Context getContext(){
        return context;
    }

    public List<T> getListData(){
        return mList;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}