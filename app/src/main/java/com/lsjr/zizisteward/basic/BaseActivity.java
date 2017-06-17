package com.lsjr.zizisteward.basic;

import com.lsjr.zizisteward.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public abstract class BaseActivity extends SuperBaseActivity {
	protected FrameLayout fl_container;
	protected RelativeLayout iv_back, re_right;
	protected TextView tv_title;
	protected TextView tv_right;
	protected TextView cancel_address;
	protected RelativeLayout mRe_base;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);
		View container = getLayoutInflater().inflate(getContainerView(), null);
		initView();
		this.fl_container.addView(container);
	}

	private void initView() {
		fl_container = (FrameLayout) findViewById(R.id.fl_container);
		iv_back = (RelativeLayout) findViewById(R.id.iv_back);
		re_right = (RelativeLayout) findViewById(R.id.re_right);
		tv_title = (TextView) findViewById(R.id.tv_title);
		cancel_address = (TextView) findViewById(R.id.cancel_address);
		mRe_base = (RelativeLayout) findViewById(R.id.re_base);
	}

	public TextView getTv_right() {
		return tv_right;
	}

	public void setSearchVisiable(boolean visiable) {
		tv_title.setVisibility(visiable ? View.GONE : View.VISIBLE);
	}

	public void setLayoutRaceVisiable(boolean visiable) {
		tv_title.setVisibility(visiable ? View.GONE : View.VISIBLE);
	}

	public void setBackVisible(boolean visible) {
		iv_back.setVisibility(visible ? View.VISIBLE : View.GONE);
		re_right.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	public void setmTitle(String title) {
		tv_title.setText(title);

	}

	public void setmTitle(int title) {
		tv_title.setText(title);

	}

	public void setmRight(int title) {
		cancel_address.setText(title);

	}

	public void setmRight(String title) {
		cancel_address.setText(title);
	}

	public void onBack(View view) {
		this.finish();
	}

	public void onImageClick(View view) {

	}

	public void onImage1Click(View view) {

	}

	public void onTextClick(View view) {

	}

	public abstract int getContainerView();

	public void onTvLeftClick(View view) {

	}

	public void onTvRightClick(View view) {

	}

	public void showToast(int resId) {
		showToast(this.getResources().getText(resId).toString());
	}

	public void showToast(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, "" + text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void showToastMy(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast t = Toast.makeText(BaseActivity.this, "" + text, Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				t.show();
			}
		});
	}

	protected void backToFragment() {
		getFragmentManager().popBackStack();
	}

	protected void redirectToPage(Class name) {
		Intent intent = new Intent(this, name);
		startActivity(intent);
	}

	protected void redirectToPageFinish(Class name) {
		Intent intent = new Intent(this, name);
		startActivity(intent);
		this.finish();
	}

	public void defaultFinish() {
		super.finish();
	}

	public void startDefaultActivity(Intent intent) {
		super.startActivity(intent);
	}

	public void startDefaultActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		this.finish();
	}

	protected void startActivity(String action) {
		startActivity(action, null);
	}

	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

}
