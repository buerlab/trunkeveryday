package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.jpush.models.BillRequest;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by teddywu on 14-7-7.
 */
public class BillConfirmDialog extends Dialog{

    private Context mConext;
    private BillRequest mRequest;

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

    public BillConfirmDialog(Context context, int theme, BillRequest request) {
        super(context, theme);
        mConext = context;
        mRequest = request;
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

        TextView nickName = (TextView)diaView.findViewById(R.id.nickname);
        nickName.setText(mRequest.senderName);

        final BillConfirmDialog self = this;
        LinearLayout submitBtn = (LinearLayout)findViewById(R.id.button_true);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                self.dismiss();

                NetService service = new NetService(getContext());
                service.confirmBill(mRequest.reqId, new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if(result.code == NetProtocol.SUCCESS){

                        }
                    }
                });
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
