package com.buerlab.returntrunk.net;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by zhongqiling on 14-5-28.
 */
public class NetService {

    public interface NetCallBack{
        public void onCall(NetProtocol result);
    }

    public interface BillsCallBack{
        public void onCall(NetProtocol result, List<Bill> bills);
    }

    private Activity mActivity = null;

    public NetService(Activity activity){
        mActivity = activity;
    }

    public void register(String username, String psw, final NetCallBack callback){

        String parms = "username="+username+"&password="+psw;

        request(mActivity.getString(R.string.server_addr)+"api/admin/register", parms, "POST", callback);
    }

    public void quickLogin(final NetCallBack callback){
        request(mActivity.getString(R.string.server_addr)+"api/admin/qlogin", createReqParms(null), "POST", callback);
    }

    public void login(String username, String psw, final NetCallBack callback){

        String parms = "username="+username+"&password="+psw;

        request(mActivity.getString(R.string.server_addr)+"api/admin/login", parms, "POST", callback);
    }

    //////////////////////////
    //USER
    //////////////////////////
    public void setUserData(Map<String, String> parms, NetCallBack callback){
        request(mActivity.getString(R.string.server_addr)+"api/user", createReqParms(parms), "PUT", callback);
    }


    //////////////////////////
    //BILLS
    //////////////////////////

    public void getBills(int num, int count, final BillsCallBack callBack){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("num", String.valueOf(num));
        parmsMap.put("count", String.valueOf(count));

        request(mActivity.getString(R.string.server_addr) + "api/bill", createReqParms(parmsMap), "GET", new NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS  && result.data != null){
                    callBack.onCall(result, extractBills(result.data));
                }else{
                    Utils.defaultNetProAction(mActivity, result);
                }
            }
        });
    }

    public void sendBill(Bill bill, final NetCallBack callBack){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("billType", bill.billType);
        parmsMap.put("from", bill.from);
        parmsMap.put("to", bill.to);
        parmsMap.put("billTime", bill.time);
        parmsMap.put("material", bill.material);

        request(mActivity.getString(R.string.server_addr)+"api/bill", createReqParms(parmsMap), "POST", callBack);
    }

    public void findBills(final BillsCallBack callback){
        request(mActivity.getString(R.string.server_addr)+"api/bill/conn", createReqParms(null), "GET", new NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS  && result.data != null){
                    callback.onCall(result, extractBills(result.data));
                }else{
                    Utils.defaultNetProAction(mActivity, result);
                }
            }
        });
    }

    public void getUpdateBill(String billId, NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("billId", billId);
        request(mActivity.getString(R.string.server_addr)+"api/bill/update", createReqParms(parmsMap), "POST", callback);
    }

    public void inviteBill(String from, String to, NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("from", from);
        parmsMap.put("to", to);
        request(mActivity.getString(R.string.server_addr)+"api/bill/invite", createReqParms(parmsMap), "POST", callback);
    }

    private String createReqParms(Map<String, String> parmsMap){
        SharedPreferences pref = mActivity.getSharedPreferences(mActivity.getString(R.string.app_name), 0);
        String userId = pref.getString("userId", "");

        StringBuilder builder = new StringBuilder();
        builder.append("userId="+userId);
//        String str = "";
        if(parmsMap != null){
            for(Map.Entry<String, String> entry : parmsMap.entrySet()){
                builder.append("&"+entry.getKey()+"="+entry.getValue()+"&");
            }
//            str = builder.toString();
        }
        return builder.toString();
    }

    public void request(String url, String parms, String method, final NetCallBack callback){
        final LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(mActivity.getFragmentManager(), "loading");

        new AsyncTask<String, Integer, NetProtocol>(){
            protected NetProtocol doInBackground(String... args){

                URL url = null;
                HttpURLConnection conn = null;

                String method = args[2];
                String urlString = method == "GET" ? args[0]+"?"+args[1] : args[0];

                try{

                    url = new URL(urlString);

                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod(method);
                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                    String cookieString = getCookie();
                    if(cookieString != null){
                        Log.i("get local cookie:", cookieString);
                        conn.setRequestProperty("Cookie", cookieString);
                    }
                    conn.setDoInput(true);

                    if(method != "GET"){
                        conn.setDoOutput(true);
                        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                        out.write(args[1]);
                        out.flush();
                        out.close();
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String currline = "";
                    while((currline=reader.readLine()) != null){
                        buffer.append(currline);
                    }
                    saveCookie(conn);
                    reader.close();
                    if(buffer.length() > 0){

                        JSONObject receiveData = new JSONObject(buffer.toString());
                        String dataString = receiveData.getString("data");
                        JSONObject data = null;
                        try{
                            data = new JSONObject(dataString);
                        }catch (JSONException e){
                            data = null;
                        }
                        return new NetProtocol(receiveData.getInt("code"), receiveData.getString("msg"), data);
                    }else{
                        return new NetProtocol(NetProtocol.NO_RESPONSE, "nothing response", null);
                    }


                }catch (Exception e) {
                    Log.d("error:", e.toString());
                    return new NetProtocol(NetProtocol.NET_EXCEPTION, e.toString(), null);
                }
                finally {
                    conn.disconnect();
                }

            }

            protected void onPostExecute(NetProtocol result){
                loadingDialog.dismiss();
                callback.onCall(result);

            }

        }.execute(url, parms, method);
    }

    private String getCookie(){
        SharedPreferences pref = mActivity.getSharedPreferences(mActivity.getString(R.string.app_name), 0);
        return pref.getString("cookie", null);
    }

    private String saveCookie(HttpURLConnection connection){

        for(int i = 0; ; i++){
            String headerkey = connection.getHeaderFieldKey(i);
            String headerValue = connection.getHeaderField(i);

            if(headerkey==null && headerValue==null){
                return null;
            }
            if("Set-Cookie".equalsIgnoreCase(headerkey)){
                List<HttpCookie> cookies = HttpCookie.parse(headerValue);
                StringBuilder stringBuilder = new StringBuilder();
                for(HttpCookie cookie : cookies){
                    if(!cookie.hasExpired()){
                        stringBuilder.append(cookie.toString());
                        stringBuilder.append(";");
                    }
                }
                SharedPreferences pref = mActivity.getSharedPreferences(mActivity.getString(R.string.app_name), 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("cookie", stringBuilder.toString());
                editor.commit();
                Log.i("---get cookie:", stringBuilder.toString());

                return null;
            }
        }
    }

    private List<Bill> extractBills(JSONObject data){
        List<Bill> returnBills = new ArrayList<Bill>();

        try{
            for(JSONObject item : extractArray(data)){
                Bill bill = new Bill(item.getString("billType"),item.getString("from"), item.getString("to"), item.getString("billTime"));
                bill.id = item.getString("billId");
                bill.state = item.getString("state");


                if(item.has("senderName"))
                    bill.setSenderName(item.getString("senderName"));
                if(item.has("material"))
                    bill.setGoodsInfo(item.getString("material"), 0, 0);
                returnBills.add(bill);
            }
            return returnBills;
        }catch (JSONException e){
            Toast toast = Toast.makeText(mActivity.getApplicationContext(), "json parse error when extract bills", 2);
            toast.show();
            return null;
        }
    }

    private List<JSONObject> extractArray(JSONObject data){
        List<JSONObject> result = new ArrayList<JSONObject>();

        try{
            for(int i = 0; ; i++) {
                if (!data.has(String.valueOf(i))) {
                    return result;
                }
                result.add(data.getJSONObject(String.valueOf(i)));
            }
        }catch (JSONException e){
            Toast toast = Toast.makeText(mActivity.getApplicationContext(), "json parse error when extract json array", 2);
            toast.show();

            return null;
        }

    }
}
