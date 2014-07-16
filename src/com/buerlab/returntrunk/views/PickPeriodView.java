package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
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

/**
 * Created by zhongqiling on 14-7-15.
 */
public class PickPeriodView extends LinearLayout {

    public interface OnPeriodLisener{
        public void onPeriodChange(String periodStr, int periodSec);
    }

    private int validDayRange = 7;
    private String[] periodDesc = new String[validDayRange];
    private int[] periodSec = new int[validDayRange];
    private String[] hoursDesc = new String[24];
    private WheelView periodWheel = null;
    private WheelView hoursWheel = null;
    private boolean mScrolling = false;
    private OnPeriodLisener mLisener = null;

    public PickPeriodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickPeriodView(Context context){
        super(context);

        init(context);

    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pick_period_view, this);

        for(int i = 0; i < validDayRange; i++){
            periodDesc[i] = i+"天";
            periodSec[i] = i*24*60*60;
        }

        ArrayWheelAdapter<String> periodItems = new ArrayWheelAdapter<String>(context, periodDesc);
        periodWheel = (WheelView)view.findViewById(R.id.pick_period);
        periodWheel.setViewAdapter(periodItems);
        periodWheel.setVisibleItems(6);
        periodWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!mScrolling && mLisener != null)
                    mLisener.onPeriodChange(periodDesc[periodWheel.getCurrentItem()], periodSec[periodWheel.getCurrentItem()]);
            }
        });

        periodWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                mScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mScrolling = false;
                if(mLisener != null)
                    mLisener.onPeriodChange(periodDesc[periodWheel.getCurrentItem()], periodSec[periodWheel.getCurrentItem()]);
            }
        });

        for(int j = 0; j < 24; j++){
            hoursDesc[j] = j+"小时";
        }
        ArrayWheelAdapter<String> hoursItems = new ArrayWheelAdapter<String>(context, hoursDesc);
        hoursWheel = (WheelView)view.findViewById(R.id.pick_period_hour);
        hoursWheel.setViewAdapter(hoursItems);
        hoursWheel.setVisibleItems(6);
        hoursWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!mScrolling && mLisener != null)
                    mLisener.onPeriodChange(getCurrDesc(), getCurrSec());
            }
        });

        periodWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                mScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mScrolling = false;
                if(mLisener != null)
                    mLisener.onPeriodChange(getCurrDesc(), getCurrSec());
            }
        });
    }


    public void setLisener(OnPeriodLisener lisener){
        mLisener = lisener;
        //第一次读地址
        if(mLisener != null){
            mLisener.onPeriodChange(getCurrDesc(), getCurrSec());
        }
    }

    private int getCurrSec(){
        return periodSec[periodWheel.getCurrentItem()]+hoursWheel.getCurrentItem()*60*60;
    }

    private String getCurrDesc(){
        return periodDesc[periodWheel.getCurrentItem()]+hoursDesc[hoursWheel.getCurrentItem()];
    }
}
