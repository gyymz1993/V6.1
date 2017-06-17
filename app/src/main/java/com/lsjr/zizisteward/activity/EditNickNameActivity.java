package com.lsjr.zizisteward.activity;

import android.app.Activity;
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
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class EditNickNameActivity extends Activity {
	private EditText et_nickname;
	private TextView tv_finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_nickname);
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		tv_finish = (TextView) findViewById(R.id.tv_finish);
		et_nickname.setText(App.getUserInfo().getUsername());
		tv_finish.setOnClickListener(new OnClickListener() {

			private String name;

			@Override
			public void onClick(View v) {
				name = et_nickname.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(EditNickNameActivity.this, "用户名是空的啊", Toast.LENGTH_SHORT).show();
					return;
				}
				CustomDialogUtils.startCustomProgressDialog(EditNickNameActivity.this, "正在修改!");
				Map<String, String> map = new HashMap<String, String>();
				map.put("OPT", "10");
				map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
				map.put("username", name);
				new HttpClientGet(EditNickNameActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						CustomDialogUtils.stopCustomProgressDialog(EditNickNameActivity.this);
						BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
						App.getUserInfo().setUsername(name);
						PreferencesUtils.putObject(EditNickNameActivity.this, "userinfo", App.getUserInfo());
//						PersonalInfoActivity.nick.setText(name);
						ToastUtils.show(getApplicationContext(), bean.getMsg());
						finish();
					}

					@Override
					public void onFailure(MyError myError) {
						super.onFailure(myError);
						CustomDialogUtils.stopCustomProgressDialog(EditNickNameActivity.this);
					}

				});

			}
		});

		((RelativeLayout) findViewById(R.id.re_back)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
