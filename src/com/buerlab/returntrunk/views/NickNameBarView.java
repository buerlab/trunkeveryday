package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.models.NickBarData;
import com.buerlab.returntrunk.models.User;

/**
 * Created by teddywu on 14-7-3.
 */
public class NickNameBarView extends LinearLayout {

    ImageView mIDNumIcon;
    ImageView mDriverLicenseIcon;
    ImageView mTrunkLicenseIcon;
    ImageView mLocationIcon;
    TextView mNickName;
    StarsViewWithText mStar;
    public NickNameBarView(Context context) {
        super(context);
    }

    public NickNameBarView(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.nickname_bar_view, this);
        init();
    }

    private void init(){
        mNickName = (TextView)findViewById(R.id.nickname);
        mIDNumIcon = (ImageView)findViewById(R.id.idnum_icon);
        mDriverLicenseIcon = (ImageView)findViewById(R.id.driver_license_icon);
        mTrunkLicenseIcon = (ImageView)findViewById(R.id.trunk_license_icon);
        mLocationIcon = (ImageView)findViewById(R.id.location_icon);
        mStar = (StarsViewWithText)findViewById(R.id.stars_view);
        mStar.setSize(16);
    }

    public void  setUser(NickBarData user,String myUserType){
        mNickName.setText(user.nickName);

        //如果我是司机，那么我要看的是货主信息
        if(myUserType != "driver"){

            mStar.setStar(user.driverStars);
            if(!user.driverLicenseVerified.equals("2")){
                mDriverLicenseIcon.setVisibility(GONE);
            }
            if(!user.trunkLicenseVerified.equals("2")){
                mTrunkLicenseIcon.setVisibility(GONE);
            }
            if(false){
                mLocationIcon.setVisibility(GONE);
            }
        }else {
            mStar.setStar(user.ownerStars);
            mDriverLicenseIcon.setVisibility(GONE);
            mTrunkLicenseIcon.setVisibility(GONE);
            mLocationIcon.setVisibility(GONE);
        }

        if(!user.IDNumVerified.equals("2")){
            mIDNumIcon.setVisibility(GONE);
        }





    }

}
