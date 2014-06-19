package com.buerlab.returntrunk;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * Created by teddywu on 14-6-19.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
