package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.SendCircleFriendsActivity;

/**
 * Created by Administrator on 2017/5/22.
 */

public class ZeroToVipActivity extends Activity {
    private WebView webview;
    String url;
    private ProgressBar progressBar;
    RelativeLayout re_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zero_to_vip);
        url = getIntent().getStringExtra("url");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webview = (WebView) findViewById(R.id.webview);
        re_back = (RelativeLayout) findViewById(R.id.re_back);
        WebSettings settings = webview.getSettings();
        webview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }

            }
        });
        re_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
        webview.loadUrl(HttpConfig.IMAGEHOST + url);
        webview.addJavascriptInterface(new DemoJavaInterface(), "control");
    }

    private class DemoJavaInterface {

        @JavascriptInterface
        public void beansTaskOne(String id) {
            Intent intent;
            if (id.equals("1")) {
                 /*实名认证*/
                intent = new Intent(getApplicationContext(), RealNameConfirmActivity.class);
                startActivity(intent);
            }
            if (id.equals("2")) {
                /*通讯录---加好友*/
                App.requestAddressBook(ZeroToVipActivity.this, 3);
            }
            if (id.equals("3")) {
                /*发一次朋友圈*/
                intent = new Intent(getApplicationContext(), SendCircleFriendsActivity.class);
                startActivity(intent);
            }
            if (id.equals("4")) {
              /*分享时视*/
                intent = new Intent(getApplicationContext(), ZiXunActivity.class);
                intent.putExtra("type", "vip_shishi");
                startActivity(intent);
            }
            if (id.equals("5")) {
                /*分享美食*/
                intent = new Intent(getApplicationContext(), SixthNewActivity.class);
                startActivity(intent);
            }
            if (id.equals("6")) {
                /*与管家互动*/
                if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(ZeroToVipActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                } else {
                    App.CallSteward(ZeroToVipActivity.this);
                }
            }
            if (id.equals("7")) {
                  /*到通讯录---好友互动*/
                App.requestAddressBook(ZeroToVipActivity.this, 3);
            }
            if (id.equals("8")) {
               /*新建话题*/
                App.requestAddressBook(ZeroToVipActivity.this, 1);
            }
            if (id.equals("9")) {
                /*到话题——加入朋友的群*/
                App.requestAddressBook(ZeroToVipActivity.this, 1);
            }
            if (id.equals("10")) {
                /*反馈孜赏*/
                intent = new Intent(getApplicationContext(), ZiXunActivity.class);
                intent.putExtra("type", "vip_zishang");
                startActivity(intent);
            }

        }

    }

    // android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
