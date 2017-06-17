package com.lsjr.zizisteward.basic;

public class BaseResponse {
	private int state;
	private String errorMsg;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
