package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class NewsDetailActivity extends BaseActivity {

    private WebView mWebview;

    @Override
    public int getContainerView() {
        return R.layout.activity_zixun_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("资讯详情");
        mWebview = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWebview.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);

        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "50");
        map.put("newsid", getIntent().getStringExtra("newsid"));
        new HttpClientGet(NewsDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                NewsBean bean = GsonUtil.getInstance().fromJson(result, NewsBean.class);
                mWebview.loadUrl(HttpConfig.IMAGEHOST + bean.getHotdetailsUrl());
            }
        });

    }

    public class NewsBean {
        private String error;
        private String msg;
        private String hotdetailsUrl;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getHotdetailsUrl() {
            return hotdetailsUrl;
        }

        public void setHotdetailsUrl(String hotdetailsUrl) {
            this.hotdetailsUrl = hotdetailsUrl;
        }

    }

}
