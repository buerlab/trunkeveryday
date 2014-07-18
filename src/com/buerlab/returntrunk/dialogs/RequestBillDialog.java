package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.EventLogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhongqiling on 14-7-10.
 */
public class RequestBillDialog extends Dialog {

    private static final String TAG = "RequestBillDialog";

    private Context mConext;
    private Bill mBill;


    public RequestBillDialog(Context context, int theme, Bill bill) {
        super(context, theme);
        mConext = context;
        mBill = bill;
        init();
    }

    private void init(){
        View diaView=View.inflate(mConext, R.layout.request_bill_dialog, null);

        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = Utils.getScreenSize()[0]; //设置宽度
        lp.height = Utils.getScreenSize()[1];
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL;
        setCanceledOnTouchOutside(true);

//        PickAddrView pickAddrView = (PickAddrView)findViewById(R.id.pick_addr_view);
//        pickAddrView.setListener(this);

        View b = (View)findViewById(R.id.button_true);
        final Dialog self = this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetService service= new NetService(self.getContext());
                service.pickBill(mBill.id, "", new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if (result.code == NetProtocol.SUCCESS){
                        }
                    }
                });
                dismiss();
            }
        });

        findViewById(R.id.button_false).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismiss();
            }
        });

        if(Utils.getVersionType(mConext).equals("driver")){
            EventLogUtils.EventLog(mConext, EventLogUtils.tthcc_driver_requestBillDialog);
        }else {
            //TODO 货主版
        }
    }
}
