package com.lsjr.zizisteward.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.MainPageAdapter;
import com.lsjr.zizisteward.fragment.AllProjectFragment;
import com.lsjr.zizisteward.fragment.GoingFragment;
import com.lsjr.zizisteward.fragment.MyReserveFragment;
import com.lsjr.zizisteward.fragment.WaitConfirmFragment;
import com.lsjr.zizisteward.fragment.WaitEvalFragment;
import com.lsjr.zizisteward.fragment.WaitPayFragment;

import java.util.ArrayList;
import java.util.List;

public class ServiceProjectActivity extends FragmentActivity
		implements OnPageChangeListener, OnCheckedChangeListener, OnClickListener {
	private ViewPager pager;
	private RadioGroup group;
	private RadioButton button0, button1, button2, button3, button4, button5;
	private List<Fragment> fragments;
	private MainPageAdapter adapter;
	private RelativeLayout re_back;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_service_new_project);
		re_back = (RelativeLayout) findViewById(R.id.re_back);
		fragments = new ArrayList<Fragment>();
		fragments.add(new MyReserveFragment());// 我的预定
		fragments.add(new AllProjectFragment());// 全部方案
		fragments.add(new WaitConfirmFragment());// 待确认
		fragments.add(new WaitPayFragment());// 待付款
		fragments.add(new GoingFragment());// 进行中
		fragments.add(new WaitEvalFragment());// 已完成

		pager = (ViewPager) findViewById(R.id.all_page);
		adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(fragments.size() - 1);
		pager.addOnPageChangeListener(this);
		group = (RadioGroup) findViewById(R.id.radioGroup1);
		button0 = (RadioButton) findViewById(R.id.radio0);
		button1 = (RadioButton) findViewById(R.id.radio1);
		button2 = (RadioButton) findViewById(R.id.radio2);
		button3 = (RadioButton) findViewById(R.id.radio3);
		button4 = (RadioButton) findViewById(R.id.radio4);
		button5 = (RadioButton) findViewById(R.id.radio5);
		group.setOnCheckedChangeListener(this);
		initView();

	}

	private void initView() {
		re_back.setOnClickListener(this);
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
		button0.setChecked(false);
		button1.setChecked(false);
		button2.setChecked(false);
		button3.setChecked(false);
		button4.setChecked(false);
		button5.setChecked(false);

		switch (position) {
		case 0:
			button0.setChecked(true);
			break;
		case 1:
			button1.setChecked(true);
			break;
		case 2:
			button2.setChecked(true);
			break;
		case 3:
			button3.setChecked(true);
			break;
		case 4:
			button4.setChecked(true);
			break;
		case 5:
			button5.setChecked(true);
			break;

		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio0:
			pager.setCurrentItem(0);
			break;
		case R.id.radio1:
			pager.setCurrentItem(1);
			break;
		case R.id.radio2:
			pager.setCurrentItem(2);
			break;
		case R.id.radio3:
			pager.setCurrentItem(3);
			break;
		case R.id.radio4:
			pager.setCurrentItem(4);
			break;
		case R.id.radio5:
			pager.setCurrentItem(5);
			break;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_back:
			finish();
			break;
		}
	}
}
