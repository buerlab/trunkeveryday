package com.buerlab.returntrunk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by zhongqiling on 14-6-8.
 */
public class BillLayoutFactory {

    static public View createInviteBill(LayoutInflater inflater, Bill bill){
        View inviteBill = inflater.inflate(R.layout.new_bill_invitation, null, false);
        int bViewId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = inflater.inflate(bViewId, null, false);
        ViewGroup container = (ViewGroup)inviteBill.findViewById(R.id.new_bill_invitation_bill);
        container.addView(bView);

        ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);

        return inviteBill;
    }

    static public View createFindBill(LayoutInflater inflater, Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.find_bill_goods : R.layout.find_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);

        ((TextView)bView.findViewById(R.id.find_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.find_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.find_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.find_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.find_bill_mat)).setText(bill.material);

        return bView;
    }

    static public View createSendBill(LayoutInflater inflater, Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.new_bill_goods : R.layout.new_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);

        ((TextView)bView.findViewById(R.id.new_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.new_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.new_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.new_bill_mat)).setText(bill.material);
        return bView;
    }

    static public View createBill(LayoutInflater inflater, Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = (View)inflater.inflate(layoutId, null, false);

        ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);

        return bView;
    }

}
