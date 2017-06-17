package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.MainPageAdapter;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.fragment.AllFragment;
import com.lsjr.zizisteward.fragment.NoPayFragment;
import com.lsjr.zizisteward.fragment.PaidFragment;
import com.lsjr.zizisteward.fragment.WaitEvaluateFragment;
import com.lsjr.zizisteward.fragment.WaitGetGoodsFragment;

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

public class MyAllOrderActivity extends FragmentActivity
		implements OnPageChangeListener, OnCheckedChangeListener, OnClickListener {

	private ViewPager pager;
	private RadioGroup group;
	private RadioButton button0, button1, button2, button3, button4;
	private List<Fragment> fragments;
	private MainPageAdapter adapter;
	private RelativeLayout mRe_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.activity_all_order);
		mRe_back = (RelativeLayout) findViewById(R.id.re_back);
		fragments = new ArrayList<Fragment>();
		fragments.add(new AllFragment());
		fragments.add(new NoPayFragment());// 未付款
		fragments.add(new PaidFragment());// 待发货
		fragments.add(new WaitGetGoodsFragment());// 待收货
		fragments.add(new WaitEvaluateFragment());// 待评价

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

		group.setOnCheckedChangeListener(this);

		String order = getIntent().getStringExtra("order");
		if (order.equals("all")) {
			((RadioButton) group.getChildAt(0)).setChecked(true);
		} else if (order.equals("nopay")) {
			((RadioButton) group.getChildAt(1)).setChecked(true);
		} else if (order.equals("paid")) {
			((RadioButton) group.getChildAt(2)).setChecked(true);
		} else if (order.equals("wait_getgoods")) {
			((RadioButton) group.getChildAt(3)).setChecked(true);
		} else if (order.equals("wait_evaluate")) {
			((RadioButton) group.getChildAt(4)).setChecked(true);
		}

		initView();
	}

	private void initView() {
		mRe_back.setOnClickListener(this);
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

		}
	}

	private void getTabState(int index) {

		button0.setChecked(false);
		button1.setChecked(false);
		button2.setChecked(false);
		button3.setChecked(false);
		button4.setChecked(false);

		switch (index) {
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
