package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.EmailBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditEmailActivity extends Activity {
	private EditText et_email;
	private TextView tv_finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_email);
		et_email = (EditText) findViewById(R.id.et_email);
		et_email.setText(App.getUserInfo().getEmail());
		tv_finish = (TextView) findViewById(R.id.tv_finish);
		tv_finish.setOnClickListener(new OnClickListener() {

			private String email;

			@Override
			public void onClick(View v) {
				email = et_email.getText().toString().trim();
				if (TextUtils.isEmpty(email)) {
					Toast.makeText(EditEmailActivity.this, "输邮箱啊", Toast.LENGTH_SHORT).show();
					return;
				}
				CustomDialogUtils.startCustomProgressDialog(EditEmailActivity.this, "正在修改!");
				Map<String, String> map = new HashMap<String, String>();
				map.put("OPT", "11");
				map.put("id", App.getUserInfo().getId());
				map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
				map.put("emailaddress", email);
				new HttpClientGet(EditEmailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						System.out.println("邮箱" + result);
						CustomDialogUtils.stopCustomProgressDialog(EditEmailActivity.this);
						EmailBean bean = GsonUtil.getInstance().fromJson(result, EmailBean.class);
						Toast.makeText(EditEmailActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
						App.getUserInfo().setEmail(email);
						PreferencesUtils.putObject(EditEmailActivity.this, "userinfo", App.getUserInfo());
						finish();
					}

					@Override
					public void onFailure(MyError myError) {
						super.onFailure(myError);
						CustomDialogUtils.stopCustomProgressDialog(EditEmailActivity.this);
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
