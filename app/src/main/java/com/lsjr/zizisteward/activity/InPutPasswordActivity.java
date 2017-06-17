
package com.lsjr.zizisteward.activity;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.PaidBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.OnPasswordInputFinish;
import com.mybetterandroid.wheel.other.PassView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class InPutPasswordActivity extends Activity implements Runnable, Callback {
    private View black;
    private ProgressDialog dialog = null;
    private Context context;
    private int mGoodsIdx = 0;
    private final String mMode = "01";
    // private final String mMode = "47";
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
    private Handler mHandler = null;
    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
    private PassView passwordview;
    private String password;
    private String totalprice;
    private String name;
    private String phone;
    private String address;
    private String photo;
    private String color;
    private String size;
    private String count;
    private String title;
    private String indentNo;
    private String hms;
    private String danjia;
    private String fukuananniu;

    public InPutPasswordActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        context = this;
        mHandler = new Handler(this);
        passwordview = new PassView(this);

        setContentView(passwordview);

        Intent intent = getIntent();
        fukuananniu = intent.getStringExtra("fukuananniu");

        totalprice = intent.getStringExtra("totalprice");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        photo = intent.getStringExtra("photo");
        color = intent.getStringExtra("color");
        size = intent.getStringExtra("size");
        count = intent.getStringExtra("count");
        title = intent.getStringExtra("title");
        indentNo = intent.getStringExtra("indentNo");
        hms = intent.getStringExtra("hms");
        danjia = intent.getStringExtra("danjia");

        passwordview.setTag(0);
        passwordview.setOnFinishInput(new OnPasswordInputFinish() {// 这里是实现输入完后的逻辑

            @Override
            public void inputFinish() {
                password = passwordview.getStrPassword();
                Log.i("test", "交易密码是  " + password);
                // 首先验证交易密码 是否正确
                getCheckPassWord();

            }
        });
        passwordview.getCancelImageView().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        passwordview.getForgetTextView().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(InPutPasswordActivity.this,
                // PayPasswordActivity.class);
                // startActivity(intent);
            }
        });
        passwordview.getFinish().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String transflow;// 交易流水号

    private String sign;// 银联返回结果
    private String decode;

    protected void getCheckPassWord() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "197");
        map.put("mid", App.getUserInfo().getId());
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        System.out.println(indentNo + " " + totalprice + " " + password);
        map.put("deal_pwd", password);
        map.put("gnum", indentNo);
        map.put("order_price", totalprice);

        new HttpClientGet(InPutPasswordActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            private PaidBean bean;

            @Override
            public void onSuccess(String result) {
                System.out.println("输入完后的消息" + result);
                bean = GsonUtil.getInstance().fromJson(result, PaidBean.class);
//				transflow = bean.getTransflow();
                Log.i("test", "交易流水号" + transflow);

                // Toast.makeText(InPutPasswordActivity.this, bean.getMsg(),
                // Toast.LENGTH_SHORT).show();

                mGoodsIdx = (Integer) passwordview.getTag();
                dialog = ProgressDialog.show(context, "", "正在努力的获取tn中,请稍候...", true);
                new Thread(InPutPasswordActivity.this).start();

            }

            @Override
            public void onFailure(MyError myError) {
                System.out.println("输入错误的消息  " + myError);
                // 验证失败
                AlertDialog.Builder builder = new AlertDialog.Builder(InPutPasswordActivity.this);
                builder.setMessage("支付密码不正确,您还可以输入2次!");
                builder.setNegativeButton("忘记密码", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(InPutPasswordActivity.this, PayPasswordActivity.class);
//						startActivity(intent);
//						finish();
                    }
                });
                builder.setNeutralButton("重新输入", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TextView[] tvList = passwordview.tvList;
                        tvList[0].setText(null);
                        tvList[1].setText(null);
                        tvList[2].setText(null);
                        tvList[3].setText(null);
                        tvList[4].setText(null);
                        tvList[5].setText(null);
                        dialog.dismiss();
                    }
                });

                builder.create().show();

            }
        });
    }

    @Override
    public void run() {
        // String tn = null;
        // InputStream is;
        // try {
        //
        // String url = TN_URL_01;
        //
        // URL myURL = new URL(url);
        // URLConnection ucon = myURL.openConnection();
        // ucon.setConnectTimeout(120000);
        // is = ucon.getInputStream();
        // int i = -1;
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // while ((i = is.read()) != -1) {
        // baos.write(i);
        // }
        //
        // tn = baos.toString();
        // is.close();
        // baos.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        Message msg = mHandler.obtainMessage();
        msg.obj = transflow;
        mHandler.sendMessage(msg);
    }

    public abstract void doStartUnionPayPlugin(Activity activity, String tn, String mode);

    public boolean handleMessage(Message msg) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        String tn = "";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        String str = data.getExtras().getString("pay_result");
        // Log.i("test", "进来的结果 " + str);

        if (str.equalsIgnoreCase("success")) {

            if (data.hasExtra("result_data")) {
                result = data.getExtras().getString("result_data");
                // Log.i("test", "返回的银联结果 " + result);

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
                    // try {
                    //// decode = URLDecoder.decode(result, "UTF-8");
                    // } catch (UnsupportedEncodingException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                private BasicParameterBean bean;

                                @Override
                                public void onResponse(String response) {
                                    Log.i("test", "自己返回结果 " + response);
                                    bean = GsonUtil.getInstance().fromJson(response, BasicParameterBean.class);
                                    // // 银联支付成功后 进入到客户端的支付成功界面 携带交易流水号
                                    Intent intent = new Intent(InPutPasswordActivity.this, PaySuccessActivity.class);
                                    if ("123".equals(fukuananniu)) {
                                        intent.putExtra("indentNo", indentNo);
                                        intent.putExtra("fukuananniu", "456");
                                        intent.putExtra("transflow", transflow);

                                    } else {
                                        intent.putExtra("totalprice", totalprice);// 总价

                                        intent.putExtra("name", name);
                                        intent.putExtra("phone", phone);
                                        intent.putExtra("address", address);
                                        intent.putExtra("photo", photo);
                                        intent.putExtra("title", title);
                                        intent.putExtra("color", color);
                                        intent.putExtra("size", size);
                                        intent.putExtra("danjia", danjia);
                                        intent.putExtra("count", count);
                                        intent.putExtra("indentNo", indentNo);
                                        intent.putExtra("hms", hms);
                                        intent.putExtra("transflow", transflow);
                                    }

                                    startActivity(intent);
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("sgin", result);

                            return params;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(postRequest);
                } else {
                    // Toast.makeText(InPutPasswordActivity.this, "支付异常",
                    // Toast.LENGTH_SHORT).show();
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
