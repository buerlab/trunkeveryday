package com.buerlab.returntrunk;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.fragments.BillFragment;
import com.buerlab.returntrunk.models.Bill;

import java.util.*;

/**
 * Created by zhongqiling on 14-5-31.
 */
public class BillPageAdapter extends FragmentStatePagerAdapter{

    private int mCount = 1;
    private List<Bill> mBills = new ArrayList<Bill>();

    public BillPageAdapter(FragmentManager fm){
        super(fm);
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
    public BaseFragment getItem(int position){
        return new BillFragment(mBills.get(position));
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
