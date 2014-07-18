package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BackBaseActivity;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.models.Location;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.MultiPicSelector.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by teddywu on 14-6-17.
 */

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 *
 */
public class BaiduMapActivity extends BackBaseActivity {

    private static final String TAG = "BaiduMapActivity";

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfigeration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;

    boolean isFirstLoc = true;// 是否首次定位
    NetService service;

    private Timer timer = new Timer();
    private TimerTask task;
    private final  static int MAX_TIME = 15 * 60 * 1000;
//    private final  static int MAX_TIME = 5* 1000;
//    private int time = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //要做的事情
            getData();
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        setActionBarLayout("查看司机位置");

        if (Utils.getSDKVersionNumber()<=7){
            Utils.showToast(this,"抱歉，系统版本过低，不支持地图功能。");
            finish();
        }
        service = new NetService(this);


        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();

        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, MAX_TIME);
    }


    private void getData(){

        String lily = "53c4b2407938ee1089520738";
        String a15507507418 = "53c38b347938ee65f08ef387";
        service.getUserLocation(lily,new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS && result.data!=null){
                    Location location = new Location(result.data);
                    MyLocationData locData = new MyLocationData.Builder()
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(Double.valueOf(location.latitude))
                            .longitude(Double.valueOf(location.longitude)).build();
                    mBaiduMap.setMyLocationData(locData);
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        LatLng ll = new LatLng(Double.valueOf(location.latitude),
                                Double.valueOf(location.longitude));
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                        mBaiduMap.animateMapStatus(u);
                    }
                }else {
                    Utils.defaultNetProAction(self,result);
                }
            }
        });
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
//        mLocClient.stop();
        // 关闭定位图层
        timer.cancel();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
