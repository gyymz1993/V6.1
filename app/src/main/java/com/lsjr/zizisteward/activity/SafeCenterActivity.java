package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

import android.os.Bundle;

public class SafeCenterActivity extends BaseActivity {

	@Override
	public int getContainerView() {
		// TODO Auto-generated method stub
		return R.layout.activtiy_safe_center;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setmTitle("安全设置");
	}
}
