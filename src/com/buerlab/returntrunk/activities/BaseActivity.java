package com.buerlab.returntrunk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

}
