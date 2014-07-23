package com.buerlab.returntrunk.activities;

import android.os.Bundle;
import android.webkit.WebView;
import com.buerlab.returntrunk.R;

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

        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.loadUrl("http://www.baidu.com");
    }
}
