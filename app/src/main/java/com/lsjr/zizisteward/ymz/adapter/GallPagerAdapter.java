package com.lsjr.zizisteward.ymz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.L;
import com.lsjr.zizisteward.ymz.bean.DescriptionBean;


import java.util.List;

public class GallPagerAdapter extends MyPageradapter {

    private Context context;
    List<DescriptionBean.RegularsBean> mdata;
    private LayoutInflater mInflater;

    public GallPagerAdapter(Context context, List<DescriptionBean.RegularsBean> regulars) {
        this.mdata = regulars;
        this.context = context;
        mInflater=LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<DescriptionBean.RegularsBean> regulars){
        this.mdata=regulars;

        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            view=mInflater.inflate(R.layout.item_men_vp,null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
       // imageView.setTag(position);
       // imageView.setImageResource(R.drawable.vip_bg);
       // L.e("HttpConfig.IMAGEHOST +mdata.get(position).getImg()"+HttpConfig.IMAGEHOST +mdata.get(position).getImg());
        Glide.with(context).load(HttpConfig.IMAGEHOST +mdata.get(position%mdata.size()).getImg()).into(viewHolder.imageView);
        viewHolder.tvContent.setText(mdata.get(position%mdata.size()).getDescription()+"");
        viewHolder.tvPrice.setText(mdata.get(position%mdata.size()).getUpgrade_amount()+"");
        //imageView.setTag(position);
       // Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + mShare_img).into(mIv_shane_ceshi);
        //imageView.setImageResource(mdata.get(position));
        return view;
    }


    class ViewHolder{
        ImageView imageView;
        TextView tvContent;
        TextView tvPrice;
        public ViewHolder(View view) {
            imageView= (ImageView) view.findViewById(R.id.id_ig_vp);
            tvContent= (TextView) view.findViewById(R.id.id_tv_content);
            tvPrice= (TextView) view.findViewById(R.id.id_tv_price);
        }


    }



    @Override
    public int getCount() {
        return mdata == null ? 0 : Integer.MAX_VALUE;
    }
}