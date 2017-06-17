package com.lsjr.zizisteward.common.activtiy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.BrandActivity.DianPuList;
import com.lsjr.zizisteward.activity.BrandActivity.DianPuListDetail;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BrandBean;
import com.lsjr.zizisteward.bean.BrandBean.BrandInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopAndBrand extends BaseActivity {
    /* 传过来类型 店铺或者品牌 */
    private String type;
    /* 店铺数据 */
    private List<DianPuListDetail> list_shop = new ArrayList<DianPuListDetail>();
    /* 品牌数据 */
    private List<BrandInfo> list_brand = new ArrayList<BrandInfo>();
    private RecyclerView recyclerview;

    @Override
    public int getContainerView() {
        return R.layout.fragment_brand_recyclerview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("shepin")) {
            setmTitle("品牌列表");
        } else {
            setmTitle("店铺列表");
        }
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        int space = getResources().getDimensionPixelSize(R.dimen.recyclerview);
        recyclerview.addItemDecoration(new SpaceItemDecoration(space));
        initData();
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.bottom = space;
            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) % 3 == 0) {
                outRect.left = 0;
            }
//            if (parent.getChildLayoutPosition(view) != 0) {
////                outRect.top = space;
//
//            }

        }
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position);

        void ItemLongClickListener(View view, int position);
    }

    public class NewRecyclerViewAdapter extends RecyclerView.Adapter<LiChenqiHolder> {
        private List<DianPuListDetail> list_shop;
        private Context context;
        private OnItemClickListener clickListener;

        public void setOnClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public NewRecyclerViewAdapter(Context context, List<DianPuListDetail> list_shop) {
            this.context = context;
            this.list_shop = list_shop;
        }


        @Override
        public LiChenqiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
            LiChenqiHolder holder = new LiChenqiHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final LiChenqiHolder holder, int position) {
            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curPosition = holder.getAdapterPosition();
                        clickListener.ItemClickListener(holder.itemView, curPosition);
                    }
                });
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_shop.get(position).getBlogo())
                    .animate(android.R.anim.slide_in_left).into(holder.iv_brand);
        }

        @Override
        public int getItemCount() {
            return list_brand == null ? 0 : list_shop.size();
        }
    }

    public class LiChenqiHolder extends RecyclerView.ViewHolder {
        ImageView iv_brand;

        public LiChenqiHolder(View view) {
            super(view);
            iv_brand = (ImageView) view.findViewById(R.id.iv_brand);
        }
    }

    public class BrandRecyclerViewAdapter extends RecyclerView.Adapter<LiChenqiHolder> {
        private Context context;
        private List<BrandInfo> list_brand;
        private OnItemClickListener clickListener;

        public void setOnClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public BrandRecyclerViewAdapter(Context context, List<BrandInfo> list_brand) {
            this.context = context;
            this.list_brand = list_brand;
        }

        @Override
        public LiChenqiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
            LiChenqiHolder holder = new LiChenqiHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final LiChenqiHolder holder, int position) {
            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int curPosition = holder.getAdapterPosition();
                        clickListener.ItemClickListener(holder.itemView, curPosition);
                    }
                });
            }
            Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_brand.get(position).getBlogo())
                    .animate(android.R.anim.slide_in_left).into(holder.iv_brand);
        }

        @Override
        public int getItemCount() {
            return list_brand == null ? 0 : list_brand.size();
        }
    }


    private void initData() {
        Map<String, String> map = new HashMap<String, String>();
        if (type.equals("shepin")) {
            map.put("OPT", "22");// 品牌列表
        } else {
            map.put("OPT", "299");// 店铺列表
        }
        new HttpClientGet(ShopAndBrand.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("消息" + result);
                if (type.equals("shepin")) {
                    BrandBean bean = GsonUtil.getInstance().fromJson(result, BrandBean.class);
                    list_brand = bean.getBrands();
                    BrandRecyclerViewAdapter adapter_brand = new BrandRecyclerViewAdapter(ShopAndBrand.this, list_brand);
                    recyclerview.setAdapter(adapter_brand);
                    adapter_brand.setOnClickListener(new OnItemClickListener() {
                        @Override
                        public void ItemClickListener(View view, int position) {
                            Intent intent = new Intent(getApplicationContext(), NewShopDetailActivity.class);// 品牌列表对应的品牌详情
                            intent.putExtra("title", list_brand.get(position).getBname());
                            intent.putExtra("id", list_brand.get(position).getId());
                            intent.putExtra("type", "brand");
                            startActivity(intent);
                        }

                        @Override
                        public void ItemLongClickListener(View view, int position) {

                        }
                    });
                } else {
                    DianPuList dian_bean = GsonUtil.getInstance().fromJson(result, DianPuList.class);
                    list_shop = dian_bean.getStore();
                    NewRecyclerViewAdapter adapter = new NewRecyclerViewAdapter(ShopAndBrand.this, list_shop);
                    recyclerview.setAdapter(adapter);
                    adapter.setOnClickListener(new OnItemClickListener() {
                        @Override
                        public void ItemClickListener(View view, int position) {
                            Intent intent = new Intent(getApplicationContext(), NewShopDetailActivity.class);// 店铺列表对应的店铺详情
                            intent.putExtra("title", list_shop.get(position).getBname());
                            intent.putExtra("id", list_shop.get(position).getId());
                            intent.putExtra("type", "shop");
                            startActivity(intent);
                        }

                        @Override
                        public void ItemLongClickListener(View view, int position) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

}
