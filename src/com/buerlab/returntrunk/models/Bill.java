package com.buerlab.returntrunk.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-5-30.
 */
public class Bill {

    static public String BILLTYPE_TRUNK = "trunk";
    static public String BILLTYPE_GOODS = "goods";

    public String id = "";
    public String senderName = "";
    public String senderId = "";
    public String phoneNum = "";
    public String billType = "";
    public String from = "";
    public String to = "";
    //It must be a timestamp
    public String time = "";
    public int visitedTimes = 0;

    public String state = "";
    public List<String> inviteTo = new ArrayList<String>();
    public List<String> inviteFrom = new ArrayList<String>();

    public float price = 0.0f;
    public float weight = 0.0f;
    public String material = "";
    public String comment = "";

    private Trunk trunk = new Trunk();
    public String trunkType = "";
    public float trunkLength = 0.0f;
    public float trunkLoad = 0.0f;
    public String licensePlate = "";

    //to simplify the communication to server, make a list value to a single string and parse in the same way.
    static public String listToString(List<String> input){
        String result = "";
        if(input != null && input.size() > 0){
            for(int i = 0; i < input.size(); i++)
                result += input.get(i)+"-";
            result = result.substring(0, result.length()-1);
        }
        return result;
    }

    public Bill(String _billType, String _from, String _to, String _time){
        billType = _billType;
        from = _from;
        to = _to;
        time = _time;
    }

    public Bill(JSONObject item) throws Exception{
        try{
            billType = item.getString("billType");
            from = item.getString("from");
            to = item.getString("to");
            time = item.getString("billTime");
            id = item.getString("billId");
            setSenderName(item.getString("senderName"));
            senderId = item.getString("sender");

            if(item.has("visitedTimes"))
                visitedTimes = item.getInt("visitedTimes");
            if(item.has("phoneNum"))
                phoneNum = item.getString("phoneNum");
            if(item.has("state"))
                state = item.getString("state");

            if(item.has("material"))
                material = item.getString("material");
            if(item.has("weight"))
                weight = Float.valueOf(item.getString("weight"));
            if(item.has("price"))
                price = Float.valueOf(item.getString("price"));
        }catch (Exception e){
            throw new Exception("bill init from jsonobject error");
        }
    }

    public Map<String, String> toParmsMap(){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("billType", billType);
        parmsMap.put("from", from);
        parmsMap.put("to", to);
        parmsMap.put("billTime", time);
        if(billType.equals(Bill.BILLTYPE_GOODS)){
            parmsMap.put("material", material);
            parmsMap.put("price", String.valueOf(price));
            parmsMap.put("weight", String.valueOf(weight));
            parmsMap.put("comment", comment);
        }else{
        }

        return parmsMap;
    }

    public void setSenderName(String value){
        this.senderName = value;
    }

    public void setGoodsInfo(String _material, float _price, float _weight, String _comment){
        this.price = _price;
        this.weight = _weight;
        this.material = _material;
        this.comment = _comment;
    }

    public void setTrunkInfo(String _trunkType, float _trunkLength, float _trunkLoad, String _licensePlate){
        this.trunkType = _trunkType;
        this.trunkLength = _trunkLength;
        this.trunkLoad = _trunkLoad;
        this.licensePlate = _licensePlate;
    }

    public void setTrunk(Trunk _trunk){
        this.trunk = _trunk;
        this.trunkType = trunk.type;
        this.trunkLength = trunk.length;
        this.trunkLoad = trunk.load;
        this.licensePlate = trunk.lisencePlate;
    }

    public Trunk getTrunk(){ return trunk; }
}
