package com.buerlab.returntrunk;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.buerlab.returntrunk.jpush.JPushCenter;

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
    }
}
