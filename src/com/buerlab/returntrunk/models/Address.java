package com.buerlab.returntrunk.models;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-3.
 */
public class Address {
    //NEVER CHANGE THIS BECAUSE IT IS CONSITENCE WITH DATA IN SERVER!!!
    private final String mSpliter = "-";

    private List<String> mAddrList = null;

    public Address(List<String> addrs){
        mAddrList = addrs;
    }

    public Address(String fullString){
        mAddrList = Arrays.asList(fullString.split(mSpliter));
    }

    public String toFullString(){
        return toFullString(mSpliter);
    }

    public String toFullString(String spliter){
        return arrayToString(mAddrList, spliter);
    }

    public String toShortString(){
        return toShortString(mSpliter);
    }

    public String toShortString(String spliter){
        if(mAddrList.size() > 2){
            List<String> clipList = mAddrList.subList(mAddrList.size()-2, mAddrList.size());
            return arrayToString(clipList, spliter);
        }
        return toFullString(spliter);
    }

    public List<String> getAddrList(){
        return mAddrList;
    }

    private String arrayToString(List<String> list, String spliter){
        String result = "";
        for(String addr : list)
            result += addr + spliter;
        return result.substring(0, result.length()-1);
    }
}
