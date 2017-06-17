package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
public class OrderTicketServiceActivity extends BaseActivity {

    private WebView webview;

    @Override
    public int getContainerView() {
        return R.layout.activity_order_ticket_service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("订票服务");
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
        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "308");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("订票服务" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String url = jsonObject.getString("TicketsServiceUrl");
                    System.out.println("订票服务连接" + HttpConfig.IMAGEHOST + url);
                    webview.loadUrl(HttpConfig.IMAGEHOST + url);
                    webview.addJavascriptInterface(new DemoJavaInterface(), "control");
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
        public void addshade(int code, String msg) {
            if (code >= 0) {
                Intent intent = new Intent(OrderTicketServiceActivity.this, BasicWebViewActivity.class);
                startActivity(intent);
                finish();
            } else {
                ToastUtils.show(getApplicationContext(), msg);
            }
        }

    }

}
