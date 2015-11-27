package com.bison;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bison.app.components.fragments.ProgressDialogFragment;
import com.developer.bsince.log.GOL;

public class MainActivity extends AppCompatActivity {

    private final static String TAG ="LIFE_CYCLE";

    private boolean hasCrated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressDialogFragment.newInstance("load").show(this);

    }
    public void showOnBindData(View v){
        System.out.print(""+(1/0));
    }

    public void showOnLoading(View v){
    }
    public void showOnEmpty(View v){
        }
    public void showOnError(View v){
        GOL.e("root==>"+R.id.root);
    }

    public static String isNull(Object o){
        if(o==null){
            return "null";
        }
        return "not null";
    }

    @Override
    protected void onPause() {
        super.onPause();
        GOL.tag(TAG).e("onPause-------------------------->"+",Configuration:"+isChangingConfigurations());
    }

    @Override
    protected void onStop() {
        super.onStop();
        GOL.tag(TAG).e("onStop-------------------------->"+",Configuration:"+isChangingConfigurations());
    }

    @Override
    protected void onResume() {
        super.onResume();
        GOL.tag(TAG).e("onResume-------------------------->"+",Configuration:"+isChangingConfigurations());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GOL.tag(TAG).e("onRestart-------------------------->"+",Configuration:"+isChangingConfigurations());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isFinishing()){
            hasCrated = true;
        }
        GOL.tag(TAG).e("onDestroy-------------------------->"+",Configuration:"+isChangingConfigurations()+",isFinish:"+isFinishing());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        GOL.tag(TAG).e("onConfigurationChanged-------------------------->");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        GOL.tag(TAG).e("onSaveInstanceState-------------------------->"+",Configuration:"+isChangingConfigurations());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GOL.tag(TAG).e("onRestoreInstanceState-------------------------->"+",Configuration:"+isChangingConfigurations());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
