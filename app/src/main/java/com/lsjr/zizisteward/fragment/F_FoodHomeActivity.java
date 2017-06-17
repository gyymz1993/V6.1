package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.FoodHomeBean;
import com.lsjr.zizisteward.bean.FoodHomeBean.Cate;
import com.lsjr.zizisteward.bean.FoodHomeBean.Cate.Comment;
import com.lsjr.zizisteward.bean.FoodHomeBean.Catebanner;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.FoodDetailsActivity;
import com.lsjr.zizisteward.ly.activity.FoodToShareActivity;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.ly.activity.TalentShowActivity;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.mybetterandroid.wheel.widget.CirclePageIndicator;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class F_FoodHomeActivity extends Fragment implements OnClickListener {

    private View rootView;
    private MyListView mlv;
    private List<Cate> cates;
    private List<Catebanner> catebanner;
    private LinearLayout ll_screening;
    private ScrollView scrollView;
    private int height = 0;

    private FrameLayout fl;
    private ViewPager vp;
    private CirclePageIndicator cpi;
    private int skip = 0;
    private int space = 4;
    private int current;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (null == rootView) {

            rootView = inflater.from(getContext()).inflate(R.layout.f_food_home_activity, null);

            mlv = (MyListView) rootView.findViewById(R.id.mlv);
            scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
            ll_screening = (LinearLayout) rootView.findViewById(R.id.ll_screening);
            this.vp = (ViewPager) rootView.findViewById(R.id.vp);
            this.fl = (FrameLayout) rootView.findViewById(R.id.fl);
            this.cpi = (CirclePageIndicator) rootView.findViewById(R.id.cpi);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl.getLayoutParams(); // 取控件textView当前的布局参数
            height = (int) (wm.getDefaultDisplay().getWidth() / 1.6);
            linearParams.height = height;
            fl.setLayoutParams(linearParams);

            this.vp.setOffscreenPageLimit(1);
            this.vp.setPageMargin(5);

            int ballradius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                    getResources().getDisplayMetrics());

            this.cpi.setRadius(ballradius);
            this.cpi.setFillColor(Color.parseColor("#F77F3E"));
            this.cpi.setPageColor(Color.parseColor("#D8D8D8"));
            this.cpi.setStrokeWidth(0);

            mlv.setFocusable(false);
            getData();

            mlv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    boolean _isLogin = PreferencesUtils.getBoolean(getContext(), "isLogin");

                    if (_isLogin) {
						startActivity(new Intent(getContext(), FoodDetailsActivity.class).putExtra("id",
								cates.get(position).getId()));
                    } else {
						startActivity(new Intent(getContext(), NoteLoginActivity.class).putExtra("personal", "health_vp"));
                    }
                }
            });

            ll_screening.setOnClickListener(this);
        }

        return rootView;
    }

    private class FFHAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<Cate> cate;

        public FFHAdapter(Context context, List<Cate> cate) {
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

                convertView = LayoutInflater.from(context).inflate(R.layout.f_food_home_activity_item, null);

                view = new ViewHolder(convertView);

                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.iv.getLayoutParams(); // 取控件textView当前的布局参数
                linearParams.height = height;
                view.iv.setLayoutParams(linearParams);

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

            view.tv_recommended.setText(
                    cate.get(position).getExpert().equals("0") ? "" : cate.get(position).getExpert() + "名美食达人推荐  >");

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
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(0).getPhoto())
                                    .into(view.riv_head);
                        } else if (i == 1) {
                            view.riv_head.setVisibility(View.VISIBLE);
                            view.riv_head_one.setVisibility(View.VISIBLE);
                            view.riv_head_two.setVisibility(View.GONE);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(0).getPhoto())
                                    .into(view.riv_head);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(1).getPhoto())
                                    .into(view.riv_head_one);

                        } else if (i == 2) {
                            view.riv_head.setVisibility(View.VISIBLE);
                            view.riv_head_one.setVisibility(View.VISIBLE);
                            view.riv_head_two.setVisibility(View.VISIBLE);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(0).getPhoto())
                                    .into(view.riv_head);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(1).getPhoto())
                                    .into(view.riv_head_one);
                            Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(2).getPhoto())
                                    .into(view.riv_head_two);
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
                this.iv = (ImageView) v.findViewById(R.id.iv);
                this.tv_title = (TextView) v.findViewById(R.id.tv_title);
                this.tv_content = (TextView) v.findViewById(R.id.tv_content);
                this.riv_head = (RoundImageView) v.findViewById(R.id.riv_head);
                this.tv_sentiment = (TextView) v.findViewById(R.id.tv_sentiment);
                this.tv_recommended = (TextView) v.findViewById(R.id.tv_recommended);
                this.tv_positioning = (TextView) v.findViewById(R.id.tv_positioning);
                this.riv_head_one = (RoundImageView) v.findViewById(R.id.riv_head_one);
                this.riv_head_two = (RoundImageView) v.findViewById(R.id.riv_head_two);
            }
        }
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "411");
        map.put("currPage", "1");
        map.put("ad_city_id", "420100");

        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println("美食 " + result);

                FoodHomeBean fhBean = new Gson().fromJson(result, FoodHomeBean.class);

                cates = fhBean.getCate();
                catebanner = fhBean.getCatebanner();

                vp.setCurrentItem(catebanner.size());
                vp.setAdapter(pageAdapter);
                cpi.setViewPager(vp);
                cpi.setOnPageChangeListener(pageListener);
                pageListener.onPageSelected(skip);

                if (null == catebanner || catebanner.size() == 1) {
                    cpi.setVisibility(View.VISIBLE);
                }

                FFHAdapter fAdapter = new FFHAdapter(getContext(), cates);

                mlv.setAdapter(fAdapter);

                scrollView.fullScroll(ScrollView.FOCUS_UP);

                mlv.setSelectionAfterHeaderView();
            }

        });
    }

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

    private PagerAdapter pageAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return catebanner == null ? 0 : catebanner.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            Picasso.with(mContext).load(HttpConfig.IMAGEHOST + catebanner.get(position).getSpic()).into(iv);

            iv.setScaleType(ScaleType.FIT_XY);
            container.addView(iv);

            iv.setTag(position);
            iv.setOnClickListener(new OnClickListener() {
                private Intent mIntent;

                @Override
                public void onClick(View v) {
                    if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                        int pos = (int) v.getTag();
						startActivity(new Intent(getContext(), FoodDetailsActivity.class).putExtra("id",
								catebanner.get(pos).getId()));
                    } else {
						startActivity(new Intent(getContext(), NoteLoginActivity.class).putExtra("personal", "health_vp"));
                    }
                }
            });
            return iv;
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_screening:
                boolean _isLogin = PreferencesUtils.getBoolean(getContext(), "isLogin");

                if (_isLogin) {
				startActivity(new Intent(getContext(), FoodToShareActivity.class));
                } else {
				startActivity(new Intent(getContext(), NoteLoginActivity.class).putExtra("personal", "health_vp"));
                }

                break;
        }
    }
}
