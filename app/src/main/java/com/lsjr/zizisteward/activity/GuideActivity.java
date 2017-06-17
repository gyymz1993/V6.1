package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager viewpager;
    private LinearLayout ll_jump, ll_pages;
    private Context context = GuideActivity.this;
    private List<Integer> guides = Arrays.asList(R.drawable.guide_one_big);
    /*整体Views集合列表*/
    private List<View> views;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll_jump = (LinearLayout) findViewById(R.id.jump);
        ll_pages = (LinearLayout) findViewById(R.id.pages);
        /*控件监听*/
        initListener();
      /*初始化跳过布局*/
        initJump();
        /*初始化页数*/
        initPages();
        /*初始化Viewpager*/
        initViewPager();
    }


    public void initListener() {
        ll_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SimpleVoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initJump() {
        TextView tv_jump = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_jump.setText("跳过");
        tv_jump.setTextColor(0xffffffff);
        tv_jump.setTextSize(18);
        tv_jump.setLayoutParams(params);
        ll_jump.addView(tv_jump);
    }

    private void initPages() {
        TextView tv_pages = new TextView(context);
        LinearLayout.LayoutParams params_page = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_pages.setText("1/2");
        tv_pages.setTextColor(0xffffffff);
        tv_pages.setTextSize(18);
        tv_pages.setLayoutParams(params_page);
        ll_pages.addView(tv_pages);
    }

    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(context, getView());
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(this);
    }

    // 获得Views
    private List<View> getView() {
        views = new ArrayList<>();
        View view6 = getLayoutInflater().inflate(R.layout.guide_layout_6, null);

        for (Integer guide : guides) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(guide);
            views.add(imageView);
        }
        TextView tv_text = (TextView) view6.findViewById(R.id.tv_text);
        tv_text.setTextSize(18);
        RelativeLayout re_into = (RelativeLayout) view6.findViewById(R.id.re_into);
        re_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SimpleVoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        views.add(view6);
        return views;

    }

    class ViewPagerAdapter extends PagerAdapter {
        private Context context;
        private List<View> views;

        public ViewPagerAdapter(Context context, List<View> views) {
            this.context = context;
            this.views = views;
        }

        @Override
        public int getCount() {
            return views == null ? 0 : views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = views.get(position);
            if (container.equals(view.getParent())) {
                container.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        if (position == 1) {
            ll_pages.setVisibility(View.GONE);
            ll_jump.setVisibility(View.GONE);
        }
        if (position == 0) {
            ll_pages.setVisibility(View.VISIBLE);
            ll_jump.setVisibility(View.VISIBLE);
        }
    }

    public void onPageScrollStateChanged(int state) {
    }
}
