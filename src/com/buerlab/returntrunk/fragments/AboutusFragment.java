package com.buerlab.returntrunk.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.FeedbackActivity;
import com.buerlab.returntrunk.activities.UserAgreementActivity;
import com.buerlab.returntrunk.utils.EventLogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengUpdateAgent;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhongqiling on 14-5-31.
 */
public class AboutusFragment extends BaseFragment {

    private static final String TAG = "AboutusFragment";
    View mRoot;

    TextView version;
    UMSocialService mController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.about_us, container, false);
        mRoot = fragView;
        init();
        return  fragView;
    }

    private void init(){
        mRoot.findViewById(R.id.user_feedback_wrapper).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), FeedbackActivity.class);
               startActivity(intent);
           }
       });

        mRoot.findViewById(R.id.user_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserAgreementActivity.class);
                startActivity(intent);
            }
        });

        version = (TextView)mRoot.findViewById(R.id.version);
        version.setText(Utils.getVersion(getActivity()));

        mRoot.findViewById(R.id.update_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.forceUpdate(getActivity());
            }
        });

        initUmengSocialShared();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onShow(){
        if(Utils.getVersionType(self.getActivity()).equals("driver")){
            EventLogUtils.EventLog(self.getActivity(), EventLogUtils.tthcc_driver_aboutus_enterFragment);
        }else {
            //TODO 货主版
        }
    }

    private void initUmengSocialShared(){
        // 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share",
                RequestType.SOCIAL);


        // 设置分享内容
        mController.setShareContent(getShareText()+getShareUrl());
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(getActivity(),
                getShareImage()));
        mController.setAppWebSite(SHARE_MEDIA.RENREN, getShareUrl());

//        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

        UMImage mUMImgBitmap = new UMImage(getActivity(),getShareImage());

        //设置微信分享的内容
        WeiXinShareContent weixinContent = new WeiXinShareContent(mUMImgBitmap);
        weixinContent.setShareContent(getShareText());
        weixinContent.setTitle(getShareTitle());
        weixinContent.setTargetUrl(getShareUrl());
        weixinContent.setAppWebSite(getShareUrl());
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent(mUMImgBitmap);
        circleMedia.setShareContent(getShareText());
        circleMedia.setTargetUrl(getShareUrl());
        circleMedia.setTitle(getShareText());
        circleMedia.setAppWebSite(getShareUrl());
        mController.setShareMedia(circleMedia);

        //设置QQ分享的内容
        QQShareContent qqShareContent = new QQShareContent(mUMImgBitmap);
        qqShareContent.setTargetUrl(getShareUrl());
        qqShareContent.setTitle(getShareTitle());
        qqShareContent.setShareContent(getShareText());
        qqShareContent.setAppWebSite(getShareUrl());
        mController.setShareMedia(qqShareContent);

        //设置QQ空间分享的内容
        QZoneShareContent qZoneShareContent = new QZoneShareContent((mUMImgBitmap));
        qZoneShareContent.setTargetUrl(getShareUrl());
        qZoneShareContent.setTitle(getShareTitle());
        qZoneShareContent.setShareContent(getShareText());
        qZoneShareContent.setAppWebSite(getShareUrl());
        mController.setShareMedia(qqShareContent);

        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appID = "wx6b255dd85f63eb05";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();


        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，
        //参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，
        //参数3为开发者在QQ互联申请的APP kEY.
        //TODO 换成自己的
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
        mRoot.findViewById(R.id.social_share_wrapper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.openShare(self.getActivity(), false);
            }
        });
    }

    private String getShareUrl(){
        String value = MobclickAgent.getConfigParams(getActivity(), "share_url");
        if (value == null || value.length()==0){
            value = "http://115.29.8.74:9288";
        }
        return  value;
    }

    private String getShareText(){
        String versionType = Utils.getVersionType(this.getActivity());
        String value = MobclickAgent.getConfigParams(getActivity(), versionType + "_share_content");
        if (value == null || value.length()==0){
            value = "天天回程车，便捷、安全、高效的货车预定平台！";
        }
        return  value;
    }

    private String getShareImage(){
        String versionType = Utils.getVersionType(this.getActivity());
        String value = MobclickAgent.getConfigParams(getActivity(), versionType + "_share_image");
        if (value == null || value.length()==0){
            value = MobclickAgent.getConfigParams(getActivity(), "share_image");
        }
        if (value == null || value.length()==0){
            value = "http://115.29.8.74:9288/images/icon144.png";
        }
        return  value;
    }

    private String getShareTitle(){
        String versionType = Utils.getVersionType(this.getActivity());
        String value = MobclickAgent.getConfigParams(getActivity(), "share_title");
        if (value == null || value.length()==0){
            value = "天天回程车";
        }
        return  value;
    }
}