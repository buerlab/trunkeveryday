package com.buerlab.returntrunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqiling on 14-6-11.
 */
public class Trunk {


    public String type = "";
    public float length= 0.0f;
    public float load = 0.0f;
    public String lisencePlate = "";

    public Trunk(){}

    public Trunk(String _type, float _length, float _load, String _licensePlate){
        type = _type;
        length = _length;
        load = _load;
        lisencePlate = _licensePlate;
    }

    public Map<String, String> toParmsMap(){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("type", type);
        parmsMap.put("length", String.valueOf(length));
        parmsMap.put("load", String.valueOf(load));
        parmsMap.put("licensePlate", lisencePlate);
        return parmsMap;
    }

    public String toString(){
        return lisencePlate+"-"+type+"-"+String.valueOf(length)+"m-"+String.valueOf(load)+"kg";
    }
}
