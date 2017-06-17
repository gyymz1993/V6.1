package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
public class ComboDetailActivity extends BaseActivity {
    private WebView mWebview;
    private String mSpid;

    @Override
    public int getContainerView() {
        return R.layout.activity_travel_custom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("套餐详情");
        mSpid = getIntent().getStringExtra("spid");
        System.out.println("我的id" + mSpid);
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
        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "273");
        map.put("spid", mSpid);
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("套餐详情" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String comboDetailUrl = jsonObject.getString("comboDetailUrl");
                    System.out.println("套餐详情地址" + HttpConfig.IMAGEHOST + comboDetailUrl);

                    mWebview.loadUrl(HttpConfig.IMAGEHOST + comboDetailUrl);// 联网操作
                    mWebview.addJavascriptInterface(new DemoInterface(), "control");

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

    public class DemoInterface {

        @JavascriptInterface
        public void Callthehousekeeper() {
            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
            startActivity(phoneIntent);
        }

        @JavascriptInterface
        public void serviceprogrammedemand(int code, String msg) {// 提交定制
            System.out.println("生命" + code + msg);
            if (code == 1) {
                ToastUtils.show(getApplicationContext(), msg);
            } else {
                ToastUtils.show(getApplicationContext(), msg);
            }

        }
    }
}
