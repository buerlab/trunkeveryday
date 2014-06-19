package com.buerlab.returntrunk.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

/**
 * Created by teddywu on 14-6-19.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}


