package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PayDetailActivity extends Activity implements OnClickListener {
	private ImageView iv_cancel;
	private TextView dismiss;
	private TextView tv_sprice;
	private RelativeLayout pay_way;
	private Button pay;
	private String total_price;
	private String intentNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_pay_detail);
		pay_way = (RelativeLayout) findViewById(R.id.RelativeLayout2);
		pay = (Button) findViewById(R.id.pay);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
		dismiss = (TextView) findViewById(R.id.tv_dismiss);
		tv_sprice = (TextView) findViewById(R.id.tv_sprice);

		Intent intent = getIntent();
		total_price = intent.getStringExtra("total_price");
		intentNo = intent.getStringExtra("intentNo");

		tv_sprice.setText(total_price + "元");

		initLayout();
	}

	private void initLayout() {
		iv_cancel.setOnClickListener(this);
		dismiss.setOnClickListener(this);
		pay.setOnClickListener(this);
		findViewById(R.id.tv_credit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(PayDetailActivity.this, R.style.dialog);
				dialog.setContentView(R.layout.dialog_kaifang);
				Window window = dialog.getWindow();
				window.setGravity(Gravity.CENTER | Gravity.CENTER);
				dialog.show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cancel:
			finish();
			break;
		case R.id.tv_dismiss:
			finish();
			break;
		case R.id.pay:// 立即付款
			Intent intent = new Intent(PayDetailActivity.this, JARActivity.class);
			intent.putExtra("total_price", total_price);
			intent.putExtra("indentNo", intentNo);
			startActivity(intent);
			finish();
		}
	}

}
