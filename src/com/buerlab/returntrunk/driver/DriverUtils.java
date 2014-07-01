package com.buerlab.returntrunk.driver;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.buerlab.returntrunk.driver.activities.initDriverActivity;
import com.buerlab.returntrunk.User;
import com.buerlab.returntrunk.driver.activities.LoginActivity;
import com.buerlab.returntrunk.driver.activities.MainActivity;
import com.buerlab.returntrunk.driver.activities.SetTrunkActivity;
import com.buerlab.returntrunk.net.NetProtocol;

/**
 * Created by zhongqiling on 14-7-1.
 */
public class DriverUtils {

    static public void defaultNetProAction(Activity activity, NetProtocol result){
        if(activity.getApplicationContext() != null){
            if(result.code == NetProtocol.AUTH_ERROR){
                Toast toast = Toast.makeText(activity.getApplicationContext(), "请先登录", 2);
                toast.show();

                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast toast = Toast.makeText(activity.getApplicationContext(),
                        "http request error, code:"+result.code+"msg:"+result.msg, 2);
                toast.show();
            }
        }

    }

    static public void safeSwitchToMainActivity(Activity from){
        if(User.getInstance().nickName.isEmpty()){
            from.startActivity(new Intent(from, initDriverActivity.class));
            from.finish();
        }else if(User.getInstance().trunks.isEmpty()){
            from.startActivity(new Intent(from, SetTrunkActivity.class));
            from.finish();
        }else{
            from.startActivity(new Intent(from, MainActivity.class));
            from.finish();
        }
    }
}
