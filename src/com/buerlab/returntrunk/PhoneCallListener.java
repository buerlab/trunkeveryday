package com.buerlab.returntrunk;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by zhongqiling on 14-7-24.
 */
public class PhoneCallListener extends PhoneStateListener{
    public interface OnPhoneCallBack{
        public void onCalling();
        public void onCallEnd();
    }

    private Context mContext = null;
    private OnPhoneCallBack mCallBack = null;

    public PhoneCallListener(Context context){
        mContext = context;
    }

    public void listen(OnPhoneCallBack callBack){
        mCallBack = callBack;
        TelephonyManager mTM = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(TelephonyManager.CALL_STATE_RINGING == state) {
        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
            mCallBack.onCalling();
        }
        if(TelephonyManager.CALL_STATE_IDLE == state) {
            //when this state occurs, and your flag is set, restart your app
            mCallBack.onCallEnd();
        }
    }


}
