package com.lsjr.zizisteward.ly.activity;

import java.util.List;

import android.R.integer;

public class MyFocusidsBean {
	private String error;
	private String msg;
	private List<MyFocusids> myfocusids;

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

	public List<MyFocusids> getMyfocusids() {
		return myfocusids;
	}

	public void setMyfocusids(List<MyFocusids> myfocusids) {
		this.myfocusids = myfocusids;
	}

	public static class MyFocusids {
		private String photo;
		private String user_name;
		private String userid;
		private int space;
		private int type;
		private int get_rid;

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public int getSpace() {
			return space;
		}

		public void setSpace(int space) {
			this.space = space;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getGet_rid() {
			return get_rid;
		}

		public void setGet_rid(int get_rid) {
			this.get_rid = get_rid;
		}

	}
}
