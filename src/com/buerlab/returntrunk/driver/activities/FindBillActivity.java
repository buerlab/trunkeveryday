package com.buerlab.returntrunk.driver.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.widget.ListView;
import com.buerlab.returntrunk.Bill;
import com.buerlab.returntrunk.FindBillListAdapter;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-24.
 */
public class FindBillActivity extends BaseActivity implements EventCenter.OnEventListener{

    private FindBillListAdapter findBillListAdapter = null;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_bill_frag);
        getActionBar().setTitle("推荐货源");
        getActionBar().setDisplayHomeAsUpEnabled(true);


        ListView list = (ListView)findViewById(R.id.find_bill_list);
        findBillListAdapter = new FindBillListAdapter(this);
        list.setAdapter(findBillListAdapter);

        EventCenter.shared().addEventListener(DataEvent.PHONE_CALL, this);

        refresh();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        EventCenter.shared().removeEventListener(DataEvent.PHONE_CALL, this);
    }


    public void refresh(){
        NetService service = new NetService(this);
        service.findBills(new NetService.BillsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<Bill> bills) {
                if(bills != null){
                    findBillListAdapter.setBills(bills);
                }
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

    public void onEventCall(DataEvent e){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + (String) e.data));
        startActivity(intent);
    }
}