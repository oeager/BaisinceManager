package com.bison;

import android.os.Bundle;

import com.bison.app.components.activities.BaseAppActivity;
import com.bison.app.ui.swipeback.SwipeConfiguration;


/**
 * Created by oeager on 2015/8/17 0017.
 * email: oeager@foxmail.com
 */
public class Login extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        supportSwipeBack(SwipeConfiguration.VERTICAL, false);
    }


    @Override
    public void initTitleBar() {

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void registerComponentListeners() {

    }
}
