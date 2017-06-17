package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.MainPageAdapter;
import com.lsjr.zizisteward.fragment.AbstractFragment;
import com.lsjr.zizisteward.fragment.CustomFragment;
import com.lsjr.zizisteward.fragment.TravelComboFragment;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

public class TaiWanHealthTravelActivity extends FragmentActivity
		implements OnCheckedChangeListener, OnPageChangeListener {
	private ViewPager pager;
	private RadioGroup group;
	private RadioButton button0, button1, button2;
	private List<Fragment> fragments;
	private MainPageAdapter adapter;
	private RelativeLayout mRe_back;
	public String mSpid;
	private String mTtid;
	private String mTdapid;
	private Drawable mDrawable;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_taiwan_travel);
		group = (RadioGroup) findViewById(R.id.radioGroup1);
		button0 = (RadioButton) findViewById(R.id.radio0);
		button1 = (RadioButton) findViewById(R.id.radio1);
		button2 = (RadioButton) findViewById(R.id.radio2);
		mRe_back = (RelativeLayout) findViewById(R.id.re_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		mSpid = getIntent().getStringExtra("spid");// 商品id
		mTtid = getIntent().getStringExtra("ttid");// 主题id
		mTdapid = getIntent().getStringExtra("tdapid");// 区域id
		String name = getIntent().getStringExtra("name");// 主题名称
		tv_title.setText(name);
		System.out.println("商品id" + mSpid);
		PreferencesUtils.putString(getApplicationContext(), "spid", mSpid);
		PreferencesUtils.putString(getApplicationContext(), "ttid", mTtid);
		PreferencesUtils.putString(getApplicationContext(), "tdapid", mTdapid);

		fragments = new ArrayList<Fragment>();
		fragments.add(new AbstractFragment());// 简介
		fragments.add(new CustomFragment());// 定制
		fragments.add(new TravelComboFragment());// 套餐

		pager = (ViewPager) findViewById(R.id.all_page);

		adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(adapter);

		Resources resources = this.getResources();
		mDrawable = resources.getDrawable(R.drawable.huangxian);
		mDrawable.setBounds(1, 1, 120, 8);

		button0.setCompoundDrawables(null, null, null, mDrawable);
		button1.setCompoundDrawables(null, null, null, null);
		button2.setCompoundDrawables(null, null, null, null);

		pager.setOffscreenPageLimit(fragments.size() - 1);
		pager.addOnPageChangeListener(this);

		group.setOnCheckedChangeListener(this);
		mRe_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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

		}
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

		switch (position) {
		case 0:
			button0.setChecked(true);
			button0.setCompoundDrawables(null, null, null, mDrawable);
			button1.setCompoundDrawables(null, null, null, null);
			button2.setCompoundDrawables(null, null, null, null);

			break;
		case 1:
			button1.setChecked(true);
			button0.setCompoundDrawables(null, null, null, null);
			button1.setCompoundDrawables(null, null, null, mDrawable);
			button2.setCompoundDrawables(null, null, null, null);
			break;
		case 2:
			button2.setChecked(true);
			button0.setCompoundDrawables(null, null, null, null);
			button1.setCompoundDrawables(null, null, null, null);
			button2.setCompoundDrawables(null, null, null, mDrawable);
			break;

		}

	}

	public class BasicTravelBean {
		private String comboDetailUrl;
		private String customizationUrl;
		private String error;
		private String introUrl;
		private String msg;

		public String getComboDetailUrl() {
			return comboDetailUrl;
		}

		public void setComboDetailUrl(String comboDetailUrl) {
			this.comboDetailUrl = comboDetailUrl;
		}

		public String getCustomizationUrl() {
			return customizationUrl;
		}

		public void setCustomizationUrl(String customizationUrl) {
			this.customizationUrl = customizationUrl;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getIntroUrl() {
			return introUrl;
		}

		public void setIntroUrl(String introUrl) {
			this.introUrl = introUrl;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}
}
