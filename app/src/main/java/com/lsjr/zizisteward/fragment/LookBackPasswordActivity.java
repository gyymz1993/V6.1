package com.lsjr.zizisteward.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SettingPasActivity;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LookBackPasswordActivity extends BaseActivity implements OnClickListener {

	private TextView tv_next, tv_get_code;
	private EditText et_phone, et_input_code;
	private Intent intent;
	private BasicParameterBean bean;
	private TimeCount time = new TimeCount(60000, 1000);
	public static final Pattern IS_PHONE = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$");
	private Map<String, String> map;

	@Override
	public int getContainerView() {
		return R.layout.activity_lookback_password;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("找回密码");
		tv_next = (TextView) findViewById(R.id.tv_next);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_input_code = (EditText) findViewById(R.id.et_input_code);
		tv_get_code = (TextView) findViewById(R.id.tv_get_code);
		initLayout();
	}

	private void initLayout() {
		tv_next.setOnClickListener(this);
		tv_get_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_next:
			final String phone = et_phone.getText().toString().trim();
			String code = et_input_code.getText().toString().trim();
			if (TextUtils.isEmpty(phone)) {
				Toast.makeText(LookBackPasswordActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(code)) {
				Toast.makeText(LookBackPasswordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			map = new HashMap<String, String>();
			map.put("OPT", "5");
			map.put("cellPhone", phone);
			map.put("randomCode", code);
			new HttpClientGet(LookBackPasswordActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					System.out.println("找回密码" + result);
					bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
					Toast.makeText(LookBackPasswordActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
					intent = new Intent(LookBackPasswordActivity.this, SettingPasActivity.class);
					intent.putExtra("phone", phone);
					startActivity(intent);
				}

				@Override
				public void onFailure(MyError myError) {
					super.onFailure(myError);
				}
			});

			break;
		case R.id.tv_get_code:
			String phone2 = et_phone.getText().toString().trim();
			if (TextUtils.isEmpty(phone2)) {
				Toast.makeText(LookBackPasswordActivity.this, "输手机号啊", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!IS_PHONE.matcher(phone2).matches()) {
				Toast.makeText(this, "请输入正确的手机号码哦", Toast.LENGTH_LONG).show();
				return;

			}
			map = new HashMap<String, String>();
			map.put("OPT", "4");
			map.put("cellPhone", phone2);
			map.put("state", "1");
			new HttpClientGet(LookBackPasswordActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					time.start();
					System.out.println("这是什么啊" + result);
					bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
					Toast.makeText(LookBackPasswordActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(MyError myError) {
					super.onFailure(myError);
				}
			});

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
			tv_get_code.setClickable(false);
			tv_get_code.setText("重新获取" + millisUntilFinished / 1000 + "s");
		}

		@Override
		public void onFinish() {
			tv_get_code.setText("获取验证码");
			tv_get_code.setClickable(true);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (time != null) {
			time.cancel();
		}
	}
}
