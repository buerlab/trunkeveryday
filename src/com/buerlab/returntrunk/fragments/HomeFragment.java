package com.buerlab.returntrunk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.buerlab.returntrunk.R;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class HomeFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_frag, container, false);
    }
}