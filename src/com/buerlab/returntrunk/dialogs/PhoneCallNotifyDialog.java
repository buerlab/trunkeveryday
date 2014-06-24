package com.buerlab.returntrunk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhongqiling on 14-6-20.
 */
public class PhoneCallNotifyDialog extends DialogFragment {

    private String mName = "";
    public PhoneCallNotifyDialog(String name){
        mName = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("是否和"+mName+"达成交易？")
                .setPositiveButton("达成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("不达成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
