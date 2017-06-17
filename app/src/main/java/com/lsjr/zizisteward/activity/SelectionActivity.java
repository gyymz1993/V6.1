package com.lsjr.zizisteward.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectionActivity extends BaseActivity {

    private SuperListView mListview_recommend;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private List<ActivityDetail> list = new ArrayList<ActivityDetail>();
    private ListAdapter mAdapter;

    @Override
    public int getContainerView() {
        return R.layout.activity_zizi_recommend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("精选活动");
        mListview_recommend = (SuperListView) findViewById(R.id.listview_recommend);
        mListview_recommend.setVerticalScrollBarEnabled(false);
        init();

    }

    private void init() {
        mAdapter = new ListAdapter(SelectionActivity.this, list);
        mListview_recommend.setAdapter(mAdapter);
        mListview_recommend.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });
        mListview_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        mListview_recommend.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectionActivity.this, HomeBrandDetail.class);
                intent.putExtra("sid", list.get(position - 1).getId());
                startActivity(intent);

            }
        });

        mListview_recommend.refresh();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "72");
        map.put("currPage", String.valueOf(pageNum++));
        map.put("city_id", "420100");
        new HttpClientGet(SelectionActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("精选活动" + result);
                ActivityBean bean = GsonUtil.getInstance().fromJson(result, ActivityBean.class);
                if (pageNum == 1) {
                    list = bean.getActivity().getPage();
                    mAdapter = new ListAdapter(SelectionActivity.this, list);
                    mListview_recommend.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                } else {
                    list.addAll(bean.getActivity().getPage());
                    mAdapter.setList(list);
                }
                if (list.size() < bean.getActivity().getPageSize()) {
                    mListview_recommend.setIsLoadFull(false);
                }
                mListview_recommend.finishRefresh();
                mListview_recommend.finishLoadMore();

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private List<ActivityDetail> list;
        private ViewHolder mHolder;

        public ListAdapter(Context context, List<ActivityDetail> list) {
            this.context = context;
            this.list = list;

        }

        public void setList(List<ActivityDetail> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(ActivityDetail page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ActivityDetail page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ActivityDetail> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);

            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).centerCrop()
                    .animate(android.R.anim.slide_in_left).into(mHolder.mIv_recommend);
            mHolder.mTv_one.setText(list.get(position).getSname());
            mHolder.mTv_two.setText(list.get(position).getSkeyword());

            return convertView;
        }

        private class ViewHolder {
            private ImageView mIv_recommend;
            private TextView mTv_one;
            private TextView mTv_two;

            public ViewHolder(View v) {
                mIv_recommend = (ImageView) v.findViewById(R.id.iv_recommend);
                mTv_one = (TextView) v.findViewById(R.id.tv_one);
                mTv_two = (TextView) v.findViewById(R.id.tv_two);
            }
        }
    }

    private class ActivityBean {
        private String error;
        private String msg;
        private ActivityInfo activity;

        public ActivityInfo getActivity() {
            return activity;
        }

        public void setActivity(ActivityInfo activity) {
            this.activity = activity;
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

    }

    private class ActivityInfo {

        private int currPage;
        private List<ActivityDetail> page;
        private int pageSize;
        private String pageTitle;
        private int totalCount;
        private int totalPageCount;

        public List<ActivityDetail> getPage() {
            return page;
        }

        public void setPage(List<ActivityDetail> page) {
            this.page = page;
        }

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPageCount() {
            return totalPageCount;
        }

        public void setTotalPageCount(int totalPageCount) {
            this.totalPageCount = totalPageCount;
        }
    }

    private class ActivityDetail {
        private String entityId;
        private String id;
        private String skeyword;
        private String sname;
        private String spicfirst;
        private String sprice;

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkeyword() {
            return skeyword;
        }

        public void setSkeyword(String skeyword) {
            this.skeyword = skeyword;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSpicfirst() {
            return spicfirst;
        }

        public void setSpicfirst(String spicfirst) {
            this.spicfirst = spicfirst;
        }

        public String getSprice() {
            return sprice;
        }

        public void setSprice(String sprice) {
            this.sprice = sprice;
        }
    }

}
