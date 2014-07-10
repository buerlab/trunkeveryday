package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-26.
 */
public class PickTrunkTypeView extends LinearLayout {

    public interface OnTrunkTypeListener{
        //addr is a list contains 3 elements, prov,city,region in order.
        public void OnTrunkTypeChanged(String trunkType);
    }

    public WheelView trunkWheel = null;
//    public LinearLayout regWheelContainer = null;

    private boolean trunkScrolling = false;
    private OnTrunkTypeListener mListener = null;
    private String trunkType;
    private Context mContext = null;

    public PickTrunkTypeView(final Context context){
        super(context);

        init(context);
    }
    public PickTrunkTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private  void init(Context context){
        mContext = context;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View)inflater.inflate(R.layout.pick_trunk_type_view, this);

        final String[] trunkTypes = {"厢车","平板车","高栏车","低栏车","集装车","面包车"};
        ArrayWheelAdapter trunkAdapter = new ArrayWheelAdapter(context, trunkTypes);
        trunkWheel = (WheelView)view.findViewById(R.id.pick_trunk_type);
        trunkWheel.setVisibleItems(6);
        trunkWheel.setViewAdapter(trunkAdapter);

        trunkType= trunkTypes[trunkWheel.getCurrentItem()];


        trunkWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!trunkScrolling) {
                }
            }
        });

        trunkWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                trunkScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                trunkScrolling = false;
                trunkType= trunkTypes[trunkWheel.getCurrentItem()];
                mListener.OnTrunkTypeChanged(trunkType);
            }
        });

    }

    public void setListener(OnTrunkTypeListener listener){
        mListener = listener;
        //第一次读地址
        if(mListener != null){
            mListener.OnTrunkTypeChanged(trunkType);
        }

    }
}
