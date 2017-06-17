package com.lsjr.zizisteward.ly.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.CallButtonActivtiy;
import com.lsjr.zizisteward.activity.HotelDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CateLabelBean;
import com.lsjr.zizisteward.bean.CateLabelBean.Cate_Label;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class FoodDetailsActivity extends Activity implements OnClickListener {

    private LinearLayout ll_back;
    private LinearLayout ll_share;
    private WebView wv;
    private String id;
    private String cateDetailUrl;
    private String cateShareUrl;
    private String sell_points;
    private String sname;
    private String spic;
    private TextView tv_title;
    private List<Cate_Label> cate_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.food_details_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.wv = (WebView) super.findViewById(R.id.wv);
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_share = (LinearLayout) super.findViewById(R.id.ll_share);

        this.ll_back.setOnClickListener(this);
        this.ll_share.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");

        getAddress();

    }

    public class DemoJavaInterface {

        @JavascriptInterface
        public void housekeeper() {
            //呼叫管家
            //startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006")));

            if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
                Toast.makeText(FoodDetailsActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
            } else {
                App.CallSteward(FoodDetailsActivity.this);
            }
        }

        @JavascriptInterface
        public void thephone(String number) {
            //拨打电话

            if (number.length() > 1) {
                startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + number)));
            }
        }

        @JavascriptInterface
        public void Participatereview() {
            //参与点评
            getData();
        }

        @JavascriptInterface
        public void appointmentsubmit(String code, String msg) {
            //预约提交
            startActivityForResult(new Intent(FoodDetailsActivity.this, ReservationSuccessTips.class), 7);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 1:
                wv.loadUrl(HttpConfig.IMAGEHOST + cateDetailUrl);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:

                if (wv.canGoBack()) {
                    wv.goBack();
                } else {
                    finish();
                }

                break;

            case R.id.ll_share:
                ShareSDK.initSDK(FoodDetailsActivity.this);
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                String productUrl = HttpConfig.IMAGEHOST + cateShareUrl;
                oks.setTitle(sell_points);
                oks.setTitleUrl(productUrl);
                oks.setImageUrl(HttpConfig.IMAGEHOST + spic);
                //oks.setText(productUrl);
                //oks.setImagePath("/sdcard/logods.jpg");//确保sdcard 存在图片
                oks.setUrl(productUrl);
                oks.setSite("孜孜管家");
                oks.setSiteUrl(productUrl);

                oks.setCallback(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        System.out.println("分享失败");
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        App.AddShareRecord(FoodDetailsActivity.this,0);
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        System.out.println("取消分享");
                    }
                });

                oks.show(FoodDetailsActivity.this);
                break;
        }
    }

    private void getAddress() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "298");
        map.put("shop_id", id);
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(FoodDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println(result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    cateDetailUrl = jsonObject.getString("cateDetailUrl");
                    String expertUrl = jsonObject.getString("expertUrl");
                    cateShareUrl = jsonObject.getString("cateShareUrl");
                    sell_points = jsonObject.getString("sell_points");
                    sname = jsonObject.getString("sname");
                    spic = jsonObject.getString("spic");

                    setUrl();

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

    private void setUrl() {

        WebSettings s = wv.getSettings();
        wv.setVerticalScrollBarEnabled(false);
        s.setJavaScriptEnabled(true);
        s.setSupportZoom(true);
        s.setBuiltInZoomControls(true);

        wv.setVerticalScrollBarEnabled(false);
        wv.addJavascriptInterface(new DemoJavaInterface(), "control");

        tv_title.setText(sname);

        wv.loadUrl(HttpConfig.IMAGEHOST + cateDetailUrl);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (url.contains("domesticconsumercomment")) {
                    tv_title.setText(sname);
                }

                if (url.contains("talentrecommendation")) {
                    tv_title.setText("达人推荐");
                }
                super.onPageFinished(view, url);
            }
        });
    }

    // android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
            wv.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "296");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(FoodDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                CateLabelBean clBean = new Gson().fromJson(result, CateLabelBean.class);
                if (null != clBean && clBean.getError().equals("1")) {
                    cate_label = clBean.getCate_label();

                    if (clBean.getIs_expert().equals("0")) {
                        startActivity(new Intent(FoodDetailsActivity.this, NotExpertReviewActivity.class)
                                .putExtra("label", (Serializable) cate_label).putExtra("id", id));
                    } else {
                        startActivity(new Intent(FoodDetailsActivity.this, ExpertReviewActivity.class)
                                .putExtra("label", (Serializable) cate_label).putExtra("id", id));
                    }
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }
}
