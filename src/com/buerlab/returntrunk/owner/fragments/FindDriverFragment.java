package com.buerlab.returntrunk.owner.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.buerlab.returntrunk.FindBillListAdapter;
import com.buerlab.returntrunk.FindBillListAdapter2;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class FindDriverFragment extends BaseFragment implements EventCenter.OnEventListener {
    private FindBillListAdapter2 findBillListAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.find_good_frag, container, false);

        ListView list = (ListView)view.findViewById(R.id.find_bill_list);
        findBillListAdapter = new FindBillListAdapter2(getActivity());
        list.setAdapter(findBillListAdapter);

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
    public void onHiddenChanged(boolean hidden){
        if(!hidden){
            refresh();
        }
    }

    public void refresh(){
        NetService service = new NetService(getActivity());
        service.findBills(new NetService.BillsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<Bill> bills) {
                if(bills != null){
                    findBillListAdapter.setBills(bills);
                }
            }
        });
    }

    public void onEventCall(DataEvent e){
        Bill bill = (Bill)e.data;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+bill.phoneNum));
        getActivity().startActivity(intent);
    }
}