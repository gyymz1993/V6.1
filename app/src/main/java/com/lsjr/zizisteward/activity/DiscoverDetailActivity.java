package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.DiscoverDetailBean;
import com.lsjr.zizisteward.bean.DiscoverDetailBean.CommentsList;
import com.lsjr.zizisteward.bean.DiscoverDetailBean.Share;
import com.lsjr.zizisteward.bean.DiscoverDetailBean.Shop;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("SimpleDateFormat")
public class DiscoverDetailActivity extends BaseActivity implements OnClickListener {

    private ListView mListview_comment;
    private EditText mEt_comments;
    private Button mBt_send;
    private String mShare_id;
    private DiscoverDetailBean mBean;
    private CircleImageView mYouliao_yuantu;
    private TextView mTv_username;
    private TextView mTv_time;
    private ImageView mIv_pic_one;
    private ImageView mIv_pic_two;
    private ImageView mIv_pic_three;
    private TextView mTv_content;
    private TextView mTv_count;
    private TextView mTv_name;
    private TextView mTv_price;
    private List<CommentsList> list = new ArrayList<CommentsList>();
    private String mShopUrl;
    private TextView mTv_buy;
    private MyCommentAdapter mAdapter;
    private RelativeLayout mRe_dianzan;
    private Map<String, String> mMap;
    private Intent mIntent;

    @Override
    public int getContainerView() {
        return R.layout.activity_discover_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("发现详情");
        mShare_id = getIntent().getStringExtra("share_id");
        mListview_comment = (ListView) findViewById(R.id.listview_comment);
        mEt_comments = (EditText) findViewById(R.id.et_comments);
        mBt_send = (Button) findViewById(R.id.bt_send);
        mYouliao_yuantu = (CircleImageView) findViewById(R.id.youliao_yuantu);
        mTv_username = (TextView) findViewById(R.id.tv_username);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mIv_pic_one = (ImageView) findViewById(R.id.iv_pic_one);
        mIv_pic_two = (ImageView) findViewById(R.id.iv_pic_two);
        mIv_pic_three = (ImageView) findViewById(R.id.iv_pic_three);
        mTv_content = (TextView) findViewById(R.id.tv_content);
        mTv_count = (TextView) findViewById(R.id.tv_count);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_price = (TextView) findViewById(R.id.tv_price);
        mTv_buy = (TextView) findViewById(R.id.tv_buy);

        mRe_dianzan = (RelativeLayout) findViewById(R.id.RelativeLayout99);

        initLayout();
        getData();

    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "46");
        map.put("share_id", mShare_id);
        map.put("sorder", "");
        new HttpClientGet(DiscoverDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("发现详情" + result);
                mBean = GsonUtil.getInstance().fromJson(result, DiscoverDetailBean.class);
                Share share = mBean.getShare();
                list = mBean.getComments();

                if (list != null && 0 < list.size()) {
                    mAdapter = new MyCommentAdapter(DiscoverDetailActivity.this, list);
                    mListview_comment.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                String pic = share.getShareImg();
                String[] pics = pic.split(",");
                Picasso.with(DiscoverDetailActivity.this).load(HttpConfig.IMAGEHOST + share.getPhoto())
                        .into(mYouliao_yuantu);

                Picasso.with(DiscoverDetailActivity.this).load(HttpConfig.IMAGEHOST + pics[0]).into(mIv_pic_one);
                Picasso.with(DiscoverDetailActivity.this).load(HttpConfig.IMAGEHOST + pics[1]).into(mIv_pic_two);
                mTv_username.setText(share.getUser_name());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = formatter.format(share.getShare_time().getTime());
                mTv_time.setText(time);
                mTv_content.setText(share.getContent());
                mTv_count.setText(share.getShare_like());
                Shop shop = mBean.getShop();

                Picasso.with(DiscoverDetailActivity.this).load(HttpConfig.IMAGEHOST + shop.getSpicfirst())
                        .into(mIv_pic_three);
                mTv_name.setText(shop.getSname());
                mTv_price.setText("￥" + shop.getSprice());
                mShopUrl = shop.getShopUrl();

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void initLayout() {
        mBt_send.setOnClickListener(this);
        mTv_buy.setOnClickListener(this);
        mRe_dianzan.setOnClickListener(this);
        mYouliao_yuantu.setOnClickListener(this);

    }

    public class MyCommentAdapter extends BaseAdapter {
        private Context context;
        private List<CommentsList> list;

        public MyCommentAdapter(Context context, List<CommentsList> list) {
            this.list = list;
            this.context = context;

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
                convertView = LayoutInflater.from(DiscoverDetailActivity.this).inflate(R.layout.item_comment, parent,
                        false);
            }
            CircleImageView photo = BaseViewHolder.get(convertView, R.id.photo);

            TextView tv_comment_name = BaseViewHolder.get(convertView, R.id.tv_comment_name);
            TextView tv_comment_content = BaseViewHolder.get(convertView, R.id.tv_comment_content);
            TextView comment_time = BaseViewHolder.get(convertView, R.id.comment_time);
            Picasso.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto()).into(photo);
            tv_comment_name.setText(list.get(position).getUser_name());
            tv_comment_content.setText(list.get(position).getContent());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(list.get(position).getComment_time().getTime());
            comment_time.setText(time);

            return convertView;
        }

    }

    @Override
    public void onClick(View v) {
        mMap = new HashMap<String, String>();
        switch (v.getId()) {
            case R.id.bt_send:
                String content = mEt_comments.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(DiscoverDetailActivity.this, "您还没有评论哦,评论一番吧!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mMap.put("OPT", "188");
                mMap.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                mMap.put("share_id", mShare_id);
                mMap.put("content", content);
                new HttpClientGet(DiscoverDetailActivity.this, null, mMap, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("晒单评论" + result);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                        if ("1".equals(bean.getError())) {
                            getData();
                            mEt_comments.setText(null);

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                            }, 100);
                        }

                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });

                break;
            case R.id.tv_buy:
                mIntent = new Intent(DiscoverDetailActivity.this, BaskBrandDetailActivity.class);
                mIntent.putExtra("mShopUrl", mShopUrl);
                startActivity(mIntent);

                break;
            case R.id.RelativeLayout99:// 点赞
                getBaskSupprt("187", "");

                break;
            case R.id.youliao_yuantu:
                mIntent = new Intent(DiscoverDetailActivity.this, DiscoverListActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private void getBaskSupprt(String opt, final String content) {
        mMap = new HashMap<String, String>();
        mMap.put("OPT", opt);
        mMap.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        mMap.put("share_id", mShare_id);
        new HttpClientGet(DiscoverDetailActivity.this, null, mMap, false, new HttpClientGet.CallBacks<String>() {

            @SuppressWarnings("unused")
            @Override
            public void onSuccess(String result) {
                JSONObject mJsonObject;
                try {
                    mJsonObject = new JSONObject(result.toString());
                    String error = mJsonObject.getString("error");
                    String msg = mJsonObject.getString("msg");
                    if ("".equals(content)) {
                        mTv_count.setText(String.valueOf(Integer.valueOf(mTv_count.getText().toString()) + 1));

                    }

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
}
