package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupShareListBean;
import com.lsjr.zizisteward.bean.GroupShareListBean.GroupShareList;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupShareActivity extends Activity implements OnClickListener {

    private MyListView mlv;
    private LinearLayout ll;
    private GSAdapter adapter;
    private LinearLayout ll_back;
    private LinearLayout ll_more;
    private LinearLayout ll_cancel;
    private PopupWindow popupWindow;
    private TextView tv_check_delete;
    private TextView tv_prompt;
    private PullToRefreshLayout ptrl;

    private int pageNum = 1;
    private String Groupid;
    private String activity;
    private String pageSize = "8";
    private boolean space = false;
    private List<GroupShareList> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.group_share_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.tv_prompt = (TextView) super.findViewById(R.id.tv_prompt);
        this.ll = (LinearLayout) super.findViewById(R.id.ll);
        this.mlv = (MyListView) super.findViewById(R.id.mlv);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_more = (LinearLayout) super.findViewById(R.id.ll_more);
        this.ll_cancel = (LinearLayout) super.findViewById(R.id.ll_cancel);
        this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.refresh_view);
        this.tv_check_delete = (TextView) super.findViewById(R.id.tv_check_delete);

        this.ptrl.setOnRefreshListener(new MyListener());

        this.ll_back.setOnClickListener(this);
        this.ll_more.setOnClickListener(this);
        this.ll_cancel.setOnClickListener(this);
        this.tv_check_delete.setOnClickListener(this);

        this.Groupid = getIntent().getStringExtra("Groupid");
        this.activity = getIntent().getStringExtra("activity");

        if (activity.equals("gan")) {
            ll_more.setVisibility(View.VISIBLE);
        } else {
            ll_more.setVisibility(View.GONE);
        }

        this.mlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                startActivity(new Intent(GroupShareActivity.this, ShareDetailsActivity.class)
                        .putExtra("id", list.get(position).getId()));
            }
        });

        this.getData(2);
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            pageNum = 1;
            getData(0);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            if (ll.getVisibility() == View.VISIBLE) {
                pageNum += 1;
                getData(1);
            } else {
                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

    private void getData(final int space) {
        CustomDialogUtils.startCustomProgressDialog(GroupShareActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "312");
        map.put("fid", Groupid);
        map.put("currPage", String.valueOf(pageNum));
        map.put("pageSize", pageSize);

        new HttpClientGet(GroupShareActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                CustomDialogUtils.stopCustomProgressDialog(GroupShareActivity.this);

                GroupShareListBean gslBean = new Gson().fromJson(result, GroupShareListBean.class);

                switch (space) {
                    case 0:

                        list = new ArrayList<>();

                        list = gslBean.getGroupShareList();

                        if (null != list && list.size() > 0) {
                            ptrl.setVisibility(View.VISIBLE);
                            tv_prompt.setVisibility(View.GONE);

                            adapter = new GSAdapter(GroupShareActivity.this, list);

                            mlv.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            if (list.size() > 7) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                            ptrl.refreshFinish(ptrl.SUCCEED);

                        } else {
                            ptrl.setVisibility(View.GONE);
                            tv_prompt.setVisibility(View.VISIBLE);
                        }

                        break;

                    case 1:

                        List<GroupShareList> space = new ArrayList<>();

                        space = gslBean.getGroupShareList();

                        if (null != space && space.size() >= 1) {
                            list.addAll(space);
                        }

                        adapter.notifyDataSetChanged();

                        if (list.size() > 7) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }

                        ptrl.loadmoreFinish(ptrl.SUCCEED);

                        break;

                    case 2:

                        list = new ArrayList<>();

                        list = gslBean.getGroupShareList();

                        if (null != list && list.size() > 0) {
                            ptrl.setVisibility(View.VISIBLE);
                            tv_prompt.setVisibility(View.GONE);

                            adapter = new GSAdapter(GroupShareActivity.this, list);

                            mlv.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            if (list.size() > 7) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                        } else {
                            ptrl.setVisibility(View.GONE);
                            tv_prompt.setVisibility(View.VISIBLE);
                        }

                        break;
                }
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(GroupShareActivity.this);
                super.onFailure(myError);
            }
        });
    }

    private class GSAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<GroupShareList> list;
        private SimpleDateFormat format;

        public GSAdapter(Context context, List<GroupShareList> list) {
            this.context = context;
            this.list = list;
            format = new SimpleDateFormat("yyyy年MM月dd日");
        }

        @Override
        public int getCount() {
            return null == list ? 0 : list.size();
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

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.group_share_activity_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            view.tv_title.setText(list.get(position).getFlock_title());

            view.tv_content.setText(list.get(position).getShare_content());

            if (null == list.get(position).getPhoto() || list.get(position).getPhoto().length() < 1) {
                view.iv.setImageResource(R.drawable.icon_share_default);
            } else {
                String[] space = list.get(position).getPhoto().split(",");
                Glide.with(context).load(HttpConfig.IMAGEHOST + space[0]).into(view.iv);
            }

            String time = format.format(Long.parseLong(list.get(position).getStime().getTime()));

            view.tv_time.setText(time);

            if (space) {

                view.ll_check.setVisibility(View.VISIBLE);

            } else {

                view.ll_check.setVisibility(View.GONE);

            }

            if (list.get(position).isLy()) {
                view.iv_check.setImageResource(R.drawable.icon_card_check_true);
            } else {
                view.iv_check.setImageResource(R.drawable.icon_card_check_false);
            }

            view.ll_check.setTag(position);
            view.ll_check.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    list.get(pos).setLy(!list.get(pos).isLy());
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private RoundImageView iv;
            private TextView tv_title;
            private TextView tv_time;
            private TextView tv_content;
            private ImageView iv_check;
            private LinearLayout ll_check;

            public ViewHolder(View v) {
                this.iv_check = (ImageView) v.findViewById(R.id.iv_check);
                this.ll_check = (LinearLayout) v.findViewById(R.id.ll_check);
                this.iv = (RoundImageView) v.findViewById(R.id.iv);
                this.tv_time = (TextView) v.findViewById(R.id.tv_time);
                this.tv_title = (TextView) v.findViewById(R.id.tv_title);
                this.tv_content = (TextView) v.findViewById(R.id.tv_content);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_more:
                showPopupWindow(ll_more);
                break;

            case R.id.ll_cancel:

                tv_check_delete.setVisibility(View.GONE);

                space = false;

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setLy(false);
                }

                ll_more.setVisibility(View.VISIBLE);
                ll_cancel.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();

                break;

            case R.id.tv_check_delete:

                String id = "";

                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).isLy()) {
                        if (id.equals("")) {
                            id = list.get(i).getId();
                        } else {
                            id += "," + list.get(i).getId();
                        }
                    }
                }

                if (id.length() >= 1) {
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "315");
                    map.put("tufs_id", id);
                    new HttpClientGet(GroupShareActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println(result);
                            space = false;
                            getData(2);
                            tv_check_delete.setVisibility(View.GONE);
                            ll_more.setVisibility(View.VISIBLE);
                            ll_cancel.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }
                    });
                } else {
                    Toast.makeText(GroupShareActivity.this, "没有选择任何分享...", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getData(2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(2);
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(GroupShareActivity.this)
                .inflate(R.layout.group_share_activity_pop, null);
        // 设置按钮的点击事件
        TextView tv_send = (TextView) contentView
                .findViewById(R.id.tv_send);

        TextView tv_delete = (TextView) contentView
                .findViewById(R.id.tv_delete);

        tv_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                startActivity(new Intent(GroupShareActivity.this, SendShareActivity.class)
                        .putExtra("Groupid", Groupid));
            }
        });

        tv_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tv_check_delete.setVisibility(View.VISIBLE);
                ll_more.setVisibility(View.GONE);
                ll_cancel.setVisibility(View.VISIBLE);
                space = true;
                adapter.notifyDataSetChanged();
            }
        });

        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }
}
