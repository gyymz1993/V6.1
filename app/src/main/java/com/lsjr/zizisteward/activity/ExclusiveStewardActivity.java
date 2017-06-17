package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class ExclusiveStewardActivity extends BaseActivity {

    private WebView webview;
    private String mList;
    private RelativeLayout mIv_back;

    @Override
    public int getContainerView() {
        return R.layout.activity_exclusive_steward;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("专属管家");
        mList = getIntent().getStringExtra("list");
        webview = (WebView) findViewById(R.id.webview);
        mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
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
        initLayout();
    }

    private void initLayout() {
        HashMap<String, String> map = new HashMap<>();
        if ("0".equals(mList)) {// 管家列表进来的
            map.put("OPT", "243");
            map.put("stname", getIntent().getStringExtra("name"));
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
            new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("跟新管家消息" + result);
                    GuanJiaBean2 bean2 = GsonUtil.getInstance().fromJson(result, GuanJiaBean2.class);
                    webview.loadUrl(HttpConfig.IMAGEHOST + bean2.upStewardUrl);
                    webview.addJavascriptInterface(new DemoJavaInterface(), "control");

                }

                @Override
                public void onFailure(MyError myError) {
                    super.onFailure(myError);
                }

            });

        } else if ("1".equals(mList)) {// 个人中心进来的
            map.put("OPT", "242");
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
            new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("查看管家消息" + result);
                    GuanJiaBean bean = GsonUtil.getInstance().fromJson(result, GuanJiaBean.class);
                    webview.loadUrl(HttpConfig.IMAGEHOST + bean.stewardUrl);
                    webview.addJavascriptInterface(new DemoJavaInterface(), "control");

                }

                @Override
                public void onFailure(MyError myError) {
                    super.onFailure(myError);
                }
            });
        }

    }

    String stname2;

    public class DemoJavaInterface {
        @JavascriptInterface
        public void Replacethehousekeeper(String stname) {
            stname2 = stname;
            Intent intent = new Intent(getApplicationContext(), StewardListActivity.class);
            intent.putExtra("stname2", stname2);
            startActivity(intent);
        }

        @JavascriptInterface
        public void Contactthehousekeeping(String gmobiles) {// 联系管家
            System.out.println("管家电话" + gmobiles);
            Intent phoneIntent1 = new Intent("android.intent.action.CALL", Uri.parse("tel:" + gmobiles));
            startActivity(phoneIntent1);
        }

        @JavascriptInterface
        public void Contactplatform() {
            Intent phoneIntent2 = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
            startActivity(phoneIntent2);
        }

        // 点击升级
        @JavascriptInterface
        public void Membershipupgrade(String url) {
            Intent intent = new Intent(getApplicationContext(), VipUpgradeActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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

    public class GuanJiaBean {
        private String error;
        private String msg;
        private String stewardUrl;

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

        public String getStewardUrl() {
            return stewardUrl;
        }

        public void setStewardUrl(String stewardUrl) {
            this.stewardUrl = stewardUrl;
        }

    }

    public class GuanJiaBean2 {
        private String error;
        private String msg;
        private String upStewardUrl;

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

        public String getUpStewardUrl() {
            return upStewardUrl;
        }

        public void setUpStewardUrl(String upStewardUrl) {
            this.upStewardUrl = upStewardUrl;
        }
    }
}
