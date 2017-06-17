package com.lsjr.zizisteward.common.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//名品分类
public class FamousBrandClassicActivity extends BaseActivity
        implements OnClickListener, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private RelativeLayout re_right;
    private TitleAdapter mAdapter;
    private JiangPinTitleAdapter jiangAdapter;
    private HorizontalScrollView hsl;
    private LinearLayout ll_parent;
    private List<FamousType> list_type = new ArrayList<FamousType>();
    private List<ProductType> list_type_jiangpin = new ArrayList<ProductType>();
    private LayoutInflater layoutInflater;
    private TextView toolsTextviews[];
    private View views[];
    private PullToRefreshView mMain_pull_refresh_view;
    private MyGridView brand_list_gridview;
    private List<FamousList> list_goods = new ArrayList<FamousList>();
    private List<ProductList> list_product = new ArrayList<ProductList>();
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private int typeClassic = 0;
    private int pageNum = 1;
    private String type;


    @Override
    public int getContainerView() {
        return R.layout.activity_famous_brand_classic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        re_right = (RelativeLayout) findViewById(R.id.re_right);// 品牌按钮或者店铺按钮
        hsl = (HorizontalScrollView) findViewById(R.id.hsl);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        layoutInflater = LayoutInflater.from(FamousBrandClassicActivity.this);
        mMain_pull_refresh_view = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
        brand_list_gridview = (MyGridView) findViewById(R.id.brand_list_gridview);
        mMain_pull_refresh_view.setOnHeaderRefreshListener(this);
        mMain_pull_refresh_view.setOnFooterRefreshListener(this);
        init();
        getData();
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
                    JiangPinClass bean = GsonUtil.getInstance().fromJson(result, JiangPinClass.class);
                    list_type_jiangpin = bean.getProductType();
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
                    getDataList(0, 1, 0);
                } else {
                    ShePingClass bean = GsonUtil.getInstance().fromJson(result, ShePingClass.class);
                    list_type = bean.getFamousType();
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
                    getDataList(0, 1, 0);
                }

            }

        });
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mMain_pull_refresh_view.postDelayed(new Runnable() {
            public void run() {
                mMain_pull_refresh_view.onFooterRefreshComplete();
                pageNum++;
                getDataList(typeClassic, pageNum, 1);
            }
        }, 500);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mMain_pull_refresh_view.postDelayed(new Runnable() {

            @Override
            public void run() {
                mMain_pull_refresh_view.onHeaderRefreshComplete();
                pageNum = 1;
                getDataList(typeClassic, pageNum, 0);
            }
        }, 500);
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
                JiangPin bean = GsonUtil.getInstance().fromJson(result, JiangPin.class);
                ShangPin _bean = GsonUtil.getInstance().fromJson(result, ShangPin.class);

                switch (mode) {
                    case 0:
                        if (type.equals("jiangpin")) {
                            list_product = bean.getProductList();
                            jiangAdapter = new JiangPinTitleAdapter(FamousBrandClassicActivity.this, list_product);
                            brand_list_gridview.setAdapter(jiangAdapter);
                            jiangAdapter.notifyDataSetChanged();
                            brand_list_gridview.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid",
                                            list_product.get(position).getId()).putExtra("mode", "jiangpin").putExtra("hava_dianpu", "no"));
                                }
                            });
                        } else {
                            list_goods = _bean.getFamousList();
                            mAdapter = new TitleAdapter(FamousBrandClassicActivity.this, list_goods);
                            brand_list_gridview.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            brand_list_gridview.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid",
                                            list_goods.get(position).getId()).putExtra("mode", "shepin").putExtra("hava_dianpu", "no"));
                                }
                            });
                        }
                        break;

                    case 1:

                        if (type.equals("jiangpin")) {

                            List<ProductList> _product = new ArrayList<ProductList>();
                            _product = bean.getProductList();

                            if (null != _product && _product.size() > 0) {
                                list_product.addAll(_product);
                                jiangAdapter = new JiangPinTitleAdapter(FamousBrandClassicActivity.this, list_product);
                                brand_list_gridview.setAdapter(jiangAdapter);
                                jiangAdapter.notifyDataSetChanged();
                            }

                            brand_list_gridview.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid",
                                            list_product.get(position).getId()).putExtra("mode", "jiangpin").putExtra("hava_dianpu", "no"));
                                }
                            });
                        } else {

                            List<FamousList> _goods = new ArrayList<FamousList>();
                            _goods = _bean.getFamousList();

                            if (null != _goods && _goods.size() > 0) {
                                list_goods.addAll(_goods);
                                mAdapter = new TitleAdapter(FamousBrandClassicActivity.this, list_goods);
                                brand_list_gridview.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                            brand_list_gridview.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid",
                                            list_goods.get(position).getId()).putExtra("mode", "shepin").putExtra("hava_dianpu", "no"));
                                }
                            });
                        }
                        break;
                }
            }
        });
    }

    private OnClickListener toolsItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            changeTextColor(v.getId());
            changeTextLocation(v.getId());
            getDataList(v.getId(), 1, 0);
            typeClassic = v.getId();
        }
    };

    private void init() {
        re_right.setOnClickListener(this);
    }

    private class TitleAdapter extends BaseAdapter {

        private ViewHolder mHolder;
        private Context context;
        private List<FamousList> list_goods;

        public TitleAdapter(Context context, List<FamousList> list_goods) {
            this.context = context;
            this.list_goods = list_goods;
        }

        public void setList(List<FamousList> list_goods) {
            this.list_goods = list_goods;
            notifyDataSetChanged();
        }

        public void add(FamousList list_goods) {
            this.list_goods.add(list_goods);
            notifyDataSetChanged();
        }

        public void addFirst(FamousList page) {
            this.list_goods.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<FamousList> list) {
            this.list_goods.addAll(list);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list_goods.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list_goods.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list_goods == null ? 0 : list_goods.size();
        }

        @Override
        public Object getItem(int position) {
            return list_goods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_brand_list, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_goods.get(position).getSpic())
                    .into(mHolder.iv_classic);
            mHolder.tv_brand.setText(list_goods.get(position).getSname());
            mHolder.tv_price.setText("￥" + list_goods.get(position).getSprice());
            return convertView;
        }

    }

    private class JiangPinTitleAdapter extends BaseAdapter {

        private ViewHolder mHolder;
        private Context context;
        private List<ProductList> list_product;

        public JiangPinTitleAdapter(Context context, List<ProductList> list_product) {
            this.context = context;
            this.list_product = list_product;
        }

        public void setList(List<ProductList> list_product) {
            this.list_product = list_product;
            notifyDataSetChanged();
        }

        public void add(ProductList list_product) {
            this.list_product.add(list_product);
            notifyDataSetChanged();
        }

        public void addFirst(ProductList page) {
            this.list_product.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ProductList> list_product) {
            this.list_product.addAll(list_product);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list_product.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list_product.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list_product == null ? 0 : list_product.size();
        }

        @Override
        public Object getItem(int position) {
            return list_product.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_brand_list, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_product.get(position).getSpic())
                    .into(mHolder.iv_classic);
            mHolder.tv_brand.setText(list_product.get(position).getSname());
            mHolder.tv_price.setText("￥" + list_product.get(position).getSprice());
            return convertView;
        }

    }

    public class ViewHolder {
        private ImageView iv_classic;
        TextView tv_brand, tv_price;

        public ViewHolder(View view) {
            iv_classic = (ImageView) view.findViewById(R.id.iv_classic);
            tv_brand = (TextView) view.findViewById(R.id.tv_brand);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_right:
                if (type.equals("jiangpin")) {
                    Intent intent = new Intent(getApplicationContext(), ShopAndBrand.class);// 进去店铺
                    intent.putExtra("type", "jiangpin");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ShopAndBrand.class);// 进去品牌
                    intent.putExtra("type", "shepin");
                    startActivity(intent);
                }
                break;
        }
    }

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

    public class JiangPin {
        List<ProductList> ProductList;

        public List<ProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<ProductList> productList) {
            ProductList = productList;
        }
    }


    public class ProductList {
        String id;
        String sname;
        String spic;
        String sprice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSpic() {
            return spic;
        }

        public void setSpic(String spic) {
            this.spic = spic;
        }

        public String getSprice() {
            return sprice;
        }

        public void setSprice(String sprice) {
            this.sprice = sprice;
        }
    }

    public class ShangPin {
        List<FamousList> FamousList;

        public List<FamousList> getFamousList() {
            return FamousList;
        }

        public void setFamousList(List<FamousList> famousList) {
            FamousList = famousList;
        }

    }

    public class FamousList {
        String id;
        String sname;
        String spic;
        String sprice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getSpic() {
            return spic;
        }

        public void setSpic(String spic) {
            this.spic = spic;
        }

        public String getSprice() {
            return sprice;
        }

        public void setSprice(String sprice) {
            this.sprice = sprice;
        }
    }

    public class ShePingClass {
        List<FamousType> FamousType;

        public List<FamousType> getFamousType() {
            return FamousType;
        }

        public void setFamousType(List<FamousType> famousType) {
            FamousType = famousType;
        }
    }

    public class FamousType {
        String classify_type;
        String description;
        String entityId;
        String id;
        String is_hobby;
        String persistent;
        String tgrade;
        String tname;
        String tpath;
        String tpid;
        String tshow;
        String type_icon;
        String type_icons;
        String type_img;

        public String getClassify_type() {
            return classify_type;
        }

        public void setClassify_type(String classify_type) {
            this.classify_type = classify_type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_hobby() {
            return is_hobby;
        }

        public void setIs_hobby(String is_hobby) {
            this.is_hobby = is_hobby;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public String getTgrade() {
            return tgrade;
        }

        public void setTgrade(String tgrade) {
            this.tgrade = tgrade;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getTpath() {
            return tpath;
        }

        public void setTpath(String tpath) {
            this.tpath = tpath;
        }

        public String getTpid() {
            return tpid;
        }

        public void setTpid(String tpid) {
            this.tpid = tpid;
        }

        public String getTshow() {
            return tshow;
        }

        public void setTshow(String tshow) {
            this.tshow = tshow;
        }

        public String getType_icon() {
            return type_icon;
        }

        public void setType_icon(String type_icon) {
            this.type_icon = type_icon;
        }

        public String getType_icons() {
            return type_icons;
        }

        public void setType_icons(String type_icons) {
            this.type_icons = type_icons;
        }

        public String getType_img() {
            return type_img;
        }

        public void setType_img(String type_img) {
            this.type_img = type_img;
        }
    }

    public class JiangPinClass {
        List<ProductType> ProductType;

        public List<ProductType> getProductType() {
            return ProductType;
        }

        public void setProductType(List<ProductType> productType) {
            ProductType = productType;
        }
    }

    public class ProductType {
        String id;
        String tname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }
    }
}
