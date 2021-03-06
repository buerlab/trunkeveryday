package com.buerlab.returntrunk.driver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.driver.DriverUtils;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhongqiling on 14-6-11.
 */
public class SetTrunkActivity extends BaseActivity {

    private static final String TAG = "SetTrunkActivity";

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

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_trunk_activity);
        setActionBarLayout(WITH_NONE);
        final Activity self = this;
        Button confirmBtn = (Button)findViewById(R.id.set_trunk_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText typeText = (EditText)self.findViewById(R.id.set_trunk_type);
                String type =typeText.getText().toString();
                EditText lengthText = (EditText)self.findViewById(R.id.set_trunk_length);
                float length = Float.valueOf(lengthText.getText().toString());
                EditText loadText = (EditText)self.findViewById(R.id.set_trunk_load);
                float load = Float.valueOf(loadText.getText().toString());
                EditText lisenceText = (EditText)self.findViewById(R.id.set_trunk_licensePlate);
                String listence = lisenceText.getText().toString();
                final Trunk trunk = new Trunk(type, length, load, listence);

                NetService service = new NetService(self);
                service.addUserTrunk(trunk, new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if(result.code == NetProtocol.SUCCESS){
                            User.getInstance().addTrunk(trunk);

                            Toast toast = Toast.makeText(self, "添加货车成功！", 2);
                            toast.show();

                            DriverUtils.safeSwitchToMainActivity(self);
                        }else{
                            DriverUtils.defaultNetProAction(self, result);
                        }
                    }
                });
            }
        });
    }
}