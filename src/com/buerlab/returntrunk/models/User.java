package com.buerlab.returntrunk.models;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.driver.activities.SetTrunkActivity;
import com.buerlab.returntrunk.driver.activities.initDriverActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class User {
    static public String USERTYPE_TRUNK = "driver";
    static public String USERTYPE_OWNER = "owner";

    static public boolean validate(Activity from){
        if(User.getInstance().getUserType().isEmpty()){
            from.startActivity(new Intent(from, initDriverActivity.class));
            from.finish();
            return false;
        }else if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK) && User.getInstance().trunks!=null &&   User.getInstance().trunks.isEmpty()){
            from.startActivity(new Intent(from, SetTrunkActivity.class));
            from.finish();
            return false;
        }
        return true;
    }

    public String userId = "";
    public String username = "";
    public String nickName = "";
    public String phoneNum = "";
    public String homeLocation = "";
    public String IDNum = "";
    public String IDNumVerified = "0";
    public String driverLicense="";
    public String driverLicenseVerified="0";

    private List<Bill> mBills = null;
    private List<Bill> mHistoryBills;

    //type would be trunk or owner
    private String userType = "";
    //billtype contains trunk, goods
    private String billType = "";
    private int newBillDialog;
    private int newBill;


    public String useTrunk = "";
    public List<Trunk> trunks;

    private List<Comment> mDriverComments;
    private List<Comment> mOnwerComments;
    static private User instance = null;
    static public User getInstance(){
        if(instance == null){
            instance = new User();
            instance.init();
        }
        return instance;
    }

    public void init(){
        userId = "";
        username = "";
        nickName = "";
        phoneNum = "";
        homeLocation = "";
        IDNum="";
        IDNumVerified = "0";
        driverLicense="";
        driverLicenseVerified="0";
        mBills = new ArrayList<Bill>();
        mDriverComments = new ArrayList<Comment>();
        //type would be trunk or owner
        userType = "";
        //billtype contains trunk, goods
        billType = "";
        newBillDialog = R.layout.new_bill_trunk_dialog;
        newBill = R.layout.new_bill_trunk;

        trunks = new ArrayList<Trunk>();
        useTrunk = "";
    }

    public void initUser(JSONObject obj){
        init();
        try{
            if(obj.has("userId"))
                this.userId = obj.getString("userId");
            if(obj.has("username"))
                this.username = obj.getString("username");
            if(obj.has("phoneNum"))
                this.phoneNum = obj.getString("phoneNum");
            if(obj.has("userType"))
                setUserType(obj.getString("userType"));
            if(obj.has("nickName"))
                this.nickName = obj.getString("nickName");
            if(obj.has("trunks"))
                this.trunks = extractTrunk(obj.getJSONArray("trunks"));
            if(obj.has("homeLocation"))
                this.homeLocation = obj.getString("homeLocation");
            if(obj.has("IDNum"))
                this.IDNum = obj.getString("IDNum");
            if(obj.has("IDNumVerified"))
                this.IDNumVerified = obj.getString("IDNumVerified");
            if(obj.has("driverLicense"))
                this.driverLicense = obj.getString("driverLicense");
            if(obj.has("driverLicenseVerified"))
                this.driverLicenseVerified = obj.getString("driverLicenseVerified");
            if(obj.has("useTrunk"))
                this.useTrunk = obj.getString("useTrunk");

        }catch (JSONException e){
            Log.d("USER INIT ERROR", e.toString());
        }
    }

    public void setUserType(String type){
        if(type.equals(USERTYPE_OWNER)){
            billType = "goods";
            newBillDialog = R.layout.new_bill_goods_dialog;
            newBill = R.layout.new_bill_goods;
        }else if(type.equals(USERTYPE_TRUNK)){
            billType = "trunk";
            newBillDialog = R.layout.new_bill_trunk_dialog;
            newBill = R.layout.new_bill_trunk;
        }else{
            Log.d("-------WRONG USERTYPE!!", type);
            return;
        }
        userType = type;
    }

    public void initBills(List<Bill> bills){
        mBills = bills;
    }

    public List<Bill> getBills(){
        return mBills;
    }

    public Bill getBill(String billid){
        for(Bill bill : mBills){
            if(bill.id.equals(billid))
                return bill;
        }
        return null;
    }

    public void addBill(Bill bill){
        mBills.add(bill);
    }

    public void initHistoryBills(List<Bill> bills){ mHistoryBills = bills; }

    public void extendHistoryBills(List<Bill> bills){ mHistoryBills.addAll(bills); }

    public List<Bill> getHistoryBills(){ return mHistoryBills; }

    public void addTrunk(Trunk trunk){
        trunks.add(trunk);
    }

    public void initDriverComments(List<Comment> comments){
        mDriverComments = comments;
    }
    public void initOwnerComments(List<Comment> comments){
        mOnwerComments = comments;
    }
    public List<Comment> getDriverComment(){ return mDriverComments;}
    public List<Comment> getOnwerComment(){ return mOnwerComments;}

    public String getUserType(){
        return userType;
    }

    public String getBillType(){
        return billType;
    }

    public int getNewBillDialog(){
        return newBillDialog;
    }
    public int getNewBillId(){
        return newBill;
    }

    public static List<Trunk> extractTrunk(JSONArray data){
        List<Trunk> result = new ArrayList<Trunk>();

        try{
            for(int i = 0; i < data.length(); i++) {
                JSONObject d = data.getJSONObject(i);
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
                result.add(t);
            }
            return result;
        }catch (JSONException e){
            Log.d("EXRACT TRUNK ERROR INIT USER", e.toString());
            return null;
        }
    }

}
