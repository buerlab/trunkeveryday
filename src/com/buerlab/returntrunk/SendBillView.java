package com.buerlab.returntrunk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by zhongqiling on 14-6-24.
 */
public class SendBillView extends FrameLayout{

    public SendBillView(Context context, Bill bill){
        super(context);

        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.new_bill_goods : R.layout.new_bill_trunk;
        LayoutInflater.from(context).inflate(layoutId, this);
    }


}
