package com.buerlab.returntrunk;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-5-30.
 */
public class Bill {

    static public String BILLTYPE_TRUNK = "trunk";
    static public String BILLTYPE_GOODS = "goods";

    public String id = "";
    public String senderName = "";
    public String billType = "";
    public String from = "";
    public String to = "";
    public String time = "";

    public String state = "";
    public List<String> inviteTo = new ArrayList<String>();
    public List<String> inviteFrom = new ArrayList<String>();

    public float price = 0.0f;
    public float weight = 0.0f;
    public String material = "";

    public String trunkType = "";
    public float trunkLength = 0.0f;
    public String licensePlate = "";

    public Bill(String _billType, String _from, String _to, String _time){
        billType = _billType;
        from = _from;
        to = _to;
        time = _time;
    }

    public Bill(JSONObject object){

    }

    public void setSenderName(String value){
        this.senderName = value;
    }

    public void setGoodsInfo(String _material, float _price, float _weight){
        this.price = _price;
        this.weight = _weight;
        this.material = _material;
    }

    public void setTrunkInfo(String _trunkType, float _trunkLength, String _licensePlate){
        this.trunkType = _trunkType;
        this.trunkLength = _trunkLength;
        this.licensePlate = _licensePlate;
    }
}
