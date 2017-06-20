package com.lsjr.zizisteward.ymz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.fragment.ShePinFragment;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ymz.bean.LoveHouseBean;

import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/14 13:05
 */

public class LoveHouseAdapter extends BaseRecyclerAdapter<LoveHouseAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<LoveHouseBean.HottestBean> mdata;

    public LoveHouseAdapter(Context contex,List<LoveHouseBean.HottestBean> list) {
        this.context = contex;
        this.mdata=list;
        inflater=LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<LoveHouseBean.HottestBean> list){
        this.mdata=list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        return new ViewHolder(inflater.inflate(R.layout.item_love_house,null),true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
        Glide.with(context).load(HttpConfig.IMAGEHOST + mdata.get(position).getImageFileName()).into(holder.mIv_recommend);
        holder.mTv_one.setText(mdata.get(position).getLocation());
        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recycleItemListener!=null){
                    recycleItemListener.onItemClick(position);
                }
            }
        });
        //holder.mTv_two.setText(mdata.get(position).getSname());
    }

    public void setRecycleItemListener(RecycleItemListener recycleItemListener) {
        this.recycleItemListener = recycleItemListener;
    }

    RecycleItemListener  recycleItemListener;
    public interface RecycleItemListener{
        void onItemClick(int position);
    }



    @Override
    public int getAdapterItemCount() {
        return mdata==null?0:mdata.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder{
        private ImageView mIv_recommend;
        private TextView mTv_one;
        private TextView mTv_two;
        private LinearLayout itemRoot;
        public ViewHolder(View view, boolean isItem) {
            super(view);
            if (isItem){
                itemRoot = (LinearLayout) view.findViewById(R.id.item_root);
                mIv_recommend = (ImageView) view.findViewById(R.id.iv_recommend);
                mTv_one = (TextView) view.findViewById(R.id.tv_one);
                mTv_two = (TextView) view.findViewById(R.id.tv_two);
            }

        }

    }
}
