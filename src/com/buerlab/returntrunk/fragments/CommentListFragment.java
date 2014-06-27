package com.buerlab.returntrunk.fragments;

//import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class CommentListFragment extends BaseFragment{

    View mView;
    private TextView tips = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.comments, container, false);
        tips = (TextView)mView.findViewById(R.id.send_bill_frag_tips);
        init();
        return  mView;
    }

    public void init(){

    }
}