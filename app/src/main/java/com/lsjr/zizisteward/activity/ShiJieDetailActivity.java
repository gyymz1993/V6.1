package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicUserInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

@SuppressLint({"SetJavaScriptEnabled", "SimpleDateFormat"})
public class ShiJieDetailActivity extends Activity implements OnClickListener {

    private String mId, content, photo, shareImg, time, sight_type, user_name, custom_tag, zan_count, is_zan,
            is_collect, image_size, level, user_id;
    private RoundImageView mYouliao_yuantu;
    private TextView mTv_name;
    private ImageView mIv_level;
    private TextView mTv_time;
    private String[] mImags;
    private TextView mTv_label;
    private TextView mTv_content;
    private ImageView mIv_eval;
    private List<Comments> comments_list = new ArrayList<Comments>();
    private MyListView mComment_list;
    private TextView mTv_eval;
    private ImageView mIv_zan_gray;
    private ImageView mIv_zan_red;
    private TextView mTv_praise;
    private RelativeLayout mRe_zan;
    private RelativeLayout mRe_collect;
    private ImageView mIv_collect_gray;
    private ImageView mIv_collect_red;
    private Intent mIntent;
    private RelativeLayout mDelete;
    private RelativeLayout mBack;
    private RelativeLayout mShare;
    private View mView_null;
    private String mShare_content;
    private String mShare_img;
    private String mShare_url;
    private ShareBean mBean;
    private String errorMsg;
    private MyListView mIv_listview;
    private String[] mSize_imgs;
    private ImageAdapter mAdapter;
    private int mWidthPixels;
    private ImageView mIv_shane_ceshi;
    private String user_level;
    private boolean zan_state, collect_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijie_detail);
        mYouliao_yuantu = (RoundImageView) findViewById(R.id.youliao_yuantu);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mIv_level = (ImageView) findViewById(R.id.iv_level);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mIv_listview = (MyListView) findViewById(R.id.iv_listview);
        mView_null = findViewById(R.id.view_null);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidthPixels = dm.widthPixels;
        mTv_label = (TextView) findViewById(R.id.tv_label);// 自定义标签
        mTv_content = (TextView) findViewById(R.id.tv_content);// 用户发送的世界内容
        mDelete = (RelativeLayout) findViewById(R.id.delete);// 删除视界
        mBack = (RelativeLayout) findViewById(R.id.back);// 返回
        mShare = (RelativeLayout) findViewById(R.id.share);// 分享
        mTv_eval = (TextView) findViewById(R.id.tv_eval);// 评论数量
        mTv_praise = (TextView) findViewById(R.id.tv_praise);// 赞的数量
        mIv_eval = (ImageView) findViewById(R.id.iv_eval);// 评论图标
        mComment_list = (MyListView) findViewById(R.id.comment_list);// 评论列表
        mRe_zan = (RelativeLayout) findViewById(R.id.re_zan);// 点击赞
        mIv_zan_gray = (ImageView) findViewById(R.id.iv_zan_gray);// 灰色赞
        mIv_zan_red = (ImageView) findViewById(R.id.iv_zan_red);// 灰色赞
        mRe_collect = (RelativeLayout) findViewById(R.id.re_collect);// 点击收藏
        mIv_collect_gray = (ImageView) findViewById(R.id.iv_collect_gray);// 灰色收藏
        mIv_collect_red = (ImageView) findViewById(R.id.iv_collect_red);// 红色收藏
        mIv_shane_ceshi = (ImageView) findViewById(R.id.iv_shane_ceshi);// 测试分享图片
    }

    /**
     * 分享数据
     */
    private void getShareData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "318");
        map.put("order_id", mId);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("分享信息" + result);
                mBean = GsonUtil.getInstance().fromJson(result, ShareBean.class);
                mShare_content = mBean.getContent();
                mShare_img = mBean.getShareImg();
                mShare_url = mBean.getUrl();
                Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + mShare_img).into(mIv_shane_ceshi);

            }

            @Override
            public void onFailure(MyError myError) {
                errorMsg = myError.getErrorMessage();
                // super.onFailure(myError);
            }
        });
    }

    @Override
    protected void onResume() {
        mId = getIntent().getStringExtra("id");
        content = getIntent().getStringExtra("content");
        photo = getIntent().getStringExtra("photo");
        System.out.println("图片" + photo);
        shareImg = getIntent().getStringExtra("shareImg");
        time = getIntent().getStringExtra("time");
        user_name = getIntent().getStringExtra("user_name");
        sight_type = getIntent().getStringExtra("sight_type");
        custom_tag = getIntent().getStringExtra("custom_tag");
        zan_count = getIntent().getStringExtra("zan_count");
        image_size = getIntent().getStringExtra("image_size");
        level = getIntent().getStringExtra("level");
        user_id = getIntent().getStringExtra("user_id");
        zan_state = getIntent().getBooleanExtra("zan_state", false);
        collect_state = getIntent().getBooleanExtra("collect_state", false);

        System.out.println("点赞状态" + zan_state);

        mImags = shareImg.split(",");
        mSize_imgs = image_size.split(",");
        if (user_id.equals(App.getUserInfo().getId())) {
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.GONE);
        }

        mAdapter = new ImageAdapter(ShiJieDetailActivity.this, mImags);
        mIv_listview.setAdapter(mAdapter);

        getData();
        getShareData();
        getLevelData();
        init();
        initListener();
        super.onResume();
    }

    private void getLevelData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "199");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("用户消息" + result);
                BasicUserInfo bean = GsonUtil.getInstance().fromJson(result, BasicUserInfo.class);
                user_level = bean.getCredit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == 222) {
            mId = data.getStringExtra("id");
            content = data.getStringExtra("content");
            photo = data.getStringExtra("photo");
            System.out.println("图片恢复" + photo);
            shareImg = data.getStringExtra("shareImg");
            time = data.getStringExtra("time");
            user_name = data.getStringExtra("user_name");
            sight_type = data.getStringExtra("sight_type");
            custom_tag = data.getStringExtra("custom_tag");
            zan_count = data.getStringExtra("zan_count");
            is_zan = data.getStringExtra("is_zan");
            is_collect = data.getStringExtra("is_collect");
            image_size = data.getStringExtra("image_size");
            level = data.getStringExtra("level");
            user_id = data.getStringExtra("user_id");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 控件监听
     */
    private void initListener() {
        mDelete.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mIv_eval.setOnClickListener(this);
        mRe_zan.setOnClickListener(this);
        mRe_collect.setOnClickListener(this);
        mYouliao_yuantu.setOnClickListener(this);
    }

    /**
     * 赋值操作
     */
    private void init() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String share_time = formatter.format(Long.valueOf(time));
        mTv_time.setText(share_time);
        Glide.with(ShiJieDetailActivity.this).load(HttpConfig.IMAGEHOST + photo).into(mYouliao_yuantu);
        mTv_name.setText(user_name);

        if (level.equals("0")) {
            mIv_level.setImageResource(R.drawable.level_zero);
        } else if (level.equals("1")) {
            mIv_level.setImageResource(R.drawable.level_one);
        } else if (level.equals("2")) {
            mIv_level.setImageResource(R.drawable.level_two);
        } else if (level.equals("3")) {
            mIv_level.setImageResource(R.drawable.level_three);
        } else if (level.equals("6")) {
            mIv_level.setImageResource(R.drawable.level_six);
        } else if (level.equals("5")) {
            mIv_level.setImageResource(R.drawable.level_five);
        } else if (level.equals("4")) {
            mIv_level.setImageResource(R.drawable.level_five);
        }

        if (TextUtils.isEmpty(custom_tag)) {
            mTv_label.setVisibility(View.GONE);
            mView_null.setVisibility(View.GONE);
        } else {
            mTv_label.setVisibility(View.VISIBLE);
            mTv_label.setText(custom_tag);
            mView_null.setVisibility(View.VISIBLE);
        }
        mTv_content.setText(content);
        System.out.println("内容" + content);

        if (zan_state == false) {
            mTv_praise.setText("赞  " + zan_count);
            mIv_zan_gray.setVisibility(View.VISIBLE);
            mIv_zan_red.setVisibility(View.GONE);
        } else {
            mTv_praise.setText("赞  " + zan_count);
            mIv_zan_gray.setVisibility(View.GONE);
            mIv_zan_red.setVisibility(View.VISIBLE);
        }

        if (collect_state == false) {
            mIv_collect_red.setVisibility(View.GONE);
            mIv_collect_gray.setVisibility(View.VISIBLE);
        } else {
            mIv_collect_red.setVisibility(View.VISIBLE);
            mIv_collect_gray.setVisibility(View.GONE);
        }

    }

    /**
     * 接口请求数据
     */
    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "66");
        map.put("sorder", "");
        map.put("share_id", mId);
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        new HttpClientGet(ShiJieDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("视界详情" + result);
                ShiJie bean = GsonUtil.getInstance().fromJson(result, ShiJie.class);
                comments_list = bean.getComments();
                CommentListAdapter adapter = new CommentListAdapter(ShiJieDetailActivity.this, comments_list);
                mComment_list.setAdapter(adapter);

                mTv_eval.setText("评论  " + String.valueOf(comments_list.size()));
            }

        });
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private String[] mImags;
        private Viewholdr mHolder;

        public ImageAdapter(Context context, String[] mImags) {
            this.context = context;
            this.mImags = mImags;
        }

        @Override
        public int getCount() {
            return mImags == null ? 0 : mImags.length;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_share_collect_detail, null);
                mHolder = new Viewholdr(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Viewholdr) convertView.getTag();
            }
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mHolder.mIv.getLayoutParams();
            double space = 0;
            double _with = Double.valueOf(mSize_imgs[position * 2]);
            double _height = Double.valueOf(mSize_imgs[position * 2 + 1]);
            space = (mWidthPixels) / _with;
            linearParams.width = mWidthPixels;
            linearParams.height = (int) (_height * space);
            mHolder.mIv.setLayoutParams(linearParams);
            Glide.with(context).load(HttpConfig.IMAGEHOST + mImags[position]).into(mHolder.mIv);
            return convertView;
        }

    }

    public class CommentListAdapter extends BaseAdapter {

        private Viewholdr mHolder;
        private Context context;
        private List<Comments> comments_list;

        public CommentListAdapter(Context context, List<Comments> comments_list) {
            this.comments_list = comments_list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return comments_list == null ? 0 : comments_list.size();
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

                convertView = LayoutInflater.from(ShiJieDetailActivity.this).inflate(R.layout.item_images, null);
                mHolder = new Viewholdr(convertView);
                convertView.setTag(mHolder);

            } else {

                mHolder = (Viewholdr) convertView.getTag();

            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + comments_list.get(position).getPhoto())
                    .into(mHolder.mComment_list_yuantu);
            if ("0".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_zero);
            }
            if ("1".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_one);
            }
            if ("2".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_two);
            }
            if ("3".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_three);
            }
            if ("5".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_five);
            }
            if ("6".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_six);
            }
            if ("4".equals(comments_list.get(position).getCredit_level_id())) {
                mHolder.mIv_comment_level.setImageResource(R.drawable.level_three);
            }

            mHolder.mTv_comment_content.setText(comments_list.get(position).getContent());
            mHolder.mTv_comment_name.setText(comments_list.get(position).getUser_name());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String comment_time = formatter
                    .format(Long.valueOf(comments_list.get(position).getComment_time().getTime()));
            mHolder.mTv_comment_time.setText(comment_time);
            return convertView;
        }
    }

    public class Viewholdr {

        private RoundImageView mComment_list_yuantu;
        private TextView mTv_comment_name;
        private TextView mTv_comment_time;
        private TextView mTv_comment_content;
        private ImageView mIv_comment_level;
        private ImageView mIv;

        public Viewholdr(View view) {
            mComment_list_yuantu = (RoundImageView) view.findViewById(R.id.comment_list_yuantu);
            mTv_comment_name = (TextView) view.findViewById(R.id.tv_comment_name);
            mTv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);
            mTv_comment_content = (TextView) view.findViewById(R.id.tv_comment_content);
            mIv_comment_level = (ImageView) view.findViewById(R.id.iv_comment_level);
            mIv = (ImageView) view.findViewById(R.id.iv);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                if (!TextUtils.isEmpty(errorMsg)) {
                    ToastUtils.show(getApplicationContext(), "该视界正在审核,暂不能分享...");
                    return;
                }
                ShareSDK.initSDK(ShiJieDetailActivity.this);
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                String productUrl = HttpConfig.IMAGEHOST + mShare_url;
                oks.setTitle("视界详情分享");
                oks.setTitleUrl(productUrl);
                oks.setText(mShare_content);
                oks.setImageUrl(HttpConfig.IMAGEHOST + mShare_img);
                System.out.println("什么地址" + HttpConfig.IMAGEHOST + mShare_img);

                mIv_shane_ceshi.setDrawingCacheEnabled(true);
                Bitmap bm = mIv_shane_ceshi.getDrawingCache();
                String imagePath = saveImageToGallery(ShiJieDetailActivity.this, bm);

                oks.setImagePath(imagePath);// 确保sdcard 存在图片
                oks.setUrl(productUrl);
                oks.setSite("孜孜官网");
                oks.setSiteUrl(productUrl);
                oks.setCallback(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        System.out.println("分享失败");
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "300");
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                System.out.println("积分结果" + result);
                            }
                        });
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        System.out.println("取消分享");
                    }
                });
                oks.show(ShiJieDetailActivity.this);
                break;
            case R.id.delete:
                final Dialog dialog = new Dialog(ShiJieDetailActivity.this, R.style.dialog);
                dialog.setContentView(R.layout.popup_delete_address);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER | Gravity.CENTER);
                TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                tv_msg.setText("确定删除该时视吗?");
                ((TextView) dialog.findViewById(R.id.tv_cancel)).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ((TextView) dialog.findViewById(R.id.tv_confirm)).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "73");
                        map.put("share_id", mId);
                        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                try {
                                    PreferencesUtils.putBoolean(getApplicationContext(), "isChange", true);
                                    JSONObject jsonObject = new JSONObject(result);
                                    ToastUtils.show(getApplicationContext(), jsonObject.getString("msg"));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(MyError myError) {
                                super.onFailure(myError);
                            }
                        });

                    }
                });

                dialog.show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.iv_eval:
                if (user_level.equals("0")) {
                    ToastUtils.show(getApplicationContext(), "您的等级不够，暂时无法评论...");
                } else {
                    mIntent = new Intent(ShiJieDetailActivity.this, SendEvalActivity.class);
                    mIntent.putExtra("id", mId);
                    mIntent.putExtra("type", "0");
                    startActivity(mIntent);
                }
                break;
            case R.id.re_zan:
                if (mIv_zan_gray.getVisibility() == View.VISIBLE && mIv_zan_red.getVisibility() == View.GONE) {
                    // 去点赞
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "187");
                    map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                    map.put("share_id", mId);
                    new HttpClientGet(ShiJieDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println("点赞" + result);
                            try {
                                PreferencesUtils.putBoolean(getApplicationContext(), "isChange", true);
                                JSONObject object = new JSONObject(result);
                                String msg = object.getString("msg");
                                String support = object.getString("support");
                                // ToastUtils.show(getApplicationContext(), msg);
                                mIv_zan_gray.setVisibility(View.GONE);
                                mIv_zan_red.setVisibility(View.VISIBLE);
                                mTv_praise.setText("赞  " + support);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }

                    });

                }
                if (mIv_zan_gray.getVisibility() == View.GONE && mIv_zan_red.getVisibility() == View.VISIBLE) {
                    // 取消赞
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "67");
                    map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                    map.put("share_id", mId);
                    new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println("取消赞" + result);
                            try {
                                PreferencesUtils.putBoolean(getApplicationContext(), "isChange", true);
                                JSONObject object = new JSONObject(result);
                                String msg = object.getString("msg");
                                String support = object.getString("support");
                                // ToastUtils.show(getApplicationContext(), msg);
                                mIv_zan_gray.setVisibility(View.VISIBLE);
                                mIv_zan_red.setVisibility(View.GONE);
                                mTv_praise.setText("赞  " + support);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });
                }

                break;
            case R.id.re_collect:
                if (mIv_collect_gray.getVisibility() == View.GONE && mIv_collect_red.getVisibility() == View.VISIBLE) {
                    // 取消收藏
                    HashMap<String, String> map = new HashMap<>();
                    map.put("OPT", "69");
                    map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                    map.put("share_id", mId);
                    new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            try {
                                PreferencesUtils.putBoolean(getApplicationContext(), "isChange", true);
                                JSONObject jsonObject = new JSONObject(result);
                                String msg = jsonObject.getString("msg");
                                // ToastUtils.show(getApplicationContext(), msg);
                                mIv_collect_red.setVisibility(View.GONE);
                                mIv_collect_gray.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }
                    });
                }
                if (mIv_collect_gray.getVisibility() == View.VISIBLE && mIv_collect_red.getVisibility() == View.GONE) {
                    // 去收藏
                    HashMap<String, String> map = new HashMap<>();
                    map.put("OPT", "68");
                    map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                    map.put("share_id", mId);
                    new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            try {
                                PreferencesUtils.putBoolean(getApplicationContext(), "isChange", true);
                                JSONObject jsonObject = new JSONObject(result);
                                String msg = jsonObject.getString("msg");
                                // ToastUtils.show(getApplicationContext(), msg);
                                mIv_collect_red.setVisibility(View.VISIBLE);
                                mIv_collect_gray.setVisibility(View.GONE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }
                    });
                }
                break;
            case R.id.youliao_yuantu:
                mIntent = new Intent(getApplicationContext(), PersonalShiJieStatisticsActivity.class);
                mIntent.putExtra("user_id", user_id);
                mIntent.putExtra("shijie_statistics", "shishi_detail");
                startActivityForResult(mIntent, 111);
                break;

        }
    }

    public class ShiJie {
        private List<Comments> comments;
        private String msg;
        private String error;

        public List<Comments> getComments() {
            return comments;
        }

        public void setComments(List<Comments> comments) {
            this.comments = comments;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    public class Comments {
        private String answer_time;
        private CommentTime comment_time;
        private String content;
        private String credit_level_id;
        private String entityId;
        private String id;
        private String persistent;
        private String photo;
        private String sid;
        private String sorder;
        private String user_id;
        private String user_name;

        public String getAnswer_time() {
            return answer_time;
        }

        public void setAnswer_time(String answer_time) {
            this.answer_time = answer_time;
        }

        public CommentTime getComment_time() {
            return comment_time;
        }

        public void setComment_time(CommentTime comment_time) {
            this.comment_time = comment_time;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSorder() {
            return sorder;
        }

        public void setSorder(String sorder) {
            this.sorder = sorder;
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
    }

    public class CommentTime {
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

    public String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        return file.getPath();
    }

    public class ShareBean {
        private String content;
        private String error;
        private String msg;
        private String shareImg;
        private String url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getShareImg() {
            return shareImg;
        }

        public void setShareImg(String shareImg) {
            this.shareImg = shareImg;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}
