package com.buerlab.returntrunk.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.driver.DriverUtils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.MultiPicSelector.Util;
import com.buerlab.returntrunk.views.PickAddrView;

import java.util.List;

/**
 * Created by teddywu on 14-7-7.
 */
public class AddCommentDialog extends Dialog implements View.OnClickListener{

    private Context mConext;
    LinearLayout groupText0;
    LinearLayout groupText1;
    LinearLayout groupText2;
    LinearLayout groupText3;
    LinearLayout starBtn0;
    LinearLayout starBtn1;
    LinearLayout starBtn2;
    LinearLayout starBtn3;
    EditText otherText;
    View mView;

    TextView comment_text_0_1;
    TextView comment_text_0_2;
    TextView comment_text_0_3;

    TextView comment_text_1_1;
    TextView comment_text_1_2;
    TextView comment_text_1_3;

    TextView comment_text_2_1;
    TextView comment_text_2_2;
    TextView comment_text_2_3;

    TextView comment_text_3_1;

    TextView comment_text_0_other;
    TextView comment_text_1_other;
    TextView comment_text_2_other;
    TextView comment_text_3_other;

    LinearLayout confirmBtn;
    NetService service;

    int currentStarNum;
    String currentText;
    OnItemClassListener onItemClassListener;

    String billId;
    String toUserId = User.getInstance().userId;  //DEBUG

    Dialog self = this;
    public AddCommentDialog(Context context, int theme, String _toUserId, String _billId) {
        super(context, theme);
        mConext = context;
        toUserId = _toUserId;
        billId = _billId;
        init();
    }


    public AddCommentDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }

    public AddCommentDialog(Context context, int theme, String billId) {
        super(context, theme);
        mConext = context;
        this.billId = billId;
        init();
    }

    public void setBillId(String billId){
        this.billId = billId;
    }

    public void setToUserId(String toUserId){
        this.toUserId = toUserId;
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.comment_add_popup, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);
        mView =diaView;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        lp.height = Utils.getScreenSize()[1];
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL;
        setCanceledOnTouchOutside(true);
        onItemClassListener = new OnItemClassListener();
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initData();
    }

    private void initData(){
        groupText0 = (LinearLayout)mView.findViewById(R.id.comment_group_0);
        groupText1 = (LinearLayout)mView.findViewById(R.id.comment_group_1);
        groupText2 = (LinearLayout)mView.findViewById(R.id.comment_group_2);
        groupText3 = (LinearLayout)mView.findViewById(R.id.comment_group_3);



        starBtn0 = (LinearLayout)mView.findViewById(R.id.star_btn_0);
        starBtn1 = (LinearLayout)mView.findViewById(R.id.star_btn_1);
        starBtn2 = (LinearLayout)mView.findViewById(R.id.star_btn_2);
        starBtn3 = (LinearLayout)mView.findViewById(R.id.star_btn_3);

        starBtn0.setOnClickListener(this);
        starBtn1.setOnClickListener(this);
        starBtn2.setOnClickListener(this);
        starBtn3.setOnClickListener(this);

        otherText = (EditText)mView.findViewById(R.id.other_text);
        otherText.setVisibility(View.GONE);


        comment_text_0_1 = (TextView)mView.findViewById(R.id.comment_text_0_1);
        comment_text_0_2 = (TextView)mView.findViewById(R.id.comment_text_0_2);
        comment_text_0_3 = (TextView)mView.findViewById(R.id.comment_text_0_3);

        comment_text_1_1 = (TextView)mView.findViewById(R.id.comment_text_1_1);
        comment_text_1_2 = (TextView)mView.findViewById(R.id.comment_text_1_2);
        comment_text_1_3 = (TextView)mView.findViewById(R.id.comment_text_1_3);

        comment_text_2_1 = (TextView)mView.findViewById(R.id.comment_text_2_1);
        comment_text_2_2 = (TextView)mView.findViewById(R.id.comment_text_2_2);
        comment_text_2_3 = (TextView)mView.findViewById(R.id.comment_text_2_3);

        comment_text_3_1 = (TextView)mView.findViewById(R.id.comment_text_3_1);

        comment_text_0_other =  (TextView)mView.findViewById(R.id.comment_text_0_other);
        comment_text_1_other =  (TextView)mView.findViewById(R.id.comment_text_1_other);
        comment_text_2_other =  (TextView)mView.findViewById(R.id.comment_text_2_other);
        comment_text_3_other =  (TextView)mView.findViewById(R.id.comment_text_3_other);


        comment_text_0_1.setOnClickListener(onItemClassListener);
        comment_text_0_2.setOnClickListener(onItemClassListener);
        comment_text_0_3.setOnClickListener(onItemClassListener);

        comment_text_1_1.setOnClickListener(onItemClassListener);
        comment_text_1_2.setOnClickListener(onItemClassListener);
        comment_text_1_3.setOnClickListener(onItemClassListener);

        comment_text_2_1.setOnClickListener(onItemClassListener);
        comment_text_2_2.setOnClickListener(onItemClassListener);
        comment_text_2_3.setOnClickListener(onItemClassListener);

        comment_text_3_1.setOnClickListener(onItemClassListener);

        comment_text_0_other.setOnClickListener(this);
        comment_text_1_other.setOnClickListener(this);
        comment_text_2_other.setOnClickListener(this);
        comment_text_3_other.setOnClickListener(this);

        confirmBtn = (LinearLayout)mView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentText == null || currentText.length() == 0){
                    Utils.showToast(mConext,"请选择评价");

                }else {

                    service.addComment(currentStarNum,currentText,User.getInstance().nickName,
                            User.getInstance().userId,
                            toUserId,
                            billId,
                            new NetService.NetCallBack() {
                                @Override
                                public void onCall(NetProtocol result) {
                                    if(result.code == NetProtocol.SUCCESS){
                                        Utils.showToast(mConext,"评论成功");
                                        self.dismiss();
                                    }else {
                                        DriverUtils.defaultNetProAction((Activity)mConext,result);
                                    }
                                }
                            });
                    Utils.showToast(mConext,"star is "+ currentStarNum + ";text is "+ currentText);
                }

            }
        });
        otherText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentText =s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        service = new NetService(mConext);

        //初始化
        hideAllRadioGroup();
        groupText3.setVisibility(View.VISIBLE);
        starBtn0.setBackgroundResource(R.drawable.radius_star_left);
        starBtn1.setBackgroundResource(R.drawable.star_middle);
        starBtn2.setBackgroundResource(R.drawable.star_middle);
        starBtn3.setBackgroundResource(R.drawable.radius_star_right_clicked);
        currentStarNum=3;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.star_btn_0:
                hideAllRadioGroup();
                groupText0.setVisibility(View.VISIBLE);
                starBtn0.setBackgroundResource(R.drawable.radius_star_left_clicked);
                starBtn1.setBackgroundResource(R.drawable.star_middle);
                starBtn2.setBackgroundResource(R.drawable.star_middle);
                starBtn3.setBackgroundResource(R.drawable.radius_star_right);
                currentStarNum = 0;
                currentText = null;
                break;
            case R.id.star_btn_1:
                hideAllRadioGroup();
                groupText1.setVisibility(View.VISIBLE);
                starBtn0.setBackgroundResource(R.drawable.radius_star_left);
                starBtn1.setBackgroundResource(R.drawable.star_middle_clicked);
                starBtn2.setBackgroundResource(R.drawable.star_middle);
                starBtn3.setBackgroundResource(R.drawable.radius_star_right);

                currentStarNum=1;
                currentText = null;
                break;
            case R.id.star_btn_2:
                hideAllRadioGroup();
                groupText2.setVisibility(View.VISIBLE);
                starBtn0.setBackgroundResource(R.drawable.radius_star_left);
                starBtn1.setBackgroundResource(R.drawable.star_middle);
                starBtn2.setBackgroundResource(R.drawable.star_middle_clicked);
                starBtn3.setBackgroundResource(R.drawable.radius_star_right);
                currentStarNum=2;
                currentText = null;
                break;
            case R.id.star_btn_3:
                hideAllRadioGroup();
                groupText3.setVisibility(View.VISIBLE);
                starBtn0.setBackgroundResource(R.drawable.radius_star_left);
                starBtn1.setBackgroundResource(R.drawable.star_middle);
                starBtn2.setBackgroundResource(R.drawable.star_middle);
                starBtn3.setBackgroundResource(R.drawable.radius_star_right_clicked);
                currentStarNum=3;
                currentText = null;
                break;
            case R.id.comment_text_0_other:
            case R.id.comment_text_1_other:
            case R.id.comment_text_2_other:
            case R.id.comment_text_3_other:
                resetItems();
                v.setVisibility(View.GONE);
                v.setBackgroundResource(R.color.popup_comment_clicked_item);
                currentText = null;
                otherText.setText("");
                otherText.setVisibility(View.VISIBLE);
                otherText.selectAll();
                break;
        }
    }

    private void hideAllRadioGroup(){
        groupText0.setVisibility(View.GONE);
        groupText1.setVisibility(View.GONE);
        groupText2.setVisibility(View.GONE);
        groupText3.setVisibility(View.GONE);

        resetItems();
    }



    private void resetItems(){
        comment_text_0_1.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_0_2.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_0_3.setBackgroundResource(R.drawable.clickable_popup_textview);

        comment_text_1_1.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_1_2.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_1_3.setBackgroundResource(R.drawable.clickable_popup_textview);

        comment_text_2_1.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_2_2.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_2_3.setBackgroundResource(R.drawable.clickable_popup_textview);

        comment_text_3_1.setBackgroundResource(R.drawable.clickable_popup_textview);

        comment_text_0_other.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_1_other.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_2_other.setBackgroundResource(R.drawable.clickable_popup_textview);
        comment_text_3_other.setBackgroundResource(R.drawable.clickable_popup_textview);

        comment_text_0_other.setVisibility(View.VISIBLE);
        comment_text_1_other.setVisibility(View.VISIBLE);
        comment_text_2_other.setVisibility(View.VISIBLE);
        comment_text_3_other.setVisibility(View.VISIBLE);
        otherText.setVisibility(View.GONE);

    }

    class OnItemClassListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            resetItems();
            v.setBackgroundResource(R.color.popup_comment_clicked_item);
            currentText = ((TextView)v).getText().toString().substring(2); //去掉数字1. 2.

        }
    }
}
