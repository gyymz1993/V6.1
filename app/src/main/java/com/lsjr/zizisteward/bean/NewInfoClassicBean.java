package com.lsjr.zizisteward.bean;

import java.util.List;

public class NewInfoClassicBean {
	private String error;
	private String msg;
	private List<ClassicDetail> news_types;

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

	public List<ClassicDetail> getNews_types() {
		return news_types;
	}

	public void setNews_types(List<ClassicDetail> news_types) {
		this.news_types = news_types;
	}

}
