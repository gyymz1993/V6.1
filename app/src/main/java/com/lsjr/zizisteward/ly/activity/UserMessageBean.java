package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class UserMessageBean {

	private String error;
	private String msg;
	private List<UserMessage> userMessage;

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

	public List<UserMessage> getUserMessage() {
		return userMessage;
	}
	
	public void setUserMessage(List<UserMessage> userMessage) {
		this.userMessage = userMessage;
	}

	public class UserMessage {
		private String user_name;
		private String id;
		private String photo;
		private String name;
		private String is_show_realName;
		private String is_pass_account_search;
		private String is_pass_nickName_search;

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIs_show_realName() {
			return is_show_realName;
		}

		public void setIs_show_realName(String is_show_realName) {
			this.is_show_realName = is_show_realName;
		}

		public String getIs_pass_account_search() {
			return is_pass_account_search;
		}

		public void setIs_pass_account_search(String is_pass_account_search) {
			this.is_pass_account_search = is_pass_account_search;
		}

		public String getIs_pass_nickName_search() {
			return is_pass_nickName_search;
		}

		public void setIs_pass_nickName_search(String is_pass_nickName_search) {
			this.is_pass_nickName_search = is_pass_nickName_search;
		}
	}
}
