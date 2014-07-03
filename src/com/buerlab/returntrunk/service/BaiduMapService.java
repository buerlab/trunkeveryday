package com.buerlab.returntrunk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.buerlab.returntrunk.net.NetService;

import  java.util.Date;

/**
 * Android Service 示例
 *
 * @author dev
 *
 */
public class BaiduMapService extends Service implements
        OnGetGeoCoderResultListener {
    private static final String TAG = "BaiduMapService" ;
    public static final String ACTION = "com.buerlab.returntrunk.service.BaiduMapService";
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    NetService service;
    Date date = null;
    Date currentDate = null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    MyLocationData locData;
    final  static int PERIOD = 1000 * 60 * 15;  //15分钟报一次
//    final  static int PERIOD = 1000 * 5;  //test
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

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.v(TAG, "抱歉，未能找到结果");
            service.uploadLocation(result.getLocation().latitude,
                    result.getLocation().longitude,
                    null,
                    null,
                    null,null);

        }else{
            service.uploadLocation(result.getLocation().latitude,
                    result.getLocation().longitude,
                    result.getAddressDetail().province,
                    result.getAddressDetail().city,
                    result.getAddressDetail().district,
                    null);
            Log.v(TAG, result.getAddress()+";"+result.getAddressDetail().province+result.getAddressDetail().city+result.getAddressDetail().district);
        }
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
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();

                LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());

                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
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
