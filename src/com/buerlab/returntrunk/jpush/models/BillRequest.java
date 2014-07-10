package com.buerlab.returntrunk.jpush.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhongqiling on 14-7-10.
 */
public class BillRequest {

    public String senderName = "";
    public String reqId = "";

    public BillRequest(JSONObject data) throws AssertionError{
        try{
            senderName = data.getString("senderName");
            reqId = data.getString("reqId");
        }catch (JSONException e){
            throw new AssertionError("data is invalid");
        }
    }
}
