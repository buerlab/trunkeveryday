package com.buerlab.returntrunk.models;

/**
 * Created by zhongqiling on 14-7-22.
 */
public class RecommendBill {

    static public final String LOCAL_TRUNK = "local_trunk";

    public String recommendBillType = "";
    public NickBarData userData = null;
    public Bill bill = null;

    public RecommendBill(NickBarData _userData, Bill _bill, String type){
        userData = _userData;
        bill = _bill;
        recommendBillType = type;
    }
}
