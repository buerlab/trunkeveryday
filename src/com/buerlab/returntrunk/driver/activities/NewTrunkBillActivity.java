package com.buerlab.returntrunk.driver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.adapters.TrunkSpinnerAdapter;
import com.buerlab.returntrunk.dialogs.PickAddrDialog;
import com.buerlab.returntrunk.dialogs.PickTimeDialog;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-23.
 */
public class NewTrunkBillActivity extends BaseActivity implements EventCenter.OnEventListener{

    LinearLayout mExtraContainer = null;

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

        setContentView(R.layout.new_trunk_bill_activity);
        getActionBar().setTitle("发送回程车单");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        fromText = (TextView)findViewById(R.id.new_bill_from_text);
        toText = (TextView)findViewById(R.id.new_bill_to_text);
        timeText = (TextView)findViewById(R.id.new_bill_time_text);
        mExtraContainer = (LinearLayout)findViewById(R.id.new_bill_extra_container);

        Spinner trunkSpinner = (Spinner)findViewById(R.id.new_bill_trunk_spinner);
        List<String> trunks = new ArrayList<String>();
        for(Trunk trunk : User.getInstance().trunks){
            trunks.add(trunk.toString());
        }
        TrunkSpinnerAdapter adapter = new TrunkSpinnerAdapter(this, trunks);
        trunkSpinner.setAdapter(adapter);

        //选择出发地监听事件
        Button pickFromBtn = (Button)findViewById(R.id.new_bill_from_btn);
        final NewTrunkBillActivity self = this;
        pickFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = fromText;
                PickAddrDialog dialog = new PickAddrDialog();
                dialog.show(getFragmentManager(), "pickaddr");
            }
        });

        //选择目的地监听事件
        Button pickToBtn = (Button)findViewById(R.id.new_bill_to_btn);
        pickToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = toText;
                PickAddrDialog dialog = new PickAddrDialog();
                dialog.show(getFragmentManager(), "pickaddr");
            }
        });

        //选择时间地监听事件
        Button pickTimeBtn = (Button)findViewById(R.id.new_bill_time_btn);
        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = timeText;
                PickTimeDialog dialog = new PickTimeDialog();
                dialog.show(getFragmentManager(), "picktime");
            }
        });

        Button submitBtn = (Button)findViewById(R.id.new_bill_activity_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currFromContent == null || currToContent == null || currTimeStamp == ""){
                    Toast toast = Toast.makeText(self, "请填写完整车单", 2);
                    toast.show();
                    return;
                }
                self.finish();
                final Bill bill = new Bill(Bill.BILLTYPE_TRUNK, new Address(currFromContent).toFullString(),
                        new Address(currToContent).toFullString(), currTimeStamp);

                NetService service = new NetService(self);
                service.sendBill(bill, new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if (result.code == NetProtocol.SUCCESS) {
                            DataEvent evt = new DataEvent(DataEvent.NEW_BILL, bill);
                            EventCenter.shared().dispatch(evt);
                        } else {
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
            Address addr = new Address(data);
            currEditView.setText(addr.toFullString());
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