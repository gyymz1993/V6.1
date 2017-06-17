package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.alipay.PayResult;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.utils.WXPayUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class VipUpgradeActivity extends BaseActivity {
    //支付宝参数
    public static final String APPID = "2017050207078320";
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCc5bRHWn8/NEk8iAImVHGI+VKeyZoaYAyk7Poa+xXPiKXaDrZRafhVnhP/MAaYLUucQUl5vUHYryVNgCrZOT0X5v29NAv8rZnemX7wIj0dPawU8zGv9ZxWUfEXDQbzansuVrNKg+1dtwA4/RDlS5A7tiReyS1lGEZGGuANfjKqjdzC1ArgKa8koySKQWviIiit9sF6zn41ezZFdn7ae2cuqZmcKbuSA+gW2bIvg8eaATu3aUksW1EblJRRGt0pJrUPXYEmtcbDUnoHYkqZvysQCWz8N9z8TVF5KQ+406J1DIeef7Dnxp5d9tUqVBDTZDxdRX6YyBakjCvMSuGqBmBDAgMBAAECggEBAI0pTi30ig//pvTDGcwKpaurRl5+3Btm13l7jPrBdTono7CxE+/j+/5sH2m1d/lqp2CKjOTvFlMyIcm8ytBt4z2iIiEctrw8JPhV+hNrerjYR/8BX28E+Afh6ZvnqJ1Q7Esgzmb4k5OBlr2vUog6mCZhFhAByMnwMSnZ/EOni0ZOtRXZdZSBH2JlT+KOQpFopKZA49RoqR+5oOUHwkxMr36OomwbQPht+khCoHo8roy/6RDEBFyMMTmRvmvtiHzhFxOTx7r2zfWAQdcPrNwBr4elgMapHtm0dJqoDiInYCnXYycuOlBc9g0fnggXMjv13eg227rYJIrGH+6TPe4C6UECgYEA84OVTYHrazzFPbz0H9SSvMY0nSe4qgOQMHDDIzjti0Z8kubogM/Cdm7yPj7Nh2N3dL0MBsAqfFMyHD4dO7C2vhMxlSh12CgSZjjK40xTFUQlIwvC9kxmcQADeBKlCDF9+o0ThT33C79zARVp7Mj1nsFLkTIAF22Y+24pQC4m1ekCgYEApPEr5kZweO2Gd/O1F4m7/hEZ9JQNI5NiajuK2EDGXt8IJx6TVXPZrz7H00LrhyYrEdeXgHz244TwAZwd9yV865/bJzN8CJpR/DEEsQHiVfo0LEs9mXsKiaT6I00aQZ6uU9WN2upC289gixdo+c8bix9MdA3de2PZJQ8eBDCV7UsCgYEAjERBHhI1/uFUZAmRPTx/AYnSCKw2rIe86IorfQBvpAgH/b5QMtJ5myqxErWuQcxDpNS4NrM+RbrOZIJK6HUT7ky0BMz3hHkgkA0qoN74BInqMlO2C5VadMCjPujOcve/LzFQCzH0OaofnoItL51aDgYddxcsAlK5CiscS8HJ0PECgYBAUkBRU4TG07HNkz86h57FDDw38YhSKEaHsOKLRG9XTdhrEPRZrYzlVzErxHv+vzaqhY1yMMlCnnPN3OiemYLmi/c1iVFENZHlK+RtdpOh/alc4JaMBLxuQuS84XNsxYmr7aqdBR+/glZex2lLiaVvEmyJEWMenChw2D1XWu8MkwKBgQDAdgYMD5eJlL5dfhDBmESWidU9HDsQrow7I0vLdgWpMpYuRaNxQUGSnVtri7R+o5P3sLcd4RPmWvKWwb8vr3CuQr4pXJZ/PzxKRSqzOAo+oLWg6gr/g+PDm8NqHO9jmPY2P3g1pmwqRn4LnmhIf7dQ3k8jlXEIUDpXk1/+UUcE/w==";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private WebView webview;
    private String mUrl;
    private RelativeLayout mRe_back;

    @Override
    public int getContainerView() {
        return R.layout.activity_vip_upgrade;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("会员升级");
        mUrl = getIntent().getStringExtra("url");
        webview = (WebView) findViewById(R.id.webview);
        mRe_back = (RelativeLayout) findViewById(R.id.iv_back);
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

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

        webview.loadUrl(HttpConfig.IMAGEHOST + mUrl);
        webview.addJavascriptInterface(new DemoJavaInterface(), "control");
        mRe_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    public class DemoJavaInterface {

        @JavascriptInterface
        public void Memberstobuy(int code, final String msg, final String gunm, final String Serialnumber, final String price, final String whether) {// 返回按钮交互
            System.out.println("什么码" + code + "  信息" + msg + "  订单号" + gunm + "  流水号" + Serialnumber + "  价格" + price
                    + "  什么类型" + whether);
            if (code > 0) {
                final Dialog dialog = new Dialog(VipUpgradeActivity.this, R.style.dialog);
                dialog.setContentView(R.layout.dialog_pay_selector);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER | Gravity.CENTER);
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                RelativeLayout re_wei = (RelativeLayout) dialog.findViewById(R.id.re_wei);
                RelativeLayout re_zhifu = (RelativeLayout) dialog.findViewById(R.id.re_zhifu);
                RelativeLayout re_yinlian = (RelativeLayout) dialog.findViewById(R.id.re_yinlian);
                final ImageView iv_click_weixin = (ImageView) dialog.findViewById(R.id.iv_click_weixin);
                final ImageView iv_click_weixin_down = (ImageView) dialog
                        .findViewById(R.id.iv_click_weixin_down);
                final ImageView iv_click_zhifubao = (ImageView) dialog.findViewById(R.id.iv_click_zhifubao);
                final ImageView iv_click_zhifubao_down = (ImageView) dialog
                        .findViewById(R.id.iv_click_zhifubao_down);
                final ImageView iv_click_yinlian = (ImageView) dialog.findViewById(R.id.iv_click_yinlian);
                final ImageView iv_click_yinlian_down = (ImageView) dialog
                        .findViewById(R.id.iv_click_yinlian_down);
                TextView tv_pay = (TextView) dialog.findViewById(R.id.tv_pay);
                re_wei.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (iv_click_weixin.getVisibility() == View.VISIBLE) {
                            iv_click_weixin.setVisibility(View.GONE);
                            iv_click_weixin_down.setVisibility(View.VISIBLE);
                            iv_click_yinlian.setVisibility(View.VISIBLE);
                            iv_click_yinlian_down.setVisibility(View.GONE);
                            iv_click_zhifubao.setVisibility(View.VISIBLE);
                            iv_click_zhifubao_down.setVisibility(View.GONE);
                        }
                    }
                });
                re_zhifu.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (iv_click_zhifubao.getVisibility() == View.VISIBLE) {
                            iv_click_weixin.setVisibility(View.VISIBLE);
                            iv_click_weixin_down.setVisibility(View.GONE);
                            iv_click_yinlian.setVisibility(View.VISIBLE);
                            iv_click_yinlian_down.setVisibility(View.GONE);
                            iv_click_zhifubao.setVisibility(View.GONE);
                            iv_click_zhifubao_down.setVisibility(View.VISIBLE);
                        }
                    }
                });
                re_yinlian.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (iv_click_yinlian.getVisibility() == View.VISIBLE) {
                            iv_click_weixin.setVisibility(View.VISIBLE);
                            iv_click_weixin_down.setVisibility(View.GONE);
                            iv_click_yinlian.setVisibility(View.GONE);
                            iv_click_yinlian_down.setVisibility(View.VISIBLE);
                            iv_click_zhifubao.setVisibility(View.VISIBLE);
                            iv_click_zhifubao_down.setVisibility(View.GONE);
                        }
                    }
                });
                tv_pay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (iv_click_weixin_down.getVisibility() == View.GONE
                                && iv_click_zhifubao_down.getVisibility() == View.GONE
                                && iv_click_yinlian_down.getVisibility() == View.GONE) {
                            ToastUtils.show(VipUpgradeActivity.this, "请选择一种支付方式");
                        } else {
                            dialog.dismiss();
                            if (iv_click_weixin_down.getVisibility() == View.VISIBLE) {//微信支付
                                HashMap<String, String> map = new HashMap<>();
                                map.put("OPT", "338");
                                map.put("tradeNo", gunm);
                                map.put("amount", price);
                                map.put("body", msg);
                                map.put("user_ip", WXPayUtils.getIPAddress(VipUpgradeActivity.this));
                                map.put("transflow_type", "1");
                                new HttpClientGet(VipUpgradeActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.print("后台返回值" + result);
                                        NewPersonalActivity.WeiXinPayBean wx_bean = GsonUtil.getInstance().fromJson(result, NewPersonalActivity.WeiXinPayBean.class);
                                        List<NewPersonalActivity.alipayValue> wx_code = wx_bean.getAlipayValue();
                                        PayReq payReq = new PayReq();
                                        payReq.appId = wx_code.get(0).getAppid();/*应用id*/
                                        payReq.partnerId = wx_code.get(0).getPartnerid();/*商户号*/
                                        payReq.prepayId = wx_code.get(0).getPrepayid();/*预支付订单号*/
                                        payReq.nonceStr = wx_code.get(0).getNoncestr();/*随机字符串*/
                                        payReq.timeStamp = wx_code.get(0).getTimestamp();/*时间戳*/
                                        payReq.packageValue = "Sign=WXPay";/*扩展字段*/
                                        payReq.sign = wx_code.get(0).getSign();/*签名*/
                                        if (WXPayUtils.isWXAppInstall()) {
                                            App.msgApi.sendReq(payReq);
                                        } else {
                                            ToastUtils.show(getApplicationContext(), "您还未安装微信APP，请先安装APP");
                                        }
                                    }
                                });
                            }
                            if (iv_click_zhifubao_down.getVisibility() == View.VISIBLE) {//支付宝支付
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("OPT", "94");
                                map.put("tradeNo", gunm);
                                map.put("amount", price);
                                map.put("description", msg);
                                map.put("title", msg);
                                map.put("transflow_type", "1");
                                new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println(result);
                                        JSONObject jObject;
                                        try {
                                            jObject = new JSONObject(result);
                                            final String orderInfo = jObject.getString("alipayValue");
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(VipUpgradeActivity.this);
                                                    Map<String, String> result = alipay.payV2(orderInfo, true);
                                                    Log.i("msp", result.toString());
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = result;
                                                    alipayHandler.sendMessage(msg);
                                                }
                                            };
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
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
                            if (iv_click_yinlian_down.getVisibility() == View.VISIBLE) {//银联支付
                                Intent intent = new Intent(getApplicationContext(), VipJARActivity.class);
                                intent.putExtra("gunm", gunm);
                                intent.putExtra("Serialnumber", Serialnumber);
                                intent.putExtra("price", price);
                                intent.putExtra("whether", whether);
                                startActivity(intent);
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                ToastUtils.show(getApplicationContext(), msg);
            }


        }

    }

    @SuppressLint("HandlerLeak")
    private Handler alipayHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    System.out.println("resultInfo:  " + resultInfo);
                    System.out.println("resultStatus:  " + resultStatus);

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    // android webview点击返回键返回上一个html
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
