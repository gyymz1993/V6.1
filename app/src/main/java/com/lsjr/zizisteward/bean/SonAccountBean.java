package com.lsjr.zizisteward.bean;

import java.util.List;

public class SonAccountBean {
	private String error;
	private String msg;
	private List<SonAccountInfo> sonUser;

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

	public List<SonAccountInfo> getSonUser() {
		return sonUser;
	}

	public void setSonUser(List<SonAccountInfo> sonUser) {
		this.sonUser = sonUser;
	}

}
