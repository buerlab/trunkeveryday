package com.buerlab.returntrunk.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.support.v7.widget.*;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.buerlab.returntrunk.PhoneCallListener;
import com.buerlab.returntrunk.PickBillCallListener;
import com.buerlab.returntrunk.activities.OnPhotoClick;
import com.buerlab.returntrunk.dialogs.AddCommentDialog;
import com.buerlab.returntrunk.dialogs.PhoneConfirmDialog2;
import com.buerlab.returntrunk.dialogs.RequestBillDialog;
import com.buerlab.returntrunk.models.*;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.BillViewContxtMenu;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.owner.fragments.FindDriverFragment;
import com.buerlab.returntrunk.utils.MultiPicSelector.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zhongqiling on 14-6-8.
 */
public class ViewsFactory {

    static public View createInviteBill(LayoutInflater inflater, Bill bill){
        View inviteBill = inflater.inflate(R.layout.new_bill_invitation, null, false);
        int bViewId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = inflater.inflate(bViewId, null, false);
        if(inviteBill != null && bView != null){
            ViewGroup container = (ViewGroup)inviteBill.findViewById(R.id.new_bill_invitation_bill);
            container.addView(bView);

            ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
            ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
            ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
            ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.timestampToDisplay(bill.time));
            if(bill.billType.equals(Bill.BILLTYPE_GOODS))
                ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);
        }

        return inviteBill;
    }

    static public View createFindBill(final LayoutInflater inflater, final RecommendBill recommendBill){
        Bill bill = recommendBill.bill;
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.find_bill_goods : R.layout.find_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);
        if(bView != null) {
            fillFindBill(bView, recommendBill);
        }
        return bView;
    }

    static public View createFindGoodBill(final LayoutInflater inflater, final RecommendBill recommendBill){
        int layoutId = R.layout.find_bill_goods;
        View bView = inflater.inflate(layoutId, null, false);
        if(bView != null){
            fillFindBill(bView, recommendBill);

        }

        return bView;
    }

    static public void fillFindBill(final View bView, final RecommendBill recommendBill){
        final Bill bill = recommendBill.bill;
        ((NickNameBarView)bView.findViewById(R.id.find_bill_user_bar)).setUser(recommendBill.userData,User.getInstance().getUserType());
        ((TextView)bView.findViewById(R.id.find_bill_from)).setText(new Address(bill.from).toShortString());
        ((TextView)bView.findViewById(R.id.find_bill_to)).setText(new Address(bill.to).toShortString());
        ((TextView)bView.findViewById(R.id.find_bill_time)).setText(Utils.timestampToDisplay(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS)){
            ((TextView)bView.findViewById(R.id.find_bill_mat)).setText(bill.material);
            ((TextView)bView.findViewById(R.id.find_bill_weight)).setText(String.valueOf(bill.weight));
            ((TextView)bView.findViewById(R.id.find_bill_price)).setText(String.valueOf(bill.price));
        }
        else if(bill.billType.equals(Bill.BILLTYPE_TRUNK)){
            ImageView typeIcon = ((ImageView)bView.findViewById(R.id.find_bill_trunk_logo));
            ((TextView)bView.findViewById(R.id.find_bill_trunk)).setText(bill.trunkType);
            ((TextView)bView.findViewById(R.id.find_bill_load)).setText(String.valueOf(bill.trunkLoad));
            ((TextView)bView.findViewById(R.id.find_bill_length)).setText(String.valueOf(bill.trunkLength));
            android.support.v7.widget.GridLayout gridLayout = (android.support.v7.widget.GridLayout)bView.findViewById(R.id.trunk_gridlayout);
            gridLayout.removeAllViews();
            ImageLoader imageLoader = ImageLoader.getInstance();
            int width = (Utils.getScreenSize()[0] - 100)/8;
                if(recommendBill.userData.trunkPicFilePaths!=null){
                    for(int i =0;i<recommendBill.userData.trunkPicFilePaths.size();i++){
                        ImageView iv = new ImageView(bView.getContext());
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.setMargins(0,0,5,0);
                        params.width = width;
                        params.height = width;
                        gridLayout.addView(iv,params);

                        imageLoader.displayImage(bView.getContext().getString(R.string.server_addr2)+recommendBill.userData.trunkPicFilePaths.get(i),iv);
                        iv.setOnClickListener(new OnPhotoClick(bView.getContext(), i,recommendBill.userData.trunkPicFilePaths));
                    }
                    gridLayout.setVisibility(View.VISIBLE);
                }else {
                    gridLayout.setVisibility(View.GONE);
                }

            if(bill.trunkType.equals("厢车") ){
                typeIcon.setImageResource(R.drawable.che_xc);
            }else if(bill.trunkType.equals("低栏车") ){
                typeIcon.setImageResource(R.drawable.che_dlc);
            }else if(bill.trunkType.equals("高栏车") ){
                typeIcon.setImageResource(R.drawable.che_glc);
            }else if(bill.trunkType.equals("集装车") ){
                typeIcon.setImageResource(R.drawable.che_jzxc);
            }else if(bill.trunkType.equals("面包车") ){
                typeIcon.setImageResource(R.drawable.che_mbc);
            }else if(bill.trunkType.equals("平板车") ){
                typeIcon.setImageResource(R.drawable.che_cbc);
            }
        }

        ImageView typeLogo = (ImageView)bView.findViewById(R.id.find_bill_type_logo);
        if(typeLogo != null && recommendBill.recommendBillType.equals(Bill.BILLTYPE_TRUNK)){
            typeLogo.setVisibility(View.VISIBLE);
        }else if(typeLogo != null){
            typeLogo.setVisibility(View.INVISIBLE);
        }

        ImageView phoneBtn = (ImageView)bView.findViewById(R.id.find_bill_phone);
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneConfirmDialog2 phoneConfirmDialog2 = new PhoneConfirmDialog2(bView.getContext(),bill,R.style.dialog);
                phoneConfirmDialog2.show();
            }
        });


        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }


    static public View createSendBill(LayoutInflater inflater, final Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.new_bill_goods : R.layout.new_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);
        fillSendBill(bView, bill);

        return bView;
    }

    static public void fillSendBill(final View bView,final Bill bill){
        ((TextView)bView.findViewById(R.id.new_bill_from)).setText(new Address(bill.from).toShortString());
        ((TextView)bView.findViewById(R.id.new_bill_to)).setText(new Address(bill.to).toShortString());
        ((TextView)bView.findViewById(R.id.new_bill_time)).setText(Utils.timestampToDisplay(bill.time));
        ((TextView)bView.findViewById(R.id.new_bill_visitedtimes)).setText(String.valueOf(bill.visitedTimes));
        ((TextView)bView.findViewById(R.id.new_bill_comment)).setText(String.valueOf(bill.comment));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS)){
            ((TextView)bView.findViewById(R.id.new_bill_goods)).setText(bill.material);
            ((TextView)bView.findViewById(R.id.new_bill_weight)).setText(String.valueOf(bill.weight));
            ((TextView)bView.findViewById(R.id.new_bill_price)).setText(String.valueOf(bill.price));
        }

        bView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BillViewContxtMenu menu = new BillViewContxtMenu();
                menu.setListener(new BillViewContxtMenu.OnBillContextListener() {
                    @Override
                    public void onDelete() {
                        NetService service = new NetService(BaseActivity.currActivity);
                        service.deleteBill(bill, new NetService.NetCallBack() {
                            @Override
                            public void onCall(NetProtocol result) {
                                if(result.code == NetProtocol.SUCCESS){
                                    EventCenter.shared().dispatch(new DataEvent(DataEvent.DELETE_BILL, bill));
                                }else{
                                    Utils.defaultNetProAction(BaseActivity.currActivity, result);
                                }

                            }
                        });

                    }
                });
                menu.show(BaseActivity.currActivity.getSupportFragmentManager(), "menu");

                return true;
            }
        });
    }

    static public View createHisotryBill(LayoutInflater inflater, final HistoryBill bill){

        int layoutId = getHistoryBillLayout(bill.billType);
        View bView = inflater.inflate(layoutId, null, false);
        if(bView != null){
            fillHistoryBill(bView, bill);
        }
        return bView;
    }

    static public void fillHistoryBill(final View bView,final HistoryBill bill){

        ((NickNameBarView)bView.findViewById(R.id.history_bill_nickName_bar)).setUser(bill.senderData, bill.senderType);
        Utils.safeSetText((TextView) bView.findViewById(R.id.history_bill_from), new Address(bill.fromAddr).toShortString());
        Utils.safeSetText((TextView) bView.findViewById(R.id.history_bill_to), new Address(bill.toAddr).toShortString());
        Utils.safeSetText((TextView) bView.findViewById(R.id.history_bill_mat), bill.material);
        Utils.safeSetText((TextView) bView.findViewById(R.id.history_bill_price), String.valueOf(bill.price));
        Utils.safeSetText((TextView) bView.findViewById(R.id.history_bill_weight), String.valueOf(bill.weight));
        Utils.safeSetText((TextView) bView.findViewById(R.id.history_bill_deal_time), Utils.timestampToDisplay(bill.sendTime));
        Button callBtn = (Button)bView.findViewById(R.id.history_bill_call);
        Button commentBtn = (Button)bView.findViewById(R.id.history_bill_comment);
        if(callBtn != null)
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bill.senderPhoneNum.length() > 0){
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bill.senderPhoneNum));
                        BaseActivity.currActivity.startActivity(intent);
                    }else{
                        Toast toast = Toast.makeText(BaseActivity.currActivity, "该用户没有留下手机号", 2);
                        toast.show();
                    }
                }
            });
        if(commentBtn != null)
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCommentDialog dialog = new AddCommentDialog(bView.getContext(), R.style.dialog, bill.sender, bill.id);
                    dialog.show();
                }
            });
    }


    static public View createBill(LayoutInflater inflater, Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = (View)inflater.inflate(layoutId, null, false);
        if(bView != null)
        {
            ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
            ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
            ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
            ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.timestampToDisplay(bill.time));
            if(bill.billType.equals(Bill.BILLTYPE_GOODS))
                ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);
        }
        return bView;
    }

    static private int getHistoryBillLayout(String billType){
        if(billType.equals(HistoryBill.TYPE_GOODS))
            return R.layout.history_bill_goods;
        else if(billType.equals(HistoryBill.TYPE_TRUNK))
                return R.layout.history_bill_trunk;
        else
            return R.layout.history_bill_user;
    }

}
