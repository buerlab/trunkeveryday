package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.views.SendBillView;
import com.buerlab.returntrunk.views.ViewsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class SendBillListAdapter extends BaseAdapter {

    private List<Bill> mBills = new ArrayList<Bill>();
    private Map<String, View> billViewMap = new HashMap<String, View>();
    private Context mContxt = null;

    public SendBillListAdapter(Context context){
        mContxt = context;
    }

    public void setBills(List<Bill> bills){
        mBills = bills;
        notifyDataSetChanged();
    }

    public List<Bill> getBills(){
        return mBills;
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
            view = new SendBillView(mContxt, bill);
        }else{
            ((SendBillView)view).update(bill);
        }
        billViewMap.put(bill.id, view);
        return view;
    }

    public View getViewOfBill(String billId){
        if(billViewMap.containsKey(billId))
            return billViewMap.get(billId);
        else
            return null;
    }


}
