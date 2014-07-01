package com.buerlab.returntrunk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;
import com.buerlab.returntrunk.driver.activities.LoginActivity;
import com.buerlab.returntrunk.driver.activities.MainActivity;
import com.buerlab.returntrunk.driver.activities.SetTrunkActivity;
import com.buerlab.returntrunk.driver.activities.initDriverActivity;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.owner.activities.OwnerLoginActivity;
import com.buerlab.returntrunk.owner.activities.OwnerMainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhongqiling on 14-6-3.
 */
public class Utils {

    static public void defaultNetProAction(Activity activity, NetProtocol result){
        if(activity.getApplicationContext() != null){
            if(result.code == NetProtocol.AUTH_ERROR){
                Toast toast = Toast.makeText(activity.getApplicationContext(), "请先登录", 2);
                toast.show();
                Intent intent = null;
                if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK)){
                    intent = new Intent(activity, LoginActivity.class);
                }else{
                    intent = new Intent(activity, OwnerLoginActivity.class);
                }

                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast toast = Toast.makeText(activity.getApplicationContext(),
                        "http request error, code:"+result.code+"msg:"+result.msg, 2);
                toast.show();
            }
        }

    }

    static public void safeSwitchToMainActivity(Activity from){
        if(User.getInstance().nickName.isEmpty()){
            from.startActivity(new Intent(from, initDriverActivity.class));
            from.finish();
        }else{
            from.startActivity(new Intent(from, OwnerMainActivity.class));
            from.finish();
        }
    }


    static public String tsToTimeString(String ts){
        try {
            Timestamp timestamp = new Timestamp(Long.parseLong(ts));
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            return format.format(timestamp);
        }catch (Exception e){
            return ts;
        }
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

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
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
}
