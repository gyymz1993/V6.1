package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private TextView tv_login;// 下一步
	private EditText et_cell_phone, et_yan, et_yao;
	private TextView get_code;// 获取验证码
	public static final Pattern IS_PHONE = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$");
	private String cell_phone;
	private String verification_code;
	private String invite_code;
	private TimeCount time = new TimeCount(60000, 1000);
	private RelativeLayout mXieyi;
	private Intent mIntent;

	@Override
	public int getContainerView() {
		return R.layout.activity_register;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("手机号验证");
		tv_login = (TextView) findViewById(R.id.tv_login);
		et_cell_phone = (EditText) findViewById(R.id.et_cell_phone);
		get_code = (TextView) findViewById(R.id.get_code);
		et_yan = (EditText) findViewById(R.id.et_yan);
		et_yao = (EditText) findViewById(R.id.et_yao);
		mXieyi = (RelativeLayout) findViewById(R.id.RelativeLayout4);
		initLayout();
	}

	private void initLayout() {
		tv_login.setOnClickListener(this);
		get_code.setOnClickListener(this);
		mXieyi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login:// 下一步
			cell_phone = et_cell_phone.getText().toString().trim();
			verification_code = et_yan.getText().toString().trim();
			invite_code = et_yao.getText().toString().trim();
			if (TextUtils.isEmpty(cell_phone)) {
				Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
				return;

			}

			if (TextUtils.isEmpty(verification_code)) {
				Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;

			}

			CustomDialogUtils.startCustomProgressDialog(RegisterActivity.this, "正在验证!");
			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("OPT", "3");
			map2.put("cellPhone", cell_phone);
			if (TextUtils.isEmpty(invite_code)) {
				map2.put("invitCode", "");
			} else {
				map2.put("invitCode", invite_code);
			}
			map2.put("randomCode", verification_code);
			new HttpClientGet(RegisterActivity.this, null, map2, false, new HttpClientGet.CallBacks<String>() {

				private BasicParameterBean bean;

				@Override
				public void onSuccess(String result) {
					Log.i("test", "注册消息" + result);
					bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
					Toast.makeText(RegisterActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
					CustomDialogUtils.stopCustomProgressDialog(RegisterActivity.this);
					// 保存注册的手机号
					App.getInstance().setUserCellPhone(cell_phone);
					mIntent = new Intent(RegisterActivity.this, SettingLoginPsdActivity.class);
					mIntent.putExtra("invitCode", invite_code);// 邀请码
					startActivity(mIntent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}

				@Override
				public void onFailure(MyError myError) {
					super.onFailure(myError);
					CustomDialogUtils.stopCustomProgressDialog(RegisterActivity.this);
				}
			});

			break;
		case R.id.get_code:
			String cell_phone = et_cell_phone.getText().toString().trim();
			if (TextUtils.isEmpty(cell_phone)) {
				Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
				return;

			}
			if (!IS_PHONE.matcher(cell_phone).matches()) {
				Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
				return;

			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("OPT", "4");
			map.put("cellPhone", cell_phone);
			map.put("state", "0");
			new HttpClientGet(RegisterActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					time.start();
					Log.i("test", "短信验证码" + result);
					BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
					Toast.makeText(RegisterActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onFailure(MyError myError) {
					super.onFailure(myError);
				}
			});

			break;
		case R.id.RelativeLayout4:
			mIntent = new Intent(RegisterActivity.this, XieYiActivity.class);
			startActivity(mIntent);
			break;
		}
	}

	@SuppressWarnings("unused")
	private class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			get_code.setClickable(false);
			get_code.setText("重新获取" + millisUntilFinished / 1000 + "s");
		}

		@Override
		public void onFinish() {
			get_code.setText("获取验证码");
			get_code.setClickable(true);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (time != null) {
			time.cancel();
		}
	}

}
