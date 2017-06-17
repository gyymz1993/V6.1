package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CateLabelBean;
import com.lsjr.zizisteward.bean.CateLabelBean.Cate_Label;
import com.lsjr.zizisteward.bean.LianJieBean;
import com.lsjr.zizisteward.bean.QueryAddressBean;
import com.lsjr.zizisteward.bean.QueryAddressInfo;
import com.lsjr.zizisteward.common.activtiy.NewShopDetailActivity;
import com.lsjr.zizisteward.common.activtiy.YuDingInfoActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.ExpertReviewActivity;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;
import com.lsjr.zizisteward.ly.activity.NotExpertReviewActivity;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.BitmapUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

//商品详情
//html页面  立即下单  判断当前用户是否登录  如果没有登录 则跳转到登录界面
@SuppressLint({"SetJavaScriptEnabled", "NewApi", "JavascriptInterface"})
public class HomeBrandDetail extends Activity {
    private WebView webview;
    private LianJieBean mBean;
    private boolean mState;
    private String url;
    private String mSid, leixing, hava_dianpu;
    private RelativeLayout back, re_share;
    private List<Cate_Label> cate_label;
    private String base_id = null;
    private String webview_title;
    private ImageView iv_shane_ceshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_brand_detail);
        mSid = getIntent().getStringExtra("sid");
        leixing = getIntent().getStringExtra("mode");
        hava_dianpu = getIntent().getStringExtra("hava_dianpu");
        getData();
        back = (RelativeLayout) findViewById(R.id.back);/*返回按钮*/
        re_share = (RelativeLayout) findViewById(R.id.re_share);
        iv_shane_ceshi = (ImageView) findViewById(R.id.iv_shane_ceshi);/*分享缩略图*/
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

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
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

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webview_title = title;
            }

        };

        webview.setWebChromeClient(wvcc);
    }

    @Override
    protected void onResume() {
        mState = PreferencesUtils.getBoolean(HomeBrandDetail.this, "isLogin");

        if (mState == true) {
            re_share.setVisibility(View.VISIBLE);
        } else {
            re_share.setVisibility(View.GONE);
        }

        if (mState == true) {
            getShareData();
        }

        super.onResume();
    }

    private void getShareData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "511");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("shop_id", mSid);
        new HttpClientGet(HomeBrandDetail.this, null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.print("结果" + result);
                final ShopShareBean bean = GsonUtil.getInstance().fromJson(result, ShopShareBean.class);
                Glide.with(HomeBrandDetail.this).load(HttpConfig.IMAGEHOST + bean.getSpicfirst()).into(iv_shane_ceshi);
                re_share.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareSDK.initSDK(HomeBrandDetail.this);
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();
                        String productUrl = HttpConfig.IMAGEHOST + bean.getUrl();
                        oks.setTitle(bean.getSname());
                        oks.setTitleUrl(productUrl);
                        oks.setText(bean.getSell_points());
                        oks.setImageUrl(HttpConfig.IMAGEHOST + bean.getSpicfirst());
                        System.out.println("什么地址" + HttpConfig.IMAGEHOST + "");

                        iv_shane_ceshi.setDrawingCacheEnabled(true);
                        Bitmap bm = iv_shane_ceshi.getDrawingCache();
                        String imagePath = BitmapUtils.saveImageToGallery(HomeBrandDetail.this, bm);

                        oks.setImagePath(imagePath);// 确保sdcard 存在图片
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
                                App.AddShareRecord(HomeBrandDetail.this, 0);
                            }

                            @Override
                            public void onCancel(Platform arg0, int arg1) {
                                System.out.println("取消分享");
                            }
                        });
                        oks.show(HomeBrandDetail.this);
                    }
                });
            }
        });

    }

    private void getData() {
        boolean state = PreferencesUtils.getBoolean(HomeBrandDetail.this, "isLogin");
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "37");
        map.put("sid", mSid);
        if (state == true) {
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        } else if (state == false) {
            map.put("user_id", "");
        }
        new HttpClientGet(HomeBrandDetail.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                mBean = GsonUtil.getInstance().fromJson(result, LianJieBean.class);
                System.out.println(HttpConfig.IMAGEHOST + mBean.getProductUrl());
                webview.loadUrl(HttpConfig.IMAGEHOST + mBean.getProductUrl());
                webview.addJavascriptInterface(new DemoJavaInterface(), "control");
                System.out.println("下单链接" + HttpConfig.IMAGEHOST + mBean.getProductUrl());

            }
        });

    }

    private Dialog mDialog;
    private ProgressBar mProgressBar;

    public class DemoJavaInterface {

        public DemoJavaInterface() {
            super();
        }

        @JavascriptInterface
        public void sendUrls(String urls, String state) {// 立即下单
            System.out.println("李莎碰传过来" + urls + "类型" + state);
            url = urls;
            if (mState == true) {// 已登录
                // 判断用户是否增加收货地址
                Map<String, String> map = new HashMap<String, String>();
                map.put("OPT", "13");
                map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                new HttpClientGet(HomeBrandDetail.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        QueryAddressBean bean = GsonUtil.getInstance().fromJson(result, QueryAddressBean.class);
                        List<QueryAddressInfo> list = bean.getCheckaddr();
                        if (list.size() > 0) {
                            Intent intent = new Intent(HomeBrandDetail.this, NewJARActivty.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        } else {
                            mDialog = new Dialog(HomeBrandDetail.this, R.style.dialog);
                            mDialog.setContentView(R.layout.popup_delete_address);
                            Window window = mDialog.getWindow();
                            window.setGravity(Gravity.CENTER | Gravity.CENTER);
                            TextView msg = (TextView) mDialog.findViewById(R.id.tv_msg);
                            msg.setText("您还没有收货地址,请前去增加");
                            ((TextView) mDialog.findViewById(R.id.tv_cancel)).setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });
                            ((TextView) mDialog.findViewById(R.id.tv_confirm))
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            mDialog.dismiss();
                                            Intent intent = new Intent(HomeBrandDetail.this, AddAddressActivity.class);
                                            intent.putExtra("brand_detail", "brand_detail");
                                            startActivityForResult(intent, 527);

                                        }
                                    });

                            mDialog.show();
                        }

                    }
                });
            } else if (mState == false) {

                mDialog = new Dialog(HomeBrandDetail.this, R.style.dialog);
                mDialog.setContentView(R.layout.popup_delete_address);
                Window window = mDialog.getWindow();
                window.setGravity(Gravity.CENTER | Gravity.CENTER);
                TextView msg = (TextView) mDialog.findViewById(R.id.tv_msg);
                msg.setText("您还未登录,请前去登录");

                ((TextView) mDialog.findViewById(R.id.tv_cancel)).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                ((TextView) mDialog.findViewById(R.id.tv_confirm)).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        Intent intent = new Intent(HomeBrandDetail.this, NoteLoginActivity.class);
                        intent.putExtra("personal", "homebrand");
                        startActivityForResult(intent, 527);

                    }
                });

                mDialog.show();
            }

        }

        @JavascriptInterface
        public void housekeeper() {// 联系管家
            if (mState == true) {
                Intent phoneIntent = new Intent(getApplicationContext(), CallButtonActivtiy.class);
                startActivity(phoneIntent);
            } else {
                Intent intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
                intent.putExtra("personal", "home_brand_2");
                startActivity(intent);
            }

        }

        @JavascriptInterface
        public void addfillininformation(int code, String msg) {
            if (code >= 0) {
                ToastUtils.show(getApplicationContext(), msg);
                Intent intent = new Intent(HomeBrandDetail.this, BasicWebViewActivity.class);
                startActivity(intent);
                finish();
            } else {
                System.out.println("李晨奇" + msg);
                ToastUtils.show(getApplicationContext(), msg);
            }

        }

        @JavascriptInterface
        public void prompt(String msg) {
            ToastUtils.show(getApplicationContext(), msg);
        }

        @JavascriptInterface
        public void appointmentsubmit(int code, String msg) {// 预定交互
            if (code >= 0) {
                startActivity(new Intent(getApplicationContext(), BasicWebViewActivity.class));
                finish();
            } else {
                ToastUtils.show(getApplicationContext(), msg);
            }
        }

        @JavascriptInterface
        public void Participatereview() {// 参与点评
            if (mState == true) {
                Map<String, String> map = new HashMap<>();
                map.put("OPT", "296");
                map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                new HttpClientGet(HomeBrandDetail.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {

                        CateLabelBean clBean = new Gson().fromJson(result, CateLabelBean.class);
                        if (null != clBean && clBean.getError().equals("1")) {
                            cate_label = clBean.getCate_label();

                            if (clBean.getIs_expert().equals("0")) {
                                startActivity(new Intent(HomeBrandDetail.this, NotExpertReviewActivity.class)
                                        .putExtra("label", (Serializable) cate_label).putExtra("id", mSid));
                            } else {
                                startActivity(new Intent(HomeBrandDetail.this, ExpertReviewActivity.class)
                                        .putExtra("label", (Serializable) cate_label).putExtra("id", mSid));
                            }
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
            } else {
                Intent intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
                intent.putExtra("personal", "dianpin");
                startActivity(intent);
            }

        }

        @JavascriptInterface
        public void thephone(String number) {// 电话联系商家
            System.out.println("商家电话" + number);
            Intent phoneIntent1 = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
            startActivity(phoneIntent1);
        }

        @JavascriptInterface
        public void Thepath(String url) {// 酒店预订交互
            Intent intent = new Intent(getApplicationContext(), YuDingInfoActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("type", "2");
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void Shopdetailspage(String brandid) {// 回到店铺
            if (hava_dianpu.equals("yes")) {
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), NewShopDetailActivity.class);
                intent.putExtra("id", brandid);
                intent.putExtra("title", webview_title);
                if (leixing.equals("jiangpin")) {
                    intent.putExtra("type", "shop");
                } else {
                    intent.putExtra("type", "");
                }
                startActivity(intent);
            }

        }

        @JavascriptInterface
        public void Darenrecommended(String shopid) {// 达人推荐
            if (mState == true) {
                Intent intent = new Intent(getApplicationContext(), GoodsEvaluationActivity.class);
                intent.putExtra("shopid", shopid);
                startActivity(intent);
            } else {
                base_id = shopid;
                Intent intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
                intent.putExtra("personal", "daren");
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 517) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), GoodsEvaluationActivity.class);
                    intent.putExtra("shopid", base_id);
                    startActivity(intent);
                }
            }, 200);
        }
        if (requestCode == 527) {
            webview.reload();
            getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public class ShopShareBean {
        private String error;

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

        private String msg;
        private String sell_points;
        private String sname;
        private String spicfirst;
        private String url;
    }

}
