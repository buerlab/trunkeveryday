package com.buerlab.returntrunk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class PickUserTypeActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_usertype_activity);

        Button trunkBtn = (Button)findViewById(R.id.pick_usertype_trunk);
        trunkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick(User.USERTYPE_TRUNK);
            }
        });

        Button ownerBtn = (Button)findViewById(R.id.pick_usertype_owner);
        ownerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick(User.USERTYPE_OWNER);
            }
        });
    }

    public void pick(final String type){
        EditText nicknameText = (EditText)findViewById(R.id.pick_usertype_nickname);
        final String nickname = nicknameText.getText().toString();
        if(nickname.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "请输入你的称呼", 2);
            toast.show();
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("userType", type);
        map.put("nickName", nickname);
        NetService service = new NetService(this);
        final Activity self = this;
        service.setUserData(map, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().setUserType(type);
                    User.getInstance().nickName = nickname;

                    Utils.safeSwitchToMainActivity(self);
                }
                else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }

}