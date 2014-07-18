package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.BillViewContxtMenu;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.models.Address;

/**
 * Created by zhongqiling on 14-7-15.
 */
public class SendBillView extends LinearLayout {

    private Bill mBill = null;
    private int mBillLayout;
    private View mContainer = null;

    public SendBillView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendBillView(Context context, Bill bill){
        super(context);
        mBillLayout = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.new_bill_goods : R.layout.new_bill_trunk;
        init(context);
        update(bill);
    }

    private void init(Context context){
        mContainer = LayoutInflater.from(context).inflate(mBillLayout, this);

        mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BillViewContxtMenu menu = new BillViewContxtMenu();
                menu.setListener(new BillViewContxtMenu.OnBillContextListener() {
                    @Override
                    public void onDelete() {
                        NetService service = new NetService(BaseActivity.currActivity);
                        service.deleteBill(mBill, new NetService.NetCallBack() {
                            @Override
                            public void onCall(NetProtocol result) {
                                if(result.code == NetProtocol.SUCCESS){
                                    User.getInstance().removeBill(mBill);
                                    EventCenter.shared().dispatch(new DataEvent(DataEvent.DELETE_BILL, mBill));
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
    }

    public void update(Bill bill){
        mBill = bill;

        ((TextView)mContainer.findViewById(R.id.new_bill_from)).setText(new Address(bill.from).toShortString());
        ((TextView)mContainer.findViewById(R.id.new_bill_to)).setText(new Address(bill.to).toShortString());
        ((TextView)mContainer.findViewById(R.id.new_bill_time)).setText(Utils.timestampToDisplay(bill.time));
        ((TextView)mContainer.findViewById(R.id.new_bill_comment)).setText(String.valueOf(bill.comment));
        updateValidTime();
        updateVisitTimes();

        if(bill.billType.equals(Bill.BILLTYPE_GOODS)){
            ((TextView)mContainer.findViewById(R.id.new_bill_goods)).setText(bill.material);
            ((TextView)mContainer.findViewById(R.id.new_bill_weight)).setText(String.valueOf(bill.weight));
            ((TextView)mContainer.findViewById(R.id.new_bill_price)).setText(String.valueOf(bill.price));
        }
    }

    public void updateValidTime(){

        String validTimeStr = "";
        int[] validTime = Utils.secTransform(mBill.getValidLeftSec());
        if(validTime[0] > 0)
            validTimeStr = validTime[0]+"天";
        else if(validTime[1] > 0)
            validTimeStr = validTime[1]+"小时";
        else
            validTimeStr = "即将结束";

        ((TextView)mContainer.findViewById(R.id.new_bill_valid_time)).setText(validTimeStr);
    }

    public void updateVisitTimes(){
        ((TextView)mContainer.findViewById(R.id.new_bill_visitedtimes)).setText(String.valueOf(mBill.visitedTimes));
    }
}
