package com.buerlab.returntrunk.owner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.adapters.SendBillListAdapter;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.owner.activities.NewGoodsBillActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-27.
 */
public class SendGoodsBillFragment extends BaseFragment implements EventCenter.OnEventListener{

    private static final String TAG = "SendGoodsBillFragment";

    private SendBillListAdapter mAdapter = null;
    private boolean mBillsInit = false;
    private LinearLayout tips;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_goods_bill_frag, container, false);
        ImageView sendBtn = (ImageView)view.findViewById(R.id.send_bill_send_btn);

        ListView listView = (ListView)view.findViewById(R.id.send_bill_list);
        mAdapter = new SendBillListAdapter(getActivity());
        tips = (LinearLayout)view.findViewById(R.id.send_bill_frag_tips);
        listView.setAdapter(mAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewGoodsBillActivity.class);
                getActivity().startActivity(intent);
            }
        });

        EventCenter.shared().addEventListener(DataEvent.NEW_BILL, this);


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        EventCenter.shared().addEventListener(DataEvent.BILL_OVERDUE, this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventCenter.shared().removeEventListener(DataEvent.BILL_OVERDUE, this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventCenter.shared().removeEventListener(DataEvent.NEW_BILL, this);

    }

    public void onEventCall(DataEvent event){
        if(event.type.equals(DataEvent.NEW_BILL)){
            mAdapter.notifyDataSetChanged();

            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "添加成功", 2);
            toast.show();
        }else if(event.type.equals(DataEvent.BILL_OVERDUE)){
            List<Bill> billsToRemove = (List<Bill>)event.data;
            if(billsToRemove != null && billsToRemove.size()>0){
                Toast toast = Toast.makeText(this.getActivity(), "你有"+billsToRemove.size()+"个过期单子被删除", 2);
                toast.show();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onShow(){
        if(!mBillsInit)
            initBills();
        else
            mAdapter.notifyDataSetChanged();
    }

    private void initBills(){
        final SendBillListAdapter adapter = mAdapter;
        NetService service = new NetService(getActivity());
        service.getBills(0, -1, new NetService.BillsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<Bill> bills) {
                if(result.code == NetProtocol.SUCCESS){
                    if(bills != null) {
                        User.getInstance().initBills(bills);
                        adapter.setBills(bills);
                        if(bills.size()>0){
                            tips.setVisibility(View.GONE);
                        }else {
                            tips.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

}