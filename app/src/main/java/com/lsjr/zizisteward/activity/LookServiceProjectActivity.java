package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LookServiceProjectActivity extends BaseActivity {

	@Override
	public int getContainerView() {
		return R.layout.activity_look_service_project;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("提示");
		TextView tv_look = (TextView) findViewById(R.id.tv_look);
		tv_look.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ServiceProjectActivity.class));
			}
		});

	}
}
