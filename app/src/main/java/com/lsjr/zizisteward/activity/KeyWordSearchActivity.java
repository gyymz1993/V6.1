package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
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
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.SearchBean;
import com.lsjr.zizisteward.bean.SearchBean.PingPaiList;
import com.lsjr.zizisteward.bean.SearchBean.ShangPingList;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.PullableScrollView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWordSearchActivity extends Activity implements OnClickListener {
    private TextView tv_get_search_word, quxiao, tv_brand;
    private RelativeLayout search_kuang;
    private String keyword;
    private List<ShangPingList> list_shangpin = new ArrayList<ShangPingList>();// 商品
    private List<PingPaiList> list_pinpai = new ArrayList<PingPaiList>();// 品牌
    private MyListView listview_shangpin;// 商品
    private BrandListAdapter adapter;// 商品
    private HorizontalScrollView scrollview;
    private LinearLayout ll_parent;
    private TextView toolsTextviews[];
    private ImageView images[];
    private View views[];
    private PullToRefreshLayout pullto_refre;
    private PullableScrollView psv;
    private LinearLayout ll;
    private int pageNum = 1;
    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_search);
        search_kuang = (RelativeLayout) findViewById(R.id.search_kuang);// 搜索框
        quxiao = (TextView) findViewById(R.id.quxiao);// 取消按钮
        tv_get_search_word = (TextView) findViewById(R.id.tv_get_search_word);// 搜索文字
        pullto_refre = (PullToRefreshLayout) findViewById(R.id.pullto_refre);
        psv = (PullableScrollView) findViewById(R.id.psv);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        listview_shangpin = (MyListView) findViewById(R.id.listview_shangpin);// 商品
        ll = (LinearLayout) findViewById(R.id.ll);
        keyword = getIntent().getStringExtra("keyword");
        tv_get_search_word.setText(keyword);
        state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        listview_shangpin.setFocusable(false);
        pullto_refre.setOnRefreshListener(new BrandListener());

        initLayout();
        getBaseData(2);
        listview_shangpin.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KeyWordSearchActivity.this, HomeBrandDetail.class);// 商品对应的商品详情
                intent.putExtra("sid", list_shangpin.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void getBaseData(final int mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "35");
        map.put("skeyword", keyword);
        if (state == false) {
            map.put("user_id", "");
        } else {
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        }
        map.put("currPage", String.valueOf(pageNum));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @SuppressWarnings("static-access")
            @Override
            public void onSuccess(String result) {
                System.out.println("搜索结果" + result);
                SearchBean bean = GsonUtil.getInstance().fromJson(result, SearchBean.class);
                switch (mode) {
                    case 0:// 刷新
                        list_shangpin = bean.getShops();
                        adapter = new BrandListAdapter(KeyWordSearchActivity.this, list_shangpin);
                        listview_shangpin.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (null != list_shangpin && list_shangpin.size() > 5) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }
                        pullto_refre.refreshFinish(pullto_refre.SUCCEED);
                        break;
                    case 1:// 加载更多
                        if (bean.getShops() != null) {
                            List<ShangPingList> list_more = new ArrayList<ShangPingList>();
                            list_more = bean.getShops();

                            if (list_more.size() > 0) {
                                list_shangpin.addAll(list_more);
                                adapter.setList(list_shangpin);
                                adapter.notifyDataSetChanged();
                            }

                            if (null != list_more && list_more.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                        }
                        pullto_refre.loadmoreFinish(pullto_refre.SUCCEED);
                        break;
                    case 2:// 进来就加载
                        list_shangpin = bean.getShops();
                        list_pinpai = bean.getBrands();
                        if (list_shangpin.size() > 0 || list_pinpai.size() > 0) {
                            adapter = new BrandListAdapter(KeyWordSearchActivity.this, list_shangpin);
                            listview_shangpin.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (null != list_shangpin && list_shangpin.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }
                        }

                        if (list_pinpai.size() == 0 && list_shangpin.size() == 0) {
                            scrollview.setVisibility(View.GONE);
                            psv.setVisibility(View.GONE);
                        }

					/* 品牌横向排列滑动 **/
                        if (list_pinpai.size() > 0) {
                            scrollview.setVisibility(View.VISIBLE);
                            toolsTextviews = new TextView[list_pinpai.size()];
                            images = new ImageView[list_pinpai.size()];
                            views = new View[list_pinpai.size()];
                            for (int i = 0; i < list_pinpai.size(); i++) {
                                View view = getLayoutInflater().from(getApplicationContext())
                                        .inflate(R.layout.item_brand_head, null);
                                view.setId(i);
                                view.setOnClickListener(toolsItemListener);
                                TextView textView = (TextView) view.findViewById(R.id.tv_brand);
                                textView.setText(list_pinpai.get(i).getBname());
                                ImageView iv_brand = (ImageView) view.findViewById(R.id.iv_brand);
                                Glide.with(getApplicationContext())
                                        .load(HttpConfig.IMAGEHOST + list_pinpai.get(i).getBlogo()).into(iv_brand);
                                ll_parent.addView(view);
                                toolsTextviews[i] = textView;
                                images[i] = iv_brand;
                                views[i] = view;
                            }

                        } else {
                            scrollview.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });

    }

    private class BrandListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getBaseData(0);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (ll.getVisibility() == View.VISIBLE) {
                pageNum++;
                getBaseData(1);
            } else {
                pullto_refre.loadmoreFinish(pullToRefreshLayout.SUCCEED);
            }
        }

    }

    private void initLayout() {
        search_kuang.setOnClickListener(this);
        quxiao.setOnClickListener(this);
    }

    private OnClickListener toolsItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(KeyWordSearchActivity.this, BrandToGoodsListActivity.class);// 品牌对应的商品列表
            System.out.println("什么鬼" + v.getId());
            intent.putExtra("id", list_pinpai.get(v.getId()).getId());
            startActivity(intent);
        }
    };

    public class BrandListAdapter extends BaseAdapter {
        private Context context;
        private List<ShangPingList> list_shangpin;

        public BrandListAdapter(Context context, List<ShangPingList> list_shangpin) {
            this.context = context;
            this.list_shangpin = list_shangpin;
        }

        public void setList(List<ShangPingList> list_shangpin) {
            this.list_shangpin = list_shangpin;
            notifyDataSetChanged();
        }

        public void add(ShangPingList page) {
            this.list_shangpin.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ShangPingList page) {
            this.list_shangpin.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ShangPingList> list_shangpin) {
            this.list_shangpin.addAll(list_shangpin);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list_shangpin.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list_shangpin.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list_shangpin == null ? 0 : list_shangpin.size();
        }

        @Override
        public Object getItem(int position) {
            return list_shangpin.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_brand_list_search,
                        parent, false);
            }
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int widthPixels = dm.widthPixels;
            ImageView iv_search_brand = BaseViewHolder.get(convertView, R.id.iv_search_brand);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_search_brand.getLayoutParams();
            params1.height = widthPixels * 1 / 4;
            params1.width = widthPixels * 1 / 4;
            iv_search_brand.setLayoutParams(params1);
            TextView tv_search_name = BaseViewHolder.get(convertView, R.id.tv_search_name);
            Picasso.with(getApplicationContext())
                    .load(HttpConfig.IMAGEHOST + list_shangpin.get(position).getSpicfirst()).into(iv_search_brand);
            tv_search_name.setText(list_shangpin.get(position).getSname());
            return convertView;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_kuang:
                finish();
                break;
            case R.id.quxiao:
                Intent intent = new Intent(this, SixthNewActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, SixthNewActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
