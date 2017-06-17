package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.common.activtiy.YuDingInfoActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

@SuppressLint("SetJavaScriptEnabled")
public class HotelDetailActivity extends Activity {

    private WebView mWebview;
    private String mId;
    private RelativeLayout mIv_back, share;
    private String mShare_title;
    private String mShare_img;
    private String mShare_url;
    private String mShare_content;
    private String mImagePath, Yourtime, Checktime;
    private ImageView mIv_image;
    private String new_weixin_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        mIv_image = (ImageView) findViewById(R.id.iv_image);
        mIv_back = (RelativeLayout) findViewById(R.id.back);
        share = (RelativeLayout) findViewById(R.id.share);
        mWebview = (WebView) findViewById(R.id.webview);
        mId = getIntent().getStringExtra("id");
        mImagePath = getIntent().getStringExtra("imagePath");
        Checktime = getIntent().getStringExtra("Checktime");
        Yourtime = getIntent().getStringExtra("Yourtime");
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
        getShareData();
        getData();
        initLayout();
    }

    public String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        return file.getPath();
    }

    private void getShareData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "317");
        map.put("shop_id", mId);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("分享消息" + result);
                HotelShareBean bean = GsonUtil.getInstance().fromJson(result, HotelShareBean.class);
                mShare_title = bean.getSell_points();
                mShare_img = bean.getSpicfirst();
                mShare_url = bean.getUrl();
                mShare_content = bean.getSname();
                Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + mShare_img).into(mIv_image);
            }
        });
    }

    private void initLayout() {
        mIv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebview.canGoBack()) {
                    mWebview.goBack();
                } else {
                    finish();
                }

            }
        });
        share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(HotelDetailActivity.this);
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                String productUrl = HttpConfig.IMAGEHOST + mShare_url;
                oks.setTitle(mShare_title);
                oks.setTitleUrl(productUrl);
                oks.setText(mShare_content);
                oks.setImageUrl(HttpConfig.IMAGEHOST + mShare_img);// qq好友

                mIv_image.setDrawingCacheEnabled(true);
                Bitmap bitmap = mIv_image.getDrawingCache();
                new_weixin_url = saveImageToGallery(HotelDetailActivity.this, bitmap);

                oks.setImagePath(new_weixin_url);// 确保sdcard
                oks.setUrl(productUrl);
                oks.setSite("孜孜官网");
                oks.setSiteUrl(productUrl);
                oks.setCallback(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        System.out.println("分享失败");
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "300");
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        new HttpClientGet(getApplicationContext(), null, map, false,
                                new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println("积分结果" + result);
                                    }
                                });
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        System.out.println("取消分享");
                    }
                });
                oks.show(HotelDetailActivity.this);
            }
        });
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "311");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("shopid", mId);
        map.put("Checktime", Checktime);
        map.put("Yourtime", Yourtime);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("商品详情" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String url = jsonObject.getString("GrogshopServiceUrl");
                    mWebview.loadUrl(HttpConfig.IMAGEHOST + url);
                    mWebview.addJavascriptInterface(new DemoJavaInterface(), "control");
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
        public void addroomreservation(int code, String msg) {// 提交
            if (code >= 0) {
                Intent intent = new Intent(HotelDetailActivity.this, BasicWebViewActivity.class);
                startActivity(intent);
                finish();
            } else {
                ToastUtils.show(getApplicationContext(), msg);
            }
        }

        @JavascriptInterface
        public void Thepath(String url) {
            Intent intent = new Intent(getApplicationContext(), YuDingInfoActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("type", "2");
            startActivity(intent);
        }
    }

    // 重写返回键
    // android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class HotelShareBean {
        private String error;
        private String msg;
        private String sell_points;
        private String sinfo;
        private String sname;
        private String spicfirst;
        private String url;

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

        public String getSell_points() {
            return sell_points;
        }

        public void setSell_points(String sell_points) {
            this.sell_points = sell_points;
        }

        public String getSinfo() {
            return sinfo;
        }

        public void setSinfo(String sinfo) {
            this.sinfo = sinfo;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSpicfirst() {
            return spicfirst;
        }

        public void setSpicfirst(String spicfirst) {
            this.spicfirst = spicfirst;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
