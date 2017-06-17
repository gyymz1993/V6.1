package com.lsjr.zizisteward.bean;

import java.util.List;

public class QueryAddressBean {
	private List<QueryAddressInfo> checkaddr;
	private String error;
	private String msg;

	public List<QueryAddressInfo> getCheckaddr() {
		return checkaddr;
	}

	public void setCheckaddr(List<QueryAddressInfo> checkaddr) {
		this.checkaddr = checkaddr;
	}

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

}
