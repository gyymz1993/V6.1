package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.activity.SelectionActivity;
import com.lsjr.zizisteward.activity.TravelWebViewActivity;
import com.lsjr.zizisteward.activity.ZiZiPierreActivity;
import com.lsjr.zizisteward.activity.ZiZiWorldActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.mybetterandroid.wheel.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewHomeFragment extends Fragment implements OnClickListener {
    private View rootView;
    private FrameLayout mFrame_viewpage;
    private ViewPager home_viewpager;
    private CirclePageIndicator home_cpi;
    private int current;
    private List<Advertisements> adv_list;
    private List<NewRecommend> list_recommend = new ArrayList<NewRecommend>();
    private List<FoodData> list_food = new ArrayList<FoodData>();
    private List<PierreData> list_pieer = new ArrayList<PierreData>();
    private List<ActivityData> list_activity = new ArrayList<ActivityData>();
    private GridView mGridview_recommend, gridview_ziwei, gridview_zizi_pierre, gridview_zizi_activity;
    private ImageView mIv_ziwei, iv_activity;
    private DisplayMetrics mDm;
    private RelativeLayout mRecommend_change;
    private Intent mIntent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_new_home, null);
            mSwipe_fr = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_fr);
            mFrame_viewpage = (FrameLayout) rootView.findViewById(R.id.FrameLayout1);// 轮播图父布局
            home_viewpager = (ViewPager) rootView.findViewById(R.id.home_viewpager);// 轮播图
            home_cpi = (CirclePageIndicator) rootView.findViewById(R.id.home_cpi);// 小球点
            int ballradius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                    getResources().getDisplayMetrics());
            home_cpi.setRadius(ballradius);
            home_cpi.setFillColor(Color.parseColor("#F77F3E"));
            home_cpi.setPageColor(Color.parseColor("#D8D8D8"));
            home_cpi.setStrokeWidth(0);

            mDm = getResources().getDisplayMetrics();
            int widthPixels = mDm.widthPixels;

            int heightP = widthPixels * 5 / 8;
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mFrame_viewpage.getLayoutParams();
            linearParams.width = widthPixels;
            linearParams.height = heightP;
            mFrame_viewpage.setLayoutParams(linearParams);

            mGridview_recommend = (GridView) rootView.findViewById(R.id.gridview_recommend);// 孜孜推荐
            mRecommend_change = (RelativeLayout) rootView.findViewById(R.id.recommend_change);// 孜孜推荐刷新
            gridview_ziwei = (GridView) rootView.findViewById(R.id.gridview_ziwei);// 孜味天下
            gridview_zizi_pierre = (GridView) rootView.findViewById(R.id.gridview_zizi_pierre);// 孜孜臻品
            gridview_zizi_activity = (GridView) rootView.findViewById(R.id.gridview_zizi_activity);// 精选活动
            mIv_ziwei = (ImageView) rootView.findViewById(R.id.iv_ziwei);// 孜味天下图片
            iv_activity = (ImageView) rootView.findViewById(R.id.iv_activity);// 精选活动图片
            mRe_zizipireer_more = (RelativeLayout) rootView.findViewById(R.id.re_zizipireer_more);// 孜孜臻品更多

            int heightZi = widthPixels / 2;
            RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) mIv_ziwei.getLayoutParams();
            linearParams2.width = widthPixels;
            linearParams2.height = heightZi;
            mIv_ziwei.setLayoutParams(linearParams2);

            int heightAi = widthPixels / 2;
            RelativeLayout.LayoutParams linearParams3 = (RelativeLayout.LayoutParams) iv_activity.getLayoutParams();
            linearParams3.width = widthPixels;
            linearParams3.height = heightAi;
            iv_activity.setLayoutParams(linearParams3);

            getData();
            initListener();

        }
        return rootView;
    }

    private void getChangeData() {
        boolean state = PreferencesUtils.getBoolean(getContext(), "isLogin");
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "82");
        if (state == true) {
            map.put("name", App.getUserInfo().getName());
        } else {
            map.put("name", "");
        }
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("更新推荐列表" + result);
                ZiZiRecommend bean = GsonUtil.getInstance().fromJson(result, ZiZiRecommend.class);
                list_recommend = bean.getDiligent_recommend();
                RecommendAdapter recommend_adapter = new RecommendAdapter(getContext(), list_recommend);
                mGridview_recommend.setAdapter(recommend_adapter);
            }

        });
    }

    private void initListener() {
        mSwipe_fr.setColorSchemeResources(R.color.yellow);
        // mSwipe_fr.setProgressBackgroundColor(R.color.black_deep);
        // mSwipe_fr.setProgressBackgroundColorSchemeColor(R.color.black_deep);
        mSwipe_fr.setProgressBackgroundColorSchemeResource(R.color.black_deep);
        mSwipe_fr.setSize(SwipeRefreshLayout.LARGE);
        mSwipe_fr.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mSwipe_fr.setRefreshing(false);
                        getChangeData();
                        getData();
                    }
                }, 500);
            }
        });
        mRecommend_change.setOnClickListener(this);
        mIv_ziwei.setOnClickListener(this);
        mRe_zizipireer_more.setOnClickListener(this);
        iv_activity.setOnClickListener(this);
        home_viewpager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipe_fr.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipe_fr.setEnabled(true);
                        break;
                }

                return false;
            }
        });
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "24");
        map.put("currPage", "1");
        map.put("name", "");
        map.put("city_id", "420100");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("新首页" + result);
                NewHome bean = GsonUtil.getInstance().fromJson(result, NewHome.class);
                adv_list = bean.getAdvertisements();

                home_viewpager.setAdapter(pageAdapter);
                home_cpi.setViewPager(home_viewpager);
                home_cpi.setOnPageChangeListener(pageListener);
                pageListener.onPageSelected(0);

                // 孜孜推荐
                list_recommend = bean.getDiligent_recommend();
                int size = list_recommend.size();
                int length = 200;
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDm);
                float density = mDm.density;
                int gridviewWidth = (int) (size * (length + 4) * density);
                int itemWidth = (int) (length * density);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                mGridview_recommend.setLayoutParams(params); // 重点
                mGridview_recommend.setColumnWidth(itemWidth); // 重点
                mGridview_recommend.setHorizontalSpacing(15); // 间距
                mGridview_recommend.setStretchMode(GridView.NO_STRETCH);
                mGridview_recommend.setNumColumns(size); // 重点

                RecommendAdapter recommend_adapter = new RecommendAdapter(getContext(), list_recommend);
                mGridview_recommend.setAdapter(recommend_adapter);
                mGridview_recommend.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mIntent = new Intent(getContext(), HomeBrandDetail.class);
                        mIntent.putExtra("sid", list_recommend.get(position).getId());
                        mIntent.putExtra("mode", "shepin");
                        mIntent.putExtra("hava_dianpu", "no");
                        startActivity(mIntent);
                    }
                });

                // 孜味天下图片
                Glide.with(getContext()).load(HttpConfig.IMAGEHOST + bean.getShouImg().getDiligentFood())
                        .into(mIv_ziwei);
                list_food = bean.getHomePageMapData().getDiligentFood();

                int size2 = list_food.size();
                int length2 = 200;
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDm);
                int gridviewWidth2 = (int) (size2 * (length2 + 4) * density);
                int itemWidth2 = (int) (length2 * density);

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(gridviewWidth2,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                gridview_ziwei.setLayoutParams(params2); // 重点
                gridview_ziwei.setColumnWidth(itemWidth2); // 重点
                gridview_ziwei.setHorizontalSpacing(15); // 间距
                gridview_ziwei.setStretchMode(GridView.NO_STRETCH);
                gridview_ziwei.setNumColumns(size2); // 重点

                FoodAdapter food_adapter = new FoodAdapter(getContext(), list_food);
                gridview_ziwei.setAdapter(food_adapter);
                gridview_ziwei.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mIntent = new Intent(getContext(), HomeBrandDetail.class);
                        mIntent.putExtra("sid", list_food.get(position).getId());
                        startActivity(mIntent);
                    }
                });

                // 孜孜臻品
                list_pieer = bean.getHomePageMapData().getDiligentPierre();

                int size3 = list_pieer.size();
                int length3 = 200;
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDm);
                int gridviewWidth3 = (int) (size3 * (length3 + 4) * density);
                int itemWidth3 = (int) (length3 * density);

                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(gridviewWidth3,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                gridview_zizi_pierre.setLayoutParams(params3); // 重点
                gridview_zizi_pierre.setColumnWidth(itemWidth3); // 重点
                gridview_zizi_pierre.setHorizontalSpacing(15); // 间距
                gridview_zizi_pierre.setStretchMode(GridView.NO_STRETCH);
                gridview_zizi_pierre.setNumColumns(size3); // 重点

                PierreAdapter pierre_adapter = new PierreAdapter(getContext(), list_pieer);
                gridview_zizi_pierre.setAdapter(pierre_adapter);
                gridview_zizi_pierre.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mIntent = new Intent(getContext(), HomeBrandDetail.class);
                        mIntent.putExtra("sid", list_pieer.get(position).getId());
                        mIntent.putExtra("mode", "shepin");
                        mIntent.putExtra("hava_dianpu", "no");
                        startActivity(mIntent);
                    }
                });

                // 精选活动图片
                Glide.with(getContext()).load(HttpConfig.IMAGEHOST + bean.getShouImg().getDiligentActivity())
                        .into(iv_activity);

                list_activity = bean.getHomePageMapData().getDiligentActivity();

                int size4 = list_activity.size();
                int length4 = 200;
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDm);
                int gridviewWidth4 = (int) (size4 * (length4 + 4) * density);
                int itemWidth4 = (int) (length4 * density);

                LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(gridviewWidth4,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                gridview_zizi_activity.setLayoutParams(params4); // 重点
                gridview_zizi_activity.setColumnWidth(itemWidth4); // 重点
                gridview_zizi_activity.setHorizontalSpacing(15); // 间距
                gridview_zizi_activity.setStretchMode(GridView.NO_STRETCH);
                gridview_zizi_activity.setNumColumns(size4); // 重点

                ActivityAdapter activity_adapter = new ActivityAdapter(getContext(), list_activity);
                gridview_zizi_activity.setAdapter(activity_adapter);
                gridview_zizi_activity.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mIntent = new Intent(getContext(), HomeBrandDetail.class);
                        mIntent.putExtra("sid", list_activity.get(position).getId());
                        startActivity(mIntent);
                    }
                });
            }

        });

    }

    public class RecommendAdapter extends BaseAdapter {
        private Context context;
        private List<NewRecommend> list_recommend;
        private ViewHolder mHolder;

        public RecommendAdapter(Context context, List<NewRecommend> list_recommend) {
            this.context = context;
            this.list_recommend = list_recommend;
        }

        @Override
        public int getCount() {
            return list_recommend == null ? 0 : list_recommend.size();
        }

        @Override
        public Object getItem(int position) {
            return list_recommend.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_home_commend, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_recommend.get(position).getSpicfirst())
                    .into(mHolder.mIv_photo);
            mHolder.mTv_name.setText(list_recommend.get(position).getSkeyword());
            mHolder.mTv_content.setText(list_recommend.get(position).getSname());
            return convertView;
        }

    }

    public class FoodAdapter extends BaseAdapter {
        private Context context;
        private List<FoodData> list_food;
        private ViewHolder mHolder;

        public FoodAdapter(Context context, List<FoodData> list_food) {
            this.context = context;
            this.list_food = list_food;
        }

        @Override
        public int getCount() {
            return list_food == null ? 0 : list_food.size();
        }

        @Override
        public Object getItem(int position) {
            return list_food.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_home_food, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_food.get(position).getSpicfirst())
                    .into(mHolder.mIv_photo_ziwei);
            mHolder.mTv_name_food.setText(list_food.get(position).getSkeyword());
            mHolder.mTv_content_food.setText(list_food.get(position).getSname());
            return convertView;
        }

    }

    public class PierreAdapter extends BaseAdapter {
        private Context context;
        private List<PierreData> list_pieer;
        private ViewHolder mHolder;

        public PierreAdapter(Context context, List<PierreData> list_pieer) {
            this.context = context;
            this.list_pieer = list_pieer;
        }

        @Override
        public int getCount() {
            return list_pieer == null ? 0 : list_pieer.size();
        }

        @Override
        public Object getItem(int position) {
            return list_pieer.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_home_pirrre, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_pieer.get(position).getSpicfirst())
                    .into(mHolder.mIv_pierre);
            mHolder.mTv_pierre.setText(list_pieer.get(position).getSname());
            return convertView;
        }

    }

    public class ActivityAdapter extends BaseAdapter {
        private Context context;
        private List<ActivityData> list_activity;
        private ViewHolder mHolder;

        public ActivityAdapter(Context context, List<ActivityData> list_activity) {
            this.context = context;
            this.list_activity = list_activity;
        }

        @Override
        public int getCount() {
            return list_activity == null ? 0 : list_activity.size();
        }

        @Override
        public Object getItem(int position) {
            return list_activity.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_home_food, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_activity.get(position).getSpicfirst())
                    .into(mHolder.mIv_photo_ziwei);
            mHolder.mTv_name_food.setText(list_activity.get(position).getSkeyword());
            mHolder.mTv_content_food.setText(list_activity.get(position).getSname());
            return convertView;
        }

    }

    public class ViewHolder {
        private RoundImageView mIv_photo;
        private TextView mTv_name;
        private TextView mTv_content;
        private RelativeLayout mRe_ziwei;
        private RoundImageView mIv_photo_ziwei;
        private TextView mTv_name_food;
        private TextView mTv_content_food;
        private ImageView mIv_pierre;
        private TextView mTv_pierre;
        private LinearLayout re_beijing_bottom, re_beijing_top;

        public ViewHolder(View view) {
            // 孜孜推荐的
            mIv_photo = (RoundImageView) view.findViewById(R.id.iv_photo);
            mTv_name = (TextView) view.findViewById(R.id.tv_name);
            mTv_content = (TextView) view.findViewById(R.id.tv_content);
            // 孜味天下的
            mRe_ziwei = (RelativeLayout) view.findViewById(R.id.re_ziwei);
            mIv_photo_ziwei = (RoundImageView) view.findViewById(R.id.iv_photo_ziwei);
            mTv_name_food = (TextView) view.findViewById(R.id.tv_name_food);
            mTv_content_food = (TextView) view.findViewById(R.id.tv_content_food);
            // 孜孜臻品
            mIv_pierre = (ImageView) view.findViewById(R.id.iv_pierre);
            mTv_pierre = (TextView) view.findViewById(R.id.tv_pierre);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommend_change:
                getChangeData();
                break;
            case R.id.iv_ziwei:
                mIntent = new Intent(getContext(), ZiZiWorldActivity.class);
                startActivity(mIntent);
                break;
            case R.id.iv_activity:
                mIntent = new Intent(getContext(), SelectionActivity.class);
                startActivity(mIntent);
                break;
            case R.id.re_zizipireer_more:
                mIntent = new Intent(getContext(), ZiZiPierreActivity.class);
                startActivity(mIntent);
                break;

        }
    }

    private PagerAdapter pageAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return adv_list == null ? 0 : adv_list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = new ImageView(container.getContext());
            Glide.with(getContext()).load(HttpConfig.IMAGEHOST + adv_list.get(position).getImage_filename()).into(iv);
            iv.setScaleType(ScaleType.FIT_XY);
            container.addView(iv);
            iv.setOnClickListener(new OnClickListener() {
                private Intent mIntent;

                @Override
                public void onClick(View v) {
                    mIntent = new Intent(getContext(), TravelWebViewActivity.class);
                    mIntent.putExtra("url", adv_list.get(position).getUrl());
                    mIntent.putExtra("title", "home");
                    startActivity(mIntent);
                }
            });
            return iv;
        }
    };

    private OnPageChangeListener pageListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            current = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private RelativeLayout mRe_zizipireer_more;
    private SwipeRefreshLayout mSwipe_fr;

    public class ZiZiRecommend {
        private List<NewRecommend> diligent_recommend;
        private String error;
        private String msg;

        public List<NewRecommend> getDiligent_recommend() {
            return diligent_recommend;
        }

        public void setDiligent_recommend(List<NewRecommend> diligent_recommend) {
            this.diligent_recommend = diligent_recommend;
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

    public class NewHome {
        private List<Advertisements> advertisements;
        private List<NewRecommend> diligent_recommend;
        private NewData homePageMapData;
        private ShouImg shouImg;
        private String error;
        private String msg;

        public List<Advertisements> getAdvertisements() {
            return advertisements;
        }

        public void setAdvertisements(List<Advertisements> advertisements) {
            this.advertisements = advertisements;
        }

        public List<NewRecommend> getDiligent_recommend() {
            return diligent_recommend;
        }

        public void setDiligent_recommend(List<NewRecommend> diligent_recommend) {
            this.diligent_recommend = diligent_recommend;
        }

        public NewData getHomePageMapData() {
            return homePageMapData;
        }

        public void setHomePageMapData(List<NewData> homePageMapData) {
            this.homePageMapData = (NewData) homePageMapData;
        }

        public ShouImg getShouImg() {
            return shouImg;
        }

        public void setShouImg(ShouImg shouImg) {
            this.shouImg = shouImg;
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

    public class NewRecommend {
        private String id;
        private String skeyword;
        private String sname;
        private String spicfirst;

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
    }

    public class NewData {
        private List<ActivityData> diligentActivity;
        private List<FoodData> diligentFood;
        private List<PierreData> diligentPierre;

        public List<ActivityData> getDiligentActivity() {
            return diligentActivity;
        }

        public void setDiligentActivity(List<ActivityData> diligentActivity) {
            this.diligentActivity = diligentActivity;
        }

        public List<FoodData> getDiligentFood() {
            return diligentFood;
        }

        public void setDiligentFood(List<FoodData> diligentFood) {
            this.diligentFood = diligentFood;
        }

        public List<PierreData> getDiligentPierre() {
            return diligentPierre;
        }

        public void setDiligentPierre(List<PierreData> diligentPierre) {
            this.diligentPierre = diligentPierre;
        }
    }

    public class ActivityData {
        private String id;
        private String skeyword;
        private String sname;
        private String spicfirst;

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
    }

    public class FoodData {
        private String id;
        private String skeyword;
        private String sname;
        private String spicfirst;

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

    }

    public class PierreData {
        private String id;
        private String skeyword;
        private String sname;
        private String spicfirst;

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
    }

    public class ShouImg {
        private String diligentActivity;
        private String diligentFood;

        public String getDiligentActivity() {
            return diligentActivity;
        }

        public void setDiligentActivity(String diligentActivity) {
            this.diligentActivity = diligentActivity;
        }

        public String getDiligentFood() {
            return diligentFood;
        }

        public void setDiligentFood(String diligentFood) {
            this.diligentFood = diligentFood;
        }
    }

    public class Advertisements {
        private String entityId;
        private String id;
        private String image_filename;
        private String target;
        private String url;

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

        public String getImage_filename() {
            return image_filename;
        }

        public void setImage_filename(String image_filename) {
            this.image_filename = image_filename;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
