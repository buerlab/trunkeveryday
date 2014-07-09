package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.views.PickAddrView;
import com.buerlab.returntrunk.views.PickTrunkTypeView;

import java.util.List;

/**
 * Created by teddywu on 14-7-7.
 */
public class PickTrunkTypeDialog extends Dialog implements PickTrunkTypeView.OnTrunkTypeListener{

    private Context mConext;
    public PickTrunkTypeDialog(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PickTrunkTypeDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.pick_trunk_type_view_wrapper, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
        setCanceledOnTouchOutside(true);

        PickTrunkTypeView pickTrunkTypeView = (PickTrunkTypeView)findViewById(R.id.pick_trunk_type_view);
        pickTrunkTypeView.setListener(this);

//        Button b = (Button)findViewById(R.id.btn_ok);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

    }

    @Override
    public void OnTrunkTypeChanged(String trunkType) {
        EventCenter.shared().dispatch(new DataEvent(DataEvent.TRUNK_TYPE_CHANGE, trunkType));
    }
}
