package com.buerlab.returntrunk.views;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.Bill;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.BillViewContxtMenu;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.List;


/**
 * Created by zhongqiling on 14-6-8.
 */
public class ViewsFactory {

    static public View createInviteBill(LayoutInflater inflater, Bill bill){
        View inviteBill = inflater.inflate(R.layout.new_bill_invitation, null, false);
        int bViewId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = inflater.inflate(bViewId, null, false);
        if(inviteBill != null && bView != null){
            ViewGroup container = (ViewGroup)inviteBill.findViewById(R.id.new_bill_invitation_bill);
            container.addView(bView);

            ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
            ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
            ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
            ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
            if(bill.billType.equals(Bill.BILLTYPE_GOODS))
                ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);
        }


        return inviteBill;
    }

    static public View createFindBill(LayoutInflater inflater, final Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.find_bill_goods : R.layout.find_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);
        if(bView != null){
            fillFindBill(bView, bill);

            Button phoneBtn = (Button)bView.findViewById(R.id.find_bill_phone);
            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!bill.phoneNum.isEmpty()){
                        EventCenter.shared().dispatch(new DataEvent(DataEvent.PHONE_CALL, bill.phoneNum));
                    }
                }
            });
        }

        return bView;
    }

    static public void fillFindBill(View bView, Bill bill){
        ((TextView)bView.findViewById(R.id.find_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.find_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.find_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.find_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.find_bill_mat)).setText(bill.material);
    }

    static public View createSendBill(LayoutInflater inflater, final Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.new_bill_goods : R.layout.new_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);
        fillSendBill(bView, bill);

        if(bView != null)
            bView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BillViewContxtMenu menu = new BillViewContxtMenu();
                    menu.setListener(new BillViewContxtMenu.OnBillContextListener() {
                        @Override
                        public void onDelete() {
                            NetService service = new NetService(BaseActivity.currActivity);
                            service.deleteBill(bill, new NetService.NetCallBack() {
                                @Override
                                public void onCall(NetProtocol result) {
                                    if(result.code == NetProtocol.SUCCESS){
                                        EventCenter.shared().dispatch(new DataEvent(DataEvent.DELETE_BILL, bill));
                                    }else{
                                        Utils.defaultNetProAction(BaseActivity.currActivity, result);
                                    }

                                }
                            });

                        }
                    });
                    menu.show(BaseActivity.currActivity.getFragmentManager(), "menu");

                    return true;
                }
            });

        return bView;
    }

    static public void fillSendBill(final View bView,final Bill bill){
        ((TextView)bView.findViewById(R.id.new_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.new_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.new_bill_time)).setText(Utils.tsToTimeString(bill.time));
    }

    static public View createBill(LayoutInflater inflater, Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = (View)inflater.inflate(layoutId, null, false);
        if(bView != null)
        {
            ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
            ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
            ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
            ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
            if(bill.billType.equals(Bill.BILLTYPE_GOODS))
                ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);
        }
        return bView;
    }

}
