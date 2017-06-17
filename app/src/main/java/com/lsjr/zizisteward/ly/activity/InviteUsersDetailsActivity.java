package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.UserProfileActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.WhetherBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InviteUsersDetailsActivity extends Activity {

    private String id;
    private TextView tv_add;
    private TextView tv_send;
    private TextView tv_nike;
    private WhetherBean wBean;
    private TextView tv_number;
    private RoundImageView riv;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.invite_users_details_activity);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_send = (TextView) findViewById(R.id.tv_send);
        riv = (RoundImageView) findViewById(R.id.riv);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_nike = (TextView) findViewById(R.id.tv_nike);

        id = getIntent().getStringExtra("id");

        getUserDta();

        ll_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wBean.getUser().getName().equals(EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(InviteUsersDetailsActivity.this,
                            R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(InviteUsersDetailsActivity.this,
                            ChatActivity.class);
                    intent.putExtra(Constant.EXTRA_USER_ID, wBean.getUser().getName());
                    intent.putExtra("userId", wBean.getUser().getName());
                    intent.putExtra("nike", wBean.getUser().getUser_name());
                    intent.putExtra("guess", false);
                    startActivity(intent);
                }

            }
        });

        tv_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AddFriends();
            }
        });
    }

    private void AddFriends() {
        CustomDialogUtils.startCustomProgressDialog(InviteUsersDetailsActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "248");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_id", id);
        new HttpClientGet(InviteUsersDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(InviteUsersDetailsActivity.this);
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    if (error.equals("1")) {
                        Toast.makeText(InviteUsersDetailsActivity.this, "已向对方提交申请,请等待对方同意...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InviteUsersDetailsActivity.this, "提交申请失败...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(InviteUsersDetailsActivity.this);
                super.onFailure(myError);
            }
        });
    }

    private void getUserDta() {
        CustomDialogUtils.startCustomProgressDialog(InviteUsersDetailsActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "322");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_id", Fragment_ChatList.addSign(Long.valueOf(id), "u"));
        new HttpClientGet(InviteUsersDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(InviteUsersDetailsActivity.this);
                System.out.println(result);
                wBean = new Gson().fromJson(result, WhetherBean.class);

                if (wBean.getWhether().equals("1")) {
                    //是好友
                    tv_send.setVisibility(View.VISIBLE);
                } else {
                    tv_add.setVisibility(View.VISIBLE);
                }

                tv_number.setText(wBean.getUser().getName());
                tv_nike.setText(wBean.getUser().getUser_name());
                Glide.with(InviteUsersDetailsActivity.this).load(HttpConfig.IMAGEHOST + wBean.getUser().getPhoto()).into(riv);
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(InviteUsersDetailsActivity.this);
                super.onFailure(myError);
            }
        });
    }
}
