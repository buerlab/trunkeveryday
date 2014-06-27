package com.buerlab.returntrunk.events;


/**
 * Created by zhongqiling on 14-6-20.
 */
public class DataEvent {
    static public String PHONE_CALL = "phone_call";
    static public String NEW_BILL = "new_bill";
    static public String DELETE_BILL = "delete_bill";
    static public String ADDR_CHANGE = "address_change";

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