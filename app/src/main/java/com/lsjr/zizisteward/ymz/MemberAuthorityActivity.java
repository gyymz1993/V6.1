package com.lsjr.zizisteward.ymz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.utils.L;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.ymz.adapter.GallPagerAdapter;
import com.lsjr.zizisteward.ymz.adapter.MemberAuthorAdapter;
import com.lsjr.zizisteward.ymz.adapter.SpacesItemDecoration;
import com.lsjr.zizisteward.ymz.bean.BenefitsBean;
import com.lsjr.zizisteward.ymz.bean.DescriptionBean;
import com.lsjr.zizisteward.ymz.custom.FixdSpeedScroll;
import com.lsjr.zizisteward.ymz.custom.GalleryViewPager;
import com.lsjr.zizisteward.ymz.inter.RecycleViewItemListener;
import com.lsjr.zizisteward.ymz.manager.FullyLinearLayoutManager;
import com.lsjr.zizisteward.ymz.utils.ScalePageTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/12 10:22
 */

@SuppressLint("Registered")
public class MemberAuthorityActivity extends BaseActivity {

    RecyclerView mbARecyclerView;
    GalleryViewPager galleryViewPager;
    ImageView imageViewChange;
    //private List<String> gellImages = new ArrayList<>();
    List<DescriptionBean.RegularsBean> regulars;
    //private List<MenBerAuthorBean> menBerAuthorBeans = new ArrayList<>();
    int changeCount;
    private GallPagerAdapter gallPagerAdapter;
    private MemberAuthorAdapter authorAdapter;

    /*多少个Item*/
    List<BenefitsBean.FitsBean> fitsBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViwebyId();
        loadVPDataForNet();

    }


    /*获取会员等级*/
    private void loadVPDataForNet() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "107");
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Tag 107", result);
                DescriptionBean descriptionBean = new Gson().fromJson(result, DescriptionBean.class);
                regulars = descriptionBean.getRegulars();
                gallPagerAdapter = new GallPagerAdapter(getApplicationContext(), regulars);
                galleryViewPager.setAdapter(gallPagerAdapter);
                galleryViewPager.setOffscreenPageLimit(regulars.size());
                changeCount = 0;
                loadRVDataforNet(regulars.get(changeCount).getId() + "");

            }
        });
    }

    /*获取等级关联权限*/
    private void loadRVDataforNet(String vipId) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "108");
        map.put("vipId", vipId);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("Tag 108", result);
                BenefitsBean benefitsBean = new Gson().fromJson(result, BenefitsBean.class);
                /*多少个Item*/
                List<BenefitsBean.FitsBean> fitsBeans = benefitsBean.getBenefits();
                for (int i = 0; i < benefitsBean.getBenefits().size(); i++) {
                    benefitsBean.getBenefits().get(i).getName();
                    benefitsBean.getBenefits().get(i).getType_icons();
                }

                MemberAuthorAdapter authorAdapter = new MemberAuthorAdapter(getApplicationContext(), fitsBeans);
                mbARecyclerView.setAdapter(authorAdapter);
                authorAdapter.setRecycleViewItemListener(new RecycleViewItemListener() {
                    @Override
                    public void onItemClick(int position) {
                        ToastUtils.show("点击回调"+position);
                        L.e("TAG  position" + position);
                        Intent intent = new Intent(MemberAuthorityActivity.this, MemberAuthorityDetailActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initViwebyId() {
        mbARecyclerView = (RecyclerView) findViewById(R.id.id_rv);
        galleryViewPager = (GalleryViewPager) findViewById(R.id.viewpager);
        imageViewChange = (ImageView) findViewById(R.id.id_ig_change);
        RelativeLayout viewPagerRoot = (RelativeLayout) findViewById(R.id.root);


        galleryViewPager.setPageTransformer(true, new ScalePageTransformer());
        setViewPagerScrollSpeed(120);
        regulars = new ArrayList<>();
//        gallPagerAdapter = new GallPagerAdapter(getApplicationContext(), regulars);
//        setViewPagerScrollSpeed(120);
//        galleryViewPager.setAdapter(gallPagerAdapter);

        fitsBeans = new ArrayList<>();
        authorAdapter = new MemberAuthorAdapter(getApplicationContext(), fitsBeans);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(getApplicationContext());
        mbARecyclerView.setNestedScrollingEnabled(true);
        mbARecyclerView.setHasFixedSize(true);
        mbARecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        mbARecyclerView.setLayoutManager(linearLayoutManager);
        mbARecyclerView.setAdapter(authorAdapter);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        galleryViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                L.e("setOnPageChangeListener  position" + position);
                changeCount = position;
                String vipId = regulars.get(position%regulars.size()).getId() + "";
                loadRVDataforNet(vipId);
            }
        });

        imageViewChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeCount == regulars.size() - 1) {
                    changeCount = 0;
                } else {
                    changeCount++;
                }
                galleryViewPager.setCurrentItem(changeCount);
            }
        });




    }


    public void setViewPagerScrollSpeed(int ducration) {
        Interpolator interpolator = new AccelerateInterpolator();
        FixdSpeedScroll fixdSpeedScroll = new FixdSpeedScroll(getApplicationContext(), interpolator);
        fixdSpeedScroll.setmDucration(ducration);
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(galleryViewPager, fixdSpeedScroll);
        } catch (Exception e) {

        }
    }


    @Override
    public int getContainerView() {
        return R.layout.activity_memberauthor;
    }
}
