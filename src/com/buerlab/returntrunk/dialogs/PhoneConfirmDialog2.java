package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.utils.EventLogUtils;

/**
 * Created by teddywu on 14-7-7.
 */
public class PhoneConfirmDialog2 extends Dialog{

    private Context mConext;
    public PhoneConfirmDialog2(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PhoneConfirmDialog2(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.confirm_phone_popup2, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        lp.height = Utils.getScreenSize()[1];
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL;
        setCanceledOnTouchOutside(true);

        if(Utils.getVersionType(mConext).equals("driver")){
            EventLogUtils.EventLog(mConext, EventLogUtils.tthcc_driver_phoneConfirmDialog);
        }else {
            //TODO 货主版
        }
//        PickAddrView pickAddrView = (PickAddrView)findViewById(R.id.pick_addr_view);
//        pickAddrView.setListener(this);

//        Button b = (Button)findViewById(R.id.btn_ok);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

    }



}
