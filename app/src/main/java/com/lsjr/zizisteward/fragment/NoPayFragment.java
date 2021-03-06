package com.lsjr.zizisteward.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.JARActivity;
import com.lsjr.zizisteward.activity.NewPersonalActivity;
import com.lsjr.zizisteward.activity.OrderDetailActivity;
import com.lsjr.zizisteward.alipay.PayResult;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.NoPayBean;
import com.lsjr.zizisteward.bean.NoPayBean.NoPayDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.utils.WXPayUtils;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoPayFragment extends Fragment {
    private View rootView;
    private SuperListView mSlv_all;
    private NoPayAdapter mAdapter;
    private boolean isRefresh = true;
    private int pageNum = 1;
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
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                        isRefresh = true;
                        getData();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_nopay, null);
            mSlv_all = (SuperListView) rootView.findViewById(R.id.slv_all);
            mTv_no_order = (TextView) rootView.findViewById(R.id.tv_no_order);
            mSlv_all.setVerticalScrollBarEnabled(false);
            initView();
        }
        return rootView;
    }

    private void initView() {
        mAdapter = new NoPayAdapter(getActivity(), list);
        mSlv_all.setAdapter(mAdapter);
        mSlv_all.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });
        mSlv_all.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        mSlv_all.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("indentNo", list.get(position - 1).getGnum());
                startActivity(intent);

            }
        });

        mSlv_all.refresh();
    }

    private List<NoPayDetail> list = new ArrayList<NoPayDetail>();
    private NoPayBean mBean;
    private TextView mTv_no_order;

    @Override
    public void onResume() {
        isRefresh = true;
        getData();
        super.onResume();
    }

    protected void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "41");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum++));
        map.put("pay_state", "0");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("未支付列表" + result);
                mBean = GsonUtil.getInstance().fromJson(result, NoPayBean.class);
                if (mBean.getShop_indent().getTotalCount() == 0) {
                    mSlv_all.setVisibility(View.GONE);
                    mTv_no_order.setVisibility(View.VISIBLE);
                } else {
                    mSlv_all.setVisibility(View.VISIBLE);
                    mTv_no_order.setVisibility(View.GONE);
                    if (null != mBean && "1".equals(mBean.getError())) {

                        if (1 != pageNum) {
                            list.addAll(mBean.getShop_indent().getPage());
                            mAdapter.setList(list);
                        } else {
                            list = mBean.getShop_indent().getPage();
                            mAdapter = new NoPayAdapter(getActivity(), list);
                            mSlv_all.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                    if (list.size() < mBean.getShop_indent().getPageSize()) {
                        mSlv_all.setIsLoadFull(false);
                    }

                    mSlv_all.finishRefresh();
                    mSlv_all.finishLoadMore();
                }

            }

        });
    }

    public class NoPayAdapter extends BaseAdapter {
        private Context context;
        private List<NoPayDetail> list;
        private ViewHolder mHolder;

        public NoPayAdapter(Context context, List<NoPayDetail> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<NoPayDetail> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(NoPayDetail page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(NoPayDetail page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<NoPayDetail> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.no_pay_fragment_item, parent, false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getGpic()).into(mHolder.mIv_pic);
            mHolder.mTv_brand.setText(list.get(position).getGsname());
            mHolder.mTv_number.setText("订单号: " + list.get(position).getGnum());
            mHolder.mTv_price.setText("￥" + list.get(position).getOrder_price());
            mHolder.mTv_price.setTextColor(Color.parseColor("#ffc52c"));
            int num = list.size();
            System.out.println("代付款订单数" + num);
            PreferencesUtils.putInt(getContext(), "num", num);
            mHolder.mTv_pay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getContext(), R.style.dialog);
                    dialog.setContentView(R.layout.dialog_pay_selector);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                    RelativeLayout re_wei = (RelativeLayout) dialog.findViewById(R.id.re_wei);
                    RelativeLayout re_zhifu = (RelativeLayout) dialog.findViewById(R.id.re_zhifu);
                    RelativeLayout re_yinlian = (RelativeLayout) dialog.findViewById(R.id.re_yinlian);
                    final ImageView iv_click_weixin = (ImageView) dialog.findViewById(R.id.iv_click_weixin);
                    final ImageView iv_click_weixin_down = (ImageView) dialog.findViewById(R.id.iv_click_weixin_down);
                    final ImageView iv_click_zhifubao = (ImageView) dialog.findViewById(R.id.iv_click_zhifubao);
                    final ImageView iv_click_zhifubao_down = (ImageView) dialog
                            .findViewById(R.id.iv_click_zhifubao_down);
                    final ImageView iv_click_yinlian = (ImageView) dialog.findViewById(R.id.iv_click_yinlian);
                    final ImageView iv_click_yinlian_down = (ImageView) dialog.findViewById(R.id.iv_click_yinlian_down);
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
                                ToastUtils.show(getContext(), "请选择一种支付方式");
                            } else {
                                dialog.dismiss();
                                if (iv_click_weixin_down.getVisibility() == View.VISIBLE) {//微信支付
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("OPT", "337");
                                    map.put("tradeNo", list.get(position).getGnum());
                                    map.put("amount", list.get(position).getOrder_price());
                                    map.put("body", list.get(position).getGsname());
                                    map.put("user_ip", WXPayUtils.getIPAddress(getContext()));
                                    new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

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
                                                ToastUtils.show(getContext(), "您还未安装微信APP，请先安装APP");
                                            }
                                        }
                                    });

                                }
                                if (iv_click_zhifubao_down.getVisibility() == View.VISIBLE) {//支付宝支付
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("OPT", "93");
                                    map.put("tradeNo", list.get(position).getGnum());
                                    map.put("amount", list.get(position).getOrder_price());
                                    map.put("description", list.get(position).getGsname());
                                    map.put("title", list.get(position).getGsname());
                                    new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

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
                                                        PayTask alipay = new PayTask(getActivity());
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
                                    Intent intent = new Intent(getContext(), JARActivity.class);
                                    intent.putExtra("total_price", list.get(position).getOrder_price());
                                    intent.putExtra("indentNo", list.get(position).getGnum());
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

                }
            });
            return convertView;
        }

    }

    public class ViewHolder {

        private ImageView mIv_pic;
        private TextView mTv_brand;
        private TextView mTv_number;
        private TextView mTv_price;
        private TextView mTv_pay;

        public ViewHolder(View view) {
            mIv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            mTv_brand = (TextView) view.findViewById(R.id.tv_brand);
            mTv_number = (TextView) view.findViewById(R.id.tv_number);
            mTv_price = (TextView) view.findViewById(R.id.tv_price);
            mTv_pay = (TextView) view.findViewById(R.id.tv_pay);
        }

    }
}
