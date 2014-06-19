package com.buerlab.returntrunk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.buerlab.returntrunk.net.NetService;


/**
 * Created by zhongqiling on 14-6-13.
 */
public class AddCommentDialog extends DialogFragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    RadioGroup groupText0;
    RadioGroup groupText1;
    RadioGroup groupText2;
    RadioGroup groupText3;
    Button starBtn0;
    Button starBtn1;
    Button starBtn2;
    Button starBtn3;
    EditText otherText;
    View mView;

    RadioButton comment_text_0_1;
    RadioButton comment_text_0_2;
    RadioButton comment_text_0_3;

    RadioButton comment_text_1_1;
    RadioButton comment_text_1_2;
    RadioButton comment_text_1_3;

    RadioButton comment_text_2_1;
    RadioButton comment_text_2_2;
    RadioButton comment_text_2_3;

    RadioButton comment_text_3_1;

    RadioButton comment_text_0_other;
    RadioButton comment_text_1_other;
    RadioButton comment_text_2_other;
    RadioButton comment_text_3_other;

    NetService service;

    int currentStarNum;
    String currentText;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.star_btn_0:
                hideAllRadioGroup();
                groupText0.setVisibility(View.VISIBLE);
                currentStarNum = 0;
                break;
            case R.id.star_btn_1:
                hideAllRadioGroup();
                groupText1.setVisibility(View.VISIBLE);
                currentStarNum=1;
                break;
            case R.id.star_btn_2:
                hideAllRadioGroup();
                groupText2.setVisibility(View.VISIBLE);
                currentStarNum=2;
                break;
            case R.id.star_btn_3:
                hideAllRadioGroup();
                groupText3.setVisibility(View.VISIBLE);
                currentStarNum=3;
                break;
            case R.id.comment_text_0_other:
            case R.id.comment_text_1_other:
            case R.id.comment_text_2_other:
            case R.id.comment_text_3_other:
                otherText.setText("");
                otherText.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }


    public interface AddCommentDialogListener{
        public void onAddCommentDialogConfirm();
        public void oAddCommentDialogCancel();
    }

    private void init(){
        groupText0 = (RadioGroup)mView.findViewById(R.id.comment_group_0);
        groupText1 = (RadioGroup)mView.findViewById(R.id.comment_group_1);
        groupText2 = (RadioGroup)mView.findViewById(R.id.comment_group_2);
        groupText3 = (RadioGroup)mView.findViewById(R.id.comment_group_3);



        starBtn0 = (Button)mView.findViewById(R.id.star_btn_0);
        starBtn1 = (Button)mView.findViewById(R.id.star_btn_1);
        starBtn2 = (Button)mView.findViewById(R.id.star_btn_2);
        starBtn3 = (Button)mView.findViewById(R.id.star_btn_3);

        starBtn0.setOnClickListener(this);
        starBtn1.setOnClickListener(this);
        starBtn2.setOnClickListener(this);
        starBtn3.setOnClickListener(this);

        otherText = (EditText)mView.findViewById(R.id.other_text);
        otherText.setVisibility(View.GONE);


        comment_text_0_1 = (RadioButton)mView.findViewById(R.id.comment_text_0_1);
        comment_text_0_2 = (RadioButton)mView.findViewById(R.id.comment_text_0_2);
        comment_text_0_3 = (RadioButton)mView.findViewById(R.id.comment_text_0_3);

        comment_text_1_1 = (RadioButton)mView.findViewById(R.id.comment_text_1_1);
        comment_text_1_2 = (RadioButton)mView.findViewById(R.id.comment_text_1_2);
        comment_text_1_3 = (RadioButton)mView.findViewById(R.id.comment_text_1_3);

        comment_text_2_1 = (RadioButton)mView.findViewById(R.id.comment_text_2_1);
        comment_text_2_2 = (RadioButton)mView.findViewById(R.id.comment_text_2_2);
        comment_text_2_3 = (RadioButton)mView.findViewById(R.id.comment_text_2_3);

        comment_text_3_1 = (RadioButton)mView.findViewById(R.id.comment_text_3_1);

        comment_text_0_other =  (RadioButton)mView.findViewById(R.id.comment_text_0_other);
        comment_text_1_other =  (RadioButton)mView.findViewById(R.id.comment_text_1_other);
        comment_text_2_other =  (RadioButton)mView.findViewById(R.id.comment_text_2_other);
        comment_text_3_other =  (RadioButton)mView.findViewById(R.id.comment_text_3_other);


        comment_text_0_1.setOnClickListener(this);
        comment_text_0_2.setOnClickListener(this);
        comment_text_0_3.setOnClickListener(this);

        comment_text_1_1.setOnClickListener(this);
        comment_text_1_2.setOnClickListener(this);
        comment_text_1_3.setOnClickListener(this);

        comment_text_2_1.setOnClickListener(this);
        comment_text_2_2.setOnClickListener(this);
        comment_text_2_3.setOnClickListener(this);

        comment_text_3_1.setOnClickListener(this);

        comment_text_0_other.setOnClickListener(this);
        comment_text_1_other.setOnClickListener(this);
        comment_text_2_other.setOnClickListener(this);
        comment_text_3_other.setOnClickListener(this);

        service = new NetService(getActivity());
        currentStarNum = 0;
    }

    private void hideAllRadioGroup(){
        groupText0.setVisibility(View.GONE);
        groupText1.setVisibility(View.GONE);
        groupText2.setVisibility(View.GONE);
        groupText3.setVisibility(View.GONE);
        otherText.setVisibility(View.GONE);
    }
    private AddCommentDialogListener mListener = null;

    public void setListener(AddCommentDialogListener listener){ mListener = listener; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.comment_add_dialog, null, false);

        init();
        builder.setView(mView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        TextView typeText = (TextView)view.findViewById(R.id.set_trunk_type);
//                        TextView lengthText = (TextView)view.findViewById(R.id.set_trunk_length);
//                        TextView loadText = (TextView)view.findViewById(R.id.set_trunk_load);
//                        TextView lisenceText = (TextView)view.findViewById(R.id.set_trunk_licensePlate);
//                        String type = typeText.getText().toString();
//                        float length = Float.valueOf(lengthText.getText().toString());
//                        float load = Float.valueOf(loadText.getText().toString());
//                        String lisence = lisenceText.getText().toString();
//                        if(mListener != null)
//                            mListener.onAddCommentDialogConfirm(new Trunk(type, length, load, lisence));

                        service.addComment(1,"nice","李大爷",
                                User.getInstance().userId,
                                User.getInstance().userId,
                                "5399818a7938ee399731c688",
                                null);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mListener!=null)
                            mListener.oAddCommentDialogCancel();
                    }
                });

        return builder.create();
    }
}
