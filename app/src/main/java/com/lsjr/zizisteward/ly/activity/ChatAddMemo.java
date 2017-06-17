package com.lsjr.zizisteward.ly.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.date.OptionsPickerView;
import com.lsjr.zizisteward.date.TimePickerView;
import com.lsjr.zizisteward.http.HttpClientGet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

/**
 * 聊天界面消息添加到备忘录
 */
public class ChatAddMemo extends Activity implements OnClickListener {

    private InputMethodManager imm;
    private String content;
    private LinearLayout ll_back;
    private LinearLayout ll_sure;
    private LinearLayout ll_time;
    private EditText et_title;
    private EditText et_content;
    private TextView tv_time;
    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.chat_add_memo);
        this.findViewById();
    }

    private void findViewById() {

        this.tv_time = (TextView) super.findViewById(R.id.tv_time);
        this.et_title = (EditText) super.findViewById(R.id.et_title);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);
        this.ll_time = (LinearLayout) super.findViewById(R.id.ll_time);
        this.et_content = (EditText) super.findViewById(R.id.et_content);

        this.content = getIntent().getStringExtra("content");
        this.et_content.setText(content);

        this.imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        this.ll_back.setOnClickListener(this);
        this.ll_sure.setOnClickListener(this);
        this.ll_time.setOnClickListener(this);

        // 时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);

        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv_time.setText(getTime(date));
            }
        });
    }

    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sure:

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(et_title.getWindowToken(), 0); // 强制隐藏键盘
                    imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0); // 强制隐藏键盘
                }

                String te = et_title.getText().toString();
                String ct = et_content.getText().toString();
                String time = tv_time.getText().toString();

                if (TextUtils.isEmpty(te)) {
                    Toast.makeText(ChatAddMemo.this, "标题不能为空...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ct)) {
                    Toast.makeText(ChatAddMemo.this, "内容不能为空...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(time)) {
                    Toast.makeText(ChatAddMemo.this, "请选择时间...", Toast.LENGTH_SHORT).show();
                } else {
                    SaveText(te, ct, time + " 00:00:00");
                }

                break;

            case R.id.ll_time:
                pvTime.show();
                break;

            default:
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(et_title.getWindowToken(), 0); // 强制隐藏键盘
                    imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0); // 强制隐藏键盘
                }
                finish();
                break;
        }
    }

    private void SaveText(String title, String content, String time) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "351");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("title", title);
        map.put("content", content);
        map.put("time", time);

        new HttpClientGet(ChatAddMemo.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");
                            String msg = jObject.getString("msg");

                            if (error.equals("1")) {
                                Toast.makeText(ChatAddMemo.this, "添加成功...", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChatAddMemo.this, "添加失败,请重试...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
