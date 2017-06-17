package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

import android.os.Bundle;

public class EyeBoundActivity extends BaseActivity {

	@Override
	public int getContainerView() {
		// TODO Auto-generated method stub
		return R.layout.activity_eye_bound;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setmTitle("视界");
		setmRight("发送");

	}
}
