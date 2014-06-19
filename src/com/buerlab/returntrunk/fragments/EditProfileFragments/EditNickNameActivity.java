package com.buerlab.returntrunk.fragments.EditProfileFragments;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import com.buerlab.returntrunk.MainActivity;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.fragments.ProfileFragment;

/**
 * Created by zhongqiling on 14-6-17.
 */
public class EditNickNameActivity extends EditProfileBaseActivity {
    private static final String TAG = "EditNickNameActivity" ;
    public static final int EDIT_OK = 1;
    public static final int EDIT_FAIL = 2;
    View mRoot;
    ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_nickname);
        init();
//        setActionBarLayout( R.layout.actionbar );
//        setResult(20,  getIntent());
//        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    public void onOptionSweep(MenuItem i)
    {
        finish();
    }
    public void onOptionScrub(MenuItem i)
    {
        finish();
    }
    public void onOptionVacuum(MenuItem i)
    {
        finish();
    }

//    public void setActionBarLayout( int layoutId ){
//        ActionBar actionBar = getActionBar();
//        if( null != actionBar ){
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle("编辑昵称");
//            actionBar.setDisplayShowHomeEnabled(true);
//            actionBar.setDisplayShowCustomEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(true);
//            LayoutInflater inflator = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflator.inflate(layoutId, null);
//            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            actionBar.setCustomView(v,layout);
//        }
//    }

     private void onClick(){

    }
}