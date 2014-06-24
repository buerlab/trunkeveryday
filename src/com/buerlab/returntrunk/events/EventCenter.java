package com.buerlab.returntrunk.events;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.*;

/**
 * Created by zhongqiling on 14-6-20.
 */
public class EventCenter extends Handler implements Runnable{

    public interface OnEventListener {
        public void onEventCall(DataEvent e);
    }

    private static int CENTER_MSG = 0;

    private Map<String, List<OnEventListener>> eventMap = null;
    private Queue<DataEvent> eventQueue = null;
    private boolean mLoop = true;
    private int mLoopInterval = 100;

    static private EventCenter instance = null;
    static public EventCenter shared(){
        if(instance == null){
            instance = new EventCenter();
        }
        return instance;
    }

    public EventCenter(){

        eventMap = new HashMap<String, List<OnEventListener>>();
        eventQueue = new LinkedList<DataEvent>();

        new Thread(this).start();
    }

    public void addEventListener(String eventType, OnEventListener callback){
        if(!eventMap.containsKey(eventType)){
            List<OnEventListener> list = new ArrayList<OnEventListener>();
            list.add(callback);
            eventMap.put(eventType, list);
        }else{
            eventMap.get(eventType).add(callback);
        }
    }

    public void removeEventListener(String eventType, OnEventListener callback){
        if(eventMap.containsKey(eventType) && eventMap.get(eventType).contains(callback)){
            eventMap.get(eventType).remove(callback);
        }
    }

    public void dispatch(DataEvent e){
        eventQueue.offer(e);
    }

    @Override
    public void run(){
        while (mLoop){
            try{
                Thread.sleep(mLoopInterval);
            }catch (Exception e){
                Log.e("EventCenter Error", "WHILE SLEEPING");
            }

            DataEvent e = null;
            if((e = eventQueue.poll()) != null){
                this.obtainMessage(CENTER_MSG, e).sendToTarget();
            }
        }
    }

    @Override
    public void handleMessage(Message msg){
        if(msg.what == CENTER_MSG){
            DataEvent e = (DataEvent)msg.obj;
            if(eventMap.containsKey(e.type)){
                for(OnEventListener callback : eventMap.get(e.type))
                    callback.onEventCall(e);
            }
        }
    }
}
