package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by teddywu on 14-6-17.
 */
public class EditNickNameActivity extends BackBaseActivity {
    private static final String TAG = "EditNickNameActivity" ;
    ActionBar mActionBar;

    EditText mNickEdit ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_nickname);
        init();
        setActionBarLayout("修改称呼");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        mNickEdit = (EditText)findViewById(R.id.edit_nickname);
        mNickEdit.setText(User.getInstance().nickName);
//        mNickEdit.setSelectAllOnFocus(true);
    }


    public void save(View i)
    {
        final String nickname = mNickEdit.getText()==null? "":mNickEdit.getText().toString();
        if(nickname.length()==0){
            Utils.showToast(this,"请输入你的称呼");

            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("nickName", nickname);
        NetService service = new NetService(this);
        final Activity self = this;
        service.setUserData(map, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().nickName = nickname;
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
                    EventCenter.shared().dispatch(evt);
                    finish();
                }
                else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }





}