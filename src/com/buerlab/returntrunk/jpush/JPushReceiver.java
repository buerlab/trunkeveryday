package com.buerlab.returntrunk.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.PhoneCallNotifyDialog;

/**
 * Created by zhongqiling on 14-6-19.
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + msg);

            JPushProtocal protocal = new JPushProtocal(msg);
            BaseActivity curr = BaseActivity.currActivity;
            if(curr != null){
                PhoneCallNotifyDialog dialog = new PhoneCallNotifyDialog(protocal.msg);
                dialog.show(curr.getFragmentManager(), "phonecall");
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));

            JPushProtocal protocal = new JPushProtocal(msg);
            JPushCenter.shared().dispatch(protocal);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
}
