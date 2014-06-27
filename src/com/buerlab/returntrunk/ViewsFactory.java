package com.buerlab.returntrunk;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.BillViewContxtMenu;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.List;


/**
 * Created by zhongqiling on 14-6-8.
 */
public class ViewsFactory {

    static public View createInviteBill(LayoutInflater inflater, Bill bill){
        View inviteBill = inflater.inflate(R.layout.new_bill_invitation, null, false);
        int bViewId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = inflater.inflate(bViewId, null, false);
        ViewGroup container = (ViewGroup)inviteBill.findViewById(R.id.new_bill_invitation_bill);
        container.addView(bView);

        ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);

        return inviteBill;
    }

    static public View createFindBill(LayoutInflater inflater, final Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.find_bill_goods : R.layout.find_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);

        ((TextView)bView.findViewById(R.id.find_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.find_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.find_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.find_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.find_bill_mat)).setText(bill.material);

        Button phoneBtn = (Button)bView.findViewById(R.id.find_bill_phone);
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bill.phoneNum.isEmpty()){
                    EventCenter.shared().dispatch(new DataEvent(DataEvent.PHONE_CALL, bill.phoneNum));
                }
            }
        });

        return bView;
    }

    static public View createSendBill(LayoutInflater inflater, final Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.new_bill_goods : R.layout.new_bill_trunk;
        View bView = inflater.inflate(layoutId, null, false);

        ((TextView)bView.findViewById(R.id.new_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.new_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.new_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.new_bill_mat)).setText(bill.material);

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
                menu.show(BaseActivity.currActivity.getFragmentManager(), "menu");

                return true;
            }
        });


        return bView;
    }

    static public View createBill(LayoutInflater inflater, Bill bill){
        int layoutId = bill.billType.equals(Bill.BILLTYPE_GOODS) ? R.layout.simple_bill_goods : R.layout.simple_bill_trunk;
        View bView = (View)inflater.inflate(layoutId, null, false);

        ((TextView)bView.findViewById(R.id.simple_bill_name)).setText(bill.senderName);
        ((TextView)bView.findViewById(R.id.simple_bill_from)).setText(bill.from);
        ((TextView)bView.findViewById(R.id.simple_bill_to)).setText(bill.to);
        ((TextView)bView.findViewById(R.id.simple_bill_time)).setText(Utils.tsToTimeString(bill.time));
        if(bill.billType.equals(Bill.BILLTYPE_GOODS))
            ((TextView)bView.findViewById(R.id.simple_bill_mat)).setText(bill.material);

        return bView;
    }

    static public View createPickAddrView(final Context context, LayoutInflater inflater){
        View view = (View)inflater.inflate(R.layout.pick_addr_view, null, false);
        final String[] provs = AssetManager.shared().getProvinces();

        ArrayWheelAdapter provAdapter = new ArrayWheelAdapter(context, provs);
        final WheelView provWheel = (WheelView)view.findViewById(R.id.pick_addr_prov);
        provWheel.setVisibleItems(6);
        provWheel.setViewAdapter(provAdapter);

        final WheelView cityWheel = (WheelView)view.findViewById(R.id.pick_addr_city);
        cityWheel.setVisibleItems(6);
        cityWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, AssetManager.shared().getCities(provs[0])));

        String[] cities = AssetManager.shared().getCities(provs[0]);
        String[] regions = AssetManager.shared().getRegions(provs[0], cities[0]);
        final LinearLayout regWheelContainer = (LinearLayout)view.findViewById(R.id.pick_addr_reg);
        if(regions != null && regions.length > 0)
        {
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            regWheelContainer.addView(createWheelView(context, new ArrayWheelAdapter(context, regions)), parms);
        }


        provWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currProv = provs[newValue];
                ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(context, AssetManager.shared().getCities(currProv));
                cityWheel.setViewAdapter(adapter);
//                EventCenter.shared().dispatch(new DataEvent(DataEvent.ADDR_CHANGE, cityWheel.getCurrentItem()));
            }
        });

        cityWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currProv = (String)((ArrayWheelAdapter)provWheel.getViewAdapter()).getItemText(provWheel.getCurrentItem());
                String currCity = (String)((ArrayWheelAdapter)cityWheel.getViewAdapter()).getItemText(newValue);
                String[] regions = AssetManager.shared().getRegions(currProv, currCity);
                if(regions != null && regions.length > 0){
                    ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(context, regions);
                    if(regWheelContainer.getChildCount() > 0)
                        ((WheelView)regWheelContainer.getChildAt(0)).setViewAdapter(adapter);
                    else{
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        regWheelContainer.addView(createWheelView(context, adapter), parms);
                    }

                }
                else if(regions != null && regions.length == 0){
                    regWheelContainer.removeAllViews();
                }
            }
        });


        return view;
    }

    static private WheelView createWheelView(Context context, ArrayWheelAdapter adapter){
        WheelView wheelView = new WheelView(context);
        wheelView.setViewAdapter(adapter);
        wheelView.setVisibleItems(6);
        return wheelView;
    }

}
