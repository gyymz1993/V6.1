package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class PhysicaDetailsActivity extends Activity implements OnClickListener {

    private String id;
    private WebView wv;
    private TextView tv_sure;
    private LinearLayout ll_back;
    private LinearLayout ll_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.physica_details_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.wv = (WebView) super.findViewById(R.id.wv);
        this.tv_sure = (TextView) super.findViewById(R.id.tv_sure);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_share = (LinearLayout) super.findViewById(R.id.ll_share);

        this.tv_sure.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_share.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");

        this.wv.getSettings().setDomStorageEnabled(true);

        WebSettings s = wv.getSettings();

        s.setJavaScriptEnabled(true);

        this.getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "306");
        map.put("shopid", id);
        new HttpClientGet(PhysicaDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jObject = new JSONObject(result);
                    String Thepath = jObject.getString("Thepath");
                    String error = jObject.getString("error");

                    if (error.equals("1")) {
                        wv.loadUrl(HttpConfig.IMAGEHOST + Thepath);
                    }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_sure:
                startActivity(new Intent(PhysicaDetailsActivity.this, MedicalAppointmentActivity.class).putExtra("id", id));
                break;

            case R.id.ll_share:

                getShareUrl();

                break;
        }
    }

    private void getShareUrl() {
        Map<String,String> map = new HashMap<>();
        map.put("OPT","511");
        map.put("user_id",EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()),"u"));
        map.put("shop_id",id);
        new HttpClientGet(PhysicaDetailsActivity.this,null,map,false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    //图片
                    String spicfirst = object.getString("spicfirst");
                    //名称
                    String sname = object.getString("sname");
                    //卖点
                    String url = object.getString("url");
                    //跳转路径
                    String sell_points = object.getString("sell_points");

                    ShareSDK.initSDK(PhysicaDetailsActivity.this);
                    OnekeyShare oks = new OnekeyShare();
                    oks.disableSSOWhenAuthorize();
                    String productUrl = HttpConfig.IMAGEHOST + url;
                    oks.setTitle("体检分享");
                    oks.setText(sell_points);
                    oks.setTitleUrl(productUrl);
                    oks.setImageUrl(HttpConfig.IMAGEHOST + spicfirst);
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
                                App.AddShareRecord(PhysicaDetailsActivity.this,0);
                        }

                        @Override
                        public void onCancel(Platform arg0, int arg1) {
                            System.out.println("取消分享");
                        }
                    });

                    oks.show(PhysicaDetailsActivity.this);
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
