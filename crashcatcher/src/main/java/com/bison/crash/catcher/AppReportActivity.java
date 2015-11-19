package com.bison.crash.catcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by oeager on 2015/11/18.
 * email:oeager@foxmail.com
 */
public class AppReportActivity extends Activity {

    private TextView titleTex;

    private TextView descriptionTex;

    private TextView logReviewTex;

    private Button negativeButton;

    private Button positiveButton;

    final static String TITLE = "title";

    final static String DESCRIPTION = "description";

    private Class<?> cls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_crash_layout);
        initComponents();
        initComponentsData();
    }


    void initComponents() {
        setFinishOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        titleTex = (TextView) findViewById(R.id.title);
        descriptionTex = (TextView) findViewById(R.id.description);
        logReviewTex = (TextView) findViewById(R.id.logReview);
        negativeButton = (Button) findViewById(R.id.negativeBtn);
        positiveButton = (Button) findViewById(R.id.positiveBtn);
        String logText = getString(R.string.default_report_log);
        SpannableString spannableString = new SpannableString(logText);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(AppReportActivity.this, cls);
                i.putExtras(getIntent().getExtras());
                startActivity(i);
                finish();
            }
        }, 0, logText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(0xff777777),0, logText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        logReviewTex.setText(spannableString);
        logReviewTex.setMovementMethod(LinkMovementMethod.getInstance());
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogReportService.start(getApplicationContext(),getIntent().getExtras());
                finish();
            }
        });
    }

    void initComponentsData() {
        Bundle intent = getIntent().getExtras();
        String title = intent.getString(TITLE);
        String description = intent.getString(DESCRIPTION);
        titleTex.setText(title);
        descriptionTex.setText(description);
        cls = (Class<?>) intent.getSerializable(CrashCatcher.LOG_VIEW);
    }
}
    