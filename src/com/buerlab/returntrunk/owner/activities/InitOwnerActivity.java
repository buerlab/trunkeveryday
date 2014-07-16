package com.buerlab.returntrunk.owner.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.PickAddrDialog;
import com.buerlab.returntrunk.driver.DriverUtils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.jpush.JPushUtils;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.owner.OwnerUtils;
import com.buerlab.returntrunk.utils.Address;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class InitOwnerActivity extends BaseActivity implements EventCenter.OnEventListener {

    private static final String TAG = "InitOwnerActivity";

    private TextView homeLocationText = null;
    private LinearLayout homeLocationBtn = null;
//    final private InitDriverActivity self = this;

    private String phonenum;

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
        setContentView(R.layout.init_user_activity);
        setActionBarLayout("注册",WITH_BACK);

        if(!AssetManager.shared().hasInit){
            AssetManager.shared().init(getApplicationContext());
        }
        Utils.init(this);

        phonenum = getIntent().getStringExtra("phonenum");

        homeLocationText = (TextView)findViewById(R.id.init_user_homelocation_text);
        homeLocationBtn = (LinearLayout)findViewById(R.id.init_user_homelocation_btn);
        Button trunkBtn = (Button)findViewById(R.id.init_user_confirm);
        trunkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();
            }
        });
        final BaseActivity self = this;
        homeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickAddrDialog dialog2 = new PickAddrDialog(self,R.style.dialog);
                dialog2.show();
            }
        });

    }

    public void pick(){
        EditText nicknameText = (EditText)findViewById(R.id.init_user_nickname);
        EditText passWordText = (EditText)findViewById(R.id.init_user_password);

        final String nickname = nicknameText.getText().toString();
        final String homeLocation =homeLocationText.getText().toString();
        final String password = passWordText.getText().toString();

        if(password.length()==0){
            Utils.showToast(this,"请输入密码");
            return;
        }

        if(password.length()>16){
            Utils.showToast(this,"密码长度不超过16位");
            return;
        }

        if(nickname.length()==0){
            Utils.showToast(this,"请输入您的称呼，不超过4个字");
            return;
        }
        if(nickname.length()>4){
            Utils.showToast(this,"称呼少于4个字就可以了");
            return;
        }

        if(homeLocation == null){
            Utils.showToast(this,"请输入常住地");
            return;
        }




        final NetService service = new NetService(this);
        final Activity self = this;

        service.register(phonenum, password, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if (result.code == NetProtocol.SUCCESS) {
                    //保存cookie登录态
                    User.getInstance().initUser(result.data);
                    SharedPreferences pref = self.getSharedPreferences(self.getString(R.string.app_name), 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userId", User.getInstance().userId);
                    editor.commit();
                    JPushUtils.registerAlias(self, User.getInstance().userId);

                    //注册好了把信息填一下
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("nickName", nickname);
                    map.put("homeLocation",homeLocation);

                    service.setUserData(map, new NetService.NetCallBack() {
                        @Override
                        public void onCall(NetProtocol result) {
                            if(result.code == NetProtocol.SUCCESS){
                                User.getInstance().setUserType(User.USERTYPE_OWNER);
                                User.getInstance().nickName = nickname;
                                User.getInstance().homeLocation = homeLocation;
//                                startActivity(new Intent(self, MainActivity.class));
//                                finish();
                                OwnerUtils.safeSwitchToMainActivity(self);
                            }
                            else{
                                OwnerUtils.defaultNetProAction(self, result);
                            }
                        }
                    });
                } else {
                    OwnerUtils.defaultNetProAction(self,result);
                }
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        EventCenter.shared().addEventListener(DataEvent.ADDR_CHANGE, this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        EventCenter.shared().removeEventListener(DataEvent.ADDR_CHANGE, this);
    }

    public void onEventCall(DataEvent e){

        if(e.type.equals(DataEvent.ADDR_CHANGE)){
            List<String> data = (List<String>)e.data;
            Address addr = new Address(data);
            homeLocationText.setText(addr.toFullString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}