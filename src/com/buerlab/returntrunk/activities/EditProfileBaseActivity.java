package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.BaseActivity;

/**
 * Created by teddywu on 14-6-19.
 */
public abstract class EditProfileBaseActivity extends BaseActivity {

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

    public void setActionBarLayout(String title){
        setActionBarLayout(title,WITH_BACK);
    }
}
