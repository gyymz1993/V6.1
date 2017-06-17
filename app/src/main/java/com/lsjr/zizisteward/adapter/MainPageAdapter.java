package com.lsjr.zizisteward.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public MainPageAdapter(FragmentManager fm) {
		super(fm);
	}

	public MainPageAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments) {
		super(supportFragmentManager);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
