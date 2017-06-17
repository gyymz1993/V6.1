package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.FriendsCircleDetailBean;
import com.lsjr.zizisteward.bean.FriendsCircleDetailBean.Comments;
import com.lsjr.zizisteward.bean.FriendsCircleDetailBean.Share;
import com.lsjr.zizisteward.bean.FriendsCircleDetailBean.Shop;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.photoflow.ImagePagerActivity;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class ActivityFriendsCircleDetails extends Activity implements OnClickListener {

    private TextView tv_buy;
    private TextView tv_num;
    private TextView tv_send;
    private TextView tv_name;
    private TextView tv_time;
    private ImageView iv_like;
    private ImageView iv_goods;
    private EditText et_content;
    private LinearLayout ll_pop;
    private TextView tv_content;
    private LinearLayout ll_back;
    private CircleImageView c_iv;
    private LinearLayout ll_like;
    private com.lsjr.zizisteward.newview.MyListView mlv_picture;
    private TextView tv_goods_name;
    private LinearLayout ll_publish;
    private com.lsjr.zizisteward.newview.MyListView mlv_comments;
    private TextView tv_goods_price;
    private RelativeLayout rl_goods;
    private LinearLayout ll_parent;
    private LinearLayout ll_delete;
    private ImageView iv_like_cancel;
    private LinearLayout ll_share;

    private Share share;
    private Shop shop;
    private List<Comments> comments = new ArrayList<>();

    private CAdapter cAdapter;
    private ShowAdapter sAdapter;

    private int width;
    private String name;
    private String photo;
    private String[] str;
    private ScrollView sv;
    private String user_id;
    private String[] img_wh;
    private String share_id;
    private int pos;
    private InputMethodManager imm;

    private boolean space = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_friends_circle_details);
        this.findViewById();
    }

    private void findViewById() {

        this.sv = (ScrollView) super.findViewById(R.id.sv);
        this.tv_num = (TextView) super.findViewById(R.id.tv_num);
        this.tv_buy = (TextView) super.findViewById(R.id.tv_buy);
        this.tv_time = (TextView) super.findViewById(R.id.tv_time);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.tv_send = (TextView) super.findViewById(R.id.tv_send);
        this.iv_like = (ImageView) super.findViewById(R.id.iv_like);
        this.c_iv = (CircleImageView) super.findViewById(R.id.c_iv);
        this.ll_pop = (LinearLayout) super.findViewById(R.id.ll_pop);
        this.iv_goods = (ImageView) super.findViewById(R.id.iv_goods);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_like = (LinearLayout) super.findViewById(R.id.ll_like);
        this.tv_content = (TextView) super.findViewById(R.id.tv_content);
        this.et_content = (EditText) super.findViewById(R.id.et_content);
        this.ll_share = (LinearLayout) super.findViewById(R.id.ll_share);
        this.ll_delete = (LinearLayout) super.findViewById(R.id.ll_delete);
        this.ll_parent = (LinearLayout) super.findViewById(R.id.ll_parent);
        this.rl_goods = (RelativeLayout) super.findViewById(R.id.rl_goods);
        this.ll_publish = (LinearLayout) super.findViewById(R.id.ll_publish);
        this.mlv_comments = (MyListView) super.findViewById(R.id.mlv_comments);
        this.tv_goods_name = (TextView) super.findViewById(R.id.tv_goods_name);
        this.tv_goods_price = (TextView) super.findViewById(R.id.tv_goods_price);
        this.iv_like_cancel = (ImageView) super.findViewById(R.id.iv_like_cancel);
        this.mlv_picture = (com.lsjr.zizisteward.newview.MyListView) super.findViewById(R.id.mlv_picture);

        this.ll_pop.setOnClickListener(this);
        this.tv_buy.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.iv_like.setOnClickListener(this);
        this.tv_send.setOnClickListener(this);
        this.ll_share.setOnClickListener(this);
        this.ll_delete.setOnClickListener(this);
        this.iv_like_cancel.setOnClickListener(this);

        pos = getIntent().getIntExtra("pos", 19900707);
        name = getIntent().getStringExtra("name");
        photo = getIntent().getStringExtra("photo");
        user_id = getIntent().getStringExtra("user_id");
        share_id = getIntent().getStringExtra("share_id");

        if (null != name) {
            this.tv_name.setText(name);
        }

        if (null != photo) {
            Glide.with(ActivityFriendsCircleDetails.this).load(HttpConfig.IMAGEHOST + photo).into(c_iv);
        }

        getData();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //getPeople();
    }

    private void getPeople() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "502");
        map.put("shareId", share_id);
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(user_id), "u"));

        new HttpClientGet(ActivityFriendsCircleDetails.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("502   " + result);

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }

        });
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "237");
        map.put("share_id", share_id);
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(user_id), "u"));
        new HttpClientGet(ActivityFriendsCircleDetails.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println("朋友圈 详情:  " + result);

                FriendsCircleDetailBean fcdBean = new Gson().fromJson(result, FriendsCircleDetailBean.class);

                share = fcdBean.getShare();
                comments = fcdBean.getComments();
                shop = fcdBean.getShop();

                if (null != share && null != share.getShareImg() && share.getShareImg().length() > 0) {
                    str = share.getShareImg().split(",");

                    if (null == str || str.length < 1) {
                        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mlv_picture
                                .getLayoutParams(); // 取控件textView当前的布局参数
                        linearParams.height = 1;
                        mlv_picture.setLayoutParams(linearParams);
                    } else {
                        sAdapter = new ShowAdapter(ActivityFriendsCircleDetails.this, str);
                        mlv_picture.setAdapter(sAdapter);
                        sAdapter.notifyDataSetChanged();
                    }
                }

                if (null != share) {

                    if (null != share.getShare_time_uid() && share.getShare_time_uid().length() > 0) {
                        String[] space = share.getShare_time_uid().split(",");

                        boolean ly = false;

                        for (int i = 0; i < space.length; i++) {
                            if (space[i].equals(App.getUserInfo().getId())) {
                                ly = true;
                            }
                        }

                        if (ly) {
                            iv_like_cancel.setVisibility(View.VISIBLE);
                            iv_like.setVisibility(View.GONE);
                        } else {
                            iv_like_cancel.setVisibility(View.GONE);
                            iv_like.setVisibility(View.VISIBLE);
                        }

                    } else {
                        iv_like_cancel.setVisibility(View.GONE);
                        iv_like.setVisibility(View.VISIBLE);
                    }
                }

                if (null != share && null != share.getImg_wh() && share.getImg_wh().length() > 0) {
                    img_wh = share.getImg_wh().split(",");
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                if (null != share & null != share.getShare_time().getTime()) {

                    Date date = new Date(Long.valueOf(share.getShare_time().getTime()));

                    if (null != date) {
                        tv_time.setText(format.format(date));
                    }
                }

                if (null != share && null != share.getContent()) {
                    tv_content.setText(share.getContent());
                }

                if (null != share && null != share.getShare_like()) {
                    tv_num.setText(returnNumber(share.getShare_like()));
                }

                if (null != comments && comments.size() > 0) {
                    cAdapter = new CAdapter(ActivityFriendsCircleDetails.this, comments);
                    mlv_comments.setAdapter(cAdapter);
                    cAdapter.notifyDataSetChanged();
                } else {
                    RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mlv_comments
                            .getLayoutParams(); // 取控件textView当前的布局参数
                    linearParams.height = 1;
                    mlv_comments.setLayoutParams(linearParams);
                }

                if (null != shop) {

                    if (null != shop.getSpicfirst()) {
                        Glide.with(ActivityFriendsCircleDetails.this).load(HttpConfig.IMAGEHOST + shop.getSpicfirst())
                                .into(iv_goods);
                    }

                    if (null != shop.getSprice()) {
                        tv_goods_price.setText("¥" + shop.getSprice());
                    }

                    if (null != shop.getSname()) {
                        tv_goods_name.setText(shop.getSname());
                    }

                    if (null != shop.getShopUrl()) {
                        rl_goods.setVisibility(View.VISIBLE);
                    }
                }

                if (App.getUserInfo().getId().equals(share.getUser_id())) {
                    ll_delete.setVisibility(View.VISIBLE);
                } else {
                    ll_delete.setVisibility(View.INVISIBLE);
                }

                DisplayMetrics dm = new DisplayMetrics();
                ActivityFriendsCircleDetails.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                width = dm.widthPixels;

                sv.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private class ShowAdapter extends BaseAdapter {

        private Context context;
        private String[] str;
        private ShowHolder sHolder;

        public ShowAdapter(Context context, String[] str) {
            this.context = context;
            this.str = str;
        }

        @Override
        public int getCount() {
            return null == str ? 0 : str.length;
        }

        @Override
        public Object getItem(int position) {
            return str[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.show_item, null);

                sHolder = new ShowHolder(convertView);

                convertView.setTag(sHolder);

            } else {

                sHolder = (ShowHolder) convertView.getTag();
            }

            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) sHolder.iv.getLayoutParams();
            double space = 0;
            double _with = Double.valueOf(img_wh[position * 2]);
            double _height = Double.valueOf(img_wh[position * 2 + 1]);

            space = (width - 160) / _with;

            linearParams.width = width - 160;
            linearParams.height = (int) (_height * space);

            linearParams.leftMargin = 30;
            // linearParams.rightMargin = 80;
            sHolder.iv.setLayoutParams(linearParams);

            Picasso.with(context).load(HttpConfig.IMAGEHOST + str[position]).into(sHolder.iv);

            final ArrayList<String> imageUrls = new ArrayList<>(Arrays.asList(str));

            sHolder.iv.setTag(position);
            sHolder.iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    imageBrower(pos, imageUrls);
                }
            });

            return convertView;
        }

        protected void imageBrower(int position, ArrayList<String> urls2) {
            Intent intent = new Intent(context, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            context.startActivity(intent);
        }

        private class ShowHolder {
            private ImageView iv;

            public ShowHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
            }
        }
    }

    private class CAdapter extends BaseAdapter {
        private List<Comments> comments;
        private Context context;
        private AHolder aHolder;

        public CAdapter(Context context, List<Comments> comments) {
            this.context = context;
            this.comments = comments;
        }

        @Override
        public int getCount() {
            return null == comments ? 0 : comments.size();
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.comments_item, null);

                aHolder = new AHolder(convertView);

                convertView.setTag(aHolder);

            } else {
                aHolder = (AHolder) convertView.getTag();
            }

            aHolder.tv_content.setText(comments.get(position).getContent());
            aHolder.tv_name.setText(comments.get(position).getUser_name());

            if (null != comments.get(position).getComment_time().getTime()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(Long.valueOf(comments.get(position).getComment_time().getTime()));
                if (null != date) {
                    aHolder.tv_time.setText(format.format(date));
                }
            }

            if (null != comments.get(position).getPhoto()) {
                Glide.with(context).load(HttpConfig.IMAGEHOST + comments.get(position).getPhoto())
                        .into(aHolder.clv_head);
            }

            if (comments.get(position).getUser_id().equals(App.getUserInfo().getId())) {
                aHolder.tv_delete.setVisibility(View.VISIBLE);
            } else {
                aHolder.tv_delete.setVisibility(View.GONE);
            }

            aHolder.tv_delete.setTag(position);
            aHolder.tv_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int pos = (int) v.getTag();
                    CustomDialogUtils.startCustomProgressDialog(context, "请稍候");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "501");
                    System.out.println(comments.get(pos).getId());
                    map.put("shareId", comments.get(pos).getId());

                    new HttpClientGet(ActivityFriendsCircleDetails.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CustomDialogUtils.stopCustomProgressDialog(context);
                            System.out.println(result);

                            try {
                                JSONObject jObject = new JSONObject(result);
                                String error = jObject.getString("error");

                                if (error.equals("1")) {
                                    comments.remove(pos);
                                    cAdapter.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(context);
                            super.onFailure(myError);
                        }
                    });
                }
            });

            return convertView;
        }

        private class AHolder {
            private TextView tv_name;
            private TextView tv_content;
            private TextView tv_time;
            private CircleImageView clv_head;
            private TextView tv_delete;

            public AHolder(View v) {
                this.tv_delete = (TextView) v.findViewById(R.id.tv_delete);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_time = (TextView) v.findViewById(R.id.tv_time);
                this.tv_content = (TextView) v.findViewById(R.id.tv_content);
                this.clv_head = (CircleImageView) v.findViewById(R.id.clv_head);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_back:
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);

                finish();
                break;

            case R.id.ll_pop:

                this.ll_pop.setVisibility(View.GONE);
                if (null != shop) {
                    if (null != shop.getShopUrl()) {
                        rl_goods.setVisibility(View.GONE);
                    }
                }
                this.ll_publish.setVisibility(View.VISIBLE);

                et_content.setFocusable(true);
                et_content.setFocusableInTouchMode(true);
                et_content.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) et_content.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_content, 0);

                break;

            case R.id.iv_like_cancel:
                CustomDialogUtils.startCustomProgressDialog(ActivityFriendsCircleDetails.this, "请稍候");

                Map<String, String> _map = new HashMap<>();
                _map.put("OPT", "67");
                _map.put("share_id", share_id);
                _map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                new HttpClientGet(ActivityFriendsCircleDetails.this, null, _map, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                CustomDialogUtils.stopCustomProgressDialog(ActivityFriendsCircleDetails.this);
                                System.out.println(result);
                                JSONObject jObject;
                                try {
                                    jObject = new JSONObject(result);

                                    String error = jObject.getString("error");
                                    String msg = jObject.getString("msg");

                                    if (error.equals("1")) {
//									String support = jObject.getString("support");
//									share.setShare_like(support);
//									tv_num.setText(support);
                                        iv_like_cancel.setVisibility(View.GONE);
                                        iv_like.setVisibility(View.VISIBLE);
                                        tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) - 1));

                                        Fragment_Circle_Friends.circle.get(pos).setShare_like(tv_num.getText().toString());
                                        Fragment_Circle_Friends.circle.get(pos).setLy(false);
                                        Fragment_Circle_Friends.fcf_adapter.notifyDataSetChanged();

                                        space = true;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(MyError myError) {
                                CustomDialogUtils.stopCustomProgressDialog(ActivityFriendsCircleDetails.this);
                                super.onFailure(myError);
                            }
                        });

                break;

            case R.id.iv_like:
                CustomDialogUtils.startCustomProgressDialog(ActivityFriendsCircleDetails.this, "请稍候");
                Map<String, String> map = new HashMap<>();
                map.put("OPT", "187");
                map.put("share_id", share_id);
                map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                new HttpClientGet(ActivityFriendsCircleDetails.this, null, map, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                CustomDialogUtils.stopCustomProgressDialog(ActivityFriendsCircleDetails.this);
                                System.out.println(result);
                                JSONObject jObject;
                                try {
                                    jObject = new JSONObject(result);

                                    String error = jObject.getString("error");
                                    String msg = jObject.getString("msg");

                                    if (error.equals("1")) {

                                        iv_like_cancel.setVisibility(View.VISIBLE);
                                        iv_like.setVisibility(View.GONE);
                                        //getData();
                                        tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) + 1));
                                        Fragment_Circle_Friends.circle.get(pos).setShare_like(tv_num.getText().toString());
                                        Fragment_Circle_Friends.circle.get(pos).setLy(true);
                                        Fragment_Circle_Friends.fcf_adapter.notifyDataSetChanged();
                                        space = true;
                                    }

                                    Toast.makeText(ActivityFriendsCircleDetails.this, msg, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(MyError myError) {
                                CustomDialogUtils.stopCustomProgressDialog(ActivityFriendsCircleDetails.this);
                                super.onFailure(myError);
                            }
                        });

                break;

            case R.id.tv_buy:
                startActivity(new Intent(ActivityFriendsCircleDetails.this, HomeBrandDetail.class).putExtra("sid",
                        shop.getSid()));

                break;

            case R.id.tv_send:

                String content = et_content.getText().toString();

                if (null != content && content.length() > 0) {
                    Map<String, String> map_send = new HashMap<>();
                    map_send.put("OPT", "188");
                    map_send.put("share_id", share_id);
                    map_send.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map_send.put("content", content);

                    new HttpClientGet(ActivityFriendsCircleDetails.this, null, map_send, false,
                            new HttpClientGet.CallBacks<String>() {

                                @Override
                                public void onSuccess(String result) {
                                    et_content.setText("");
                                    imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                                    getData();
                                    Fragment_Circle_Friends.circle.get(pos)
                                            .setShare_comment(String.valueOf(Integer.valueOf(
                                                    Fragment_Circle_Friends.circle
                                                            .get(pos).getShare_comment()) + 1));
                                    Fragment_Circle_Friends.fcf_adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(MyError myError) {
                                    super.onFailure(myError);
                                }
                            });
                }

                break;

            case R.id.ll_delete:
                CustomDialogUtils.startCustomProgressDialog(ActivityFriendsCircleDetails.this, "正在删除,请稍候!");
                Map<String, String> map_delete = new HashMap<>();
                map_delete.put("OPT", "239");
                map_delete.put("share_id", share.getId());
                new HttpClientGet(ActivityFriendsCircleDetails.this, null, map_delete, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                CustomDialogUtils.stopCustomProgressDialog(ActivityFriendsCircleDetails.this);
                                setResult(1);
                                finish();
                            }

                            @Override
                            public void onFailure(MyError myError) {
                                CustomDialogUtils.stopCustomProgressDialog(ActivityFriendsCircleDetails.this);
                                super.onFailure(myError);
                            }
                        });
                break;

            case R.id.ll_share:
                Map<String,String> map_share = new HashMap<>();
                map_share.put("OPT","512");
                map_share.put("order_id",share_id);
                new HttpClientGet(ActivityFriendsCircleDetails.this,null,map_share,false, new HttpClientGet.CallBacks<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            String content = object.getString("content");
                            String url = object.getString("url");
                            String shareImg = object.getString("shareImg");
                            String msg = object.getString("msg");

                            ShareSDK.initSDK(ActivityFriendsCircleDetails.this);
                            OnekeyShare oks = new OnekeyShare();
                            oks.disableSSOWhenAuthorize();
                            String productUrl = HttpConfig.IMAGEHOST + url;
                            oks.setTitle("朋友圈分享");
                            oks.setTitleUrl(productUrl);
                            oks.setImageUrl(HttpConfig.IMAGEHOST + shareImg);
                            //oks.setText(productUrl);
                            //oks.setImagePath("/sdcard/logods.jpg");//确保sdcard 存在图片
                            oks.setUrl(productUrl);
                            oks.setText("您的好友发现了一个新世界，点击开启新世界入口");
                            oks.setSite("孜孜管家");
                            oks.setSiteUrl(productUrl);

                            oks.setCallback(new PlatformActionListener() {

                                @Override
                                public void onError(Platform arg0, int arg1, Throwable arg2) {
                                    System.out.println("分享失败");
                                }

                                @Override
                                public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                                    App.AddShareRecord(ActivityFriendsCircleDetails.this,2);
                                }

                                @Override
                                public void onCancel(Platform arg0, int arg1) {
                                    System.out.println("取消分享");
                                }
                            });

                            oks.show(ActivityFriendsCircleDetails.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
                break;
        }
    }

    private String returnNumber(String space) {

        int length = space.length();

        if (length <= 4) {
            return space;
        } else if (length <= 8) {
            return space.substring(0, length - 4) + "万";
        } else if (length > 8) {
            return space.substring(0, length - 8) + "亿";
        }

        return space;
    }
}
