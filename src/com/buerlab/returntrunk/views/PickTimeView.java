package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.models.PickTime;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-27.
 */
public class PickTimeView extends LinearLayout {

    public interface OnTimeLisener{
        public void onTimeChange(List<String> timeList, String timestamp);
    }

    private OnTimeLisener mLisener = null;

    private PickTime mTime = null;

    WheelView dayWheel = null;
    WheelView periodWheel = null;
    WheelView timeWheel = null;

    private boolean mDayScrolling = false;
    private boolean mPeriodScrolling = false;
    private boolean mTimeScrolling = false;


    public PickTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickTimeView(Context context){
        super(context);
        init(context);
    }

    public void setTime(String timestamp){
        int[] indexs = mTime.fromTimeStamp(timestamp);
        dayWheel.setCurrentItem(indexs[0]);
        periodWheel.setCurrentItem(indexs[1]);
        timeWheel.setCurrentItem(indexs[2]);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pick_time_view, this);
        mTime = new PickTime();

        ArrayWheelAdapter<String> dayAdapter = new ArrayWheelAdapter<String>(context, mTime.getDaysDesc());

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
        periodWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, mTime.getPeriodDesc()));
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

        timeWheel = (WheelView)view.findViewById(R.id.pick_time_time);
        timeWheel.setVisibleItems(6);
        timeWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, mTime.getTimeDesc()));
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
        //第一次读地址
        if(mLisener != null){
            mLisener.onTimeChange(getTimeList(), getTimeStamp());
        }
    }

    public List<String> getTimeList(){
        List<String> result = new ArrayList<String>();

        result.add(mTime.getDaysDesc()[dayWheel.getCurrentItem()]);
        result.add(mTime.getPeriodDesc()[periodWheel.getCurrentItem()]);
        result.add(mTime.getTimeDesc()[timeWheel.getCurrentItem()]);
        return result;
    }

    public String getTimeStamp(){
        return mTime.toTimeStamp(dayWheel.getCurrentItem(), periodWheel.getCurrentItem(), timeWheel.getCurrentItem());
    }

}
