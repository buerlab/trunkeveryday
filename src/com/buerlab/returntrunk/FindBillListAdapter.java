package com.buerlab.returntrunk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.buerlab.returntrunk.views.ViewsFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhongqiling on 14-6-5.
 */
public class FindBillListAdapter extends BaseAdapter {

    private List<Bill> mBills = new ArrayList<Bill>();
    private LayoutInflater mInflater = null;

    public FindBillListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public void setBills(List<Bill> bills){
        mBills = bills;
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
        final Bill bill = mBills.get(position);
        View view = convertView;
        if(view == null){
            view = ViewsFactory.createFindBill(mInflater, bill);
        }else{
            ViewsFactory.fillFindBill(view, bill);
        }
        return view;
    }

}
