package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingPasActivity extends BaseActivity implements OnClickListener {

	private EditText new_pass, confirm_pass;
	private TextView tv_next;
	private String phone;

	public boolean isEmoji(String string) {
		Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
				Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		return m.find();
	}

	@Override
	public int getContainerView() {
		return R.layout.activity_setting_psd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("设置密码");
		new_pass = (EditText) findViewById(R.id.new_pass);
		confirm_pass = (EditText) findViewById(R.id.confirm_pass);
		tv_next = (TextView) findViewById(R.id.tv_next);
		phone = getIntent().getStringExtra("phone");
		tv_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_next:
			String pass = new_pass.getText().toString().trim();
			String pass2 = confirm_pass.getText().toString().trim();
			if (TextUtils.isEmpty(pass)) {
				Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
				return;

			}
			if (TextUtils.isEmpty(pass2)) {
				Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
				return;

			}

			if (isEmoji(pass)) {
				ToastUtils.show(getApplicationContext(), "密码不能为特殊字符哦...");
				return;
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("OPT", "6");
			map.put("cellPhone", phone);
			map.put("newpwd", pass);
			map.put("confirmPassword", pass2);
			new HttpClientGet(SettingPasActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
					Toast.makeText(SettingPasActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SettingPasActivity.this, NoteLoginActivity.class);
					intent.putExtra("personal", "setting");
					startActivity(intent);
					finish();
				}

				@Override
				public void onFailure(MyError myError) {
					// TODO Auto-generated method stub
					super.onFailure(myError);
				}
			});

			break;

		}
	}
}
