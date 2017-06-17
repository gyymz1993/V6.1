package com.lsjr.zizisteward.common.activtiy;

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
import com.lsjr.zizisteward.activity.BasicWebViewActivity;
import com.lsjr.zizisteward.activity.HotelOrderActivity;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.ToastUtils;

@SuppressLint("SetJavaScriptEnabled")
public class YuDingInfoActivity extends BaseActivity {
    private WebView yangben_webview;
    private ProgressBar mProgressBar;

    @Override
    public int getContainerView() {
        return R.layout.activity_yuding_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        String type = getIntent().getStringExtra("type");
        if ("1".equals(type)) {
            setmTitle("酒店预定");
        } else {
            setmTitle("预定信息");
        }
        RelativeLayout iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        yangben_webview = (WebView) findViewById(R.id.yangben_webview);
        WebSettings settings = yangben_webview.getSettings();
        yangben_webview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        yangben_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        yangben_webview.loadUrl(HttpConfig.IMAGEHOST + url);
        yangben_webview.addJavascriptInterface(new DemoJavaInterface(), "control");
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
        iv_back.setOnClickListener(new OnClickListener() {

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
        public void Thequery(String destination, String Checktime, String Yourtime, String keyword, String Starprice) {// 查询
            System.out.println("目的地" + destination + "时间" + Checktime + "时间" + Yourtime);
            Intent intent = new Intent(getApplicationContext(), HotelOrderActivity.class);
            intent.putExtra("destination", destination);
            intent.putExtra("Checktime", Checktime);
            intent.putExtra("Yourtime", Yourtime);
            intent.putExtra("keyword", keyword);
            intent.putExtra("Starprice", Starprice);
            startActivity(intent);
        }

        @JavascriptInterface
        public void addroomreservation(int code, String msg) {// 提交
            if (code >= 0) {
                Intent intent = new Intent(YuDingInfoActivity.this, BasicWebViewActivity.class);
                startActivity(intent);
                finish();
            } else {
                ToastUtils.show(getApplicationContext(), msg);
            }
        }
    }

    // android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && yangben_webview.canGoBack()) {
            yangben_webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
