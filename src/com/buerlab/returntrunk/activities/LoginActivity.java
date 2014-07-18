package com.buerlab.returntrunk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.controls.MainController;
import com.buerlab.returntrunk.driver.DriverUtils;
import com.buerlab.returntrunk.jpush.JPushUtils;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.owner.OwnerUtils;

import com.buerlab.returntrunk.utils.EventLogUtils;
import com.umeng.analytics.AnalyticsConfig;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;



/**
 * Created by zhongqiling on 14-5-28.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    EditText userText;
    EditText pswText;
    Button loginbtn;
    final Activity self = this;
    NetService service;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.login_activity);
        userText = (EditText)findViewById(R.id.login_user_input);
        pswText = (EditText)findViewById(R.id.login_psw_input);
        loginbtn = (Button)findViewById(R.id.login_confirm_btn);

        if(Utils.getVersionType(this).equals("driver")){
            AnalyticsConfig.setAppkey("53c5184156240bb4720f0f39");
        }else {
            //TODO 货主版
        }

        service = new NetService(this);
        setActionBarLayout("登录",WITH_NONE);

        Utils.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean phonenumVerified(String phonenum){
        if(phonenum.length() == 11){
            return true;
        }else {
            return  false;
        }
    }

    public void login(){

        if(Utils.getVersionType(this).equals("driver")){
            EventLogUtils.EventLog(this,EventLogUtils.tthcc_driver_login_btn);
        }else {
            //TODO 货主版
        }
        if(userText.getText()==null || pswText.getText()==null){
            return;
        }
        String userTextStr = userText.getText().toString();
        String pswTextStr = pswText.getText().toString();

        if(userTextStr.length()==0){
            Utils.showToast(this,"请输入手机号码");
            return;
        }

        if(!phonenumVerified(userTextStr)){
            Utils.showToast(this,"请输入有效的手机号码");
            return;
        }

        if(pswTextStr.length()==0){
            Utils.showToast(this,"请输入密码");
            return;
        }



        service.login(userTextStr, pswTextStr, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    if(Utils.getVersionType(self).equals("driver")){
                        EventLogUtils.EventLog(self,EventLogUtils.tthcc_driver_login_btn_success);
                    }else {
                        //TODO 货主版
                    }
                    JSONObject data = result.data;
                    try{
                        User.getInstance().initUser(data.getJSONObject("user"));
                        MainController.shared().sync(data.getJSONObject("control"));
                    }catch (JSONException e){
                        Toast toast = Toast.makeText(self, "userdata init fail!!", 2);
                        toast.show();
                    }
//                    SharedPreferences pref = self.getSharedPreferences(self.getString(R.string.app_name), 0);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("userId", User.getInstance().userId);
//                    editor.commit();
                    Utils.setGlobalData(self,"userId",User.getInstance().userId);
                    JPushUtils.registerAlias(self, User.getInstance().userId);

                    String versionType = Utils.getVersionType(self);
                    if(versionType.equals( "driver")){
                        DriverUtils.safeSwitchToMainActivity(self);
                        finish();
                    }else if(versionType.equals( "owner")) {
                        OwnerUtils.safeSwitchToMainActivity(self);
                        finish();
                    }else {
                        Utils.showToast(self,"versonType error");

                    }
//                    DriverUtils.safeSwitchToMainActivity(self);
                }else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }
}