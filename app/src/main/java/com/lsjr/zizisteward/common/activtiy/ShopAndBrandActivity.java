package com.lsjr.zizisteward.common.activtiy;

import java.util.ArrayList;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.MainPageAdapter;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ShopAndBrandActivity extends FragmentActivity implements OnPageChangeListener, OnCheckedChangeListener {
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private ViewPager mAll_page;
	private MainPageAdapter mAdapter;
	private RadioGroup mRadioGroup1;
	private RadioButton mRadio0;
	private RadioButton mRadio1;
	private Intent mIntent;
	private Drawable mDrawable;
	private RelativeLayout mRe_back;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_shop_and_brand);
		fragments.add(new ShopFragment());// 店铺
		fragments.add(new BrandFragment());// 品牌
		mRe_back = (RelativeLayout) findViewById(R.id.re_back);
		mRadioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		mRadio0 = (RadioButton) findViewById(R.id.radio0);
		mRadio1 = (RadioButton) findViewById(R.id.radio1);

		mAll_page = (ViewPager) findViewById(R.id.all_page);

		mAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
		mAll_page.setAdapter(mAdapter);

		Resources resources = this.getResources();
		mDrawable = resources.getDrawable(R.drawable.shop_and_brand);
		mDrawable.setBounds(1, 1, 120, 8);

		mRadio0.setCompoundDrawables(null, null, null, mDrawable);
		mRadio1.setCompoundDrawables(null, null, null, null);

		mAll_page.setOffscreenPageLimit(fragments.size() - 1);
		mAll_page.addOnPageChangeListener(this);
		mRadioGroup1.setOnCheckedChangeListener(this);
		initListener();

	}

	private void initListener() {
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
			mAll_page.setCurrentItem(0);
			break;
		case R.id.radio1:
			mAll_page.setCurrentItem(1);
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

	private void getTabState(int index) {
		mRadio0.setChecked(false);
		mRadio1.setChecked(false);

		switch (index) {
		case 0:
			mRadio0.setChecked(true);
			mRadio0.setCompoundDrawables(null, null, null, mDrawable);
			mRadio1.setCompoundDrawables(null, null, null, null);
			break;
		case 1:
			mRadio1.setChecked(true);
			mRadio0.setCompoundDrawables(null, null, null, null);
			mRadio1.setCompoundDrawables(null, null, null, mDrawable);
			break;

		}
	}
}
