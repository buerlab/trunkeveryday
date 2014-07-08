package com.buerlab.returntrunk.owner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.PickAddrDialog;
import com.buerlab.returntrunk.dialogs.PickTimeDialog;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.Address;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-27.
 */
public class NewGoodsBillActivity extends BaseActivity implements EventCenter.OnEventListener{

    private EditText goodsText = null;
    private EditText weightText = null;
    private EditText priceText = null;
    private EditText commentText = null;
    private TextView fromText = null;
    private TextView toText = null;
    private TextView timeText = null;
    private TextView currEditView = null;

    private List<String> currFromContent = null;
    private List<String> currToContent = null;
    private List<String> currTimeContent = null;
    private String currTimeStamp = "";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_goods_bill_activity);
        setActionBarLayout("发送货单",WITH_BACK);
//        getActionBar().setTitle("发送货单");
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        goodsText = (EditText)findViewById(R.id.new_bill_goods);
        weightText = (EditText)findViewById(R.id.new_bill_weight);
        priceText = (EditText)findViewById(R.id.new_bill_price);
        commentText = (EditText)findViewById(R.id.new_bill_comment);
        fromText = (TextView)findViewById(R.id.new_bill_from_text);
        toText = (TextView)findViewById(R.id.new_bill_to_text);
        timeText = (TextView)findViewById(R.id.new_bill_time_text);

        //选择出发地监听事件
        Button pickFromBtn = (Button)findViewById(R.id.new_bill_from_btn);
        final NewGoodsBillActivity self = this;
        pickFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = fromText;

                PickAddrDialog dialog = new PickAddrDialog(self,R.style.dialog);
                dialog.show();
            }
        });

        //选择目的地监听事件
        Button pickToBtn = (Button)findViewById(R.id.new_bill_to_btn);
        pickToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = toText;
                PickAddrDialog dialog = new PickAddrDialog(self,R.style.dialog);
                dialog.show();
            }
        });

        //选择时间地监听事件
        Button pickTimeBtn = (Button)findViewById(R.id.new_bill_time_btn);
        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = timeText;
                PickTimeDialog dialog = new PickTimeDialog(self,R.style.dialog);
                dialog.show();
            }
        });

        Button submitBtn = (Button)findViewById(R.id.new_bill_activity_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currFromContent == null || currToContent == null || currTimeContent == null ||
                   goodsText.getText().length() == 0 || priceText.getText().length() == 0 || weightText.getText().length() == 0){
                    Toast toast = Toast.makeText(self, "请填写完整货单", 2);
                    toast.show();
                    return;
                }
                Bill bill = new Bill(Bill.BILLTYPE_GOODS, new Address(currFromContent).toFullString(),
                        new Address(currToContent).toFullString(), currTimeStamp);

                bill.setGoodsInfo(goodsText.getText().toString(), Float.valueOf(priceText.getText().toString()),
                        Float.valueOf(weightText.getText().toString()), commentText.getText().toString());

                NetService service = new NetService(self);
                final Bill billToSend = bill;
                service.sendBill(billToSend, new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if(result.code == NetProtocol.SUCCESS){
                            DataEvent evt = new DataEvent(DataEvent.NEW_BILL, billToSend);
                            EventCenter.shared().dispatch(evt);

                            self.finish();
                        }else{
                            Utils.defaultNetProAction(self, result);
                        }
                    }
                });
            }
        });
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

    public void onEventCall(DataEvent e){
        if(e.type.equals(DataEvent.TIME_SETTLE)){
            currTimeStamp = (String)e.data;
        }
        else if(e.type.equals(DataEvent.ADDR_CHANGE)){
            List<String> data = (List<String>)e.data;
            currEditView.setText(new Address(data).toFullString());
            if(currEditView == fromText)
                currFromContent = data;
            else if(currEditView == toText)
                currToContent = data;
        }else if(e.type.equals(DataEvent.TIME_CHANGE)){
            List<String> data = (List<String>)e.data;
            currEditView.setText(Bill.listToString(data));
            currTimeContent = data;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
