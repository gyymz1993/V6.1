package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.FriendDataBean;
import com.lsjr.zizisteward.bean.FriendDataBean.FriendsDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrCodePersonalActivity extends Activity implements OnClickListener {

    private RoundImageView riv_head;
    private GridView gv;
    private TextView tv_name;
    private GVAdapter gAdapter;
    private TextView tv_integral;
    private TextView tv_nike_name;
    private TextView tv_real_name;
    private ImageView iv_level;
    private LinearLayout ll_nike_name;
    private LinearLayout ll_real_name;
    private LinearLayout ll_back;
    private TextView tv_add;
    private LinearLayout ll_sentenced_empty;
    private FriendDataBean fdBean;
    private List<FriendsDetail> fd = new ArrayList<>();

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.qr_code_personal_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.ll_sentenced_empty = (LinearLayout) super.findViewById(R.id.ll_sentenced_empty);
        this.gv = (GridView) super.findViewById(R.id.gv);
        this.tv_add = (TextView) super.findViewById(R.id.tv_add);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.iv_level = (ImageView) super.findViewById(R.id.iv_level);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.riv_head = (RoundImageView) super.findViewById(R.id.riv_head);
        this.tv_integral = (TextView) super.findViewById(R.id.tv_integral);
        this.tv_real_name = (TextView) super.findViewById(R.id.tv_real_name);
        this.tv_nike_name = (TextView) super.findViewById(R.id.tv_nike_name);
        this.ll_nike_name = (LinearLayout) super.findViewById(R.id.ll_nike_name);
        this.ll_real_name = (LinearLayout) super.findViewById(R.id.ll_real_name);

        this.tv_add.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");

        getData(id);
    }

    private void getData(String _id) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "432");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friendid", _id);

        new HttpClientGet(QrCodePersonalActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        fdBean = new Gson().fromJson(result, FriendDataBean.class);

                        fd = fdBean.getFriendsDetail();

                        if (null == fdBean.getFriendRemark() || fdBean.getFriendRemark().length() < 1) {
                            tv_name.setText(fdBean.getUser_name());
                            ll_nike_name.setVisibility(View.GONE);
                        } else {
                            tv_name.setText(fdBean.getFriendRemark());
                            ll_nike_name.setVisibility(View.VISIBLE);
                            tv_nike_name.setText(fdBean.getUser_name());
                        }

                        if (null == fdBean.getReality_name() || fdBean.getReality_name().length() < 1) {
                            ll_real_name.setVisibility(View.INVISIBLE);
                        } else {
                            ll_real_name.setVisibility(View.VISIBLE);
                            tv_real_name.setText(fdBean.getReality_name());
                        }

                        tv_integral.setText(null == fdBean.getScore() ? "积分 0" : "积分 " + fdBean.getScore());

                        switch (Integer.valueOf(fdBean.getCredit_level_id())) {
                            case 0:
                                iv_level.setImageResource(R.drawable.level_zero);
                                break;

                            case 1:
                                iv_level.setImageResource(R.drawable.level_one);
                                break;

                            case 2:
                                iv_level.setImageResource(R.drawable.level_two);
                                break;

                            case 3:
                                iv_level.setImageResource(R.drawable.level_three);
                                break;

                            case 4:
                                iv_level.setImageResource(R.drawable.level_three);
                                break;

                            case 5:
                                iv_level.setImageResource(R.drawable.level_five);
                                break;
                        }

                        if (null != fd && fd.size() > 0) {
                            System.out.println(fd.size());
                            gv.setVisibility(View.VISIBLE);
                            Picasso.with(QrCodePersonalActivity.this).load(HttpConfig.IMAGEHOST + fd.get(0).getPhoto()).into(riv_head);
                            gAdapter = new GVAdapter(QrCodePersonalActivity.this, fd);
                            gv.setAdapter(gAdapter);
                            gAdapter.notifyDataSetChanged();
                        } else {
                            if (null != fdBean.getPhoto()) {
                                Picasso.with(QrCodePersonalActivity.this).load(HttpConfig.IMAGEHOST + fdBean.getPhoto()).into(riv_head);
                            }

                            ll_sentenced_empty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
    }

    private class GVAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<FriendsDetail> fd;

        public GVAdapter(Context context, List<FriendsDetail> fd) {
            this.context = context;
            this.fd = fd;
        }

        @Override
        public int getCount() {
            return null == fd ? 0 : fd.size();
        }

        @Override
        public Object getItem(int position) {
            return fd.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.user_profile_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            if (null != fd.get(position).getShareImg() && fd.get(position).getShareImg().length() > 0) {
                String[] str = (fd.get(position).getShareImg()).split(",");

                if (null != str && str.length > 0) {
                    System.out.println(str[0]);
                    Picasso.with(context).load(HttpConfig.IMAGEHOST + str[0]).into(view.iv);
                } else {
                    view.iv.setImageResource(R.drawable.icon_new_none);
                }
            } else {
                view.iv.setImageResource(R.drawable.icon_new_none);
            }

            return convertView;
        }

        private class ViewHolder {
            private ImageView iv;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_add:

                IfIsFriends();

                break;
        }
    }

    private void IfIsFriends() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "267");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_id", id);
        new HttpClientGet(QrCodePersonalActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    if (error.equals("1")) {
                        AddFriends();
                    } else {
                        Toast.makeText(QrCodePersonalActivity.this, "已是好友...", Toast.LENGTH_SHORT).show();
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

    private void AddFriends() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "248");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_id", id);
        new HttpClientGet(QrCodePersonalActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    if (error.equals("1")) {
                        Toast.makeText(QrCodePersonalActivity.this, "已向对方提交申请,请等待对方同意...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QrCodePersonalActivity.this, "提交申请失败...", Toast.LENGTH_SHORT).show();
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
