package com.lsjr.zizisteward.bean;

import java.util.List;

public class GroupFriendBean {
	private String msg;
	private String error;
	private List<GroupFriend> groupFriend;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<GroupFriend> getGroupFriend() {
		return groupFriend;
	}

	public void setGroupFriend(List<GroupFriend> groupFriend) {
		this.groupFriend = groupFriend;
	}

	public static class GroupFriend {
		private String account;
		private String entityId;
		private String friend_id;
		private String id;
		private String name;
		private boolean persistent;
		private String photo;
		private String user_id;
		private String user_name;
		private boolean space;

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getFriend_id() {
			return friend_id;
		}

		public void setFriend_id(String friend_id) {
			this.friend_id = friend_id;
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

		public boolean isPersistent() {
			return persistent;
		}

		public void setPersistent(boolean persistent) {
			this.persistent = persistent;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public boolean isSpace() {
			return space;
		}

		public void setSpace(boolean space) {
			this.space = space;
		}
	}
}
