package com.lsjr.zizisteward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.RegisterInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingLoginPsdActivity extends BaseActivity implements OnClickListener {

    private TextView tv_next;
    private EditText et_login_psd, et_confirm_psd, et_nick;
    private String mInvitCode;

    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    @Override
    public int getContainerView() {
        return R.layout.activity_set_login_pass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("设置登录密码");
        mInvitCode = getIntent().getStringExtra("invitCode");// 邀请码
        tv_next = (TextView) findViewById(R.id.tv_next);// 完成
        et_login_psd = (EditText) findViewById(R.id.et_login_psd);// 登录密码
        et_confirm_psd = (EditText) findViewById(R.id.et_confirm_psd);// 确认密码
        et_nick = (EditText) findViewById(R.id.et_nick);// 昵称
        initLayout();
    }

    private void initLayout() {
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:// 完成
                final String login_psd = et_login_psd.getText().toString().trim();
                String confrim_psd = et_confirm_psd.getText().toString().trim();
                String userCellPhone = App.getInstance().getUserCellPhone();
                if (TextUtils.isEmpty(et_nick.getText().toString())) {
                    ToastUtils.show(getApplicationContext(), "请设置昵称");
                    return;
                }

                if (TextUtils.isEmpty(login_psd)) {
                    Toast.makeText(this, "请设置登录密码", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(confrim_psd)) {
                    Toast.makeText(this, "请再次输入登录密码", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (isEmoji(login_psd)) {
                    ToastUtils.show(getApplicationContext(), "密码不能为特殊字符哦...");
                    return;
                }

                CustomDialogUtils.startCustomProgressDialog(SettingLoginPsdActivity.this, "正在设置!");
                Map<String, String> map = new HashMap<String, String>();
                map.put("OPT", "100");
                map.put("cellPhone", userCellPhone);
                if (TextUtils.isEmpty(mInvitCode)) {
                    map.put("invitCode", "");
                } else {
                    map.put("invitCode", mInvitCode);
                }
                map.put("password", login_psd);
                map.put("verifyPassword", confrim_psd);
                map.put("nickname", et_nick.getText().toString());
                new HttpClientGet(SettingLoginPsdActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("注册" + result);
                        RegisterInfo bean = GsonUtil.getInstance().fromJson(result, RegisterInfo.class);
                        Toast.makeText(SettingLoginPsdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        CustomDialogUtils.stopCustomProgressDialog(SettingLoginPsdActivity.this);
                       // App.setRegisterInfo(bean);
                        App.getInstance().setPassWord(login_psd);
                        Intent intent = new Intent(getApplicationContext(), NewInterestClassicActivity.class);
                        intent.putExtra("type", "set_password");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                        CustomDialogUtils.stopCustomProgressDialog(SettingLoginPsdActivity.this);
                    }
                });

        }
    }
}
