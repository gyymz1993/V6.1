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
import com.lsjr.zizisteward.bean.OutDoorsDepartmentBean;
import com.lsjr.zizisteward.fragment.ApplianceFragemnt;
import com.lsjr.zizisteward.fragment.DressFragment;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class TravelDepartmentActivity extends FragmentActivity
		implements OnPageChangeListener, OnCheckedChangeListener {
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ViewPager page;
	private RadioGroup group;
	private RadioButton radio0, radio2;
	private MainPageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traveldepartment);
		((RelativeLayout) findViewById(R.id.back)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		fragments.add(new DressFragment());// 穿戴
		fragments.add(new ApplianceFragemnt());// 用品

		page = (ViewPager) findViewById(R.id.all_page);
		group = (RadioGroup) findViewById(R.id.radioGroup1);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		adapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
		page.setAdapter(adapter);

		page.setOffscreenPageLimit(fragments.size() - 1);

		page.addOnPageChangeListener(this);

		group.setOnCheckedChangeListener(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "58");
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("分类" + result);
				OutDoorsDepartmentBean bean = GsonUtil.getInstance().fromJson(result, OutDoorsDepartmentBean.class);
				radio0.setText(bean.getTypes().get(0).getTname());
				radio2.setText(bean.getTypes().get(1).getTname());

			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio0:
			page.setCurrentItem(0);
			break;

		case R.id.radio2:
			page.setCurrentItem(1);
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
		radio0.setChecked(false);
		radio2.setChecked(false);
		switch (position) {
		case 0:
			radio0.setChecked(true);
			break;
		case 1:
			radio2.setChecked(true);
			break;

		}
	}

}