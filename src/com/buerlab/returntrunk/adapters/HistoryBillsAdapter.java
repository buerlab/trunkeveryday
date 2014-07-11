package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ViewSwitcher;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.HistoryBill;
import com.buerlab.returntrunk.views.ViewsFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-4.
 */
public class HistoryBillsAdapter extends BaseAdapter {

    private List<HistoryBill> mBills = null;
    private LayoutInflater mInflater = null;

    public HistoryBillsAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mBills = new LinkedList<HistoryBill>();
    }

    public void setBills(List<HistoryBill> bills){
        mBills = bills;
        notifyDataSetChanged();
    }

    public void addBill(HistoryBill bill){
        mBills.add(bill);
        notifyDataSetChanged();
    }

    public List<HistoryBill> getBills(){
        return mBills;
    }

    public void removeBill(HistoryBill bill){
        mBills.remove(bill);
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
        HistoryBill bill = mBills.get(position);
        View view = convertView;
        if(view == null){
            view = ViewsFactory.createHisotryBill(mInflater, bill);
        }else{
            ViewsFactory.fillHistoryBill(view, bill);
        }

        return view;
    }
}
