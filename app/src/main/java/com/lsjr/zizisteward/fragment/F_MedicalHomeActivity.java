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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.MedicalHomeBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.FunctionalDevelopmentInterfaceActivity;
import com.lsjr.zizisteward.ly.activity.MedicalActivity;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.PhysicaDetailsActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.mybetterandroid.wheel.widget.CirclePageIndicator;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class F_MedicalHomeActivity extends Fragment implements OnClickListener {

    final String TAG = "MHTAG";
    private View rootView;
    private FrameLayout fl;
    private ViewPager vp;
    private CirclePageIndicator cpi;
    MedicalHomeBean homeBean;
    Context mContext;
    private int skip = 0;
    private int space = 4;
    private int current;

    /**
     * 体检推荐
     */
    private List<MedicalHomeBean.Amedicalr> Amedicalr = null;
    /**
     * 名医推荐
     */
    private List<MedicalHomeBean.Famousdoctor> Famousdoctor = null;
    /**
     * 轮播图
     */
    private List<MedicalHomeBean.Advertisements> advertisements = null;

    private ImageView iv_banner;
    /**
     * 医疗美容
     */
    private LinearLayout ll_health_beauty;
    /**
     * 新特药品
     */
    private LinearLayout ll_new_drugs;
    /**
     * 在线名医
     */
    private LinearLayout ll_online_doctors;
    /**
     * 中医养生
     */
    private LinearLayout ll_chinese_medicine_good_health;
    /**
     * 健康体检
     */
    private TextView tv_hcu;
    /**
     * 医院挂号
     */
    private TextView tv_hr;

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

            this.rootView = inflater.from(getContext()).inflate(R.layout.f_medical_home_activity, null);

            this.tv_hr = (TextView) rootView.findViewById(R.id.tv_hr);
            this.tv_hcu = (TextView) rootView.findViewById(R.id.tv_hcu);
            this.vp = (ViewPager) rootView.findViewById(R.id.vp);
            this.fl = (FrameLayout) rootView.findViewById(R.id.fl);
            this.iv_banner = (ImageView) rootView.findViewById(R.id.iv_banner);
            this.cpi = (CirclePageIndicator) rootView.findViewById(R.id.cpi);

            this.ll_health_beauty = (LinearLayout) rootView.findViewById(R.id.ll_health_beauty);
            this.ll_new_drugs = (LinearLayout) rootView.findViewById(R.id.ll_new_drugs);
            this.ll_online_doctors = (LinearLayout) rootView.findViewById(R.id.ll_online_doctors);
            this.ll_chinese_medicine_good_health = (LinearLayout) rootView
                    .findViewById(R.id.ll_chinese_medicine_good_health);

            this.tv_hr.setOnClickListener(this);
            this.tv_hcu.setOnClickListener(this);
            this.ll_health_beauty.setOnClickListener(this);
            this.ll_new_drugs.setOnClickListener(this);
            this.ll_online_doctors.setOnClickListener(this);
            this.ll_chinese_medicine_good_health.setOnClickListener(this);

            this.vp.setOffscreenPageLimit(1);
            this.vp.setPageMargin(5);

            int ballradius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                    getResources().getDisplayMetrics());

            this.cpi.setRadius(ballradius);
            this.cpi.setFillColor(Color.parseColor("#F77F3E"));
            this.cpi.setPageColor(Color.parseColor("#D8D8D8"));
            this.cpi.setStrokeWidth(0);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) iv_banner.getLayoutParams();
            int height = (int) (wm.getDefaultDisplay().getWidth() / 2);
            linearParams.height = height;

            this.getData();
        }

        return rootView;
    }

    private void getData() {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "301");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                homeBean = new Gson().fromJson(result, MedicalHomeBean.class);

                advertisements = homeBean.getAdvertisements();
                Amedicalr = homeBean.getAmedicalr();
                Famousdoctor = homeBean.getFamousdoctor();

                vp.setCurrentItem(Amedicalr.size());
                vp.setAdapter(pageAdapter);
                cpi.setViewPager(vp);
                cpi.setOnPageChangeListener(pageListener);
                pageListener.onPageSelected(skip);

                if (null == advertisements || advertisements.size() == 1) {
                    cpi.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    private PagerAdapter pageAdapter = new PagerAdapter() {

        private TextView tv_record_title;
        private TextView tv_record_name;
        private TextView tv_record_price;
        private RoundImageView iv;
        private LinearLayout ll_parent;

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            if (homeBean == null) {
                Log.i(TAG, "homebean is null");
                return 0;
            } else if (homeBean.getAmedicalr() == null || homeBean.getAmedicalr().size() < 0) {
                Log.i(TAG, "homebean lenth zero");
                return 0;
            }
            Log.i(TAG, "come in=" + homeBean.getAmedicalr().size());
            return homeBean.getAmedicalr().size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.f_medical_item, container, false);

            ll_parent = (LinearLayout) view.findViewById(R.id.ll_parent);

            iv = (RoundImageView) view.findViewById(R.id.iv);

            tv_record_title = (TextView) view.findViewById(R.id.tv_record_title);

            tv_record_name = (TextView) view.findViewById(R.id.tv_record_name);

            tv_record_price = (TextView) view.findViewById(R.id.tv_record_price);

            iv.setScaleType(ScaleType.FIT_XY);

            if (null == Amedicalr || Amedicalr.size() == 0) {
                iv.setImageResource(R.drawable.health_pic);
            } else {
                tv_record_title.setText(Amedicalr.get(position).getSname());
                tv_record_name.setText(Amedicalr.get(position).getBname());
                tv_record_price.setText("¥ " + Amedicalr.get(position).getSprice());
                Picasso.with(getContext()).load(HttpConfig.IMAGEHOST + Amedicalr.get(position).getSpicfirst()).into(iv);
            }

            ll_parent.setTag(position);
            ll_parent.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                        int pos = (int) v.getTag();
                        startActivity(new Intent(getContext(), PhysicaDetailsActivity.class)
                                .putExtra("id", Amedicalr.get(pos).getId())
                                .putExtra("title", Amedicalr.get(pos).getSname()));
                    } else {
                        startActivity(new Intent(getContext(), NoteLoginActivity.class).putExtra("personal", "health_vp"));
                    }
                }
            });
            container.addView(view);
            return view;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_health_beauty:
                startActivity(new Intent(getContext(), FunctionalDevelopmentInterfaceActivity.class));
                break;

            case R.id.ll_new_drugs:
                startActivity(new Intent(getContext(), FunctionalDevelopmentInterfaceActivity.class));
                break;

            case R.id.ll_online_doctors:
                startActivity(new Intent(getContext(), FunctionalDevelopmentInterfaceActivity.class));
                break;

            case R.id.ll_chinese_medicine_good_health:
                startActivity(new Intent(getContext(), FunctionalDevelopmentInterfaceActivity.class));
                break;

            case R.id.tv_hcu:
                if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                    startActivity(new Intent(getContext(), MedicalActivity.class));
                } else {
                    startActivity(new Intent(getContext(), NoteLoginActivity.class).putExtra("personal", "health"));
                }

                break;

            case R.id.tv_hr:
                startActivity(new Intent(getContext(), FunctionalDevelopmentInterfaceActivity.class));
                break;
        }
    }
}
