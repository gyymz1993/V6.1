package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.alipay.PayResult;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.utils.WXPayUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//未支付订单详情
@SuppressLint("SimpleDateFormat")
public class OrderDetailActivity extends BaseActivity implements OnClickListener {
    private List<WeiFuKuanBean.WeiFuKuanInfo.WeiFuKuanDetail> list = new ArrayList<>();
    private String mIndentNo;
    private TextView mTv_total_price;
    private TextView mTv_name;
    private TextView mTv_phone;
    private TextView mTv_address;
    private TextView mIntendNO;
    private TextView mTime;
    private TextView mPay;
    private OrderListAdapter mAdapter;
    private MyListView mListview;
    private ImageView iv_click_weixin, iv_click_weixin_down, iv_click_zhifubao, iv_click_zhifubao_down,
            iv_click_yinlian, iv_click_yinlian_down;
    private RelativeLayout re_wei, re_zhifu, re_yinlian;
    //支付宝参数
    public static final String APPID = "2017050207078320";
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCc5bRHWn8/NEk8iAImVHGI+VKeyZoaYAyk7Poa+xXPiKXaDrZRafhVnhP/MAaYLUucQUl5vUHYryVNgCrZOT0X5v29NAv8rZnemX7wIj0dPawU8zGv9ZxWUfEXDQbzansuVrNKg+1dtwA4/RDlS5A7tiReyS1lGEZGGuANfjKqjdzC1ArgKa8koySKQWviIiit9sF6zn41ezZFdn7ae2cuqZmcKbuSA+gW2bIvg8eaATu3aUksW1EblJRRGt0pJrUPXYEmtcbDUnoHYkqZvysQCWz8N9z8TVF5KQ+406J1DIeef7Dnxp5d9tUqVBDTZDxdRX6YyBakjCvMSuGqBmBDAgMBAAECggEBAI0pTi30ig//pvTDGcwKpaurRl5+3Btm13l7jPrBdTono7CxE+/j+/5sH2m1d/lqp2CKjOTvFlMyIcm8ytBt4z2iIiEctrw8JPhV+hNrerjYR/8BX28E+Afh6ZvnqJ1Q7Esgzmb4k5OBlr2vUog6mCZhFhAByMnwMSnZ/EOni0ZOtRXZdZSBH2JlT+KOQpFopKZA49RoqR+5oOUHwkxMr36OomwbQPht+khCoHo8roy/6RDEBFyMMTmRvmvtiHzhFxOTx7r2zfWAQdcPrNwBr4elgMapHtm0dJqoDiInYCnXYycuOlBc9g0fnggXMjv13eg227rYJIrGH+6TPe4C6UECgYEA84OVTYHrazzFPbz0H9SSvMY0nSe4qgOQMHDDIzjti0Z8kubogM/Cdm7yPj7Nh2N3dL0MBsAqfFMyHD4dO7C2vhMxlSh12CgSZjjK40xTFUQlIwvC9kxmcQADeBKlCDF9+o0ThT33C79zARVp7Mj1nsFLkTIAF22Y+24pQC4m1ekCgYEApPEr5kZweO2Gd/O1F4m7/hEZ9JQNI5NiajuK2EDGXt8IJx6TVXPZrz7H00LrhyYrEdeXgHz244TwAZwd9yV865/bJzN8CJpR/DEEsQHiVfo0LEs9mXsKiaT6I00aQZ6uU9WN2upC289gixdo+c8bix9MdA3de2PZJQ8eBDCV7UsCgYEAjERBHhI1/uFUZAmRPTx/AYnSCKw2rIe86IorfQBvpAgH/b5QMtJ5myqxErWuQcxDpNS4NrM+RbrOZIJK6HUT7ky0BMz3hHkgkA0qoN74BInqMlO2C5VadMCjPujOcve/LzFQCzH0OaofnoItL51aDgYddxcsAlK5CiscS8HJ0PECgYBAUkBRU4TG07HNkz86h57FDDw38YhSKEaHsOKLRG9XTdhrEPRZrYzlVzErxHv+vzaqhY1yMMlCnnPN3OiemYLmi/c1iVFENZHlK+RtdpOh/alc4JaMBLxuQuS84XNsxYmr7aqdBR+/glZex2lLiaVvEmyJEWMenChw2D1XWu8MkwKBgQDAdgYMD5eJlL5dfhDBmESWidU9HDsQrow7I0vLdgWpMpYuRaNxQUGSnVtri7R+o5P3sLcd4RPmWvKWwb8vr3CuQr4pXJZ/PzxKRSqzOAo+oLWg6gr/g+PDm8NqHO9jmPY2P3g1pmwqRn4LnmhIf7dQ3k8jlXEIUDpXk1/+UUcE/w==";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

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
                        Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    @Override
    public int getContainerView() {
        return R.layout.activity_order_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("订单详情");
        mIndentNo = getIntent().getStringExtra("indentNo");
        mListview = (MyListView) findViewById(R.id.listview);
        mTv_total_price = (TextView) findViewById(R.id.tv_total_price);// 总价
        mTv_name = (TextView) findViewById(R.id.tv_name);// 姓名
        mTv_phone = (TextView) findViewById(R.id.tv_phone);// 电话
        mTv_address = (TextView) findViewById(R.id.tv_address);// 详细地址
        mIntendNO = (TextView) findViewById(R.id.intendNO);// 订单号
        mTime = (TextView) findViewById(R.id.time);// 订单时间
        mPay = (TextView) findViewById(R.id.pay);// 确认付款

        re_wei = (RelativeLayout) findViewById(R.id.re_wei);
        re_zhifu = (RelativeLayout) findViewById(R.id.re_zhifu);
        re_yinlian = (RelativeLayout) findViewById(R.id.re_yinlian);
        iv_click_weixin = (ImageView) findViewById(R.id.iv_click_weixin);
        iv_click_weixin_down = (ImageView) findViewById(R.id.iv_click_weixin_down);
        iv_click_zhifubao = (ImageView) findViewById(R.id.iv_click_zhifubao);
        iv_click_zhifubao_down = (ImageView) findViewById(R.id.iv_click_zhifubao_down);
        iv_click_yinlian = (ImageView) findViewById(R.id.iv_click_yinlian);
        iv_click_yinlian_down = (ImageView) findViewById(R.id.iv_click_yinlian_down);

        getData();
        initListener();
    }

    private void initListener() {
        re_wei.setOnClickListener(this);
        re_zhifu.setOnClickListener(this);
        re_yinlian.setOnClickListener(this);
    }

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "40");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("gnum", mIndentNo);
        new HttpClientGet(OrderDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("未支付订单详情" + result);
                final WeiFuKuanBean bean = GsonUtil.getInstance().fromJson(result, WeiFuKuanBean.class);
                list = bean.getContentData().getDelivery();
                mAdapter = new OrderListAdapter(OrderDetailActivity.this, list);
                mListview.setAdapter(mAdapter);
                mTv_total_price.setText("合计: ￥" + bean.getContentData().order_price);
                mTv_name.setText(list.get(0).getCname());
                mTv_phone.setText(list.get(0).getCphone());
                mTv_address.setText(list.get(0).getCaddr());
                mIntendNO.setText("订单编号:" + mIndentNo);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
                if (TextUtils.isEmpty(bean.getContentData().getOrder_time())) {
                    mTime.setText("下单时间:");
                } else {
                    String order_time = formatter.format(Long.valueOf(bean.getContentData().getOrder_time()));
                    mTime.setText("下单时间:" + order_time);
                }
                // 确认付款
                mPay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (iv_click_weixin_down.getVisibility() == View.GONE
                                && iv_click_zhifubao_down.getVisibility() == View.GONE
                                && iv_click_yinlian_down.getVisibility() == View.GONE) {
                            ToastUtils.show(getApplicationContext(), "请选择一种支付方式");
                        } else {
                            if (iv_click_weixin_down.getVisibility() == View.VISIBLE) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("OPT", "337");
                                map.put("tradeNo", mIndentNo);
                                map.put("amount", list.get(0).getService_price());
                                map.put("body", list.get(0).getDsname());
                                map.put("user_ip", WXPayUtils.getIPAddress(OrderDetailActivity.this));
                                new HttpClientGet(OrderDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

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
                            if (iv_click_yinlian_down.getVisibility() == View.VISIBLE) {
                                Intent intent = new Intent(getApplicationContext(), JARActivity.class);
                                intent.putExtra("total_price", bean.getContentData().order_price);
                                intent.putExtra("indentNo", mIndentNo);
                                startActivity(intent);
                            }
                            if (iv_click_zhifubao_down.getVisibility() == View.VISIBLE) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("OPT", "93");
                                map.put("tradeNo", mIndentNo);
                                map.put("amount", list.get(0).getService_price());
                                map.put("description", list.get(0).dsname);
                                map.put("title", list.get(0).dsname);
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
                                                    PayTask alipay = new PayTask(OrderDetailActivity.this);
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

                        }
                    }
                });
                // 点击商品到商品详情
                mListview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), HomeBrandDetail.class);
                        intent.putExtra("sid", bean.getContentData().getDelivery().get(position).getSid());// 商品id
                        startActivity(intent);
                    }
                });

            }

        });

    }

    private class OrderListAdapter extends BaseAdapter {

        private ViewHolder mHolder;
        private Context context;
        private List<WeiFuKuanBean.WeiFuKuanInfo.WeiFuKuanDetail> list;

        public OrderListAdapter(Context context,
                                List<WeiFuKuanBean.WeiFuKuanInfo.WeiFuKuanDetail> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);

            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getDsimg()).into(mHolder.mIv_pic);
            mHolder.mTv_brand.setText(list.get(position).getDsname());
            mHolder.mTv_price.setText("￥" + list.get(position).getService_price());
            mHolder.mTv_count.setText("x" + list.get(position).getDnumber());
            return convertView;
        }

    }

    public class ViewHolder {

        private ImageView mIv_pic;
        private TextView mTv_brand;
        private TextView mTv_price;
        private TextView mTv_count;

        public ViewHolder(View view) {
            mIv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            mTv_brand = (TextView) view.findViewById(R.id.tv_brand);
            mTv_price = (TextView) view.findViewById(R.id.tv_price);
            mTv_count = (TextView) view.findViewById(R.id.tv_count);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_wei:
                if (iv_click_weixin.getVisibility() == View.VISIBLE) {
                    iv_click_weixin.setVisibility(View.GONE);
                    iv_click_weixin_down.setVisibility(View.VISIBLE);
                    iv_click_yinlian.setVisibility(View.VISIBLE);
                    iv_click_yinlian_down.setVisibility(View.GONE);
                    iv_click_zhifubao.setVisibility(View.VISIBLE);
                    iv_click_zhifubao_down.setVisibility(View.GONE);
                }
                break;
            case R.id.re_zhifu:
                if (iv_click_zhifubao.getVisibility() == View.VISIBLE) {
                    iv_click_weixin.setVisibility(View.VISIBLE);
                    iv_click_weixin_down.setVisibility(View.GONE);
                    iv_click_yinlian.setVisibility(View.VISIBLE);
                    iv_click_yinlian_down.setVisibility(View.GONE);
                    iv_click_zhifubao.setVisibility(View.GONE);
                    iv_click_zhifubao_down.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.re_yinlian:
                if (iv_click_yinlian.getVisibility() == View.VISIBLE) {
                    iv_click_weixin.setVisibility(View.VISIBLE);
                    iv_click_weixin_down.setVisibility(View.GONE);
                    iv_click_yinlian.setVisibility(View.GONE);
                    iv_click_yinlian_down.setVisibility(View.VISIBLE);
                    iv_click_zhifubao.setVisibility(View.VISIBLE);
                    iv_click_zhifubao_down.setVisibility(View.GONE);
                }
                break;
        }
    }

    public class WeiFuKuanBean {
        private WeiFuKuanInfo contentData;
        private String error;
        private String msg;

        public WeiFuKuanInfo getContentData() {
            return contentData;
        }

        public void setContentData(WeiFuKuanInfo contentData) {
            this.contentData = contentData;
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

        private class WeiFuKuanInfo {
            private String assess_order;
            private String bask_order;
            private List<WeiFuKuanDetail> delivery;
            private String gid;
            private String gnum;
            private String gsname;
            private String order_price;
            private String order_time;
            private String pay_state;
            // private String pay_time;
            private String transflow;

            public String getAssess_order() {
                return assess_order;
            }

            public void setAssess_order(String assess_order) {
                this.assess_order = assess_order;
            }

            public String getBask_order() {
                return bask_order;
            }

            public void setBask_order(String bask_order) {
                this.bask_order = bask_order;
            }

            public List<WeiFuKuanDetail> getDelivery() {
                return delivery;
            }

            public void setDelivery(List<WeiFuKuanDetail> delivery) {
                this.delivery = delivery;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getGnum() {
                return gnum;
            }

            public void setGnum(String gnum) {
                this.gnum = gnum;
            }

            public String getGsname() {
                return gsname;
            }

            public void setGsname(String gsname) {
                this.gsname = gsname;
            }

            public String getOrder_price() {
                return order_price;
            }

            public void setOrder_price(String order_price) {
                this.order_price = order_price;
            }

            public String getOrder_time() {
                return order_time;
            }

            public void setOrder_time(String order_time) {
                this.order_time = order_time;
            }

            public String getPay_state() {
                return pay_state;
            }

            public void setPay_state(String pay_state) {
                this.pay_state = pay_state;
            }

            public String getTransflow() {
                return transflow;
            }

            public void setTransflow(String transflow) {
                this.transflow = transflow;
            }

            private class WeiFuKuanDetail {
                private String aid;
                private String caddr;
                private String clocation;
                private String cname;
                private String cphone;
                private String dcolor;
                private String dnumber;
                private String dsimg;
                private String dsize;
                private String dsname;
                private String entityId;
                private String gmid;
                private String id;
                private String kid;
                private String persistent;
                private String service_price;
                private String sid;// 商品id

                public String getSid() {
                    return sid;
                }

                public void setSid(String sid) {
                    this.sid = sid;
                }

                public String getAid() {
                    return aid;
                }

                public void setAid(String aid) {
                    this.aid = aid;
                }

                public String getCaddr() {
                    return caddr;
                }

                public void setCaddr(String caddr) {
                    this.caddr = caddr;
                }

                public String getClocation() {
                    return clocation;
                }

                public void setClocation(String clocation) {
                    this.clocation = clocation;
                }

                public String getCname() {
                    return cname;
                }

                public void setCname(String cname) {
                    this.cname = cname;
                }

                public String getCphone() {
                    return cphone;
                }

                public void setCphone(String cphone) {
                    this.cphone = cphone;
                }

                public String getDcolor() {
                    return dcolor;
                }

                public void setDcolor(String dcolor) {
                    this.dcolor = dcolor;
                }

                public String getDnumber() {
                    return dnumber;
                }

                public void setDnumber(String dnumber) {
                    this.dnumber = dnumber;
                }

                public String getDsimg() {
                    return dsimg;
                }

                public void setDsimg(String dsimg) {
                    this.dsimg = dsimg;
                }

                public String getDsize() {
                    return dsize;
                }

                public void setDsize(String dsize) {
                    this.dsize = dsize;
                }

                public String getDsname() {
                    return dsname;
                }

                public void setDsname(String dsname) {
                    this.dsname = dsname;
                }

                public String getEntityId() {
                    return entityId;
                }

                public void setEntityId(String entityId) {
                    this.entityId = entityId;
                }

                public String getGmid() {
                    return gmid;
                }

                public void setGmid(String gmid) {
                    this.gmid = gmid;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getKid() {
                    return kid;
                }

                public void setKid(String kid) {
                    this.kid = kid;
                }

                public String getPersistent() {
                    return persistent;
                }

                public void setPersistent(String persistent) {
                    this.persistent = persistent;
                }

                public String getService_price() {
                    return service_price;
                }

                public void setService_price(String service_price) {
                    this.service_price = service_price;
                }
            }
        }
    }

}
