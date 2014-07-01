package com.buerlab.returntrunk.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.buerlab.returntrunk.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-27.
 */
public class PickTimeView extends LinearLayout {

    public interface OnTimeLisener{
        public void onTimeChange(List<String> timeList, String timestamp);
    }

    private OnTimeLisener mLisener = null;

    private int mYear = -1;
    private int mMonth = -1;
    private int[] mDays = null;
    private int[] mHours = null;

    String[] daysDesc = null;
    String[] periodItems = {"上午", "下午"};
    String[] timeDesc = null;

    WheelView dayWheel = null;
    WheelView periodWheel = null;
    WheelView timeWheel = null;

    private boolean mDayScrolling = false;
    private boolean mPeriodScrolling = false;
    private boolean mTimeScrolling = false;

    public PickTimeView(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pick_time_view, this);

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        daysDesc = new String[10];
        mDays = new int[10];
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

        ArrayWheelAdapter<String> dayAdapter = new ArrayWheelAdapter<String>(context, daysDesc);

        dayWheel = (WheelView)view.findViewById(R.id.pick_time_day);
        dayWheel.setVisibleItems(6);
        dayWheel.setViewAdapter(dayAdapter);
        dayWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!mDayScrolling && mLisener != null){
                    mLisener.onTimeChange(getTimeList(), getTimeStamp());
                }
            }
        });
        dayWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                mDayScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mDayScrolling = false;
                if(mLisener != null)
                    mLisener.onTimeChange(getTimeList(), getTimeStamp());
            }
        });


        periodWheel = (WheelView)view.findViewById(R.id.pick_time_period);
        periodWheel.setVisibleItems(6);
        periodWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, periodItems));
        periodWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!mPeriodScrolling && mLisener != null)
                    mLisener.onTimeChange(getTimeList(), getTimeStamp());
            }
        });
        periodWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                mPeriodScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mPeriodScrolling = false;
                if(mLisener != null)
                    mLisener.onTimeChange(getTimeList(), getTimeStamp());
            }
        });

        timeDesc = new String[12];
        mHours = new int[12];
        for(int j = 0; j < 12; j++) {
            timeDesc[j] = (j + 1) + "点";
            mHours[j] = j+1;
        }
        timeWheel = (WheelView)view.findViewById(R.id.pick_time_time);
        timeWheel.setVisibleItems(6);
        timeWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, timeDesc));
        timeWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!mTimeScrolling && mLisener != null)
                    mLisener.onTimeChange(getTimeList(), getTimeStamp());
            }
        });
        timeWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                mTimeScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mTimeScrolling = false;
                if(mLisener != null)
                    mLisener.onTimeChange(getTimeList(), getTimeStamp());
            }
        });

    }

    public void setLisener(OnTimeLisener lisener){
        mLisener = lisener;
    }

    public List<String> getTimeList(){
        List<String> result = new ArrayList<String>();

        result.add(daysDesc[dayWheel.getCurrentItem()]);
        result.add(periodItems[periodWheel.getCurrentItem()]);
        result.add(timeDesc[timeWheel.getCurrentItem()]);
        return result;
    }

    public String getTimeStamp(){
        int hourValue = mHours[timeWheel.getCurrentItem()];
        int currHours = periodWheel.getCurrentItem()==0 ? hourValue : hourValue+12;
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDays[dayWheel.getCurrentItem()], currHours, 0);
        return calendar.getTime().toString();
    }

}
