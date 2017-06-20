package com.lsjr.zizisteward.ymz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.ymz.custom.zonm.PullToZoomScrollViewEx;
import com.lsjr.zizisteward.ymz.utils.DisplayUtils;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/15 10:28
 */

public class MemberAuthorityDetailActivity extends BaseActivity {

    PullToZoomScrollViewEx scrollView;

    //ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
         initThisView();

    }

    @Override
    public int getContainerView() {
        return R.layout.activity_mem_author_detail1;
    }

    private void initTitle() {
        setmTitle("权益详情");
    }


    private void initThisView() {

        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        loadViewForCode();
        ImageView imageView= (ImageView) scrollView.getZoomView().findViewById(R.id.id_ig_title);


        Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + "/images?uuid=0d2eb055-a471-4a1d-abb7-305bd712df35").into(imageView);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width= DisplayUtils.getDisplayWidth(getApplicationContext());
        layoutParams.height=layoutParams.width/3*2;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        // View headView = LayoutInflater.from(this).inflate(R.layout.activity_mem_author_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.activity_mem_author_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_mem_author_content_view, null, false);
        // scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    private void findView() {
        //ImageView imageView= (ImageView) findViewById(R.id.id_ig_title);
        //Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + "/images?uuid=0d2eb055-a471-4a1d-abb7-305bd712df35").into(imageView);
    }

}
