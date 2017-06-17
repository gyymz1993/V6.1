package com.lsjr.zizisteward.basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SuperBaseActivity extends Activity {
	protected boolean slideable = true;

	protected String getClassName() {
		return SuperBaseActivity.class.getName();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().activities.add(this);
	}

	protected void toast(String msg) {
		Toast.makeText(SuperBaseActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		App.getInstance().activities.remove(this);
	}

	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
