package com.lsjr.zizisteward.bean;

import java.io.Serializable;
import java.util.List;

public class CateLabelBean {

	private String error;
	private String msg;
	private String is_expert;
	private List<Cate_Label> cate_label;

	public void setIs_expert(String is_expert) {
		this.is_expert = is_expert;
	}
	
	public String getIs_expert() {
		return is_expert;
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

	public List<Cate_Label> getCate_label() {
		return cate_label;
	}

	public void setCate_label(List<Cate_Label> cate_label) {
		this.cate_label = cate_label;
	}

	public static class Cate_Label implements Serializable {
		private String id;
		private String labelName;
		private boolean space;
		
		public boolean isSpace() {
			return space;
		}
		
		public void setSpace(boolean space) {
			this.space = space;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLabelName() {
			return labelName;
		}

		public void setLabelName(String labelName) {
			this.labelName = labelName;
		}
	}
}
