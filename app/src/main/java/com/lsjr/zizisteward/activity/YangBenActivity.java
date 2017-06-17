package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;

@SuppressLint("SetJavaScriptEnabled")
public class YangBenActivity extends Activity {
    private WebView yangben_webview;
    private ProgressBar mProgressBar;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yangben);
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        final TextView tv_title = (TextView) findViewById(R.id.tv_title);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        String url = getIntent().getStringExtra("url");
        mode = getIntent().getStringExtra("mode");
        yangben_webview = (WebView) findViewById(R.id.yangben_webview);
        WebSettings settings = yangben_webview.getSettings();
        yangben_webview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        yangben_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }

            }
        });

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);

            }

        };
        yangben_webview.setWebChromeClient(wvcc);
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                yangben_webview.loadUrl(url);
                // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
                return true;
            }
        };
        yangben_webview.setWebViewClient(wvc);
        yangben_webview.loadUrl(HttpConfig.IMAGEHOST + url);
        System.out.println("地址" + HttpConfig.IMAGEHOST + url);
        yangben_webview.addJavascriptInterface(new DemoJavaInterface(), "control");
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (yangben_webview.canGoBack()) {
                    yangben_webview.goBack();
                } else {
                    finish();
                }
            }
        });

    }

    public class DemoJavaInterface {

        @JavascriptInterface
        public void Goodsdetails(String shopid) {
            Intent intent = new Intent(getApplicationContext(), HomeBrandDetail.class);
            intent.putExtra("sid", shopid);
            if (mode.equals("jiangpin")) {
                intent.putExtra("mode", "jiangpin");
            } else {
                intent.putExtra("mode", "shepin");
            }
            intent.putExtra("hava_dianpu", "no");
            startActivity(intent);
        }
    }

}
