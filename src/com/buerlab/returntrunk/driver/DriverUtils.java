package com.buerlab.returntrunk.driver;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.driver.activities.*;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.owner.activities.OwnerMainActivity;

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
                String tips ="";
                if(result.msg!=null && result.msg.length() > 0){
                    tips = result.msg;
                }else{
                    tips= "http request error, code:"+result.code+"msg:"+result.msg;
                }

                Utils.showToast(activity.getApplicationContext(), tips);
            }
        }

    }

    static public void safeSwitchToMainActivity(Activity from){
//        if(User.getInstance().nickName.isEmpty()){
//            from.startActivity(new Intent(from, InitDriverActivity.class));
//            from.finish();
        if(User.getInstance().trunks.isEmpty()){
            Intent intent = new Intent(from,AddTrunkActivity.class);
            intent.putExtra("enterByLogin",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            from.startActivity(intent);

//            from.finish();
        }else{
            Intent intent = new Intent(from, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("without_splash",true);
            from.startActivity(intent);
//            from.finish();
        }
    }
}
