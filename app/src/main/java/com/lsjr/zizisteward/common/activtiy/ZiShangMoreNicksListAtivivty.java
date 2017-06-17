package com.lsjr.zizisteward.common.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.OthersExampleActivity;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZiShangMoreNicksListAtivivty extends BaseActivity {
    SuperListView listview;
    private boolean login_state;
    String name;
    private int pageNum = 1;
    private List<ZiShangSearchNickAndLabelActivity.UserEnjoy> list_users = new ArrayList<ZiShangSearchNickAndLabelActivity.UserEnjoy>();
    private NickListAdapter adapter_users;
    private boolean isRefresh = true;
    private Intent intent;

    @Override
    public int getContainerView() {
        return R.layout.activity_shishi_search_more_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("名称列表");
        name = getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.text)).setText("人的名称");
        ((RelativeLayout) findViewById(R.id.iv_back)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        listview = (SuperListView) findViewById(R.id.listview);
        initListener();
    }

    private void initListener() {
        adapter_users = new NickListAdapter(ZiShangMoreNicksListAtivivty.this, list_users);
        listview.setAdapter(adapter_users);
        listview.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }

        });
        listview.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (login_state == true) {
                    intent = new Intent(getApplicationContext(), OthersExampleActivity.class);
                    intent.putExtra("publish_id", list_users.get(position - 1).getId());// 发布者id
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
                    intent.putExtra("personal", "zishangsearch");
                    startActivityForResult(intent, 1);
                }
            }
        });
        listview.refresh();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            adapter_users.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "91");
        map.put("keyword", name);
        map.put("currPage", String.valueOf(pageNum++));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("孜赏昵称" + result);
                ZiShangNickListBean bean = GsonUtil.getInstance().fromJson(result, ZiShangNickListBean.class);
                if (pageNum == 1) {
                    adapter_users = new NickListAdapter(ZiShangMoreNicksListAtivivty.this, list_users);
                    listview.setAdapter(adapter_users);
                    adapter_users.notifyDataSetChanged();
                } else {
                    list_users.addAll(bean.getZiziEnjoyNameList().getPage());
                    adapter_users.setList(list_users);
                }
                if (list_users.size() < bean.getZiziEnjoyNameList().getPageSize()) {
                    listview.setIsLoadFull(false);
                }
                listview.finishRefresh();
                listview.finishLoadMore();
            }
        });
    }

    @Override
    protected void onResume() {
        login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        super.onResume();
    }

    private class NickListAdapter extends BaseAdapter {
        Context context;
        private ViewHolder mHolder;
        List<ZiShangSearchNickAndLabelActivity.UserEnjoy> list_users;

        public NickListAdapter(Context context, List<ZiShangSearchNickAndLabelActivity.UserEnjoy> list_users) {
            this.context = context;
            this.list_users = list_users;
        }

        public void setList(List<ZiShangSearchNickAndLabelActivity.UserEnjoy> list_users) {
            this.list_users = list_users;
            notifyDataSetChanged();
        }

        public void add(ZiShangSearchNickAndLabelActivity.UserEnjoy page) {
            this.list_users.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ZiShangSearchNickAndLabelActivity.UserEnjoy page) {
            this.list_users.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ZiShangSearchNickAndLabelActivity.UserEnjoy> list_users) {
            this.list_users.addAll(list_users);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list_users.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list_users.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list_users == null ? 0 : list_users.size();
        }

        @Override
        public Object getItem(int position) {
            return list_users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_famous_search, parent, false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.mName.setText(list_users.get(position).getUser_name());
            if ("0".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_zero);
            }
            if ("1".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_one);
            }
            if ("2".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_two);
            }
            if ("3".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            }
            if ("4".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            }
            if ("5".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_five);
            }
            if ("6".equals(list_users.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_six);
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_users.get(position).getPhoto())
                    .into(mHolder.mUser_photo);
            return convertView;
        }

    }

    private class ViewHolder {
        private RoundImageView mUser_photo;
        private ImageView mIv_level;
        private TextView mName;

        public ViewHolder(View view) {
            mUser_photo = (RoundImageView) view.findViewById(R.id.user_photo);
            mIv_level = (ImageView) view.findViewById(R.id.iv_level);
            mName = (TextView) view.findViewById(R.id.name);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ZiShangNickListBean {
        private ziziEnjoyNameList ziziEnjoyNameList;

        public ziziEnjoyNameList getZiziEnjoyNameList() {
            return ziziEnjoyNameList;
        }

        public void setZiziEnjoyNameList(ziziEnjoyNameList ziziEnjoyNameList) {
            this.ziziEnjoyNameList = ziziEnjoyNameList;
        }
    }

    class ziziEnjoyNameList {
        private int currPage;
        private List<ZiShangSearchNickAndLabelActivity.UserEnjoy> page;
        private int pageSize;
        private String pageTitle;
        private int totalCount;
        private int totalPageCount;

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<ZiShangSearchNickAndLabelActivity.UserEnjoy> getPage() {
            return page;
        }

        public void setPage(List<ZiShangSearchNickAndLabelActivity.UserEnjoy> page) {
            this.page = page;
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
}
