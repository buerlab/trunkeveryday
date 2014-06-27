package com.buerlab.returntrunk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.adapters.TrunkListAdapter;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.views.PickAddrView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-23.
 */
public class NewTrunkBillActivity extends BaseActivity implements PickAddrView.OnAddrListener{

    LinearLayout mExtraContainer = null;

    private TextView fromText = null;
    private TextView toText = null;
    private TextView timeText = null;
    private TextView currEditView = null;

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
        TrunkListAdapter adapter = new TrunkListAdapter(this, trunks);
        trunkSpinner.setAdapter(adapter);

        Button pickFromBtn = (Button)findViewById(R.id.new_bill_from_btn);
        final NewTrunkBillActivity self = this;
        pickFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = fromText;
                mExtraContainer.removeAllViews();
                PickAddrView view = new PickAddrView(self);
                view.setListener(self);
                mExtraContainer.addView(view);
            }
        });

        Button pickToBtn = (Button)findViewById(R.id.new_bill_to_btn);
        pickToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currEditView = toText;
                mExtraContainer.removeAllViews();
                PickAddrView view = new PickAddrView(self);
                view.setListener(self);
                mExtraContainer.addView(view);
            }
        });

        Button submitBtn = (Button)findViewById(R.id.new_bill_activity_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataEvent evt = new DataEvent(DataEvent.NEW_BILL, new Bill(Bill.BILLTYPE_TRUNK, "beijing", "guangzhou","2:00"));
                EventCenter.shared().dispatch(evt);
                self.finish();
            }
        });

    }

    public void OnAddrChanged(List<String> addr){
        String fullAddr = "";
        for(String i : addr)
            fullAddr += i+"-";
        currEditView.setText(fullAddr.substring(0, fullAddr.length()-1));
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