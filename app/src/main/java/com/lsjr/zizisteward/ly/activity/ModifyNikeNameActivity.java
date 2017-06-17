package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import com.hyphenate.chatuidemo.DemoHelper;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.EditNickNameActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ModifyNikeNameActivity extends Activity implements OnClickListener {
	
	private LinearLayout ll_back;
	private LinearLayout ll_sure;
	private EditText et;
	private String name;
	private ImageView iv_clear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modify_nike_name_activity);
		this.findViewById();
	}


	private void findViewById() {
		this.et = (EditText) super.findViewById(R.id.et);
		this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);
		
		this.ll_back.setOnClickListener(this);
		this.ll_sure.setOnClickListener(this);
		this.iv_clear.setOnClickListener(this);
		
		name = getIntent().getStringExtra("name");
		
		et.setText(name);
		
		this.et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					iv_clear.setVisibility(View.VISIBLE);
				} else {
					iv_clear.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_clear:
			this.et.setText("");
			break;

		case R.id.ll_back:
			finish();
			break;
			
		case R.id.ll_sure:
			final String name = et.getText().toString();
			
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(ModifyNikeNameActivity.this, "请检查输入的昵称...", Toast.LENGTH_SHORT).show();
				return;
			}
			
			CustomDialogUtils.startCustomProgressDialog(ModifyNikeNameActivity.this, "正在修改昵称...");
			Map<String, String> map = new HashMap<String, String>();
			map.put("OPT", "10");
			map.put("id", App.getUserInfo().getId());
			map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
			map.put("username", name);
			new HttpClientGet(ModifyNikeNameActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					
					App.getUserInfo().setUsername(name);
					PreferencesUtils.putObject(ModifyNikeNameActivity.this, "userinfo", App.getUserInfo());
					
					CustomDialogUtils.stopCustomProgressDialog(ModifyNikeNameActivity.this);
					Toast.makeText(ModifyNikeNameActivity.this, "更新昵称成功", Toast.LENGTH_SHORT)
							.show();
					Fragment_AddressBook.requestAddressBook();
					PersonalInformationActivity.tv_nickname.setText(name);
					finish();
					
//					new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							boolean updatenick = DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(name);
//							if (!updatenick) {
//								runOnUiThread(new Runnable() {
//									public void run() {
//										CustomDialogUtils.stopCustomProgressDialog(ModifyNikeNameActivity.this);
//										Toast.makeText(ModifyNikeNameActivity.this, getString(R.string.toast_updatenick_fail), Toast.LENGTH_SHORT)
//												.show();
//										Fragment_AddressBook.requestAddressBook();
//										Fragment_Circle_Friends.tv_name.setText(name);
//										PersonalInformationActivity.tv_nickname.setText(name);
//										finish();
//									}
//								});
//							} else {
//								runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										CustomDialogUtils.stopCustomProgressDialog(ModifyNikeNameActivity.this);
//										Toast.makeText(ModifyNikeNameActivity.this, getString(R.string.toast_updatenick_success), Toast.LENGTH_SHORT)
//												.show();
//										Fragment_AddressBook.requestAddressBook();
//										setResult(7);
//										finish();
//									}
//								});
//							}
//						}
//					}).start();
				}
				
				@Override
				public void onFailure(MyError myError) {
					super.onFailure(myError);
					CustomDialogUtils.stopCustomProgressDialog(ModifyNikeNameActivity.this);
				}
			});
			
			break;
		}
	}
}
