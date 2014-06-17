package com.buerlab.returntrunk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * Created by zhongqiling on 14-6-13.
 */
public class NewTrunkDialog extends DialogFragment{

    public interface NewTrunkDialogListener{
        public void onNewTrunkDialogConfirm(Trunk trunk);
        public void onNewTrunkDialogCancel();
    }

    private NewTrunkDialogListener mListener = null;

    public void setListener(NewTrunkDialogListener listener){ mListener = listener; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.new_trunk_dialog, null, false);

        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView typeText = (TextView)view.findViewById(R.id.set_trunk_type);
                        TextView lengthText = (TextView)view.findViewById(R.id.set_trunk_length);
                        TextView loadText = (TextView)view.findViewById(R.id.set_trunk_load);
                        TextView lisenceText = (TextView)view.findViewById(R.id.set_trunk_licensePlate);
                        String type = typeText.getText().toString();
                        float length = Float.valueOf(lengthText.getText().toString());
                        float load = Float.valueOf(loadText.getText().toString());
                        String lisence = lisenceText.getText().toString();
                        if(mListener != null)
                            mListener.onNewTrunkDialogConfirm(new Trunk(type, length, load, lisence));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mListener!=null)
                            mListener.onNewTrunkDialogCancel();
                    }
                });

        return builder.create();
    }
}
