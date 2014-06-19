package com.buerlab.returntrunk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.jpush.JPushUtils;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zhongqiling on 14-5-28.
 */
public class LoginActivity extends Activity {
    public static final String addr = "http:127.0.0.1:8888";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        final EditText userText = (EditText)findViewById(R.id.login_user_input);
        final EditText pswText = (EditText)findViewById(R.id.login_psw_input);

        Button loginbtn = (Button)findViewById(R.id.login_confirm_btn);
        final Activity self = this;
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetService service = new NetService(self);
                final String username = userText.getText().toString();
                final String psw = pswText.getText().toString();
                if(username.length()>0 && psw.length()>0)
                {
                    service.register(username, psw, new NetService.NetCallBack() {
                        @Override
                        public void onCall(NetProtocol result) {
                            if(result.code == NetProtocol.SUCCESS || result.code == NetProtocol.USER_EXISTED_ERROR){
                                login();
                            }else{
                                Toast toast = Toast.makeText(self.getApplicationContext(), "login error:"+result.msg, 2);
                                toast.show();
                            }
                        }
                    });
                }else{
                    Toast toast = Toast.makeText(self.getApplicationContext(), "请输入有效的用户名", 2);
                    toast.show();
                }

            }
        });

    }

    public void login(){
        final EditText userText = (EditText)findViewById(R.id.login_user_input);
        final EditText pswText = (EditText)findViewById(R.id.login_psw_input);
        final Activity self = this;
        NetService service1 = new NetService(self);
        service1.login(userText.getText().toString(), pswText.getText().toString(), new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().initUser(result.data);
                    SharedPreferences pref = self.getSharedPreferences(self.getString(R.string.app_name), 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userId", User.getInstance().userId);
                    editor.commit();
                    JPushUtils.registerAlias(self, User.getInstance().userId);

                    Utils.safeSwitchToMainActivity(self);
                }else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }
}