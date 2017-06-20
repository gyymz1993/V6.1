package com.lsjr.zizisteward.ymz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.TravelWebViewActivity;
import com.lsjr.zizisteward.fragment.ShePinFragment;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.L;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.ymz.adapter.BannerViewPagerAdapter;
import com.lsjr.zizisteward.ymz.adapter.LoveHouseAdapter;
import com.lsjr.zizisteward.ymz.bean.LoveHouseBean;
import com.lsjr.zizisteward.ymz.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/14 11:44
 */

public class LoveHouseFragment extends Fragment {

    private View headView;
    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private LoveHouseAdapter adapter;
    private int page = 1;
    private LayoutInflater inflate;
    private View contentView;
    List<ImageView> indictorImags;
    List<LoveHouseBean.BannerBean> bannerData;
    BannerViewPagerAdapter bannerViewPagerAdapter;
    /*空间大师  */
    List<LoveHouseBean.RecommendBean> recommend;
    /*最热列表*/
    List<LoveHouseBean.HottestBean> hottest;
    ViewPager viewPager;

    /*空间名师*/
    LinearLayout layoutRcommendContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.activity_love_house, container, false);
        inflate = LayoutInflater.from(getContext());
        findView();
        initView();
        loadDataForNet();
        return contentView;
    }

    private void loadDataForNet() {
        L.e("爱家 loadDataForNet");
        final HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "525");
        map.put("currPage", page + "");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                xRefreshView.stopRefresh();
                Log.e("Tag 525", result);
                LoveHouseBean loveHouseBean = GsonUtil.getInstance().fromJson(result, LoveHouseBean.class);
                bannerData = loveHouseBean.getBanner();
                recommend = loveHouseBean.getRecommend();
                hottest = loveHouseBean.getHottest();
                setBannerData();
                setHsLayoutRecommendData();
                adapter.notifyDataSetChanged(hottest);
            }
        });

    }

    private View getHeadView() {
        headView = inflate.inflate(R.layout.include_activity_love_house_head, null);
        viewPager = (ViewPager) headView.findViewById(R.id.id_viewpager);
        TextView tvMore = (TextView) headView.findViewById(R.id.id_tv_more);
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MemberAuthorityActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout layoutContainer = (LinearLayout) headView.findViewById(R.id.id_hs_container);
        setHsLayoutShopData(layoutContainer);
        layoutRcommendContainer = (LinearLayout) headView.findViewById(R.id.id_recomend_container);


        return headView;
    }


    private void setBannerData() {
        if (bannerData == null && bannerData.size() == 0) return;
        bannerViewPagerAdapter = new BannerViewPagerAdapter(bannerData);
        bannerViewPagerAdapter.setBannerSelectListener(new BannerViewPagerAdapter.BannerSelectListener() {
            @Override
            public void onSelect(int position) {
                Intent mIntent = new Intent(getContext(), TravelWebViewActivity.class);
                mIntent.putExtra("url", bannerData.get(position%bannerData.size()).getUrl());
                mIntent.putExtra("title", "home");
                startActivity(mIntent);
            }
        });
        indictorImags = new ArrayList<>();
        viewPager.setOffscreenPageLimit(bannerData.size());
        viewPager.setAdapter(bannerViewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
            }
        });
        viewPager.setCurrentItem(0);
        LinearLayout llContainerIndictor = (LinearLayout) headView.findViewById(R.id.ll_container_indictor);
        if (bannerData.size() == 0) return;
        llContainerIndictor.removeAllViews();
        for (int i = 0; i < bannerData.size(); i++) {
            View view = inflate.inflate(R.layout.view_cycle_viewpager_indicator, null);
            view.setTag(i);
            indictorImags.add((ImageView) view.findViewById(R.id.image_indicator));
            llContainerIndictor.addView(view);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = 20;
        llContainerIndictor.setLayoutParams(params);
        setIndicator(0);

    }

    /*设置指示器*/
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < indictorImags.size(); i++) {
            indictorImags.get(i).setBackgroundResource(R.drawable.icon_point);
        }
        indictorImags.get(selectedPosition % indictorImags.size()).setBackgroundResource(R.drawable.icon_point_pre);
    }


    /*居家好物*/
    private void setHsLayoutShopData(LinearLayout layoutContainer) {
        layoutContainer.removeAllViews();
        for (int i = 0; i < 8; i++) {
            View view = inflate.inflate(R.layout.item_love_house_view, null);
            //ImageView imageView=(ImageView) view.findViewById(R.id.iv_ig_lovehouser);
            //TextView textView=(TextView) view.findViewById(R.id.iv_tv_lovehouser);

            view.setTag(i);
            layoutContainer.addView(view);
        }
    }


    /*大师空间*/
    private void setHsLayoutRecommendData() {
        layoutRcommendContainer.removeAllViews();
        for (int i = 0; i < recommend.size(); i++) {
            View view = inflate.inflate(R.layout.item_love_house_recommend, null);
            RoundImageView imageView = (RoundImageView) view.findViewById(R.id.id_ig_recommed);
            TextView textView = (TextView) view.findViewById(R.id.id_tv_recommed_name);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = DisplayUtils.getDisplayWidth(getContext()) / 2;
            Glide.with(getContext()).load(HttpConfig.IMAGEHOST + recommend.get(i).getImageFileName()).into(imageView);
            textView.setText(recommend.get(i).getLocation());
            view.setTag(i);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getContext(), TravelWebViewActivity.class);
                    mIntent.putExtra("url", recommend.get(finalI).getUrl());
                    mIntent.putExtra("title", "home");
                    startActivity(mIntent);
                }
            });
            layoutRcommendContainer.addView(view);
        }
    }


    private void initView() {
        hottest = new ArrayList<>();
        adapter = new LoveHouseAdapter(getActivity(), hottest);
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.stopLoadMore(true);
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                //super.onRefresh(isPullDown);
                loadDataForNet();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }
        });
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        adapter.setHeaderView(getHeadView(), recyclerView);
        adapter.setRecycleItemListener(new LoveHouseAdapter.RecycleItemListener() {
            @Override
            public void onItemClick(int position) {
                Intent mIntent = new Intent(getContext(), TravelWebViewActivity.class);
                mIntent.putExtra("url", recommend.get(position).getUrl());
                mIntent.putExtra("title", "home");
                startActivity(mIntent);
            }
        });
        recyclerView.setAdapter(adapter);
    }


    public void findView() {
        xRefreshView = (XRefreshView) contentView.findViewById(R.id.id_xfrv);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.id_rv);
    }


}
