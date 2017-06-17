package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ServiceProjectDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.fragment.GoingFragment.GoingBean;
import com.lsjr.zizisteward.fragment.GoingFragment.GoingInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
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

public class WaitEvalFragment extends Fragment {
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
            view = inflater.inflate(R.layout.fragment_wait_eval, null);
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
        map.put("service_type", "4");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("待评价方案" + result);
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
            this.context = context;
            this.list = list;

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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_project_eval, null);
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

            Double price = Double.valueOf(list.get(position).getBalance())
                    + Double.valueOf(list.get(position).getDeposit());
            mHolder.mAdvance.setText("￥" + String.valueOf(price));// 价格

            return convertView;
        }

    }

    public class ViewHolder {
        private TextView mName;// 名称
        private TextView mTime;// 时间
        private TextView mAdvance;// 价格
        private TextView mEval;// 评价

        public ViewHolder(View view) {
            mName = (TextView) view.findViewById(R.id.name);
            mTime = (TextView) view.findViewById(R.id.time);
            mAdvance = (TextView) view.findViewById(R.id.advance);
            mEval = (TextView) view.findViewById(R.id.eval);
        }
    }
}
