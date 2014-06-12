package com.buerlab.returntrunk;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhongqiling on 14-5-27.
 */
public class NewBillDialog extends DialogFragment {

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

        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        String timeStr = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " "
                                + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() + ":00";
                        DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

                        try {
                            Date date = format.parse(timeStr);
                            Bill bill = new Bill(billtype, tFrom.getText().toString(), tTo.getText().toString(), String.valueOf(date.getTime()));
                            if(billtype == Bill.BILLTYPE_GOODS){
                                EditText matT = (EditText)view.findViewById(R.id.new_bill_dialog_mat);
                                EditText weightT = (EditText)view.findViewById(R.id.new_bill_dialog_weight);
                                EditText priceT = (EditText)view.findViewById(R.id.new_bill_dialog_price);
                                EditText commentT = (EditText)view.findViewById(R.id.new_bill_dialog_comment);
                                float price = Float.parseFloat(priceT.getText().toString());
                                float weight = Float.parseFloat(weightT.getText().toString());
                                bill.setGoodsInfo(matT.getText().toString(), price, weight, commentT.getText().toString());
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