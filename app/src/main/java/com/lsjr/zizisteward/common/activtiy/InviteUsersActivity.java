package com.lsjr.zizisteward.common.activtiy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.InviteUsersBean;
import com.lsjr.zizisteward.bean.InviteUsersBean.Friends.Page;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;
import com.lsjr.zizisteward.ly.activity.InviteUsersDetailsActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteUsersActivity extends Activity {

    private MyListView mlv;
    private LinearLayout ll;
    private LinearLayout ll_back;
    private PullToRefreshLayout ptrl;
    private IUAdapter adapter;
    private int pageNum = 1;
    private List<Page> pages;
    private TextView tv_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.invite_users_activity);

        this.mlv = (MyListView) super.findViewById(R.id.mlv);
        this.ll = (LinearLayout) super.findViewById(R.id.ll);
        this.tv_num = (TextView) super.findViewById(R.id.tv_num);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.refresh_view);

        this.ptrl.setOnRefreshListener(new MyListener());

        this.getData(0);

        this.ll_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.mlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(InviteUsersActivity.this
                        , InviteUsersDetailsActivity.class)
                        .putExtra("id", pages.get(position).getId()));
            }
        });
    }

    private void getData(final int space) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "84");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum));

        new HttpClientGet(InviteUsersActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println(result);

                InviteUsersBean iuBean = new Gson().fromJson(result, InviteUsersBean.class);

                tv_num.setText(iuBean.getFriends().getTotalCount());

                switch (space) {
                    case 0:
                        // 第一次加载

                        pages = new ArrayList<>();

                        pages = iuBean.getFriends().getPage();

                        adapter = new IUAdapter(InviteUsersActivity.this, pages);

                        mlv.setAdapter(adapter);

                        if (pages.size() > 15) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }

                        break;

                    case 1:
                        // 刷新

                        pages = new ArrayList<>();

                        pages = iuBean.getFriends().getPage();

                        adapter = new IUAdapter(InviteUsersActivity.this, pages);

                        mlv.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        if (pages.size() > 15) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }

                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                        break;

                    case 2:
                        // 加载更多

                        List<Page> _pages = new ArrayList<>();

                        _pages = iuBean.getFriends().getPage();

                        if (null != _pages && _pages.size() > 0) {

                            pages.addAll(_pages);

                            adapter = new IUAdapter(InviteUsersActivity.this, pages);

                            mlv.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
                        }

                        if (_pages.size() > 15) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }

                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                        break;
                }
            }
        });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getData(1);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pageNum += 1;
            getData(2);
        }
    }

    private class IUAdapter extends BaseAdapter {

        private ViewHolder view;
        private Context context;
        private List<Page> pages;

        public IUAdapter(Context context, List<Page> pages) {
            this.context = context;
            this.pages = pages;
        }

        @Override
        public int getCount() {
            return null == pages ? 0 : pages.size();
        }

        @Override
        public Object getItem(int position) {
            return pages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_address_book_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            view.tv_lv_item_tag.setVisibility(View.GONE);

            if (null == pages.get(position).getUser_name() || pages.get(position).getUser_name().length() < 1) {
                view.tv_name.setText(pages.get(position).getName());
            } else {
                view.tv_name.setText(pages.get(position).getUser_name());
            }

            Glide.with(context).load(HttpConfig.IMAGEHOST + pages.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(view.iv_head);

            return convertView;
        }

        private class ViewHolder {

            private RoundImageView iv_head;
            private TextView tv_name;
            private TextView tv_lv_item_tag;

            public ViewHolder(View v) {
                this.iv_head = (RoundImageView) v.findViewById(R.id.iv_head);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_lv_item_tag = (TextView) v.findViewById(R.id.tv_lv_item_tag);
            }
        }
    }
}
