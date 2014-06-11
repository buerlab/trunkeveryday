package com.buerlab.returntrunk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class EntryActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.entry_activity);

        NetService service = new NetService(this);
        final Activity self = this;
        service.quickLogin(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().initUser(result.data);
                    SharedPreferences pref = self.getSharedPreferences(self.getString(R.string.app_name), 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userId", User.getInstance().userId);
                    editor.commit();
                    if(User.getInstance().getUserType().length() == 0){
                        self.startActivity(new Intent(self, PickUserTypeActivity.class));
                    }else{
                        self.startActivity(new Intent(self, MainActivity.class));
                    }
                    self.finish();
                }
                else{
                    Intent intent = new Intent(self, LoginActivity.class);
                    self.startActivity(intent);
                    self.finish();
                }
            }
        });
    }
}