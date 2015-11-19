package com.bison;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bison.app.components.fragments.BaseFragment;

/**
 * Created by oeager on 2015/11/13.
 * email:oeager@foxmail.com
 */
public class TestFragment extends BaseFragment<Activity> {


    private final static int duration = 5000;

    @Override
    protected void initComponents(View createView, Bundle savedInstanceState) {
        TextView textView = (TextView) createView.findViewById(R.id.test);
        textView.setText(getArguments().getString("key"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOnLoading(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(duration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showOnEmpty(true);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void initComponentsData(Bundle savedInstanceState) {
    }

    @Override
    public void onGetEmptyEvent() {
        showOnLoading(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showOnLoadFail(1, true);
                    }
                });
            }
        }).start();

    }

    @Override
    public void onGetErrorEvent(final int errorType) {
        showOnLoading(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       showOnBindData(true);
                    }
                });
            }
        }).start();
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

    @Override
    protected void onShowToUserFirst() {
        showOnLoading(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      showOnBindData(true);
                    }
                });
            }
        }).start();
    }
}
    