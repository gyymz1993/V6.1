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
import com.lsjr.zizisteward.fragment.AllProjectFragment.ServiceTime;
import com.lsjr.zizisteward.fragment.AllProjectFragment.ToTime;
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
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoingFragment extends Fragment {
    private View view;
    private SuperListView mSlv_all;
    private TextView mTv_no_order;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private List<GoingInfo> list = new ArrayList<GoingInfo>();
    private ProjectAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_going, null);
            mSlv_all = (SuperListView) view.findViewById(R.id.slv_all);
            mTv_no_order = (TextView) view.findViewById(R.id.tv_no_order);
            mSlv_all.setVerticalScrollBarEnabled(false);
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
        map.put("service_type", "3");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("进行中方案" + result);
                GoingBean bean = GsonUtil.getInstance().fromJson(result, GoingBean.class);

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
        private List<GoingInfo> list;

        public ProjectAdapter(Context context, List<GoingInfo> list) {
            this.list = list;
            this.context = context;
        }

        public void setList(List<GoingInfo> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(GoingInfo page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(GoingInfo page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<GoingInfo> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_project_going, null);
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
            // TODO
            Double price = Double.valueOf(list.get(position).getDeposit())
                    + Double.valueOf(list.get(position).getBalance());
            mHolder.mAdvance.setText("￥" + String.valueOf(price));

            mHolder.mSure_project.setOnClickListener(new OnClickListener() {// 确认完成

                @Override
                public void onClick(View v) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("OPT", "281");
                    map.put("tspid", list.get(position).getId());
                    map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                    new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
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

            return convertView;
        }

    }

    public class ViewHolder {
        private TextView mName;// 名称
        private TextView mTime;// 时间
        private TextView mAdvance;// 价格
        private TextView mSure_project;// 确认完成

        public ViewHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mTime = (TextView) view.findViewById(R.id.time);
            mAdvance = (TextView) view.findViewById(R.id.advance);
            mSure_project = (TextView) view.findViewById(R.id.sure_project);
        }
    }

    public class GoingBean {
        private String error;
        private String msg;
        private ServiceTime serviceTime;
        private List<GoingInfo> service_plan_by;

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

        public List<GoingInfo> getService_plan_by() {
            return service_plan_by;
        }

        public void setService_plan_by(List<GoingInfo> service_plan_by) {
            this.service_plan_by = service_plan_by;
        }
    }

    public class GoingInfo {
        private AffirmTime affirm_time;
        private String balance;
        private String balance_transflow;
        private String content;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
}
