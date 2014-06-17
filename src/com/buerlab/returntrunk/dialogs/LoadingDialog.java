package com.buerlab.returntrunk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhongqiling on 14-6-3.
 */
public class LoadingDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("loading");
        return builder.create();
    }
}