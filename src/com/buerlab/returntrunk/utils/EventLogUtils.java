package com.buerlab.returntrunk.utils;

import android.content.Context;
import com.buerlab.returntrunk.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by teddywu on 14-7-16.
 */
public class EventLogUtils {

    //发布回程车
    public final static String tthcc_driver_NewTrunkBill_enterActivity = "tthcc_driver_NewTrunkBill_enterActivity"; //货车版_发布回程车_进入界面
    public final static String tthcc_driver_NewTrunkBill_btn = "tthcc_driver_NewTrunkBill_btn"; //货车版_发布回程车_点击按钮
    public final static String tthcc_driver_NewTrunkBill_btn_success = "tthcc_driver_NewTrunkBill_btn_success"; //货车版_发布回程车_点击按钮_成功

    //推荐货源
    public final static String tthcc_driver_findBill_enterActivity = "tthcc_driver_findBill_enterActivity"; //货车版_推荐

    //各种fragment
    public final static String tthcc_driver_historyBill_enterFragment = "tthcc_driver_historyBill_enterFragment"; //货车版_历史货单
    public final static String tthcc_driver_aboutus_enterFragment = "tthcc_driver_aboutus_enterFragment"; //货车版_关于我们
    public final static String tthcc_driver_setting_enterFragment = "tthcc_driver_setting_enterFragment"; //货车版_设置
    public final static String tthcc_driver_trunkList_enterFragment = "tthcc_driver_trunkList_enterFragment"; //货车版_车辆管理
    public final static String tthcc_driver_commentList_enterFragment = "tthcc_driver_commentList_enterFragment"; //货车版_评论列表
    public final static String tthcc_driver_home_enterFragment = "tthcc_driver_home_enterFragment"; //货车版_首页
    public final static String tthcc_driver_profile_enterFragment = "tthcc_driver_profile_enterFragment"; //货车版_基本资料

    //添加货车
    public final static String tthcc_driver_addTrunk_btn = "tthcc_driver_addTrunk_btn";  //货车版_车辆管理页面的添加货车按钮
    public final static String tthcc_driver_addTrunk_btn_success = "tthcc_driver_addTrunk_btn_success";  //货车版_车辆管理页面的添加货车按钮_成功

    //登录流程相关
    public final static String tthcc_driver_login_btn = "tthcc_driver_login_btn";  //货车版_在登录页点击登录按钮
    public final static String tthcc_driver_login_btn_success = "tthcc_driver_login_btn_success";  //货车版_在登录页登录_成功

    //注册流程相关
    public final static String tthcc_driver_register_btn = "tthcc_driver_register_btn"; //货车版_注册页点击注册按钮
    public final static String tthcc_driver_getRegCode = "tthcc_driver_getRegCode";  //货车版_获取注册验证码按钮
    public final static String tthcc_driver_register_btn_success = "tthcc_driver_register_btn_success"; //货车版_注册页点击注册按钮_成功

    public final static String tthcc_driver_InitDriver_btn = "tthcc_driver_InitDriver_btn"; //货车版_初始化信息_点击按钮（密码称呼常住地）
    public final static String tthcc_driver_InitDriver_btn_success = "tthcc_driver_InitDriver_btn_success"; //货车版_初始化信息_点击按钮_成功（密码称呼常住地）

    public final static String tthcc_driver_InitDriver_addTrunk_btn = "tthcc_driver_InitDriver_addTrunk_btn";  //货车版_初始化第一辆车_点击按钮
    public final static String tthcc_driver_InitDriver_addTrunk_btn_success = "tthcc_driver_InitDriver_addTrunk_btn_success";  //货车版_初始化第一辆车_点击按钮_成功

    //各种dialog
    public final static String tthcc_driver_billConfirmDialog = "tthcc_driver_billConfirmDialog";  //货车版_确认交易消息框
    public final static String tthcc_driver_phoneConfirmDialog = "tthcc_driver_phoneConfirmDialog";  //货车版_打电话给司机
    public final static String tthcc_driver_PublishConfirmDialog = "tthcc_driver_PublishConfirmDialog"; //货车版_离开常住地弹窗确认是否发布回程车
    public final static String tthcc_driver_requestBillDialog = "tthcc_driver_requestBillDialog"; //货车版_交易确认_你是否和胡先生达成交易

    //1.继续完善 2.货主版的




    //用法参考
    //http://dev.umeng.com/analytics/android/advanced-integration-guide#1

    public static void EventLog(Context c,String eventId, HashMap<String,String> map){
        MobclickAgent.onEvent(c, eventId, map);
    }

    public static void EventLog(Context c,String eventId){
        MobclickAgent.onEvent(c, eventId);
    }

}
