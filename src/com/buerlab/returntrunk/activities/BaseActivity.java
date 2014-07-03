package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.TextView;
import com.buerlab.returntrunk.R;

/**
 * Created by teddywu on 14-6-19.
 */
public class BaseActivity extends FragmentActivity {

    public static BaseActivity currActivity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        currActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearReference();
    }

    private void clearReference(){
        if(currActivity != null && currActivity.equals(this)){
            currActivity = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.empty, menu); //为了占位
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void setActionBarLayout(String title){
        setActionBarLayout(title,false);
    }

    public void setActionBarLayout(String title,boolean withBack){
        ActionBar actionBar = getActionBar();
        if( null != actionBar ){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(title);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);

            if(withBack){
                actionBar.setLogo(R.drawable.back);
            }
            LayoutInflater inflator = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.actionbar_custom, null);
            ((TextView)v.findViewById(R.id.actionbar_text)).setText(title);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            actionBar.setCustomView(v,layout);
        }
    }
}
