package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalExaminationMoreActivity extends Activity implements OnClickListener {

    private String title;
    private MyListView mlv;
    private int pageNum = 1;
    private int pageSize = 8;
    private TextView tv_title;
    private PEMAdapter adapter;
    private LinearLayout ll_back;
    private LinearLayout ll_load;
    private PullToRefreshLayout ptrl;
    private List<MoreBean.Shop> shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.physical_examination_more_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.mlv = (MyListView) super.findViewById(R.id.mlv);
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_load = (LinearLayout) super.findViewById(R.id.ll_load);
        this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.ptrl);

        this.ptrl.setOnRefreshListener(new MyListener());

        this.ll_back.setOnClickListener(this);

        this.getData(0);
    }

    private void getData(final int space) {
        this.title = getIntent().getStringExtra("title");

        this.tv_title.setText(title);

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "303");
        map.put("currPage", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("More_types", title);

        new HttpClientGet(this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                MoreBean mBean = new Gson().fromJson(result, MoreBean.class);

                switch (space) {
                    case 0:
                        shop = mBean.getShop();
                        adapter = new PEMAdapter(PhysicalExaminationMoreActivity.this, shop);
                        mlv.setAdapter(adapter);
                        break;

                    case 1:
                        adapter = null;
                        shop = null;
                        shop = mBean.getShop();
                        adapter = new PEMAdapter(PhysicalExaminationMoreActivity.this, shop);
                        mlv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        break;

                    case 2:
                        List<MoreBean.Shop> shop_space = new ArrayList<>();
                        shop_space = mBean.getShop();

                        if (null != shop_space && shop_space.size() > 0) {
                            shop.addAll(shop_space);
                            adapter = new PEMAdapter(PhysicalExaminationMoreActivity.this, shop);
                            mlv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                        break;
                }

                if (null != shop && shop.size() > 5) {
                    ll_load.setVisibility(View.VISIBLE);
                } else {
                    ll_load.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

        this.mlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(PhysicalExaminationMoreActivity.this, PhysicaDetailsActivity.class).putExtra("id", shop.get(position).getId()));
            }
        });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getData(1);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pageNum++;
            getData(2);
        }
    }

    private class PEMAdapter extends BaseAdapter {

        private ViewHolder view;
        private Context context;
        private List<MoreBean.Shop> shop;

        public PEMAdapter(Context context, List<MoreBean.Shop> shop) {
            this.context = context;
            this.shop = shop;
        }

        @Override
        public int getCount() {
            return null == shop ? 0 : shop.size();
        }

        @Override
        public Object getItem(int position) {
            return shop.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.physical_examination_more_activity_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            this.view.tv_service_name.setText(shop.get(position).getSname());
            this.view.tv_company_name.setText(shop.get(position).getBname());
            this.view.tv_price.setText("¥ " + shop.get(position).getSprice());
//			this.view.tv_original_price.setText("¥ " + shop.get(position).getCost_price());
//			
//			this.view.tv_original_price.getPaint().setAntiAlias(true);//抗锯齿
//
//			this.view.tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰

            Picasso.with(context).load(HttpConfig.IMAGEHOST + shop.get(position).getSpicfirst()).into(view.iv);

            return convertView;
        }

        private class ViewHolder {
            /**推荐*/
            /**
             * 图片
             */
            private ImageView iv;
            /**
             * 服务名称
             */
            private TextView tv_service_name;
            /**
             * 公司名字
             */
            private TextView tv_company_name;
            /**
             * 原价
             */
            private TextView tv_original_price;
            private LinearLayout ll_service;
            private TextView tv_price;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
                this.tv_price = (TextView) v.findViewById(R.id.tv_price);
                this.ll_service = (LinearLayout) v.findViewById(R.id.ll_service);
                this.tv_service_name = (TextView) v.findViewById(R.id.tv_service_name);
                this.tv_company_name = (TextView) v.findViewById(R.id.tv_company_name);
                this.tv_original_price = (TextView) v.findViewById(R.id.tv_original_price);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
