package com.buerlab.returntrunk.driver.activities;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import cn.jpush.android.api.JPushInterface;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.fragments.HistoryBillsFragment;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.PhoneCallNotifyDialog;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.driver.fragments.DriverHomeFragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements JPushCenter.OnJpushListener {

    private static final String TAG = "MainActivity";
    private int currFrag = -1;
    private int currHomeFrag = -1;
    private List<String> fragsList = Arrays.asList("首页","基本资料","历史货单","车辆管理","我的评价","设置","关于我们");
    private List<String> homeFragsList = Arrays.asList("sendbill", "findbill");

    private String[] mPlanetTitles;
//    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList;
//    private ActionBarDrawerToggle mDrawerToggle = null;

    private SlideMenu slideMenu = null;
    final FragmentActivity self = this;
    boolean withoutSplash;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getActionBar().hide();
        setContentView(R.layout.main);
        Utils.setDriverVersion(this);
        //启动位置上报服务
//        startService(new Intent(this, BaiduMapService.class));
//        JPushCenter.shared().register(JPushProtocal.JPUSH_PHONE_CALL, this);
        AssetManager.shared().init(this);
        Utils.init(this);
//        SDKInitializer.initialize(getApplicationContext());

        NetService service = new NetService(this);

        withoutSplash = getIntent().getBooleanExtra("without_splash",false);

        if(withoutSplash){
            FragmentManager manager = getSupportFragmentManager();
            Fragment entry = manager.findFragmentByTag("entry");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(entry);
            transaction.commit();
        }


        service.quickLogin(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().initUser(result.data);
                    User.getInstance().setUserType(User.USERTYPE_TRUNK);

                    Map<String, String> jpushmap = new HashMap<String, String>();
                    jpushmap.put("driverJPushId", JPushInterface.getRegistrationID(self.getApplicationContext()));
                    NetService netservice = new NetService(self.getApplicationContext());
                    netservice.setUserData(jpushmap, null);

                    //注册用户初始化事件，用于个人资料得以初始化数据
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
                    EventCenter.shared().dispatch(evt);

                    if(User.validate(self)){
                        SharedPreferences pref = self.getSharedPreferences(self.getString(R.string.app_name), 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("userId", User.getInstance().userId);
                        editor.commit();
//                        String alias = User.getInstance().userId+User.USERTYPE_TRUNK;
//                        JPushUtils.registerAlias(self, alias);
                        JPushUtils.registerAlias(self, "zql");
//                        JPushUtils.registerAlias();

                        init();
                        FragmentManager manager = self.getSupportFragmentManager();
//                        FragmentManager manager = self.getFragmentManager();
                        Fragment entry = manager.findFragmentByTag("entry");
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.hide(entry);
                        transaction.commit();
                        setActionBarLayout("天天回程车",WITH_MENU);
                        getActionBar().show();

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
        super.onDestroy();
    }


    private void init(){
        if(getActionBar() != null)
            getActionBar().setHomeButtonEnabled(true);

        slideMenu = (SlideMenu)findViewById(R.id.main_slideMenu);
        slideMenu.init(this, R.menu.slide_menu, new SlideMenuInterface.OnSlideMenuItemClickListener() {
            @Override
            public void onSlideMenuItemClick(int itemId) {
                switch (itemId){
                    case R.id.slide_menu_home:setFrag(0);break;
                    case R.id.slide_menu_profile:setFrag(1);break;
                    case R.id.slide_menu_bill_history:setFrag(2);break;
                    case R.id.slide_menu_trunk:setFrag(3);break;
                    case R.id.slide_menu_comment:setFrag(4);break;
                    case R.id.slide_menu_setting:setFrag(5);break;
                    case R.id.slide_menu_aboutus:setFrag(6);break;
                    default:break;
                }
            }
        }, 300);

        // set optional header image
        slideMenu.setHeaderImage(getResources().getDrawable(R.drawable.logo1));
//        FragmentManager manager = getSupportFragmentManager();
//        ((DriverHomeFragment)manager.findFragmentById(R.id.send_bill_frag)).init();
//        ((HistoryBillsFragment)manager.findFragmentById(R.id.main_history_frag)).init();
        setFrag(0);

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

    public void onJPushCall(JPushProtocal protocal) {
        PhoneCallNotifyDialog dialog = new PhoneCallNotifyDialog(protocal.msg);
        dialog.show(getFragmentManager(), "phonecall");
    }


    private  void startLocationService(){

//        if(User.getInstance().getUserType() == "driver"){
            //启动位置上报服务
            startService(new Intent(this, BaiduMapService.class));
//        }
    }

    private void setFrag(int index){
        if(currFrag == index)
            return;
        execSetFrag(index, fragsList);
        setActionBarLayout(fragsList.get(index),WITH_MENU);
        currFrag = index;
    }

    private void execSetFrag(int index, List<String> tags){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < tags.size(); i++){
            BaseFragment fragment = (BaseFragment)manager.findFragmentByTag(tags.get(i));
            if(fragment !=null){
                if(i == index){
                    transaction.show(fragment);
                    fragment.onShow();
                }else{
                    transaction.hide(fragment);
                }
            }
        }
        transaction.commit();
    }

    //按back键不退出，保留在后台
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // menuUtils.createTwoDispatcher(event);
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
        }
        return false;
    }
}
