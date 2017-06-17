package com.lsjr.zizisteward.lcq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.common.activtiy.FamousBrandClassicActivity;
import com.lsjr.zizisteward.common.activtiy.ShopAndBrand;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.recyclerview.SwipeRecyclerView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ShopAndFamousClassicActivity extends BaseActivity {
    private SwipeRecyclerView swipe_recyclerview;
    private HorizontalScrollView hsl;
    private LinearLayout ll_parent;
    private String type;
    private RelativeLayout re_right;
    private LayoutInflater layoutInflater;
    /*匠品分类*/
    private List<FamousBrandClassicActivity.ProductType> list_type_jiangpin = new ArrayList<FamousBrandClassicActivity.ProductType>();
    private TextView toolsTextviews[];
    private View views[];
    /*奢品分类*/
    private List<FamousBrandClassicActivity.FamousType> list_type = new ArrayList<FamousBrandClassicActivity.FamousType>();
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private int typeClassic = 0;
    private int pageNum = 1;

    /*奢品内容*/
    private List<FamousBrandClassicActivity.FamousList> list_goods = new ArrayList<FamousBrandClassicActivity.FamousList>();
    private FamousRecyclerViewAdapter adapter_famous;

    /*匠品内容*/
    private List<FamousBrandClassicActivity.ProductList> list_shop = new ArrayList<FamousBrandClassicActivity.ProductList>();
    private ShopRecyclerViewAdaptere adapter_shop;

    @Override
    public int getContainerView() {
        return R.layout.activity_shop_and_famous_recyclerview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("jiangpin")) {
            setmTitle("匠品分类");
            setmRight("店铺");
        }
        if (type.equals("shepin")) {
            setmTitle("奢品分类");
            setmRight("品牌");
        }
        hsl = (HorizontalScrollView) findViewById(R.id.hsl);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        swipe_recyclerview = (SwipeRecyclerView) findViewById(R.id.swipe_recyclerview);
        re_right = (RelativeLayout) findViewById(R.id.re_right);// 品牌按钮或者店铺按钮
        layoutInflater = LayoutInflater.from(ShopAndFamousClassicActivity.this);
//        swipe_recyclerview.getSwipeRefreshLayout()
//                .setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipe_recyclerview.getSwipeRefreshLayout().setColorSchemeResources(R.color.yellow);
        swipe_recyclerview.getSwipeRefreshLayout().setProgressBackgroundColorSchemeResource(R.color.black_deep);
        swipe_recyclerview.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 2));
        swipe_recyclerview.getRecyclerView().setHasFixedSize(true);
        int space = getResources().getDimensionPixelSize(R.dimen.recyclerview);
        swipe_recyclerview.getRecyclerView().addItemDecoration(new SpaceItemDecoration(space));
        getData();
        init();
        initListener();

    }

    private void initListener() {
        re_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShopAndBrand.class);
                if (type.equals("jiangpin")) {
                    intent.putExtra("type", "jiangpin");
                } else {
                    intent.putExtra("type", "shepin");
                }
                startActivity(intent);
            }
        });
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
            outRect.right = space;
            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) % 2 == 0) {/*每行第一个item*/
                outRect.right = 0;
            }
        }
    }


    private void init() {
        swipe_recyclerview.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                /*刷新*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        getDataList(typeClassic, pageNum, 1);
                    }
                }, 600);
            }

            @Override
            public void onLoadMore() {
                /*加载更多*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum++;
                        getDataList(typeClassic, pageNum, 2);
                    }
                }, 800);
            }
        });

        //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
        //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
        /*我这里不想自动刷新所以注释了*/
//        swipe_recyclerview.setRefreshing(true);
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        if (type.equals("jiangpin")) {
            map.put("OPT", "421");
        } else {
            map.put("OPT", "425");
        }
        map.put("currPage", "1");
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("分类" + result);
                if (type.equals("jiangpin")) {
                    FamousBrandClassicActivity.JiangPinClass shop_classic_bean = GsonUtil.getInstance().fromJson(result, FamousBrandClassicActivity.JiangPinClass.class);
                    list_type_jiangpin = shop_classic_bean.getProductType();
                    toolsTextviews = new TextView[list_type_jiangpin.size()];
                    views = new View[list_type_jiangpin.size()];
                    for (int i = 0; i < list_type_jiangpin.size(); i++) {
                        View view = layoutInflater.inflate(R.layout.item_sheping, null);
                        view.setId(i);
                        view.setOnClickListener(toolsItemListener);
                        TextView textView = (TextView) view.findViewById(R.id.tv_name);
                        textView.setText(list_type_jiangpin.get(i).getTname());
                        ll_parent.addView(view);
                        toolsTextviews[i] = textView;
                        views[i] = view;
                    }
                    changeTextColor(0);
                    getDataList(0, 1, 1);
                } else {
                    FamousBrandClassicActivity.ShePingClass famous_classic_bean = GsonUtil.getInstance().fromJson(result, FamousBrandClassicActivity.ShePingClass.class);
                    list_type = famous_classic_bean.getFamousType();
                    toolsTextviews = new TextView[list_type.size()];
                    views = new View[list_type.size()];
                    for (int i = 0; i < list_type.size(); i++) {
                        View view = layoutInflater.inflate(R.layout.item_sheping, null);
                        view.setId(i);
                        view.setOnClickListener(toolsItemListener);
                        TextView textView = (TextView) view.findViewById(R.id.tv_name);
                        textView.setText(list_type.get(i).getTname());
                        ll_parent.addView(view);
                        toolsTextviews[i] = textView;
                        views[i] = view;
                    }
                    changeTextColor(0);
                    getDataList(0, 1, 1);
                }

            }

        });
    }

    private void getDataList(int i, int page, final int mode) {
        HashMap<String, String> map = new HashMap<>();
        if (type.equals("jiangpin")) {
            map.put("OPT", "422");
            map.put("spid", list_type_jiangpin.get(i).getId());
            System.out.println("匠品的id" + list_type_jiangpin.get(i).getId());
        } else {
            map.put("OPT", "424");
            map.put("spid", list_type.get(i).getId());
        }
        map.put("currPage", String.valueOf(page));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("分类商品" + result);
                FamousBrandClassicActivity.JiangPin shop_bean = GsonUtil.getInstance().fromJson(result, FamousBrandClassicActivity.JiangPin.class);
                FamousBrandClassicActivity.ShangPin famous_bean = GsonUtil.getInstance().fromJson(result, FamousBrandClassicActivity.ShangPin.class);
                switch (mode) {
                    case 1:
                        /*刷新*/
                        if (type.equals("jiangpin")) {

                            list_shop = shop_bean.getProductList();
                            adapter_shop = new ShopRecyclerViewAdaptere(ShopAndFamousClassicActivity.this, list_shop);
                            swipe_recyclerview.setAdapter(adapter_shop);

                            adapter_shop.setOnClickListener(new OnItemClickListener() {
                                @Override
                                public void ItemClickListener(View view, int position) {
                                    startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid",
                                            list_shop.get(position).getId()).putExtra("mode", "jiangpin").putExtra("hava_dianpu", "no"));
                                }

                                @Override
                                public void ItemLongClickListener(View view, int position) {
                                    ToastUtils.show(getApplicationContext(), "长按第" + position + "个");
                                }
                            });

                        } else {

                            list_goods = famous_bean.getFamousList();
                            adapter_famous = new FamousRecyclerViewAdapter(ShopAndFamousClassicActivity.this, list_goods);
                            swipe_recyclerview.setAdapter(adapter_famous);


                            adapter_famous.setOnClickListener(new OnItemClickListener() {
                                @Override
                                public void ItemClickListener(View view, int position) {
                                    startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid",
                                            list_goods.get(position).getId()).putExtra("mode", "shepin").putExtra("hava_dianpu", "no"));
                                }

                                @Override
                                public void ItemLongClickListener(View view, int position) {
                                    ToastUtils.show(getApplicationContext(), "长按第" + position + "个");
                                }
                            });

                        }

                        swipe_recyclerview.complete();

                        break;
                    case 2:
                        /*加载更多*/
                        if (type.equals("jiangpin")) {

                            if (shop_bean.getProductList().size() == 0) {
                                swipe_recyclerview.onNoMore("已加载完所有数据");
                            } else {
                                list_shop.addAll(shop_bean.getProductList());
                                adapter_shop.notifyDataSetChanged();
                            }

                        } else {

                            if (famous_bean.getFamousList().size() == 0) {
                                swipe_recyclerview.onNoMore("已加载完所有数据");
                            } else {
                                list_goods.addAll(famous_bean.getFamousList());
                                adapter_famous.notifyDataSetChanged();
                            }

                        }
                        swipe_recyclerview.stopLoadingMore();
                        break;
                }


            }
        });
    }

    /*接口监听*/
    public interface OnItemClickListener {
        void ItemClickListener(View view, int position);

        void ItemLongClickListener(View view, int position);
    }

    /*奢品adapter*/
    public class FamousRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        private Context context;
        private List<FamousBrandClassicActivity.FamousList> list_goods;
        private OnItemClickListener clickListener;

        public void setOnClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public FamousRecyclerViewAdapter(Context context, List<FamousBrandClassicActivity.FamousList> list_goods) {
            this.list_goods = list_goods;
            this.context = context;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_brand_list, parent, false);
            BaseViewHolder holder = new BaseViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final BaseViewHolder holder, int position) {
            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        clickListener.ItemClickListener(holder.itemView, currentPos);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        clickListener.ItemLongClickListener(holder.itemView, currentPos);
                        return true;
                    }
                });
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_goods.get(position).getSpic()).into(holder.iv_classic);
            holder.tv_brand.setText(list_goods.get(position).getSname());
            holder.tv_price.setText("￥" + list_goods.get(position).getSprice());
        }

        @Override
        public int getItemCount() {
            return list_goods == null ? 0 : list_goods.size();
        }
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_classic;
        TextView tv_brand, tv_price;

        public BaseViewHolder(View view) {
            super(view);
            iv_classic = (ImageView) view.findViewById(R.id.iv_classic);
            tv_brand = (TextView) view.findViewById(R.id.tv_brand);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }
    }


    /*匠品adapter*/
    public class ShopRecyclerViewAdaptere extends RecyclerView.Adapter<BaseViewHolder> {
        private Context context;
        private List<FamousBrandClassicActivity.ProductList> list_shop;
        private OnItemClickListener clickListener;

        public void setOnClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public ShopRecyclerViewAdaptere(Context context, List<FamousBrandClassicActivity.ProductList> list_shop) {
            this.context = context;
            this.list_shop = list_shop;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_brand_list, parent, false);
            BaseViewHolder holder = new BaseViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final BaseViewHolder holder, int position) {
            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        clickListener.ItemClickListener(holder.itemView, currentPos);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        clickListener.ItemLongClickListener(holder.itemView, currentPos);
                        return true;
                    }
                });
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_shop.get(position).getSpic()).into(holder.iv_classic);
            holder.tv_brand.setText(list_shop.get(position).getSname());
            holder.tv_price.setText("￥" + list_shop.get(position).getSprice());
        }

        @Override
        public int getItemCount() {
            return list_shop == null ? 0 : list_shop.size();
        }
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeTextColor(v.getId());
            changeTextLocation(v.getId());
            getDataList(v.getId(), 1, 1);
            typeClassic = v.getId();
        }
    };

    /**
     * 选中改变颜色
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextviews.length; i++) {
            if (i != id) {
                toolsTextviews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextviews[i].setTextColor(0xff000000);
                toolsTextviews[i].setTextSize(16);
            }
        }
        toolsTextviews[id].setBackgroundResource(android.R.color.white);
        toolsTextviews[id].setTextColor(0xffff5d5e);
        toolsTextviews[id].setTextSize(18);
    }

    /**
     * 改变栏目位置
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getLeft() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        hsl.smoothScrollTo(x, 0);
    }

    /**
     * 返回scrollview的中间位置
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = hsl.getRight() - hsl.getLeft();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     */
    private int getViewheight(View view) {
        return view.getRight() - view.getLeft();
    }
}
