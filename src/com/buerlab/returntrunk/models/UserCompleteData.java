package com.buerlab.returntrunk.models;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.driver.activities.AddTrunkActivity;
import com.buerlab.returntrunk.net.NetService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class UserCompleteData {

    public String nickName = "";
    public String phoneNum = "";
    public String homeLocation = "";
    public String IDNumVerified = "0";
    public String driverLicenseVerified="0";
    public String trunkLicenseVerified="0";
    public String userId = "";
    public Trunk trunk;
    public String regTime;
    public String userType;
    public double stars;
    public List<Comment> comments;

    public UserCompleteData(JSONObject obj){
        initUserCompleteData(obj);
    }

    public void initUserCompleteData(JSONObject obj){
        try{
            if(obj.has("userId"))
                this.userId = obj.getString("userId");
            if(obj.has("phoneNum"))
                this.phoneNum = obj.getString("phoneNum");
            if(obj.has("userType"))
                this.userType= obj.getString("userType");
            if(obj.has("nickName"))
                this.nickName = obj.getString("nickName");
            if(obj.has("trunk"))
                this.trunk = extractTrunk(obj.getJSONObject("trunk"));
            if(obj.has("homeLocation"))
                this.homeLocation = obj.getString("homeLocation");
            if(obj.has("IDNumVerified"))
                this.IDNumVerified = obj.getString("IDNumVerified");
            if(obj.has("driverLicenseVerified"))
                this.driverLicenseVerified = obj.getString("driverLicenseVerified");

            if(obj.has("trunkLicenseVerified"))
                this.trunkLicenseVerified = obj.getString("trunkLicenseVerified");

            if(obj.has("stars")){
                this.stars = obj.getDouble("tars");
            }

            if(obj.has("commments")){
                comments = NetService.extractComments(obj.getJSONArray("comments"));
            }
            if(obj.has("regtime")){
                String timeStr = obj.getString("regtime");
                regTime = Utils.timestampToDisplay(timeStr,Utils.YEAR_MONTH_DAY);
            }
        }catch (JSONException e){
            Log.d("USER INIT ERROR", e.toString());
        }
    }

    public static Trunk extractTrunk(JSONObject d){
        try{
            String type = d.has("type") ? d.getString("type") : "";
            float length = d.has("length") ? Float.valueOf(d.getString("length")) : 0.0f;
            float load = d.has("load") ? Float.valueOf(d.getString("load")) : 0.0f;
            String lisence = d.has("licensePlate") ? d.getString("licensePlate") : "";

            Trunk t = new Trunk(type, length, load, lisence);

            //可能没有
            if(d.has("isUsed")){
                t.isUsed = d.getBoolean("isUsed");
            }else{
                t.isUsed = false;
            }
            if(d.has("trunkLicense")){
                t.trunkLicense = d.getString("trunkLicense");
            }
            if(d.has("trunkLicenseVerified")){
                t.trunkLicenseVerified = d.getString("trunkLicenseVerified");
            }
            if(d.has("trunkPicFilePaths")){
                JSONArray pics =  d.getJSONArray("trunkPicFilePaths");
                ArrayList<String> picArrayList = new ArrayList<String>();
                for(int j = 0; j < pics.length(); j++) {
                    picArrayList.add((String)pics.get(j));
                }
                t.trunkPicFilePaths = picArrayList;
            }
            return t;
        }catch (JSONException e){
            Log.e("UserCompleteData","UserCompleteData Prase error");
            return null;
        }
    }
}
