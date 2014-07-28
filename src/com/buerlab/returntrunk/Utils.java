package com.buerlab.returntrunk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.controls.MainController;
import com.buerlab.returntrunk.driver.activities.InitDriverActivity;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.owner.activities.OwnerMainActivity;
import com.buerlab.returntrunk.views.StarsView;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhongqiling on 14-6-3.
 */
public class Utils {

    static public void defaultNetProAction(Activity activity, NetProtocol result){
        if(activity != null && activity.getApplicationContext() != null){
            if(result.code == NetProtocol.AUTH_ERROR){
                Toast toast = Toast.makeText(activity.getApplicationContext(), "请先登录", 2);
                toast.show();
                Intent intent = null;
//                if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK)){
                    intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                }
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast toast = Toast.makeText(activity.getApplicationContext(),
                        "http request error, code:"+result.code+"msg:"+result.msg, 2);
                toast.show();
            }
        }

    }
//
//    static public void safeSwitchToMainActivity(Activity from){
//        if(User.getInstance().nickName.isEmpty()){
//            from.startActivity(new Intent(from, InitDriverActivity.class));
//            from.finish();
//        }else{
//            from.startActivity(new Intent(from, OwnerMainActivity.class));
//            from.finish();
//        }
//    }

    public final static int FULL_TIME_STRING = 1;
    public final static int YEAR_MONTH_DAY = 2;
    public final static int YEAR_MONTH_DAY_TIME = 3;
    static public String timestampToDisplay(long ts, int format){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ts);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            String period = "上午";
            if(hours > 12){
                period = "下午";
                hours -= 12;
            }
            String ret ;

            if(format == FULL_TIME_STRING){
                ret = calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+
                        calendar.get(Calendar.DAY_OF_MONTH)+"日 "+period+" "+hours+"点";
            }else if(format ==YEAR_MONTH_DAY){
                Date date = new Date(ts);
                java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
                ret = format1.format(date);
            }else if(format ==YEAR_MONTH_DAY_TIME){
                ret=(calendar.get(Calendar.MONTH)+1)+"月"+
                    calendar.get(Calendar.DAY_OF_MONTH)+"日 "+period+" "+hours+"点";
            }else {
                //默认
                ret = calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+
                        calendar.get(Calendar.DAY_OF_MONTH)+"日 "+period+""+hours+"点";
            }
            return ret;

        }catch (Exception e){
            return String.valueOf(ts) ;
        }
    }

    public static  String timestampToDisplay(long ts){
        return timestampToDisplay(ts,YEAR_MONTH_DAY_TIME);
    }
    static public String timestampToDisplay(String ts){
        try{
            return timestampToDisplay(Long.parseLong(ts));
        }catch (Exception e){
            return "";
        }
    }
    static public String timestampToDisplay(String ts,int format){
        return timestampToDisplay(Long.parseLong(ts,format));
    }

    static public String timeListToString(List<String> input){
        String result = "";
        if(input != null && input.size() > 0){
            for(int i = 0; i < input.size(); i++)
                result += input.get(i)+" ";
            result = result.substring(0, result.length()-1);
        }
        return result;
    }

    static public int[] secTransform(long second){
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = (int)second%(24*3600);
        d = (int)second/(24*3600);
        h = temp/3600;
        int[] result = {d, h};
        return result;
    }



    static public List<JSONObject> extractArray(JSONObject data){
        List<JSONObject> result = new ArrayList<JSONObject>();

        try{
            for(int i = 0; ; i++) {
                if (!data.has(String.valueOf(i))) {
                    return result;
                }
                result.add(data.getJSONObject(String.valueOf(i)));
            }
        }catch (JSONException e){

            return null;
        }

    }

    public static void safeSetText(TextView view, String value){
        if(view != null)
            view.setText(value);
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static long getCurrTimeStamp(){
        return new Date().getTime()+ MainController.shared().serverAdjustMills;
    }

    //是负责身份证规则的字符串
    public static boolean matchIDNumRex(String idnum){

        //身份证正则表达式(15位)
        String isIDCard15="^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        //身份证正则表达式(18位)
        String isIDCard18="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[\\d|X|x]$";

        Pattern pattern = Pattern.compile(isIDCard15);
        Matcher matcher = pattern.matcher(idnum);

        boolean b= matcher.matches();

        if(b){
            return true;
        }else {
            pattern = Pattern.compile(isIDCard18);
            matcher = pattern.matcher(idnum);
            b = matcher.matches();
            if(b){
                return  true;
            }else {
                return  false;
            }
        }
    }

    public static void showToast(Context c,String text){
        Toast toast = Toast.makeText(c, text, 2);
        toast.show();
    }


    /* 请求码*/
    public static final int IMAGE_REQUEST_CODE = 500;
    public static final int CAMERA_REQUEST_CODE = 501;
    public static final int RESULT_REQUEST_CODE = 502;
    /*临时图片名称*/
    public static final String IMAGE_FILE_NAME = "tmp.jpg";

    //通用选择照片弹出框
    //后续要在activiy中实现onActivityResult，具体实现看EditIDNumActivity.java
    public static void showPickPicDialog(final Activity c ,String title) {
        String[] items = new String[] { "选择本地图片", "拍照" };

        new AlertDialog.Builder(c)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                c.startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (Utils.hasSDCard()) {
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                                }
                                c.startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public static void  init(Activity c){
        screenSize = getScreenSize(c);
        mDisplayMetrics= new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    private static int[] screenSize;
    private static DisplayMetrics mDisplayMetrics;
    public static int[] getScreenSize(){
        return screenSize;
    }

    private static int[] getScreenSize(Activity c){
        int[] size = new int[2] ;
        DisplayMetrics metrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;  // 屏幕宽度（像素）
        int height = metrics.heightPixels;  // 屏幕高度（像素）
        float density = metrics.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metrics.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        size[0] = width;
        size[1] = height;
        return  size;
    }

    public static int dip2px(float dipValue){
        float scale = mDisplayMetrics.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        return (int)(dipValue * scale +0.5f);
    }

    public static int px2dip(float pxValue){
        float scale = mDisplayMetrics.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        return (int)(pxValue / scale +0.5f);
    }

    public static void setGlobalData(Context c,String key, String val){
        SharedPreferences preferences=c.getSharedPreferences(c.getString(R.string.app_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static String getGlobalData(Context c,String key){
        SharedPreferences preferences=c.getSharedPreferences(c.getString(R.string.app_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        String val = preferences.getString(key, "");
        return val;
    }

    public static void clearGlobalData(Context c){
        SharedPreferences pref = c.getSharedPreferences(c.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static void setDriverVersion(Context c){
        SharedPreferences preferences=c.getSharedPreferences("version_type",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("version_type", "driver");
        editor.commit();
    }

    public static void setOwnerVersion(Context c){
        SharedPreferences preferences=c.getSharedPreferences("version_type",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("version_type", "owner");
        editor.commit();
    }
    public static String getVersionType(Context c){
        SharedPreferences preferences=c.getSharedPreferences("version_type",Context.MODE_PRIVATE);
        String versionType = preferences.getString("version_type", "");
        return versionType;
    }



    //用于友盟的集成测试
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }


//    平台版本 API级别
//    Android 3.0 11
//    Android 2.3.3 10
//    Android 2.3  9
//    Android 2.2  8
//    Android 2.1  7
//    Android 2.0.1  6
//    Android 2.0   5
//    Android 1.6    4
//    Android 1.5   3
//    Android 1.1   2
//    Android 1.0    1

    //获取sdk 号
    public static int getSDKVersionNumber() {

        return android.os.Build.VERSION.SDK_INT;
    }

    //判断是否有网络连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断WIFI网络是否可用
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断MOBILE网络是否可用
    public  static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    //获取友盟的配置信息
    public static String getConfigString(Context c, String key, String defaultStr){
            String value = MobclickAgent.getConfigParams(c, key);
            if (value == null || value.length()==0){
                value = defaultStr;
            }
            return  value;
    }

    public static String getStringByFloat(float num){
        java.text.DecimalFormat df= new java.text.DecimalFormat("#,##0.0");
        String strValue = df.format(num);
        return strValue;
    }
}
