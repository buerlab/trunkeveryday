package com.buerlab.returntrunk.controls;

import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-15.
 */
public class BillOverDueCheckJob extends ScheduleJob {

    @Override
    public void execute(){
        if(User.getInstance().getBills() != null){
            List<String> billsToRemove = new ArrayList<String>();
            for(Bill bill : User.getInstance().getBills()){
                if(bill.getValidLeftTime()<0){
                    billsToRemove.add(bill.id);
                }
            }

            if(billsToRemove.size() > 0){
                EventCenter.shared().dispatch(new DataEvent(DataEvent.BILL_TO_OVERDUE, billsToRemove));
            }
        }

    }
}
