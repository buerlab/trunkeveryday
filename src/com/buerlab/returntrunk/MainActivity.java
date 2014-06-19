package com.buerlab.returntrunk;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cn.jpush.android.api.JPushInterface;
import com.baidu.mapapi.SDKInitializer;
import com.buerlab.returntrunk.fragments.FindBillFragment;
import com.buerlab.returntrunk.fragments.SendBillFragment;
import com.buerlab.returntrunk.fragments.SettingFragment;
import com.buerlab.returntrunk.jpush.JPushCenter;
import com.buerlab.returntrunk.jpush.JPushProtocal;
import com.buerlab.returntrunk.jpush.JPushUtils;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.coboltforge.slidemenu.SlideMenu;
import com.coboltforge.slidemenu.SlideMenuInterface;

import com.buerlab.returntrunk.service.BaiduMapService;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements JPushCenter.OnJpushListener {

    private int currFrag = -1;
    private int currHomeFrag = -1;
    private List<String> fragsList = Arrays.asList("首页", "设置");
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
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.main);

        //启动位置上报服务
        startService(new Intent(this, BaiduMapService.class));
        JPushCenter.shared().register(JPushProtocal.JPUSH_PHONE_CALL, this);

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
                        JPushUtils.registerAlias(self, User.getInstance().userId);

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

    @Override
    protected void onResume(){
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy(){
        JPushCenter.shared().unregister(JPushProtocal.JPUSH_PHONE_CALL, this);
    }


    private void init(){
        getActionBar().setHomeButtonEnabled(true);

        slideMenu = (SlideMenu)findViewById(R.id.main_slideMenu);
        slideMenu.init(this, R.menu.slide_menu, new SlideMenuInterface.OnSlideMenuItemClickListener() {
            @Override
            public void onSlideMenuItemClick(int itemId) {
                if(itemId == R.id.slide_menu_home){
                    setFrag(0);
                }else if(itemId == R.id.slide_menu_setting){
                    setFrag(1);
                }
            }
        }, 300);

        // set optional header image
        slideMenu.setHeaderImage(getResources().getDrawable(R.drawable.ic_launcher));

        FragmentManager manager = getFragmentManager();
        ((FindBillFragment)manager.findFragmentById(R.id.find_bill_frag)).init();
        ((SendBillFragment)manager.findFragmentById(R.id.send_bill_frag)).init();
        ((SettingFragment)manager.findFragmentById(R.id.main_setting_frag)).init();

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

    public void onJPushCall(JPushProtocal protocal){
        Toast toast = Toast.makeText(this, "server push:"+protocal.msg, 3);
        toast.show();
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
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < tags.size(); i++){
            Fragment fragment = manager.findFragmentByTag(tags.get(i));
            if(i == index){
                transaction.show(fragment);
            }else{
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

}
