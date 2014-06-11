package com.buerlab.returntrunk;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.buerlab.returntrunk.net.NetProtocol;
import org.json.JSONException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by zhongqiling on 14-6-3.
 */
public class Utils {

    static public void defaultNetProAction(Activity activity, NetProtocol result){
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

    static public String tsToTimeString(String ts){
        try {
            Timestamp timestamp = new Timestamp(Long.parseLong(ts));
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            return format.format(timestamp);
        }catch (Exception e){
            return ts;
        }

    }

}
