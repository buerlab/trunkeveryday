package com.buerlab.returntrunk;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.buerlab.returntrunk.fragments.BaseDialogFragment;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.models.User;


import java.text.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhongqiling on 14-5-27.
 */
public class NewBillDialog extends BaseDialogFragment {

    public interface NewBillDialogListener{
        public void onNewBillDialogConfirm(Bill bill);
        public void onNewBillDialogCancel();
    }

    NewBillDialogListener mListener;

    public NewBillDialog(NewBillDialogListener listener){
        mListener = listener;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

//        try{
//            mListener = (NewBillDialogListener)activity;
//        }catch (ClassCastException e){
//            throw new ClassCastException(activity.toString()
//                    + " must implement NewBillDialogListener");
//        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(User.getInstance().getNewBillDialog(), null);

        final EditText tFrom = (EditText)view.findViewById(R.id.new_bill_dialog_from);
        final EditText tTo = (EditText)view.findViewById(R.id.new_bill_dialog_to);
        final TimePicker timePicker = (TimePicker)view.findViewById(R.id.new_bill_dialog_time);
        final String billtype = User.getInstance().getBillType();

        if(User.getInstance().getUserType().equals(User.USERTYPE_TRUNK)){
            Spinner trunkSpinner = (Spinner)view.findViewById(R.id.new_bill_dialog_trunk);
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                    R.array.own_trunks, android.R.layout.simple_spinner_item);
//            for(Trunk trunk : User.getInstance().trunks){
//                adapter.add(trunk.toString());
//            }
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            List<String> trunks = new ArrayList<String>();
            for(Trunk trunk : User.getInstance().trunks){
                trunks.add(trunk.toString());
            }

            String [] trunkStrings = new String[trunks.size()];
            for(int i =0;i<trunks.size();i++){
                trunkStrings[i] = trunks.get(i).toString();
            }
            ArrayAdapter adapter =new  ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,trunkStrings);
            adapter.setDropDownViewResource(R.layout.trunk_span_item);

            trunkSpinner.setAdapter(adapter);

        }

        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        String timeStr = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " "
                                + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() + ":00";
                        DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

                        try {
                            Date date = format.parse(timeStr);
                            Bill bill = new Bill(billtype, tFrom.getText().toString(), tTo.getText().toString(), String.valueOf(date.getTime()));
                            if (billtype.equals(Bill.BILLTYPE_GOODS)) {
                                EditText matT = (EditText) view.findViewById(R.id.new_bill_dialog_mat);
                                EditText weightT = (EditText) view.findViewById(R.id.new_bill_dialog_weight);
                                EditText priceT = (EditText) view.findViewById(R.id.new_bill_dialog_price);
                                EditText commentT = (EditText) view.findViewById(R.id.new_bill_dialog_comment);
                                float price = Float.parseFloat(priceT.getText().toString());
                                float weight = Float.parseFloat(weightT.getText().toString());
                                bill.setGoodsInfo(matT.getText().toString(), price, weight, commentT.getText().toString());
                            } else if (billtype.equals(Bill.BILLTYPE_TRUNK)) {
                                Spinner trunkSpinner = (Spinner) view.findViewById(R.id.new_bill_dialog_trunk);
                                Trunk selectTrunk = User.getInstance().trunks.get(trunkSpinner.getSelectedItemPosition());
                                bill.setTrunk(selectTrunk);
                            }
                            mListener.onNewBillDialogConfirm(bill);

                        } catch (ParseException e) {
                            Log.d("ERROR:", e.toString());
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNewBillDialogCancel();
                    }
                });

        return builder.create();
    }
}