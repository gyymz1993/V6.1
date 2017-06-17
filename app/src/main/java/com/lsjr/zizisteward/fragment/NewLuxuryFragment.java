package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.TravelWebViewActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.lcq.activity.BannerViewPager;
import com.lsjr.zizisteward.lcq.activity.CustomGifHeader;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */

public class NewLuxuryFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private View rootView;
    private XRefreshView xrefreshview;
    private RecyclerView recyclerview;
    private List<NewHomeFragment.Advertisements> list_banner = new ArrayList<NewHomeFragment.Advertisements>();
    private ShePinFragment.ShePinBean bean;
    private List<ShePinFragment.DataList> list = new ArrayList<ShePinFragment.DataList>();
    private int pageNum = 1;
    private NewLuxuryAdapter adapter_luxury;
    private BannerViewPager viewpager;
    private LinearLayout ll_add_imageview;
    private ViewPagerAdapter adapter;
    private ImageView[] indicators;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_luxury_new, container, false);
            xrefreshview = (XRefreshView) rootView.findViewById(R.id.xrefreshview);
            recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
            getBannerData();/*广告图数据*/
            getData(1, 1);/*奢品列表数据*/
            initLayout();
        }
        return rootView;
    }

    private void getData(int page, final int mode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "423");
        map.put("currPage", String.valueOf(page));
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                bean = GsonUtil.getInstance().fromJson(result, ShePinFragment.ShePinBean.class);
                adapter.notifyDataSetChanged();
                switch (mode) {
                    case 1:
                        //*刷新*//
                        list = bean.getProducts();
                        adapter_luxury.notifyDataSetChanged(list);
                        xrefreshview.stopRefresh();
                        xrefreshview.setLoadComplete(false);
                        break;
                    case 2:
                        //*加载更多*//*
                        if (bean.getProducts().size() == 0) {
                            xrefreshview.setLoadComplete(true);
                        } else {
                            list.addAll(bean.getProducts());
                            adapter_luxury.notifyDataSetChanged(list);
                            xrefreshview.stopLoadMore(false);
                        }
                        break;
                }


            }
        });

    }

    private void getBannerData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "24");
        map.put("currPage", "1");
        map.put("name", "");
        map.put("city_id", "420100");
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                ToastUtils.show(getContext(), "什么鬼");
                System.out.println("新首页" + result);
                NewHomeFragment.NewHome bean = GsonUtil.getInstance().fromJson(result, NewHomeFragment.NewHome.class);
                list_banner = bean.getAdvertisements();
                adapter_luxury.notifyDataSetChanged(list);
                initViewPager();
            }

        });

    }

    private HorizontalScrollView hsl;
    private LinearLayout ll_parent;
    private RelativeLayout re_classic_licqi;
    private ImageView iv_classic_licqi;
    private TextView tv_classic_licqi;
    private View view_licqi;
    private RelativeLayout[] re_nums;
    private ImageView[] iv_nums;
    private TextView[] tv_nums;
    private View[] view_nums;
    private String[] titles = {"配饰", "服饰", "美妆", "健康", "医疗", "美食", "李晨奇", "李慧"};
    private int[] images = {R.drawable.user_head, R.drawable.user_head, R.drawable.user_head, R.drawable.user_head, R.drawable.user_head, R.drawable.user_head, R.drawable.user_head, R.drawable.user_head};
    private View views[];

    private void initLayout() {
        adapter_luxury = new NewLuxuryAdapter(getContext(), list);
        xrefreshview.setPullLoadEnable(true);
        recyclerview.setHasFixedSize(true);
        /*recyclerview嵌套scrollview卡顿问题*/
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });

        View headerView = adapter_luxury.setHeaderView(R.layout.activity_banner_head, recyclerview);
        LinearLayout ll_head = (LinearLayout) headerView.findViewById(R.id.ll_head);
        viewpager = (BannerViewPager) headerView.findViewById(R.id.viewpager);
        ll_add_imageview = (LinearLayout) headerView.findViewById(R.id.ll_add_imageview);
        hsl = (HorizontalScrollView) headerView.findViewById(R.id.hsl);
        ll_parent = (LinearLayout) headerView.findViewById(R.id.ll_parent);
         /*奢品分类*/
        views = new View[titles.length];
        re_nums = new RelativeLayout[titles.length];
        iv_nums = new ImageView[titles.length];
        tv_nums = new TextView[titles.length];
        view_nums = new View[titles.length];
        for (int i = 0; i < titles.length; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.luxury_classic, null);
            view.setId(i);
            re_classic_licqi = (RelativeLayout) view.findViewById(R.id.re_classic_licqi);
            iv_classic_licqi = (ImageView) view.findViewById(R.id.iv_classic_licqi);
            tv_classic_licqi = (TextView) view.findViewById(R.id.tv_classic_licqi);
            view_licqi = view.findViewById(R.id.view_licqi);
            iv_classic_licqi.setImageResource(images[i]);
            tv_classic_licqi.setText(titles[i]);
            ll_parent.addView(view);

            re_nums[i] = re_classic_licqi;
            iv_nums[i] = iv_classic_licqi;
            tv_nums[i] = tv_classic_licqi;
            view_nums[i] = view_licqi;
            views[i] = view;

        }

        initViewPager();

        CustomGifHeader header = new CustomGifHeader(getContext());
        xrefreshview.setCustomHeaderView(header);
        recyclerview.setAdapter(adapter_luxury);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);
        adapter_luxury.setCustomLoadMoreView(new XRefreshViewFooter(getContext()));

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        getData(1, 1);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pageNum++;
                getData(pageNum, 2);
            }
        });
    }

    private void initViewPager() {
        indicators = new ImageView[list_banner.size()];
        for (int i = 0; i < list_banner.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_cycle_viewpager_indicator, null);
            ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
            indicators[i] = iv;
            ll_add_imageview.addView(view);
        }
        adapter = new ViewPagerAdapter();
        setIndicator(0);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(this);
        viewpager.setAdapter(adapter);

        viewpager.setParent(recyclerview);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = 20;
        ll_add_imageview.setLayoutParams(params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class NewLuxuryAdapter extends BaseRecyclerAdapter<NewLuxuryHolder> {
        private Context context;
        private List<ShePinFragment.DataList> list;

        public NewLuxuryAdapter(Context context, List<ShePinFragment.DataList> list) {
            this.list = list;
            this.context = context;
        }

        public void notifyDataSetChanged(List<ShePinFragment.DataList> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public NewLuxuryHolder getViewHolder(View view) {
            return new NewLuxuryHolder(view, false);
        }

        @Override
        public NewLuxuryHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_shepin, parent, false);
            NewLuxuryHolder holder = new NewLuxuryHolder(view, true);
            return holder;
        }

        @Override
        public void onBindViewHolder(NewLuxuryHolder holder, int position, boolean isItem) {
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpic()).into(holder.mIv_recommend);
            holder.mTv_one.setText(list.get(position).getBname());
            holder.mTv_two.setText(list.get(position).getSname());
        }

        @Override
        public int getAdapterItemCount() {
            return list == null ? 0 : list.size();
        }

    }

    private class NewLuxuryHolder extends RecyclerView.ViewHolder {

        private ImageView mIv_recommend;
        private TextView mTv_one;
        private TextView mTv_two;

        public NewLuxuryHolder(View view, boolean isItem) {
            super(view);
            mIv_recommend = (ImageView) view.findViewById(R.id.iv_recommend);
            mTv_one = (TextView) view.findViewById(R.id.tv_one);
            mTv_two = (TextView) view.findViewById(R.id.tv_two);
        }

    }


    /*设置指示器*/
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(R.drawable.icon_point);
        }
        if (indicators.length > selectedPosition)
            indicators[selectedPosition].setBackgroundResource(R.drawable.icon_point_pre);
    }


    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return list_banner == null ? 0 : list_banner.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = new ImageView(container.getContext());
            Glide.with(getContext()).load(HttpConfig.IMAGEHOST + list_banner.get(position).getImage_filename()).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                private Intent mIntent;

                @Override
                public void onClick(View v) {
                    mIntent = new Intent(getContext(), TravelWebViewActivity.class);
                    if (position == 0) {
                        mIntent.putExtra("url", list_banner.get(0).getUrl());
                        mIntent.putExtra("title", "home");
                    } else if (position == 1) {
                        mIntent.putExtra("url", list_banner.get(1).getUrl());
                        mIntent.putExtra("title", "home");
                    } else if (position == 2) {
                        mIntent.putExtra("url", list_banner.get(2).getUrl());
                        mIntent.putExtra("title", "home");
                    } else if (position == 3) {
                        mIntent.putExtra("url", list_banner.get(3).getUrl());
                        mIntent.putExtra("title", "home");
                    }
                    startActivity(mIntent);
                }
            });

            return iv;

        }
    }

}
