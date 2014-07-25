package com.buerlab.returntrunk.driver.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BackBaseActivity;
import com.buerlab.returntrunk.dialogs.BillConfirmDialog;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.FindBillListAdapter;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.RecommendBill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.EventLogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-24.
 */
public class FindBillActivity extends BackBaseActivity implements EventCenter.OnEventListener{

    private static final String TAG = "FindBillActivity";
    private PullToRefreshListView mListView = null;
    private FindBillListAdapter findBillListAdapter = null;
    private LinearLayout tips = null;

    private List<RecommendBill> totalBills = null;
    private List<RecommendBill> mBills = null;
    private final int numberOfShowOnce = 5;
    private int billCursor = 0;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.find_good_frag);
        setActionBarLayout("推荐货源");

        mListView = (PullToRefreshListView)findViewById(R.id.find_bill_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        findBillListAdapter = new FindBillListAdapter(this);
        mListView.setAdapter(findBillListAdapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute(refreshView.getCurrentMode()==PullToRefreshBase.Mode.PULL_FROM_END);
            }
        });

        EventCenter.shared().addEventListener(DataEvent.PHONE_CALL, this);
        tips = (LinearLayout)findViewById(R.id.no_bill_tips);


        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);          //统计时长

        HashMap<String, String> param = new  HashMap<String,String>();
        param.put("test","1");
        param.put("test2","2");
        EventLogUtils.EventLog(self,EventLogUtils.tthcc_driver_findBill_enterActivity,param);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        EventCenter.shared().removeEventListener(DataEvent.PHONE_CALL, this);
    }


    public void refresh(){
        NetService service = new NetService(this);
        service.findBills(new NetService.RecomendBillsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<RecommendBill> bills) {
                if(bills != null){
                    totalBills = bills;
                    mBills = new ArrayList<RecommendBill>();
                    billCursor = 0;
                    findBillListAdapter.setBills(mBills);
                    extendBills();
                    if(bills.size()>0){
                         tips.setVisibility(View.GONE);
                    }
                }
                mListView.onRefreshComplete();
            }
        });
    }

    private void extendBills(){
        if(billCursor < totalBills.size()){
            int end = Math.min(totalBills.size(), billCursor+numberOfShowOnce);
            mBills.addAll(totalBills.subList(billCursor, end));
            billCursor += numberOfShowOnce;
            findBillListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
//                showDialog();
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

    private void showDialog(){
//        PhoneConfirmDialog2 dialog2 = new PhoneConfirmDialog2(this,R.style.dialog);
//        PublishConfirmDialog dialog2 = new PublishConfirmDialog(this,R.style.dialog);
        BillConfirmDialog dialog2 = new BillConfirmDialog(this, R.style.dialog);
        dialog2.show();
    }

    private class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... parms) {
            // Simulates a background job.
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            };
            return parms[0];
        }

        @Override
        protected void onPostExecute(Boolean isPullEnd) {
            if(isPullEnd) {
                extendBills();
                mListView.onRefreshComplete();
            }else
                refresh();

            super.onPostExecute(isPullEnd);
        }
    }

}