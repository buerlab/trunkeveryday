package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.umeng.analytics.MobclickAgent;



/**
 * Created by zhongqiling on 14-5-28.
 */
public class FeedbackActivity extends BackBaseActivity {
    private static final String TAG = "FeedbackActivity";
    EditText feedback_str;
    Button feedback_btn;

    NetService mService;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feedback_acitivy);
        init();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

    private void init(){
        setActionBarLayout("用户反馈");
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
        if(feedback_str.getText()==null){
            return;
        }
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