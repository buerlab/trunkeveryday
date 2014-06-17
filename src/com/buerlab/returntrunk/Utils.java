package com.buerlab.returntrunk;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.buerlab.returntrunk.net.NetProtocol;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    static public void safeSwitchToMainActivity(Activity from){
        if(User.getInstance().getUserType().isEmpty()){
            from.startActivity(new Intent(from, PickUserTypeActivity.class));
            from.finish();
        }else if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK) && User.getInstance().trunks.isEmpty()){
            from.startActivity(new Intent(from, SetTrunkActivity.class));
            from.finish();
        }else{
            from.startActivity(new Intent(from, MainActivity.class));
            from.finish();
        }
    }


    static public List<JSONObject> extractArray(JSONObject data){
        List<JSONObject> result = new ArrayList<JSONObject>();

        try{
            for(int i = 0; ; i++) {
                if (!data.has(String.valueOf(i))) {
                    return result;
                }
                result.add(data.getJSONObject(String.valueOf(i)));
            }
        }catch (JSONException e){

            return null;
        }

    }

}
