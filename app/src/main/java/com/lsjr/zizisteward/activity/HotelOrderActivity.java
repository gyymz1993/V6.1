package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class HotelOrderActivity extends BaseActivity {
    private List<GrogshopService> list = new ArrayList<GrogshopService>();
    private SuperListView mSlv_all;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private HotelListAdapter mAdapter;
    String destination, Checktime, keyword, Starprice, Yourtime;
    private ImageView mIv_no;
    private TextView mTv_no;

    @Override
    public int getContainerView() {
        return R.layout.activity_order_hotel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("全部酒店");
        Intent intent = getIntent();
        destination = intent.getStringExtra("destination");
        Checktime = intent.getStringExtra("Checktime");
        Yourtime = intent.getStringExtra("Yourtime");
        keyword = intent.getStringExtra("keyword");
        Starprice = intent.getStringExtra("Starprice");
        mSlv_all = (SuperListView) findViewById(R.id.slv_all);
        View hotel_view = getLayoutInflater().inflate(R.layout.hotel_head, null);
        mTv_no = (TextView) hotel_view.findViewById(R.id.tv_no);
        mIv_no = (ImageView) hotel_view.findViewById(R.id.iv_no);
        mSlv_all.addHeaderView(hotel_view);
        mSlv_all.setVerticalScrollBarEnabled(false);
        init();
    }

    private void init() {
        mAdapter = new HotelListAdapter(HotelOrderActivity.this, list);
        mSlv_all.setAdapter(mAdapter);
        mSlv_all.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });
        mSlv_all.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        mSlv_all.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HotelOrderActivity.this, HotelDetailActivity.class);
                intent.putExtra("id", list.get(position - 2).getId());
                intent.putExtra("imagePath", list.get(position - 2).getSpic());
                intent.putExtra("Checktime", Checktime);
                intent.putExtra("Yourtime", Yourtime);
                startActivity(intent);
            }
        });
        mSlv_all.refresh();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "310");
        map.put("currPage", String.valueOf(pageNum++));
        map.put("ad_city_id", "420100");
        map.put("destination", destination);
        map.put("Checktime", Checktime);
        map.put("keyword", keyword);
        map.put("Starprice", Starprice);
        map.put("Yourtime", Yourtime);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("酒店列表" + result);
                HotelList bean = GsonUtil.getInstance().fromJson(result, HotelList.class);
                if (bean.getWhether().equals("0")) {
                    mTv_no.setVisibility(View.VISIBLE);
                    mIv_no.setVisibility(View.VISIBLE);
                    mIv_no.setImageResource(R.drawable.nohotel);
                    mTv_no.setText("酒店推荐");
                    System.out.println("没有找到合适的酒店");
                } else {
                    mTv_no.setVisibility(View.GONE);
                    mIv_no.setVisibility(View.GONE);
                    System.out.println("找到了合适的酒店");
                }
                if (null != bean && "1".equals(bean.getError())) {

                    if (1 != pageNum) {
                        list.addAll(bean.getGrogshopService());
                        mAdapter.setList(list);
                    } else {
                        list = bean.getGrogshopService();
                        mAdapter = new HotelListAdapter(HotelOrderActivity.this, list);
                        mSlv_all.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                if (list.size() < bean.getGrogshopService().size()) {
                    mSlv_all.setIsLoadFull(false);
                }

                mSlv_all.finishRefresh();
                mSlv_all.finishLoadMore();

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    public class HotelListAdapter extends BaseAdapter {
        private List<GrogshopService> list;
        private Context context;
        private ViewHolder mHolder;

        public HotelListAdapter(Context context, List<GrogshopService> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<GrogshopService> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(GrogshopService page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(GrogshopService page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<GrogshopService> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_hotel_list, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.mTv_name.setText(list.get(position).getSname());
            mHolder.mTv_label.setText(list.get(position).getSkeyword());
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpic()).into(mHolder.mIv_name);
            if ("1".equals(list.get(position).getHotel_level())) {
                mHolder.mXing_one.setVisibility(View.VISIBLE);
                mHolder.mXing_two.setVisibility(View.GONE);
                mHolder.mXing_three.setVisibility(View.GONE);
                mHolder.mXing_four.setVisibility(View.GONE);
                mHolder.mXing_five.setVisibility(View.GONE);
            } else if ("2".equals(list.get(position).getHotel_level())) {
                mHolder.mXing_one.setVisibility(View.VISIBLE);
                mHolder.mXing_two.setVisibility(View.VISIBLE);
                mHolder.mXing_three.setVisibility(View.GONE);
                mHolder.mXing_four.setVisibility(View.GONE);
                mHolder.mXing_five.setVisibility(View.GONE);
            } else if ("3".equals(list.get(position).hotel_level)) {
                mHolder.mXing_one.setVisibility(View.VISIBLE);
                mHolder.mXing_two.setVisibility(View.VISIBLE);
                mHolder.mXing_three.setVisibility(View.VISIBLE);
                mHolder.mXing_four.setVisibility(View.GONE);
                mHolder.mXing_five.setVisibility(View.GONE);
            } else if ("4".equals(list.get(position).getHotel_level())) {
                mHolder.mXing_one.setVisibility(View.VISIBLE);
                mHolder.mXing_two.setVisibility(View.VISIBLE);
                mHolder.mXing_three.setVisibility(View.VISIBLE);
                mHolder.mXing_four.setVisibility(View.VISIBLE);
                mHolder.mXing_five.setVisibility(View.GONE);
            } else if ("5".equals(list.get(position).getHotel_level())) {
                mHolder.mXing_one.setVisibility(View.VISIBLE);
                mHolder.mXing_two.setVisibility(View.VISIBLE);
                mHolder.mXing_three.setVisibility(View.VISIBLE);
                mHolder.mXing_four.setVisibility(View.VISIBLE);
                mHolder.mXing_five.setVisibility(View.VISIBLE);
            } else if ("0".equals(list.get(position).getHotel_level())) {
                mHolder.mXing_one.setVisibility(View.GONE);
                mHolder.mXing_two.setVisibility(View.GONE);
                mHolder.mXing_three.setVisibility(View.GONE);
                mHolder.mXing_four.setVisibility(View.GONE);
                mHolder.mXing_five.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    public class ViewHolder {
        private ImageView mIv_name;
        private TextView mTv_name;
        private TextView mTv_label;
        private ImageView mXing_one;
        private ImageView mXing_two;
        private ImageView mXing_three;
        private ImageView mXing_four;
        private ImageView mXing_five;

        public ViewHolder(View view) {
            mIv_name = (ImageView) view.findViewById(R.id.iv_name);
            mTv_name = (TextView) view.findViewById(R.id.tv_name);
            mTv_label = (TextView) view.findViewById(R.id.tv_label);
            mXing_one = (ImageView) view.findViewById(R.id.xing_one);
            mXing_two = (ImageView) view.findViewById(R.id.xing_two);
            mXing_three = (ImageView) view.findViewById(R.id.xing_three);
            mXing_four = (ImageView) view.findViewById(R.id.xing_four);
            mXing_five = (ImageView) view.findViewById(R.id.xing_five);
        }
    }

    public class HotelList {
        private List<GrogshopService> GrogshopService;
        private String error;
        private String msg;
        private String Whether;

        public String getWhether() {
            return Whether;
        }

        public void setWhether(String whether) {
            Whether = whether;
        }

        public List<GrogshopService> getGrogshopService() {
            return GrogshopService;
        }

        public void setGrogshopService(List<GrogshopService> grogshopService) {
            GrogshopService = grogshopService;
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
    }

    public class GrogshopService {
        private String hotel_level;
        private String is_approve;
        private String article_number;
        private String audit;
        private String cost_price;
        private String entityId;
        private String feature_item;
        private String id;
        private String mname;
        private String persistent;
        private String putaway;
        private String samount;
        private String sbin;
        private String sbrand;
        private String scolour;
        private String scount;
        private String sell_points;
        private String shop_address;
        private String shop_type;
        private String shot;
        private String simg;
        private String sinfo;
        private String sisrec;
        private String size;
        private String skeyword;
        private String sname;
        private String snew;
        private String spic;
        private String spid;
        private String sprice;
        private String tdapid;
        private String themeId;

        public String getHotel_level() {
            return hotel_level;
        }

        public void setHotel_level(String hotel_level) {
            this.hotel_level = hotel_level;
        }

        public String getIs_approve() {
            return is_approve;
        }

        public void setIs_approve(String is_approve) {
            this.is_approve = is_approve;
        }

        public String getArticle_number() {
            return article_number;
        }

        public void setArticle_number(String article_number) {
            this.article_number = article_number;
        }

        public String getAudit() {
            return audit;
        }

        public void setAudit(String audit) {
            this.audit = audit;
        }

        public String getCost_price() {
            return cost_price;
        }

        public void setCost_price(String cost_price) {
            this.cost_price = cost_price;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getFeature_item() {
            return feature_item;
        }

        public void setFeature_item(String feature_item) {
            this.feature_item = feature_item;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public String getPutaway() {
            return putaway;
        }

        public void setPutaway(String putaway) {
            this.putaway = putaway;
        }

        public String getSamount() {
            return samount;
        }

        public void setSamount(String samount) {
            this.samount = samount;
        }

        public String getSbin() {
            return sbin;
        }

        public void setSbin(String sbin) {
            this.sbin = sbin;
        }

        public String getSbrand() {
            return sbrand;
        }

        public void setSbrand(String sbrand) {
            this.sbrand = sbrand;
        }

        public String getScolour() {
            return scolour;
        }

        public void setScolour(String scolour) {
            this.scolour = scolour;
        }

        public String getScount() {
            return scount;
        }

        public void setScount(String scount) {
            this.scount = scount;
        }

        public String getSell_points() {
            return sell_points;
        }

        public void setSell_points(String sell_points) {
            this.sell_points = sell_points;
        }

        public String getShop_address() {
            return shop_address;
        }

        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
        }

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }

        public String getShot() {
            return shot;
        }

        public void setShot(String shot) {
            this.shot = shot;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getSinfo() {
            return sinfo;
        }

        public void setSinfo(String sinfo) {
            this.sinfo = sinfo;
        }

        public String getSisrec() {
            return sisrec;
        }

        public void setSisrec(String sisrec) {
            this.sisrec = sisrec;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
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

        public String getSnew() {
            return snew;
        }

        public void setSnew(String snew) {
            this.snew = snew;
        }

        public String getSpic() {
            return spic;
        }

        public void setSpic(String spic) {
            this.spic = spic;
        }

        public String getSpid() {
            return spid;
        }

        public void setSpid(String spid) {
            this.spid = spid;
        }

        public String getSprice() {
            return sprice;
        }

        public void setSprice(String sprice) {
            this.sprice = sprice;
        }

        public String getTdapid() {
            return tdapid;
        }

        public void setTdapid(String tdapid) {
            this.tdapid = tdapid;
        }

        public String getThemeId() {
            return themeId;
        }

        public void setThemeId(String themeId) {
            this.themeId = themeId;
        }
    }
}
