package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

import android.os.Bundle;

public class ClassicDetailActivity extends BaseActivity {

	@Override
	public int getContainerView() {
		// TODO Auto-generated method stub
		return R.layout.activity_classic_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setmTitle("服务详情");
	}
}
