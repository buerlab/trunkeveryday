package com.buerlab.returntrunk.utils;

/**
 * Created by zhongqiling on 14-7-19.
 */
public class PeriodTimeUtils {
    static public int[] parsePeriodSec(int sec){
        int[] result = {sec/(24*60*60), (sec%(24*60*60))/(60*60)};
        return result;
    }

    static public String getPeriodDesc(int sec){
        int[] time = parsePeriodSec(sec);
        return String.valueOf(time[0])+"天"+String.valueOf(time[1])+"小时";
    }
}
