package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SendEvalActivity extends Activity {
    private EditText mEt_content;
    private LinearLayout mLl_send;
    private String mId, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_eval);
        mId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        mEt_content = (EditText) findViewById(R.id.et_content);
        mLl_send = (LinearLayout) findViewById(R.id.ll_send);
        ((LinearLayout) findViewById(R.id.ll_back)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEt_content.setFocusable(true);
        mEt_content.setFocusableInTouchMode(true);
        mEt_content.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEt_content, 0);
            }
        }, 100);
        initView();
    }

    private void initView() {
        mLl_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEt_content.getText().toString().trim())) {
                    ToastUtils.show(getApplicationContext(), "评论内容不能为空哦...");
                    return;
                }
                CustomDialogUtils.startCustomProgressDialog(SendEvalActivity.this, "正在发送评论");
                HashMap<String, String> map = new HashMap<>();
                if (type.equals("1")) {
                    map.put("OPT", "336");
                    map.put("shop_id", mId);
                    map.put("comment", mEt_content.getText().toString().trim());
                } else {
                    map.put("OPT", "188");
                    map.put("share_id", mId);
                    map.put("content", mEt_content.getText().toString().trim());
                }
                map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                new HttpClientGet(SendEvalActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                        ToastUtils.show(getApplicationContext(), bean.getMsg());
                        Intent intent = getIntent();
                        SendEvalActivity.this.setResult(418, intent);
                        SendEvalActivity.this.finish();
                        CustomDialogUtils.stopCustomProgressDialog(SendEvalActivity.this);

                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                        CustomDialogUtils.stopCustomProgressDialog(SendEvalActivity.this);
                    }
                });

            }
        });
    }
}
