package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.views.PickPeriodView;
import com.buerlab.returntrunk.views.PickTimeView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-7-15.
 */
public class PickPeriodDialog extends Dialog implements PickPeriodView.OnPeriodLisener{

    private Context mConext;
    public PickPeriodDialog(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PickPeriodDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.pick_period_view_wrapper, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        PickPeriodView pickView = (PickPeriodView)diaView.findViewById(R.id.pick_period_view);
        pickView.setLisener(this);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = Utils.getScreenSize()[0]; //设置宽度
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
        setCanceledOnTouchOutside(true);

    }

    public void onPeriodChange(String periodStr, int periodSec){
        List data = new ArrayList();
        data.add(periodStr);
        data.add(periodSec);
        EventCenter.shared().dispatch(new DataEvent(DataEvent.PERIOD_CHANGE, data));
    }
}
