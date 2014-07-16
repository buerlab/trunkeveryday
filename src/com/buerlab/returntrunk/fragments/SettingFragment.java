package com.buerlab.returntrunk.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.activities.UserCompleteDataActivity;
import com.buerlab.returntrunk.dialogs.*;
import com.buerlab.returntrunk.dialogs.AddCommentDialog;
import com.buerlab.returntrunk.driver.DriverUtils;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by zhongqiling on 14-6-4.
 */

public class SettingFragment extends BaseFragment{

    Button logoutBtn;
    View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.setting_frag, container, false);
//        init();
        logoutBtn = (Button)mView.findViewById(R.id.logout_confirm_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        mView.findViewById(R.id.see_my_complete_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeMyCompleteData(v);
            }
        });

        mView.findViewById(R.id.commment_myself).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.buerlab.returntrunk.dialogs.AddCommentDialog dialog = new AddCommentDialog(self.getActivity(),R.style.dialog);
                dialog.show();
            }
        });
        return  mView;
    }

    public void logout(View v){
        Activity activity = getActivity();
            Utils.clearGlobalData(getActivity());

            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();

            Toast toast = Toast.makeText(activity.getApplicationContext(), "已注销", 2);
            toast.show();
    }

    public void seeMyCompleteData(View v){
        Intent intent = new Intent(getActivity(), UserCompleteDataActivity.class);
        intent.putExtra("userId",User.getInstance().userId);
        intent.putExtra("getType",User.getInstance().getUserType());
        startActivity(intent);
    }

}