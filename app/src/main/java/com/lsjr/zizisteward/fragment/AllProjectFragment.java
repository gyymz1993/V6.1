package com.lsjr.zizisteward.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.lsjr.zizisteward.activity.NewPersonalActivity;
import com.lsjr.zizisteward.activity.ProjectJARActivity;
import com.lsjr.zizisteward.activity.ServiceProjectDetailActivity;
import com.lsjr.zizisteward.alipay.PayResult;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.fragment.WaitConfirmFragment.PushTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.AffirmTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.PayAffirmTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.PayServiceTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.StewardPriceTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.utils.WXPayUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProjectFragment extends Fragment {
    private View view;
    private SuperListView mSlv_all;
    private TextView mTv_no_order;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private List<ServicePlan> list = new ArrayList<ServicePlan>();
    private ProjectAdapter mAdapter;
    private long mCurrentTimeMillis;// 当前时间
    private long continue_time = 24 * 60 * 60 * 1000;// 持续时间
    //支付宝参数
    public static final String APPID = "2017050207078320";
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCc5bRHWn8/NEk8iAImVHGI+VKeyZoaYAyk7Poa+xXPiKXaDrZRafhVnhP/MAaYLUucQUl5vUHYryVNgCrZOT0X5v29NAv8rZnemX7wIj0dPawU8zGv9ZxWUfEXDQbzansuVrNKg+1dtwA4/RDlS5A7tiReyS1lGEZGGuANfjKqjdzC1ArgKa8koySKQWviIiit9sF6zn41ezZFdn7ae2cuqZmcKbuSA+gW2bIvg8eaATu3aUksW1EblJRRGt0pJrUPXYEmtcbDUnoHYkqZvysQCWz8N9z8TVF5KQ+406J1DIeef7Dnxp5d9tUqVBDTZDxdRX6YyBakjCvMSuGqBmBDAgMBAAECggEBAI0pTi30ig//pvTDGcwKpaurRl5+3Btm13l7jPrBdTono7CxE+/j+/5sH2m1d/lqp2CKjOTvFlMyIcm8ytBt4z2iIiEctrw8JPhV+hNrerjYR/8BX28E+Afh6ZvnqJ1Q7Esgzmb4k5OBlr2vUog6mCZhFhAByMnwMSnZ/EOni0ZOtRXZdZSBH2JlT+KOQpFopKZA49RoqR+5oOUHwkxMr36OomwbQPht+khCoHo8roy/6RDEBFyMMTmRvmvtiHzhFxOTx7r2zfWAQdcPrNwBr4elgMapHtm0dJqoDiInYCnXYycuOlBc9g0fnggXMjv13eg227rYJIrGH+6TPe4C6UECgYEA84OVTYHrazzFPbz0H9SSvMY0nSe4qgOQMHDDIzjti0Z8kubogM/Cdm7yPj7Nh2N3dL0MBsAqfFMyHD4dO7C2vhMxlSh12CgSZjjK40xTFUQlIwvC9kxmcQADeBKlCDF9+o0ThT33C79zARVp7Mj1nsFLkTIAF22Y+24pQC4m1ekCgYEApPEr5kZweO2Gd/O1F4m7/hEZ9JQNI5NiajuK2EDGXt8IJx6TVXPZrz7H00LrhyYrEdeXgHz244TwAZwd9yV865/bJzN8CJpR/DEEsQHiVfo0LEs9mXsKiaT6I00aQZ6uU9WN2upC289gixdo+c8bix9MdA3de2PZJQ8eBDCV7UsCgYEAjERBHhI1/uFUZAmRPTx/AYnSCKw2rIe86IorfQBvpAgH/b5QMtJ5myqxErWuQcxDpNS4NrM+RbrOZIJK6HUT7ky0BMz3hHkgkA0qoN74BInqMlO2C5VadMCjPujOcve/LzFQCzH0OaofnoItL51aDgYddxcsAlK5CiscS8HJ0PECgYBAUkBRU4TG07HNkz86h57FDDw38YhSKEaHsOKLRG9XTdhrEPRZrYzlVzErxHv+vzaqhY1yMMlCnnPN3OiemYLmi/c1iVFENZHlK+RtdpOh/alc4JaMBLxuQuS84XNsxYmr7aqdBR+/glZex2lLiaVvEmyJEWMenChw2D1XWu8MkwKBgQDAdgYMD5eJlL5dfhDBmESWidU9HDsQrow7I0vLdgWpMpYuRaNxQUGSnVtri7R+o5P3sLcd4RPmWvKWwb8vr3CuQr4pXJZ/PzxKRSqzOAo+oLWg6gr/g+PDm8NqHO9jmPY2P3g1pmwqRn4LnmhIf7dQ3k8jlXEIUDpXk1/+UUcE/w==";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    Dialog dialog;
    TextView cancel, tv_pay;
    RelativeLayout re_wei, re_yinlian, re_zhifu;
    ImageView iv_click_weixin, iv_click_weixin_down, iv_click_zhifubao, iv_click_zhifubao_down, iv_click_yinlian, iv_click_yinlian_down;
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
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        Toast.makeText(getActivity(), "取消支付", Toast.LENGTH_SHORT).show();
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
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_all, null);
            mSlv_all = (SuperListView) view.findViewById(R.id.slv_all);
            mTv_no_order = (TextView) view.findViewById(R.id.tv_no_order);
            mSlv_all.setVerticalScrollBarEnabled(false);
            mCurrentTimeMillis = System.currentTimeMillis();
            initView();
        }
        return view;
    }

    private void initDialog() {
        dialog = new Dialog(getContext(), R.style.dialog);
        dialog.setContentView(R.layout.dialog_pay_selector);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        cancel = (TextView) dialog.findViewById(R.id.cancel);
        re_wei = (RelativeLayout) dialog.findViewById(R.id.re_wei);
        re_zhifu = (RelativeLayout) dialog.findViewById(R.id.re_zhifu);
        re_yinlian = (RelativeLayout) dialog.findViewById(R.id.re_yinlian);
        iv_click_weixin = (ImageView) dialog.findViewById(R.id.iv_click_weixin);
        iv_click_weixin_down = (ImageView) dialog
                .findViewById(R.id.iv_click_weixin_down);
        iv_click_zhifubao = (ImageView) dialog.findViewById(R.id.iv_click_zhifubao);
        iv_click_zhifubao_down = (ImageView) dialog
                .findViewById(R.id.iv_click_zhifubao_down);
        iv_click_yinlian = (ImageView) dialog.findViewById(R.id.iv_click_yinlian);
        iv_click_yinlian_down = (ImageView) dialog
                .findViewById(R.id.iv_click_yinlian_down);
        tv_pay = (TextView) dialog.findViewById(R.id.tv_pay);
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
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void payMode(final int position, final String type) {
        initDialog();
        dialog.show();
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
                        map.put("OPT", "338");
                        map.put("tradeNo", list.get(position).getGnum());
                        map.put("amount", list.get(position).getDeposit());
                        map.put("body", list.get(position).getName());
                        map.put("user_ip", WXPayUtils.getIPAddress(getContext()));
                        map.put("transflow_type", type);
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
                        map.put("OPT", "94");
                        map.put("tradeNo", list.get(position).getGnum());
                        map.put("amount", list.get(position).getDeposit());
                        map.put("description", list.get(position).getName());
                        map.put("title", list.get(position).getName());
                        map.put("transflow_type", type);
                        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                System.out.println("支付宝" + result);
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
                        Intent intent = new Intent(getContext(), ProjectJARActivity.class);
                        intent.putExtra("total_price", list.get(position).getDeposit());
                        intent.putExtra("intentNo", list.get(position).getGnum());// 服务号
                        intent.putExtra("tspid", list.get(position).getId());// 服务id
                        intent.putExtra("transflow_type", type);
                        startActivityForResult(intent, 3);
                    }
                }
            }
        });
    }

    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60; // 取整
            second = second % 60; // 取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + ":" + minute + ":" + second;
        return strtime;

    }

    private void initView() {
        mAdapter = new ProjectAdapter(getContext(), list);
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
                if ("会员购买".equals(list.get(position - 1).getContent())) {
                    ToastUtils.show(getContext(), "该订单没有详情,请点击其他哦...");
                } else {
                    Intent intent = new Intent(getContext(), ServiceProjectDetailActivity.class);
                    intent.putExtra("tspid", list.get(position - 1).getId());
                    intent.putExtra("type", "");
                    startActivity(intent);
                }

            }
        });
        mSlv_all.refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 && resultCode == 33) {
            isRefresh = true;
            getData();
        }
        if (requestCode == 4 && resultCode == 33) {
            isRefresh = true;
            getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        isRefresh = true;
        getData();
        super.onResume();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "274");
        map.put("currPage", String.valueOf(pageNum++));
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("全部方案" + result);
                ProjectBean bean = GsonUtil.getInstance().fromJson(result, ProjectBean.class);

                if (null != bean && "1".equals(bean.getError())) {

                    if (1 != pageNum) {
                        list.addAll(bean.getService_plan_by());
                        mAdapter.setList(list);
                    } else {

                        if (bean.getService_plan_by().size() == 0) {
                            mTv_no_order.setVisibility(View.VISIBLE);
                            mSlv_all.setVisibility(View.GONE);
                        } else {
                            mTv_no_order.setVisibility(View.GONE);
                            mSlv_all.setVisibility(View.VISIBLE);

                            list = bean.getService_plan_by();
                            mAdapter = new ProjectAdapter(getActivity(), list);
                            mSlv_all.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        }

                    }

                    if (list.size() < bean.getService_plan_by().size()) {
                        mSlv_all.setIsLoadFull(false);
                    }

                    mSlv_all.finishRefresh();
                    mSlv_all.finishLoadMore();

                }


            }

        });

    }

    public class ProjectAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder mHolder;
        private List<ServicePlan> list;

        public ProjectAdapter(Context context, List<ServicePlan> list) {
            this.list = list;
            this.context = context;

        }

        public void setList(List<ServicePlan> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(ServicePlan page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ServicePlan page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ServicePlan> list) {
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
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_project_all, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);

            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.mName.setText(list.get(position).getName());// 名称
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String from_time = formatter.format(Long.valueOf(list.get(position).getFrom_time().getTime()));
            String to_time = formatter.format(Long.valueOf(list.get(position).getTo_time().getTime()));
            mHolder.mTime.setText(from_time + "至" + to_time);// 起始时间

            long push_time = Long.valueOf(list.get(position).getPush_time().getTime());
            long ding_time = Long.valueOf(list.get(position).getAffirm_time().getTime());
            long wei_time = Long.valueOf(list.get(position).getSteward_price_time().getTime());
            long push_time2 = (push_time + continue_time - mCurrentTimeMillis) / 1000;
            long ding_time2 = (ding_time + continue_time - mCurrentTimeMillis) / 1000;
            long wei_time2 = (wei_time + continue_time - mCurrentTimeMillis) / 1000;

            if ("2".equals(list.get(position).getIs_push())) {// 待确认
                mHolder.mSure.setText("待确认");
                mHolder.mAdvance.setText("预付定金   " + "￥" + list.get(position).getDeposit());
                mHolder.mTv_hour.setVisibility(View.VISIBLE);
                mHolder.mTv_minute.setVisibility(View.VISIBLE);
                mHolder.mTv_second.setVisibility(View.VISIBLE);
                mHolder.mSure_project.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setVisibility(View.GONE);
                mHolder.mSure_project.setText("确认方案");
                mHolder.mSure_project.setEnabled(true);
                mHolder.mSure_project.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "278");
                        map.put("tspid", list.get(position).getId());
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
                                        BasicParameterBean.class);
                                ToastUtils.show(getContext(), bean.getMsg());
                                getData();

                            }

                            @Override
                            public void onFailure(MyError myError) {
                                super.onFailure(myError);
                            }
                        });
                    }
                });

                String formatLongToTimeStr = formatLongToTimeStr(push_time2);
                String[] split = formatLongToTimeStr.split(":");
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        mHolder.mTv_hour.setText("剩余时间: " + split[0] + "小时");
                    }
                    if (i == 1) {
                        mHolder.mTv_minute.setText(split[1] + "分钟");
                    }
                    if (i == 2) {
                        mHolder.mTv_second.setText(split[2] + "秒");
                    }

                }

            }
            if ("3".equals(list.get(position).getIs_push())) {// 待确认超时
                mHolder.mSure.setText("确认超时");
                mHolder.mAdvance.setText("预付定金   " + "￥" + list.get(position).getDeposit());
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);
                mHolder.mSure_project.setVisibility(View.GONE);
                mHolder.mSure_project_down.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setEnabled(false);
                mHolder.mSure_project_down.setText("确认方案");

            }
            if ("4".equals(list.get(position).getIs_push())) {// 待付款 支付定金
                mHolder.mSure.setText("待付款");
                mHolder.mAdvance.setText("预付定金   " + "￥" + list.get(position).getDeposit());
                mHolder.mTv_hour.setVisibility(View.VISIBLE);
                mHolder.mTv_minute.setVisibility(View.VISIBLE);
                mHolder.mTv_second.setVisibility(View.VISIBLE);
                mHolder.mSure_project.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setVisibility(View.GONE);
                mHolder.mSure_project.setText("支付定金");
                mHolder.mSure_project.setEnabled(true);
                mHolder.mSure_project.setOnClickListener(new OnClickListener() {// 支付定金

                    @Override
                    public void onClick(View v) {
                        payMode(position, "1");
                    }
                });

                String formatLongToTimeStr = formatLongToTimeStr(ding_time2);
                String[] split = formatLongToTimeStr.split(":");
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        mHolder.mTv_hour.setText("剩余时间: " + split[0] + "小时");
                    }
                    if (i == 1) {
                        mHolder.mTv_minute.setText(split[1] + "分钟");
                    }
                    if (i == 2) {
                        mHolder.mTv_second.setText(split[2] + "秒");
                    }

                }

            }

            if ("5".equals(list.get(position).getIs_push())) {// 待付款 支付定金超时
                mHolder.mSure.setText("付款超时");
                mHolder.mAdvance.setText("预付定金   " + "￥" + list.get(position).getDeposit());
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);
                mHolder.mSure_project.setVisibility(View.GONE);
                mHolder.mSure_project_down.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setText("支付定金");
                mHolder.mSure_project_down.setEnabled(false);

            }
            if ("6".equals(list.get(position).getIs_push()) || "7".equals(list.get(position).getIs_push())
                    || "8".equals(list.get(position).getIs_push())) {// 待付款
                // 方案筹备中
                mHolder.mSure.setText("待付款");
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);
                mHolder.mAdvance.setText("方案筹备中...");
                mHolder.mSure_project.setVisibility(View.GONE);
                mHolder.mSure_project_down.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setText("支付尾款");
                mHolder.mSure_project_down.setEnabled(false);

            }
            if ("9".equals(list.get(position).getIs_push())) {// 待付款 支付尾款
                mHolder.mSure.setText("待付款");
                mHolder.mAdvance.setText("尾款   " + "￥" + list.get(position).getBalance());
                mHolder.mTv_hour.setVisibility(View.VISIBLE);
                mHolder.mTv_minute.setVisibility(View.VISIBLE);
                mHolder.mTv_second.setVisibility(View.VISIBLE);
                mHolder.mSure_project.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setVisibility(View.GONE);
                mHolder.mSure_project.setText("支付尾款");
                mHolder.mSure_project.setEnabled(true);
                mHolder.mSure_project.setOnClickListener(new OnClickListener() {// 支付尾款

                    @Override
                    public void onClick(View v) {
                        payMode(position, "2");
                    }
                });

                String formatLongToTimeStr = formatLongToTimeStr(wei_time2);
                String[] split = formatLongToTimeStr.split(":");
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        mHolder.mTv_hour.setText("剩余时间: " + split[0] + "小时");
                    }
                    if (i == 1) {
                        mHolder.mTv_minute.setText(split[1] + "分钟");
                    }
                    if (i == 2) {
                        mHolder.mTv_second.setText(split[2] + "秒");
                    }

                }

            }
            if ("10".equals(list.get(position).getIs_push())) {// 待付款 支付尾款超时
                mHolder.mSure.setText("付款超时");
                mHolder.mAdvance.setText("尾款   " + "￥" + list.get(position).getBalance());
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);
                mHolder.mSure_project.setVisibility(View.GONE);
                mHolder.mSure_project_down.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setText("支付尾款");
                mHolder.mSure_project_down.setEnabled(false);

            }
            if ("11".equals(list.get(position).getIs_push())) {// 进行中
                mHolder.mSure.setText("进行中");
                Double price = Double.valueOf(list.get(position).getDeposit())
                        + Double.valueOf(list.get(position).getBalance());
                mHolder.mAdvance.setText("￥" + String.valueOf(price));
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);
                mHolder.mSure_project.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setVisibility(View.GONE);
                mHolder.mSure_project.setText("确认完成");
                mHolder.mSure_project.setEnabled(true);
                mHolder.mSure_project.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OPT", "281");
                        map.put("tspid", list.get(position).getId());
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
                                        BasicParameterBean.class);
                                ToastUtils.show(getContext(), bean.getMsg());
                                getData();
                            }

                            @Override
                            public void onFailure(MyError myError) {
                                super.onFailure(myError);
                            }
                        });
                    }
                });

            }
            if ("12".equals(list.get(position).getIs_push())) {// 已完成
                mHolder.mSure.setText("已完成");
                Double price = Double.valueOf(list.get(position).getDeposit())
                        + Double.valueOf(list.get(position).getBalance());
                mHolder.mAdvance.setText("￥" + String.valueOf(price));
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);
                mHolder.mSure_project.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setVisibility(View.GONE);
                mHolder.mSure_project.setText("已完成");
                mHolder.mSure_project.setEnabled(true);

            }
            return convertView;
        }

    }

    public class ViewHolder {
        private TextView mName;// 名称
        private TextView mSure;// 状态
        private TextView mTime;// 时间
        private TextView mAdvance;// 价格
        private TextView mSure_project;// 可点击
        private TextView mSure_project_down;// 不可点击
        private TextView mTv_hour;
        private TextView mTv_minute;
        private TextView mTv_second;

        public ViewHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mSure = (TextView) view.findViewById(R.id.sure);
            mTime = (TextView) view.findViewById(R.id.time);
            mAdvance = (TextView) view.findViewById(R.id.advance);
            mSure_project = (TextView) view.findViewById(R.id.sure_project);
            mSure_project_down = (TextView) view.findViewById(R.id.sure_project_down);
            mTv_hour = (TextView) view.findViewById(R.id.tv_hour);
            mTv_minute = (TextView) view.findViewById(R.id.tv_minute);
            mTv_second = (TextView) view.findViewById(R.id.tv_second);

        }
    }

    public class ProjectBean {
        private String error;
        private String msg;
        private ServiceTime serviceTime;
        private List<ServicePlan> service_plan_by;

        public List<ServicePlan> getService_plan_by() {
            return service_plan_by;
        }

        public void setService_plan_by(List<ServicePlan> service_plan_by) {
            this.service_plan_by = service_plan_by;
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

        public ServiceTime getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(ServiceTime serviceTime) {
            this.serviceTime = serviceTime;
        }

    }

    public class ServiceTime {
        private String service_confirmed_time;
        private String service_deposit_time;
        private String service_final_time;

        public String getService_confirmed_time() {
            return service_confirmed_time;
        }

        public void setService_confirmed_time(String service_confirmed_time) {
            this.service_confirmed_time = service_confirmed_time;
        }

        public String getService_deposit_time() {
            return service_deposit_time;
        }

        public void setService_deposit_time(String service_deposit_time) {
            this.service_deposit_time = service_deposit_time;
        }

        public String getService_final_time() {
            return service_final_time;
        }

        public void setService_final_time(String service_final_time) {
            this.service_final_time = service_final_time;
        }
    }

    public class ServicePlan {
        private AffirmTime affirm_time;
        private String balance;
        private String balance_;
        private String content;
        private String deposit;
        private String deposit_;
        private String entityId;
        private String floor_price;
        private FromTime from_time;
        private String gnum;
        private String id;
        private String is_edit;
        private String is_push;
        private String is_show;
        private String name;
        private PayAffirmTime pay_affirm_time;
        private PayServiceTime pay_service_time;
        private String persistent;
        private PushTime push_time;
        private StewardPriceTime steward_price_time;
        private String stname;
        private ToTime to_time;
        private String top_price;
        private String tpid;
        private String type_img;
        private String type_state;
        private String user_id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public AffirmTime getAffirm_time() {
            return affirm_time;
        }

        public void setAffirm_time(AffirmTime affirm_time) {
            this.affirm_time = affirm_time;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getBalance_() {
            return balance_;
        }

        public void setBalance_(String balance_) {
            this.balance_ = balance_;
        }

        public String getDeposit() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }

        public String getDeposit_() {
            return deposit_;
        }

        public void setDeposit_(String deposit_) {
            this.deposit_ = deposit_;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getFloor_price() {
            return floor_price;
        }

        public void setFloor_price(String floor_price) {
            this.floor_price = floor_price;
        }

        public FromTime getFrom_time() {
            return from_time;
        }

        public void setFrom_time(FromTime from_time) {
            this.from_time = from_time;
        }

        public String getGnum() {
            return gnum;
        }

        public void setGnum(String gnum) {
            this.gnum = gnum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_edit() {
            return is_edit;
        }

        public void setIs_edit(String is_edit) {
            this.is_edit = is_edit;
        }

        public String getIs_push() {
            return is_push;
        }

        public void setIs_push(String is_push) {
            this.is_push = is_push;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PayAffirmTime getPay_affirm_time() {
            return pay_affirm_time;
        }

        public void setPay_affirm_time(PayAffirmTime pay_affirm_time) {
            this.pay_affirm_time = pay_affirm_time;
        }

        public PayServiceTime getPay_service_time() {
            return pay_service_time;
        }

        public void setPay_service_time(PayServiceTime pay_service_time) {
            this.pay_service_time = pay_service_time;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public PushTime getPush_time() {
            return push_time;
        }

        public void setPush_time(PushTime push_time) {
            this.push_time = push_time;
        }

        public StewardPriceTime getSteward_price_time() {
            return steward_price_time;
        }

        public void setSteward_price_time(StewardPriceTime steward_price_time) {
            this.steward_price_time = steward_price_time;
        }

        public String getStname() {
            return stname;
        }

        public void setStname(String stname) {
            this.stname = stname;
        }

        public ToTime getTo_time() {
            return to_time;
        }

        public void setTo_time(ToTime to_time) {
            this.to_time = to_time;
        }

        public String getTop_price() {
            return top_price;
        }

        public void setTop_price(String top_price) {
            this.top_price = top_price;
        }

        public String getTpid() {
            return tpid;
        }

        public void setTpid(String tpid) {
            this.tpid = tpid;
        }

        public String getType_img() {
            return type_img;
        }

        public void setType_img(String type_img) {
            this.type_img = type_img;
        }

        public String getType_state() {
            return type_state;
        }

        public void setType_state(String type_state) {
            this.type_state = type_state;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    public class FromTime {
        private String date;
        private String day;
        private String hours;
        private String minutes;
        private String month;
        private String nanos;
        private String seconds;
        private String time;
        private String timezoneOffset;
        private String year;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getNanos() {
            return nanos;
        }

        public void setNanos(String nanos) {
            this.nanos = nanos;
        }

        public String getSeconds() {
            return seconds;
        }

        public void setSeconds(String seconds) {
            this.seconds = seconds;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(String timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public class ToTime {
        private String date;
        private String day;
        private String hours;
        private String minutes;
        private String month;
        private String nanos;
        private String seconds;
        private String time;
        private String timezoneOffset;
        private String year;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getNanos() {
            return nanos;
        }

        public void setNanos(String nanos) {
            this.nanos = nanos;
        }

        public String getSeconds() {
            return seconds;
        }

        public void setSeconds(String seconds) {
            this.seconds = seconds;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(String timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
