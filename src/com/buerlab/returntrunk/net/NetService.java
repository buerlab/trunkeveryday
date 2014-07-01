package com.buerlab.returntrunk.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.LoadingDialog;
import com.buerlab.returntrunk.utils.FormatUtils;
import org.json.JSONArray;
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

    public interface UserCallBack{ public void onCall(NetProtocol result, JSONObject userData); }

    public interface BillsCallBack{ public void onCall(NetProtocol result, List<Bill> bills); }

    public interface CommentsCallBack{ public void onCall(NetProtocol result, List<Comment> comments); }

    private Activity mActivity = null;
    private Context mContext = null;

    public NetService(Activity activity){
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
    }

    public NetService(Context context){
        mContext = context;
    }

    public void register(String username, String psw, final NetCallBack callback){

        String parms = "phoneNum="+username+"&password="+psw;

        request(mContext.getString(R.string.server_addr)+"api/admin/register", parms, "POST", callback);
    }

    public void quickLogin(final NetCallBack callback){
        request(mContext.getString(R.string.server_addr)+"api/admin/qlogin", createReqParms(null), "POST", callback);
    }

    public void login(String username, String psw, final NetCallBack callback){

        String parms = "phoneNum="+username+"&password="+psw;

        request(mContext.getString(R.string.server_addr)+"api/admin/login", parms, "POST", callback);
    }

    //////////////////////////
    //USER
    //////////////////////////
    public void setUserData(Map<String, String> parms, NetCallBack callback){
        request(mContext.getString(R.string.server_addr)+"api/user", createReqParms(parms), "PUT", callback);
    }

    public void getUserData(NetCallBack callback){
        request(mContext.getString(R.string.server_addr)+"api/user", createReqParms(null), "GET", callback);
    }


    //////////////////////////
    //TRUNK
    //////////////////////////
    public  void deleteTrunk(String licensePlate,NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("licensePlate", licensePlate);
        request(mContext.getString(R.string.server_addr) + "api/user/trunk/delete", createReqParms(parmsMap), "POST", callback);
    }

    public void addUserTrunk(Trunk trunk, NetCallBack callback){
        request(mContext.getString(R.string.server_addr)+"api/user/trunk", createReqParms(trunk.toParmsMap()), "POST", callback);
    }

    public  void useTrunk(String licensePlate,NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("licensePlate", licensePlate);
        request(mContext.getString(R.string.server_addr) + "api/user/trunk/use", createReqParms(parmsMap), "POST", callback);
    }

    //////////////////////////
    //BILLS
    //////////////////////////

    public void getBills(int num, int count, final BillsCallBack callBack){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("num", String.valueOf(num));
        parmsMap.put("count", String.valueOf(count));

        request(mContext.getString(R.string.server_addr) + "api/bill", createReqParms(parmsMap), "GET", new NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS  && result.arrayData != null){
                    callBack.onCall(result, extractBills(result.arrayData));
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
        if(bill.billType.equals(Bill.BILLTYPE_GOODS)){
            parmsMap.put("material", bill.material);
            parmsMap.put("price", String.valueOf(bill.price));
            parmsMap.put("weight", String.valueOf(bill.weight));
        }else{
            parmsMap.put("trunkLength", String.valueOf(bill.trunkLength));
            parmsMap.put("trunkWeight", String.valueOf(bill.weight));
            parmsMap.put("licensePlate", bill.licensePlate);
        }

        request(mContext.getString(R.string.server_addr)+"api/bill", createReqParms(parmsMap), "POST", callBack);
    }

    public void deleteBill(Bill bill, final NetCallBack callBack){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("billid", bill.id);

        request(mContext.getString(R.string.server_addr)+"api/bill/delete", createReqParms(parmsMap), "POST", callBack);
    }

    public void findBills(final BillsCallBack callback){
        request(mContext.getString(R.string.server_addr)+"api/bill/conn", createReqParms(null), "GET", new NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS  && result.arrayData != null){
                    callback.onCall(result, extractBills(result.arrayData));
                }else{
                    Utils.defaultNetProAction(mActivity, result);
                }
            }
        });
    }

    public void getUpdateBill(String billId, NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("billId", billId);
        request(mContext.getString(R.string.server_addr)+"api/bill/update", createReqParms(parmsMap), "POST", callback);
    }

    public void inviteBill(String from, String to, NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("from", from);
        parmsMap.put("to", to);
        request(mContext.getString(R.string.server_addr)+"api/bill/invite", createReqParms(parmsMap), "POST", callback);
    }

    //////////////////////////
    //COMMENTS
    //////////////////////////

    public void getComments(int num, int count ,final CommentsCallBack callBack){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("num", String.valueOf(num));
        parmsMap.put("count", String.valueOf(count));

        request(mContext.getString(R.string.server_addr) + "api/comment", createReqParms(parmsMap), "GET", new NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS  && result.arrayData != null){
                    callBack.onCall(result, extractComments(result.arrayData));
                }else{
                    Utils.defaultNetProAction(mActivity, result);
                }
            }
        });
    }

    public void addComment(int starNum,
                           String commentText,
                           String fromUserName,
                           String fromUserId,
                           String toUserId,
                           String billId,
                           NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("starNum",Integer.toString(starNum) );
        parmsMap.put("text", commentText);
        parmsMap.put("fromUserName", fromUserName);
        parmsMap.put("fromUserId", fromUserId);
        parmsMap.put("toUserId", toUserId);
        parmsMap.put("billId", billId);
        urlRequest(mContext.getString(R.string.server_addr) + "api/comment", createReqParms(parmsMap), "POST", callback);
    }



    public void uploadLocation(double latitude,double longitude, String prov,String city,String district, NetCallBack callback){
        Map<String, String> parmsMap = new HashMap<String, String>();
        parmsMap.put("latitude", Double.toString(latitude) );
        parmsMap.put("longitude", Double.toString(longitude));

        if(prov != null){
            parmsMap.put("prov", prov);
        }

        if(city!=null){
            parmsMap.put("city", city);
        }

        if(district!=null){
            parmsMap.put("district", district);
        }


//        parmsMap.put("time",time);
        urlRequest(mContext.getString(R.string.server_addr) + "api/location", createReqParms(parmsMap), "POST", callback);
    }



    private String createReqParms(Map<String, String> parmsMap){
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), 0);
        String userId = pref.getString("userId", "");

        StringBuilder builder = new StringBuilder();
        builder.append("userId=" + userId);
        builder.append("&userType="+User.getInstance().getUserType());
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

        if(mActivity != null){
            final LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.show(mActivity.getFragmentManager(), "loading");
            urlRequest(url, parms, method, new NetCallBack() {
                @Override
                public void onCall(NetProtocol result) {
                    loadingDialog.dismiss();
                    callback.onCall(result);
                }
            });
        }else{
            urlRequest(url, parms, method, callback);
        }
    }

    public void urlRequest(String url, String parms, String method, final NetCallBack callback){
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

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer buffer = new StringBuffer();
                    String currline = "";
                    while((currline=reader.readLine()) != null){
                        buffer.append(currline);
                    }
                    saveCookie(conn);
                    reader.close();
                    in.close();

                    if(buffer.length() > 0){

                        JSONObject receiveData = new JSONObject(buffer.toString());
                        String dataString = receiveData.getString("data");
                        JSONObject data = null;
                        JSONArray arrayData = null;
                        try{
                            data = new JSONObject(dataString);
                        }catch (JSONException e){
                            data = null;
                            try{
                                arrayData = new JSONArray(dataString);
                            }catch (JSONException e2){
                                arrayData = null;
                            }
                        }
                        return new NetProtocol(receiveData.getInt("code"), receiveData.getString("msg"), data, arrayData);
                    }else{
                        return new NetProtocol(NetProtocol.NO_RESPONSE, "nothing response", null, null);
                    }


                }catch (Exception e) {
                    Log.d("error:", e.toString());
                    return new NetProtocol(NetProtocol.NET_EXCEPTION, e.toString(), null, null);
                }
                finally {
                    conn.disconnect();
                }

            }

            protected void onPostExecute(NetProtocol result){
                if(callback !=null){
                    callback.onCall(result);
                }


            }

        }.execute(url, parms, method);
    }

    public void uploadPic(String url,Bitmap bitmap, String filename, final NetCallBack callback){

        if(mActivity != null){
            final LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.show(mActivity.getFragmentManager(), "loading");
            _uploadPic(url, bitmap, filename, new NetCallBack() {
                @Override
                public void onCall(NetProtocol result) {
                    loadingDialog.dismiss();
                    callback.onCall(result);
                }
            });
        }else{
            _uploadPic(url, bitmap, filename, callback);
        }

    }

    public void _uploadPic(String url,Bitmap bitmap,String filename, final NetCallBack callBack){
        new AsyncTask<Object, Integer, NetProtocol>() {
            @Override
            protected NetProtocol doInBackground(Object... params) {
                String end ="\r\n";
                String twoHyphens ="--";
                String boundary ="*****";
                HttpURLConnection con = null;


                try
                {
                    String actionUrl = (String)params[0];
                    Bitmap bitmap = (Bitmap)params[1];
                    String filename = (String)params[2];
                    URL url =new URL(actionUrl);
                    con=(HttpURLConnection)url.openConnection();
                    /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary="+boundary);
                      /* 设置DataOutputStream */
                    DataOutputStream ds =
                            new DataOutputStream(con.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; "+
                            "name=\"file\";filename=\""+
                            filename +"\""+ end);
                    ds.writeBytes(end);
                     /* 取得文件的FileInputStream */
                    InputStream fStream = FormatUtils.getInstance().Bitmap2InputStream(bitmap);
        //            FileInputStream fStream =new FileInputStream(uploadFile);
                     /* 设置每次写入1024bytes */
                    int bufferSize =1024;
                    byte[] buffer =new byte[bufferSize];
                    int length =-1;
                     /* 从文件读取数据至缓冲区 */
                    while((length = fStream.read(buffer)) !=-1)
                    {
                      /* 将资料写入DataOutputStream中 */
                        ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fStream.close();
                    ds.flush();
                   /* 取得Response内容 */
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer stringBuffer = new StringBuffer();
                    String currline = "";
                    while((currline=reader.readLine()) != null){
                        stringBuffer.append(currline);
                    }
                    reader.close();
                    in.close();
                     /* 关闭DataOutputStream */
                    ds.close();

                    if(stringBuffer.length() > 0){

                        JSONObject receiveData = new JSONObject(stringBuffer.toString());
                        String dataString = receiveData.getString("data");
                        JSONObject data = null;
                        JSONArray arrayData = null;
                        try{
                            data = new JSONObject(dataString);
                        }catch (JSONException e){
                            data = null;
                            try{
                                arrayData = new JSONArray(dataString);
                            }catch (JSONException e2){
                                arrayData = null;
                            }
                        }
                        return new NetProtocol(receiveData.getInt("code"), receiveData.getString("msg"), data, arrayData);
                    }else{
                        return new NetProtocol(NetProtocol.NO_RESPONSE, "nothing response", null, null);
                    }

                }
                catch(Exception e)
                {
                    Log.d("error:", e.toString());
                    return new NetProtocol(NetProtocol.NET_EXCEPTION, e.toString(), null, null);
                }
                finally {
                    con.disconnect();
                }
            }

            protected void onPostExecute(NetProtocol result){
                if(callBack !=null){
                    callBack.onCall(result);
                }
            }
        }.execute(url, bitmap, filename);
    }

    private String getCookie(){
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), 0);
        return pref.getString("cookie", null);
    }

    private void saveCookie(HttpURLConnection connection){
        Map<String, List<String>> headers = connection.getHeaderFields();
        Iterator it = headers.keySet().iterator();

        while(it.hasNext()){
            Object key = it.next();
            if( "Set-Cookie".equalsIgnoreCase(((String)key))){
                SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.app_name), 0);
                String casheCookieString = pref.getString("cookie", "");
                Map<String, String> cookieMap = new HashMap<String, String>();
                if(!casheCookieString.isEmpty()){
                    for(String eachCookie : casheCookieString.split(";")){
                        String[] keyAndValue = eachCookie.split("=");
                        cookieMap.put(keyAndValue[0], keyAndValue[1]);
                    }
                }


                for(String cookieString : headers.get(key)){
                    List<HttpCookie> cookies = HttpCookie.parse(cookieString);
                    for(HttpCookie cookie : cookies){
                        if(!cookie.hasExpired()){
                            cookieMap.put(cookie.getName(), cookie.getValue());
                        }
                    }
                }

                StringBuilder stringBuilder = new StringBuilder();
                Iterator it2 = cookieMap.entrySet().iterator();
                while(it2.hasNext()){
                    Map.Entry entry = (Map.Entry) it2.next();
                    stringBuilder.append(entry.getKey()+"="+entry.getValue());
                    stringBuilder.append(";");
                }

                SharedPreferences pref2 = mContext.getSharedPreferences(mContext.getString(R.string.app_name), 0);
                SharedPreferences.Editor editor = pref2.edit();
                editor.putString("cookie", stringBuilder.toString());
                editor.commit();
                Log.i("---get cookie:", stringBuilder.toString());
            }
        }

    }

    private List<Bill> extractBills(JSONArray data){
        List<Bill> returnBills = new ArrayList<Bill>();

        try{
            for(int i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                Bill bill = new Bill(item.getString("billType"),item.getString("from"), item.getString("to"), item.getString("billTime"));
                bill.id = item.getString("billId");
                if(item.has("phoneNum"))
                    bill.phoneNum = item.getString("phoneNum");
                if(item.has("state"))
                    bill.state = item.getString("state");
                if(item.has("senderName"))
                    bill.setSenderName(item.getString("senderName"));
                if(item.has("material"))
                    bill.setGoodsInfo(item.getString("material"), 0, 0, "");
                returnBills.add(bill);
            }
            return returnBills;
        }catch (JSONException e){
            Toast toast = Toast.makeText(mContext, "json parse error when extract bills", 2);
            toast.show();
            return null;
        }
    }

    private List<Comment> extractComments(JSONArray data){
        List<Comment> retComments = new ArrayList<Comment>();
        try{
            for(int i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                Comment comment = new Comment(item.getInt("starNum"),
                        item.getString("commentTime"),
                        item.getString("fromUserName"),
                        item.getString("fromUserId"),
                        item.getString("toUserId"),
                        item.getString("billId"),
                        item.getString("text"));
                comment.id = item.getString("commentId");
                retComments.add(comment);
            }
            return retComments;
        }catch (JSONException e){
            Utils.showToast(mContext,"json parse error when extract comments");
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
            Toast toast = Toast.makeText(mContext, "json parse error when extract json array", 2);
            toast.show();

            return null;
        }

    }
}
