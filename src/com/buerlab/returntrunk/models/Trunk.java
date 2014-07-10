package com.buerlab.returntrunk.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqiling on 14-6-11.
 */
public class Trunk {

    static public final String TYPE_FLAT = "平板车";
    static public final String TYPE_HIGH_FENCE = "高栏";
    static public final String TYPE_CONTAINER = "集装车";
    static public final String TYPE_VAN = "面包车";


    public String type = "";
    public float length= 0.0f;
    public float load = 0.0f;
    public String lisencePlate = "";

    public Boolean isUsed = false;
    //option
    public String trunkLicense;
    public String trunkLicenseVerified="0";
    public ArrayList<String> trunkPicFilePaths;

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

        if(trunkLicense!=null){
            parmsMap.put("trunkLicense", trunkLicense);
        }

        parmsMap.put("trunkLicenseVerified", trunkLicenseVerified);
        parmsMap.put("isUsed",String.valueOf(isUsed) );
        if(trunkPicFilePaths!=null && !trunkPicFilePaths.isEmpty()){
            String picFileStr="";
            for(int i =0;i<trunkPicFilePaths.size();i++){
                if(i<trunkPicFilePaths.size()-1){
                    picFileStr += trunkPicFilePaths.get(i) + "|";
                }else{
                    picFileStr += trunkPicFilePaths.get(i);
                }
            }
            parmsMap.put("trunkPicFilePaths",picFileStr );
        }
        return parmsMap;
    }

    public String toString(){
        return lisencePlate+"-"+type+"-"+String.valueOf(length)+"m-"+String.valueOf(load)+"kg";
    }
}
