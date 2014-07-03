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
import com.buerlab.returntrunk.views.PickTimeView;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-28.
 */
public class PickTimeDialog extends BaseDialogFragment implements PickTimeView.OnTimeLisener{

    private String mTimeStamp = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] provs = AssetManager.shared().getProvinces();

        PickTimeView view = new PickTimeView(getActivity());
        view.setLisener(this);

        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventCenter.shared().dispatch(new DataEvent(DataEvent.TIME_SETTLE, mTimeStamp));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
        return dialog;
    }

    public void onTimeChange(List<String> time, String timeStamp){
        mTimeStamp = timeStamp;
        EventCenter.shared().dispatch(new DataEvent(DataEvent.TIME_CHANGE, time));
    }
}
