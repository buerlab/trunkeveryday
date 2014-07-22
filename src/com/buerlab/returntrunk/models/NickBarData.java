package com.buerlab.returntrunk.models;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by teddywu on 14-7-5.
 */
public class NickBarData {
    public String userType = "";
    public String userId = "";
    public String nickName = "";
    public double driverStars =0;
    public double ownerStars =0;
    public String IDNumVerified = "0";
    public String driverLicenseVerified="0";
    public String trunkLicenseVerified="0";

    public NickBarData(JSONObject obj){
        try {
            if(obj.has("userType"))
                userType = obj.getString("userType");
            if(obj.has("userId")){
                userId = obj.getString("userId");
            }
            if(obj.has("nickName")){
                nickName = obj.getString("nickName");
            }
            if(obj.has("driverStars")){
                driverStars = obj.getDouble("driverStars");
            }
            if(obj.has("ownerStars")){
                ownerStars = obj.getDouble("ownerStars");
            }
            if(obj.has("IDNumVerified")){
                IDNumVerified = obj.getString("IDNumVerified");
            }
            if(obj.has("driverLicenseVerified")){
                driverLicenseVerified = obj.getString("driverLicenseVerified");
            }
            if(obj.has("trunkLicenseVerified")){
                trunkLicenseVerified = obj.getString("trunkLicenseVerified");
            }
        }catch (JSONException e){
            Log.d("USER INIT ERROR", e.toString());
        }
    }
}
