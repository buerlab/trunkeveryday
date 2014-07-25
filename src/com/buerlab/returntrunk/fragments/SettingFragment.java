package com.buerlab.returntrunk.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.activities.BaiduMapActivity;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.activities.UserCompleteDataActivity;
import com.buerlab.returntrunk.dialogs.AddCommentDialog;
import com.buerlab.returntrunk.models.Setting;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.EventLogUtils;
import org.jraf.android.backport.switchwidget.Switch;

/**
 * Created by zhongqiling on 14-6-4.
 */

public class SettingFragment extends BaseFragment{

    private static final String TAG = "SettingFragment";

    Button logoutBtn;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.setting_frag, container, false);
//        init();

//        final ToggleButton notifyBtn = (ToggleButton)mView.findViewById(R.id.setting_notify);
//        notifyBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                NetService service = new NetService(self.getActivity());
//                service.switchSetting(Setting.PushSetting, isChecked, new NetService.NetCallBack() {
//                    @Override
//                    public void onCall(NetProtocol result) {
//                        if(result.code == NetProtocol.SUCCESS){
//                            Toast toast = Toast.makeText(self.getActivity(), "成功", 2);
//                            toast.show();
//                        }
//                    }
//                });
//            }
//        });
//
//        ToggleButton locateBtn = (ToggleButton)mView.findViewById(R.id.setting_locate);
//        locateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                NetService service = new NetService(self.getActivity());
//                service.switchSetting(Setting.LocateSetting, isChecked, new NetService.NetCallBack() {
//                    @Override
//                    public void onCall(NetProtocol result) {
//                        if(result.code == NetProtocol.SUCCESS){
//                            Toast toast = Toast.makeText(self.getActivity(), "成功", 2);
//                            toast.show();
//                        }
//                    }
//                });
//            }
//        });

        Switch pushBtn = (Switch)mView.findViewById(R.id.setting_push);


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

        mView.findViewById(R.id.get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(self.getActivity(),BaiduMapActivity.class);
                startActivity(i);
            }
        });

        mView.findViewById(R.id.exit_confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
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

    @Override
    public void onShow(){
        if(Utils.getVersionType(self.getActivity()).equals("driver")){
            EventLogUtils.EventLog(self.getActivity(), EventLogUtils.tthcc_driver_setting_enterFragment);
        }else {
            //TODO 货主版
        }
    }
}