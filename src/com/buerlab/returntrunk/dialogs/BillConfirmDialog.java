package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;

/**
 * Created by teddywu on 14-7-7.
 */
public class BillConfirmDialog extends Dialog{

    private Context mConext;
    public BillConfirmDialog(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public BillConfirmDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.confirm_bill_popup, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        lp.height = Utils.getScreenSize()[1];
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL;
        setCanceledOnTouchOutside(true);

//        PickAddrView pickAddrView = (PickAddrView)findViewById(R.id.pick_addr_view);
//        pickAddrView.setListener(this);

//        Button b = (Button)findViewById(R.id.btn_ok);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        final BillConfirmDialog self = this;
        LinearLayout submitBtn = (LinearLayout)findViewById(R.id.button_true);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismiss();

            }
        });

        LinearLayout cancelBtn = (LinearLayout)findViewById(R.id.button_false);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismiss();
            }
        });


    }



}
