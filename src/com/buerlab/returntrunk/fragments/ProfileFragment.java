package com.buerlab.returntrunk.fragments;

//import android.app.Fragment;
import android.app.FragmentTransaction;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.User;
import com.buerlab.returntrunk.fragments.EditProfileFragments.EditNickNameFragmentActivity;

/**
 * Created by zhongqiling on 14-6-17.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment" ;
    LinearLayout nickNameEdit ;
    View mRoot;

    public final static int EDIT_NICKNAME = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot =  inflater.inflate(R.layout.profile_frag, container, false);
        intUI();
        return mRoot;
    }

    private void intUI(){

        nickNameEdit = (LinearLayout)mRoot.findViewById(R.id.edit_nickname);
        nickNameEdit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_nickname: goToEditNickNameFragment();break;
            default:break;
        }
    }

    private void goToEditNickNameFragment(){
        Intent intent = new Intent(getActivity(),EditNickNameFragmentActivity.class);
        String nickname = User.getInstance().nickName;
        getActivity().startActivityForResult(intent, EDIT_NICKNAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EDIT_NICKNAME:
                if (resultCode == 20){
                    Log.i(TAG, data.getStringExtra("nickname"));
                }
                break;
            default:break;
        }

    }
}