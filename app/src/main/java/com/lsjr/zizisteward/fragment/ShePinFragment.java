package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.lsjr.zizisteward.activity.YangBenActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.lcq.activity.ShopAndFamousClassicActivity;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.PullableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShePinFragment extends Fragment {
    private View rootView;
    private PullToRefreshLayout pullto_refre;
    private PullableScrollView psv;
    private TextView tv_name;
    private RelativeLayout classic;
    private ImageView iv_one, iv_two;
    private MyListView listview;
    private LinearLayout ll;
    private ShePinBean mBean;
    private List<PicList> banner_list = new ArrayList<PicList>();
    private List<DataList> list = new ArrayList<DataList>();
    private int pageNum = 1;
    private ShePinList mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_new_jiangpin, null);
            pullto_refre = (PullToRefreshLayout) rootView.findViewById(R.id.pullto_refre);
            psv = (PullableScrollView) rootView.findViewById(R.id.psv);
            tv_name = (TextView) rootView.findViewById(R.id.tv_name);// 精选匠品或者精品
            classic = (RelativeLayout) rootView.findViewById(R.id.classic);// 分类按钮
            iv_one = (ImageView) rootView.findViewById(R.id.iv_one);// 图片1
            iv_two = (ImageView) rootView.findViewById(R.id.iv_two);// 图片2
            listview = (MyListView) rootView.findViewById(R.id.listview);
            ll = (LinearLayout) rootView.findViewById(R.id.ll);// 上拉加载更多
            listview.setFocusable(false);
            pullto_refre.setOnRefreshListener(new LiChenqiListener());
            tv_name.setText("精选奢品");
            getData(2);
            initListener();
            listview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getContext(), HomeBrandDetail.class).putExtra("sid",
                            list.get(position).getId()).putExtra("mode", "shepin").putExtra("hava_dianpu", "no"));
                }
            });

        }
        return rootView;
    }

    private void initListener() {
        classic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), FamousBrandClassicActivity.class).putExtra("type", "shepin"));
                startActivity(new Intent(getContext(), ShopAndFamousClassicActivity.class).putExtra("type", "shepin"));
            }
        });
    }

    private class LiChenqiListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getData(0);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (ll.getVisibility() == View.VISIBLE) {
                pageNum++;
                getData(1);
            } else {
                pullto_refre.loadmoreFinish(pullToRefreshLayout.SUCCEED);
            }
        }

    }

    private void getData(final int mode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "423");
        map.put("currPage", String.valueOf(pageNum));
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("奢品" + result);
                mBean = GsonUtil.getInstance().fromJson(result, ShePinBean.class);

                switch (mode) {
                    case 0:// 刷新
                        list = mBean.getProducts();
                        mAdapter = new ShePinList(getContext(), list);
                        listview.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        if (null != list && list.size() > 5) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }
                        pullto_refre.refreshFinish(pullto_refre.SUCCEED);
                        break;
                    case 1:// 加载更多
                        if (mBean.getProducts() != null) {
                            List<DataList> list_more = new ArrayList<DataList>();
                            list_more = mBean.getProducts();

                            if (list_more.size() > 0) {
                                list.addAll(list_more);
                                mAdapter.setList(list);
                                mAdapter.notifyDataSetChanged();
                            }

                            if (null != list_more && list_more.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                        }
                        pullto_refre.loadmoreFinish(pullto_refre.SUCCEED);
                        break;
                    case 2:// 进来就加载的
                        list = mBean.getProducts();
                        mAdapter = new ShePinList(getContext(), list);
                        listview.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        if (null != list && list.size() > 5) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }
                        break;

                }

                banner_list = mBean.getBanner();
                Glide.with(getContext()).load(HttpConfig.IMAGEHOST + banner_list.get(0).getImage_filename())
                        .into(iv_one);
                Glide.with(getContext()).load(HttpConfig.IMAGEHOST + banner_list.get(1).getImage_filename())
                        .into(iv_two);
                iv_one.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), YangBenActivity.class).putExtra("url",
                                banner_list.get(0).getUrl()).putExtra("mode", "shepin"));
                    }
                });
                iv_two.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), YangBenActivity.class).putExtra("url",
                                banner_list.get(1).getUrl()).putExtra("mode", "shepin"));
                    }
                });

            }

        });
    }

    public class ShePinList extends BaseAdapter {

        private ViewHolder mHolder;
        private List<DataList> list;
        private Context context;

        public ShePinList(Context context, List<DataList> list) {
            this.list = list;
            this.context = context;
        }

        public void setList(List<DataList> list) {
            this.list = list;
        }

        public void add(DataList page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(DataList page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<DataList> list) {
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shepin, parent, false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int widthPixels = dm.widthPixels;
            int dip2px = DensityUtil.dip2px(getContext(), 20);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mHolder.mIv_recommend.getLayoutParams();
            params.width = widthPixels - dip2px;
            params.height = (widthPixels - dip2px) * 10 / 17;
            mHolder.mIv_recommend.setLayoutParams(params);

            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpic()).into(mHolder.mIv_recommend);
            mHolder.mTv_one.setText(list.get(position).getBname());
            mHolder.mTv_two.setText(list.get(position).getSname());
            return convertView;

        }
    }

    private class ViewHolder {
        private ImageView mIv_recommend;
        private TextView mTv_one;
        private TextView mTv_two;

        public ViewHolder(View view) {
            mIv_recommend = (ImageView) view.findViewById(R.id.iv_recommend);
            mTv_one = (TextView) view.findViewById(R.id.tv_one);
            mTv_two = (TextView) view.findViewById(R.id.tv_two);
        }
    }

    public class ShePinBean {
        private List<PicList> banner;
        private String error;
        private String msg;
        private List<DataList> products;

        public List<PicList> getBanner() {
            return banner;
        }

        public void setBanner(List<PicList> banner) {
            this.banner = banner;
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

        public List<DataList> getProducts() {
            return products;
        }

        public void setProducts(List<DataList> products) {
            this.products = products;
        }
    }

    public class PicList {
        private String id;
        private String image_filename;
        private String location;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage_filename() {
            return image_filename;
        }

        public void setImage_filename(String image_filename) {
            this.image_filename = image_filename;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class DataList {
        private String bname;
        private String id;
        private String sbrand;
        private String sell_points;
        private String skeyword;
        private String sname;
        private String spic;

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSbrand() {
            return sbrand;
        }

        public void setSbrand(String sbrand) {
            this.sbrand = sbrand;
        }

        public String getSell_points() {
            return sell_points;
        }

        public void setSell_points(String sell_points) {
            this.sell_points = sell_points;
        }

        public String getSkeyword() {
            return skeyword;
        }

        public void setSkeyword(String skeyword) {
            this.skeyword = skeyword;
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
    }
}
