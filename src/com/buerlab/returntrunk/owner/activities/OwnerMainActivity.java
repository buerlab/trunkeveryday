package com.buerlab.returntrunk.owner.activities;

import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import com.baidu.mapapi.SDKInitializer;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.LoginActivity;
import com.buerlab.returntrunk.controls.BillOverDueCheckJob;
import com.buerlab.returntrunk.controls.MainController;
import com.buerlab.returntrunk.controls.Scheduler;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.dialogs.PhoneCallNotifyDialog;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.fragments.SettingFragment;
import com.buerlab.returntrunk.jpush.JPushCenter;
import com.buerlab.returntrunk.jpush.JPushProtocal;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.service.BaiduMapService;
import com.coboltforge.slidemenu.SlideMenu;
import com.coboltforge.slidemenu.SlideMenuInterface;

import com.testin.agent.TestinAgent;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.update.UmengUpdateAgent;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.widget.DrawerLayout;

public class OwnerMainActivity extends BaseActivity implements JPushCenter.OnJpushListener {

    private static final String TAG = "MainActivity";
    private int currFrag = -1;
    private int currHomeFrag = -1;
    private List<String> fragsList = Arrays.asList("首页","基本资料","历史货单","我的评价","设置","关于我们");
    private List<String> homeFragsList = Arrays.asList("sendbill", "findbill");

    private String[] mPlanetTitles;
//    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList;
//    private ActionBarDrawerToggle mDrawerToggle = null;
    final BaseActivity self = this;

    private SlideMenu slideMenu = null;

    boolean withoutSplash;

    private final static String WITHOUT_SPLASH = "splash_shown";
    NetService service;
    BroadcastReceiver connectionReceiver;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            performRestoreInstanceState(savedInstanceState);
        }

        getSupportActionBar().hide();
        setContentView(R.layout.main_goods);
        service = new NetService(this);
        Utils.setOwnerVersion(this);
        Utils.init(this);
        initTestin();//初始化Testin质量分析
//        JPushCenter.shared().register(JPushProtocal.JPUSH_PHONE_CALL, this);
        AssetManager.shared().init(this);
        MainController.shared().init(getApplicationContext());
        initBaiduService();

        //是否展示闪屏页
        withoutSplash = getIntent().getBooleanExtra("without_splash",false);
        if(withoutSplash){
            hideEntryFragment();
        }

        //无网络下
        if(!Utils.isNetworkConnected(this)){
            hideEntryFragment();
            getSupportActionBar().show();
            init();
            withoutSplash = true;
            Utils.setGlobalData(this,"hasLogined", "false");
        }else {
            fastLogin();
        }

        registerConnectionReceiver();
    }

    private void registerConnectionReceiver(){
        connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Utils.isNetworkConnected(self)){

                    if(Utils.getGlobalData(self,"hasLogined").equals("false")){
                        if(self.hasStop){
                            Utils.setGlobalData(self,"needToQuickLogin","true");
                        }else {
                            fastLogin();
                        }
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }
    private void fastLogin(){
        service.quickLogin(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    JSONObject data = result.data;
                    try{
                        User.getInstance().initUser(data.getJSONObject("user"));
                        MainController.shared().sync(data.getJSONObject("control"));
                    }catch (JSONException e){
                        Toast toast = Toast.makeText(self, "userdata init fail!!", 2);
                        toast.show();
                    }

                    User.getInstance().setUserType(User.USERTYPE_OWNER);

                    Map<String, String> jpushmap = new HashMap<String, String>();
                    jpushmap.put("ownerJPushId", JPushInterface.getRegistrationID(self.getApplicationContext()));
                    NetService netservice = new NetService(self.getApplicationContext());
                    netservice.setUserData(jpushmap, null);

                    //注册用户初始化事件，用于个人资料得以初始化数据
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
                    EventCenter.shared().dispatch(evt);

                    if(User.validate(self)){
                        Utils.setGlobalData(self,"userId",User.getInstance().userId);
//                        JPushUtils.registerAlias(self, User.getInstance().userId+User.USERTYPE_OWNER);
//                        JPushUtils.registerAlias(self, "zql");
//                        JPushUtils.registerAlias();
                        Utils.setGlobalData(self,"hasLogined", "true");
                        init();

                        hideEntryFragment();
                        getSupportActionBar().show();
                        setActionBarLayout("天天回程车",WITH_MENU);
                    }
                }
                else{
                    toLoginUI();
                }
            }
        });

        service.uploadLocation(100.2,33333,"hubeu", "guangdjong","jfkdlasdf", null);
    }

    private void performRestoreInstanceState(Bundle savedInstanceState) {
        withoutSplash = savedInstanceState.getBoolean(WITHOUT_SPLASH, withoutSplash);
    }

    private void toLoginUI() {
        Intent intent = new Intent(self, LoginActivity.class);
        self.startActivity(intent);
        self.finish();
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(Utils.getGlobalData(self,"needToQuickLogin").equals("true")){
            fastLogin();
            Utils.setGlobalData(self,"needToQuickLogin","false");
        }
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);       //统计时长
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
    }

    private void initTestin(){
        TestinAgent.init(this, "61c544bb7fefa0392340c2976e89e373");//此行必须放在super.onCreate后
    }

    private void init(){
        if(getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);
        slideMenu = (SlideMenu)findViewById(R.id.main_slideMenu);
        slideMenu.init(this, R.menu.slide_menu_goods, new SlideMenuInterface.OnSlideMenuItemClickListener() {
            @Override
            public void onSlideMenuItemClick(int itemId) {

                switch (itemId){
                    case R.id.slide_menu_home:setFrag(0);break;
                    case R.id.slide_menu_profile:setFrag(1);break;
                    case R.id.slide_menu_bill_history:setFrag(2);break;
                    case R.id.slide_menu_comment:setFrag(3);break;
                    case R.id.slide_menu_setting:setFrag(4);break;
                    case R.id.slide_menu_aboutus:setFrag(5);break;
                    default:break;
                }
            }
        }, 300);

        // set optional header image
        slideMenu.setHeaderImage(getResources().getDrawable(R.drawable.logo1));

        setFrag(0);
    }

    //初始化百度地图
    private void initBaiduService(){

        if (Utils.getSDKVersionNumber()>7){
            //百度sdk inital
            SDKInitializer.initialize(getApplicationContext());
            //启动位置上报服务
            startService(new Intent(this, BaiduMapService.class));
        }

    }

    //隐藏闪屏页
    private void hideEntryFragment(){
        FragmentManager manager = getSupportFragmentManager();
        Fragment entry = manager.findFragmentByTag("entry");
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(entry);
        transaction.commitAllowingStateLoss();
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
        dialog.show(getSupportFragmentManager(), "phonecall");
    }

    private void switchToFrag(String tag){
        int index = fragsList.indexOf(tag);
        if(index >= 0){
            setFrag(index);
        }
    }

    private void setFrag(int index){
        if(currFrag == index)
            return;
        execSetFrag(index, fragsList);
//        getActionBar().setTitle(fragsList.get(index));
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
