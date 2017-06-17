package com.lsjr.zizisteward.ly.activity;

import java.util.Comparator;

import com.lsjr.zizisteward.bean.UserListBean.UserList;

public class PinyinComparatorRead implements Comparator<UserList> {
	@Override
	public int compare(UserList lhs, UserList rhs) {
		// TODO Auto-generated method stub
		return sort(lhs, rhs);
	}

	private int sort(UserList lhs, UserList rhs) {
		// 获取ascii值
		int lhs_ascii = lhs.getFpinyin().toUpperCase().charAt(0);
		int rhs_ascii = rhs.getFpinyin().toUpperCase().charAt(0);
		// 判断若不是字母，则排在字母之后
		if (lhs_ascii < 65 || lhs_ascii > 90)
			return 1;
		else if (rhs_ascii < 65 || rhs_ascii > 90)
			return -1;
		else
			return lhs.getPinyin().compareTo(rhs.getPinyin());
	}
}
