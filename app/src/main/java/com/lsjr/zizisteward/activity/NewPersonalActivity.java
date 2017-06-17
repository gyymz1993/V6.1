package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.alipay.PayResult;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.common.activtiy.SelectorAddressActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.utils.WXPayUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint({"SetJavaScriptEnabled", "NewApi", "JavascriptInterface"})
public abstract class NewPersonalActivity extends Activity implements Callback, Runnable {
    private Context context;
    private Handler mHandler;
    private ProgressDialog dialog = null;
    private final String mMode = "00";
    private WebView webview;
    private String push_link;

    //支付宝参数
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017050207078320";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097
     * &docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCc5bRHWn8/NEk8iAImVHGI+VKeyZoaYAyk7Poa+xXPiKXaDrZRafhVnhP/MAaYLUucQUl5vUHYryVNgCrZOT0X5v29NAv8rZnemX7wIj0dPawU8zGv9ZxWUfEXDQbzansuVrNKg+1dtwA4/RDlS5A7tiReyS1lGEZGGuANfjKqjdzC1ArgKa8koySKQWviIiit9sF6zn41ezZFdn7ae2cuqZmcKbuSA+gW2bIvg8eaATu3aUksW1EblJRRGt0pJrUPXYEmtcbDUnoHYkqZvysQCWz8N9z8TVF5KQ+406J1DIeef7Dnxp5d9tUqVBDTZDxdRX6YyBakjCvMSuGqBmBDAgMBAAECggEBAI0pTi30ig//pvTDGcwKpaurRl5+3Btm13l7jPrBdTono7CxE+/j+/5sH2m1d/lqp2CKjOTvFlMyIcm8ytBt4z2iIiEctrw8JPhV+hNrerjYR/8BX28E+Afh6ZvnqJ1Q7Esgzmb4k5OBlr2vUog6mCZhFhAByMnwMSnZ/EOni0ZOtRXZdZSBH2JlT+KOQpFopKZA49RoqR+5oOUHwkxMr36OomwbQPht+khCoHo8roy/6RDEBFyMMTmRvmvtiHzhFxOTx7r2zfWAQdcPrNwBr4elgMapHtm0dJqoDiInYCnXYycuOlBc9g0fnggXMjv13eg227rYJIrGH+6TPe4C6UECgYEA84OVTYHrazzFPbz0H9SSvMY0nSe4qgOQMHDDIzjti0Z8kubogM/Cdm7yPj7Nh2N3dL0MBsAqfFMyHD4dO7C2vhMxlSh12CgSZjjK40xTFUQlIwvC9kxmcQADeBKlCDF9+o0ThT33C79zARVp7Mj1nsFLkTIAF22Y+24pQC4m1ekCgYEApPEr5kZweO2Gd/O1F4m7/hEZ9JQNI5NiajuK2EDGXt8IJx6TVXPZrz7H00LrhyYrEdeXgHz244TwAZwd9yV865/bJzN8CJpR/DEEsQHiVfo0LEs9mXsKiaT6I00aQZ6uU9WN2upC289gixdo+c8bix9MdA3de2PZJQ8eBDCV7UsCgYEAjERBHhI1/uFUZAmRPTx/AYnSCKw2rIe86IorfQBvpAgH/b5QMtJ5myqxErWuQcxDpNS4NrM+RbrOZIJK6HUT7ky0BMz3hHkgkA0qoN74BInqMlO2C5VadMCjPujOcve/LzFQCzH0OaofnoItL51aDgYddxcsAlK5CiscS8HJ0PECgYBAUkBRU4TG07HNkz86h57FDDw38YhSKEaHsOKLRG9XTdhrEPRZrYzlVzErxHv+vzaqhY1yMMlCnnPN3OiemYLmi/c1iVFENZHlK+RtdpOh/alc4JaMBLxuQuS84XNsxYmr7aqdBR+/glZex2lLiaVvEmyJEWMenChw2D1XWu8MkwKBgQDAdgYMD5eJlL5dfhDBmESWidU9HDsQrow7I0vLdgWpMpYuRaNxQUGSnVtri7R+o5P3sLcd4RPmWvKWwb8vr3CuQr4pXJZ/PzxKRSqzOAo+oLWg6gr/g+PDm8NqHO9jmPY2P3g1pmwqRn4LnmhIf7dQ3k8jlXEIUDpXk1/+UUcE/w==";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    protected void onResume() {
        context = this;
        mHandler = new Handler(this);

        webview = (WebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new DemoJavaInterface(), "control");
        System.out.println("支付链接" + HttpConfig.IMAGEHOST + push_link);
        webview.loadUrl(HttpConfig.IMAGEHOST + push_link);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_new_personal_detail);
        push_link = getIntent().getStringExtra("url");
        ((RelativeLayout) findViewById(R.id.re_back)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private String number;
    private String transflow_num;
    private String totalPrice;

    public class DemoJavaInterface {

        public DemoJavaInterface() {
            super();
        }

        /*0元订单点击交互*/
        @JavascriptInterface
        public void indentUrls() {
            Intent intent = new Intent(getApplicationContext(), MyAllOrderActivity.class);
            intent.putExtra("order", "paid");
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void sendGnumTransflowTotaPrice(String gnum, String transflow, String totaPrice) {// 确认下单银联支付
            CustomDialogUtils.startCustomProgressDialog(NewPersonalActivity.this, "正在调转银联支付...");
            System.out.println("订单号" + gnum + "交易号" + transflow);
            number = gnum;
            transflow_num = transflow;
            totalPrice = totaPrice;

            dialog = ProgressDialog.show(context, "", "正在努力的获取tn中,请稍候...", true);
            new Thread(NewPersonalActivity.this).start();

            CustomDialogUtils.stopCustomProgressDialog(NewPersonalActivity.this);
        }

        @JavascriptInterface
        public void usershopaid(String shopid, String userid, String amount, String addressid) {// 选择地址交互
            System.out.println("第一个" + shopid + "  第二个" + userid + "  第三个" + amount + "  第四个" + addressid);
            Intent intent = new Intent(getApplicationContext(), SelectorAddressActivity.class);
            intent.putExtra("shopid", shopid);
            intent.putExtra("amount", amount);
            startActivityForResult(intent, 1);
        }

        /*支付宝支付交互*/
        @JavascriptInterface
        public void ZFBpayment(String body, String subject, String out_trade_no, String total_amount) {
            System.out.println("支付宝参数第一个" + body + "第二个" + subject + "第三个" + out_trade_no + "第四个" + total_amount);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("OPT", "93");
            map.put("tradeNo", out_trade_no);
            map.put("amount", total_amount);
            map.put("description", body);
            map.put("title", subject);
            new HttpClientGet(NewPersonalActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

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
                                PayTask alipay = new PayTask(NewPersonalActivity.this);
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

        /*微信支付交互*/
        @JavascriptInterface
        public void WXpayment(final String body, final String out_trade_no, final String total_amount) {
            System.out.print("描述" + body + "订单号" + out_trade_no + "钱" + total_amount);
            HashMap<String, String> map = new HashMap<>();
            map.put("OPT", "337");
            map.put("tradeNo", out_trade_no);
            map.put("amount", total_amount);
            map.put("body", body);
            map.put("user_ip", WXPayUtils.getIPAddress(NewPersonalActivity.this));
            new HttpClientGet(NewPersonalActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.print("后台返回值" + result);
                    WeiXinPayBean wx_bean = GsonUtil.getInstance().fromJson(result, WeiXinPayBean.class);
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
                        App.getInstance().setWxIndentNo(out_trade_no);
                        App.getInstance().setWxMoney(total_amount);
                    } else {
                        ToastUtils.show(getApplicationContext(), "您还未安装微信APP，请先安装APP");
                    }
                }
            });
        }
    }

    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NewPersonalActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(NewPersonalActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    public static String UnicodeToString(String str) {
        if (str != null && str.trim().length() > 0) {
            String un = str.trim();
            StringBuffer sb = new StringBuffer();
            int idx = un.indexOf("\\u");
            while (idx > 0) {
                if (idx > 0) {
                    sb.append(un.substring(0, idx));
                }
                String hex = un.substring(idx + 2, idx + 2 + 4);
                sb.append((char) Integer.parseInt(hex, 16));
                un = un.substring(idx + 2 + 4);
                idx = un.indexOf("\\u");
            }
            sb.append(un);
            return sb.toString();
        }

        return "";

    }

    @Override
    public void run() {
        Message msg = mHandler.obtainMessage();
        msg.obj = transflow_num;
        mHandler.sendMessage(msg);
    }

    public abstract void doStartUnionPayPlugin(Activity activity, String tn, String mode);

    @Override
    public boolean handleMessage(Message msg) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        String tn = "";
        // String tn = transflow;
        if (msg.obj == null || ((String) msg.obj).length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误提示");
            builder.setMessage("网络连接失败,请重试!");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            tn = (String) msg.obj;
            doStartUnionPayPlugin(this, tn, mMode);

        }
        return false;
    }

    public String msg = "";
    private String result;

    private String sign;
    private String decode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            push_link = data.getStringExtra("url");
            System.out.println("回调地址" + push_link);
            webview.reload();
        } else {
            if (data == null) {
                return;
            }
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {

                if (data.hasExtra("result_data")) {
                    result = data.getExtras().getString("result_data");

                    try {
                        JSONObject resultJson = new JSONObject(result);
                        sign = resultJson.getString("sign");
                        String dataOrg = resultJson.getString("data");

                        boolean ret = verify(dataOrg, sign, mMode);

                        if (ret) {

                            msg = "支付成功";

                        } else {

                            msg = "支付失败";
                        }
                    } catch (JSONException e) {
                    }
                } else {

                    msg = "支付失败";
                }
            } else if (str.equalsIgnoreCase("fail")) {
                msg = "支付失败";
            } else if (str.equalsIgnoreCase("cancel")) {
                msg = "用户取消了支付";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("支付结果通知");
            builder.setMessage(msg);
            builder.setInverseBackgroundForced(true);
            // builder.setCustomTitle();
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if ("支付成功".equals(msg)) {
                        String url = HttpConfig.IMAGEHOST + "/app/acpSgin";
                        // 获取到的银联返回码 验证返回码
                        Log.i("test", "银联返回码   " + sign);

                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("test", "自己返回结果 " + response);
                                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(response,
                                                BasicParameterBean.class);
                                        // 验证银联返回码成功之后 确定后台支付成功
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("OPT", "163");
                                        map.put("id", App.getUserInfo().getId());
                                        map.put("user_id",
                                                EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                                        map.put("gnum", number);
                                        new HttpClientGet(NewPersonalActivity.this, null, map, false,
                                                new HttpClientGet.CallBacks<String>() {

                                                    @Override
                                                    public void onSuccess(String result) {
                                                        System.out.println("李晨奇" + result);
                                                        BasicParameterBean bean2 = GsonUtil.getInstance()
                                                                .fromJson(result, BasicParameterBean.class);
                                                        Toast.makeText(NewPersonalActivity.this, bean2.getMsg(),
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(NewPersonalActivity.this,
                                                                PaySuccessActivity.class);// 支付成功界面
                                                        intent.putExtra("gnum", number);

                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    public void onFailure(MyError myError) {
                                                        super.onFailure(myError);
                                                    }

                                                    ;
                                                });

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("sgin", result);
                                params.put("gnum", number);
                                params.put("price", totalPrice);
                                params.put("user_id",
                                        EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));

                                return params;
                            }
                        };
                        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
                    } else if ("支付失败".equals(msg)) {
                        Toast.makeText(NewPersonalActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                        finish();
                    } else if ("用户取消了支付".equals(msg)) {
//                        finish();
                    }

                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    int startpay(Activity act, String tn, int serverIdentifier) {
        return 0;
    }

    private boolean verify(String msg, String sign64, String mode) {

        return true;

    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public class WeiXinPayBean {
        public List<NewPersonalActivity.alipayValue> getAlipayValue() {
            return alipayValue;
        }

        public void setAlipayValue(List<NewPersonalActivity.alipayValue> alipayValue) {
            this.alipayValue = alipayValue;
        }

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

        private List<alipayValue> alipayValue;
        private String error;
        private String msg;
    }

    public class alipayValue {
        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        private String appid;
        private String noncestr;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

    }
}
