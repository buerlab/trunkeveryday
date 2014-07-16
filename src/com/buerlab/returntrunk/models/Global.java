package com.buerlab.returntrunk.models;

/**
 * Created by teddywu on 14-7-11.
 */
public class Global {

    static private Global instance = null;
    static public Global getInstance(){
        if(instance == null){
            instance = new Global();
        }
        return instance;
    }

    public String currentProvice;
    public String currentCity;
    public String currentDistrict;

    public String getCurrentLocation(){
        String provice ="";
        String city ="";
        String dist = "";
        if(currentProvice == null || currentCity ==null || currentDistrict ==null){
            return  null;
        }else {
            int a =currentProvice.indexOf("省");
            if(a>0){
                provice = currentProvice.substring(0,a);
            }

             a=currentCity.indexOf("市");
            if(a>0){
                city = currentCity.substring(0,a);
            }
            a=currentDistrict.indexOf("区");
            if(a>0){
                dist = currentDistrict.substring(0,a);
            }

            return provice + "-" + city + "-" + dist;
        }
    }
}
