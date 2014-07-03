package com.buerlab.returntrunk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-13.
 */
public class DeleteTrunkDialog extends DialogFragment {
    public interface DeleteTrunkDialogListener{
        public void onDeleteTrunkDialogConfirm(Trunk trunk, int index);
        public void onDeleteTrunkDialogCancel();
    }

    private DeleteTrunkDialogListener mListener = null;

    public void setListener(DeleteTrunkDialogListener listener){ mListener = listener; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.delete_trunk_dialog, null, false);
        ListView trunksList = (ListView)view.findViewById(R.id.delete_trunk_dialog_list);
        trunksList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        List<String> trunkStr = new ArrayList<String>();
        for(Trunk trunk : User.getInstance().trunks){
            trunkStr.add(trunk.toString());
        }

//        TrunkListAdapter adapter = new TrunkListAdapter(getActivity(), trunkStr);
//        trunksList.setAdapter(adapter);
//
//        builder.setView(view)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });

        return builder.create();
    }
}
