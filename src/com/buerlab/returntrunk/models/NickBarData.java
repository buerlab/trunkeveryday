package com.buerlab.returntrunk.models;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public ArrayList<String> trunkPicFilePaths=null;
    public NickBarData(JSONObject obj){
        try {
            if(obj.has("userType")&& !obj.isNull("userType"))
                userType = obj.getString("userType");
            if(obj.has("userId")&& !obj.isNull("userId")){
                userId = obj.getString("userId");
            }
            if(obj.has("nickName")&& !obj.isNull("nickName")){
                nickName = obj.getString("nickName");
            }
            if(obj.has("driverStars") && !obj.isNull("driverStars")){
                driverStars = obj.getDouble("driverStars");
            }
            if(obj.has("ownerStars")&& !obj.isNull("ownerStars")){
                ownerStars = obj.getDouble("ownerStars");
            }
            if(obj.has("IDNumVerified")&& !obj.isNull("IDNumVerified")){
                IDNumVerified = obj.getString("IDNumVerified");
            }
            if(obj.has("driverLicenseVerified")&& !obj.isNull("driverLicenseVerified")){
                driverLicenseVerified = obj.getString("driverLicenseVerified");
            }
            if(obj.has("trunkLicenseVerified")&& !obj.isNull("trunkLicenseVerified")){
                trunkLicenseVerified = obj.getString("trunkLicenseVerified");
            }

            if(obj.has("trunkPicFilePaths")&& !obj.isNull("trunkPicFilePaths")){
                JSONArray ja  = obj.getJSONArray("trunkPicFilePaths");
                trunkPicFilePaths = new ArrayList<String>();
                for(int i =0;i<ja.length(); i++){
                   trunkPicFilePaths.add(ja.getString(i));
                }
            }
        }catch (JSONException e){
            Log.d("USER INIT ERROR", e.toString());
        }
    }
}
