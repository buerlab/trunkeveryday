package com.buerlab.returntrunk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.RecommendBill;
import com.buerlab.returntrunk.views.ViewsFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhongqiling on 14-6-5.
 */
public class FindBillListAdapter2 extends BaseAdapter {

    private List<RecommendBill> mBills = new ArrayList<RecommendBill>();
    private LayoutInflater mInflater = null;
    private Context mContext;
    public FindBillListAdapter2(Context context){
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setBills(List<RecommendBill> bills){
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
        final RecommendBill bill = mBills.get(position);
        View view = convertView;
        if(view == null){

            view = ViewsFactory.createFindBill(mInflater, bill);
        }else{
            ViewsFactory.fillFindBill(view, bill);
        }
        return view;
    }

}
