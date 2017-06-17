package com.lsjr.zizisteward.common.activtiy;

import java.util.Timer;
import java.util.TimerTask;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SixthNewActivity;
import com.lsjr.zizisteward.activity.ZiXunActivity;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.utils.ToastUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ZiShangSearchActivity extends BaseActivity implements OnClickListener {
	EditText ed_input;
	ImageView iv_search;
	TextView text;
	RelativeLayout iv_back;

	@Override
	public int getContainerView() {
		return R.layout.activity_world_search;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("孜赏搜索");
		ed_input = (EditText) findViewById(R.id.ed_input);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		text = (TextView) findViewById(R.id.text);
		iv_back = (RelativeLayout) findViewById(R.id.iv_back);
		text.setText("您可以搜索人的昵称、 孜赏标签");
		initListener();
	}

	@Override
	protected void onResume() {
		ed_input.setFocusable(true);
		ed_input.setFocusableInTouchMode(true);
		ed_input.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_input, 0);
			}
		}, 100);
		super.onResume();
	}

	private void initListener() {
		iv_search.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		ed_input.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					search();
					return true;
				}
				return false;
			}
		});
	}

	protected void search() {
		if (TextUtils.isEmpty(ed_input.getText().toString().trim())) {
			ToastUtils.show(getApplicationContext(), "请输入搜索关键字");
			return;
		}
		Intent intent = new Intent(getApplicationContext(), ZiShangSearchNickAndLabelActivity.class);
		intent.putExtra("name", ed_input.getText().toString().trim());
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			if (TextUtils.isEmpty(ed_input.getText().toString().trim())) {
				ToastUtils.show(getApplicationContext(), "请输入搜索关键字");
				return;
			}
			Intent intent = new Intent(getApplicationContext(), ZiShangSearchNickAndLabelActivity.class);
			intent.putExtra("name", ed_input.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.iv_back:
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
