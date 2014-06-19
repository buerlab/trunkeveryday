package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.buerlab.returntrunk.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class SendBillListAdapter extends BaseAdapter {

    private List<Bill> mBills = new ArrayList<Bill>();
    private LayoutInflater mInflater = null;

    public SendBillListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public void setBills(List<Bill> bills){
        mBills = bills;
        notifyDataSetChanged();
    }

    public void addBill(Bill bill){
        mBills.add(bill);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mBills.size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        Bill bill = mBills.get(position);
        View view = convertView;
        if(view == null){
            view = BillLayoutFactory.createSendBill(mInflater, bill);
        }else{
            ((TextView)view.findViewById(R.id.new_bill_from)).setText(bill.from);
            ((TextView)view.findViewById(R.id.new_bill_to)).setText(bill.to);
            ((TextView)view.findViewById(R.id.new_bill_time)).setText(Utils.tsToTimeString(bill.time));
            if(bill.billType.equals(Bill.BILLTYPE_GOODS))
                ((TextView)view.findViewById(R.id.new_bill_mat)).setText(bill.material);
        }

        return view;
    }
}
