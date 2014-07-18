package com.buerlab.returntrunk.models;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqiling on 14-5-30.
 */
public class Location {


    public String userId = "";
    public String latitude = "";
    public String longitude = "";
    public String prov = "";
    public String city = "";
    public String district = "";
    public String timestamp = "";


//    public Location(int _starNum, String _userType, String _commentTime, String _fromUserName, String _fromUserId, String _toUserId, String _billId, String _text, NickBarData _user){
//        starNum = _starNum;
//        userType = _userType;
//        commentTime = _commentTime;
//        fromUserName = _fromUserName;
//        fromUserId = _fromUserId;
//        toUserId = _toUserId;
//        billId = _billId;
//        text = _text;
//        mUser = _user;
//    }

    public Location(JSONObject object){
        try{
            if(object.has("userId")){
                this.userId = object.getString("userId");
            }
            if(object.has("latitude")){
                this.latitude = object.getString("latitude");
            }
            if(object.has("longitude")){
                this.longitude = object.getString("longitude");
            }
            if(object.has("prov")){
                this.prov = object.getString("prov");
            }
            if(object.has("city")){
                this.city = object.getString("city");
            }
            if(object.has("district")){
                this.district = object.getString("district");
            }
            if(object.has("timestamp")){
                this.timestamp = object.getString("timestamp");
            }

        }catch (JSONException e){
            Log.e("Location",e.toString());
        }

    }

//    public Map<String, String> toParmsMap(){
//        Map<String, String> parmsMap = new HashMap<String, String>();
//        parmsMap.put("starNum", Integer.toString(starNum) );
//        parmsMap.put("userType",userType);
//        parmsMap.put("commentTime", commentTime);
//        parmsMap.put("fromUserName", fromUserName);
//        parmsMap.put("fromUserId", fromUserId);
//        parmsMap.put("toUserId", toUserId);
//        parmsMap.put("billId", billId);
//        parmsMap.put("text",text);
//        return parmsMap;
//    }
}
