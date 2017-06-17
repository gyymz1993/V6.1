package com.lsjr.zizisteward.bean;

import java.util.List;

public class AddressBookBean {
	private String error;
	private String msg;
	private String userPhoto;
	private String userUser_id;
	private String userUser_name;
	private List<Friends> friends;

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getUserUser_id() {
		return userUser_id;
	}

	public void setUserUser_id(String userUser_id) {
		this.userUser_id = userUser_id;
	}

	public String getUserUser_name() {
		return userUser_name;
	}

	public void setUserUser_name(String userUser_name) {
		this.userUser_name = userUser_name;
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

	public List<Friends> getFriends() {
		return friends;
	}

	public void setFriends(List<Friends> friends) {
		this.friends = friends;
	}

	public static class Friends {
		private String user_name;
		private String id;
		private String entityId;
		private String friend_id;
		private boolean persistent;
		private String photo;
		private String user_id;
		private String account;
		private String pinYin;
		private String firstPinYin;
		private int type;
		private int space;
		private String friend_name;
		
		public int getSpace() {
			return space;
		}
		
		public void setSpace(int space) {
			this.space = space;
		}
		
		public String getFriend_name() {
			return friend_name;
		}
		
		public void setFriend_name(String friend_name) {
			this.friend_name = friend_name;
		}
		
		public int getType() {
			return type;
		}
		
		public void setType(int type) {
			this.type = type;
		}
		
		public String getFirstPinYin() {
			return firstPinYin;
		}
		
		public String getPinYin() {
			return pinYin;
		}
		
		public void setFirstPinYin(String firstPinYin) {
			this.firstPinYin = firstPinYin;
		}
		
		public void setPinYin(String pinYin) {
			this.pinYin = pinYin;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

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
	}
}
