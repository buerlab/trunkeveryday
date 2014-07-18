package com.buerlab.returntrunk.activities;


import android.view.MenuItem;
import com.buerlab.returntrunk.R;

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
