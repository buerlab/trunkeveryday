package com.buerlab.returntrunk.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.buerlab.returntrunk.User;
import com.buerlab.returntrunk.net.NetService;

import  java.util.Date;

/**
 * Android Service 示例
 *
 * @author dev
 *
 */
public class BaiduMapService extends Service {
    private static final String TAG = "BaiduMapService" ;
    public static final String ACTION = "com.buerlab.returntrunk.service.BaiduMapService";
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    NetService service;
    Date date = null;
    Date currentDate = null;
    final  static int PERIOD = 1000 * 60 * 15;  //15分钟报一次
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "ServiceDemo onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "ServiceDemo onCreate");
        initBaiduMapLocation();
        date = new Date();
        service= new NetService(getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "ServiceDemo onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        Log.v(TAG, "ServiceDemo onDestory");
        initBaiduMapLocation();
        super.onDestroy();
    }

    private void initBaiduMapLocation(){
        // 定位初始化
//        mLocClient = new LocationClient(this);
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            currentDate = new Date();
            if(currentDate.getTime() - date.getTime() > PERIOD){
                date = currentDate;
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();

                Log.v(TAG, "("+location.getLatitude()+","+ location.getLongitude()+")");

//                service.uploadLocation(locData,null);
            }

//            String s=HttpRequest.sendGet("http://115.29.8.74:9288/api/location", "latitude="+location.getLatitude()+"&longitude="+ location.getLatitude());
//            System.out.println(s);

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
