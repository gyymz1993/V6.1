package com.lsjr.zizisteward.quickcontacts;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加快捷联系人
 */
public class Activity_AddQuickContacts extends Activity implements OnClickListener {

    /**
     * 返回
     */
    private LinearLayout ll_back;
    /**
     * 保存
     */
    private LinearLayout ll_save;
    /**
     * 关系的父布局，点击弹出选择关系的布局
     */
    private RelativeLayout rl_relation;
    /**
     * 关系的标题
     */
    private TextView tv_relation;
    /**
     * 关系的内容
     */
    private TextView tv_content;
    /**
     * 显示是否显示关系布局的箭头
     */
    private ImageView iv_arrow;
    /**
     * 弹出关系的布局
     */
    private LinearLayout ll_all_relationships;
    /**
     * 家人
     */
    private LinearLayout ll_family;
    /**
     * 朋友
     */
    private LinearLayout ll_friend;
    /**
     * 同事
     */
    private LinearLayout ll_colleagues;
    /**
     * 其他
     */
    private LinearLayout ll_other;
    /**
     * 姓名
     */
    private EditText et_name;
    /**
     * 联系方式
     */
    private EditText et_number;
    private String user_id;
    private String relation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_add_quick_contacts);

        FindViewById();
    }

    private void FindViewById() {
        this.user_id = getIntent().getStringExtra("user_id");
        this.et_name = (EditText) super.findViewById(R.id.et_name);
        this.iv_arrow = (ImageView) super.findViewById(R.id.iv_arrow);
        this.et_number = (EditText) super.findViewById(R.id.et_number);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_save = (LinearLayout) super.findViewById(R.id.ll_save);
        this.tv_content = (TextView) super.findViewById(R.id.tv_content);
        this.ll_other = (LinearLayout) super.findViewById(R.id.ll_other);
        this.ll_family = (LinearLayout) super.findViewById(R.id.ll_family);
        this.ll_friend = (LinearLayout) super.findViewById(R.id.ll_friend);
        this.tv_relation = (TextView) super.findViewById(R.id.tv_relation);
        this.rl_relation = (RelativeLayout) super.findViewById(R.id.rl_relation);
        this.ll_colleagues = (LinearLayout) super.findViewById(R.id.ll_colleagues);
        this.ll_all_relationships = (LinearLayout) super.findViewById(R.id.ll_all_relationships);

        this.ll_back.setOnClickListener(this);
        this.ll_save.setOnClickListener(this);
        this.ll_other.setOnClickListener(this);
        this.ll_family.setOnClickListener(this);
        this.ll_friend.setOnClickListener(this);
        this.rl_relation.setOnClickListener(this);
        this.ll_colleagues.setOnClickListener(this);
        this.ll_all_relationships.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_back:

                finish();

                break;

            case R.id.ll_save:

                String content = this.tv_content.getText().toString();
                String name = this.et_name.getText().toString();
                String number = this.et_number.getText().toString();

                if (null == content || "".equals(content)) {
                    Toast.makeText(this, "还未设置联系人与您的关系...", Toast.LENGTH_SHORT).show();
                } else if (null == name || "".equals(name)) {
                    Toast.makeText(this, "还未设置联系人的姓名...", Toast.LENGTH_SHORT).show();
                } else if (null == number || "".equals(number)) {
                    Toast.makeText(this, "还未设置联系人的联系方式...", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "202");
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(user_id), "u"));
                    map.put("contact_name", name);
                    map.put("contact_mobile", number);
                    map.put("relation", relation);

                    new HttpClientGet(this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println("添加联系人:" + result);

                            try {
                                JSONObject jObject = new JSONObject(result);
                                if ("1".equals(jObject.getString("error"))) {
                                    Toast.makeText(Activity_AddQuickContacts.this, "添加联系人成功", Toast.LENGTH_SHORT).show();
                                    setResult(1);
                                    finish();
                                } else {
                                    Toast.makeText(Activity_AddQuickContacts.this, "服务器开小差了,请稍后重试...", Toast.LENGTH_SHORT).show();
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

                break;

            case R.id.rl_relation:

                if (ll_all_relationships.getVisibility() == View.VISIBLE) {

                    this.ll_all_relationships.setVisibility(View.GONE);

                    this.tv_relation.setTextColor(Color.parseColor("#222222"));

                    this.iv_arrow.setImageResource(R.drawable.icon_arrow_down);

                } else {

                    this.ll_all_relationships.setVisibility(View.VISIBLE);

                    this.tv_relation.setTextColor(Color.parseColor("#BCBCBC"));

                    this.iv_arrow.setImageResource(R.drawable.icon_arrow_up);
                }

                break;

            case R.id.ll_all_relationships:

                this.ll_all_relationships.setVisibility(View.GONE);

                this.tv_relation.setTextColor(Color.parseColor("#222222"));

                this.iv_arrow.setImageResource(R.drawable.icon_arrow_down);

                break;

            case R.id.ll_colleagues:

                this.ll_all_relationships.setVisibility(View.GONE);

                this.tv_relation.setTextColor(Color.parseColor("#222222"));

                this.tv_content.setText("同事");

                this.relation = "3";

                this.iv_arrow.setImageResource(R.drawable.icon_arrow_down);

                break;

            case R.id.ll_friend:

                this.ll_all_relationships.setVisibility(View.GONE);

                this.tv_relation.setTextColor(Color.parseColor("#222222"));

                this.tv_content.setText("朋友");

                this.relation = "2";

                this.iv_arrow.setImageResource(R.drawable.icon_arrow_down);

                break;

            case R.id.ll_family:

                this.ll_all_relationships.setVisibility(View.GONE);

                this.tv_relation.setTextColor(Color.parseColor("#222222"));

                this.tv_content.setText("家人");

                this.relation = "1";

                this.iv_arrow.setImageResource(R.drawable.icon_arrow_down);

                break;

            case R.id.ll_other:

                this.ll_all_relationships.setVisibility(View.GONE);

                this.tv_relation.setTextColor(Color.parseColor("#222222"));

                this.tv_content.setText("其他");

                this.relation = "0";

                this.iv_arrow.setImageResource(R.drawable.icon_arrow_down);

                break;

        }
    }
}
