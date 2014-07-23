package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.webkit.WebView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by teddywu on 14-7-22.
 */
public class UserAgreementActivity extends BackBaseActivity {

    private static final String TAG = "UserAgreementActivity" ;

    WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_agreement_layout);

        setActionBarLayout("用户协议" );

        mWebView = (WebView)findViewById(R.id.webView);
        String url = Utils.getConfigString(this, "userAgreement", "http://115.29.8.74:9288/m_user_agreement.html");
        mWebView.loadUrl(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
