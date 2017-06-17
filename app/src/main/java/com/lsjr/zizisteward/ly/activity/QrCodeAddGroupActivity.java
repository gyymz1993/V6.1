package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupMemberBean;
import com.lsjr.zizisteward.bean.GroupMemberBean.GroupMember;
import com.lsjr.zizisteward.bean.GroupsDataBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QrCodeAddGroupActivity extends Activity implements OnClickListener {

    private RoundImageView riv_head;
    private LinearLayout ll_back;
    private TextView tv_title;
    /**
     * 群资料界面只显示最多五个群成员
     */
    private ImageView iv_one, iv_two, iv_three, iv_four, iv_five;
    private List<GroupMember> groupMember = new ArrayList<>();
    private String id;
    private TextView tv_min;
    private TextView tv_max;
    private TextView tv_join;
    private TextView tv_synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.qr_code_add_group_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.tv_min = (TextView) super.findViewById(R.id.tv_min);
        this.tv_max = (TextView) super.findViewById(R.id.tv_max);
        this.iv_one = (ImageView) super.findViewById(R.id.iv_one);
        this.iv_two = (ImageView) super.findViewById(R.id.iv_two);
        this.tv_join = (TextView) super.findViewById(R.id.tv_join);
        this.iv_four = (ImageView) super.findViewById(R.id.iv_four);
        this.iv_five = (ImageView) super.findViewById(R.id.iv_five);
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.iv_three = (ImageView) super.findViewById(R.id.iv_three);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.tv_synopsis = (TextView) super.findViewById(R.id.tv_synopsis);
        this.riv_head = (RoundImageView) super.findViewById(R.id.riv_head);

        this.ll_back.setOnClickListener(this);
        this.tv_join.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");

        getGroupData(id);
        getGroupMembers(id);
    }


    private void getGroupData(String _id) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "232");
        map.put("groupId", _id);
        new HttpClientGet(QrCodeAddGroupActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                GroupsDataBean gdb = new Gson().fromJson(result, GroupsDataBean.class);
                tv_title.setText(gdb.getGroupsData().getGroupName());
                Picasso.with(QrCodeAddGroupActivity.this).load(HttpConfig.IMAGEHOST + gdb.getGroupsData().getGroupImg()).into(riv_head);
                tv_max.setText(gdb.getGroupsData().getMaxusers());
                tv_synopsis.setText(gdb.getGroupsData().getDescription());
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void getGroupMembers(String _id) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "223");
        map.put("groupId", _id);
        new HttpClientGet(QrCodeAddGroupActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("获取群成员： " + result);
                GroupMemberBean gBean = new Gson().fromJson(result, GroupMemberBean.class);
                groupMember = gBean.getGroupMember();

                tv_min.setText(String.valueOf(groupMember.size()));

                for (int i = 0; i < groupMember.size(); i++) {
                    switch (i) {
                        case 0:
                            Picasso.with(QrCodeAddGroupActivity.this)
                                    .load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
                                    .into(iv_one);
                            break;

                        case 1:
                            Picasso.with(QrCodeAddGroupActivity.this)
                                    .load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
                                    .into(iv_two);
                            break;

                        case 2:
                            Picasso.with(QrCodeAddGroupActivity.this)
                                    .load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
                                    .into(iv_three);
                            break;

                        case 3:
                            Picasso.with(QrCodeAddGroupActivity.this)
                                    .load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
                                    .into(iv_four);
                            break;

                        case 4:
                            Picasso.with(QrCodeAddGroupActivity.this)
                                    .load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
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

            case R.id.tv_join:

                IfGroupMembers();

                break;
        }
    }

    private void IfGroupMembers() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "268");
        map.put("groupId", id);
        map.put("name", PreferencesUtils.getString(QrCodeAddGroupActivity.this, "user_account"));
        new HttpClientGet(QrCodeAddGroupActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    if (error.equals("1")) {
                        JoinGroup();
                    } else {
                        Toast.makeText(QrCodeAddGroupActivity.this, "你已经加入了该群", Toast.LENGTH_SHORT).show();
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

    private void JoinGroup() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "230");
        map.put("name", PreferencesUtils.getString(QrCodeAddGroupActivity.this, "user_account"));
        map.put("groupId", id);
        new HttpClientGet(QrCodeAddGroupActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Toast.makeText(QrCodeAddGroupActivity.this, "成功加入该群...", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }
}
