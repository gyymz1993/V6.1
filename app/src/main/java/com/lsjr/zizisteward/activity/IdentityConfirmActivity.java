package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class IdentityConfirmActivity extends BaseActivity {
	private TextView tv_real_name, tv_real_no, tv_content, tv_pass;

	@Override
	public int getContainerView() {
		return R.layout.activity_identity_confirm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("身份认证");
		String type = getIntent().getStringExtra("type");
		String label = PreferencesUtils.getString(IdentityConfirmActivity.this, "label");
		tv_real_name = (TextView) findViewById(R.id.tv_real_name);
		tv_real_no = (TextView) findViewById(R.id.tv_real_no);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_pass = (TextView) findViewById(R.id.tv_pass);

		if ("1".equals(type)) {
			tv_pass.setText("审核中");
			tv_content.setText("您的身份类型正在审核中");
			if (!TextUtils.isEmpty(App.getUserInfo().getReality_name())) {
				tv_real_name.setText(App.getUserInfo().getReality_name());
			} else {
				tv_real_name.setText(App.getInstance().getRealName());
			}
			if (!TextUtils.isEmpty(App.getUserInfo().getIdentity_type())) {
				tv_real_no.setText(App.getUserInfo().getIdentity_type());
			} else {
				tv_real_no.setText(label);
			}
		}

		if ("2".equals(type)) {
			tv_pass.setText("已通过");
			tv_content.setText("您已通过身份认证");
			if (!TextUtils.isEmpty(App.getUserInfo().getReality_name())) {
				tv_real_name.setText(App.getUserInfo().getReality_name());
			} else {
				tv_real_name.setText(App.getInstance().getRealName());
			}
			if (!TextUtils.isEmpty(App.getUserInfo().getIdentity_type())) {
				tv_real_no.setText(App.getUserInfo().getIdentity_type());
			} else {
				tv_real_no.setText(label);
			}

		}

	}

}
