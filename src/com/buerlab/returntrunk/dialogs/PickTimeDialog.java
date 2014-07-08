package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.views.PickTimeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teddywu on 14-7-7.
 */
public class PickTimeDialog extends Dialog implements PickTimeView.OnTimeLisener {

    private Context mConext;
    public PickTimeDialog(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PickTimeDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.pick_time_view_wrapper, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
        setCanceledOnTouchOutside(true);

        PickTimeView pickTimeView = (PickTimeView)findViewById(R.id.pick_time_view);
        pickTimeView.setLisener(this);

//        Button b = (Button)findViewById(R.id.btn_ok);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

    }

    @Override
    public void onTimeChange(List<String> timeList, String timestamp) {
        Bundle data = new Bundle();
        data.putStringArrayList("timeList", (ArrayList) timeList);
        data.putString("timestamp",timestamp);
        EventCenter.shared().dispatch(new DataEvent(DataEvent.TIME_CHANGE, data));
    }
}
