package com.buerlab.returntrunk;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-5-30.
 */
public class Comment {


    public String id = "";
    public int starNum = 0;
    public String commentTime = "";
    public String fromUserName = "";
    public String fromUserId = "";
    public String toUserId = "";
    public String billId = "";
    public String text="";


    public Comment(int _starNum, String _commentTime, String _fromUserName, String _fromUserId, String _toUserId, String _billId,String _text){
        starNum = _starNum;
        commentTime = _commentTime;
        fromUserName = _fromUserName;
        fromUserId = _fromUserId;
        toUserId = _toUserId;
        billId = _billId;
        text = _text;
    }

    public Comment(JSONObject object){

    }

    public Map<String, String> toParmsMap(){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("starNum", Integer.toString(starNum) );
        parmsMap.put("commentTime", commentTime);
        parmsMap.put("fromUserName", fromUserName);
        parmsMap.put("fromUserId", fromUserId);
        parmsMap.put("toUserId", toUserId);
        parmsMap.put("billId", billId);
        parmsMap.put("text",text);
        return parmsMap;
    }
}
