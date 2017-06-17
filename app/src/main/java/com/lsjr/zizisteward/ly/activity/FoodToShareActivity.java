package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.R.id;
import com.lsjr.zizisteward.bean.FoodHomeBean;
import com.lsjr.zizisteward.bean.FoodHomeBean.Cate;
import com.lsjr.zizisteward.bean.FoodHomeBean.Cate.Comment;
import com.lsjr.zizisteward.bean.FoodHomeBean.Count;
import com.lsjr.zizisteward.bean.FoodHomeBean.Style;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 美食分享
 */
public class FoodToShareActivity extends Activity implements OnClickListener {

    /**
     * 返回键
     */
    private LinearLayout ll_back;
    /**
     * 地区父布局
     */
    private LinearLayout ll_region;
    /**
     * 地区文本
     */
    private TextView tv_region;
    /**
     * 筛选父布局
     */
    private LinearLayout ll_screening;
    /**
     * 展示列表
     */
    private MyListView mlv;
    /**
     * 刷新布局
     */
    private PullToRefreshLayout ptrl;
    /**
     * 加载更多布局
     */
    private LinearLayout ll;
    private FTSAdpter adapter;

    /**
     * 主要地理位置
     */
    private PopupWindow pw_main_location;

    /**
     * 全城父布局
     */
    private LinearLayout ll_s_region;
    /**
     * TextView
     */
    private TextView tv_s_region;
    /**
     * 箭头
     */
    private ImageView iv_s_region;
    private PopupWindow pw_region;

    /**
     * 智能排序父布局
     */
    private LinearLayout ll_s_sorting;
    /**
     * TextView
     */
    private TextView tv_s_sorting;
    /**
     * 箭头
     */
    private ImageView iv_s_sorting;
    private PopupWindow pw_sorting;

    /**
     * 筛选父布局
     */
    private LinearLayout ll_s_screening;
    /**
     * TextView
     */
    private TextView tv_s_screening;
    /**
     * 箭头
     */
    private ImageView iv_s_screening;
    private PopupWindow pw_screening;

    private View view;
    private int _count = 8;
    private int pageNum = 1;

    private int p_c = 0;
    private int p_t = 0;
    private int _p_c = 0;
    private int _p_t = 0;
    private int ad_areas_id = 0;
    private int popularitytype = 0;

    private List<Cate> cate;
    private List<Count> count;
    private List<Style> style;
    private ConsumptionAdapter cAdapter;
    private TypeAdapter tAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.food_to_share_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.view = super.findViewById(id.view);
        this.ll = (LinearLayout) super.findViewById(id.ll);
        this.mlv = (MyListView) super.findViewById(id.mlv);
        this.tv_region = (TextView) super.findViewById(id.tv_region);
        this.ll_back = (LinearLayout) super.findViewById(id.ll_back);
        this.ll_region = (LinearLayout) super.findViewById(id.ll_region);
        this.ll_s_region = (LinearLayout) super.findViewById(id.ll_s_region);
        this.ptrl = (PullToRefreshLayout) super.findViewById(id.refresh_view);
        this.ll_screening = (LinearLayout) super.findViewById(id.ll_screening);
        this.ll_s_sorting = (LinearLayout) super.findViewById(id.ll_s_sorting);
        this.ll_s_screening = (LinearLayout) super.findViewById(id.ll_s_screening);

        this.tv_s_region = (TextView) super.findViewById(id.tv_s_region);
        this.tv_s_sorting = (TextView) super.findViewById(id.tv_s_sorting);
        this.tv_s_screening = (TextView) super.findViewById(id.tv_s_screening);

        this.iv_s_region = (ImageView) super.findViewById(id.iv_s_region);
        this.iv_s_sorting = (ImageView) super.findViewById(id.iv_s_sorting);
        this.iv_s_screening = (ImageView) super.findViewById(id.iv_s_screening);

        this.ll_back.setOnClickListener(this);
        this.ll_region.setOnClickListener(this);
        this.ll_s_region.setOnClickListener(this);
        this.ll_s_sorting.setOnClickListener(this);
        this.ll_s_screening.setOnClickListener(this);

        this.ptrl.setOnRefreshListener(new MyListener());

        this.getFirstData();

        this.mlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(FoodToShareActivity.this, FoodDetailsActivity.class)
                        .putExtra("id", cate.get(position).getId()));
            }
        });
    }

    private class MyListener implements
            PullToRefreshLayout.OnRefreshListener {

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

    private void getFirstData() {
        CustomDialogUtils.startCustomProgressDialog(FoodToShareActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "295");
        map.put("currPage", "1");
        map.put("ad_city_id", "420100");
        map.put("ad_areas_id", "");
        map.put("popularitytype", "");
        map.put("cate_price", "");
        map.put("cate_type", "");
        new HttpClientGet(FoodToShareActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(FoodToShareActivity.this);

                FoodHomeBean fhBean = new Gson().fromJson(result, FoodHomeBean.class);

                cate = fhBean.getCate();

                count = fhBean.getCount();

                style = fhBean.getStyle();

                Style _style = new Style();
                _style.setTname("不限");
                style.add(0, _style);

                adapter = new FTSAdpter(FoodToShareActivity.this, cate);

                mlv.setAdapter(adapter);

                if (null != cate && cate.size() > 7) {
                    ll.setVisibility(View.VISIBLE);
                } else {
                    ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(FoodToShareActivity.this);
                super.onFailure(myError);
            }
        });
    }

    private void getData(final int space) {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "295");
        map.put("currPage", String.valueOf(pageNum));
        map.put("ad_city_id", "420100");
        map.put("ad_areas_id", ad_areas_id == 0 ? "" : String.valueOf(ad_areas_id));
        map.put("popularitytype", popularitytype == 0 ? "" : String.valueOf(popularitytype));
        map.put("cate_price", p_c == 0 ? "" : String.valueOf(p_c));
        map.put("cate_type", p_t == 0 ? "" : String.valueOf(p_t));

        new HttpClientGet(FoodToShareActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {

                        System.out.println(result);

                        FoodHomeBean fhBean = new Gson().fromJson(result, FoodHomeBean.class);

                        switch (space) {
                            case 0:
                                // 刷新
                                adapter = null;
                                cate = new ArrayList<>();

                                cate = fhBean.getCate();

                                adapter = new FTSAdpter(FoodToShareActivity.this, cate);

                                mlv.setAdapter(adapter);

                                if (null != cate && cate.size() > 7) {
                                    ll.setVisibility(View.VISIBLE);
                                } else {
                                    ll.setVisibility(View.GONE);
                                }

                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                                break;

                            case 1:
                                // 加载更多

                                List<Cate> _cate = new ArrayList<>();

                                _cate = fhBean.getCate();

                                if (null != _cate) {
                                    cate.addAll(_cate);
                                    adapter = new FTSAdpter(FoodToShareActivity.this, cate);
                                    mlv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                    if (_cate.size() > 7) {
                                        ll.setVisibility(View.VISIBLE);
                                    } else {
                                        ll.setVisibility(View.GONE);
                                    }
                                }

                                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
    }

    private class FTSAdpter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<Cate> cate;

        public FTSAdpter(Context context, List<Cate> cate) {
            this.context = context;
            this.cate = cate;
        }

        @Override
        public int getCount() {
            return null == cate ? 0 : cate.size();
        }

        @Override
        public Object getItem(int position) {
            return cate.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.food_to_share_activity_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();
            }

            String[] pic = cate.get(position).getSpic().split(",");

            if (null != pic && pic.length > 0) {
                Glide.with(context).load(HttpConfig.IMAGEHOST + pic[0]).into(view.iv);
            }

            view.tv_title.setText(cate.get(position).getSname());

            view.tv_positioning.setText(cate.get(position).getAreas_name());

            view.tv_recommended.setText(cate.get(position).getExpert().equals("0") ? "" : cate.get(position).getExpert() + "名美食达人推荐  >");

            view.tv_sentiment.setText(cate.get(position).getPopularity() + "人气");

            view.tv_content.setText(cate.get(position).getSinfo());

            List<Comment> comments = cate.get(position).getComment();

            if (null != comments) {

                for (int i = 0; i < comments.size(); i++) {

                    if (i < 3) {
                        if (i == 0) {
                            view.riv_head.setVisibility(View.VISIBLE);
                            view.riv_head_one.setVisibility(View.GONE);
                            view.riv_head_two.setVisibility(View.GONE);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(0).getPhoto()).into(view.riv_head);
                        } else if (i == 1) {
                            view.riv_head.setVisibility(View.VISIBLE);
                            view.riv_head_one.setVisibility(View.VISIBLE);
                            view.riv_head_two.setVisibility(View.GONE);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(0).getPhoto()).into(view.riv_head);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(1).getPhoto()).into(view.riv_head_one);

                        } else if (i == 2) {
                            view.riv_head.setVisibility(View.VISIBLE);
                            view.riv_head_one.setVisibility(View.VISIBLE);
                            view.riv_head_two.setVisibility(View.VISIBLE);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(0).getPhoto()).into(view.riv_head);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(1).getPhoto()).into(view.riv_head_one);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(2).getPhoto()).into(view.riv_head_two);
                        }
                    } else {
                        continue;
                    }
                }

            } else {
                view.riv_head.setVisibility(View.INVISIBLE);
                view.riv_head_one.setVisibility(View.INVISIBLE);
                view.riv_head_two.setVisibility(View.INVISIBLE);
            }

            view.tv_recommended.setTag(position);
            view.tv_recommended.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    startActivity(new Intent(context, TalentShowActivity.class).putExtra("id", cate.get(pos).getId()));
                }
            });

            return convertView;
        }

        private class ViewHolder {
            /**
             * 图片
             */
            private ImageView iv;
            /**
             * 标题
             */
            private TextView tv_title;
            /**
             * 定位
             */
            private TextView tv_positioning;
            /**
             * 达人头像
             */
            private RoundImageView riv_head;
            private RoundImageView riv_head_one;
            private RoundImageView riv_head_two;
            /**
             * 达人推荐
             */
            private TextView tv_recommended;
            /**
             * 人气
             */
            private TextView tv_sentiment;
            /**
             * 内容
             */
            private TextView tv_content;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(id.iv);
                this.tv_title = (TextView) v.findViewById(id.tv_title);
                this.tv_content = (TextView) v.findViewById(id.tv_content);
                this.riv_head = (RoundImageView) v.findViewById(id.riv_head);
                this.tv_sentiment = (TextView) v
                        .findViewById(id.tv_sentiment);
                this.tv_recommended = (TextView) v
                        .findViewById(id.tv_recommended);
                this.tv_positioning = (TextView) v
                        .findViewById(id.tv_positioning);
                this.riv_head_one = (RoundImageView) v
                        .findViewById(id.riv_head_one);
                this.riv_head_two = (RoundImageView) v
                        .findViewById(id.riv_head_two);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case id.ll_back:
                finish();
                break;

            case id.ll_region:
                // 地区

                if (null != pw_main_location) {
                    if (pw_main_location.isShowing()) {
                        pw_main_location.dismiss();
                    } else {
                        showpw_main_location(ll_region);
                    }
                } else {
                    showpw_main_location(ll_region);
                }

                break;

            case id.ll_s_region:
                // 全城

                if (null != pw_region) {
                    if (pw_region.isShowing()) {
                        pw_region.dismiss();
                    } else {
                        showpw_s_region(view);
                    }
                } else {
                    showpw_s_region(view);
                }

                break;

            case id.ll_s_sorting:
                // 智能排序

                if (null != pw_sorting) {
                    if (pw_sorting.isShowing()) {
                        pw_sorting.dismiss();
                    } else {
                        showpw_s_sorting(view);
                    }
                } else {
                    showpw_s_sorting(view);
                }

                break;

            case id.ll_s_screening:
                // 筛选

                if (null != pw_screening) {
                    if (pw_screening.isShowing()) {
                        pw_screening.dismiss();
                    } else {
                        showpw_s_screening(view);
                    }
                } else {
                    showpw_s_screening(view);
                }

                break;
        }
    }

    private void showpw_s_screening(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(FoodToShareActivity.this)
                .inflate(R.layout.showpw_s_screening, null);
        // 设置按钮的点击事件
        GridView mgv_consumption = (GridView) contentView
                .findViewById(id.mgv_consumption);

        GridView mgv_type = (GridView) contentView.findViewById(id.mgv_type);

        TextView tv_reset = (TextView) contentView.findViewById(id.tv_reset);
        TextView tv_sure = (TextView) contentView.findViewById(id.tv_sure);

        final String[] consumption = {"不限", "100-200", "200-300", "300-400",
                "400以上"};

        cAdapter = new ConsumptionAdapter(
                FoodToShareActivity.this, consumption);
        mgv_consumption.setAdapter(cAdapter);

        tAdapter = new TypeAdapter(FoodToShareActivity.this, style);
        mgv_type.setAdapter(tAdapter);

        tv_reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                p_c = 0;
                p_t = 0;
                _p_c = 0;
                _p_t = 0;
                cAdapter.notifyDataSetChanged();
                tAdapter.notifyDataSetChanged();
            }
        });

        tv_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                p_c = _p_c;
                p_t = _p_t;
                getData(0);
                pw_screening.dismiss();
            }
        });

        mgv_consumption.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                _p_c = position;
                cAdapter.notifyDataSetChanged();
            }
        });

        mgv_type.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                _p_t = position;
                tAdapter.notifyDataSetChanged();
            }
        });

        pw_screening = new PopupWindow(contentView,
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

        pw_screening.setTouchable(true);

        backgroundAlpha(1f);

        pw_screening.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 pw_main_location的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置pw_main_location的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        pw_screening.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        pw_screening.showAsDropDown(view);
    }

    private void showpw_s_sorting(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(FoodToShareActivity.this)
                .inflate(R.layout.showpw_s_sorting, null);
        // 设置按钮的点击事件
        ListView mlv_sorting = (ListView) contentView
                .findViewById(id.mlv_sorting);

        final String[] path = {"智能排序", "人气最高", "达人推荐"};

        SortingAdapter sAdapter = new SortingAdapter(FoodToShareActivity.this,
                path);
        mlv_sorting.setAdapter(sAdapter);

        mlv_sorting.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_s_sorting.setText(path[position]);
                popularitytype = position;
                getData(0);
                pw_sorting.dismiss();
            }
        });

        pw_sorting = new PopupWindow(contentView,
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

        pw_sorting.setTouchable(true);

        backgroundAlpha(1f);

        pw_sorting.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 pw_main_location的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置pw_main_location的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        pw_sorting.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        pw_sorting.showAsDropDown(view);
    }

    private void showpw_s_region(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(FoodToShareActivity.this)
                .inflate(R.layout.showpw_s_region, null);
        // 设置按钮的点击事件
        ListView mlv_region = (ListView) contentView
                .findViewById(id.mlv_region);

        //final String[] path = { "全城","洪山区", "汉阳区", "江汉区", "东西湖区", "青山区" };

        RegionAdapter rAdapter = new RegionAdapter(FoodToShareActivity.this, count);
        mlv_region.setAdapter(rAdapter);

        mlv_region.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_s_region.setText(count.get(position).getAreas_name());
                if (count.get(position).getAreas_name().equals("全城")) {
                    ad_areas_id = 0;
                } else {
                    ad_areas_id = Integer.valueOf(count.get(position).getAd_areas_id());
                }
                getData(0);
                pw_region.dismiss();
            }
        });

        pw_region = new PopupWindow(contentView,
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

        pw_region.setTouchable(true);

        backgroundAlpha(1f);

        pw_region.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 pw_main_location的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置pw_main_location的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        pw_region.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        pw_region.showAsDropDown(view);
    }

    private void showpw_main_location(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(FoodToShareActivity.this)
                .inflate(R.layout.showpw_main_location, null);
        // 设置按钮的点击事件
        ListView mlv_location = (ListView) contentView
                .findViewById(id.mlv_location);

        final String[] path = {"武汉"};
        LocationAdapter lAdapter = new LocationAdapter(
                FoodToShareActivity.this, path);
        mlv_location.setAdapter(lAdapter);

        mlv_location.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_region.setText(path[position]);
                pw_main_location.dismiss();
            }
        });

        pw_main_location = new PopupWindow(contentView,
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

        pw_main_location.setTouchable(true);

        backgroundAlpha(1f);

        pw_main_location.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 pw_main_location的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置pw_main_location的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        pw_main_location.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        pw_main_location.showAsDropDown(view);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    private class RegionAdapter extends BaseAdapter {

        private Context context;
        private List<Count> count;
        private ViewHolder view;

        public RegionAdapter(Context context, List<Count> count) {
            this.context = context;
            this.count = count;
        }

        @Override
        public int getCount() {
            return null == count ? 0 : count.size();
        }

        @Override
        public Object getItem(int position) {
            return count.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.showpw_s_region_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            view.tv.setText(count.get(position).getAreas_name());

            if (count.get(position).getAreas_name().equals("全城")) {
                view.tv_distance.setText("");
            } else {
                view.tv_distance.setText(count.get(position).getCount());
            }

            return convertView;
        }

        private class ViewHolder {
            private TextView tv;
            private TextView tv_distance;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(id.tv);
                this.tv_distance = (TextView) v.findViewById(id.tv_distance);
            }
        }
    }

    private class SortingAdapter extends BaseAdapter {

        private Context context;
        private String[] path;
        private ViewHolder view;

        public SortingAdapter(Context context, String[] path) {
            this.context = context;
            this.path = path;
        }

        @Override
        public int getCount() {
            return null == path ? 0 : path.length;
        }

        @Override
        public Object getItem(int position) {
            return path[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.showpw_s_sorting_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            view.tv.setText(path[position]);

            return convertView;
        }

        private class ViewHolder {
            private TextView tv;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(id.tv);
            }
        }
    }

    private class LocationAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private String[] path;

        public LocationAdapter(Context context, String[] path) {
            this.context = context;
            this.path = path;
        }

        @Override
        public int getCount() {
            return null == path ? 0 : path.length;
        }

        @Override
        public Object getItem(int position) {
            return path[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.showpw_main_location_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            view.tv.setText(path[position]);

            if (tv_region.getText().toString().equals(path[position])) {
                view.tv.setTextColor(Color.parseColor("#AE9171"));
            } else {
                view.tv.setTextColor(Color.parseColor("#333333"));
            }

            return convertView;
        }

        private class ViewHolder {
            private TextView tv;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(id.tv);
            }
        }
    }

    private class ConsumptionAdapter extends BaseAdapter {

        private Context context;
        private String[] consumption;
        private ViewHolder view;

        public ConsumptionAdapter(Context context, String[] consumption) {
            this.consumption = consumption;
            this.context = context;
        }

        @Override
        public int getCount() {
            return null == consumption ? 0 : consumption.length;
        }

        @Override
        public Object getItem(int position) {
            return consumption[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.consumption_adapter_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            if (_p_c == position) {
                view.tv.setTextColor(Color.parseColor("#B79771"));
                view.tv.setBackgroundResource(R.drawable.icon_select_box_true);
            } else {
                view.tv.setTextColor(Color.parseColor("#888888"));
                view.tv.setBackgroundResource(R.drawable.icon_select_box_false);
            }

            view.tv.setText(consumption[position]);

            return convertView;
        }

        private class ViewHolder {

            private TextView tv;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(id.tv);
            }
        }
    }

    private class TypeAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<Style> style;

        public TypeAdapter(Context context, List<Style> style) {
            this.context = context;
            this.style = style;
        }

        @Override
        public int getCount() {
            return null == style ? 0 : style.size();
        }

        @Override
        public Object getItem(int position) {
            return style.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.consumption_adapter_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            if (_p_t == position) {
                view.tv.setTextColor(Color.parseColor("#B79771"));
                view.tv.setBackgroundResource(R.drawable.icon_select_box_true);
            } else {
                view.tv.setTextColor(Color.parseColor("#888888"));
                view.tv.setBackgroundResource(R.drawable.icon_select_box_false);
            }

            view.tv.setText(style.get(position).getTname());

            return convertView;
        }

        private class ViewHolder {
            private TextView tv;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(id.tv);
            }
        }
    }
}
