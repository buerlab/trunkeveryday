package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.buerlab.returntrunk.PhoneCallListener;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.models.Address;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.EventLogUtils;

/**
 * Created by teddywu on 14-7-7.
 */
public class PhoneConfirmDialog2 extends Dialog{

    private Context mConext;
    private Bill mBill;
    public PhoneConfirmDialog2(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PhoneConfirmDialog2(Context context,Bill bill, int theme) {
        super(context, theme);
        mConext = context;
        mBill = bill;
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
        diaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_wrapper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        if(Utils.getVersionType(mConext).equals("driver")){
            EventLogUtils.EventLog(mConext, EventLogUtils.tthcc_driver_phoneConfirmDialog);
        }else {
            //TODO 货主版
        }

        ((TextView)findViewById(R.id.nickname)).setText(mBill.senderName);
        ((TextView)findViewById(R.id.find_bill_from)).setText(new Address(mBill.from).toShortString());
        ((TextView)findViewById(R.id.find_bill_to)).setText(new Address(mBill.to).toShortString());
        ((TextView)findViewById(R.id.find_bill_time)).setText(Utils.timestampToDisplay(mBill.time));
//        PickAddrView pickAddrView = (PickAddrView)findViewById(R.id.pick_addr_view);
//        pickAddrView.setListener(this);

        findViewById(R.id.button_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseActivity.currActivity != null){
                    if(mBill.phoneNum.length() > 0){
                        NetService service = new NetService(mConext);
                        service.billCall(mBill.senderId, mBill.billType, new NetService.NetCallBack() {
                            @Override
                            public void onCall(NetProtocol result) {
                                if(result.code == NetProtocol.SUCCESS){
                                    Utils.showToast(mConext,"billcall ok!");
                                }
                            }
                        });

                        new PhoneCallListener(mConext)
                                .listen(new PhoneCallListener.OnPhoneCallBack() {
                                    @Override
                                    public void onCalling() {
                                    }

                                    @Override
                                    public void onCallEnd() {
                                        RequestBillDialog dialog = new RequestBillDialog(mConext, R.style.dialog, mBill);
                                        dialog.show();
                                    }
                                });

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mBill.phoneNum));
                        BaseActivity.currActivity.startActivity(intent);

                    }else{
                        Utils.showToast(BaseActivity.currActivity,"该用户没有手机号");
                    }
                }
                dismiss();
            }
        });

        findViewById(R.id.button_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        diaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_wrapper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

    }



}
