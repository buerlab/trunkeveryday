package com.buerlab.returntrunk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.buerlab.returntrunk.BillInvitationListAdapter;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.FeedbackActivity;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhongqiling on 14-5-31.
 */
public class AboutusFragment extends BaseFragment {


    View mRoot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.about_us, container, false);
        mRoot = fragView;
        init();
        return  fragView;
    }

    private void init(){
        mRoot.findViewById(R.id.user_feedback_wrapper).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), FeedbackActivity.class);
               startActivity(intent);
           }
       });
    }
}