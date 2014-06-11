package com.buerlab.returntrunk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.buerlab.returntrunk.Bill;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;


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
        View inviteBill = inflater.inflate(R.layout.find_bill, null, false);
        int bViewId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = inflater.inflate(bViewId, null, false);
        ViewGroup container = (ViewGroup)inviteBill.findViewById(R.id.find_bill_bill);
        container.addView(bView);

        ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);

        return inviteBill;
    }

}
