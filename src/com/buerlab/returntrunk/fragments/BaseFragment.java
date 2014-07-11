package com.buerlab.returntrunk.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import com.buerlab.returntrunk.R;

/**
 * Created by teddywu on 14-6-19.
 */
public class BaseFragment extends Fragment {

    protected BaseFragment self = this;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void onShow(){}

}


