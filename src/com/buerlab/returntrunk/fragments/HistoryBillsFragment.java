package com.buerlab.returntrunk.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.adapters.HistoryBillsAdapter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.HistoryBill;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-4.
 */
public class HistoryBillsFragment extends BaseFragment{

    private PullToRefreshListView mListView = null;
    private HistoryBillsAdapter mAdapter = null;
    private boolean mHasInit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_bills_frag, container, false);

        mListView = (PullToRefreshListView)view.findViewById(R.id.history_bills_list);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mAdapter = new HistoryBillsAdapter(inflater.getContext());
        mListView.setAdapter(mAdapter);


        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                new GetDataTask().execute(refreshView.getCurrentMode()==PullToRefreshBase.Mode.PULL_FROM_END);
            }
        });

        return view;
    }

    @Override
    public void onShow(){
        initBills();
    }

    private void initBills(){
        if(true){
            NetService service = new NetService(getActivity());
            service.getDefaultHistoryBills(new NetService.HistoryBillsCallBack() {
                @Override
                public void onCall(NetProtocol result, List<HistoryBill> bills) {
                    if (result.code == NetProtocol.SUCCESS) {
                        User.getInstance().initHistoryBills(bills);
                        mAdapter.setBills(User.getInstance().getHistoryBills());
                        mHasInit = true;
                    }
                }
            });
        }else{
            mAdapter.setBills(User.getInstance().getHistoryBills());
        }
    }

    private void extendBills(final boolean isPrev){
        List<HistoryBill> bills = User.getInstance().getHistoryBills();
        String fromId = "";
        if(bills.size() > 0){
            HistoryBill last = isPrev ? bills.get(bills.size()-1) : bills.get(0);
            fromId = last.id;
        }

        final HistoryBillsFragment self = this;
        NetService service = new NetService(self.getActivity().getApplicationContext());
        service.getHistoryBill(fromId, isPrev, new NetService.HistoryBillsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<HistoryBill> bills) {
                if(result.code == NetProtocol.SUCCESS && bills.size()>0){
                    if(isPrev)
                        User.getInstance().extendHistoryBills(bills);
                    else
                        User.getInstance().headExtendHistoryBills(bills);
                    mAdapter.notifyDataSetChanged();
                }
                mListView.onRefreshComplete();
            }
        });
    }

    private class GetDataTask extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return  params[0];
        }

        @Override
        protected void onPostExecute(Boolean value) {
            extendBills(value);

            super.onPostExecute(value);
        }
    }
}
