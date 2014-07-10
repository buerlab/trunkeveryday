package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.models.UserCompleteData;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.views.StarsViewWithText;

/**
 * Created by teddywu on 14-6-17.
 */
public class UserCompleteDataActivity extends BackBaseActivity {
    private static final String TAG = "UserCompleteDataActivity" ;

    StarsViewWithText starsViewWithText;
    StarsViewWithText starsViewWithText2;
    String userId;
    String getType;
    NetService netService;

    UserCompleteData data;

    TextView textview_nickname;
    StarsViewWithText stars_view;
    TextView total_comment_count;
    TextView average_star_num;
    TextView textview_phoneNum;
    TextView home_location;

    ImageView IDNumVerifyIcon;
    TextView IDNumTextView;

    ImageView driverLicenseVerifyIcon;
    TextView driverLicenseTextView;

    ImageView trunk_license_verify;
    TextView textview_trunk_license;

    ImageView support_location_verify;
    TextView textview_support_location;

    FrameLayout tab_trunk_wrapper;

    LinearLayout container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail);
        setActionBarLayout("个人资料" );
        init();
        initData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        Utils.init(this);
        starsViewWithText = (StarsViewWithText)findViewById(R.id.stars_view);
        starsViewWithText.setStar(2.8f);
        starsViewWithText.setSize(30);

        container = (LinearLayout)findViewById(R.id.container);
        container.setVisibility(View.GONE);
        textview_nickname = (TextView)findViewById(R.id.textview_nickname);
        stars_view = (StarsViewWithText)findViewById(R.id.stars_view);
        total_comment_count = (TextView)findViewById(R.id.total_comment_count);
        average_star_num = (TextView)findViewById(R.id.average_star_num);
        textview_phoneNum = (TextView)findViewById(R.id.textview_phoneNum);
        home_location = (TextView)findViewById(R.id.home_location);

        IDNumVerifyIcon = (ImageView)findViewById(R.id.idnum_verify);
        IDNumTextView = (TextView)findViewById(R.id.textview_IDNum);

        driverLicenseVerifyIcon = (ImageView)findViewById(R.id.driver_license_verify);
        driverLicenseTextView = (TextView)findViewById(R.id.textview_driverLicense);

        trunk_license_verify = (ImageView)findViewById(R.id.trunk_license_verify);
        textview_trunk_license = (TextView)findViewById(R.id.textview_trunk_license);

        support_location_verify = (ImageView)findViewById(R.id.support_location_verify);
        textview_support_location = (TextView)findViewById(R.id.textview_support_location);

        tab_trunk_wrapper = (FrameLayout)findViewById(R.id.tab_trunk_wrapper);

    }

    private void initData(){
        userId = getIntent().getStringExtra("userId");
        getType = getIntent().getStringExtra("getType");
        if(userId.isEmpty() || getType.isEmpty()){
            Utils.showToast(this,"无法获取个人资料");
            return;
        }
        netService = new NetService(this);
        netService.getUserCompleteData(userId,getType,new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS && result.data!=null){
                    data = new UserCompleteData(result.data);
                    render();
                }else {
                    Utils.defaultNetProAction(self,result);
                }
            }
        });
    }
    private  void render(){
        if(data ==null){
            return;
        }

        container.setVisibility(View.VISIBLE);
        textview_nickname.setText(data.nickName);
        stars_view.setStar(data.stars);
        average_star_num.setText(String.valueOf(data.stars));

        if(data.comments !=null){
            total_comment_count.setText(data.comments.size());
        }

        textview_phoneNum.setText(getFormatPhoneNum(data.phoneNum));
        home_location.setText(data.homeLocation);

        int IDNumVerified =  Integer.parseInt(data.IDNumVerified);
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

        int driverLisenceVerified =  Integer.parseInt(data.driverLicenseVerified);
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

        int trunkLisenceVerified =  Integer.parseInt(data.trunkLicenseVerified);
        switch (trunkLisenceVerified){
            case 0: textview_trunk_license.setText("未审核");
                trunk_license_verify.setImageResource(R.drawable.qt2_wsh);
                break;
            case 1:textview_trunk_license.setText("审核中");
                trunk_license_verify.setImageResource(R.drawable.qt_dd);
                break;
            case 2:textview_trunk_license.setText("通过审核");
                trunk_license_verify.setImageResource(R.drawable.verified);
                break;
            case 3:textview_trunk_license.setText("审核失败");
                trunk_license_verify.setImageResource(R.drawable.qt2_wsh);
                break;
            default:break;
        }
        
         View v = LayoutInflater.from(this).inflate(R.layout.trunk_item, null);
        ImageView set_current_trunk_btn = (ImageView)v.findViewById(R.id.set_current_trunk_btn);
        set_current_trunk_btn.setVisibility(View.GONE);
        tab_trunk_wrapper.addView(v);
    }

    private String getFormatPhoneNum(String phonenum){
        if (phonenum.isEmpty())
            return phonenum;

        String str1 = phonenum.substring(0,3);
        String str2 = phonenum.substring(3,7);
        return str1 + " " + str2 + " ****";
    }

}