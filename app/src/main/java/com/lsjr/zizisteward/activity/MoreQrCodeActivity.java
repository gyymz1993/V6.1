package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MoreQrCodeActivity extends Activity implements OnClickListener {
	private TextView tv_replace;
	private TextView tv_share;
	private TextView tv_save;
	private TextView tv_scanning;
	private RelativeLayout rl_parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_qr_code_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.tv_save = (TextView) super.findViewById(R.id.tv_save);
		this.tv_share = (TextView) super.findViewById(R.id.tv_share);
		this.tv_replace = (TextView) super.findViewById(R.id.tv_replace);
		this.tv_scanning = (TextView) super.findViewById(R.id.tv_scanning);
		this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);

		this.tv_save.setOnClickListener(this);
		this.tv_share.setOnClickListener(this);
		this.rl_parent.setOnClickListener(this);
		this.tv_replace.setOnClickListener(this);
		this.tv_scanning.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_parent:
			finish();
			break;

		case R.id.tv_replace:
			setResult(1);
			//Toast.makeText(MoreQrCodeActivity.this, "功能正在开发中...", 0).show();
			finish();
			break;

		case R.id.tv_share:
			setResult(2);
			finish();
			break;

		case R.id.tv_save:
			setResult(3);
			finish();
			break;

		case R.id.tv_scanning:
			setResult(4);
			finish();
			break;
		}
	}
}
