package com.buerlab.returntrunk.controls;

import android.content.Context;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhongqiling on 14-7-15.
 */
public class MainController implements EventCenter.OnEventListener{

    private Context mContext = null;
    public long serverAdjustMills = 0;

    static private MainController instance = null;
    static public MainController shared(){
        if(instance == null){
            instance = new MainController();
        }
        return instance;
    }

    public void sync(JSONObject data){
        try{

            long serverTime = Long.valueOf(data.getString("serverTimeMills"));
            serverAdjustMills = serverTime-Calendar.getInstance().getTimeInMillis();
        }catch (JSONException e){

        }
    }

    public void init(Context context){
        mContext = context;

        Scheduler scheduler = new Scheduler();
        scheduler.addJobs(new BillOverDueCheckJob());
        new Thread(scheduler).start();

        EventCenter.shared().addEventListener(DataEvent.BILL_TO_OVERDUE, this);
    }

    public void onEventCall(DataEvent e){
        if(e.type.equals(DataEvent.BILL_TO_OVERDUE)){

            List<String> billids = (List<String>)e.data;
            NetService service = new NetService(mContext);
            service.overdueBills(billids, new NetService.NetCallBack() {
                @Override
                public void onCall(NetProtocol result) {
                    if(result.code == NetProtocol.SUCCESS){
                        if(result.arrayData != null){
                            JSONArray array = result.arrayData;
                            List<Bill> billsToRemove = new ArrayList<Bill>();
                            for(int i = 0; i < result.arrayData.length(); i++){
                                for(Bill bill : User.getInstance().getBills()){
                                    try{
                                        if(bill.id.equals(array.getString(i)))
                                            billsToRemove.add(bill);
                                    }catch (JSONException e){
                                        continue;
                                    }
                                }
                            }
                            for(Bill bill2 : billsToRemove)
                                User.getInstance().removeBill(bill2);

                            EventCenter.shared().dispatch(new DataEvent(DataEvent.BILL_OVERDUE, billsToRemove));
                        }
                    }
                }
            });
        }


    }
}
