package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditLoginPsdActivity extends Activity {
	private EditText et_old_psd, et_new_psd, et_new_next_psd;
	private TextView finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_login_psd);
		et_old_psd = (EditText) findViewById(R.id.et_old_psd);
		et_new_psd = (EditText) findViewById(R.id.et_new_psd);
		et_new_next_psd = (EditText) findViewById(R.id.et_new_next_psd);
		finish = (TextView) findViewById(R.id.finish);
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password1 = et_old_psd.getText().toString().trim();
				String password2 = et_new_psd.getText().toString().trim();
				String password3 = et_new_next_psd.getText().toString().trim();
				if (TextUtils.isEmpty(password1)) {
					Toast.makeText(EditLoginPsdActivity.this, "请输入旧登录密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(password2)) {
					Toast.makeText(EditLoginPsdActivity.this, "请设置新登录密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(password3)) {
					Toast.makeText(EditLoginPsdActivity.this, "请再次输入新登录密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!password2.equals(password3)) {
					Toast.makeText(EditLoginPsdActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
					return;

				}
				CustomDialogUtils.startCustomProgressDialog(EditLoginPsdActivity.this, "请稍后");
				Map<String, String> map = new HashMap<String, String>();
				map.put("OPT", "12");
				map.put("id", App.getUserInfo().getId());
				map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
				map.put("oldloginpwd", password1);
				map.put("newloginpwd", password2);
				map.put("confirmPassword", password3);
				new HttpClientGet(EditLoginPsdActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						CustomDialogUtils.stopCustomProgressDialog(EditLoginPsdActivity.this);
						BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
						Toast.makeText(EditLoginPsdActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
						finish();
					}

					@Override
					public void onFailure(MyError myError) {
						// TODO Auto-generated method stub
						super.onFailure(myError);

						CustomDialogUtils.stopCustomProgressDialog(EditLoginPsdActivity.this);
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
