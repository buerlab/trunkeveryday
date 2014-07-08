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

import java.util.List;

/**
 * Created by zhongqiling on 14-6-27.
 */
public class SendGoodsBillFragment extends BaseFragment implements EventCenter.OnEventListener{

    private SendBillListAdapter mAdapter = null;
    private boolean mInit = false;
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
    public void onDestroy(){
        super.onDestroy();
        EventCenter.shared().removeEventListener(DataEvent.NEW_BILL, this);
    }

    public void onEventCall(DataEvent event){
        if(event.type.equals(DataEvent.NEW_BILL)){
            final Bill bill = (Bill)event.data;
            addBill(bill);

            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "添加成功", 2);
            toast.show();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden && !mInit){
            initBills();
        }
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
                            tips.setAlpha(0.0f);
                        }else {
                            tips.setAlpha(1);
                        }
                    }
                }
            }
        });
    }

    private void addBill(Bill bill){
        mAdapter.addBill(bill);
        if(mAdapter.getCount()>0){
            tips.setAlpha(0.0f);
        }else {
            tips.setAlpha(1);
        }
    }
}