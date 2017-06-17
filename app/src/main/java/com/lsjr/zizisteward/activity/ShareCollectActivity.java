package com.lsjr.zizisteward.activity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareCollectActivity extends Activity implements OnClickListener {
    private RelativeLayout mRe_eye, re_select_xia;
    private View mPopupView;
    private PopupWindow mPopupWindow;
    private int mWidth;
    private ImageView mIv_xia;
    private ImageView mIv_shang;
    private TextView mTv_all;
    private TextView mTv_week;
    private TextView mTv_month;
    private TextView mTv_three_months;
    private TextView mTv_disabled;
    private TextView mTv_name;
    private SuperListView mListview_share;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private ShareAdapter mAdapter;
    private List<ShareCollectDetail> list = new ArrayList<ShareCollectDetail>();
    private RelativeLayout mRe_null;
    private Handler handler;
    private RelativeLayout mRe_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_collect);
        mRe_back = (RelativeLayout) findViewById(R.id.re_back);// 返回键
        mRe_eye = (RelativeLayout) findViewById(R.id.re_eye);// 视界收藏
        re_select_xia = (RelativeLayout) findViewById(R.id.re_select_xia);// 向下箭头整体框
        mTv_name = (TextView) findViewById(R.id.tv_name);// 名称
        mIv_xia = (ImageView) findViewById(R.id.iv_xia);// 下箭头
        mIv_shang = (ImageView) findViewById(R.id.iv_shang);// 上箭头
        mListview_share = (SuperListView) findViewById(R.id.listview_share);
        mRe_null = (RelativeLayout) findViewById(R.id.re_null);// 空内容显示
        int px = DensityUtil.dip2px(this, 10);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        ViewTreeObserver viewTreeObserver = re_select_xia.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                re_select_xia.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mWidth = re_select_xia.getWidth();
            }
        });

        mPopupView = getLayoutInflater().inflate(R.layout.popup_classic_time, null);
        mPopupWindow = new PopupWindow(mPopupView, widthPixels - px * 2, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mTv_all = (TextView) mPopupView.findViewById(R.id.tv_all);// 全部
        mTv_week = (TextView) mPopupView.findViewById(R.id.tv_week);// 一周内
        mTv_month = (TextView) mPopupView.findViewById(R.id.tv_month);// 一月内
        mTv_three_months = (TextView) mPopupView.findViewById(R.id.tv_three_months);// 三月内
        mTv_disabled = (TextView) mPopupView.findViewById(R.id.tv_disabled);// 已失效

        if (mPopupWindow.isShowing()) {
            mIv_xia.setVisibility(View.GONE);
            mIv_shang.setVisibility(View.VISIBLE);
        } else {
            mIv_xia.setVisibility(View.VISIBLE);
            mIv_shang.setVisibility(View.GONE);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    isRefresh = true;
                    mAdapter.removeAll();
                    getData(msg.what);
                    mAdapter.notifyDataSetChanged();
                }
                if (msg.what == 1) {
                    isRefresh = true;
                    mAdapter.removeAll();
                    getData(msg.what);
                    mAdapter.notifyDataSetChanged();
                }
                if (msg.what == 2) {
                    isRefresh = true;
                    mAdapter.removeAll();
                    getData(msg.what);
                    mAdapter.notifyDataSetChanged();
                }
                if (msg.what == 3) {
                    isRefresh = true;
                    mAdapter.removeAll();
                    getData(msg.what);
                    mAdapter.notifyDataSetChanged();
                }
                if (msg.what == 4) {
                    isRefresh = true;
                    mAdapter.removeAll();
                    getData(msg.what);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        initListener();
    }

    private void initListener() {
        mRe_eye.setOnClickListener(this);
        re_select_xia.setOnClickListener(this);
        mTv_all.setOnClickListener(this);
        mTv_week.setOnClickListener(this);
        mTv_month.setOnClickListener(this);
        mTv_three_months.setOnClickListener(this);
        mTv_disabled.setOnClickListener(this);
        mRe_back.setOnClickListener(this);
        mAdapter = new ShareAdapter(ShareCollectActivity.this, list);
        mListview_share.setAdapter(mAdapter);
        mListview_share.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                mTv_name.setText("全部");
                getData(0);
            }
        });
        mListview_share.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData(0);
            }
        });
        mListview_share.refresh();
        mListview_share.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position - 1).getIs_dispaly().equals("0")) {
                    if (!TextUtils.isEmpty(list.get(position - 1).getPhoto())) {
                        Intent intent = new Intent(ShareCollectActivity.this, ShareCollectDetailActivity.class);
                        intent.putExtra("title", list.get(position - 1).getFlock_title());
                        intent.putExtra("time", list.get(position - 1).getCtime().getTime());
                        intent.putExtra("photoUrl", list.get(position - 1).getPhoto());
                        intent.putExtra("imgWidth", list.get(position - 1).getImg_wh());
                        intent.putExtra("id", list.get(position - 1).getId());
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        ToastUtils.show(getApplicationContext(), "该收藏无详情...");
                    }

                } else {
                    ToastUtils.show(getApplicationContext(), "群主已删除该文章...");
                }
            }
        });
    }

    protected void getData(int type_num) {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "70");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum++));
        map.put("collect_type", "1");
        map.put("keyword", String.valueOf(type_num));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("分享收藏" + result);
                ShareCollectBean bean = GsonUtil.getInstance().fromJson(result, ShareCollectBean.class);
                if (null != bean && "1".equals(bean.getError())) {
                    if (bean.getCollectFlock().getTotalCount() == 0) {
                        mRe_null.setVisibility(View.VISIBLE);
                        mListview_share.setVisibility(View.GONE);
                    } else {
                        mRe_null.setVisibility(View.GONE);
                        mListview_share.setVisibility(View.VISIBLE);

                        if (1 != pageNum) {
                            list.addAll(bean.getCollectFlock().getPage());
                            mAdapter.setList(list);
                        } else {
                            list = bean.getCollectFlock().getPage();
                            mAdapter = new ShareAdapter(ShareCollectActivity.this, list);
                            mListview_share.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                        if (list.size() < bean.getCollectFlock().getPageSize()) {
                            mListview_share.setIsLoadFull(false);
                        }

                        mListview_share.finishRefresh();
                        mListview_share.finishLoadMore();
                    }

                }

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    @Override
    protected void onResume() {
        mTv_name.setText("全部");
        mListview_share.refresh();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_eye:
                Intent intent = new Intent(getApplicationContext(), EyeCollectActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.re_select_xia:
                mPopupWindow.showAsDropDown(v, 0, 5);
                break;
            case R.id.tv_all:
                mTv_name.setText("全部");
                mPopupWindow.dismiss();
                handler.sendEmptyMessage(0);
                break;
            case R.id.tv_week:
                mTv_name.setText("一周内");
                mPopupWindow.dismiss();
                handler.sendEmptyMessage(1);
                break;
            case R.id.tv_month:
                mTv_name.setText("一月内");
                mPopupWindow.dismiss();
                handler.sendEmptyMessage(2);
                break;
            case R.id.tv_three_months:
                mTv_name.setText("三月内");
                mPopupWindow.dismiss();
                handler.sendEmptyMessage(3);
                break;
            case R.id.tv_disabled:
                mTv_name.setText("已失效");
                mPopupWindow.dismiss();
                handler.sendEmptyMessage(4);
                break;
            case R.id.re_back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    public class ShareAdapter extends BaseAdapter {
        private Context context;
        private List<ShareCollectDetail> list;
        private ViewHolder mHolder;

        public ShareAdapter(Context context, List<ShareCollectDetail> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<ShareCollectDetail> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(ShareCollectDetail page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ShareCollectDetail page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ShareCollectDetail> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_share_collect, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).into(mHolder.mPhoto);
            mHolder.mTv_title.setText(list.get(position).getFlock_title());
            mHolder.mTv_content.setText(list.get(position).getShare_content());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String share_time = formatter.format(Long.valueOf(list.get(position).getCtime().getTime()));
            mHolder.mTv_time.setText(share_time);
            if (list.get(position).getIs_dispaly().equals("0")) {
                mHolder.mTv_disable.setVisibility(View.GONE);
            } else {
                mHolder.mTv_disable.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

    }

    public class ViewHolder {
        private RoundImageView mPhoto;
        private TextView mTv_title;
        private TextView mTv_content;
        private TextView mTv_time;
        private TextView mTv_disable;

        public ViewHolder(View view) {
            mPhoto = (RoundImageView) view.findViewById(R.id.photo);
            mTv_title = (TextView) view.findViewById(R.id.tv_title);
            mTv_content = (TextView) view.findViewById(R.id.tv_content);
            mTv_time = (TextView) view.findViewById(R.id.tv_time);
            mTv_disable = (TextView) view.findViewById(R.id.tv_disable);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class ShareCollectBean {
        private ShareCollectInfo collectFlock;
        private String collectShare;
        private String error;
        private String msg;

        public ShareCollectInfo getCollectFlock() {
            return collectFlock;
        }

        public void setCollectFlock(ShareCollectInfo collectFlock) {
            this.collectFlock = collectFlock;
        }

        public String getCollectShare() {
            return collectShare;
        }

        public void setCollectShare(String collectShare) {
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

    public class ShareCollectInfo {
        private String currPage;
        private List<ShareCollectDetail> page;
        private int pageSize;
        private String pageTitle;
        private int totalCount;
        private String totalPageCount;

        public String getCurrPage() {
            return currPage;
        }

        public void setCurrPage(String currPage) {
            this.currPage = currPage;
        }

        public List<ShareCollectDetail> getPage() {
            return page;
        }

        public void setPage(List<ShareCollectDetail> page) {
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

        public String getTotalPageCount() {
            return totalPageCount;
        }

        public void setTotalPageCount(String totalPageCount) {
            this.totalPageCount = totalPageCount;
        }
    }

    public class ShareCollectDetail {
        private String collect_type;
        private String csid;
        private ShareTime ctime;
        private String cuid;
        private String entityId;
        private String flock_title;
        private String id;
        private String img_wh;
        private String is_dispaly;
        private String persistent;
        private String photo;
        private String share_content;
        private String share_type;
        private String spicfirst;
        private String tshow;

        public String getCollect_type() {
            return collect_type;
        }

        public void setCollect_type(String collect_type) {
            this.collect_type = collect_type;
        }

        public String getCsid() {
            return csid;
        }

        public void setCsid(String csid) {
            this.csid = csid;
        }

        public ShareTime getCtime() {
            return ctime;
        }

        public void setCtime(ShareTime ctime) {
            this.ctime = ctime;
        }

        public String getCuid() {
            return cuid;
        }

        public void setCuid(String cuid) {
            this.cuid = cuid;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getFlock_title() {
            return flock_title;
        }

        public void setFlock_title(String flock_title) {
            this.flock_title = flock_title;
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

        public String getShare_content() {
            return share_content;
        }

        public void setShare_content(String share_content) {
            this.share_content = share_content;
        }

        public String getShare_type() {
            return share_type;
        }

        public void setShare_type(String share_type) {
            this.share_type = share_type;
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
    }
}
