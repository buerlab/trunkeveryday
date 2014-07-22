package com.buerlab.returntrunk.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-18.
 */
public class PickTime {

    private final int dayRange = 10;
    private long mTimeStamp = 0;
    private List<List<Integer>> mDates = null;
    private String[] mDaysDesc = null;
    private String[] mPeriodDesc = {"上午", "下午"};
    private String[] mTimeDesc = null;

    public PickTime(){
        Calendar calendar = Calendar.getInstance();
        mDaysDesc = new String[dayRange];
        mDates = new ArrayList<List<Integer>>();
        for(int i = 0; i < mDaysDesc.length; i++){
            Integer[] date = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
            mDates.add(Arrays.asList(date));
            mDaysDesc[i] = (date[1]+1)+"月"+date[2]+"日";
            if(i == 0)
                mDaysDesc[i] += "(今天)";
            else if(i == 1)
                mDaysDesc[i] += "(明天)";
            else if(i == 2)
                mDaysDesc[i] += "(后天)";

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mTimeDesc = new String[12];
        for(int j = 0; j < 12; j++) {
            mTimeDesc[j] = (j + 1) + "点";
        }
    }

    public int[] fromTimeStamp(String timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(timestamp));
        Integer[] times = {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
        List<Integer> timeList = Arrays.asList(times);
        int hour = calendar.get(Calendar.HOUR);
        int hourIndex = hour>0 ? hour-1 : 11;
        int periodIndex = calendar.get(Calendar.AM_PM);
        int dateIndex = mDates.indexOf(timeList);
        int[] indexs = {dateIndex, periodIndex, hourIndex};
        int[] indexs2 = {0, 0, 0};
        return dateIndex >= 0? indexs : indexs2;
    }

    public String toTimeStamp(int dateIndex, int periodIndex, int timeIndex){
        List<Integer> currDate = mDates.get(dateIndex);
        int hour = timeIndex < 11 ? timeIndex+1 : 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(currDate.get(0), currDate.get(1), currDate.get(2));
        calendar.set(Calendar.AM_PM, periodIndex);
        calendar.set(Calendar.HOUR, hour);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.valueOf(Timestamp.valueOf(sdf.format(calendar.getTime())).getTime());
    }

    public String[] getDaysDesc(){
        return mDaysDesc;
    }

    public String[] getPeriodDesc(){
        return mPeriodDesc;
    }

    public String[] getTimeDesc(){
        return mTimeDesc;
    }

}
