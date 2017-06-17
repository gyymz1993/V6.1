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
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class CarServiceActivity extends BaseActivity {
    private WebView webview;
    private ProgressBar mProgressBar;
    private RelativeLayout mIv_back;

    @Override
    public int getContainerView() {
        return R.layout.activity_order_ticket_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("专车服务");
        mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        webview = (WebView) findViewById(R.id.webview);
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
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }

            }
        });
        mIv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
        getData();
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "309");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String url = jsonObject.getString("TaxiServiceUrl");
                    webview.loadUrl(HttpConfig.IMAGEHOST + url);// 联网操作
                    webview.addJavascriptInterface(new DemoJavaInterface(), "control");
                    System.out.println("汽车服务" + HttpConfig.IMAGEHOST + url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    public class DemoJavaInterface {

        @JavascriptInterface
        public void addcarshade(int code, String msg) {
            if (code >= 0) {
                Intent intent = new Intent(CarServiceActivity.this, BasicWebViewActivity.class);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
