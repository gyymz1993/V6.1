package com.lsjr.zizisteward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

public class RealNameConfirmActivity extends BaseActivity implements OnClickListener {
    private TextView tv_login;
    private EditText et_real_name, et_identity;
    private String name;
    private String identity;
    private Intent mIntent;
    private RelativeLayout mRe_yishiming;
    private TextView mTv_real_name;
    private TextView mTv_real_no;
    private RelativeLayout mRe_shiming;

    @Override
    public int getContainerView() {
        return R.layout.activity_real_name_confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("实名认证");
        mRe_shiming = (RelativeLayout) findViewById(R.id.re_shiming);// 未实名认证布局
        tv_login = (TextView) findViewById(R.id.tv_login);// 完成
        et_real_name = (EditText) findViewById(R.id.et_real_name);// 姓名
        et_identity = (EditText) findViewById(R.id.et_identity);// 身份证号

        mRe_yishiming = (RelativeLayout) findViewById(R.id.re_yishiming);// 已实名认证布局
        mTv_real_name = (TextView) findViewById(R.id.tv_real_name);// 实名姓名
        mTv_real_no = (TextView) findViewById(R.id.tv_real_no);// 实名身份证号
        initLayout();
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    // 实名认证状态
    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("状态" + result);
                StateBean bean = GsonUtil.getInstance().fromJson(result, StateBean.class);
                if ("1".equals(bean.getIsIdNumberVerified())) {// 已实名
                    mRe_shiming.setVisibility(View.GONE);
                    mRe_yishiming.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(App.getUserInfo().getReality_name())) {
                        mTv_real_name.setText(App.getUserInfo().getReality_name());
                    } else {
                        mTv_real_name.setText(App.getInstance().getRealName());
                    }
                    if (!TextUtils.isEmpty(App.getUserInfo().getIdno())) {
                        mTv_real_no.setText(App.getUserInfo().getIdno().substring(0, 6) + "********"
                                + App.getUserInfo().getIdno().substring(14, 18));
                    } else {
                        mTv_real_no.setText(App.getInstance().getIdNo().substring(0, 6) + "********"
                                + App.getInstance().getIdNo().substring(14, 18));
                    }

                } else {
                    mRe_shiming.setVisibility(View.VISIBLE);
                    mRe_yishiming.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initLayout() {
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                name = et_real_name.getText().toString().trim();
                identity = et_identity.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(identity)) {
                    Toast.makeText(this, "请输入您的身份证号", Toast.LENGTH_SHORT).show();
                    return;

                }
                CustomDialogUtils.startCustomProgressDialog(RealNameConfirmActivity.this, "正在实名认证!");
                Map<String, String> map = new HashMap<String, String>();
                map.put("OPT", "7");
                map.put("realName", name);
                map.put("idNo", identity);
                map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                new HttpClientGet(RealNameConfirmActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    private RealNameConfirmBean bean;

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("实名认证结果" + result);
                        bean = GsonUtil.getInstance().fromJson(result, RealNameConfirmBean.class);
                        Toast.makeText(RealNameConfirmActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        CustomDialogUtils.stopCustomProgressDialog(RealNameConfirmActivity.this);
                        App.getUserInfo().setReality_name(name);
                        App.getUserInfo().setIdno(identity);
                        App.getInstance().setRealName(name);
                        App.getInstance().setIdNo(identity);
                        PreferencesUtils.putObject(RealNameConfirmActivity.this, "userinfo", App.getUserInfo());
                        Intent intent = getIntent();
                        RealNameConfirmActivity.this.setResult(419, intent);
                        RealNameConfirmActivity.this.finish();
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                        CustomDialogUtils.stopCustomProgressDialog(RealNameConfirmActivity.this);
                    }

                });

                break;
        }
    }

    public class RealNameConfirmBean {
        private String error;
        private String gmid;
        private String gname;
        private String gphoto;
        private String guser_name;
        private String msg;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getGmid() {
            return gmid;
        }

        public void setGmid(String gmid) {
            this.gmid = gmid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGphoto() {
            return gphoto;
        }

        public void setGphoto(String gphoto) {
            this.gphoto = gphoto;
        }

        public String getGuser_name() {
            return guser_name;
        }

        public void setGuser_name(String guser_name) {
            this.guser_name = guser_name;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public class StateBean {
        private String error;
        private String isIdNumberVerified;/*实名认证状态*/
        private String isPassCertificate;/*身份认证状态*/
        private String msg;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getIsIdNumberVerified() {
            return isIdNumberVerified;
        }

        public void setIsIdNumberVerified(String isIdNumberVerified) {
            this.isIdNumberVerified = isIdNumberVerified;
        }

        public String getIsPassCertificate() {
            return isPassCertificate;
        }

        public void setIsPassCertificate(String isPassCertificate) {
            this.isPassCertificate = isPassCertificate;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }
}
