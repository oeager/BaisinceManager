package com.bison;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bison.app.components.activities.BaseAppActivity;
import com.bison.app.ui.refresh.SwipeMode;
import com.bison.app.ui.refresh.SwipeRefreshLayout;


/**
 * Created by oeager on 2015/8/18 0018.
 * email: oeager@foxmail.com
 */
public class SwipeRefreshLayoutActivity extends BaseAppActivity {

    private SwipeRefreshLayout layout2;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
//        showProgressDialog(null);
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    public void initComponents() {
        listView = (ListView) findViewById(R.id.listview);
        layout2= (SwipeRefreshLayout) findViewById(R.id.swipe);
        layout2.setMode(SwipeMode.TOP);

        String [] strs = new String[30];
        for (int i = 0 ;i<strs.length;i++){
            strs[i] = "SwipeRefreshLayout:String-"+i;
        }
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strs));
    }

    @Override
    public void registerComponentListeners() {
        layout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipeMode mode) {
                layout2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout2.setRefreshing(false);
                    }
                },3000);
            }
        });
    }
}
