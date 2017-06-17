package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
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

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.BusinessPlanActivity;
import com.lsjr.zizisteward.activity.CarServiceActivity;
import com.lsjr.zizisteward.activity.CustomTravelActivity;
import com.lsjr.zizisteward.activity.OrderTicketServiceActivity;
import com.lsjr.zizisteward.activity.TravelDeepActivity;
import com.lsjr.zizisteward.common.activtiy.YuDingInfoActivity;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.mybetterandroid.wheel.widget.CirclePageIndicator;

/**
 * 出行专家
 */
public class F_TravelExpertsActivity extends Fragment implements OnClickListener {

    private View rootView;
    private FrameLayout fl;
    private ViewPager vp;
    private CirclePageIndicator cpi;
    private Context mContext;
    private int skip = 0;
    private int current;

    private ImageView iv_banner;
    /**
     * 订票服务
     */
    private LinearLayout ll_booking;
    /**
     * 专车服务
     */
    private LinearLayout ll_car;
    /**
     * 酒店预定
     */
    private LinearLayout ll_reservation;
    /**
     * 深度旅游
     */
    private LinearLayout ll_tourism;

    private int[] images = {R.drawable.icon_travel_car, R.drawable.icon_travel_yacht, R.drawable.icon_travel_plane};
    private Intent mIntent;
    private TextView mTv_custom;

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

    @SuppressWarnings("static-access")
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (null == rootView) {
            rootView = inflater.from(getContext()).inflate(R.layout.f_travel_experts_activity, null);

            this.vp = (ViewPager) rootView.findViewById(R.id.vp);
            this.fl = (FrameLayout) rootView.findViewById(R.id.fl);
            this.cpi = (CirclePageIndicator) rootView.findViewById(R.id.cpi);
            this.iv_banner = (ImageView) rootView.findViewById(R.id.iv_banner);
            this.mTv_custom = (TextView) rootView.findViewById(R.id.tv_custom);
            this.ll_booking = (LinearLayout) rootView.findViewById(R.id.ll_booking);
            this.ll_car = (LinearLayout) rootView.findViewById(R.id.ll_car);
            this.ll_reservation = (LinearLayout) rootView.findViewById(R.id.ll_reservation);
            this.ll_tourism = (LinearLayout) rootView.findViewById(R.id.ll_tourism);

            this.ll_booking.setOnClickListener(this);
            this.ll_car.setOnClickListener(this);
            this.ll_reservation.setOnClickListener(this);
            this.ll_tourism.setOnClickListener(this);
            this.mTv_custom.setOnClickListener(this);

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

        vp.setCurrentItem(3);
        vp.setAdapter(pageAdapter);
        cpi.setViewPager(vp);
        cpi.setOnPageChangeListener(pageListener);
        pageListener.onPageSelected(skip);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int dip2px = DensityUtil.dip2px(getContext(), 30);
        int widthPixels = dm.widthPixels - dip2px;
        int heightP = widthPixels / 2;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl.getLayoutParams();
        linearParams.width = widthPixels;
        linearParams.height = heightP;
        fl.setLayoutParams(linearParams);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_booking:
                // 订票服务
                if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                    mIntent = new Intent(getContext(), OrderTicketServiceActivity.class);
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(getContext(), NoteLoginActivity.class);
                    mIntent.putExtra("personal", "order_ticket");
                    startActivity(mIntent);
                }
                break;
            case R.id.ll_car:
                // 专车服务
                if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                    mIntent = new Intent(getContext(), CarServiceActivity.class);
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(getContext(), NoteLoginActivity.class);
                    mIntent.putExtra("personal", "order_car");
                    startActivity(mIntent);
                }
                break;
            case R.id.ll_reservation:
                // 酒店预定
                if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                    mIntent = new Intent(getContext(), YuDingInfoActivity.class);
                    mIntent.putExtra("url", "/Travelsector/hotelreservationinformation");
                    mIntent.putExtra("type", "1");
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(getContext(), NoteLoginActivity.class);
                    mIntent.putExtra("personal", "order_hotel");
                    startActivityForResult(mIntent, 99);
                }
                break;
            case R.id.ll_tourism:
                // 深度旅游
                if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                    mIntent = new Intent(getContext(), TravelDeepActivity.class);
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(getContext(), NoteLoginActivity.class);
                    mIntent.putExtra("personal", "travel_deep");
                    startActivity(mIntent);
                }
                break;
            case R.id.tv_custom:
                // 点击定制
                boolean state2 = PreferencesUtils.getBoolean(getContext(), "isLogin");
                System.out.println("现在状态" + state2);
                if (state2 == true) {
                    mIntent = new Intent(getContext(), CustomTravelActivity.class);
                    startActivity(mIntent);
                } else if (state2 == false) {
                    mIntent = new Intent(getContext(), NoteLoginActivity.class);
                    mIntent.putExtra("personal", "custom_travel");
                    startActivity(mIntent);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 10) {
            mIntent = new Intent(getContext(), BusinessPlanActivity.class);
            mIntent.putExtra("id", "0");
            startActivity(mIntent);
        }
        if (requestCode == 1 && resultCode == 11) {
            mIntent = new Intent(getContext(), BusinessPlanActivity.class);
            mIntent.putExtra("id", "1");
            startActivity(mIntent);
        }
        if (requestCode == 2 && resultCode == 12) {
            mIntent = new Intent(getContext(), BusinessPlanActivity.class);
            mIntent.putExtra("id", "2");
            startActivity(mIntent);
        }
        if (requestCode == 99 && resultCode == 99) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mIntent = new Intent(getContext(), YuDingInfoActivity.class);
                    mIntent.putExtra("url", "/Travelsector/hotelreservationinformation");
                    mIntent.putExtra("type", "1");
                    startActivity(mIntent);
                }
            }, 400);
        }

    }

    private PagerAdapter pageAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return images == null ? 0 : images.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setImageResource(images[position]);
            iv.setScaleType(ScaleType.FIT_XY);
            container.addView(iv);
            iv.setOnClickListener(new OnClickListener() {
                private Intent mIntent;

                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                            mIntent = new Intent(getContext(), BusinessPlanActivity.class);
                            mIntent.putExtra("id", "0");
                            startActivity(mIntent);
                        } else {
                            mIntent = new Intent(getContext(), NoteLoginActivity.class);
                            mIntent.putExtra("personal", "common0");
                            startActivityForResult(mIntent, 0);
                        }
                    } else if (position == 1) {
                        if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                            mIntent = new Intent(getContext(), BusinessPlanActivity.class);
                            mIntent.putExtra("id", "1");
                            startActivity(mIntent);
                        } else {
                            mIntent = new Intent(getContext(), NoteLoginActivity.class);
                            mIntent.putExtra("personal", "common1");
                            startActivityForResult(mIntent, 1);
                        }

                    } else if (position == 2) {
                        if (PreferencesUtils.getBoolean(getContext(), "isLogin")) {
                            mIntent = new Intent(getContext(), BusinessPlanActivity.class);
                            mIntent.putExtra("id", "2");
                            startActivity(mIntent);
                        } else {
                            mIntent = new Intent(getContext(), NoteLoginActivity.class);
                            mIntent.putExtra("personal", "common2");
                            startActivityForResult(mIntent, 2);
                        }
                    }
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

}
