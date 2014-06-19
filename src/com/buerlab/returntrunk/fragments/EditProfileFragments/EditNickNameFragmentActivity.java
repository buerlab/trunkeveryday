package com.buerlab.returntrunk.fragments.EditProfileFragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.buerlab.returntrunk.MainActivity;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.fragments.ProfileFragment;

/**
 * Created by zhongqiling on 14-6-17.
 */
public class EditNickNameFragmentActivity extends EditProfileBaseFragmentActivity {
    private static final String TAG = "EditNickNameFragmentActivity" ;
    public static final int EDIT_OK = 1;
    public static final int EDIT_FAIL = 2;
    View mRoot;
    ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_nickname);
        init();

//        setResult(20,  getIntent());
//        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("编辑昵称");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent a = new Intent(this,ProfileFragment.class);
                setResult(20);
                finish();

                //not work why?
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}