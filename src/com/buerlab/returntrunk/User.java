package com.buerlab.returntrunk;

import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class User {
    static public String USERTYPE_TRUNK = "driver";
    static public String USERTYPE_OWNER = "owner";

    public String userId = "";
    public String username = "";
    public String nickName = "";
    public String phoneNum = "";

    private List<Bill> mBills;

    //type would be trunk or owner
    private String userType = "";
    //billtype contains trunk, goods
    private String billType = "";
    private int newBillDialog;
    private int newBill;

    public String driveLisence = "";

    static private User instance = null;
    static public User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }

    public void init(){
        userId = "";
        username = "";
        nickName = "";
        phoneNum = "";
        //type would be trunk or owner
        userType = "";
        //billtype contains trunk, goods
        billType = "";
        newBillDialog = R.layout.new_bill_trunk_dialog;
        newBill = R.layout.new_bill_trunk;

        driveLisence = "";
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

    public void addBill(Bill bill){
        mBills.add(bill);
    }

    public List<Bill> getBills(){
        return mBills;
    }


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


}
