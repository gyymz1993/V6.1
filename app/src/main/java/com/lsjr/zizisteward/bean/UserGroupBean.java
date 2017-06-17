package com.lsjr.zizisteward.bean;

import java.util.List;

public class UserGroupBean {
	private String error;
	private String msg;
	private List<UserGroup> userGroup;

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

	public List<UserGroup> getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(List<UserGroup> userGroup) {
		this.userGroup = userGroup;
	}

	public static class UserGroup {
		private String groupId;
		private String groupImg;

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getGroupImg() {
			return groupImg;
		}

		public void setGroupImg(String groupImg) {
			this.groupImg = groupImg;
		}
	}
}
