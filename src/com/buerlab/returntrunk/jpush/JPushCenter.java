package com.buerlab.returntrunk.jpush;

import android.content.Context;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.BillConfirmDialog;
import com.buerlab.returntrunk.dialogs.PhoneCallNotifyDialog;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.jpush.models.BillRequest;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import org.json.JSONObject;
import java.util.*;

/**
 * Created by zhongqiling on 14-6-19.
 */
public class JPushCenter {

    public interface OnJpushListener {
        public void onJPushCall(JPushProtocal protocal);
    }

    private Context mContext = null;

    static private JPushCenter instance = null;
    static public JPushCenter shared(){
        if(instance == null){
            instance = new JPushCenter();
        }
        return instance;
    }
    public void init(Context context){ mContext = context; }

    public void onPush(final JPushProtocal protocal){

        if(protocal.code == JPushProtocal.JPUSH_PHONE_CALL){
            BaseActivity curr = BaseActivity.currActivity;
            if(curr != null){
                try{
                    BillRequest req = new BillRequest(new JSONObject(protocal.msg));
                    if(BaseActivity.currActivity != null){
                        BillConfirmDialog dialog = new BillConfirmDialog(BaseActivity.currActivity, R.style.dialog, req);
                        dialog.show();
                    }
                }catch (Exception e){

                }

            }
        }
        else if(protocal.code == JPushProtocal.BILL_VISITED){
            NetService service = new NetService(mContext);
            service.getVisitedBills(new NetService.NetCallBack() {
                @Override
                public void onCall(NetProtocol result) {
                    if (result.code == NetProtocol.SUCCESS && result.data != null) {
                        JSONObject billDict = result.data;
                        Iterator it = billDict.keys();
                        while (it.hasNext()) {
                            String billid = (String) it.next();
                            Bill bill = User.getInstance().getBill(billid);
                            if (bill != null) {
                                try {
                                    bill.visitedTimes = billDict.getInt(billid);
                                } catch (Exception e) {
                                }
                            }
                        }
                        EventCenter.shared().dispatch(new DataEvent(DataEvent.JPUSH_INFORM, protocal));
                    }
                }
            });

        }

        EventCenter.shared().dispatch(new DataEvent(DataEvent.JPUSH_INFORM, protocal));
    }

    public void onOpenNotification(JPushProtocal protocal){

    }
}
