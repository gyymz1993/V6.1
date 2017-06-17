package com.lsjr.zizisteward.common.activtiy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class ReserveCateActivity extends BaseActivity {
    private SuperListView slv_travel;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private List<Catelist> list = new ArrayList<Catelist>();
    private FoodAdapter mAdapter;

    @Override
    public int getContainerView() {
        return R.layout.activity_reserve_travel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("预定—美食");
        slv_travel = (SuperListView) findViewById(R.id.slv_travel);
        initLayout();
    }

    private void initLayout() {
        mAdapter = new FoodAdapter(ReserveCateActivity.this, list);
        slv_travel.setAdapter(mAdapter);
        slv_travel.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();

            }
        });
        slv_travel.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        slv_travel.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ServiceProjectWebViewActivity.class)
                        .putExtra("id", list.get(position - 1).getId()).putExtra("tag", "3"));

            }
        });
        slv_travel.refresh();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "417");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum++));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("美食" + result);
                FoodBean bean = GsonUtil.getInstance().fromJson(result, FoodBean.class);
                if (pageNum == 1) {
                    list = bean.Catelist;
                    mAdapter = new FoodAdapter(ReserveCateActivity.this, list);
                    slv_travel.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                } else {
                    list.addAll(bean.getCatelist());
                    mAdapter.setList(list);
                }
                if (list.size() < bean.getCatelist().size()) {
                    slv_travel.setIsLoadFull(false);
                }
                slv_travel.finishRefresh();
                slv_travel.finishLoadMore();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    @SuppressWarnings("unused")
    private class FoodAdapter extends BaseAdapter {

        private ViewHolder mHolder;
        private List<Catelist> list;
        private Context context;

        public FoodAdapter(Context context, List<Catelist> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<Catelist> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(Catelist page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(Catelist page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<Catelist> list) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_my_reserve, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.tv_name.setText(list.get(position).getSname());
            mHolder.tv_content.setVisibility(View.GONE);
            if ("0".equals(list.get(position).getIs_finish())) {
                mHolder.formulate.setVisibility(View.VISIBLE);
                mHolder.have.setVisibility(View.GONE);
            } else {
                mHolder.formulate.setVisibility(View.GONE);
                mHolder.have.setVisibility(View.VISIBLE);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
            String user = formatter.format(Long.valueOf(list.get(position).getUse_Time().getTime()));
            mHolder.time.setText(user);
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_name, tv_content, time, have, formulate;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_content = (TextView) view.findViewById(R.id.tv_content);
                time = (TextView) view.findViewById(R.id.time);
                have = (TextView) view.findViewById(R.id.have);
                formulate = (TextView) view.findViewById(R.id.formulate);
            }
        }
    }

    private class FoodBean {
        private List<Catelist> Catelist;

        public List<Catelist> getCatelist() {
            return Catelist;
        }

        public void setCatelist(List<Catelist> catelist) {
            Catelist = catelist;
        }
    }

    public class Catelist {
        private String id;
        private String is_finish;
        private String sname;
        private ShareTime use_Time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_finish() {
            return is_finish;
        }

        public void setIs_finish(String is_finish) {
            this.is_finish = is_finish;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public ShareTime getUse_Time() {
            return use_Time;
        }

        public void setUse_Time(ShareTime use_Time) {
            this.use_Time = use_Time;
        }

    }
}
