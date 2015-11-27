package com.bison;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.bison.app.components.fragments.AlertDialogFragment;

/**
 * Created by oeager on 2015/11/23.
 * email:oeager@foxmail.com
 */
public class AlertDialogFragmentTestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
    }

    public void onAction(View v){
         AlertDialogFragment.newInstance(android.R.drawable.ic_dialog_info,"title","message","pos","neg").show(this, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 if(which==DialogInterface.BUTTON_POSITIVE){
                     Toast.makeText(AlertDialogFragmentTestActivity.this,"Pos",Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(AlertDialogFragmentTestActivity.this,"Neg",Toast.LENGTH_SHORT).show();
                 }

             }
         });
    }


}
    