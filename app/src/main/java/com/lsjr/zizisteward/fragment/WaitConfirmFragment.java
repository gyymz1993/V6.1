package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ServiceProjectDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.fragment.AllProjectFragment.FromTime;
import com.lsjr.zizisteward.fragment.AllProjectFragment.ToTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.AffirmTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.PayAffirmTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.PayServiceTime;
import com.lsjr.zizisteward.fragment.WaitPayFragment.StewardPriceTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//待确认方案
public class WaitConfirmFragment extends Fragment {
    private View view;
    private SuperListView mSlv_all;
    private TextView mTv_no_order;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private List<WaitSureList> list = new ArrayList<>();
    private ProjectAdapter mAdapter;
    private long mCurrentTimeMillis;// 当前时间
    private long continue_time = 24 * 60 * 60 * 1000;// 持续时间

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wait_confirm, null);
            mSlv_all = (SuperListView) view.findViewById(R.id.slv_all);
            mTv_no_order = (TextView) view.findViewById(R.id.tv_no_order);
            mSlv_all.setVerticalScrollBarEnabled(false);

            mCurrentTimeMillis = System.currentTimeMillis();

            initView();

        }
        return view;
    }


    @Override
    public void onResume() {
        isRefresh = true;
        getData();
        super.onResume();
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

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "282");
        map.put("currPage", String.valueOf(pageNum++));
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("service_type", "1");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("待确认方案" + result);
                WaitSure bean = GsonUtil.getInstance().fromJson(result, WaitSure.class);

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
        private List<WaitSureList> list;

        public ProjectAdapter(Context context, List<WaitSureList> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<WaitSureList> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(WaitSureList page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(WaitSureList page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<WaitSureList> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_wiat_confirm, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);

            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.mName.setText(list.get(position).getName());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String from_time = formatter.format(Long.valueOf(list.get(position).getFrom_time().getTime()));
            String to_time = formatter.format(Long.valueOf(list.get(position).getTo_time().getTime()));
            mHolder.mTime.setText(from_time + "至" + to_time);

            mHolder.mAdvance.setText("预付定金   " + "￥" + list.get(position).getDeposit());

            String push_time = list.get(position).getPush_time().getTime();
            long push_time2 = Long.valueOf(push_time);
            long push_time3 = (push_time2 + continue_time - mCurrentTimeMillis) / 1000;// 确认倒计时时间

            if ("2".equals(list.get(position).getIs_push())) {
                mHolder.mSure.setText("待确认");
                mHolder.mTv_hour.setVisibility(View.VISIBLE);
                mHolder.mTv_minute.setVisibility(View.VISIBLE);
                mHolder.mTv_second.setVisibility(View.VISIBLE);

                mHolder.mSure_project.setVisibility(View.VISIBLE);
                mHolder.mSure_project.setEnabled(true);
                mHolder.mSure_project_down.setVisibility(View.GONE);

                // 用户确认方案
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

                String formatLongToTimeStr = formatLongToTimeStr(push_time3);
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
            if ("3".equals(list.get(position).getIs_push())) {
                mHolder.mSure.setText("确认超时");
                mHolder.mTv_hour.setVisibility(View.GONE);
                mHolder.mTv_minute.setVisibility(View.GONE);
                mHolder.mTv_second.setVisibility(View.GONE);

                mHolder.mSure_project.setVisibility(View.GONE);
                mHolder.mSure_project_down.setVisibility(View.VISIBLE);
                mHolder.mSure_project_down.setEnabled(false);

            }
            return convertView;
        }

    }

    public class ViewHolder {
        private TextView mName;
        private TextView mTime;
        private TextView mAdvance;
        private TextView mSure_project;
        private TextView mSure_project_down;
        private TextView mSure;
        private TextView mTv_hour;
        private TextView mTv_minute;
        private TextView mTv_second;

        public ViewHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mTime = (TextView) view.findViewById(R.id.time);
            mAdvance = (TextView) view.findViewById(R.id.advance);
            mSure_project = (TextView) view.findViewById(R.id.sure_project);
            mSure_project_down = (TextView) view.findViewById(R.id.sure_project_down);
            mSure = (TextView) view.findViewById(R.id.sure);
            mTv_hour = (TextView) view.findViewById(R.id.tv_hour);
            mTv_minute = (TextView) view.findViewById(R.id.tv_minute);
            mTv_second = (TextView) view.findViewById(R.id.tv_second);

        }
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

    public class WaitSure {
        private String error;
        private String msg;
        private WaitSureTime serviceTime;
        private List<WaitSureList> service_plan_by;

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

        public WaitSureTime getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(WaitSureTime serviceTime) {
            this.serviceTime = serviceTime;
        }

        public List<WaitSureList> getService_plan_by() {
            return service_plan_by;
        }

        public void setService_plan_by(List<WaitSureList> service_plan_by) {
            this.service_plan_by = service_plan_by;
        }
    }

    public class WaitSureTime {
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

    public class WaitSureList {
        private String content;
        private AffirmTime affirm_time;
        private String balance;
        private String balance_transflow;
        private String deposit;
        private String deposit_transflow;
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

        public String getBalance_transflow() {
            return balance_transflow;
        }

        public void setBalance_transflow(String balance_transflow) {
            this.balance_transflow = balance_transflow;
        }

        public String getDeposit() {
            return deposit;
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }

        public String getDeposit_transflow() {
            return deposit_transflow;
        }

        public void setDeposit_transflow(String deposit_transflow) {
            this.deposit_transflow = deposit_transflow;
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

    public class PushTime {
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
