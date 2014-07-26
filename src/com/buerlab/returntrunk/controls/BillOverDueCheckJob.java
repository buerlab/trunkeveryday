package com.buerlab.returntrunk.controls;

import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-7-15.
 */
public class BillOverDueCheckJob extends ScheduleJob {

    Map<String, Integer> billValidTimeMap = new HashMap<String, Integer>();

    @Override
    public void execute(){
        if(User.getInstance().getBills() != null){
            List<String> billsToRemove = new ArrayList<String>();
            List<String> billsToUpdate = new ArrayList<String>();
            for(Bill bill : User.getInstance().getBills()){
                int validtime = (int)bill.getValidLeftSec()/3600;
                if(billValidTimeMap.containsKey(bill.id) && billValidTimeMap.get(bill.id)!=validtime)
                    billsToUpdate.add(bill.id);
                billValidTimeMap.put(bill.id, validtime);

                if(bill.getValidLeftSec()<=0){
                    billsToRemove.add(bill.id);
                }
            }
            if(billsToUpdate.size() > 0)
                EventCenter.shared().dispatch(new DataEvent(DataEvent.BILL_DUE_UPDATE, billsToUpdate));
            if(billsToRemove.size() > 0){
                EventCenter.shared().dispatch(new DataEvent(DataEvent.BILL_TO_OVERDUE, billsToRemove));
            }
        }

    }
}
