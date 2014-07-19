package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.dialogs.PickAddrDialog;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.models.Address;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by teddywu on 14-6-17.
 */
public class EditHomeLocationActivity extends BackBaseActivity implements EventCenter.OnEventListener {
    private static final String TAG = "EditHomeLocationActivity" ;
    ActionBar mActionBar;

    private TextView homeLocationText = null;
    private LinearLayout homeLocationBtn = null;
    final private BaseActivity self = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_home_location);
        init();
        setActionBarLayout("编辑常住地" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

    private void init(){
//        mLocationEdit = (EditText)findViewById(R.id.edit_homeLocation);

        homeLocationText = (TextView)findViewById(R.id.init_user_homelocation_text);
        homeLocationBtn = (LinearLayout)findViewById(R.id.init_user_homelocation_btn);
        homeLocationText.setText(User.getInstance().homeLocation);
        homeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickAddrDialog dialog2 = new PickAddrDialog(self,R.style.dialog);
                dialog2.show();
            }
        });
    }

    public void save(View v){
        save();
    }

    private void save(){
        final String location = homeLocationText.getText() ==null? "":  homeLocationText.getText().toString();
        if(location.length()==0){
            Utils.showToast(this,"请输入你的常住地");

            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("homeLocation", location );
        NetService service = new NetService(this);
        final Activity self = this;
        service.setUserData(map, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().homeLocation = location;
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
                    EventCenter.shared().dispatch(evt);
                    finish();
                }
                else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }


    @Override
    public void onEventCall(DataEvent e) {
        if(e.type.equals(DataEvent.ADDR_CHANGE)){
            try{
                if(e.data !=null){
                    List<String> data = (List<String>)e.data;
                    Address addr = new Address(data);
                    homeLocationText.setText(addr.toFullString());
                }
            }catch (Exception ex){
                Log.e(TAG,ex.toString());
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventCenter.shared().addEventListener(DataEvent.ADDR_CHANGE, this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        EventCenter.shared().removeEventListener(DataEvent.ADDR_CHANGE, this);
    }
}