package com.lsjr.zizisteward.activity;

import java.util.HashMap;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.PaySuccessActivity.SuccessBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PersonCenterPaySuccessActivity extends BaseActivity implements OnClickListener {
	private Intent mIntent;
	private TextView finish;
	private String mGnum;
	private TextView mTv_name;
	private TextView mTv_phone;
	private TextView mTv_address;
	private TextView mTv_price;

	@Override
	public int getContainerView() {
		return R.layout.activity_pay_success_personal_center;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("支付成功");
		mGnum = getIntent().getStringExtra("gnum");
		finish = (TextView) findViewById(R.id.finish);
		mTv_name = (TextView) findViewById(R.id.tv_name);
		mTv_phone = (TextView) findViewById(R.id.tv_phone);
		mTv_address = (TextView) findViewById(R.id.tv_address);
		mTv_price = (TextView) findViewById(R.id.tv_price);

		initLayout();

	}

	private void initLayout() {
		finish.setOnClickListener(this);
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "40");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("gnum", mGnum);
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("支付成功详情" + result);
				SuccessBean bean = GsonUtil.getInstance().fromJson(result, SuccessBean.class);
				mTv_name.setText("姓名: " + bean.getContentData().getDelivery().get(0).getCname());
				mTv_phone.setText("电话: " + bean.getContentData().getDelivery().get(0).getCphone());
				mTv_address.setText("地址: " + bean.getContentData().getDelivery().get(0).getCaddr());
				mTv_price.setText("￥" + bean.getContentData().getOrder_price());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finish:
			finish();
			break;
		}
	}
}
