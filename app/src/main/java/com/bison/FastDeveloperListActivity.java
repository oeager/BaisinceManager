package com.bison;

import android.content.CursorLoader;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.bison.app.components.activities.BaseAppActivity;
import com.bison.app.ui.assist.ListViewHolderFactory;
import com.bison.app.ui.assist.SimpleListAdapter;

/**
 * Created by oeager on 2015/11/23.
 * email:oeager@foxmail.com
 */
public class FastDeveloperListActivity extends BaseAppActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    public void initTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.model_list));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initComponents() {
        listView = (ListView) findViewById(android.R.id.list);
//        CursorLoader
    }

    @Override
    public void registerComponentListeners() {
        listView.setAdapter(new SimpleListAdapter<String>(this,getResources().getStringArray(R.array.model_list)) {
            @Override
            protected int getItemLayout() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            protected void onBindViewHolder(ListViewHolderFactory.LayoutViewHolder holder, String s) {
                holder.setLabel(android.R.id.text1,s);
            }
        });
    }
}
    