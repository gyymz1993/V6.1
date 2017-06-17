package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.WheelViewTimeActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditAddress extends Activity implements OnClickListener {
	public static final Pattern IS_PHONE = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$");
	private TextView tv_tilte, tv_area;
	private String name, phone, address, clocation, aid, cpostcode, is_common;

	private EditText et_phone, et_name, et_detail_address;
	private RelativeLayout sel_area;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		tv_tilte = (TextView) findViewById(R.id.tv_tilte);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_area = (TextView) findViewById(R.id.tv_area);
		et_detail_address = (EditText) findViewById(R.id.et_detail_address);
		sel_area = (RelativeLayout) findViewById(R.id.RelativeLayout6);
		RelativeLayout re_finish = (RelativeLayout) findViewById(R.id.re_finish);
		re_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv_tilte.setText("修改地址");
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		phone = intent.getStringExtra("phone");
		address = intent.getStringExtra("address");
		clocation = intent.getStringExtra("clocation");
		aid = intent.getStringExtra("aid");
		cpostcode = intent.getStringExtra("cpostcode");
		is_common = intent.getStringExtra("is_common");

		System.out.println("地址id" + aid);

		et_detail_address.setText(address);
		tv_area.setText(clocation);
		et_name.setText(name);
		et_phone.setText(phone);
		tv_area.setTextColor(Color.BLACK);
		tv_area.setTextSize(18);

		initView();

	}

	private void initView() {
		sel_area.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.RelativeLayout6:
			Intent intent = new Intent(EditAddress.this, WheelViewTimeActivity.class);
			startActivityForResult(intent, 99);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 99 && resultCode == 77) {
			String address = data.getStringExtra("address");
			String province = data.getStringExtra("province");
			String city = data.getStringExtra("city");
			String area = data.getStringExtra("area");
			String zipCode = data.getStringExtra("zipCode");
			tv_area.setText(address);
			tv_area.setTextColor(Color.BLACK);
			tv_area.setTextSize(18);

		}
	}

	public void save(View view) {
		String name = et_name.getText().toString().trim();
		String phone = et_phone.getText().toString().trim();
		String address = et_detail_address.getText().toString().trim();
		String area = tv_area.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "收货人不能为空", Toast.LENGTH_SHORT).show();
			return;

		}
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "请填写手机号", Toast.LENGTH_SHORT).show();
			return;

		}
		if (TextUtils.isEmpty(address)) {
			Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
			return;

		}
		if (TextUtils.isEmpty(area)) {
			Toast.makeText(this, "请选择所在地区", Toast.LENGTH_SHORT).show();
			return;

		}
		if (!IS_PHONE.matcher(phone).matches()) {
			Toast.makeText(EditAddress.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
			return;

		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "15");
		map.put("addr_id", aid);
		map.put("mid", App.getUserInfo().getId());
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("cname", name);
		map.put("caddr", address);
		map.put("clocation", area);
		map.put("cphone", phone);
		map.put("cpostcode", cpostcode);
		map.put("is_common", is_common);
		new HttpClientGet(EditAddress.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
				Toast.makeText(EditAddress.this, bean.getMsg(), Toast.LENGTH_SHORT).show();

				// 发送广播
				Intent intent = new Intent();
				intent.setAction("action.address");
				sendBroadcast(intent);

				finish();
			}

			@Override
			public void onFailure(MyError myError) {
				// TODO Auto-generated method stub
				super.onFailure(myError);
			}
		});

	}
}
