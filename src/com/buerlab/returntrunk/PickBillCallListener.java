package com.buerlab.returntrunk;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.buerlab.returntrunk.models.Bill;

/**
 * Created by zhongqiling on 14-7-24.
 */
public class PickBillCallListener extends PhoneStateListener {

    private Bill mBill = null;

    public PickBillCallListener(Bill bill){
        mBill = bill;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(TelephonyManager.CALL_STATE_RINGING == state) {

        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.

        }
        if(TelephonyManager.CALL_STATE_IDLE == state) {
            //when this state occurs, and your flag is set, restart your app

        }
    }
}
