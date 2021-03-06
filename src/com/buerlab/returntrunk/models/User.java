package com.buerlab.returntrunk.models;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.driver.activities.AddTrunkActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class User {
    static public String USERTYPE_TRUNK = "driver";
    static public String USERTYPE_OWNER = "owner";

    static public boolean validate(Activity from){
//        if(User.getInstance().getUserType().isEmpty()){
//            from.startActivity(new Intent(from, InitDriverActivity.class));
//            from.finish();
//            return false;
//        }
        if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK) && User.getInstance().trunks!=null &&   User.getInstance().trunks.isEmpty()){
            Intent intent = new Intent(from,AddTrunkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("enterByLogin",true);
            from.startActivity(intent);
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
    private LinkedList<HistoryBill> mHistoryBills;

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

    public double driverStars;
    public double ownerStars;

    public Settings driverSettings;
    public Settings ownerSettings;

    public boolean driverCanPush = true;
    public boolean ownerCanPush = true;

    public boolean driverCanLocate = true;
    public boolean ownerCanLocate = true;

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
        mHistoryBills = new LinkedList<HistoryBill>();
        mDriverComments = new ArrayList<Comment>();
        mOnwerComments = new ArrayList<Comment>();
        //type would be trunk or owner
        userType = "";
        //billtype contains trunk, goods
        billType = "";
        newBillDialog = R.layout.new_bill_trunk_dialog;
        newBill = R.layout.new_bill_trunk;

        trunks = new ArrayList<Trunk>();
        useTrunk = "";
        driverStars = 0;
        ownerStars = 0;

        driverSettings = new Settings();
        ownerSettings = new Settings();
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
            if(obj.has("driverStars")){
                this.driverStars = obj.getDouble("driverStars");
            }
            if(obj.has("ownerStars")){
                this.ownerStars = obj.getDouble("ownerStars");
            }

            if(obj.has("ownerSettings")){
                this.ownerSettings = extrackSettings(obj.getJSONObject("ownerSettings"));
            }

            if(obj.has("driverSettings")){
                this.driverSettings = extrackSettings(obj.getJSONObject("driverSettings"));
            }


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

    public void updateBill(Bill bill){
        for(int i = 0; i < mBills.size(); i++){
            if(mBills.get(i).id == bill.id)
                mBills.set(i, bill);
        }
    }

    public void addBill(Bill bill){
        mBills.add(bill);
    }
    public void removeBill(Bill bill){ mBills.remove(bill); }

    public void initHistoryBills(List<HistoryBill> bills){
        mHistoryBills.clear();
        for(HistoryBill bill : bills)
            mHistoryBills.add(bill);
    }

    public void extendHistoryBills(List<HistoryBill> bills){ mHistoryBills.addAll(bills); }

    public void headExtendHistoryBills(List<HistoryBill> bills){
        for(int i = bills.size()-1; i >= 0; i--){
            mHistoryBills.addFirst(bills.get(i));
        }
    }

    public List<HistoryBill> getHistoryBills(){ return mHistoryBills; }

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
    public Settings getSetting(){
        return userType == User.USERTYPE_TRUNK ? driverSettings : ownerSettings;
    }

    public static Settings extrackSettings(JSONObject data){
        try{
            boolean push = data.has("push") ? data.getBoolean("push") : true;
            boolean gps = data.has("gps") ? data.getBoolean("gps") : true;
            return new Settings(push,gps);
        }catch (JSONException e){
            Log.d("EXRACT SETTING ERROR INIT USER", e.toString());
            return new Settings();
        }
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
