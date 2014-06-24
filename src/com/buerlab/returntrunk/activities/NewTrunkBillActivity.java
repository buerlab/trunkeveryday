package com.buerlab.returntrunk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.buerlab.returntrunk.Bill;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;

/**
 * Created by zhongqiling on 14-6-23.
 */
public class NewTrunkBillActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_trunk_bill_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitBtn = (Button)findViewById(R.id.new_bill_activity_submit);
        final Activity self = this;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataEvent evt = new DataEvent(DataEvent.NEW_BILL, new Bill(Bill.BILLTYPE_TRUNK, "beijing", "guangzhou","2:00"));
                EventCenter.shared().dispatch(evt);
                self.finish();
            }
        });

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