package com.lsjr.zizisteward.bean;

import java.util.List;

public class GroupMemberBean {
	private String msg;
	private String error;
	private List<GroupMember> groupMember;

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

	public List<GroupMember> getGroupMember() {
		return groupMember;
	}

	public void setGroupMember(List<GroupMember> groupMember) {
		this.groupMember = groupMember;
	}

	public static class GroupMember {
		private String groupId;
		private String member_name;
		private boolean persistent;
		private String photo;
		private String user_id;
		private String user_name;
		private String state;
		private String is_owner;
		private boolean space;
		
		public void setSpace(boolean space) {
			this.space = space;
		}
		
		public boolean isSpace() {
			return space;
		}
		
		public String getIs_owner() {
			return is_owner;
		}
		
		public void setIs_owner(String is_owner) {
			this.is_owner = is_owner;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getMember_name() {
			return member_name;
		}

		public void setMember_name(String member_name) {
			this.member_name = member_name;
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

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	}
}
