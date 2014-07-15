package com.buerlab.returntrunk.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.baidu.mapapi.SDKInitializer;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.driver.DriverUtils;
import com.buerlab.returntrunk.driver.activities.InitDriverActivity;
import com.buerlab.returntrunk.jpush.JPushUtils;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.owner.OwnerUtils;
import com.buerlab.returntrunk.owner.activities.InitOwnerActivity;


/**
 * Created by zhongqiling on 14-5-28.
 */
public class LoginActivity extends BaseActivity {

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
                    User.getInstance().initUser(result.data);
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
                        return;
                    }
//                    DriverUtils.safeSwitchToMainActivity(self);
                }else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }
}