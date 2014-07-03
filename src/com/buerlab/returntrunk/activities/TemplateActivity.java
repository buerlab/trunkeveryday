package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.buerlab.returntrunk.R;

/**
 * Created by teddywu on 14-6-17.
 */
public class TemplateActivity extends EditProfileBaseActivity {
    private static final String TAG = "TemplateActivity" ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_home_location);
        init();
        setActionBarLayout("个人资料" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    public void onOptionSave(MenuItem i)
    {

    }

}