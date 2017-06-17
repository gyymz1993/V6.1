package com.lsjr.zizisteward.common.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.lsjr.zizisteward.activity.PersonalShiJieStatisticsActivity;
import com.lsjr.zizisteward.activity.WorldSearchActivity.Users;
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

public class ShiShiSearchMoreListActivity extends BaseActivity {
    private String name;
    private SuperListView listview;
    private int pageNum = 1;
    private List<Users> list_users = new ArrayList<Users>();
    private FamousAdapter adapter_users;
    private boolean isRefresh = true;
    private boolean login_state;
    private int location = 0;
    private Intent intent;

    @Override
    public int getContainerView() {
        return R.layout.activity_shishi_search_more_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("名人列表");
        name = getIntent().getStringExtra("name");
        listview = (SuperListView) findViewById(R.id.listview);
        ((RelativeLayout) findViewById(R.id.iv_back)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        initListener();
    }

    @Override
    protected void onResume() {
        login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        super.onResume();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            adapter_users.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "89");
        map.put("keyword", name);
        map.put("currPage", String.valueOf(pageNum++));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("结果" + result);
                ShiListMoreBean bean = GsonUtil.getInstance().fromJson(result, ShiListMoreBean.class);
                if (pageNum == 1) {
                    adapter_users = new FamousAdapter(ShiShiSearchMoreListActivity.this, list_users);
                    listview.setAdapter(adapter_users);
                    adapter_users.notifyDataSetChanged();
                } else {
                    list_users.addAll(bean.getSightNameList().getPage());
                    adapter_users.setList(list_users);
                }
                if (list_users.size() < bean.getSightNameList().getPageSize()) {
                    listview.setIsLoadFull(false);
                }
                listview.finishRefresh();
                listview.finishLoadMore();
            }
        });

    }

    private void initListener() {
        adapter_users = new FamousAdapter(ShiShiSearchMoreListActivity.this, list_users);
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
                location = position - 1;
                if (login_state == true) {
                    intent = new Intent(getApplicationContext(), PersonalShiJieStatisticsActivity.class);
                    intent.putExtra("user_id", list_users.get(position - 1).getId());
                    intent.putExtra("shijie_statistics", "mingrenbang");
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
                    intent.putExtra("personal", "shishi_search_more_list");
                    startActivityForResult(intent, 1);
                }
            }
        });
        listview.refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 422) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    intent = new Intent(getApplicationContext(), PersonalShiJieStatisticsActivity.class);
                    intent.putExtra("user_id", list_users.get(location).getId());
                    intent.putExtra("shijie_statistics", "mingrenbang");
                    startActivity(intent);
                }
            }, 200);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class FamousAdapter extends BaseAdapter {
        Context context;
        private ViewHolder mHolder;
        List<Users> list_users;

        public FamousAdapter(Context context, List<Users> list_users) {
            this.context = context;
            this.list_users = list_users;
        }

        public void setList(List<Users> list_users) {
            this.list_users = list_users;
            notifyDataSetChanged();
        }

        public void add(Users page) {
            this.list_users.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(Users page) {
            this.list_users.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<Users> list_users) {
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
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class ShiListMoreBean {
        private SightNameList sightNameList;

        public SightNameList getSightNameList() {
            return sightNameList;
        }

        public void setSightNameList(SightNameList sightNameList) {
            this.sightNameList = sightNameList;
        }
    }

    private class SightNameList {
        private int currPage;
        private List<Users> page;
        private int pageSize;
        private int totalCount;
        private int totalPageCount;

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<Users> getPage() {
            return page;
        }

        public void setPage(List<Users> page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
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
