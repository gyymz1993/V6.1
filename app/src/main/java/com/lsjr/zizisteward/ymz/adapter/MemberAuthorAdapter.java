package com.lsjr.zizisteward.ymz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ymz.bean.BenefitsBean;
import com.lsjr.zizisteward.ymz.inter.RecycleViewItemListener;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/12 11:12
 */

public class MemberAuthorAdapter extends RecyclerView.Adapter<MemberAuthorAdapter.ViewHolder> {


    //private List<MenBerAuthorBean> menBerAuthorBeans = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private RecycleViewItemListener recycleViewItemListener;
    List<BenefitsBean.FitsBean> mData;
    public void setRecycleViewItemListener(RecycleViewItemListener recycleViewItemListener) {
        this.recycleViewItemListener = recycleViewItemListener;
    }

    public MemberAuthorAdapter(Context context,List<BenefitsBean.FitsBean> fitsBeans) {
        this.mData = fitsBeans;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<BenefitsBean.FitsBean> fitsBeans) {
        this.mData = fitsBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View view = layoutInflater.inflate(R.layout.item_rv_memberauthor, parent,false);
        /**/
          /*  为了完全占满*/
        View view = layoutInflater.inflate(R.layout.item_rv_memberauthor, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        viewHolder.recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MemberItemAdapter  adapter= new MemberItemAdapter(context, mData.get(position).getMember());
        holder.tvType.setText(mData.get(position).getName());
        Glide.with(context).load(HttpConfig.IMAGEHOST+mData.get(position).getType_icons()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable=new BitmapDrawable(bitmap);
                drawable.setBounds(0,0,bitmap.getWidth(),bitmap.getHeight());
                holder.tvType.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            }
        });

        holder.recyclerView.setAdapter(adapter);
        if (recycleViewItemListener!=null){
            adapter.setRecycleViewItemListener(recycleViewItemListener);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView  tvType;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.id_rv);
            tvType = (TextView) itemView.findViewById(R.id.id_tv_type);

        }
    }
}
