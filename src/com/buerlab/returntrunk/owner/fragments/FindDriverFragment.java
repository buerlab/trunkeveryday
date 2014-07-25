package com.buerlab.returntrunk.owner.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.buerlab.returntrunk.FindBillListAdapter;
import com.buerlab.returntrunk.FindBillListAdapter2;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.RecommendBill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class FindDriverFragment extends BaseFragment implements EventCenter.OnEventListener {

    private static final String TAG = "FindDriverFragment";
    private PullToRefreshListView mListView = null;
    private boolean billInited = false;

    private List<RecommendBill> totalBills = new ArrayList<RecommendBill>();
    private List<RecommendBill> mBills = new ArrayList<RecommendBill>();
    private final int numberOfShowOnce = 5;
    private int billCursor = 0;


    private FindBillListAdapter2 findBillListAdapter = null;
    private LinearLayout noBillTips = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.find_driver_frag, container, false);

        noBillTips = (LinearLayout)view.findViewById(R.id.no_bill_tips);
        mListView = (PullToRefreshListView)view.findViewById(R.id.find_bill_list);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        findBillListAdapter = new FindBillListAdapter2(getActivity());
        mListView.setAdapter(findBillListAdapter);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute(refreshView.getCurrentMode()==PullToRefreshBase.Mode.PULL_FROM_END);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        EventCenter.shared().addEventListener(DataEvent.PHONE_CALL, this);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        EventCenter.shared().removeEventListener(DataEvent.PHONE_CALL, this);
    }

    @Override
    public void onShow(){
        if(!billInited) {
            refresh();
        }

    }

    public void refresh(){
        NetService service = new NetService(getActivity());
        service.findBills(new NetService.RecomendBillsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<RecommendBill> bills) {
                if(bills != null){
                    totalBills = bills;
                    mBills = new ArrayList<RecommendBill>();
                    billCursor = 0;
                    findBillListAdapter.setBills(mBills);
                    extendBills();

                    if(mBills.size()>0)
                        noBillTips.setVisibility(View.INVISIBLE);
                    else
                        noBillTips.setVisibility(View.VISIBLE);
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

    public void onEventCall(DataEvent e){
        Bill bill = (Bill)e.data;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+bill.phoneNum));
        getActivity().startActivity(intent);
    }

    private class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            // Simulates a background job.
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Boolean isPullEnd) {
            if(isPullEnd){
                extendBills();
                mListView.onRefreshComplete();
            }else
                refresh();

            super.onPostExecute(isPullEnd);
        }
    }
}