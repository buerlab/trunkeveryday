package com.buerlab.returntrunk.driver.fragments;

//import android.app.Fragment;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.*;
import com.buerlab.returntrunk.driver.activities.MainActivity;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.driver.activities.EditDriverLicenseActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.EventLogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhongqiling on 14-6-17.
 */
public class DriverProfileFragment extends BaseFragment implements View.OnClickListener,EventCenter.OnEventListener {
    private static final String TAG = "DriverProfileFragment" ;
    LinearLayout nickNameContainer;
    LinearLayout locationContainer;
    LinearLayout IDNumContainer;
    LinearLayout driverLicenseContainer;

    TextView phoneNumTextView;
    TextView nickNameTextView;
    TextView IDNumTextView;
    TextView locationTextView;
    TextView driverLicenseTextView;

    ImageView IDNumVerifyIcon;
    ImageView driverLicenseVerifyIcon;
    View mRoot;
    User mUser;
    public final static int EDIT_NICKNAME = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot =  inflater.inflate(R.layout.profile_frag, container, false);
        intUI();
        return mRoot;
    }



    private void intUI(){

        nickNameContainer = (LinearLayout)mRoot.findViewById(R.id.container_nickName);
        nickNameContainer.setOnClickListener(this);

        locationContainer= (LinearLayout)mRoot.findViewById(R.id.container_location);
        locationContainer.setOnClickListener(this);

        IDNumContainer =(LinearLayout)mRoot.findViewById(R.id.container_IDNum);
        IDNumContainer.setOnClickListener(this);

        driverLicenseContainer =(LinearLayout)mRoot.findViewById(R.id.container_driverLicense);
        driverLicenseContainer.setOnClickListener(this);

        nickNameTextView = (TextView)mRoot.findViewById(R.id.textview_nickname);
        phoneNumTextView = (TextView)mRoot.findViewById(R.id.textview_phoneNum);
        IDNumTextView = (TextView)mRoot.findViewById(R.id.textview_IDNum);
        locationTextView = (TextView)mRoot.findViewById(R.id.textview_location);
        driverLicenseTextView = (TextView)mRoot.findViewById(R.id.textview_driverLicense);

        IDNumVerifyIcon = (ImageView)mRoot.findViewById(R.id.idnum_verify);
        driverLicenseVerifyIcon = (ImageView)mRoot.findViewById(R.id.driver_license_verify);


        EventCenter.shared().addEventListener(DataEvent.USER_UPDATE, this);

//        Button person_detail_btn = (Button)mRoot.findViewById(R.id.person_detail_btn);
//        person_detail_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.container_nickName: goToEditNickNameFragment();break;
            case R.id.container_location: goToEditLocationFragment();break;
            case R.id.container_IDNum: goToEditIDNumFragment();break;
            case R.id.container_driverLicense: goToEditDriverLisenceFragment();break;
//            case R.id.person_detail_btn:goToPersonDetail();
            default:break;
        }
    }

    @Override
    public void onShow(){
        NetService service = new NetService(this.getActivity());
        service.getUserDataWithoutLoading(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if (result.code == NetProtocol.SUCCESS && result.data != null) {
                    User.getInstance().initUser(result.data);
                    //注册用户初始化事件，用于个人资料得以初始化数据
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE, null);
                    EventCenter.shared().dispatch(evt);
                }
            }
        });
        EventLogUtils.EventLog(self.getActivity(), EventLogUtils.tthcc_driver_profile_enterFragment);
    }

    private void goToEditNickNameFragment(){
        Intent intent = new Intent(getActivity(),EditNickNameActivity.class);
        startActivity(intent);
    }

    private void goToEditLocationFragment(){
        Intent intent = new Intent(getActivity(),EditHomeLocationActivity.class);
        startActivity(intent);

    }

    private void goToEditIDNumFragment(){
        mUser = User.getInstance();
        int IDNumVerified =  Integer.parseInt(mUser.IDNumVerified);
        if(IDNumVerified==0 || IDNumVerified==3){
            Intent intent = new Intent(getActivity(),EditIDNumActivity.class);
            intent.putExtra("isEdited",false);
            startActivity(intent);
         }
    }

    private void goToEditDriverLisenceFragment(){
        mUser = User.getInstance();
        int driverLisenceVerified =  Integer.parseInt(mUser.driverLicenseVerified);
        if(driverLisenceVerified==0 || driverLisenceVerified==3){
            Intent intent = new Intent(getActivity(),EditDriverLicenseActivity.class);
            startActivity(intent);
        }
    }

    private void goToPersonDetail(){
        Intent intent = new Intent(getActivity(),UserCompleteDataActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEventCall(DataEvent e) {
        mUser = User.getInstance();
        nickNameTextView.setText(mUser.nickName);
        phoneNumTextView.setText(mUser.phoneNum);

        locationTextView.setText(mUser.homeLocation);
        int IDNumVerified =  Integer.parseInt(mUser.IDNumVerified);
        switch (IDNumVerified){
            case 0: IDNumTextView.setText("未审核");
                IDNumVerifyIcon.setImageResource(R.drawable.qt2_wsh);
                break;
            case 1:IDNumTextView.setText("审核中");
                IDNumVerifyIcon.setImageResource(R.drawable.qt_dd);
                break;
            case 2:IDNumTextView.setText("通过审核");
                IDNumVerifyIcon.setImageResource(R.drawable.verified);
                break;
            case 3:IDNumTextView.setText("审核失败");
                IDNumVerifyIcon.setImageResource(R.drawable.qt2_wtg);
                break;
            default:break;
        }

        int driverLisenceVerified =  Integer.parseInt(mUser.driverLicenseVerified);
        switch (driverLisenceVerified){
            case 0: driverLicenseTextView.setText("未审核");
                driverLicenseVerifyIcon.setImageResource(R.drawable.qt2_wsh);
                break;
            case 1:driverLicenseTextView.setText("审核中");
                driverLicenseVerifyIcon.setImageResource(R.drawable.qt_dd);
                break;
            case 2:driverLicenseTextView.setText("通过审核");
                driverLicenseVerifyIcon.setImageResource(R.drawable.verified);
                break;
            case 3:driverLicenseTextView.setText("审核失败");
                driverLicenseVerifyIcon.setImageResource(R.drawable.qt2_wsh);
                break;
            default:break;
        }
    }
}