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
import com.lsjr.zizisteward.activity.ZiShangDetailActivity;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZiShangMoreLabelsListActivity extends BaseActivity {
    private String name;
    private SuperListView listview;
    private int pageNum = 1;
    private List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> list_orders = new ArrayList<ZiShangSearchNickAndLabelActivity.OrdersEnjoy>();
    private LabelAdapter adapter;
    private boolean isRefresh = true;
    private boolean login_state;
    private Intent intent;

    @Override
    public int getContainerView() {
        return R.layout.activity_shishi_label_more_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("标签列表");
        name = getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.text)).setText("孜赏标签");
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
        adapter = new LabelAdapter(ZiShangMoreLabelsListActivity.this, list_orders);
        listview.setAdapter(adapter);
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
        listview.refresh();
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (login_state == true) {
                    intent = new Intent(getApplicationContext(), ZiShangDetailActivity.class);
                    intent.putExtra("user_photo", list_orders.get(position - 1).getPhoto());// 用户图像
                    intent.putExtra("user_name", list_orders.get(position - 1).getUser_name());// 用户名字
                    intent.putExtra("user_level", list_orders.get(position - 1).getCredit_level_id());// 用户等级图像
                    intent.putExtra("faqi_number", list_orders.get(position - 1).getTicking_number());// 发起条数
                    intent.putExtra("dashang_number", list_orders.get(position - 1).getPlay_tours());// 打赏笔数
                    intent.putExtra("time", list_orders.get(position - 1).getEnjoy_time());// 时间
                    intent.putExtra("state", list_orders.get(position - 1).getIs_finish());// 是否完成状态
                    intent.putExtra("zishang_number", list_orders.get(position - 1).getZizipeas());// 孜赏数量
                    intent.putExtra("photo_number", list_orders.get(position - 1).getEnjoyImg());// 图片数量
                    intent.putExtra("custom_number", list_orders.get(position - 1).getCustom_tag());// 标签数量
                    intent.putExtra("feedback_number", list_orders.get(position - 1).getEnjoy_ticking());// 传过去的反馈数量
                    intent.putExtra("user_id", list_orders.get(position - 1).getUser_id());// 用户id
                    intent.putExtra("content", list_orders.get(position - 1).getContent());// 评论内容
                    intent.putExtra("image_size", list_orders.get(position - 1).getImg_wh());// 图片尺寸集
                    intent.putExtra("zishang_id", list_orders.get(position - 1).getId());// 孜赏id
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
                    intent.putExtra("personal", "zishangsearch");
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    protected void getData() {
        if (isRefresh) {
            pageNum = 1;
            adapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "92");
        map.put("keyword", name);
        map.put("currPage", String.valueOf(pageNum++));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("结果" + result);
                ZiShangLabelListBean bean = GsonUtil.getInstance().fromJson(result, ZiShangLabelListBean.class);
                if (pageNum == 1) {
                    adapter = new LabelAdapter(ZiShangMoreLabelsListActivity.this, list_orders);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    list_orders.addAll(bean.getZiziEnjoyLabelList().getPage());
                    adapter.setList(list_orders);
                }
                if (list_orders.size() < bean.getZiziEnjoyLabelList().getPageSize()) {
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

    private class LabelAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder mHolder;
        private List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> list_orders;

        public LabelAdapter(Context context, List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> list_orders) {
            this.context = context;
            this.list_orders = list_orders;
        }

        public void setList(List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> list_orders) {
            this.list_orders = list_orders;
            notifyDataSetChanged();
        }

        public void add(ZiShangSearchNickAndLabelActivity.OrdersEnjoy list_orders) {
            this.list_orders.add(list_orders);
            notifyDataSetChanged();
        }

        public void addFirst(ZiShangSearchNickAndLabelActivity.OrdersEnjoy list_orders) {
            this.list_orders.add(0, list_orders);
            notifyDataSetChanged();
        }

        public void addAll(List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> list_orders) {
            this.list_orders.addAll(list_orders);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list_orders.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list_orders.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list_orders == null ? 0 : list_orders.size();
        }

        @Override
        public Object getItem(int position) {
            return list_orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_labels_search, parent, false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(context).load(HttpConfig.IMAGEHOST + list_orders.get(position).getSpicfirst())
                    .into(mHolder.label_photo);
            mHolder.label_content.setText("包含 : " + list_orders.get(position).getCustom_tag());
            return convertView;
        }

    }

    private class ViewHolder {
        private ImageView label_photo;
        private TextView label_content;

        public ViewHolder(View view) {
            label_photo = (ImageView) view.findViewById(R.id.label_photo);
            label_content = (TextView) view.findViewById(R.id.label_content);
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

    class ZiShangLabelListBean {
        private ziziEnjoyLabelList ziziEnjoyLabelList;

        public ziziEnjoyLabelList getZiziEnjoyLabelList() {
            return ziziEnjoyLabelList;
        }

        public void setZiziEnjoyLabelList(ziziEnjoyLabelList ziziEnjoyLabelList) {
            this.ziziEnjoyLabelList = ziziEnjoyLabelList;
        }

    }

    class ziziEnjoyLabelList {
        private int currPage;
        private List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> page;
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

        public List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> getPage() {
            return page;
        }

        public void setPage(List<ZiShangSearchNickAndLabelActivity.OrdersEnjoy> page) {
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
