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
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public abstract class ServicesProjectActivity extends BaseActivity implements Callback, Runnable {

    private WebView mWebview;
    private Context context;
    private Handler mHandler;
    private ProgressDialog dialog = null;
    private final String mMode = "01";

    @Override
    public int getContainerView() {
        return R.layout.activity_service_project;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("服务方案");
        context = this;
        mHandler = new Handler(this);
        mWebview = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWebview.getSettings();
        mWebview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        mWebview.addJavascriptInterface(new DemoJavaInterface(), "control");
        mWebview.loadUrl(HttpConfig.IMAGEHOST + getIntent().getStringExtra("push_link"));
        System.out.println("样本链接" + HttpConfig.IMAGEHOST + getIntent().getStringExtra("push_link"));

    }

    private String number;
    private String transflow;
    private String totalPrice;

    public class DemoJavaInterface {
        @JavascriptInterface
        public void setToken(String gnum, String time, String price) {
            number = gnum;
            transflow = time;
            totalPrice = price;

            dialog = ProgressDialog.show(context, "", "正在努力的获取tn中,请稍候...", true);
            new Thread(ServicesProjectActivity.this).start();
        }

    }

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
        msg.obj = transflow;
        mHandler.sendMessage(msg);
    }

    public abstract void doStartUnionPayPlugin(Activity activity, String tn, String mode);

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
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
                                    new HttpClientGet(ServicesProjectActivity.this, null, map, false,
                                            new HttpClientGet.CallBacks<String>() {

                                                @Override
                                                public void onSuccess(String result) {
                                                    BasicParameterBean bean2 = GsonUtil.getInstance().fromJson(result,
                                                            BasicParameterBean.class);
                                                    Toast.makeText(ServicesProjectActivity.this, bean2.getMsg(),
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ServicesProjectActivity.this,
                                                            PaySuccessActivity.class);// 支付成功界面
                                                    startActivity(intent);
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
                            params.put("price", totalPrice);
                            params.put("id", App.getUserInfo().getId());
                            params.put("user_id",
                                    EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));

                            return params;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(postRequest);
                } else {

                    finish();
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    int startpay(Activity act, String tn, int serverIdentifier) {
        return 0;
    }

    private boolean verify(String msg, String sign64, String mode) {

        return true;

    }
}
