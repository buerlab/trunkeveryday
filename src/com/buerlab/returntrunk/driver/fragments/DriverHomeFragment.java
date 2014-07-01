package com.buerlab.returntrunk.driver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.driver.activities.FindBillActivity;
import com.buerlab.returntrunk.driver.activities.NewTrunkBillActivity;
import com.buerlab.returntrunk.adapters.SendBillListAdapter;
import com.buerlab.returntrunk.driver.activities.MainActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.List;


/**
 * Created by zhongqiling on 14-5-27.
 */
public class DriverHomeFragment extends BaseFragment implements NewBillDialog.NewBillDialogListener, EventCenter.OnEventListener {

    private TextView tips = null;

    private SendBillListAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.send_bill_frag, container, false);
        tips = (TextView)v.findViewById(R.id.send_bill_frag_tips);
        return v;
    }

    public void init(){
        ListView listView = (ListView)getView().findViewById(R.id.send_bill_list);
        mAdapter = new SendBillListAdapter(getActivity());
        listView.setAdapter(mAdapter);

        Button sendBtn = (Button)getView().findViewById(R.id.frag_send_btn);
        final DriverHomeFragment self = this;
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NewBillDialog dialog = new NewBillDialog(self);
//                dialog.show(getActivity().getFragmentManager(), "what");

                Intent intent = new Intent(getActivity(), NewTrunkBillActivity.class);
                getActivity().startActivity(intent);
            }
        });

        Button findBillBtn = (Button)getView().findViewById(R.id.send_bill_frag_goods);
        findBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindBillActivity.class);
                getActivity().startActivity(intent);
            }
        });

        EventCenter.shared().addEventListener(DataEvent.NEW_BILL, this);
        EventCenter.shared().addEventListener(DataEvent.DELETE_BILL, new EventCenter.OnEventListener() {
            @Override
            public void onEventCall(DataEvent e) {
                Bill bill = (Bill)e.data;
                if(bill != null){
                    mAdapter.removeBill(bill);
                }
            }
        });

        initBills();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventCenter.shared().removeEventListener(DataEvent.NEW_BILL, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            Bundle bundle = data.getExtras();
            Bill bill =  (Bill)bundle.get("bill");
            Toast toast = Toast.makeText(this.getActivity(), "get data from:"+bundle.getString("from"), 3);
            toast.show();
        }
    }

    @Override
    public void onEventCall(DataEvent e) {
        final Bill bill = (Bill)e.data;

        NetService service = new NetService(getActivity());
        final MainActivity parActivity = (MainActivity)getActivity();
        service.sendBill(bill, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    addBill(bill);

                    Toast toast = Toast.makeText(parActivity.getApplicationContext(), "添加成功", 2);
                    toast.show();
                }else{
                    Utils.defaultNetProAction(parActivity, result);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden){
//            billsAdapter.notifyDataSetChanged();
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

                        tips.setAlpha(0.0f);
                    }
                }
            }
        });
    }

    private void addBill(Bill bill){
        mAdapter.addBill(bill);
    }


    @Override
    public void onNewBillDialogConfirm(final Bill bill) {
        Log.i("---------CONFIRM","YEAH");

        NetService service = new NetService(getActivity());
        final MainActivity parActivity = (MainActivity)getActivity();
        service.sendBill(bill, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    addBill(bill);

                    Toast toast = Toast.makeText(parActivity.getApplicationContext(), "添加成功", 2);
                    toast.show();
                }else{
                    Utils.defaultNetProAction(parActivity, result);
                }
            }
        });
    }

    @Override
    public void onNewBillDialogCancel() {

    }


}