package com.lsjr.zizisteward.bean;

import java.util.List;

public class LabelClassic {
	private String error;
	private String msg;
	private List<LabelDetailInfo> shop_types;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<LabelDetailInfo> getShop_types() {
		return shop_types;
	}

	public void setShop_types(List<LabelDetailInfo> shop_types) {
		this.shop_types = shop_types;
	}
}
