package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.BitmapUtils;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

@SuppressLint("SimpleDateFormat")
public class ZiShangDetailActivity extends Activity implements OnClickListener {

    private RelativeLayout mRe_title, re_share;
    private TextView mTv_feedback, tv_name, tv_feedback_number, tv_time, tv_state, tv_douzi_number, tv_content, tv_one,
            tv_two, tv_three, tv_feedback_number_top;
    private Intent mIntent;
    private RoundImageView mIv_name_photo;
    private String user_photo, user_name, user_level, faqi_number, dashang_number, time, state, zishang_number,
            photo_number, custom_number, feedback_number, user_id, content, image_size, zishang_id;
    private ImageView iv_level;
    private MyListView mIv_listview;
    private String[] mImages;
    private String[] mSizes;
    private int mWidthPixels;
    private int mDip2px;
    private RoundImageView mIv_name_photo_one;
    private RoundImageView mIv_name_photo_two;
    private RoundImageView mIv_name_photo_three;
    private LinearLayout mLl_dangshiren;
    private FeedBackPeopleTitle mBean;
    private RelativeLayout mIv_back;
    private final int TOSHANGOTHERS = 1;
    private int fankui_success = 0;
    private ImageView iv_shane_ceshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zishang_detail);
        Intent intent = getIntent();
        user_photo = intent.getStringExtra("user_photo");
        user_name = intent.getStringExtra("user_name");
        user_level = intent.getStringExtra("user_level");
        faqi_number = intent.getStringExtra("faqi_number");
        dashang_number = intent.getStringExtra("dashang_number");
        time = intent.getStringExtra("time");
        state = intent.getStringExtra("state");
        zishang_number = intent.getStringExtra("zishang_number");
        photo_number = intent.getStringExtra("photo_number");
        custom_number = intent.getStringExtra("custom_number");
        feedback_number = intent.getStringExtra("feedback_number");
        user_id = intent.getStringExtra("user_id");
        content = intent.getStringExtra("content");
        image_size = intent.getStringExtra("image_size");
        zishang_id = intent.getStringExtra("zishang_id");
        mIv_back = (RelativeLayout) findViewById(R.id.back);// 返回按钮
        iv_shane_ceshi = (ImageView) findViewById(R.id.iv_shane_ceshi);/*分享图片*/
        re_share = (RelativeLayout) findViewById(R.id.re_share);/*分享按钮*/
        mRe_title = (RelativeLayout) findViewById(R.id.re_title);// 头部标题框
        // 一般人进去的反馈列表布局
        mLl_dangshiren = (LinearLayout) findViewById(R.id.ll_dangshiren);// 点击当事人进去的反馈列表布局
        tv_feedback_number_top = (TextView) findViewById(R.id.tv_feedback_number_top);// 反馈数量
        mTv_feedback = (TextView) findViewById(R.id.tv_feedback);// 反馈按钮
        mIv_name_photo = (RoundImageView) findViewById(R.id.iv_name_photo);// 当前详情用户图像
        tv_name = (TextView) findViewById(R.id.tv_name);// 用户名
        iv_level = (ImageView) findViewById(R.id.iv_level);// 用户等级图
        tv_feedback_number = (TextView) findViewById(R.id.tv_feedback_number);// 发起和打赏数量
        tv_time = (TextView) findViewById(R.id.tv_time);// 时间
        tv_state = (TextView) findViewById(R.id.tv_state);// 状态
        tv_douzi_number = (TextView) findViewById(R.id.tv_douzi_number);// 豆孜数量
        tv_content = (TextView) findViewById(R.id.tv_content);// 评论内容
        mIv_listview = (MyListView) findViewById(R.id.iv_listview);// 图片数量集
        tv_one = (TextView) findViewById(R.id.tv_one);// 标签1
        tv_two = (TextView) findViewById(R.id.tv_two);// 标签2
        tv_three = (TextView) findViewById(R.id.tv_three);// 标签3
        mIv_name_photo_one = (RoundImageView) findViewById(R.id.iv_name_photo_one);
        mIv_name_photo_two = (RoundImageView) findViewById(R.id.iv_name_photo_two);
        mIv_name_photo_three = (RoundImageView) findViewById(R.id.iv_name_photo_three);
        init();
        initListener();
    }

    @Override
    protected void onResume() {
        getData();
        getShareData();
        super.onResume();
    }


    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "78");
        map.put("enjoy_id", zishang_id);
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @SuppressWarnings("static-access")
            @Override
            public void onSuccess(String result) {
                System.out.println("孜赏详情头部" + result);
                mBean = GsonUtil.getInstance().fromJson(result, FeedBackPeopleTitle.class);
                List<TickingsTitle> tilte = mBean.getTickings();
                tv_feedback_number_top.setText(feedback_number + "反馈");
                if (App.getInstance().getUserInfo().getId().equals(user_id)) {
                    mTv_feedback.setText("加价");
                    mTv_feedback.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    if ("0".equals(mBean.getIs_ticking())) {
                        mTv_feedback.setText("反馈");
                        mTv_feedback.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        mTv_feedback.setText("已反馈");
                        mTv_feedback.setTextColor(Color.parseColor("#ffffff"));
                        mTv_feedback.setBackgroundColor(Color.parseColor("#EBEBEB"));
                    }
                }

                if (tilte.size() == 0) {
                    mIv_name_photo_one.setVisibility(View.GONE);
                    mIv_name_photo_two.setVisibility(View.GONE);
                    mIv_name_photo_three.setVisibility(View.GONE);
                }
                if (tilte.size() == 1) {
                    mIv_name_photo_one.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + tilte.get(0).getPhoto())
                            .into(mIv_name_photo_one);
                    mIv_name_photo_two.setVisibility(View.GONE);
                    mIv_name_photo_three.setVisibility(View.GONE);
                }
                if (tilte.size() == 2) {
                    mIv_name_photo_one.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + tilte.get(0).getPhoto())
                            .into(mIv_name_photo_one);
                    mIv_name_photo_two.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + tilte.get(1).getPhoto())
                            .into(mIv_name_photo_two);
                    mIv_name_photo_three.setVisibility(View.GONE);
                }
                if (tilte.size() > 2) {
                    mIv_name_photo_one.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + tilte.get(0).getPhoto())
                            .into(mIv_name_photo_one);
                    mIv_name_photo_two.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + tilte.get(1).getPhoto())
                            .into(mIv_name_photo_two);
                    mIv_name_photo_three.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + tilte.get(2).getPhoto())
                            .into(mIv_name_photo_three);
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    @SuppressWarnings("static-access")
    private void init() {
        Glide.with(ZiShangDetailActivity.this).load(HttpConfig.IMAGEHOST + user_photo).into(mIv_name_photo);
        tv_name.setText(user_name);
        if ("1".equals(user_level)) {
            iv_level.setImageResource(R.drawable.level_one);
        }
        if ("2".equals(user_level)) {
            iv_level.setImageResource(R.drawable.level_two);
        }
        if ("3".equals(user_level)) {
            iv_level.setImageResource(R.drawable.level_three);
        }
        if ("4".equals(user_level)) {
            iv_level.setImageResource(R.drawable.level_three);
        }
        if ("5".equals(user_level)) {
            iv_level.setImageResource(R.drawable.level_five);
        }
        if ("6".equals(user_level)) {
            iv_level.setImageResource(R.drawable.level_six);
        }
        tv_feedback_number.setText("发起" + faqi_number + "," + "打赏" + dashang_number);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String share_time = formatter.format(Long.valueOf(time));
        tv_time.setText(share_time);
        if ("0".equals(state)) {
            tv_state.setText("未完成");
        } else {
            tv_state.setText("已完成");
        }
        tv_douzi_number.setText(zishang_number);
        tv_content.setText(content);
        mImages = photo_number.split(",");
        mSizes = image_size.split(",");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidthPixels = dm.widthPixels;
        mDip2px = DensityUtil.dip2px(ZiShangDetailActivity.this, 90);
        ImageAdapter adapter = new ImageAdapter(ZiShangDetailActivity.this, mImages);
        mIv_listview.setAdapter(adapter);
        String[] customs = custom_number.split(",");
        if (customs.length == 1) {
            tv_one.setText(customs[0]);
            tv_two.setVisibility(View.GONE);
            tv_three.setVisibility(View.GONE);
        }
        if (customs.length == 2) {
            tv_one.setText(customs[0]);
            tv_two.setVisibility(View.VISIBLE);
            tv_two.setText(customs[1]);
            tv_three.setVisibility(View.GONE);
        }
        if (customs.length == 3) {
            tv_one.setText(customs[0]);
            tv_two.setVisibility(View.VISIBLE);
            tv_three.setVisibility(View.VISIBLE);
            tv_two.setText(customs[1]);
            tv_three.setText(customs[2]);
        }

    }

    private void initListener() {
        mRe_title.setOnClickListener(this);
        mTv_feedback.setOnClickListener(this);
        mIv_name_photo.setOnClickListener(this);
        mLl_dangshiren.setOnClickListener(this);
        mIv_back.setOnClickListener(this);
    }

    public void getShareData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "513");
        map.put("order_id", zishang_id);
        new HttpClientGet(ZiShangDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.print("结果" + result);
                final LIChenQIBean bean = GsonUtil.getInstance().fromJson(result, LIChenQIBean.class);
                Glide.with(ZiShangDetailActivity.this).load(HttpConfig.IMAGEHOST + bean.getEnjoyImg()).into(iv_shane_ceshi);
                re_share.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareSDK.initSDK(ZiShangDetailActivity.this);
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();
                        String productUrl = HttpConfig.IMAGEHOST + bean.getUrl();
                        oks.setTitle("孜赏详情分享");
                        oks.setTitleUrl(productUrl);
                        oks.setText(bean.getContent());
                        oks.setImageUrl(HttpConfig.IMAGEHOST + bean.getEnjoyImg());
                        System.out.println("什么地址" + HttpConfig.IMAGEHOST + "");

                        iv_shane_ceshi.setDrawingCacheEnabled(true);
                        Bitmap bm = iv_shane_ceshi.getDrawingCache();
                        String imagePath = BitmapUtils.saveImageToGallery(ZiShangDetailActivity.this, bm);

                        oks.setImagePath(imagePath);// 确保sdcard 存在图片
                        oks.setUrl(productUrl);
                        oks.setSite("孜孜管家");
                        oks.setSiteUrl(productUrl);
                        oks.setCallback(new PlatformActionListener() {

                            @Override
                            public void onError(Platform arg0, int arg1, Throwable arg2) {
                                System.out.println("分享失败");
                            }

                            @Override
                            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                                App.AddShareRecord(ZiShangDetailActivity.this, 1);
                            }

                            @Override
                            public void onCancel(Platform arg0, int arg1) {
                                System.out.println("取消分享");
                            }
                        });
                        oks.show(ZiShangDetailActivity.this);
                    }
                });
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private String[] mImages;
        private Viewholdr mHolder;

        public ImageAdapter(Context context, String[] mImages) {
            this.context = context;
            this.mImages = mImages;
        }

        @Override
        public int getCount() {
            return mImages == null ? 0 : mImages.length;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_zishang_detail, null);
                mHolder = new Viewholdr(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Viewholdr) convertView.getTag();
            }
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mHolder.mIv.getLayoutParams();
            double space = 0;
            double _with = Double.valueOf(mSizes[position * 2]);
            double _height = Double.valueOf(mSizes[position * 2 + 1]);
            space = (mWidthPixels - mDip2px) / _with;
            linearParams.width = mWidthPixels - mDip2px;
            linearParams.height = (int) (_height * space);
            mHolder.mIv.setLayoutParams(linearParams);
            Glide.with(context).load(HttpConfig.IMAGEHOST + mImages[position]).into(mHolder.mIv);
            return convertView;
        }

    }

    public class Viewholdr {
        private RoundImageView mIv;

        public Viewholdr(View view) {
            mIv = (RoundImageView) view.findViewById(R.id.iv);
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_title:
                if ("加价".equals(mTv_feedback.getText().toString().trim())) {
                    // 自己发的孜赏 看别人给我的反馈 去打赏别人
                    mIntent = new Intent(ZiShangDetailActivity.this, FeedBackListActivity.class);
                    mIntent.putExtra("publish_id", user_id);// 用户id
                    mIntent.putExtra("feedback_number", feedback_number);
                    mIntent.putExtra("zidou", zishang_number);
                    mIntent.putExtra("zishang_id", zishang_id);// 孜赏id
                    mIntent.putExtra("state", state);
                    startActivityForResult(mIntent, TOSHANGOTHERS);
                } else {
                    // 别人发的孜赏 我去反馈别人发的孜赏
                    mIntent = new Intent(ZiShangDetailActivity.this, CommonFeedBackListActivity.class);
                    mIntent.putExtra("publish_id", user_id);
                    mIntent.putExtra("feedback_number", feedback_number);
                    mIntent.putExtra("zidou", zishang_number);
                    mIntent.putExtra("zishang_id", zishang_id);
                    startActivityForResult(mIntent, 523);
                }
                break;
            case R.id.tv_feedback:
                if (App.getInstance().getUserInfo().getId().equals(user_id)) {// 加价
                    final Dialog dialog = new Dialog(ZiShangDetailActivity.this, R.style.dialog);
                    dialog.setContentView(R.layout.add_price_zishang);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_sure = (TextView) dialog.findViewById(R.id.tv_sure);
                    final EditText et_number = (EditText) dialog.findViewById(R.id.et_number);
                    tv_sure.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(et_number.getText().toString().trim())) {
                                ToastUtils.show(getApplicationContext(), "请先编辑孜豆数");
                                return;
                            }
                            dialog.dismiss();
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("OPT", "77");
                            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                            map.put("enjoy_id", zishang_id);
                            map.put("zizipeans",
                                    String.valueOf(Integer.parseInt(et_number.getText().toString().trim()) * 500));
                            new HttpClientGet(getApplicationContext(), null, map, false,
                                    new HttpClientGet.CallBacks<String>() {

                                        @Override
                                        public void onSuccess(String result) {
                                            BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
                                                    BasicParameterBean.class);
                                            ToastUtils.show(getApplicationContext(), bean.getMsg());
                                            tv_douzi_number.setText(String.valueOf(Integer.parseInt(zishang_number)
                                                    + Integer.parseInt(et_number.getText().toString().trim()) * 500));

                                        }

                                        @Override
                                        public void onFailure(MyError myError) {
                                            super.onFailure(myError);
                                        }
                                    });
                        }
                    });
                    dialog.show();
                } else {// 反馈
                    if ("0".equals(mBean.getIs_ticking())) {
                        mIntent = new Intent(ZiShangDetailActivity.this, SendFeedBackActivity.class);
                        mIntent.putExtra("publish_id", user_id);// 发布者id
                        mIntent.putExtra("zishang_id", zishang_id);// 孜赏id
                        startActivityForResult(mIntent, 111);
                    } else {
                        ToastUtils.show(getApplicationContext(), "您对该孜赏已反馈了,不能再反馈了!");
                    }

                }
                break;
            case R.id.iv_name_photo:// 个人孜赏列表
                mIntent = new Intent(ZiShangDetailActivity.this, OthersExampleActivity.class);
                mIntent.putExtra("publish_id", user_id);// 发布者id
                startActivity(mIntent);
                break;
            case R.id.back:
                if (zishang_number.equals(tv_douzi_number.getText().toString().trim())) {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Intent intent = getIntent();
                    ZiShangDetailActivity.this.setResult(2, intent);
                    ZiShangDetailActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                if (feedback_number.equals(String.valueOf(mBean.getTickings().size()))) {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Intent intent = getIntent();
                    ZiShangDetailActivity.this.setResult(2, intent);
                    ZiShangDetailActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 1) {
            getData();
            fankui_success++;
            tv_feedback_number_top.setText(String.valueOf(Integer.parseInt(feedback_number) + fankui_success) + "反馈");
        }
        if (requestCode == TOSHANGOTHERS && resultCode == 33) {
            tv_state.setText("已完成");
            tv_feedback_number
                    .setText("发起" + faqi_number + "," + "打赏" + String.valueOf(Integer.parseInt(dashang_number) + 1));
        }
        if (resultCode == 523 && requestCode == 523) {
            Bundle bundle = data.getExtras();
            int nums = bundle.getInt("number");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            boolean isFankui = PreferencesUtils.getBoolean(getApplicationContext(), "isFankui");
            if (zishang_number.equals(tv_douzi_number.getText().toString().trim())) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                Intent intent = getIntent();
                ZiShangDetailActivity.this.setResult(2, intent);
                ZiShangDetailActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            if (feedback_number.equals(String.valueOf(mBean.getTickings().size()))) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                Intent intent = getIntent();
                ZiShangDetailActivity.this.setResult(2, intent);
                ZiShangDetailActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class FeedBackPeople {
        private String error;
        private String msg;
        private Tickings tickings;
        private String is_ticking;

        public String getIs_ticking() {
            return is_ticking;
        }

        public void setIs_ticking(String is_ticking) {
            this.is_ticking = is_ticking;
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

        public Tickings getTickings() {
            return tickings;
        }

        public void setTickings(Tickings tickings) {
            this.tickings = tickings;
        }

    }

    public class Tickings {
        private int currPage;
        private List<TickingsList> page;
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

        public List<TickingsList> getPage() {
            return page;
        }

        public void setPage(List<TickingsList> page) {
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

    public class TickingsList {
        private String content;
        private String credit_level_id;
        private String enjoyImg;
        private String enjoy_id;
        private ShareTime enjoy_time;
        private String entityId;
        private String id;
        private String img_wh;
        private String is_dispaly;
        private String name;
        private String persistent;
        private String photo;
        private String photo_number;
        private String publish_user_id;
        private String user_id;
        private String user_name;
        private String zizipeas;
        private String play_zizipeas;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlay_zizipeas() {
            return play_zizipeas;
        }

        public void setPlay_zizipeas(String play_zizipeas) {
            this.play_zizipeas = play_zizipeas;
        }

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

        public String getEnjoyImg() {
            return enjoyImg;
        }

        public void setEnjoyImg(String enjoyImg) {
            this.enjoyImg = enjoyImg;
        }

        public String getEnjoy_id() {
            return enjoy_id;
        }

        public void setEnjoy_id(String enjoy_id) {
            this.enjoy_id = enjoy_id;
        }

        public ShareTime getEnjoy_time() {
            return enjoy_time;
        }

        public void setEnjoy_time(ShareTime enjoy_time) {
            this.enjoy_time = enjoy_time;
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

        public String getPhoto_number() {
            return photo_number;
        }

        public void setPhoto_number(String photo_number) {
            this.photo_number = photo_number;
        }

        public String getPublish_user_id() {
            return publish_user_id;
        }

        public void setPublish_user_id(String publish_user_id) {
            this.publish_user_id = publish_user_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getZizipeas() {
            return zizipeas;
        }

        public void setZizipeas(String zizipeas) {
            this.zizipeas = zizipeas;
        }
    }

    public class FeedBackPeopleTitle {
        private String error;
        private String is_ticking;
        private String msg;
        private List<TickingsTitle> tickings;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getIs_ticking() {
            return is_ticking;
        }

        public void setIs_ticking(String is_ticking) {
            this.is_ticking = is_ticking;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<TickingsTitle> getTickings() {
            return tickings;
        }

        public void setTickings(List<TickingsTitle> tickings) {
            this.tickings = tickings;
        }

    }

    public class TickingsTitle {
        private String content;
        private String photo;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

    public class LIChenQIBean {
        private String content;
        private String enjoyImg;
        private String error;
        private String msg;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEnjoyImg() {
            return enjoyImg;
        }

        public void setEnjoyImg(String enjoyImg) {
            this.enjoyImg = enjoyImg;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;
    }
}
