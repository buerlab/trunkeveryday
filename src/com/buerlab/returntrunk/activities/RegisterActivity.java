package com.buerlab.returntrunk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


/**
 * Created by zhongqiling on 14-5-28.
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    EditText phoneNumEdit;
    EditText regCodeEdit;
    Button registerBtn;
    Button sendRegCodeBtn;
    NetService mService;
    final BaseActivity self = this;

//    TODO 1分钟之后再验证

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_activity);
        init();
    }


    private void init(){
        setActionBarLayout("注册", WITH_NONE);
        phoneNumEdit = (EditText)findViewById(R.id.phonenum_edittext);
        regCodeEdit = (EditText)findViewById(R.id.regcode_edittext);
        registerBtn = (Button)findViewById(R.id.register_btn);
        sendRegCodeBtn = (Button)findViewById(R.id.send_reg_code);

        mService = new NetService(this);
    }

    public void sendRegCode(View v){
        String phonenum = phoneNumEdit.getText().toString();
        if(phonenum.length()<=0){
            Utils.showToast(this,"请输入手机号码");
            return;
        }

        if(phonenum.length()!=11){
            //TODO 删掉手机号码，聚焦
            Utils.showToast(this,"手机号码格式错误，请重新输入");
            return;
        }

        mService.getRegCode(phonenum,new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    Utils.showToast(self,"验证码短信发送中...");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_login:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                ：例如现在的栈情况为：A B C D 。D此时通过intent跳转到B，如果这个intent添加FLAG_ACTIVITY_CLEAR_TOP 标记，则栈情况变为：A B。如果没有添加这个标记，则栈情况将会变成：A B C D B。
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyRegCode(View v){

        String phonenum = phoneNumEdit.getText().toString();
        String regCode = regCodeEdit.getText().toString();


        if(phonenum.length()<=0){
            Utils.showToast(this,"请输入手机号码");
            return;
        }

//        if(regCode.length()<=0){
//            Utils.showToast(this,"请输入验证码");
//            return;
//        }
        if(phonenum.length()!=11){
            //TODO 删掉手机号码，聚焦
            Utils.showToast(this,"手机号码格式错误，请重新输入");
            return;
        }

        String versionType = Utils.getVersionType(this);
        if(versionType.equals( "driver")){
            Intent intent = new Intent(self,InitDriverActivity.class);
            intent.putExtra("phonenum",phonenum);
            startActivity(intent);
        }else if(versionType.equals( "owner")) {
            Intent intent = new Intent(self,InitOwnerActivity.class);
            intent.putExtra("phonenum",phonenum);
            startActivity(intent);
        }else {
            Utils.showToast(this,"versonType error");
            return;
        }



//        if(regCode.length()!=6){
//            //TODO 删掉验证码，聚焦
//            Utils.showToast(this,"手机号码格式错误，请重新输入");
//            return;
//        }
//
//        mService.verifyRegCode(phonenum, regCode, new NetService.NetCallBack() {
//            @Override
//            public void onCall(NetProtocol result) {
//                if (result.code == NetProtocol.SUCCESS) {
//                    if (result.data.has("ret")) {
//                        try {
//                            if (result.data.getBoolean("ret")) {
//                                Utils.showToast(self,"验证成功");
//                                Intent intent = new Intent(self,InitDriverActivity.class);
//                                startActivity(intent);
//
//                            }else {
//                                Utils.showToast(self,"验证失败");
//                            }
//                        }catch (Exception e){
//                            Log.e("TAG",e.toString());
//                        }
//                    }
//                }else {
//                    DriverUtils.defaultNetProAction(self,result);
//                }
//            }
//            });
        }


    }