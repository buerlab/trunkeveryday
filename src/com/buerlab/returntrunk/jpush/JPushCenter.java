package com.buerlab.returntrunk.jpush;

import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.PhoneCallNotifyDialog;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqiling on 14-6-19.
 */
public class JPushCenter {

    public interface OnJpushListener {
        public void onJPushCall(JPushProtocal protocal);
    }

    private Map<Integer, List<OnJpushListener>> eventMap = null;

    static private JPushCenter instance = null;
    static public JPushCenter shared(){
        if(instance == null){
            instance = new JPushCenter();
        }
        return instance;
    }

    public JPushCenter(){
        eventMap = new HashMap<Integer, List<OnJpushListener>>();
    }

    public void onPush(JPushProtocal protocal){
        if(protocal.code == JPushProtocal.JPUSH_PHONE_CALL){
            BaseActivity curr = BaseActivity.currActivity;
            if(curr != null){
                PhoneCallNotifyDialog dialog = new PhoneCallNotifyDialog(protocal.msg);
                dialog.show(curr.getFragmentManager(), "phonecall");
            }
        }
        else if(protocal.code == JPushProtocal.BILL_VISITED){

        }

        EventCenter.shared().dispatch(new DataEvent(DataEvent.JPUSH_INFORM, protocal));
    }

    public void register(int code, OnJpushListener callback){
        if(!eventMap.containsKey(code)){
            List<OnJpushListener> list = new ArrayList<OnJpushListener>();
            list.add(callback);
            eventMap.put(code, list);
        }else{
            eventMap.get(code).add(callback);
        }
    }

    public void unregister(int code, OnJpushListener callback){
        if(eventMap.containsKey(code) && eventMap.get(code).contains(callback)){
            eventMap.get(code).remove(callback);
        }

    }

    public void dispatch(JPushProtocal protocal){
        if(eventMap.containsKey(protocal.code)){
            for(OnJpushListener callback : eventMap.get(protocal.code))
                callback.onJPushCall(protocal);
        }
    }
}
