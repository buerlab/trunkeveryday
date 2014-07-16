package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.Address;
import com.buerlab.returntrunk.utils.EventLogUtils;

import java.util.List;

/**
 * Created by teddywu on 14-7-7.
 */
public class PublishConfirmDialog extends Dialog implements EventCenter.OnEventListener{

    private TextView fromText = null;
    private TextView toText = null;
    private TextView timeText = null;
    private TextView currEditView = null;

    private List<String> currFromContent = null;
    private List<String> currToContent = null;
    private List<String> currTimeContent = null;

    private String currTimeStamp = "";

    private Context mConext;
    public PublishConfirmDialog(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PublishConfirmDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.confirm_publish_popup, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        lp.height=Utils.getScreenSize()[1];
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
//        setCanceledOnTouchOutside(true);
        fromText = (TextView)findViewById(R.id.new_bill_from_text);
        toText = (TextView)findViewById(R.id.new_bill_to_text);
        timeText = (TextView)findViewById(R.id.new_bill_time_text);

        //选择出发地监听事件
        LinearLayout pickFromBtn = (LinearLayout)findViewById(R.id.new_bill_from_btn);
        pickFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = fromText;
//                PickAddrDialog dialog = new PickAddrDialog();
//                dialog.show(getFragmentManager(), "pickaddr");
                PickAddrDialog dialog2 = new PickAddrDialog(mConext,R.style.dialog);
                dialog2.show();
            }
        });

        //选择目的地监听事件
        LinearLayout pickToBtn = (LinearLayout)findViewById(R.id.new_bill_to_btn);
        pickToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = toText;
//                PickAddrDialog dialog = new PickAddrDialog();
//                dialog.show(getFragmentManager(), "pickaddr");
                PickAddrDialog dialog2 = new PickAddrDialog(mConext,R.style.dialog);
                dialog2.show();
            }
        });

        //选择时间地监听事件
        LinearLayout pickTimeBtn = (LinearLayout)findViewById(R.id.new_bill_time_btn);
        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = timeText;
                PickTimeDialog dialog2 = new PickTimeDialog(mConext,R.style.dialog);
                dialog2.show();
//                PickTimeDialog dialog = new PickTimeDialog();
//                dialog.show(getFragmentManager(), "picktime");
            }
        });

        final PublishConfirmDialog self = this;
        LinearLayout submitBtn = (LinearLayout)findViewById(R.id.button_true);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currFromContent == null || currToContent == null || currTimeStamp == ""){
                    Toast toast = Toast.makeText(mConext, "请填写完整车单", 2);
                    toast.show();
                    return;
                }
                self.dismiss();
                final Bill bill = new Bill(Bill.BILLTYPE_TRUNK, new Address(currFromContent).toFullString(),
                        new Address(currToContent).toFullString(), currTimeStamp);

                NetService service = new NetService(mConext);
                service.sendBill(bill, new NetService.BillsCallBack() {
                    @Override
                    public void onCall(NetProtocol result, List<Bill> bills) {
                        if (result.code == NetProtocol.SUCCESS && bills.size()>0) {
                            DataEvent evt = new DataEvent(DataEvent.NEW_BILL, bills.get(0));
                            EventCenter.shared().dispatch(evt);
                        } else {
                            Utils.defaultNetProAction((BaseActivity)mConext, result);
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

        if(Utils.getVersionType(mConext).equals("driver")){
            EventLogUtils.EventLog(mConext, EventLogUtils.tthcc_driver_PublishConfirmDialog);
        }else {
            //TODO 货主版
        }

    }


    public void onEventCall(DataEvent e){
//        if(e.type.equals(DataEvent.TIME_SETTLE)){
//            currTimeStamp = (String)e.data;
//        }
        if(e.type.equals(DataEvent.ADDR_CHANGE)){
            List<String> data = (List<String>)e.data;
            Address addr = new Address(data);
            currEditView.setText(addr.toFullString());
            if(currEditView == fromText)
                currFromContent = data;
            else if(currEditView == toText)
                currToContent = data;
        }else if(e.type.equals(DataEvent.TIME_CHANGE)){
            Bundle d = (Bundle)e.data;

            List<String> data = d.getStringArrayList("timeList");
            currEditView.setText(Bill.listToString(data));
            currTimeStamp = d.getString("timestamp");
            currTimeContent = data;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventCenter.shared().addEventListener(DataEvent.ADDR_CHANGE, this);
        EventCenter.shared().addEventListener(DataEvent.TIME_CHANGE, this);
        EventCenter.shared().addEventListener(DataEvent.TIME_SETTLE, this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        EventCenter.shared().removeEventListener(DataEvent.ADDR_CHANGE, this);
        EventCenter.shared().removeEventListener(DataEvent.TIME_CHANGE, this);
        EventCenter.shared().removeEventListener(DataEvent.TIME_SETTLE, this);
    }
}
