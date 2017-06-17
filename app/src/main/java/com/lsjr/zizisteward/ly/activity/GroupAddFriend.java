package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chatuidemo.ui.UserProfileActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.FriendDataBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */

public class GroupAddFriend extends Activity implements View.OnClickListener {

    private ImageView iv;
    private TextView tv_add;
    private TextView tv_name;
    private LinearLayout ll_back;

    private String id;
    private String account;
    private List<FriendDataBean.FriendsDetail> fd = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.group_add_friend);

        this.iv = (ImageView) findViewById(R.id.iv);
        this.tv_add = (TextView) findViewById(R.id.tv_add);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.ll_back = (LinearLayout) findViewById(R.id.ll_back);

        this.account = getIntent().getStringExtra("account");

        this.getUserId();

        this.tv_add.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
    }

    private void getUserId() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "213");
        map.put("name", account);
        new HttpClientGet(GroupAddFriend.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("查看用户ID:  " + result);
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    id = jObject.getString("user_id");
                    getData(id);

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

        new HttpClientGet(GroupAddFriend.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        FriendDataBean fdBean = new Gson().fromJson(result, FriendDataBean.class);

                        fd = fdBean.getFriendsDetail();

                        tv_name.setText(fdBean.getUser_name());

                        Picasso.with(GroupAddFriend.this).load(HttpConfig.IMAGEHOST +  fdBean.getPhoto()).into(iv);
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_add:
                CustomDialogUtils.startCustomProgressDialog(GroupAddFriend.this, "请稍候...");
                int pos = (int) v.getTag();
                Map<String, String> map = new HashMap<>();
                map.put("OPT", "248");
                map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                map.put("friend_id", id);
                new HttpClientGet(GroupAddFriend.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {

                        try {
                            CustomDialogUtils.stopCustomProgressDialog(GroupAddFriend.this);
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");
                            if (error.equals("1")) {
                                Toast.makeText(GroupAddFriend.this, "已向对方提交申请,请等待对方同意...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GroupAddFriend.this, "提交申请失败...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            CustomDialogUtils.stopCustomProgressDialog(GroupAddFriend.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        CustomDialogUtils.stopCustomProgressDialog(GroupAddFriend.this);
                        super.onFailure(myError);
                    }
                });

                break;
        }
    }
}
