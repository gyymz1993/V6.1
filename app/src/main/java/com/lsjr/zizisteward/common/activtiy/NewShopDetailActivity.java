package com.lsjr.zizisteward.common.activtiy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.PullableScrollView;
import com.lsjr.zizisteward.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class NewShopDetailActivity extends BaseActivity implements OnClickListener {
    private String id, title, type;
    private boolean login_state;
    PullToRefreshLayout pull_to_refresh;
    PullableScrollView psv;
    LinearLayout ll;
    ImageView iv_top, iv_care, iv_all, iv_new, iv_intro, price_change;
    RoundImageView iv_logo;
    TextView b_name, tv_care_num, tv_care, tv_all, tv_new, tv_intro, moren, price, cell, tv_jianjie_title, tv_content;
    RelativeLayout re_care, re_all, re_new, re_intro, re_moren, re_price, re_cell;
    MyGridView gv_view;
    int pageNum = 1;
    NewShopBean bean;
    List<LiChenqiList> list = new ArrayList<LiChenqiList>();
    LiChenQiAdapter adapter;
    int xinpin = 0;
    int xiaoliang = 0;
    int jiage = 0;
    RelativeLayout fl;
    LinearLayout ll_second_classic;

    @Override
    public int getContainerView() {
        return R.layout.activity_new_shop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        setmTitle(title);

        pull_to_refresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        psv = (PullableScrollView) findViewById(R.id.psv);
        ll = (LinearLayout) findViewById(R.id.ll);

        iv_top = (ImageView) findViewById(R.id.iv_top);
        iv_logo = (RoundImageView) findViewById(R.id.iv_logo);
        b_name = (TextView) findViewById(R.id.b_name);
        tv_care_num = (TextView) findViewById(R.id.tv_care_num);

        re_care = (RelativeLayout) findViewById(R.id.re_care);
        iv_care = (ImageView) findViewById(R.id.iv_care);
        tv_care = (TextView) findViewById(R.id.tv_care);

        re_all = (RelativeLayout) findViewById(R.id.re_all);
        tv_all = (TextView) findViewById(R.id.tv_all);
        iv_all = (ImageView) findViewById(R.id.iv_all);

        re_new = (RelativeLayout) findViewById(R.id.re_new);
        tv_new = (TextView) findViewById(R.id.tv_new);
        iv_new = (ImageView) findViewById(R.id.iv_new);

        re_intro = (RelativeLayout) findViewById(R.id.re_intro);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        iv_intro = (ImageView) findViewById(R.id.iv_intro);

        ll_second_classic = (LinearLayout) findViewById(R.id.ll_second_classic);

        re_moren = (RelativeLayout) findViewById(R.id.re_moren);
        moren = (TextView) findViewById(R.id.moren);

        re_price = (RelativeLayout) findViewById(R.id.re_price);
        price = (TextView) findViewById(R.id.price);
        price_change = (ImageView) findViewById(R.id.price_change);

        re_cell = (RelativeLayout) findViewById(R.id.re_cell);
        cell = (TextView) findViewById(R.id.cell);

        gv_view = (MyGridView) findViewById(R.id.gv_view);

        fl = (RelativeLayout) findViewById(R.id.fl);
        tv_jianjie_title = (TextView) findViewById(R.id.tv_jianjie_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        if ("shop".equals(type)) {
            tv_intro.setText("店铺简介");
            tv_care.setText("关注店铺");
        } else {
            tv_intro.setText("品牌故事");
            tv_care.setText("关注品牌");
        }
        init();
        initListener();
    }

    private void initListener() {
        gv_view.setVisibility(View.VISIBLE);
        fl.setVisibility(View.GONE);
        iv_new.setVisibility(View.GONE);
        iv_intro.setVisibility(View.GONE);
        re_care.setOnClickListener(this);
        re_new.setOnClickListener(this);
        re_all.setOnClickListener(this);
        re_intro.setOnClickListener(this);
        re_moren.setOnClickListener(this);
        re_price.setOnClickListener(this);
        re_cell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_moren:
                moren.setTextColor(0xffff9900);
                price.setTextColor(0xff000000);
                cell.setTextColor(0xff000000);
                price_change.setImageResource(R.drawable.shop_price_no);
                jiage = 0;
                xiaoliang = 0;

                getShopData(2, xinpin, xiaoliang, jiage);
                break;
            case R.id.re_price:
                moren.setTextColor(0xff000000);
                price.setTextColor(0xffff9900);
                cell.setTextColor(0xff000000);

                xiaoliang = 0;

                if (jiage == 0) {
                    jiage = 1;
                    price_change.setImageResource(R.drawable.shop_price_down);
                    getShopData(2, xinpin, xiaoliang, jiage);
                } else if (jiage == 1) {
                    jiage = 2;
                    price_change.setImageResource(R.drawable.shop_price_up);
                    getShopData(2, xinpin, xiaoliang, jiage);
                } else if (jiage == 2) {
                    jiage = 1;
                    price_change.setImageResource(R.drawable.shop_price_down);
                    getShopData(2, xinpin, xiaoliang, jiage);
                }

                break;
            case R.id.re_cell:
                moren.setTextColor(0xff000000);
                price.setTextColor(0xff000000);
                cell.setTextColor(0xffff9900);
                price_change.setImageResource(R.drawable.shop_price_no);
                jiage = 0;
                xiaoliang = 1;
                getShopData(2, xinpin, xiaoliang, jiage);
                break;
            case R.id.re_new:
                ll_second_classic.setVisibility(View.VISIBLE);

                gv_view.setVisibility(View.VISIBLE);
                fl.setVisibility(View.GONE);

                iv_new.setVisibility(View.VISIBLE);
                tv_new.setTextColor(0xffff9900);

                iv_all.setVisibility(View.GONE);
                tv_all.setTextColor(0xff000000);

                iv_intro.setVisibility(View.GONE);
                tv_intro.setTextColor(0xff000000);

                xinpin = 1;
                getShopData(2, xinpin, xiaoliang, jiage);
                break;
            case R.id.re_all:
                ll_second_classic.setVisibility(View.VISIBLE);

                gv_view.setVisibility(View.VISIBLE);
                fl.setVisibility(View.GONE);

                iv_all.setVisibility(View.VISIBLE);
                tv_all.setTextColor(0xffff9900);

                iv_new.setVisibility(View.GONE);
                tv_new.setTextColor(0xff000000);

                iv_intro.setVisibility(View.GONE);
                tv_intro.setTextColor(0xff000000);

                xinpin = 0;

                getShopData(2, xinpin, xiaoliang, jiage);
                break;
            case R.id.re_intro:
                ll_second_classic.setVisibility(View.GONE);

                iv_all.setVisibility(View.GONE);
                tv_all.setTextColor(0xff000000);

                iv_new.setVisibility(View.GONE);
                tv_new.setTextColor(0xff000000);

                iv_intro.setVisibility(View.VISIBLE);
                tv_intro.setTextColor(0xffff9900);

                gv_view.setVisibility(View.GONE);
                fl.setVisibility(View.VISIBLE);

                tv_jianjie_title.setText(bean.getBrand().getBname());
                tv_content.setText(bean.getBrand().getBstory());

                break;
            case R.id.re_care:
                if (login_state == true) {
                    if (tv_care.getText().toString().trim().equals("关注店铺") || tv_care.getText().toString().trim().equals("关注品牌")) {
                        // 去关注
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OPT", "332");
                        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                        map.put("collect_id", id);
                        map.put("collect_type", "3");
                        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String msg = jsonObject.getString("msg");
                                    String num = jsonObject.getString("brandrow");
                                    ToastUtils.show(getApplicationContext(), msg);
                                    iv_care.setImageResource(R.drawable.shop_cancel_care);
                                    tv_care.setText("取消关注");
                                    tv_care_num.setText(num + "关注");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        // 取消关注
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OPT", "332");
                        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                        map.put("collect_id", id);
                        map.put("collect_type", "3");
                        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String msg = jsonObject.getString("msg");
                                    String num = jsonObject.getString("brandrow");
                                    ToastUtils.show(getApplicationContext(), msg);
                                    iv_care.setImageResource(R.drawable.shop_care);
                                    if ("shop".equals(type)) {
                                        tv_care.setText("关注店铺");
                                    } else {
                                        tv_care.setText("关注品牌");
                                    }
                                    tv_care_num.setText(num + "关注");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    }
                } else {
                    ToastUtils.show(getApplicationContext(), "您还未登录,请先登录");
                }
                break;
        }
    }

    private void init() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightP = widthPixels / 3;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) iv_top.getLayoutParams();
        linearParams.width = widthPixels;
        linearParams.height = heightP;
        iv_top.setLayoutParams(linearParams);

        gv_view.setFocusable(false);
        pull_to_refresh.setOnRefreshListener(new Listener());
        getData();
        getShopData(2, xinpin, xiaoliang, jiage);
        gv_view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), HomeBrandDetail.class);
                intent.putExtra("sid", list.get(position).getId());
                if (type.equals("shop")) {
                    intent.putExtra("mode", "jiangpin");
                } else {
                    intent.putExtra("mode", "shepin");
                }
                intent.putExtra("hava_dianpu", "yes");
                startActivity(intent);
            }
        });
    }

    private class Listener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getShopData(0, xinpin, xiaoliang, jiage);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (ll.getVisibility() == View.VISIBLE) {
                pageNum++;
                getShopData(1, xinpin, xiaoliang, jiage);
            } else {
                pull_to_refresh.loadmoreFinish(pullToRefreshLayout.SUCCEED);
            }
        }

    }

    private void getData() {
        login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "325");
        if (login_state == true) {
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        } else {
            map.put("user_id", "");
        }
        map.put("brand_id", id);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("李晨奇" + result);
                bean = GsonUtil.getInstance().fromJson(result, NewShopBean.class);
                Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + bean.getBrand().getBimg()).into(iv_top);
                Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + bean.getBrand().getBlogo())
                        .into(iv_logo);
                b_name.setText(bean.getBrand().getBname());
                tv_care_num.setText(bean.getBrandrow() + "关注");
                if (bean.getWhether().equals("0")) {
                    iv_care.setImageResource(R.drawable.shop_care);
                    if ("shop".equals(type)) {
                        tv_care.setText("关注店铺");
                    } else {
                        tv_care.setText("关注品牌");
                    }
                } else {
                    iv_care.setImageResource(R.drawable.shop_cancel_care);
                    tv_care.setText("取消关注");
                }

            }
        });
    }

    public void getShopData(final int mode, int isNew, int sell_type, int sprice) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "326");
        map.put("brand_id", id);
        map.put("Price", String.valueOf(sprice));
        map.put("volume", String.valueOf(sell_type));
        map.put("News", String.valueOf(isNew));
        map.put("currPage", String.valueOf(pageNum));
        map.put("pageSize", "");

        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("商品" + result);
                AllShop bean = GsonUtil.getInstance().fromJson(result, AllShop.class);
                switch (mode) {
                    case 0:// 刷新
                        list = bean.getPageshop().getPage();
                        adapter = new LiChenQiAdapter(NewShopDetailActivity.this, list);
                        gv_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (null != list && list.size() > 5) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }
                        pull_to_refresh.refreshFinish(pull_to_refresh.SUCCEED);
                        break;
                    case 1:// 加载更多
                        if (bean.getPageshop().getPage() != null) {
                            List<LiChenqiList> list_more = new ArrayList<LiChenqiList>();
                            list_more = bean.getPageshop().getPage();

                            if (list_more.size() > 0) {
                                list.addAll(list_more);
                                adapter.setList(list);
                                adapter.notifyDataSetChanged();
                            }

                            if (null != list_more && list_more.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                        }
                        pull_to_refresh.loadmoreFinish(pull_to_refresh.SUCCEED);
                        break;
                    case 2:// 进来就加载
                        list = bean.getPageshop().getPage();
                        adapter = new LiChenQiAdapter(NewShopDetailActivity.this, list);
                        gv_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;

                }

            }
        });
    }

    private class LiChenQiAdapter extends BaseAdapter {

        private ViewHolder mHolder;
        private Context context;
        private List<LiChenqiList> list;

        public LiChenQiAdapter(Context context, List<LiChenqiList> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<LiChenqiList> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(LiChenqiList list) {
            this.list.add(list);
            notifyDataSetChanged();
        }

        public void addFirst(LiChenqiList page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<LiChenqiList> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpic()).into(mHolder.iv_classic);
            mHolder.tv_brand.setText(list.get(position).getSname());
            mHolder.tv_price.setText("￥" + list.get(position).getSprice());
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

    private class AllShop {
        private Pageshop Pageshop;

        public Pageshop getPageshop() {
            return Pageshop;
        }

        public void setPageshop(Pageshop pageshop) {
            Pageshop = pageshop;
        }

    }

    private class Pageshop {
        private int currPage;
        private List<LiChenqiList> page;
        private int pageSize;
        private String pageTitle;
        private int totalCount;
        private int totalPageCount;

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<LiChenqiList> getPage() {
            return page;
        }

        public void setPage(List<LiChenqiList> page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPageCount() {
            return totalPageCount;
        }

        public void setTotalPageCount(int totalPageCount) {
            this.totalPageCount = totalPageCount;
        }
    }

    private class LiChenqiList {
        private String id;
        private String sname;
        private String spic;
        private String sprice;

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

    private class NewShopBean {
        private Brand brand;
        private String brandrow;
        private String error;
        private String msg;
        private String whether;

        public Brand getBrand() {
            return brand;
        }

        public void setBrand(Brand brand) {
            this.brand = brand;
        }

        public String getBrandrow() {
            return brandrow;
        }

        public void setBrandrow(String brandrow) {
            this.brandrow = brandrow;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getWhether() {
            return whether;
        }

        public void setWhether(String whether) {
            this.whether = whether;
        }
    }

    private class Brand {
        private String bdesc;
        private String bimg;
        private String bis_show;
        private String blogo;
        private String bname;
        private String brand_time;
        private String brand_type;
        private String bsort;
        private String bstory;
        private String burl;
        private String entityId;
        private String id;
        private String persistent;

        public String getBdesc() {
            return bdesc;
        }

        public void setBdesc(String bdesc) {
            this.bdesc = bdesc;
        }

        public String getBimg() {
            return bimg;
        }

        public void setBimg(String bimg) {
            this.bimg = bimg;
        }

        public String getBis_show() {
            return bis_show;
        }

        public void setBis_show(String bis_show) {
            this.bis_show = bis_show;
        }

        public String getBlogo() {
            return blogo;
        }

        public void setBlogo(String blogo) {
            this.blogo = blogo;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getBrand_time() {
            return brand_time;
        }

        public void setBrand_time(String brand_time) {
            this.brand_time = brand_time;
        }

        public String getBrand_type() {
            return brand_type;
        }

        public void setBrand_type(String brand_type) {
            this.brand_type = brand_type;
        }

        public String getBsort() {
            return bsort;
        }

        public void setBsort(String bsort) {
            this.bsort = bsort;
        }

        public String getBstory() {
            return bstory;
        }

        public void setBstory(String bstory) {
            this.bstory = bstory;
        }

        public String getBurl() {
            return burl;
        }

        public void setBurl(String burl) {
            this.burl = burl;
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

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }
    }

}
