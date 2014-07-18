package com.buerlab.returntrunk;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.buerlab.returntrunk.jpush.JPushCenter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.Set;

/**
 * Created by zhongqiling on 14-6-19.
 */
public class ReturnTrunkApp extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        JPushCenter.shared().init(getApplicationContext());

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.imgbg) // resource or drawable
                .showImageForEmptyUri(R.drawable.imgbg) // resource or drawable
                .showImageOnFail(R.drawable.imgbg) // resource or drawable
                .resetViewBeforeLoading(false)  // default
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        initUmeng();
        AssetManager.shared().init(this); //初始化资源
    }

    //初始化友盟
    private void initUmeng(){
        //http://dev.umeng.com/analytics/android/quick-start#1
        //货车段 友盟appkeky
//        AnalyticsConfig.setAppkey("53c5184156240bb4720f0f39");
        //友盟统计 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
        MobclickAgent.updateOnlineConfig(this);
        //禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        //友盟自动更新
        UmengUpdateAgent.update(this);
        MobclickAgent.updateOnlineConfig( this );
    }
}
