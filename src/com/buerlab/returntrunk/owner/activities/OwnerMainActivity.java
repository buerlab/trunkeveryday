package com.buerlab.returntrunk.owner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.R;
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
    private List<String> fragsList = Arrays.asList("天天回程车","基本资料","历史货单","我的评价","设置","关于我们");
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

        getActionBar().hide();
        setContentView(R.layout.main_goods);

        //启动位置上报服务
//        startService(new Intent(this, BaiduMapService.class));
//        JPushCenter.shared().register(JPushProtocal.JPUSH_PHONE_CALL, this);
        AssetManager.shared().init(this);
//        SDKInitializer.initialize(getApplicationContext());

        NetService service = new NetService(this);
        final FragmentActivity self = this;
        Utils.init(this);
        service.quickLogin(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().initUser(result.data);
                    User.getInstance().setUserType(User.USERTYPE_OWNER);

                    Map<String, String> jpushmap = new HashMap<String, String>();
                    jpushmap.put("ownerJPushId", JPushInterface.getRegistrationID(self.getApplicationContext()));
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
//                        JPushUtils.registerAlias(self, User.getInstance().userId+User.USERTYPE_OWNER);
//                        JPushUtils.registerAlias(self, "zql");
//                        JPushUtils.registerAlias();

                        init();
                        FragmentManager manager = self.getSupportFragmentManager();
//                        FragmentManager manager = self.getFragmentManager();
                        Fragment entry = manager.findFragmentByTag("entry");
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.hide(entry);
                        transaction.commit();
                        getActionBar().show();
                        setActionBarLayout("天天回程车",WITH_MENU);
                    }
                }
                else{
                    Intent intent = new Intent(self, OwnerLoginActivity.class);
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
        slideMenu.setHeaderImage(getResources().getDrawable(R.drawable.ic_launcher));

        FragmentManager manager = getSupportFragmentManager();
        ((SettingFragment)manager.findFragmentById(R.id.main_setting_frag)).init();

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
