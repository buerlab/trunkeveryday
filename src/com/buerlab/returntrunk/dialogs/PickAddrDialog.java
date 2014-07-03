package com.buerlab.returntrunk.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.fragments.BaseDialogFragment;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.views.PickAddrView;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-26.
 */
public class PickAddrDialog extends BaseDialogFragment implements PickAddrView.OnAddrListener{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] provs = AssetManager.shared().getProvinces();

        PickAddrView view = new PickAddrView(getActivity());
        view.setListener(this);

        builder.setView(view)
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
        return dialog;
    }

    public void OnAddrChanged(List<String> addr){
        EventCenter.shared().dispatch(new DataEvent(DataEvent.ADDR_CHANGE, addr));
    }
}
