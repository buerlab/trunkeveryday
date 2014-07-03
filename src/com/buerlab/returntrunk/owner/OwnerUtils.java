package com.buerlab.returntrunk.owner;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.buerlab.returntrunk.driver.activities.initDriverActivity;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.owner.activities.OwnerLoginActivity;
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

                Intent intent = new Intent(activity, OwnerLoginActivity.class);
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
        }else{
            from.startActivity(new Intent(from, OwnerMainActivity.class));
            from.finish();
        }
    }
}
