package com.lsjr.zizisteward.ly.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.RealNameConfirmActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ForTimeBeing extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_real_name_confirm);
		TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		TextView tv_confirm = (TextView) findViewById(R.id.tv_confirm);
		
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent mIntent = new Intent(ForTimeBeing.this, RealNameConfirmActivity.class);
				startActivity(mIntent);
			}
		});
	}
}
