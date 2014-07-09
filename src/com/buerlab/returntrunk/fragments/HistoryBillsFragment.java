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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_bills_frag, container, false);

        mListView = (PullToRefreshListView)view.findViewById(R.id.history_bills_list);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mAdapter = new HistoryBillsAdapter(inflater.getContext());
        mListView.setAdapter(mAdapter);


        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }
        });

        return view;
    }

    public void init(){
        List<Bill> bills = new ArrayList<Bill>();
        for(int i = 0; i < 5; i++){
            bills.add(new Bill(Bill.BILLTYPE_TRUNK, "guandong", "zhongshan", "july first"));
        }

        mAdapter.setBills(bills);
    }

    @Override
    public void onShow(){
        initBills();
    }

    private void initBills(){
        if(User.getInstance().getHistoryBills() == null){
            NetService service = new NetService(getActivity());
            service.getDefaultHistoryBills(new NetService.BillsCallBack() {
                @Override
                public void onCall(NetProtocol result, List<Bill> bills) {
                    if (result.code == NetProtocol.SUCCESS) {

                    }
                }
            });
        }else{
            mAdapter.setBills(User.getInstance().getHistoryBills());
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Bill> {

        @Override
        protected Bill doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return new Bill(Bill.BILLTYPE_TRUNK, "ffffff", "aaaaaaaa", "july jdfklasjf");
        }

        @Override
        protected void onPostExecute(Bill bill) {
            mAdapter.addBill(bill);

            // Call onRefreshComplete when the list has been refreshed.
            mListView.onRefreshComplete();

            super.onPostExecute(bill);
        }
    }
}
