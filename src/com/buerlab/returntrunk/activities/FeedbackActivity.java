package com.buerlab.returntrunk.activities;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.driver.activities.InitDriverActivity;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.owner.activities.InitOwnerActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by zhongqiling on 14-5-28.
 */
public class FeedbackActivity extends BaseActivity {
    private static final String TAG = "FeedbackActivity";
    EditText feedback_str;
    Button feedback_btn;

    NetService mService;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feedback_acitivy);
        init();
    }


    private void init(){
        setActionBarLayout("注册", WITH_NONE);
        feedback_str = (EditText)findViewById(R.id.feedback_str);
        feedback_btn = (Button)findViewById(R.id.feedback_btn);

        mService = new NetService(this);

        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeeback();
            }
        });
    }

    private void sendFeeback(){
        if (feedback_str.getText().length()==0){
            Utils.showToast(this,"请填写反馈信息");
            return;
        }

        if(feedback_str.getText().length()>10000){
            Utils.showToast(this,"反馈信息不能超过10000个字");
            return;
        }
        mService.addFeedBack(feedback_str.getText().toString(),new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    Utils.showToast(self,"感谢您的反馈！");
                    finish();
                }else {
                    Utils.defaultNetProAction(self,result);
                }
            }
        });
    }
    }