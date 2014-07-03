package com.buerlab.returntrunk.utils;

import java.util.Calendar;

/**
 * Created by zhongqiling on 14-7-3.
 */
public class TimeUtil {

    public int dayRange = 10;
    private long mTimeStamp = 0;

    public TimeUtil(String timestamp){
        mTimeStamp = Long.parseLong(timestamp);
    }


    public String[] genDayList(){
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        String[] daysDesc = new String[dayRange];
        int[] mDays = new int[dayRange];
        for(int i = 0; i < daysDesc.length; i++){
            mDays[i] = calendar.get(Calendar.DAY_OF_MONTH);
            daysDesc[i] = (calendar.get(Calendar.MONTH)+1)+"月"+mDays[i]+"日";
            if(i == 0)
                daysDesc[i] += "(今天)";
            else if(i == 1)
                daysDesc[i] += "(明天)";
            else if(i == 2)
                daysDesc[i] += "(后天)";

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysDesc;
    }
}
