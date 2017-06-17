package com.lsjr.zizisteward.ymz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.L;
import com.lsjr.zizisteward.utils.ScreenUtils;
import com.lsjr.zizisteward.ymz.bean.BenefitsBean;
import com.lsjr.zizisteward.ymz.bean.MenBerAuthorBean;
import com.lsjr.zizisteward.ymz.custom.dash.CouponLinearLayout;
import com.lsjr.zizisteward.ymz.inter.RecycleViewItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/12 11:36
 */

public class MemberItemAdapter extends RecyclerView.Adapter<MemberItemAdapter.ViewHolder> {


    public Context context;
    //private List<MenBerAuthorBean> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    List<BenefitsBean.FitsBean.MemberBean> mdata;

    public MemberItemAdapter(Context context, List<BenefitsBean.FitsBean.MemberBean> list) {
        this.context = context;
        this.mdata = list;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_memberauthor, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position < 4) {
            holder.linearLayout.setSemicircleTop(false);
            holder.linearLayout.setShapebg(true);
        } else {
            holder.linearLayout.setSemicircleTop(true);
        }
        if (getItemCount() % 4 == 0) {
            int culmCount = getItemCount() / 4;
            if (culmCount > 1) {
                if (position + 1 > 4 * (culmCount - 1)) {
                    holder.linearLayout.setSemicircleBottom(false);
                    //holder.linearLayout.setShapebg(true);
                } else {
                    holder.linearLayout.setSemicircleBottom(true);
                }
            } else {
                holder.linearLayout.setSemicircleBottom(false);
            }

        } else {
            double center = getItemCount() * 1.0f / 4.0;
            int culmCount = (int) Math.floor(center);
            if (position + 1 > 4 * culmCount) {
                holder.linearLayout.setSemicircleBottom(false);
            } else {
                holder.linearLayout.setSemicircleBottom(true);
            }
        }
       Glide.with(context).load(HttpConfig.IMAGEHOST + mdata.get(position).getIcon()).into(holder.igItemMem);
        //holder.igItemMem.setImageResource(R.drawable.leve_test);
        holder.tvItemMem.setText(mdata.get(position).getContent());
        holder.rootLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("点击点击");
                if (recycleViewItemListener != null) {
                    recycleViewItemListener.onItemClick(mdata.get(position).getEntityId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CouponLinearLayout linearLayout;
        LinearLayout rootLy;
        ImageView igItemMem;
        TextView tvItemMem;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (CouponLinearLayout) itemView.findViewById(R.id.id_coup_layout);
            igItemMem = (ImageView) itemView.findViewById(R.id.id_ig_item_mem);
            tvItemMem = (TextView) itemView.findViewById(R.id.id_tv_item_mem);

            rootLy = (LinearLayout) itemView.findViewById(R.id.id_root);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rootLy.getLayoutParams();
            int width = ScreenUtils.getScreenWidth(context);
            params.width = width / 3;

        }
    }

    RecycleViewItemListener recycleViewItemListener;

    public void setRecycleViewItemListener(RecycleViewItemListener recycleViewItemListener) {
        this.recycleViewItemListener = recycleViewItemListener;
    }
}
