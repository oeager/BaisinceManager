package com.bison;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bison.app.components.fragments.BaseFragment;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class TestFragment2 extends BaseFragment<Activity> {


    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

    @Override
    protected void initComponents(View createView, Bundle savedInstanceState) {

    }

    @Override
    protected void initComponentsData(Bundle savedInstanceState) {
//        showOnLoading(true);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                getContext().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showOnBindData(true);
//                    }
//                });
//            }
//        }).start();
    }

    @Override
    public void onGetEmptyEvent() {
        showOnLoading(false);
        Toast.makeText(getContext(),"onGetEmptyEvent",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGetErrorEvent(final int errorType) {
        showOnLoading(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOnLoadFail(1,true);
                        Toast.makeText(getContext(),"onGetErrorEvent:"+errorType,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }
}
    