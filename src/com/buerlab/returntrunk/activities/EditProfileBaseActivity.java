package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.view.MenuItem;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.BaseActivity;

/**
 * Created by teddywu on 14-6-19.
 */
public abstract class EditProfileBaseActivity extends BaseActivity {


    public void setActionBarLayout( int layoutId,String title ){
        ActionBar actionBar = getActionBar();
        if( null != actionBar ){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
//            LayoutInflater inflator = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflator.inflate(layoutId, null);
//            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            actionBar.setCustomView(v,layout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                onOptionSave(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void onOptionSave(MenuItem i);
}
