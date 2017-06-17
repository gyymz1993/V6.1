package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class ServiceProjectDetailActivity extends Activity {
    private WebView mWebview;
    private String mTspid;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_project_detail);
        mTspid = getIntent().getStringExtra("tspid");
        String type = getIntent().getStringExtra("type");
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mWebview = (WebView) findViewById(R.id.webview);
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
        });

        if (type.equals("url")) {
            mWebview.loadUrl(HttpConfig.IMAGEHOST + mTspid);
        } else {
            getData();
        }

        mWebview.setWebChromeClient(new WebChromeClient() {
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

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "284");
        map.put("tspid", mTspid);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("服务详情" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String servicePlanUrl = jsonObject.getString("servicePlanUrl");
                    mWebview.loadUrl(HttpConfig.IMAGEHOST + servicePlanUrl);
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

}
