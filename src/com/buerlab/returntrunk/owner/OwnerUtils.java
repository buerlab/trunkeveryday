package com.buerlab.returntrunk.owner;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.driver.activities.InitDriverActivity;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.owner.activities.OwnerMainActivity;

/**
 * Created by zhongqiling on 14-7-1.
 */
public class OwnerUtils {

    static public void defaultNetProAction(Activity activity, NetProtocol result){
        if(activity.getApplicationContext() != null){
            if(result.code == NetProtocol.AUTH_ERROR){
                Toast toast = Toast.makeText(activity.getApplicationContext(), "请先登录", 2);
                toast.show();

                Intent intent = new Intent(activity,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }else{
                String tips ="";
                if(result.msg!=null && !result.msg.isEmpty()){
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
//        }else{
            Intent intent = new Intent(from, OwnerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("without_splash",true);
            from.startActivity(intent);
            from.finish();
//        }
    }
}
