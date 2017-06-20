package com.lsjr.zizisteward.ymz.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libs.zxing.Config;
import com.lsjr.zizisteward.activity.TravelWebViewActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ymz.bean.LoveHouseBean;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/14 15:32
 */

public class BannerViewPagerAdapter extends PagerAdapter {

    private List<LoveHouseBean.BannerBean> mdata;
    public BannerViewPagerAdapter(List<LoveHouseBean.BannerBean> data) {
        this.mdata = data;
    }

    public void notifyDataChanged(List<LoveHouseBean.BannerBean> data) {
        this.mdata = data;
        notifyDataSetChanged();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (mdata.size()==0)return imageView;
        Glide.with(container.getContext()).load(HttpConfig.IMAGEHOST+mdata.get(position % mdata.size()).getImageFileName()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bannerSelectListener!=null){
                    bannerSelectListener.onSelect(position);
                }
            }
        });
        return imageView;
    }

    public void setBannerSelectListener(BannerSelectListener bannerSelectListener) {
        this.bannerSelectListener = bannerSelectListener;
    }

    BannerSelectListener bannerSelectListener;
    public interface BannerSelectListener{
        void onSelect(int position);
    }

    @Override
    public int getCount() {
        return mdata == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

}
