package com.lsjr.zizisteward.common.activtiy;

import java.util.ArrayList;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ZiShangFragment;
import com.lsjr.zizisteward.adapter.MainPageAdapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class CareAndCollectActivity extends FragmentActivity
		implements OnPageChangeListener, OnCheckedChangeListener, OnClickListener {
	RadioGroup radioGroup;
	RadioButton radio0;
	RadioButton radio1;
	ViewPager all_page;
	List<Fragment> fragments = new ArrayList<Fragment>();
	MainPageAdapter adapter;
	RelativeLayout back;
	Drawable drawable;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_care_and_collect);
		back = (RelativeLayout) findViewById(R.id.back);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		all_page = (ViewPager) findViewById(R.id.all_page);
		fragments.add(new CareFragment());// 关注
		fragments.add(new CollectFragment());// 收藏
		adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
		all_page.setAdapter(adapter);
		Resources resources = this.getResources();
		drawable = resources.getDrawable(R.drawable.care_and_collect);
		drawable.setBounds(1, 1, 120, 8);
		radio0.setCompoundDrawables(null, null, null, drawable);
		radio1.setCompoundDrawables(null, null, null, null);
		all_page.setOffscreenPageLimit(fragments.size() - 1);
		all_page.addOnPageChangeListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		initListener();

	}

	private void initListener() {
		back.setOnClickListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		getTabState(position);
	}

	private void getTabState(int position) {
		radio0.setChecked(false);
		radio1.setChecked(false);
		switch (position) {
		case 0:
			radio0.setChecked(true);
			radio0.setCompoundDrawables(null, null, null, drawable);
			radio1.setCompoundDrawables(null, null, null, null);
			break;
		case 1:
			radio1.setChecked(true);
			radio0.setCompoundDrawables(null, null, null, null);
			radio1.setCompoundDrawables(null, null, null, drawable);
			break;

		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio0:
			all_page.setCurrentItem(0);
			break;
		case R.id.radio1:
			all_page.setCurrentItem(1);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
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
