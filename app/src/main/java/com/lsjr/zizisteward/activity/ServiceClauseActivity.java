package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class ServiceClauseActivity extends BaseActivity {

    private WebView mWebview;

    @Override
    public int getContainerView() {
        return R.layout.activity_service_caluse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setmTitle("服务及隐私条款");
        mWebview = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWebview.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        mWebview.loadUrl(HttpConfig.IMAGEHOST + "/appService");
        initData();
    }

    private void initData() {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "189");
        new HttpClientGet(ServiceClauseActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println("开始不考试包括" + result);
                LianJie jie = GsonUtil.getInstance().fromJson(result, LianJie.class);

            }
        });

    }

    public class LianJie {
        private String error;
        private String msg;
        private String content;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }
}
