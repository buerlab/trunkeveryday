package com.buerlab.returntrunk.jpush;

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
