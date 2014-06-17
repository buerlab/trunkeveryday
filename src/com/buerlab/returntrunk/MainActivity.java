package com.buerlab.returntrunk;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.buerlab.returntrunk.fragments.FindBillFragment;
import com.buerlab.returntrunk.fragments.SendBillFragment;
import com.buerlab.returntrunk.fragments.SettingFragment;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity{

    private int currFrag = -1;
    private List<String> fragsList = Arrays.asList("sendbill", "findbill","setting");

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        NetService service = new NetService(this);
        final Activity self = this;
        service.quickLogin(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().initUser(result.data);
                    if(User.validate(self)){
                        SharedPreferences pref = self.getSharedPreferences(self.getString(R.string.app_name), 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userId", User.getInstance().userId);
                        editor.commit();

                        init();

                        FragmentManager manager = self.getFragmentManager();
                        Fragment entry = manager.findFragmentByTag("entry");
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.hide(entry);
                        transaction.commit();
                    }
                }
                else{
                    Intent intent = new Intent(self, LoginActivity.class);
                    self.startActivity(intent);
                    self.finish();
                }
            }
        });

    }

    private void init(){
        FragmentManager manager = getFragmentManager();
        ((FindBillFragment)manager.findFragmentById(R.id.find_bill_frag)).init();
        ((SendBillFragment)manager.findFragmentById(R.id.send_bill_frag)).init();
        ((SettingFragment)manager.findFragmentById(R.id.setting_frag)).init();

        Button sendbtn = (Button)findViewById(R.id.bottom_send_btn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrFrag(0);
//                showMenu();
            }
        });

        Button findBtn = (Button)findViewById(R.id.bottom_list_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrFrag(1);
            }
        });

        Button mineBtn = (Button)findViewById(R.id.bottom_mine_btn);
        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrFrag(2);
            }
        });

        setCurrFrag(0);
    }

    private void showMenu(){
        ViewGroup mainView = (ViewGroup)findViewById(R.id.main);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slider_menu);
        mainView.startAnimation(animation);
    }


    private void setCurrFrag(int index){
        if(currFrag == index)
            return;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < fragsList.size(); i++){
            Fragment fragment = manager.findFragmentByTag(fragsList.get(i));
            if(i == index){
                transaction.show(fragment);
            }else{
                transaction.hide(fragment);
            }
        }
        transaction.commit();
        currFrag = index;
    }

}
