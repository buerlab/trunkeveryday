package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.views.StarsViewWithText;

/**
 * Created by teddywu on 14-6-17.
 */
public class PersonDetailActivity extends EditProfileBaseActivity {
    private static final String TAG = "PersonDetailActivity" ;

    StarsViewWithText starsViewWithText;
    StarsViewWithText starsViewWithText2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail);
        init();
        setActionBarLayout("个人资料" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        Utils.init(this);
        starsViewWithText = (StarsViewWithText)findViewById(R.id.stars_view);
        starsViewWithText.setStar(2.8f);
        starsViewWithText.setSize(30);

        starsViewWithText2 = (StarsViewWithText)findViewById(R.id.stars_view2);
        starsViewWithText2.setStar(2.8f);


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