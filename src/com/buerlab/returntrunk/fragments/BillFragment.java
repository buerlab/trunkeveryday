package com.buerlab.returntrunk.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.buerlab.returntrunk.Bill;
import com.buerlab.returntrunk.BillInvitationListAdapter;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.User;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhongqiling on 14-5-31.
 */
public class BillFragment extends BaseFragment {

    private Bill mBill;
    private BillInvitationListAdapter inivtationAdapter = null;

    public BillFragment(Bill bill){
        mBill = bill;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.new_bill_frag, container, false);
        //init Bill
        View v = inflater.inflate(User.getInstance().getNewBillId(), null, false);
        ViewGroup billContainer = (ViewGroup)fragView.findViewById(R.id.new_bill_frag_bill);
        billContainer.addView(v);

        TextView tFrom = null;
        TextView tTo = null;
        TextView tTime = null;
        if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK)){
            tFrom = (TextView)v.findViewById(R.id.new_bill_trunk_from);
            tTo = (TextView)v.findViewById(R.id.new_bill_trunk_to);
            tTime = (TextView)v.findViewById(R.id.new_bill_trunk_time);
            TextView tTrunk = (TextView)v.findViewById(R.id.new_bill_trunk_trunk);
            tTrunk.setText(mBill.getTrunk().toString());
        }else if(User.getInstance().getUserType().equals(User.USERTYPE_OWNER)){
            ((TextView)v.findViewById(R.id.new_bill_goods_mat)).setText(mBill.material);
            tFrom = (TextView)v.findViewById(R.id.new_bill_goods_from);
            tTo = (TextView)v.findViewById(R.id.new_bill_goods_to);
            tTime = (TextView)v.findViewById(R.id.new_bill_goods_time);
        }
        tFrom.setText(mBill.from);
        tTo.setText(mBill.to);

        String timeText = "";
        try {
            Timestamp timestamp = new Timestamp(Long.parseLong(mBill.time));
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

            timeText = format.format(timestamp);
        }catch (NumberFormatException e){
            Log.i("time invalid", e.toString());
        }
        tTime.setText(timeText);

        //init invitation list
        ListView invitationList = (ListView)fragView.findViewById(R.id.new_bill_frag_invitation);
        inivtationAdapter = new BillInvitationListAdapter(getActivity());
        invitationList.setAdapter(inivtationAdapter);

        Button refreshBtn = (Button)fragView.findViewById(R.id.new_bill_frag_refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bill> bills = new ArrayList<Bill>();
                for(int i = 0; i < 5; i++){
                    bills.add(new Bill(Bill.BILLTYPE_TRUNK, "GD", "SZ", "2:00"));
                }
                inivtationAdapter.setBills(bills);
            }
        });
        return fragView;
    }
}