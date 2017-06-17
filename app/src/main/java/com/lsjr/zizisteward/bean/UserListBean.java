package com.lsjr.zizisteward.bean;

import java.util.List;

public class UserListBean {

	private String error;
	private String msg;
	private List<UserList> UserList;

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

	public List<UserList> getUserList() {
		return UserList;
	}

	public void setUserList(List<UserList> userList) {
		UserList = userList;
	}

	public static class UserList {
		private String id;
		private String name;
		private String user_name;
		private String state;
		private String pinyin;
		private String Fpinyin;
		private boolean persistent;
		
		public void setPersistent(boolean persistent) {
			this.persistent = persistent;
		}
		
		public boolean isPersistent() {
			return persistent;
		}
		
		public String getFpinyin() {
			return Fpinyin;
		}
		
		public void setFpinyin(String fpinyin) {
			Fpinyin = fpinyin;
		}
		
		public String getPinyin() {
			return pinyin;
		}
		
		public void setPinyin(String pinyin) {
			this.pinyin = pinyin;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	}
}
