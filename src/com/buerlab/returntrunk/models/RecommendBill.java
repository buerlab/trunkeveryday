package com.buerlab.returntrunk.models;

/**
 * Created by zhongqiling on 14-7-22.
 */
public class RecommendBill {

    public NickBarData userData = null;
    public Bill bill = null;

    public RecommendBill(NickBarData _userData, Bill _bill){
        userData = _userData;
        bill = _bill;
    }
}
