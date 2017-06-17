package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.HashMap;
import java.util.regex.Pattern;

@SuppressLint("ShowToast")
public class SonAccountPsdSetActivity extends BaseActivity {
	private EditText et_name;
	private EditText et_number;
	private String son_type, son_use_start, son_name, son_mobile, limit;
	private RelativeLayout mRe_kaiqi;
	private ImageView mIv_kaiqi, iv_xiadan_off, iv_xiadan_on;
	private RelativeLayout re_quanxian;
	public static final Pattern IS_PHONE = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$");
	private RelativeLayout mRe_xiadan;

	@Override
	public int getContainerView() {
		return R.layout.activity_sonaccount_psd_set;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("子账号设置");
		et_name = (EditText) findViewById(R.id.et_name);
		et_number = (EditText) findViewById(R.id.et_number);
		mRe_kaiqi = (RelativeLayout) findViewById(R.id.re_kaiqi);
		mIv_kaiqi = (ImageView) findViewById(R.id.iv_kaiqi);
		iv_xiadan_off = (ImageView) findViewById(R.id.iv_xiadan_off);
		iv_xiadan_on = (ImageView) findViewById(R.id.iv_xiadan_on);
		re_quanxian = (RelativeLayout) findViewById(R.id.re_quanxian);
		mRe_xiadan = (RelativeLayout) findViewById(R.id.re_xiadan);
		son_type = getIntent().getStringExtra("son_type");
		son_use_start = getIntent().getStringExtra("son_use_start");
		son_name = getIntent().getStringExtra("son_name");
		son_mobile = getIntent().getStringExtra("son_mobile");
		limit = getIntent().getStringExtra("limit");
		init();

	}

	private void init() {
		if ("1".equals(son_use_start)) {// 已开启
			et_name.setText(son_name);
			et_number.setText(son_mobile);
			mIv_kaiqi.setImageResource(R.drawable.safe_center_on);
			re_quanxian.setVisibility(View.VISIBLE);

		}
		mRe_kaiqi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (re_quanxian.getVisibility() == View.VISIBLE) {// 权限显示 开启状态
					// 点击请求关闭接口

					HashMap<String, String> map = new HashMap<>();
					map.put("OPT", "214");
					map.put("id", App.getUserInfo().getId());
					map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
					map.put("son_type", son_type);
					new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							mIv_kaiqi.setImageResource(R.drawable.safe_center_off);
							BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
							Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
							re_quanxian.setVisibility(View.GONE);
							et_name.setText(null);
							et_number.setText(null);

						}

						@Override
						public void onFailure(MyError myError) {
							super.onFailure(myError);

						}
					});
				} else {// 权限消失 关闭状态 点击请求开启接口

					if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
						Toast.makeText(getApplicationContext(), "开启前请输入账号名", Toast.LENGTH_SHORT).show();
						return;
					}
					if (TextUtils.isEmpty(et_number.getText().toString().trim())) {
						Toast.makeText(getApplicationContext(), "开启前请绑定手机号", Toast.LENGTH_SHORT).show();
						return;
					}
					if (!IS_PHONE.matcher(et_number.getText().toString().trim()).matches()) {
						Toast.makeText(getApplicationContext(), "请绑定正确的手机号", Toast.LENGTH_SHORT).show();
						return;

					}
					HashMap<String, String> map = new HashMap<>();
					map.put("OPT", "215");
					map.put("id", App.getUserInfo().getId());
					map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
					map.put("son_type", son_type);
					map.put("son_name", et_name.getText().toString().trim());
					map.put("son_mobile", et_number.getText().toString().trim());
					new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println("什么啊啊  " + result);
							mIv_kaiqi.setImageResource(R.drawable.safe_center_on);
							BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
							Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
							re_quanxian.setVisibility(View.VISIBLE);

						}

						@Override
						public void onFailure(MyError myError) {
							super.onFailure(myError);

						}
					});
				}

			}
		});
		if ("1".equals(limit)) {
			iv_xiadan_on.setVisibility(View.VISIBLE);
			iv_xiadan_off.setVisibility(View.GONE);
		} else {
			iv_xiadan_on.setVisibility(View.GONE);
			iv_xiadan_off.setVisibility(View.VISIBLE);
		}
		mRe_xiadan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (iv_xiadan_on.getVisibility() == View.VISIBLE && iv_xiadan_off.getVisibility() == View.GONE) {// 开启状态
					// 关闭
					HashMap<String, String> map = new HashMap<>();
					map.put("OPT", "216");
					map.put("id", App.getUserInfo().getId());
					map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
					map.put("son_type", son_type);
					map.put("limits", "1");
					map.put("is_limits", "0");
					new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							iv_xiadan_on.setVisibility(View.GONE);
							iv_xiadan_off.setVisibility(View.VISIBLE);
							BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
							Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();

						}

						@Override
						public void onFailure(MyError myError) {
							super.onFailure(myError);

						}
					});
				} else if (iv_xiadan_on.getVisibility() == View.GONE && iv_xiadan_off.getVisibility() == View.VISIBLE) {// 关闭状态

					// 开启
					HashMap<String, String> map = new HashMap<>();
					map.put("OPT", "216");
					map.put("id", App.getUserInfo().getId());
					map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
					map.put("son_type", son_type);
					map.put("limits", "1");
					map.put("is_limits", "1");
					new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							iv_xiadan_on.setVisibility(View.VISIBLE);
							iv_xiadan_off.setVisibility(View.GONE);
							BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
							Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();

						}

						@Override
						public void onFailure(MyError myError) {
							super.onFailure(myError);

						}
					});

				}

			}
		});
	}

}
