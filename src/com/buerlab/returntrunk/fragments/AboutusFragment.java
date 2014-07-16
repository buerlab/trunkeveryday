package com.buerlab.returntrunk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.buerlab.returntrunk.BillInvitationListAdapter;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.activities.FeedbackActivity;
import com.buerlab.returntrunk.models.Bill;
import com.buerlab.returntrunk.models.User;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;

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

        // 首先在您的Activity中添加如下成员变量
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
                RequestType.SOCIAL);
        // 设置分享内容
        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(getActivity(),
                "http://www.umeng.com/images/pic/banner_module_social.png"));
        mController.setAppWebSite(SHARE_MEDIA.RENREN, "http://www.umeng.com/social");

//        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

        mRoot.findViewById(R.id.social_share_wrapper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.openShare(self.getActivity(), false);
            }
        });
    }
}