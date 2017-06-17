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
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.fragment.ShePinFragment;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.L;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.ymz.adapter.BannerViewPagerAdapter;
import com.lsjr.zizisteward.ymz.adapter.LoveHouseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/14 11:44
 */

public class LoveHouseFragment extends Fragment {

    private List<ShePinFragment.DataList> mdata;
    private View headView;
    private XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private LoveHouseAdapter adapter;
    private int page = 1;
    private LayoutInflater inflate;
    private View onNetView;
    private View contentView;
    List<ImageView> indictorImags;
    private FrameLayout frameLayout;

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
        final HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "525");
        map.put("currPage", page + "");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                xRefreshView.stopRefresh();
                Log.e("Tag 525", result);
                //ShePinFragment.ShePinBean shePinBean = GsonUtil.getInstance().fromJson(result, ShePinFragment.ShePinBean.class);
                // mdata = shePinBean.getProducts();
                // adapter.notifyDataSetChanged(mdata);
            }
        });

    }

    private View getHeadView() {
        headView = inflate.inflate(R.layout.include_activity_love_house_head, null);
        ViewPager viewPager = (ViewPager) headView.findViewById(R.id.id_viewpager);
        setBannerData(viewPager);

        TextView tvMore = (TextView) headView.findViewById(R.id.id_tv_more);
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(getContext(), "更多被点击");
                Intent intent = new Intent(getContext(), MemberAuthorityActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout layoutContainer = (LinearLayout) headView.findViewById(R.id.id_hs_container);
        setHsLayoutData(layoutContainer);

        return headView;
    }


    private void setBannerData(ViewPager viewPager) {
        List<String> images = new ArrayList<>();
        indictorImags = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            images.add(HttpConfig.IMAGEHOST + "/images?uuid=0d2eb055-a471-4a1d-abb7-305bd712df35");
        }
        BannerViewPagerAdapter bannerViewPagerAdapter = new BannerViewPagerAdapter(images);
        viewPager.setOffscreenPageLimit(images.size());
        viewPager.setAdapter(bannerViewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("TAG  ", "当前点击：" + position);
                setIndicator(position);
            }
        });
        viewPager.setCurrentItem(0);


        LinearLayout llContainerIndictor = (LinearLayout) headView.findViewById(R.id.ll_container_indictor);
        for (int i = 0; i < images.size(); i++) {
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


    private void setHsLayoutData(LinearLayout layoutContainer) {
        for (int i = 0; i < 8; i++) {
            View view = inflate.inflate(R.layout.item_love_house_view, null);
            view.setTag(i);
            layoutContainer.addView(view);
        }
    }

    private void initView() {
        mdata = new ArrayList<>();
        adapter = new LoveHouseAdapter(getActivity(), mdata);
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.stopLoadMore(false);
        xRefreshView.setPullLoadEnable(true);
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
        recyclerView.setAdapter(adapter);
    }


    public void findView() {
        xRefreshView = (XRefreshView) contentView.findViewById(R.id.id_xfrv);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.id_rv);
    }


}
