package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewCallStewardActivity extends Activity implements OnClickListener {
    private RelativeLayout mRe_back;
    private CircleImageView mIv_userIcon;
    private EditText mEt_cellphone;
    private TextView mTv_name;
    private TextView mTv_nick;
    private TextView mTv_content;
    private RelativeLayout mRe_liuyan;
    private TextView mTv_xia;
    private TextView mTv_shang;
    private EditText mEt_liuyan;
    private TextView mTv_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_call_steward);
        mRe_back = (RelativeLayout) findViewById(R.id.re_back);
        mIv_userIcon = (CircleImageView) findViewById(R.id.iv_userIcon);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_nick = (TextView) findViewById(R.id.tv_nick);
        mTv_content = (TextView) findViewById(R.id.tv_content);
        mEt_cellphone = (EditText) findViewById(R.id.et_cellphone);
        mRe_liuyan = (RelativeLayout) findViewById(R.id.re_liuyan);
        mTv_xia = (TextView) findViewById(R.id.tv_xia);
        mTv_shang = (TextView) findViewById(R.id.tv_shang);
        mEt_liuyan = (EditText) findViewById(R.id.et_liuyan);
        mTv_call = (TextView) findViewById(R.id.tv_call);

        initLayout();

    }

    private void initLayout() {
        Glide.with(NewCallStewardActivity.this).load(HttpConfig.IMAGEHOST + App.getUserInfo().getGphoto())
                .into(mIv_userIcon);
        mTv_name.setText(App.getUserInfo().getGuser_name());
        if (TextUtils.isEmpty(App.getUserInfo().getReality_name())) {
            mTv_nick.setText(App.getUserInfo().getUsername() + "," + "您好!");
        } else {
            mTv_nick.setText(App.getUserInfo().getReality_name() + "," + "您好!");
        }

        mTv_content.setText(
                "很高兴为您服务,我是您的私人管家  " + App.getUserInfo().getGuser_name() + " ,按下呼叫管家,3分钟内我会拨打您的手机号码");

        mEt_cellphone.setText(App.getUserInfo().getName());

        mTv_xia.setVisibility(View.VISIBLE);
        mTv_shang.setVisibility(View.GONE);
        mEt_liuyan.setVisibility(View.GONE);

        mRe_back.setOnClickListener(this);
        mTv_call.setOnClickListener(this);
        mRe_liuyan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_back:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.tv_call:
                if (TextUtils.isEmpty(mEt_cellphone.getText().toString())) {
                    ToastUtils.show(NewCallStewardActivity.this, "请编辑您的手机号");
                    return;
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("OPT", "71");
                map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                map.put("stname", App.getUserInfo().getGname());
                map.put("dial_phone", mEt_cellphone.getText().toString());
                if (TextUtils.isEmpty(mEt_liuyan.getText().toString())) {
                    map.put("guestbook", "");
                } else {
                    map.put("guestbook", mEt_liuyan.getText().toString());
                }
                new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            ToastUtils.show(getApplicationContext(), "召唤成功,管家稍后会与您联系");
                            Intent intent = getIntent();
                            NewCallStewardActivity.this.setResult(222, intent);
                            NewCallStewardActivity.this.finish();
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
            case R.id.re_liuyan:
                if (mTv_xia.getVisibility() == View.VISIBLE && mTv_shang.getVisibility() == View.GONE) {
                    mEt_liuyan.setVisibility(View.VISIBLE);
                    mTv_shang.setVisibility(View.VISIBLE);
                    mTv_xia.setVisibility(View.GONE);
                } else if (mTv_xia.getVisibility() == View.GONE && mTv_shang.getVisibility() == View.VISIBLE) {
                    mEt_liuyan.setVisibility(View.GONE);
                    mTv_shang.setVisibility(View.GONE);
                    mTv_xia.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return super.onKeyDown(keyCode, event);

    }
}
