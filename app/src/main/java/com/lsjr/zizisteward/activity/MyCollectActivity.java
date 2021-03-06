package com.lsjr.zizisteward.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCollectActivity extends BaseActivity {

    private SuperListView mListview_shijie;
    private List<CollectList> list = new ArrayList<CollectList>();
    private boolean isRefresh = true;
    private int pageNum = 1;
    private MyCollectAdapter mAdapter;

    @Override
    public int getContainerView() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("我的收藏");
        mListview_shijie = (SuperListView) findViewById(R.id.listview_shijie);
        init();

    }

    private void init() {
        mAdapter = new MyCollectAdapter(MyCollectActivity.this, list);
        mListview_shijie.setAdapter(mAdapter);
        mListview_shijie.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });
        mListview_shijie.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        mListview_shijie.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CollectDetailActivity.class);
                intent.putExtra("content", list.get(position - 1).getContent());// 内容
                intent.putExtra("photo", list.get(position - 1).getPhoto());// 头像
                intent.putExtra("shareImg", list.get(position - 1).getShareImg());// 图片
                intent.putExtra("time", list.get(position - 1).getCtime().getTime());// 时间
                intent.putExtra("user_name", list.get(position - 1).getUser_name());// 用户名
                intent.putExtra("custom_tag", list.get(position - 1).getCustom_tag());// 标签内容
                intent.putExtra("level", list.get(position - 1).getCredit_level_id());// 用户等级
                startActivity(intent);
            }
        });

        mListview_shijie.refresh();
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "70");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum++));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("收藏" + result);
                MyCollectBean bean = GsonUtil.getInstance().fromJson(result, MyCollectBean.class);
                if (null != bean && "1".equals(bean.getError())) {

                    if (1 != pageNum) {
                        list.addAll(bean.getCollectShare().getPage());
                        mAdapter.setList(list);
                    } else {
                        list = bean.getCollectShare().getPage();
                        mAdapter = new MyCollectAdapter(MyCollectActivity.this, list);
                        mListview_shijie.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                if (list.size() < bean.getCollectShare().getPageSize()) {
                    mListview_shijie.setIsLoadFull(false);
                }

                mListview_shijie.finishRefresh();
                mListview_shijie.finishLoadMore();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    public class MyCollectAdapter extends BaseAdapter {
        private Context context;
        private List<CollectList> list;
        private ViewHolder mHolder;

        public MyCollectAdapter(Context context, List<CollectList> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<CollectList> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(CollectList page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(CollectList page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<CollectList> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_collect, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            if (list.get(position).getCredit_level_id().equals("0")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_one);
            } else if (list.get(position).getCredit_level_id().equals("1")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_one);
            } else if (list.get(position).getCredit_level_id().equals("2")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_two);
            } else if (list.get(position).getCredit_level_id().equals("3")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            } else if (list.get(position).getCredit_level_id().equals("6")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_six);
            } else if (list.get(position).getCredit_level_id().equals("5")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_five);
            } else if (list.get(position).getCredit_level_id().equals("4")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_five);
            }

            if (TextUtils.isEmpty(list.get(position).getCustom_tag())) {
                mHolder.mTv_custom.setVisibility(View.GONE);
            } else {
                mHolder.mTv_custom.setVisibility(View.VISIBLE);
                mHolder.mTv_custom.setText(list.get(position).getCustom_tag());
            }

            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).asBitmap().centerCrop()
                    .animate(android.R.anim.slide_in_left).into(mHolder.mIv_photo);
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto())
                    .into(mHolder.mYouliao_yuantu);
            mHolder.mTv_name.setText(list.get(position).getUser_name());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String share_time = formatter.format(Long.valueOf(list.get(position).getCtime().getTime()));
            mHolder.mTv_time.setText(share_time);
            mHolder.mTv_content.setText(list.get(position).getContent());
            mHolder.mTv_count.setText(list.get(position).getPhoto_number() + "张");

            return convertView;
        }

    }

    public class ViewHolder {
        private CircleImageView mYouliao_yuantu;// 用户头像
        private TextView mTv_name;// 用户名
        private TextView mTv_time;// 时间
        private ImageView mIv_level;// 会员等级图标
        private TextView mTv_content;// 内容
        private TextView mTv_count;// 图片数量
        private ImageView mIv_photo;// 图片
        private TextView mTv_custom;// 自定义标签内容

        public ViewHolder(View view) {

            mYouliao_yuantu = (CircleImageView) view.findViewById(R.id.youliao_yuantu);
            mTv_name = (TextView) view.findViewById(R.id.tv_name);
            mIv_level = (ImageView) view.findViewById(R.id.iv_level);
            mTv_time = (TextView) view.findViewById(R.id.tv_time);
            mTv_content = (TextView) view.findViewById(R.id.tv_content);
            mIv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            mTv_count = (TextView) view.findViewById(R.id.tv_count);
            mTv_custom = (TextView) view.findViewById(R.id.tv_custom);

        }
    }

    public class MyCollectBean {
        private CollectShare collectShare;
        private String error;
        private String msg;

        public CollectShare getCollectShare() {
            return collectShare;
        }

        public void setCollectShare(CollectShare collectShare) {
            this.collectShare = collectShare;
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

    public class CollectShare {

        private int currPage;
        private List<CollectList> page;
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

        public List<CollectList> getPage() {
            return page;
        }

        public void setPage(List<CollectList> page) {
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

    public class CollectList {
        private String content;
        private String credit_level_id;
        private String csid;
        private CTime ctime;
        private String cuid;
        private String custom_tag;
        private String id;
        private String img_wh;
        private String is_dispaly;
        private String name;
        private String persistent;
        private String photo;
        private String photo_number;
        private String reality_name;
        private String shareImg;
        private String spicfirst;
        private String tshow;
        private String user_name;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCredit_level_id() {
            return credit_level_id;
        }

        public void setCredit_level_id(String credit_level_id) {
            this.credit_level_id = credit_level_id;
        }

        public String getCsid() {
            return csid;
        }

        public void setCsid(String csid) {
            this.csid = csid;
        }

        public CTime getCtime() {
            return ctime;
        }

        public void setCtime(CTime ctime) {
            this.ctime = ctime;
        }

        public String getCuid() {
            return cuid;
        }

        public void setCuid(String cuid) {
            this.cuid = cuid;
        }

        public String getCustom_tag() {
            return custom_tag;
        }

        public void setCustom_tag(String custom_tag) {
            this.custom_tag = custom_tag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_wh() {
            return img_wh;
        }

        public void setImg_wh(String img_wh) {
            this.img_wh = img_wh;
        }

        public String getIs_dispaly() {
            return is_dispaly;
        }

        public void setIs_dispaly(String is_dispaly) {
            this.is_dispaly = is_dispaly;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPhoto_number() {
            return photo_number;
        }

        public void setPhoto_number(String photo_number) {
            this.photo_number = photo_number;
        }

        public String getReality_name() {
            return reality_name;
        }

        public void setReality_name(String reality_name) {
            this.reality_name = reality_name;
        }

        public String getShareImg() {
            return shareImg;
        }

        public void setShareImg(String shareImg) {
            this.shareImg = shareImg;
        }

        public String getSpicfirst() {
            return spicfirst;
        }

        public void setSpicfirst(String spicfirst) {
            this.spicfirst = spicfirst;
        }

        public String getTshow() {
            return tshow;
        }

        public void setTshow(String tshow) {
            this.tshow = tshow;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }

    private class CTime {
        private String date;
        private String day;
        private String hours;
        private String minutes;
        private String month;
        private String nanos;
        private String seconds;
        private String time;
        private String timezoneOffset;
        private String year;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getNanos() {
            return nanos;
        }

        public void setNanos(String nanos) {
            this.nanos = nanos;
        }

        public String getSeconds() {
            return seconds;
        }

        public void setSeconds(String seconds) {
            this.seconds = seconds;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(String timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
