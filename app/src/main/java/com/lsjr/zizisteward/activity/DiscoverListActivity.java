package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BaskListBean;
import com.lsjr.zizisteward.bean.BaskListBean.BaskListDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class DiscoverListActivity extends BaseActivity {

    private MyGridView mGridview;

    @Override
    public int getContainerView() {
        return R.layout.activity_my_bask;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridview = (MyGridView) findViewById(R.id.gridview);
        int px = DensityUtil.dip2px(this, 1);
        mGridview.setHorizontalSpacing(px);
        mGridview.setVerticalSpacing(px);
        getData();
        initLayout();

    }

    private void initLayout() {
        mAdapter = new MyAdapter(DiscoverListActivity.this, list);
        mGridview.setAdapter(mAdapter);
    }

    private int pageNum = 1;
    ;
    private BaskListBean mBean;
    private List<BaskListDetail> list = new ArrayList<BaskListDetail>();
    private MyAdapter mAdapter;

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "29");
        map.put("currPage", String.valueOf(pageNum++));
        map.put("typeid", "");

        new HttpClientGet(DiscoverListActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("晒单列表" + result);
                mBean = GsonUtil.getInstance().fromJson(result, BaskListBean.class);
                list = mBean.getPage().getPage();
                if (mBean != null && "1".equals(mBean.getError())) {
                    if (pageNum != 1) {

                    }
                }

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

    }

    public class MyAdapter extends BaseAdapter {
        private Context context;
        private List<BaskListDetail> list;

        public MyAdapter(Context context, List<BaskListDetail> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<BaskListDetail> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(BaskListDetail page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(BaskListDetail page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<BaskListDetail> list) {
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
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(DiscoverListActivity.this).inflate(R.layout.item_mybask, parent,
                        false);
            }
            ImageView iv_classic = BaseViewHolder.get(convertView, R.id.iv_classic);
            TextView tv_time = BaseViewHolder.get(convertView, R.id.tv_time);
            String shareImg = list.get(position).getShareImg();
            String[] pics = shareImg.split(",");
            Picasso.with(context).load(HttpConfig.IMAGEHOST + pics[0]).into(iv_classic);
            String share_time = list.get(position).getShare_time().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(Long.valueOf(share_time));
            tv_time.setText(time);

            return convertView;
        }

    }

}
