package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

@SuppressLint("SetJavaScriptEnabled")
public class LevelActivity extends BaseActivity {
    private WebView mWebview;
    private RelativeLayout mBack;
    private ProgressBar mProgressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        mWebview = (WebView) findViewById(R.id.webview_level);
        mBack = (RelativeLayout) findViewById(R.id.iv_back);
        WebSettings settings = mWebview.getSettings();
        mWebview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("里面得链家" + url);
                if (url.contains("viplevel")) {
                    setmTitle("等级介绍");
                }
                if (url.contains("VIPgradeintro")) {
                    setmTitle("规则介绍");
                }
                if (url.contains("jfrule")) {
                    setmTitle("积分介绍");
                }
                if (url.contains("VIPruleZ")) {
                    setmTitle("会费介绍");
                }

                super.onPageFinished(view, url);
            }
        });
        mWebview.loadUrl(HttpConfig.IMAGEHOST + url);
        mWebview.addJavascriptInterface(new DemoJavaInterface(), "control");
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebview.canGoBack()) {
                    mWebview.goBack();
                } else {
                    finish();
                }
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar1.setVisibility(View.GONE);
                } else {
                    mProgressBar1.setVisibility(View.VISIBLE);
                    mProgressBar1.setProgress(newProgress);
                }
            }
        });
    }

    public class DemoJavaInterface {

        public DemoJavaInterface() {
            super();
        }

        // 点击升级
        @JavascriptInterface
        public void Membershipupgrade(String url) {
            System.out.println("交互地址" + url);
			Intent intent = new Intent(getApplicationContext(), VipUpgradeActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

    }

    // android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getContainerView() {
        return R.layout.activity_level;
    }

}
