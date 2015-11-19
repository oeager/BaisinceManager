package com.bison.crash.catcher;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by oeager on 2015/11/19.
 * email:oeager@foxmail.com
 */
public class LogViewActivity extends Activity{

    private TextView detailTex;

    private Button positiveBtn;

    private Button negativeBtn;

    private String logDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_log_view_layout);
        initTitleBar();
        initComponents();
        initComponentsData();
    }

    private void initTitleBar() {
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle(getString(R.string.send_report));
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xffffff));
    }

    private void initComponents() {
        detailTex = (TextView) findViewById(R.id.detail_message);
        negativeBtn = (Button) findViewById(R.id.negativeBtn);
        positiveBtn = (Button) findViewById(R.id.positiveBtn);
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogReportService.start(LogViewActivity.this,getIntent().getExtras());
            }
        });

    }

    private void initComponentsData() {
        logDetail = getIntent().getExtras().getString(CrashCatcher.CRASH_DATA);
        detailTex.setText(logDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,1,1,"复制");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case 1:
                ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText(null, logDetail));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
    