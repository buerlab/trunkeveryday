package com.buerlab.returntrunk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by zhongqiling on 14-6-24.
 */
public class BillViewContxtMenu extends DialogFragment {

    public interface OnBillContextListener{
        public void onDelete();
    }

    private OnBillContextListener mLinstener = null;

    public void setListener(OnBillContextListener listener){ mLinstener = listener; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CharSequence[] menuItems = {"删除", "取消"};
        builder.setItems(menuItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0 && mLinstener != null){
                    mLinstener.onDelete();
                }
            }
        });

        return builder.create();
    }
}
