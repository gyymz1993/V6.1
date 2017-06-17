package com.lsjr.zizisteward.common.activtiy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

@SuppressLint("SetJavaScriptEnabled")
public class ServiceProjectWebViewActivity extends BaseActivity {
    private WebView yangben_webview;
    private ProgressBar mProgressBar;

    @Override
    public int getContainerView() {
//		return R.layout.activity_yangben;
        return R.layout.activity_project_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("方案详情");
        String id = getIntent().getStringExtra("id");
        String tag = getIntent().getStringExtra("tag");
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
        yangben_webview.loadUrl(HttpConfig.IMAGEHOST + "/bookingdetails/Reservationdetails?id=" + id + "&tag=" + tag);
        System.out.println("地址" + HttpConfig.IMAGEHOST + "/bookingdetails/Reservationdetails?id=" + id + "&tag=" + tag);
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
    }
}
