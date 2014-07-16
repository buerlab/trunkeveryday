package com.buerlab.returntrunk.models;

import org.json.JSONObject;

/**
 * Created by zhongqiling on 14-7-10.
 */
public class HistoryBill {

    static public final String TYPE_GOODS = "goods";
    static public final String TYPE_TRUNK = "trunk";
    static public final String  TYPE_USER = "user";

    public String id = "";
    public String billType = "";

    public String nickName = "";
    public String sender = "";
    public String senderType = "";
    public NickBarData senderData = null;
    public String sendTime = "";
    public String senderPhoneNum = "";


    public String fromAddr = "";
    public String toAddr = "";

    public String fromTime = "";
    public String toTime = "";

    public float price = 0;
    public float weight = 0;
    public String material = "";

    public HistoryBill(JSONObject data) throws Exception{
        try{
            JSONObject item = data.getJSONObject("bill");
            id = item.getString("id");
            billType = item.getString("billType");
            nickName = item.getString("nickName");
            sender = item.getString("senderId");
            senderType = item.getString("senderType");

            sendTime = item.getString("sendTime");

            if(item.has("fromAddr"))
                fromAddr = item.getString("fromAddr");
            if(item.has("toAddr"))
                toAddr = item.getString("toAddr");
            if(item.has("fromTime"))
                fromTime = item.getString("fromTime");
            if(item.has("toTime"))
                toTime = item.getString("toTime");
            if(item.has("price"))
                price = Float.valueOf(item.getString("price"));
            if(item.has("weight"))
                weight = Float.valueOf(item.getString("weight"));
            if(item.has("material"))
                material = item.getString("material");

            JSONObject senderJSONData = data.getJSONObject("senderData");
            senderData = new NickBarData(senderJSONData);
            if(senderJSONData.has("phoneNum"))
                senderPhoneNum = senderJSONData.getString("phoneNum");

        }catch (Exception e){
            throw new Exception("bill init from jsonobject error");
        }
    }

}
