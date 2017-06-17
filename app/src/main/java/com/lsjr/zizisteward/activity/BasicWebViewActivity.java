package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

import android.os.Bundle;

public class BasicWebViewActivity extends BaseActivity {

	@Override
	public int getContainerView() {
		return R.layout.activity_base_webview;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("提示");

	}
}
