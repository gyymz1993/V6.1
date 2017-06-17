package com.hyphenate.chatuidemo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.Constant;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.FriendDataBean;
import com.lsjr.zizisteward.bean.FriendDataBean.FriendsDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.ActivitySeparateCircleFfriends;
import com.lsjr.zizisteward.ly.activity.DeleteActivity;
import com.lsjr.zizisteward.ly.activity.Fragment_AddressBook;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends BaseActivity implements OnClickListener {

    private GridView gv;
    private TextView tv_name;
    private TextView tv_parent;
    private EditText et_remark;
    private GVAdapter gAdapter;
    private LinearLayout ll_sure;
    private ImageView headAvatar;
    private TextView tv_integral;
    private TextView tv_nike_name;
    private TextView tv_real_name;
    private FriendDataBean fdBean;
    private InputMethodManager imm;
    private TextView tv_call_number;
    private View v_call;
    private View v_one;
    private View v_two;
    private ImageView iv_level;
    private LinearLayout call;
    private LinearLayout ll_set;
    private LinearLayout ll_call;
    private LinearLayout ll_delete;
    private TextView tv_send_message;
    private LinearLayout ll_nike_name;
    private LinearLayout ll_real_name;
    private LinearLayout ll_sentenced_empty;

    private String _id;
    private String img;
    private String name;
    private String f_id;
    private String user_id;
    private String account;
    private String activity;
    private String friend_id;
    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    private List<FriendsDetail> fd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.em_activity_user_profile);
        this.initView();
    }

    private void initView() {

        this.v_one = super.findViewById(R.id.v_one);
        this.v_two = super.findViewById(R.id.v_two);
        this.v_call = super.findViewById(R.id.v_call);
        this.gv = (GridView) super.findViewById(R.id.gv);
        this.call = (LinearLayout) super.findViewById(R.id.call);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.ll_set = (LinearLayout) super.findViewById(R.id.ll_set);
        this.iv_level = (ImageView) super.findViewById(R.id.iv_level);
        this.ll_call = (LinearLayout) super.findViewById(R.id.ll_call);
        this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);
        this.et_remark = (EditText) super.findViewById(R.id.et_remark);
        this.tv_parent = (TextView) super.findViewById(R.id.tv_parent);
        this.tv_integral = (TextView) super.findViewById(R.id.tv_integral);
        this.ll_delete = (LinearLayout) super.findViewById(R.id.ll_delete);
        this.tv_nike_name = (TextView) super.findViewById(R.id.tv_nike_name);
        this.tv_real_name = (TextView) super.findViewById(R.id.tv_real_name);
        this.headAvatar = (ImageView) super.findViewById(R.id.user_head_avatar);
        this.tv_call_number = (TextView) super.findViewById(R.id.tv_call_number);
        this.ll_real_name = (LinearLayout) super.findViewById(R.id.ll_real_name);
        this.ll_nike_name = (LinearLayout) super.findViewById(R.id.ll_nike_name);
        this.tv_send_message = (TextView) super.findViewById(R.id.tv_send_message);
        this.ll_sentenced_empty = (LinearLayout) super.findViewById(R.id.ll_sentenced_empty);

        this.call.setOnClickListener(this);
        this.ll_sure.setOnClickListener(this);
        this.tv_parent.setOnClickListener(this);
        this.ll_delete.setOnClickListener(this);
        this.tv_send_message.setOnClickListener(this);
        this.imm = (InputMethodManager) UserProfileActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        this.activity = getIntent().getStringExtra("activity");

        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (null != _id && _id.length() > 0) {
                    startActivity(new Intent(UserProfileActivity.this, ActivitySeparateCircleFfriends.class)
                            .putExtra("user_id", _id)
                            .putExtra("name", tv_name.getText().toString())
                            .putExtra("photo", fdBean.getPhoto()));
                }
            }
        });

        et_remark.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ll_sure.getVisibility() == View.GONE) {
                    ll_sure.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        imm.hideSoftInputFromWindow(et_remark.getWindowToken(), 0);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fdBean = new FriendDataBean();
        fd = new ArrayList<>();

        if (activity.equals("ChatFragment")) {
            name = getIntent().getStringExtra("name");
            account = getIntent().getStringExtra("account");
            tv_send_message.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
            tv_call_number.setText(account);

            getUserId();
        } else if (activity.equals("AB")) {
            _id = getIntent().getStringExtra("_id");
            getData(_id);
            ll_delete.setVisibility(View.GONE);
            ll_call.setVisibility(View.GONE);
            v_call.setVisibility(View.GONE);
            ll_set.setVisibility(View.GONE);
            v_one.setVisibility(View.GONE);
            v_two.setVisibility(View.GONE);
        } else {
            friend_id = getIntent().getStringExtra("friend_id");
            user_id = getIntent().getStringExtra("user_id");
            img = getIntent().getStringExtra("img");
            name = getIntent().getStringExtra("name");
            account = getIntent().getStringExtra("account");
            tv_call_number.setText(account);
            getUserId();
            if (friend_id.equals("me")) {
                tv_send_message.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                ll_call.setVisibility(View.GONE);
                v_call.setVisibility(View.GONE);
            } else {
                tv_send_message.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getUserId() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "213");
        map.put("name", account);
        new HttpClientGet(UserProfileActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("查看用户ID:  " + result);
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    _id = jObject.getString("user_id");
                    f_id = _id;
                    if (null != error && null != _id) {
                        getData(_id);
                    } else {
                        ll_sentenced_empty.setVisibility(View.VISIBLE);
                        Toast.makeText(UserProfileActivity.this, "请检查网络...", Toast.LENGTH_SHORT).show();
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

    private void getData(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "210");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_id", id);

        new HttpClientGet(UserProfileActivity.this, null, map, false,
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
                            et_remark.setText(fdBean.getFriendRemark());
                            ll_nike_name.setVisibility(View.VISIBLE);
                            tv_nike_name.setText(fdBean.getUser_name());
                        }

                        if (null == fdBean.getReality_name() || fdBean.getReality_name().length() < 1) {
                            ll_real_name.setVisibility(View.INVISIBLE);
                        } else {
                            ll_real_name.setVisibility(View.VISIBLE);
                            tv_real_name.setText(fdBean.getReality_name());
                        }

                        tv_integral.setText(null == fdBean.getScore() ? "豆孜 0" : "豆孜 " + fdBean.getScore());

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
                            case 6:
                                iv_level.setImageResource(R.drawable.level_six);
                                break;
                        }


                        if (null != fd && fd.size() > 0) {
                            System.out.println(fd.size());
                            gv.setVisibility(View.VISIBLE);
                            ll_sentenced_empty.setVisibility(View.GONE);
                            Picasso.with(UserProfileActivity.this).load(HttpConfig.IMAGEHOST + fd.get(0).getPhoto()).into(headAvatar);
                            gAdapter = new GVAdapter(UserProfileActivity.this, fd);
                            gv.setAdapter(gAdapter);
                            gAdapter.notifyDataSetChanged();
                        } else {
                            if (null != fdBean.getPhoto()) {
                                Picasso.with(UserProfileActivity.this).load(HttpConfig.IMAGEHOST + fdBean.getPhoto()).into(headAvatar);
                            }
                            ll_sentenced_empty.setVisibility(View.VISIBLE);
                            gv.setVisibility(View.GONE);
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

            case R.id.ll_sure:
                imm.hideSoftInputFromWindow(et_remark.getWindowToken(), 0);

                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "255");
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("friend_id", f_id);
                    map.put("remark", null == et_remark.getText().toString() ? "" : et_remark.getText().toString());
                    new HttpClientGet(UserProfileActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String error = jsonObject.getString("error");

                                if (error.equals("1")) {
                                    ll_sure.setVisibility(View.GONE);
                                    Toast.makeText(UserProfileActivity.this, "修改备注成功...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserProfileActivity.this, "修改备注失败,请稍后重试...", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Toast.makeText(UserProfileActivity.this, "修改备注失败,请稍后重试...", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }
                    });

                break;

            case R.id.tv_send_message:

                if (name.equals(EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(UserProfileActivity.this,
                            R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(UserProfileActivity.this,
                            ChatActivity.class);
                    intent.putExtra(Constant.EXTRA_USER_ID, account);
                    intent.putExtra("userId", account);
                    intent.putExtra("nike", tv_name.getText().toString());
                    intent.putExtra("guess", false);
                    startActivity(intent);
                }

                break;

            case R.id.ll_delete:
                startActivityForResult(new Intent(UserProfileActivity.this,
                        DeleteActivity.class).putExtra("user_id", user_id)
                        .putExtra("friend_id", friend_id), 1);
                break;

            case R.id.tv_parent:

                if (null != fd && fd.size() > 0) {
                    if (null != _id && _id.length() > 0) {
                        startActivity(new Intent(UserProfileActivity.this, ActivitySeparateCircleFfriends.class)
                                .putExtra("user_id", _id)
                                .putExtra("name", tv_name.getText().toString())
                                .putExtra("photo", fdBean.getPhoto()));
                    }
                }
                break;

            case R.id.call:
                startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + account)));
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {

        switch (arg1) {
            case 2:
                Fragment_AddressBook.requestAddressBook();
                finish();
                break;
        }
    }
}

