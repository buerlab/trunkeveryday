package com.buerlab.returntrunk.events;


/**
 * Created by zhongqiling on 14-6-20.
 */
public class DataEvent {
    static public final String PHONE_CALL = "phone_call";
    static public final String NEW_BILL = "new_bill";
    static public final String DELETE_BILL = "delete_bill";
    static public final String ADDR_CHANGE = "address_change";
    static public final String TIME_CHANGE = "time_change";
    static public final String TIME_SETTLE = "time_settle";
    static public final String JPUSH_INFORM = "jpush_inform";

    static public String USER_UPDATE = "user_update";
    public String type = "";
    public Object data = null;

    public DataEvent(String eventType, Object _data){
        type = eventType;
        data = _data;
    }

    public void setData(Object _data){
        data = _data;
    }
}
