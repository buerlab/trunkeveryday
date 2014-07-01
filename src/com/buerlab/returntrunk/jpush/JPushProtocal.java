package com.buerlab.returntrunk.jpush;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhongqiling on 14-6-19.
 */
public class JPushProtocal {

    static public final int JPUSH_ERROR = -1;
    static public final int JPUSH_PHONE_CALL = 1;
    static public final int BILL_VISITED = 2;

    public int code = -1;
    public String msg = "not init";

    public JPushProtocal(String source){

        try{
            JSONObject json = new JSONObject(source);
            code = json.getInt("code");
            msg = json.getString("msg");
        }catch (JSONException e){
            code = -1;
            msg = "msg parse error";
        }

    }
}
