package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupMemberBean;
import com.lsjr.zizisteward.bean.GroupMemberBean.GroupMember;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GanapatiDataActivity extends Activity implements OnClickListener {
    /**
     * 返回键
     */
    private LinearLayout ll_back;
    /**
     * 群名称
     */
    public static TextView tv_name;
    /**
     * 群名称parent
     */
    private LinearLayout ll_name;
    /**
     * 群成员parent
     */
    private LinearLayout ll_all;
    /**
     * 六个群成员展示
     */
    private RoundImageView iv_one, iv_two, iv_three, iv_four, iv_five;
    /**
     * 群成员数量
     */
    private TextView tv_num;
    /**
     * 群成员上限
     */
    private TextView tv_max;
    /**
     * 分享二维码
     */
    private LinearLayout ll_share_code;
    /**
     * 邀请好友
     */
    private LinearLayout ll_invite_friends;
    /**
     * 简介parent
     */
    private LinearLayout ll_synopsis;
    /**
     * 简介
     */
    private TextView tv_synopsis;
    /**
     * 公开群
     */
    private SwitchButton swb;
    /**
     * 清空群聊消息
     */
    private LinearLayout ll_clear;
    /**
     * 群分享
     */
    private LinearLayout ll_group_share;
    private TextView tv_title;
    private TextView tv_dissolve;

    public static String Groupname;
    public static String Groupmin;
    public static String Groupmax;
    public static String Groupid;
    public static String Groupisopen;
    public static String Groupisowner;
    public static String Groupdescription;
    public static List<GroupMember> groupMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ganapati_data_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.swb = (SwitchButton) super.findViewById(R.id.swb);
        this.tv_num = (TextView) super.findViewById(R.id.tv_num);
        this.tv_max = (TextView) super.findViewById(R.id.tv_max);
        this.iv_two = (RoundImageView) super.findViewById(R.id.iv_two);
        this.iv_one = (RoundImageView) super.findViewById(R.id.iv_one);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.iv_four = (RoundImageView) super.findViewById(R.id.iv_four);
        this.iv_five = (RoundImageView) super.findViewById(R.id.iv_five);
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.ll_all = (LinearLayout) super.findViewById(R.id.ll_all);
        this.iv_three = (RoundImageView) super.findViewById(R.id.iv_three);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_name = (LinearLayout) super.findViewById(R.id.ll_name);
        this.ll_clear = (LinearLayout) super.findViewById(R.id.ll_clear);
        this.tv_synopsis = (TextView) super.findViewById(R.id.tv_synopsis);
        this.tv_dissolve = (TextView) super.findViewById(R.id.tv_dissolve);
        this.ll_synopsis = (LinearLayout) super.findViewById(R.id.ll_synopsis);
        this.ll_share_code = (LinearLayout) super.findViewById(R.id.ll_share_code);
        this.ll_group_share = (LinearLayout) super.findViewById(R.id.ll_group_share);
        this.ll_invite_friends = (LinearLayout) super.findViewById(R.id.ll_invite_friends);

        Groupid = getIntent().getStringExtra("Groupid");
        Groupmin = getIntent().getStringExtra("Groupmin");
        Groupmax = getIntent().getStringExtra("Groupmax");
        Groupname = getIntent().getStringExtra("Groupname");
        Groupisopen = getIntent().getStringExtra("Groupisopen");
        Groupisowner = getIntent().getStringExtra("Groupisowner");
        Groupdescription = getIntent().getStringExtra("Groupdescription");

        this.tv_num.setText(Groupmin);
        this.tv_max.setText(Groupmax);
        this.tv_name.setText(Groupname);
        this.tv_title.setText(Groupname);
        this.tv_synopsis.setText(Groupdescription);

        this.swb.setChecked(Groupisopen.equals("0") ? false : true);

        this.swb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                final boolean is = isChecked;
                Map<String, String> map = new HashMap<>();
                map.put("OPT", "228");
                map.put("groupId", Groupid);
                map.put("is_open", isChecked ? "1" : "0");
                new HttpClientGet(GanapatiDataActivity.this, null, map, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jObject = new JSONObject(result);
                                    String error = jObject.getString("error");
                                    if (error.equals("1")) {
                                        EaseChatFragment.Groupisopen = swb
                                                .isChecked() ? "1" : "0";
                                        Toast.makeText(
                                                GanapatiDataActivity.this,
                                                swb.isChecked() ? "群为不公开状态..."
                                                        : "群为公开状态...", Toast.LENGTH_SHORT)
                                                .show();
                                        swb.setChecked(is);
                                    } else {
                                        Toast.makeText(
                                                GanapatiDataActivity.this,
                                                "请检查您的网络状态...", Toast.LENGTH_SHORT).show();
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
        });

        this.ll_all.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_name.setOnClickListener(this);
        this.ll_clear.setOnClickListener(this);
        this.ll_synopsis.setOnClickListener(this);
        this.tv_dissolve.setOnClickListener(this);
        this.ll_share_code.setOnClickListener(this);
        this.ll_group_share.setOnClickListener(this);
        this.ll_invite_friends.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "223");
        map.put("name", PreferencesUtils.getString(GanapatiDataActivity.this,
                "user_account"));
        map.put("groupId", Groupid);
        new HttpClientGet(GanapatiDataActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("群成员:" + result);

                        GroupMemberBean gBean = new Gson().fromJson(result,
                                GroupMemberBean.class);
                        groupMember = gBean.getGroupMember();

                        tv_num.setText(null == groupMember ? "0" : String
                                .valueOf(groupMember.size()));

                        for (int i = 0; i < groupMember.size(); i++) {
                            switch (i) {
                                case 0:
                                    Picasso.with(GanapatiDataActivity.this)
                                            .load(HttpConfig.IMAGEHOST
                                                    + groupMember.get(i).getPhoto())
                                            .into(iv_one);
                                    break;

                                case 1:
                                    Picasso.with(GanapatiDataActivity.this)
                                            .load(HttpConfig.IMAGEHOST
                                                    + groupMember.get(i).getPhoto())
                                            .into(iv_two);
                                    break;

                                case 2:
                                    Picasso.with(GanapatiDataActivity.this)
                                            .load(HttpConfig.IMAGEHOST
                                                    + groupMember.get(i).getPhoto())
                                            .into(iv_three);
                                    break;

                                case 3:
                                    Picasso.with(GanapatiDataActivity.this)
                                            .load(HttpConfig.IMAGEHOST
                                                    + groupMember.get(i).getPhoto())
                                            .into(iv_four);
                                    break;

                                case 4:
                                    Picasso.with(GanapatiDataActivity.this)
                                            .load(HttpConfig.IMAGEHOST
                                                    + groupMember.get(i).getPhoto())
                                            .into(iv_five);
                                    break;

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:

                finish();

                break;

            case R.id.ll_name:

                Map<String, String> map = new HashMap<>();
                map.put("OPT", "240");
                map.put("groupId", Groupid);
                new HttpClientGet(GanapatiDataActivity.this, null, map, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                System.out.println("群头像: " + result);
                                try {
                                    JSONObject jObject = new JSONObject(result);
                                    String error = jObject.getString("error");

                                    if ("1".equals(error)) {
                                        String groupsPhoto = jObject
                                                .getString("groupsPhoto");
                                        startActivityForResult(
                                                new Intent(
                                                        GanapatiDataActivity.this,
                                                        ActivityChoosePicture.class)
                                                        .putExtra("activity", "gan")
                                                        .putExtra("groupId",
                                                                Groupid)
                                                        .putExtra("description",
                                                                Groupdescription)
                                                        .putExtra("groupname",
                                                                Groupname)
                                                        .putExtra("maxusers",
                                                                Groupmax)
                                                        .putExtra(
                                                                "groupsPhoto",
                                                                null == groupsPhoto ? ""
                                                                        : groupsPhoto),
                                                10);
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

                break;

            case R.id.ll_all:

                startActivityForResult(new Intent(GanapatiDataActivity.this,
                                AllGroupMembersActivity.class).putExtra("activity", "GAN"),
                        1);

                break;

            case R.id.ll_share_code:

                startActivity(new Intent(GanapatiDataActivity.this,
                        QrCodeActivity.class).putExtra("id", Groupid).putExtra(
                        "name", Groupname));

                break;

            case R.id.ll_invite_friends:

                startActivityForResult(
                        new Intent(GanapatiDataActivity.this,
                                InviteFriendsActivity.class)
                                .putExtra("Groupid", Groupid)
                                .putExtra("Groupmin", Groupmin)
                                .putExtra("Groupmax", Groupmax), 2);

                break;

            case R.id.ll_synopsis:

                startActivityForResult(
                        new Intent(GanapatiDataActivity.this,
                                GroupToIntroduceActivity.class)
                                .putExtra("activity", "GAN")
                                .putExtra("Groupid", Groupid)
                                .putExtra("Groupmax", Groupmax)
                                .putExtra("Groupname", Groupname)
                                .putExtra("Groupisowner", Groupisowner)
                                .putExtra("content", null == tv_synopsis.getText().toString()
                                        ? "" : tv_synopsis.getText().toString()), 3);

                break;

            case R.id.ll_clear:

                EMConversation conversation = EMClient
                        .getInstance()
                        .chatManager()
                        .getConversation(
                                EMClient.getInstance().groupManager()
                                        .getGroup(Groupid).getGroupId(),
                                EMConversationType.GroupChat);
                if (conversation != null) {
                    conversation.clearAllMessages();
                }

                Toast.makeText(GanapatiDataActivity.this, "清空消息成功...", Toast.LENGTH_SHORT).show();

                break;

            case R.id.tv_dissolve:

                Map<String, String> map1 = new HashMap<>();
                map1.put("OPT", "229");
                map1.put("groupId", Groupid);
                new HttpClientGet(GanapatiDataActivity.this, null, map1, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                setResult(4);
                                New_Topic_Old_Style.CreateData();
                                New_Topic_Old_Style.JoinData();
                                New_Topic_Old_Style.FriendsData();
                                finish();
                            }
                        });

                break;

            case R.id.ll_group_share:
                startActivity(new Intent(GanapatiDataActivity.this,
                        GroupShareActivity.class).putExtra("activity", "gan")
                        .putExtra("Groupid", Groupid));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                if (null != data) {
                    tv_name.setText(data.getStringExtra("name"));
                }
                break;

            case 2:
                getData();
                break;
        }

        switch (resultCode) {
            case 3:
                if (null != data) {
                    this.tv_synopsis.setText(data.getStringExtra("content"));
                }
                break;

            case 1:
                getData();
                break;
        }

    }
}
