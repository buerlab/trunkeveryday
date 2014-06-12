package com.buerlab.returntrunk.net;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by zhongqiling on 14-5-29.
 */
public class NetProtocol {

    static public final int SUCCESS = 0;
    static public final int ARGUMENT_ERROR = -1;
    static public final int DB_ERROR = -2;
    static public final int DB_DUPLICATE_ERROR = -3;
    static public final int DATAPROTOCOL_ERROR = -4;
    static public final int IS_NOT_AJAX_REQUEST = -5;
    static public final int FILE_ERROR = -6;
    static public final int AUTH_ERROR = -7;
    static public final int USER_EXISTED_ERROR = -8;

    static public final int NET_EXCEPTION = -20;
    static public final int NO_RESPONSE = -21;

    public int code;
    public String msg = "";
    public JSONObject data = null;
    public JSONArray arrayData = null;

    public NetProtocol(int _code, String _msg, JSONObject _data, JSONArray _arrayData){
        code = _code;
        msg = _msg;
        data = _data;
        arrayData = _arrayData;
    }

}
