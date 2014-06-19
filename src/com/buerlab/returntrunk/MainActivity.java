package com.buerlab.returntrunk;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.baidu.mapapi.SDKInitializer;
import com.buerlab.returntrunk.fragments.FindBillFragment;
import com.buerlab.returntrunk.fragments.SendBillFragment;
import com.buerlab.returntrunk.fragments.SettingFragment;
import com.buerlab.returntrunk.fragments.SlideMenuFragment;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.coboltforge.slidemenu.SlideMenu;
import com.coboltforge.slidemenu.SlideMenuInterface;

import com.buerlab.returntrunk.service.BaiduMapService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private int currFrag = -1;
    private int currHomeFrag = -1;
    private List<String> fragsList = Arrays.asList("首页","基本资料","历史货单","车辆管理","我的评价","设置","关于我们");
    private List<String> homeFragsList = Arrays.asList("sendbill", "findbill");

    private String[] mPlanetTitles;
//    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList;
//    private ActionBarDrawerToggle mDrawerToggle = null;

    private SlideMenu slideMenu = null;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SDKInitializer.initialize(getApplicationContext());

        NetService service = new NetService(this);
        final FragmentActivity self = this;
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
                        FragmentManager manager = self.getSupportFragmentManager();
//                        FragmentManager manager = self.getFragmentManager();
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
        getActionBar().setHomeButtonEnabled(true);

        slideMenu = (SlideMenu)findViewById(R.id.main_slideMenu);
        slideMenu.init(this, R.menu.slide_menu, new SlideMenuInterface.OnSlideMenuItemClickListener() {
            @Override
            public void onSlideMenuItemClick(int itemId) {

                switch (itemId){
                    case R.id.slide_menu_home:setFrag(0);break;
                    case R.id.slide_menu_profile:setFrag(1);break;
                    case R.id.slide_menu_bill_history:setFrag(2);break;
                    case R.id.slide_menu_car:setFrag(3);break;
                    case R.id.slide_menu_comment:setFrag(4);break;
                    case R.id.slide_menu_setting:setFrag(5);break;
                    case R.id.slide_menu_aboutus:setFrag(6);break;
                    default:break;
                }
//                if(itemId == R.id.slide_menu_home){
//                    setFrag(0);
//                }else if(itemId == R.id.slide_menu_setting){
//                    setFrag(1);
//                }
            }
        }, 300);

        // set optional header image
        slideMenu.setHeaderImage(getResources().getDrawable(R.drawable.ic_launcher));

        FragmentManager manager = getSupportFragmentManager();
        ((FindBillFragment)manager.findFragmentById(R.id.find_bill_frag)).init();
        ((SendBillFragment)manager.findFragmentById(R.id.send_bill_frag)).init();


        Button sendbtn = (Button)findViewById(R.id.bottom_send_btn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeFrag(0);
            }
        });

        Button findBtn = (Button)findViewById(R.id.bottom_list_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeFrag(1);
            }
        });
        setFrag(0);
        setHomeFrag(0);

        startLocationService();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                slideMenu.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private  void startLocationService(){

//        if(User.getInstance().getUserType() == "driver"){
            //启动位置上报服务
            startService(new Intent(this, BaiduMapService.class));
//        }

    }

    private void setHomeFrag(int index){
        if(currHomeFrag == index)
            return;
        execSetFrag(index, homeFragsList);

        currHomeFrag = index;
    }


    private void setFrag(int index){
        if(currFrag == index)
            return;
        execSetFrag(index, fragsList);
        getActionBar().setTitle(fragsList.get(index));
        currFrag = index;
    }

    private void execSetFrag(int index, List<String> tags){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < tags.size(); i++){
            Fragment fragment = manager.findFragmentByTag(tags.get(i));
            if(fragment !=null){
                if(i == index){
                    transaction.show(fragment);
                }else{
                    transaction.hide(fragment);
                }
            }

        }
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
    }
}
