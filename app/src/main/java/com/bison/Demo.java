package com.bison;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bison.app.components.activities.BaseAppActivity;


/**
 * Created by oeager on 2015/8/14 0014.
 * email: oeager@foxmail.com
 */
public class Demo extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void onAction(View v){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
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
