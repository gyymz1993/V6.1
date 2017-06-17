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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.PersonalShiJieStatisticsActivity;
import com.lsjr.zizisteward.activity.ShiJieDetailActivity;
import com.lsjr.zizisteward.activity.WorldSearchActivity.Orders;
import com.lsjr.zizisteward.activity.WorldSearchActivity.ShiShiSearchBean;
import com.lsjr.zizisteward.activity.WorldSearchActivity.Users;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShiShiSearchFamousAndLabelActivity extends BaseActivity implements OnClickListener {
    private String name;
    private RelativeLayout re_parent;
    private TextView tv_name;
    private ImageView label_photo, label_photo_two;
    private MyListView listview_famous;
    private RelativeLayout more_famous_people, re_label_one, re_label_two, more_labels;
    private TextView text, label_content, label_content_two;
    private List<Orders> list_orders = new ArrayList<Orders>();
    private List<Users> list_users = new ArrayList<Users>();
    private FamousAdapter famous_adapter;
    private LinearLayout ll_famous_people, ll_label;
    private View view_fengexian, view_label_xian;
    private Intent intent;
    private boolean login_state;
    private int location = 0;

    @Override
    public int getContainerView() {
        return R.layout.activity_shishisearch_famous_and_label;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("时视搜索");
        name = getIntent().getStringExtra("name");
        re_parent = (RelativeLayout) findViewById(R.id.re_parent);// 搜索框
        tv_name = (TextView) findViewById(R.id.tv_name);// 搜索框里的文字
        text = (TextView) findViewById(R.id.text);// 无搜索内容
        listview_famous = (MyListView) findViewById(R.id.listview_famous);// 名人列表
        more_famous_people = (RelativeLayout) findViewById(R.id.more_famous_people);// 更多名人
        ll_famous_people = (LinearLayout) findViewById(R.id.ll_famous_people);// 名人显示与否
        view_fengexian = findViewById(R.id.view_fengexian);// 分割线
        ll_label = (LinearLayout) findViewById(R.id.ll_label);// 标签显示与否
        re_label_one = (RelativeLayout) findViewById(R.id.re_label_one);// 标签布局1
        label_photo = (ImageView) findViewById(R.id.label_photo);// 标签图标1
        label_content = (TextView) findViewById(R.id.label_content);// 标签内容1
        re_label_two = (RelativeLayout) findViewById(R.id.re_label_two);// 标签布局2
        label_photo_two = (ImageView) findViewById(R.id.label_photo_two);// 标签图标2
        label_content_two = (TextView) findViewById(R.id.label_content_two);// 标签内容2
        more_labels = (RelativeLayout) findViewById(R.id.more_labels);// 更多标签
        view_label_xian = findViewById(R.id.view_label_xian);// 标签分割线
        tv_name.setText(name);
        initListener();
    }

    @Override
    protected void onResume() {
        getData();
        login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        super.onResume();
    }

    private void initListener() {
        // 到个人时视统计
        listview_famous.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                location = position;
                if (login_state == true) {
                    intent = new Intent(getApplicationContext(), PersonalShiJieStatisticsActivity.class);
                    intent.putExtra("user_id", list_users.get(position).getId());
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
        more_famous_people.setOnClickListener(this);
        more_labels.setOnClickListener(this);
        re_parent.setOnClickListener(this);
        re_label_one.setOnClickListener(this);
        re_label_two.setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.iv_back)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    /* 到时视详情 */
    private void toPersonalShiShi(int item) {
        location = item;
        if (login_state == true) {
            Intent intent = new Intent(ShiShiSearchFamousAndLabelActivity.this, ShiJieDetailActivity.class);
            intent.putExtra("id", list_orders.get(item).getId());
            intent.putExtra("content", list_orders.get(item).getContent());// 内容
            intent.putExtra("photo", list_orders.get(item).getPhoto());// 头像
            intent.putExtra("shareImg", list_orders.get(item).getShareImg());// 图片
            intent.putExtra("time", list_orders.get(item).getShare_time());// 时间
            intent.putExtra("user_name", list_orders.get(item).getUser_name());// 用户名
            intent.putExtra("sight_type", list_orders.get(item).getSight_type());// 内容类型
            intent.putExtra("custom_tag", list_orders.get(item).getCustom_tag());// 标签内容
            intent.putExtra("zan_count", list_orders.get(item).getShare_like());// 点赞次数
            String share_time_uid = list_orders.get(item).getShare_time_uid();
            if (share_time_uid != null && share_time_uid.length() > 0) {
                String[] zan_ids = share_time_uid.split(",");
                for (int i = 0; i < zan_ids.length; i++) {
                    if (App.getUserInfo().getId().equals(zan_ids[i])) {
                        intent.putExtra("zan_state", true);// 点赞状态已点赞
                    } else {
                        intent.putExtra("zan_state", false);// 点赞状态
                    }
                }
            }
            String collect_time_uid = list_orders.get(item).getCollect_time_uid();
            if (collect_time_uid != null && collect_time_uid.length() > 0) {
                String[] collect_ids = collect_time_uid.split(",");
                for (int i = 0; i < collect_ids.length; i++) {
                    if (App.getUserInfo().getId().equals(collect_ids[i])) {
                        intent.putExtra("collect_state", true);// 收藏状态已收藏
                    } else {
                        intent.putExtra("collect_state", false);// 收藏状态
                    }
                }
            }
            intent.putExtra("image_size", list_orders.get(item).getImg_wh());// 图片尺寸
            intent.putExtra("level", list_orders.get(item).getCredit_level_id());// 用户等级
            intent.putExtra("user_id", list_orders.get(item).getUser_id());// 用户id
            startActivityForResult(intent, 2);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
            intent.putExtra("personal", "shishi_search_more_list_label");
            startActivityForResult(intent, 1);
        }
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

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "98");
        map.put("keyword", name);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                ShiShiSearchBean bean = GsonUtil.getInstance().fromJson(result, ShiShiSearchBean.class);
                list_orders = bean.getSights().getOrders();
                list_users = bean.getSights().getUsers();
                if (list_orders.size() == 0 && list_users.size() == 0) {
                    ll_famous_people.setVisibility(View.GONE);
                    ll_label.setVisibility(View.GONE);
                    view_fengexian.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                } else {
                    text.setVisibility(View.GONE);
                    if (list_users.size() > 0 && list_orders.size() > 0) {// 名人和标签同时有
                        view_fengexian.setVisibility(View.VISIBLE);
                        ll_label.setVisibility(View.VISIBLE);
                        ll_famous_people.setVisibility(View.VISIBLE);
                        /* 名人数据 */
                        setFamousData();
						/* 标签数据 */
                        if (list_orders.size() == 1) {// 只有一个标签
                            setFirstLaber();
                        }
                        if (list_orders.size() > 1) {// 多个标签
                            setSecondLaber();
                        }
                    }
                    if (list_users.size() > 0 && list_orders.size() == 0) {// 只有名人
                        ll_famous_people.setVisibility(View.VISIBLE);
                        ll_label.setVisibility(View.GONE);
                        view_fengexian.setVisibility(View.GONE);
                        setFamousData();
                    }
                    if (list_orders.size() > 0 && list_users.size() == 0) {// 只有标签
                        ll_famous_people.setVisibility(View.GONE);
                        ll_label.setVisibility(View.VISIBLE);
                        view_fengexian.setVisibility(View.GONE);
                        if (list_orders.size() == 1) {
                            setFirstLaber();
                        }
                        if (list_orders.size() > 1) {
                            setSecondLaber();
                        }
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_famous_people:
                intent = new Intent(getApplicationContext(), ShiShiSearchMoreListActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.re_parent:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.more_labels:
                intent = new Intent(getApplicationContext(), ShiShiLabelMoreListActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.re_label_one:
                toPersonalShiShi(0);
                break;
            case R.id.re_label_two:
                toPersonalShiShi(1);
                break;
        }
    }

    private void setFirstLaber() {
        view_label_xian.setVisibility(View.GONE);
        re_label_two.setVisibility(View.GONE);
        Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_orders.get(0).getSpicfirst())
                .into(label_photo);
        label_content.setText(list_orders.get(0).getCustom_tag());
    }

    private void setSecondLaber() {
        view_label_xian.setVisibility(View.VISIBLE);
        re_label_two.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_orders.get(0).getSpicfirst())
                .into(label_photo);
        label_content.setText(list_orders.get(0).getCustom_tag());
        Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_orders.get(1).getSpicfirst())
                .into(label_photo_two);
        label_content_two.setText(list_orders.get(1).getCustom_tag());
    }

    private void setFamousData() {
        famous_adapter = new FamousAdapter(ShiShiSearchFamousAndLabelActivity.this, list_users);
        listview_famous.setAdapter(famous_adapter);
    }

    private class FamousAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder mHolder;
        private List<Users> list_users;

        public FamousAdapter(Context context, List<Users> list_users) {
            this.context = context;
            this.list_users = list_users;
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
}
