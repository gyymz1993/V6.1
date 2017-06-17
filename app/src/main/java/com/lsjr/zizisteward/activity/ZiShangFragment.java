package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class ZiShangFragment extends Fragment implements OnClickListener {
    private View rootView, view2, listview_head;
    private int[] classics = {R.drawable.dapai, R.drawable.qingshe, R.drawable.caopai, R.drawable.xianliang,
            R.drawable.gexing, R.drawable.dingzhi, R.drawable.jiangpin, R.drawable.huwai, R.drawable.meiwei,
            R.drawable.jiaju, R.drawable.chuxing, R.drawable.jiangjian};
    private String[] names = {"大牌", "轻奢", "潮牌", "限量", "个性", "定制", "匠品", "户外", "美味", "家居", "出行", "健检"};
    private RelativeLayout mRe_classic, re_state, re_time, re_money;
    private View mClassic_view, state_view, time_view, money_view;
    private PopupWindow mClassic_window, state_window, time_window, money_window;
    private MyGridView mBrand_gridview;
    private TextView tv_one, tv_two, tv_three, tv_four;
    private SuperListView mListview_zishang;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private ZiShangAdapter mAdapter;
    private TextView mTv_no_content;
    private ImageView mAdd_zishang;
    private List<ZiShangInfo> list = new ArrayList<ZiShangInfo>();
    private List<HotsList> hots_list = new ArrayList<HotsList>();
    private TextView mAll_state;
    private TextView mNodown_state;
    private TextView mFinished_state;
    private TextView mAll_time;
    private TextView mTwo_days;
    private TextView mWeeks;
    private TextView mMore_weeks;
    private TextView mRandom;
    private TextView mHightodown;
    private TextView mDowntohigh;
    private TextView mMoretwowan;
    private ImageView mIv_jiantou;
    private final int SENDZI = 1;
    private final int ZISHANGDETAIL = 2;
    private int loadMore = 0;
    private String fenlei = "";
    /* 热门排行 */
    private HorizontalScrollView hsl;
    private LinearLayout ll_parent;
    NumberFormat numberFormat;
    private boolean login_state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zishang, null);
            mListview_zishang = (SuperListView) rootView.findViewById(R.id.listview_zishang);
            mAdd_zishang = (ImageView) rootView.findViewById(R.id.add_zishang);// 增加孜赏按钮
            mTv_no_content = (TextView) rootView.findViewById(R.id.tv_no_content);// 无内容显示
            // 这是头部view
            listview_head = inflater.inflate(R.layout.listview_head, null);
            mListview_zishang.addHeaderView(listview_head);
            hsl = (HorizontalScrollView) listview_head.findViewById(R.id.hsl);
            ll_parent = (LinearLayout) listview_head.findViewById(R.id.ll_parent);

            mRe_classic = (RelativeLayout) listview_head.findViewById(R.id.re_classic);// 分类
            mIv_jiantou = (ImageView) listview_head.findViewById(R.id.iv_jiantou);// 分类箭头
            re_state = (RelativeLayout) listview_head.findViewById(R.id.re_state);// 状态
            re_time = (RelativeLayout) listview_head.findViewById(R.id.re_time);// 时间
            re_money = (RelativeLayout) listview_head.findViewById(R.id.re_money);// 金额
            view2 = listview_head.findViewById(R.id.view2);// 分割线
            tv_one = (TextView) listview_head.findViewById(R.id.tv_one);// 分类文字
            tv_two = (TextView) listview_head.findViewById(R.id.tv_two);// 状态文字
            tv_three = (TextView) listview_head.findViewById(R.id.tv_three);// 时间文字
            tv_four = (TextView) listview_head.findViewById(R.id.tv_four);// 赏金文字

            // 分类弹窗
            mClassic_view = getActivity().getLayoutInflater().inflate(R.layout.popup_zishang_classic, null);
            mClassic_window = new PopupWindow(mClassic_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                    true);
            mClassic_window.setOutsideTouchable(true);
            mClassic_window.setTouchable(true);
            // mClassic_window.setFocusable(false);
            mClassic_window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            mBrand_gridview = (MyGridView) mClassic_view.findViewById(R.id.brand_gridview);
            RelativeLayout re_fenlei = (RelativeLayout) mClassic_view.findViewById(R.id.re_fenlei);
            re_fenlei.getBackground().setAlpha(255);

            // 状态弹窗
            state_view = getActivity().getLayoutInflater().inflate(R.layout.popup_zishang_state, null);
            state_window = new PopupWindow(state_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            state_window.setOutsideTouchable(true);
            state_window.setTouchable(true);
            // state_window.setFocusable(false);
            state_window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            mAll_state = (TextView) state_view.findViewById(R.id.all_state);
            mNodown_state = (TextView) state_view.findViewById(R.id.nodown_state);
            mFinished_state = (TextView) state_view.findViewById(R.id.finished_state);

            // 时间弹窗
            time_view = getActivity().getLayoutInflater().inflate(R.layout.popup_zishang_time, null);
            time_window = new PopupWindow(time_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            time_window.setOutsideTouchable(true);
            time_window.setTouchable(true);
            // time_window.setFocusable(false);
            time_window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            mAll_time = (TextView) time_view.findViewById(R.id.all_time);
            mTwo_days = (TextView) time_view.findViewById(R.id.two_days);
            mWeeks = (TextView) time_view.findViewById(R.id.weeks);
            mMore_weeks = (TextView) time_view.findViewById(R.id.more_weeks);

            // 金额弹窗
            money_view = getActivity().getLayoutInflater().inflate(R.layout.popup_zishang_money, null);
            money_window = new PopupWindow(money_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            money_window.setOutsideTouchable(true);
            // money_window.setFocusable(false);
            money_window.setTouchable(true);
            money_window.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            mRandom = (TextView) money_view.findViewById(R.id.random);
            mHightodown = (TextView) money_view.findViewById(R.id.hightodown);
            mDowntohigh = (TextView) money_view.findViewById(R.id.downtohigh);
            mMoretwowan = (TextView) money_view.findViewById(R.id.moretwowan);

            initView();
            initListener();
            initHotData();// 热门排行

        }
        return rootView;
    }

    private OnClickListener toolsItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean state = PreferencesUtils.getBoolean(getContext(), "isLogin");
            if (state == true) {
                Intent intent = new Intent(getContext(), ZiShangDetailActivity.class);
                intent.putExtra("user_photo", hots_list.get(v.getId()).getPhoto());// 用户图像
                intent.putExtra("user_name", hots_list.get(v.getId()).getUser_name());// 用户名字
                intent.putExtra("user_level", hots_list.get(v.getId()).getCredit_level_id());// 用户等级图像
                intent.putExtra("faqi_number", hots_list.get(v.getId()).getTicking_number());// 发起条数
                intent.putExtra("dashang_number", hots_list.get(v.getId()).getPlay_tours());// 打赏笔数
                intent.putExtra("time", hots_list.get(v.getId()).getEnjoy_time().getTime());// 时间
                intent.putExtra("state", hots_list.get(v.getId()).getIs_finish());// 是否完成状态
                intent.putExtra("zishang_number", hots_list.get(v.getId()).getZizipeas());// 孜赏数量
                intent.putExtra("photo_number", hots_list.get(v.getId()).getEnjoyImg());// 图片数量
                intent.putExtra("custom_number", hots_list.get(v.getId()).getCustom_tag());// 标签数量
                intent.putExtra("feedback_number", hots_list.get(v.getId()).getEnjoy_ticking());// 传过去的反馈数量
                intent.putExtra("user_id", hots_list.get(v.getId()).getUser_id());// 用户id
                intent.putExtra("content", hots_list.get(v.getId()).getContent());// 评论内容
                intent.putExtra("image_size", hots_list.get(v.getId()).getImg_wh());// 图片尺寸集
                intent.putExtra("zishang_id", hots_list.get(v.getId()).getId());// 孜赏id
                startActivity(intent);
            } else {
                final Dialog dialog = new Dialog(getContext(), R.style.dialog);
                dialog.setContentView(R.layout.popup_delete_address);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER | Gravity.CENTER);
                TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                tv_msg.setText("请先登录,好吗?");
                tv_confirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), NoteLoginActivity.class);
                        intent.putExtra("personal", "zishang_detail");
                        startActivity(intent);
                    }
                });
                tv_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    };

    private View views[];
    private TextView texts[], nums[];
    private RoundImageView users[];
    private RelativeLayout re_titles[];
    private LinearLayout ll_beijings[];

    private void initHotData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "75");
        map.put("type_id", "0");
        map.put("keyword", "0");
        map.put("currPage", "1");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                ZiShangListBean bean = GsonUtil.getInstance().fromJson(result, ZiShangListBean.class);
                hots_list = bean.getHots();

                views = new View[hots_list.size()];
                texts = new TextView[hots_list.size()];
                nums = new TextView[hots_list.size()];
                re_titles = new RelativeLayout[hots_list.size()];
                ll_beijings = new LinearLayout[hots_list.size()];
                users = new RoundImageView[hots_list.size()];
                for (int i = 0; i < hots_list.size(); i++) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.item_top_ranking, null);
                    view.setId(i);
                    view.setOnClickListener(toolsItemListener);
                    RelativeLayout re_parent = (RelativeLayout) view.findViewById(R.id.re_parent);
                    RoundImageView iv_one = (RoundImageView) view.findViewById(R.id.iv_one);
                    LinearLayout re_beijing = (LinearLayout) view.findViewById(R.id.re_beijing);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                    TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
                    Glide.with(getContext()).load(HttpConfig.IMAGEHOST + hots_list.get(i).getSpicfirst()).into(iv_one);
                    tv_num.setText(hots_list.get(i).getEnjoy_ticking());

                    ll_parent.addView(view);
                    texts[i] = tv_name;
                    nums[i] = tv_num;
                    users[i] = iv_one;
                    re_titles[i] = re_parent;
                    ll_beijings[i] = re_beijing;
                    views[i] = view;
                }

            }

        });
    }

    @Override
    public void onStop() {
        mClassic_window.dismiss();
        state_window.dismiss();
        time_window.dismiss();
        money_window.dismiss();
        super.onStop();
    }

    @Override
    public void onResume() {
        login_state = PreferencesUtils.getBoolean(getContext(), "isLogin");
        isRefresh = true;
        getData("0", "0");
        super.onResume();
    }

    private void initListener() {
        mRe_classic.setOnClickListener(this);
        re_state.setOnClickListener(this);
        re_time.setOnClickListener(this);
        re_money.setOnClickListener(this);
        mAdd_zishang.setOnClickListener(this);
        mAll_state.setOnClickListener(this);
        mNodown_state.setOnClickListener(this);
        mFinished_state.setOnClickListener(this);
        mAll_time.setOnClickListener(this);
        mTwo_days.setOnClickListener(this);
        mWeeks.setOnClickListener(this);
        mMore_weeks.setOnClickListener(this);
        mRandom.setOnClickListener(this);
        mHightodown.setOnClickListener(this);
        mDowntohigh.setOnClickListener(this);
        mMoretwowan.setOnClickListener(this);
    }

    private void getData(String type, String keyword) {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "75");
        map.put("type_id", type);
        map.put("keyword", keyword);
        map.put("currPage", String.valueOf(pageNum++));

        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("孜赏列表" + result);
                ZiShangListBean bean = GsonUtil.getInstance().fromJson(result, ZiShangListBean.class);
                if (1 != pageNum) {
                    list.addAll(bean.getEnjoys().getPage());
                    mAdapter.setList(list);
                } else {
                    list = bean.getEnjoys().getPage();
                    mAdapter = new ZiShangAdapter(getActivity(), list);
                    mListview_zishang.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                if (list.size() < bean.getEnjoys().getPageSize()) {
                    mListview_zishang.setIsLoadFull(false);
                }

                mListview_zishang.finishRefresh();
                mListview_zishang.finishLoadMore();
            }

        });
    }

    private void initView() {
        // 创建一个数值格式化对象
        numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后0位写几就精确到后面几位
        numberFormat.setMaximumFractionDigits(0);
        mClassic_view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mClassic_window.isShowing()) {
                    tv_one.setTextColor(Color.parseColor("#000000"));
                } else {
                    tv_one.setTextColor(Color.parseColor("#bbbbbb"));
                }

                return true;
            }
        });

        FenLeiAdapter adapter = new FenLeiAdapter();
        mBrand_gridview.setAdapter(adapter);
        mBrand_gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRefresh = true;
                mAdapter.removeAll();
                getData("1", names[position]);
                mClassic_window.dismiss();
                mAdapter.notifyDataSetChanged();
                fenlei = names[position];
                loadMore = 111;
            }
        });

        mAdapter = new ZiShangAdapter(getContext(), list);
        mListview_zishang.setAdapter(mAdapter);
        mListview_zishang.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                loadMore = 0;
                getData("0", "0");
            }
        });

        mListview_zishang.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                if (loadMore == 0) {
                    getData("0", "0");
                } else if (loadMore == 41) {
                    getData("4", "1");
                } else if (loadMore == 42) {
                    getData("4", "2");
                } else if (loadMore == 43) {
                    getData("4", "3");
                } else if (loadMore == 44) {
                    getData("4", "4");
                } else if (loadMore == 32) {
                    getData("3", "2");
                } else if (loadMore == 37) {
                    getData("3", "7");
                } else if (loadMore == 38) {
                    getData("3", "8");
                } else if (loadMore == 20) {
                    getData("2", "0");
                } else if (loadMore == 21) {
                    getData("2", "1");
                } else if (loadMore == 111) {
                    getData("1", fenlei);
                }

            }
        });
        mListview_zishang.refresh();
        mListview_zishang.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (login_state == true) {
                    Intent intent = new Intent(getContext(), ZiShangDetailActivity.class);
                    intent.putExtra("user_photo", list.get(position - 2).getPhoto());// 用户图像
                    intent.putExtra("user_name", list.get(position - 2).getUser_name());// 用户名字
                    intent.putExtra("user_level", list.get(position - 2).getCredit_level_id());// 用户等级图像
                    intent.putExtra("faqi_number", list.get(position - 2).getTicking_number());// 发起条数
                    intent.putExtra("dashang_number", list.get(position - 2).getPlay_tours());// 打赏笔数
                    intent.putExtra("time", list.get(position - 2).getEnjoy_time().getTime());// 时间
                    intent.putExtra("state", list.get(position - 2).getIs_finish());// 是否完成状态
                    intent.putExtra("zishang_number", list.get(position - 2).getZizipeas());// 孜赏数量
                    intent.putExtra("photo_number", list.get(position - 2).getEnjoyImg());// 图片数量
                    intent.putExtra("custom_number", list.get(position - 2).getCustom_tag());// 标签数量
                    intent.putExtra("feedback_number", list.get(position - 2).getEnjoy_ticking());// 传过去的反馈数量
                    intent.putExtra("user_id", list.get(position - 2).getUser_id());// 用户id
                    intent.putExtra("content", list.get(position - 2).getContent());// 评论内容
                    intent.putExtra("image_size", list.get(position - 2).getImg_wh());// 图片尺寸集
                    intent.putExtra("zishang_id", list.get(position - 2).getId());// 孜赏id
                    startActivityForResult(intent, ZISHANGDETAIL);
                } else {
                    final Dialog dialog = new Dialog(getContext(), R.style.dialog);
                    dialog.setContentView(R.layout.popup_delete_address);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_msg.setText("请先登录,好吗?");
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), NoteLoginActivity.class);
                            intent.putExtra("personal", "zishang_detail");
                            startActivity(intent);
                        }
                    });
                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    public class FenLeiAdapter extends BaseAdapter {

        private ViewHolder mHolder;

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_zishang_classic, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.mIv_classic.setImageResource(classics[position]);
            mHolder.mTv_classic.setText(names[position]);
            return convertView;
        }

    }

    public class ZiShangAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder mHolder;
        private List<ZiShangInfo> list;

        public ZiShangAdapter(Context context, List<ZiShangInfo> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<ZiShangInfo> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(ZiShangInfo page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ZiShangInfo page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ZiShangInfo> list) {
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_zishang_list, parent, false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            String img_wh = list.get(position).getImg_wh();
            String enjoyImg = list.get(position).getEnjoyImg();
            String[] images = enjoyImg.split(",");
            String[] size = img_wh.split(",");
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int widthPixels = dm.widthPixels;
            int dip2px = DensityUtil.dip2px(getContext(), 90);

            // LinearLayout.LayoutParams linearParams =
            // (LinearLayout.LayoutParams) mHolder.mIv_zishang_photo
            // .getLayoutParams();
            // double space = 0;
            // double _with = Double.valueOf(size[position * 2]);
            // double _height = Double.valueOf(size[position * 2 + 1]);
            // space = (widthPixels - dip2px) / _with;
            // linearParams.width = widthPixels - dip2px;
            // linearParams.height = (int) (_height * space);
            // mHolder.mIv_zishang_photo.setLayoutParams(linearParams);
            // Glide.with(getActivity()).load(HttpConfig.IMAGEHOST +
            // images[0]).into(mHolder.mIv_zishang_photo);
            Glide.with(getActivity()).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst())
                    .into(mHolder.mIv_zishang_photo);

            Glide.with(getActivity()).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto())
                    .into(mHolder.mIv_name_photo);
            mHolder.mTv_name.setText(list.get(position).getUser_name());
            if ("1".equals(list.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_one);
            }
            if ("2".equals(list.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_two);
            }
            if ("3".equals(list.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            }
            if ("4".equals(list.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            }
            if ("5".equals(list.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_five);
            }
            if ("6".equals(list.get(position).getCredit_level_id())) {
                mHolder.mIv_level.setImageResource(R.drawable.level_six);
            }
            /*发起数量*/
            int fanqinumber = Integer.parseInt(list.get(position).getTicking_number());
            /*打赏数量*/
            int dashangnumber = Integer.parseInt(list.get(position).getPlay_tours());

            String result = numberFormat.format((float) dashangnumber / (float) fanqinumber * 100);

            mHolder.mTv_feedback_number.setText(
                    list.get(position).getTicking_number() + "个任务" + ", " + "打赏率" + result + "%");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String share_time = formatter.format(Long.valueOf(list.get(position).getEnjoy_time().getTime()));
            mHolder.mTv_time.setText(share_time);
            if ("1".equals(list.get(position).getIs_finish())) {
                mHolder.mTv_state.setText("已完成");
                mHolder.tv_is_done.setText("已完成");
                mHolder.tv_is_done.setBackgroundResource(R.drawable.yiwancheng);
                mHolder.re_go_to_fankui.setVisibility(View.GONE);
            } else {
                mHolder.mTv_state.setText("未完成");
                mHolder.tv_is_done.setText("未完成");
                mHolder.tv_is_done.setBackgroundResource(R.drawable.weiwancheng);
                mHolder.re_go_to_fankui.setVisibility(View.VISIBLE);
            }
            mHolder.mTv_douzi_number.setText(list.get(position).getZizipeas());
            mHolder.mTv_content.setText(list.get(position).getContent());

            if (list.get(position).getEnjoy_ticking().equals("0")) {/*0条反馈*/
                mHolder.tv_is_hava_fankui.setText("暂无反馈");
                mHolder.tv_is_hava_fankui.setBackgroundResource(R.drawable.meiyoufankui);
            } else {
                mHolder.tv_is_hava_fankui.setText("已有反馈" + list.get(position).getEnjoy_ticking());
                mHolder.tv_is_hava_fankui.setBackgroundResource(R.drawable.yiyoufankui);
            }
            String custom_tag = list.get(position).getCustom_tag();
            String[] tags = custom_tag.split(",");
            if (tags.length == 1) {
                mHolder.mTv_classic_one.setText(tags[0]);
                mHolder.mTv_classic_two.setVisibility(View.GONE);
                mHolder.mTv_classic_three.setVisibility(View.GONE);
            }
            if (tags.length == 2) {
                mHolder.mTv_classic_one.setText(tags[0]);
                mHolder.mTv_classic_two.setVisibility(View.VISIBLE);
                mHolder.mTv_classic_two.setText(tags[1]);
                mHolder.mTv_classic_three.setVisibility(View.GONE);
            }
            if (tags.length == 3) {
                mHolder.mTv_classic_one.setText(tags[0]);
                mHolder.mTv_classic_two.setVisibility(View.VISIBLE);
                mHolder.mTv_classic_two.setText(tags[1]);
                mHolder.mTv_classic_three.setVisibility(View.VISIBLE);
                mHolder.mTv_classic_three.setText(tags[2]);
            }

            mHolder.re_go_to_fankui.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (login_state == true) {
                        if (App.getUserInfo().getId().equals(list.get(position).getUser_id())) {
                            ToastUtils.show(getContext(), "不能反馈自己发的孜赏");
                        } else {
                            Intent intent = new Intent(getContext(), SendFeedBackActivity.class);
                            intent.putExtra("publish_id", list.get(position).getUser_id());// 发布者id
                            intent.putExtra("zishang_id", list.get(position).getId());// 孜赏id
                            startActivityForResult(intent, 111);
                        }
                    } else {
                        ToastUtils.show(getContext(), "您还未登录，请先登录");
                    }
                }
            });
            mHolder.tv_is_hava_fankui.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (login_state == true) {
                        if (App.getUserInfo().getId().equals(list.get(position).getUser_id())) {/*自己发的孜赏到加价*/
                            Intent intent = new Intent(getContext(), FeedBackListActivity.class);
                            intent.putExtra("publish_id", list.get(position).getUser_id());// 用户id
                            intent.putExtra("feedback_number", list.get(position).getEnjoy_ticking());
                            intent.putExtra("zidou", list.get(position).getZizipeas());
                            intent.putExtra("zishang_id", list.get(position).getId());// 孜赏id
                            intent.putExtra("state", list.get(position).getIs_finish());
                            startActivityForResult(intent, 525);
                        } else {/*别人发的孜赏到反馈*/
                            Intent intent = new Intent(getContext(), CommonFeedBackListActivity.class);
                            intent.putExtra("publish_id", list.get(position).getUser_id());
                            intent.putExtra("feedback_number", list.get(position).getEnjoy_ticking());
                            intent.putExtra("zidou", list.get(position).getZizipeas());
                            intent.putExtra("zishang_id", list.get(position).getId());
                            startActivityForResult(intent, 523);
                        }
                    } else {
                        ToastUtils.show(getContext(), "您还未登录，请先登录");
                    }
                }
            });
            return convertView;
        }

    }

    public class ViewHolder {

        private RoundImageView mIv_one;
        private LinearLayout mRe_beijing;
        private ImageView mIv_classic;
        private TextView mTv_classic;
        private RoundImageView mIv_name_photo;
        private TextView mTv_name;
        private ImageView mIv_level;
        private TextView mTv_feedback_number;
        private TextView mTv_time;
        private TextView mTv_state;
        private TextView mTv_douzi_number;
        private TextView mTv_content;
        private RoundImageView mIv_zishang_photo;
        private TextView mTv_classic_one;
        private TextView mTv_fankui_number;
        private RelativeLayout mRe_fenlei_beijing, re_go_to_fankui;
        private TextView mTv_classic_two;
        private TextView mTv_classic_three;
        private TextView mTv_num, tv_is_hava_fankui, tv_is_done;

        public ViewHolder(View view) {
            mTv_num = (TextView) view.findViewById(R.id.tv_num);// 热门排行的反馈数量
            mRe_beijing = (LinearLayout) view.findViewById(R.id.re_beijing);// 背景透明
            mIv_one = (RoundImageView) view.findViewById(R.id.iv_one);// 图片
            mIv_classic = (ImageView) view.findViewById(R.id.iv_classic);// 分类图片
            mTv_classic = (TextView) view.findViewById(R.id.tv_classic);// 分类文字
            mRe_fenlei_beijing = (RelativeLayout) view.findViewById(R.id.re_fenlei_beijing);// 分类背景

            mIv_name_photo = (RoundImageView) view.findViewById(R.id.iv_name_photo);// 用户图像
            mTv_name = (TextView) view.findViewById(R.id.tv_name);// 用户名
            mIv_level = (ImageView) view.findViewById(R.id.iv_level);// 用户等级图标
            mTv_feedback_number = (TextView) view.findViewById(R.id.tv_feedback_number);// 发起和打赏数量
            mTv_time = (TextView) view.findViewById(R.id.tv_time);// 时间
            mTv_state = (TextView) view.findViewById(R.id.tv_state);// 状态
            mTv_douzi_number = (TextView) view.findViewById(R.id.tv_douzi_number);// 豆孜数量
            mTv_content = (TextView) view.findViewById(R.id.tv_content);// 评论内容
            mIv_zishang_photo = (RoundImageView) view.findViewById(R.id.iv_zishang_photo);// 孜赏图片
            mTv_classic_one = (TextView) view.findViewById(R.id.tv_classic_one);// 标签1
            mTv_classic_two = (TextView) view.findViewById(R.id.tv_classic_two);// 标签2
            mTv_classic_three = (TextView) view.findViewById(R.id.tv_classic_three);// 标签3
            mTv_fankui_number = (TextView) view.findViewById(R.id.tv_fankui_number);// 反馈数量
            tv_is_hava_fankui = (TextView) view.findViewById(R.id.tv_is_hava_fankui);//是否反馈及数量
            tv_is_done = (TextView) view.findViewById(R.id.tv_is_done);//是否完成
            re_go_to_fankui = (RelativeLayout) view.findViewById(R.id.re_go_to_fankui);//去反馈
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SENDZI && resultCode == 1) {
            mListview_zishang.refresh();
        }
        if (requestCode == ZISHANGDETAIL && resultCode == 2) {
            mListview_zishang.refresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_classic:
                mClassic_window.showAsDropDown(view2, 0, 0);
                tv_one.setTextColor(Color.parseColor("#000000"));
                tv_two.setTextColor(Color.parseColor("#BBBBBB"));
                tv_three.setTextColor(Color.parseColor("#BBBBBB"));
                tv_four.setTextColor(Color.parseColor("#BBBBBB"));
                break;
            case R.id.re_state:
                state_window.showAsDropDown(view2, 0, 0);
                tv_one.setTextColor(Color.parseColor("#BBBBBB"));
                tv_two.setTextColor(Color.parseColor("#000000"));
                tv_three.setTextColor(Color.parseColor("#BBBBBB"));
                tv_four.setTextColor(Color.parseColor("#BBBBBB"));
                break;
            case R.id.re_time:
                time_window.showAsDropDown(view2, 0, 0);
                tv_one.setTextColor(Color.parseColor("#BBBBBB"));
                tv_two.setTextColor(Color.parseColor("#BBBBBB"));
                tv_three.setTextColor(Color.parseColor("#000000"));
                tv_four.setTextColor(Color.parseColor("#BBBBBB"));
                break;
            case R.id.re_money:
                money_window.showAsDropDown(view2, 0, 0);
                tv_one.setTextColor(Color.parseColor("#BBBBBB"));
                tv_two.setTextColor(Color.parseColor("#BBBBBB"));
                tv_three.setTextColor(Color.parseColor("#BBBBBB"));
                tv_four.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.add_zishang:
                boolean state = PreferencesUtils.getBoolean(getContext(), "isLogin");
                if (state == true) {
                    Intent intent = new Intent(getActivity(), SendZiShangActivity.class);
                    startActivityForResult(intent, SENDZI);
                } else {
                    final Dialog dialog = new Dialog(getContext(), R.style.dialog);
                    dialog.setContentView(R.layout.popup_delete_address);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_msg.setText("请先登录,好吗?");
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), NoteLoginActivity.class);
                            intent.putExtra("personal", "send_zishang");
                            startActivity(intent);
                        }
                    });
                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.all_state:
                isRefresh = true;
                mAdapter.removeAll();
                getData("0", "0");
                state_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.nodown_state:
                loadMore = 20;
                isRefresh = true;
                mAdapter.removeAll();
                getData("2", "0");
                state_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.finished_state:
                loadMore = 21;
                isRefresh = true;
                mAdapter.removeAll();
                getData("2", "1");
                state_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.all_time:
                isRefresh = true;
                mAdapter.removeAll();
                getData("0", "0");
                time_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.two_days:
                loadMore = 32;
                isRefresh = true;
                mAdapter.removeAll();
                getData("3", "2");
                time_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.weeks:
                loadMore = 37;
                isRefresh = true;
                mAdapter.removeAll();
                getData("3", "7");
                time_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.more_weeks:
                loadMore = 38;
                isRefresh = true;
                mAdapter.removeAll();
                getData("3", "8");
                time_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.random:
                loadMore = 41;
                isRefresh = true;
                mAdapter.removeAll();
                getData("4", "1");
                money_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.hightodown:
                loadMore = 42;
                isRefresh = true;
                mAdapter.removeAll();
                getData("4", "2");
                money_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.downtohigh:
                loadMore = 43;
                isRefresh = true;
                mAdapter.removeAll();
                getData("4", "3");
                money_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.moretwowan:
                loadMore = 44;
                isRefresh = true;
                mAdapter.removeAll();
                getData("4", "4");
                money_window.dismiss();
                mAdapter.notifyDataSetChanged();
                break;

        }
    }

    public class ZiShangListBean {
        private Enjoys enjoys;
        private String error;
        private String msg;
        private List<HotsList> hots;

        public List<HotsList> getHots() {
            return hots;
        }

        public void setHots(List<HotsList> hots) {
            this.hots = hots;
        }

        public Enjoys getEnjoys() {
            return enjoys;
        }

        public void setEnjoys(Enjoys enjoys) {
            this.enjoys = enjoys;
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

    public class HotsList {
        private String content;
        private String credit_level_id;
        private String custom_tag;
        private String enjoyImg;
        private String enjoy_ticking;
        private ShareTime enjoy_time;
        private String entityId;
        private String id;
        private String img_wh;
        private String is_finish;
        private String persistent;
        private String photo;
        private String play_tours;
        private String spicfirst;
        private String ticking_number;
        private String user_id;
        private String user_name;
        private String zizipeas;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCredit_level_id() {
            return credit_level_id;
        }

        public void setCredit_level_id(String credit_level_id) {
            this.credit_level_id = credit_level_id;
        }

        public String getCustom_tag() {
            return custom_tag;
        }

        public void setCustom_tag(String custom_tag) {
            this.custom_tag = custom_tag;
        }

        public String getEnjoyImg() {
            return enjoyImg;
        }

        public void setEnjoyImg(String enjoyImg) {
            this.enjoyImg = enjoyImg;
        }

        public String getEnjoy_ticking() {
            return enjoy_ticking;
        }

        public void setEnjoy_ticking(String enjoy_ticking) {
            this.enjoy_ticking = enjoy_ticking;
        }

        public ShareTime getEnjoy_time() {
            return enjoy_time;
        }

        public void setEnjoy_time(ShareTime enjoy_time) {
            this.enjoy_time = enjoy_time;
        }

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

        public String getImg_wh() {
            return img_wh;
        }

        public void setImg_wh(String img_wh) {
            this.img_wh = img_wh;
        }

        public String getIs_finish() {
            return is_finish;
        }

        public void setIs_finish(String is_finish) {
            this.is_finish = is_finish;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPlay_tours() {
            return play_tours;
        }

        public void setPlay_tours(String play_tours) {
            this.play_tours = play_tours;
        }

        public String getSpicfirst() {
            return spicfirst;
        }

        public void setSpicfirst(String spicfirst) {
            this.spicfirst = spicfirst;
        }

        public String getTicking_number() {
            return ticking_number;
        }

        public void setTicking_number(String ticking_number) {
            this.ticking_number = ticking_number;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getZizipeas() {
            return zizipeas;
        }

        public void setZizipeas(String zizipeas) {
            this.zizipeas = zizipeas;
        }
    }

    public class Enjoys {
        private int currPage;
        private List<ZiShangInfo> page;
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

        public List<ZiShangInfo> getPage() {
            return page;
        }

        public void setPage(List<ZiShangInfo> page) {
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

    public class ZiShangInfo {
        private String content;
        private String credit_level_id;
        private String custom_tag;
        private String enjoyImg;
        private String enjoy_ticking;
        private ShareTime enjoy_time;
        private String entityId;
        private String id;
        private String img_wh;
        private String is_finish;
        private String persistent;
        private String photo;
        private String play_tours;
        private String spicfirst;
        private String ticking_number;
        private String user_id;
        private String user_name;
        private String zizipeas;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCredit_level_id() {
            return credit_level_id;
        }

        public void setCredit_level_id(String credit_level_id) {
            this.credit_level_id = credit_level_id;
        }

        public String getCustom_tag() {
            return custom_tag;
        }

        public void setCustom_tag(String custom_tag) {
            this.custom_tag = custom_tag;
        }

        public String getEnjoyImg() {
            return enjoyImg;
        }

        public void setEnjoyImg(String enjoyImg) {
            this.enjoyImg = enjoyImg;
        }

        public String getEnjoy_ticking() {
            return enjoy_ticking;
        }

        public void setEnjoy_ticking(String enjoy_ticking) {
            this.enjoy_ticking = enjoy_ticking;
        }

        public ShareTime getEnjoy_time() {
            return enjoy_time;
        }

        public void setEnjoy_time(ShareTime enjoy_time) {
            this.enjoy_time = enjoy_time;
        }

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

        public String getImg_wh() {
            return img_wh;
        }

        public void setImg_wh(String img_wh) {
            this.img_wh = img_wh;
        }

        public String getIs_finish() {
            return is_finish;
        }

        public void setIs_finish(String is_finish) {
            this.is_finish = is_finish;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPlay_tours() {
            return play_tours;
        }

        public void setPlay_tours(String play_tours) {
            this.play_tours = play_tours;
        }

        public String getSpicfirst() {
            return spicfirst;
        }

        public void setSpicfirst(String spicfirst) {
            this.spicfirst = spicfirst;
        }

        public String getTicking_number() {
            return ticking_number;
        }

        public void setTicking_number(String ticking_number) {
            this.ticking_number = ticking_number;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getZizipeas() {
            return zizipeas;
        }

        public void setZizipeas(String zizipeas) {
            this.zizipeas = zizipeas;
        }
    }

}
