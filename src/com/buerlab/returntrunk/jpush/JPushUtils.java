package com.buerlab.returntrunk.jpush;

import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import java.util.Set;

/**
 * Created by zhongqiling on 14-6-19.
 */
public class JPushUtils {

    static public void registerAlias(Context context, String alias){
        JPushInterface.setAlias(context, alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == 0) {
                    Log.i("-----------JPUSHINTERFACE:", "REGISTER AILIAS SUCCESS!!");
                } else {
                    Log.i("-------------JPUSHINTERFACE:", "REGISTER AILIAS WRONG!!");
                }
            }
        });
    }

}
