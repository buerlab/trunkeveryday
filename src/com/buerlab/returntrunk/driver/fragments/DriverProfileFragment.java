package com.buerlab.returntrunk.driver.fragments;

//import android.app.Fragment;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.driver.activities.EditDriverLicenseActivity;
import com.buerlab.returntrunk.activities.EditIDNumActivity;
import com.buerlab.returntrunk.activities.EditNickNameActivity;
import com.buerlab.returntrunk.activities.EditHomeLocationActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.BaseFragment;

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


        EventCenter.shared().addEventListener(DataEvent.USER_UPDATE, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.container_nickName: goToEditNickNameFragment();break;
            case R.id.container_location: goToEditLocationFragment();break;
            case R.id.container_IDNum: goToEditIDNumFragment();break;
            case R.id.container_driverLicense: goToEditDriverLisenceFragment();break;
            default:break;
        }
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

    @Override
    public void onEventCall(DataEvent e) {
        mUser = User.getInstance();
        nickNameTextView.setText(mUser.nickName);
        phoneNumTextView.setText(mUser.phoneNum);

        locationTextView.setText(mUser.homeLocation);
        int IDNumVerified =  Integer.parseInt(mUser.IDNumVerified);
        switch (IDNumVerified){
            case 0: IDNumTextView.setText("未审核");break;
            case 1:IDNumTextView.setText("审核中");break;
            case 2:IDNumTextView.setText("通过审核");break;
            case 3:IDNumTextView.setText("审核失败");break;
            default:break;
        }

        int driverLisenceVerified =  Integer.parseInt(mUser.driverLicenseVerified);
        switch (driverLisenceVerified){
            case 0: driverLicenseTextView.setText("未审核");break;
            case 1:driverLicenseTextView.setText("审核中");break;
            case 2:driverLicenseTextView.setText("通过审核");break;
            case 3:driverLicenseTextView.setText("审核失败");break;
            default:break;
        }
    }
}