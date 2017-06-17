package com.lsjr.zizisteward.bean;

public class GroupsDataBean {
	private String msg;
	private String error;
	private GroupsData groupsData;

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

	public GroupsData getGroupsData() {
		return groupsData;
	}

	public void setGroupsData(GroupsData groupsData) {
		this.groupsData = groupsData;
	}

	public static class GroupsData {

		private String description;
		private String groupId;
		private String owner;
		private String maxusers;
		private boolean persistent;
		private String is_open;
		private String groupName;
		private String groupImg;
		
		public String getGroupImg() {
			return groupImg;
		}
		
		public void setGroupImg(String groupImg) {
			this.groupImg = groupImg;
		}
		
		public String getGroupName() {
			return groupName;
		}
		
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getIs_open() {
			return is_open;
		}
		
		public void setIs_open(String is_open) {
			this.is_open = is_open;
		}
		
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public String getMaxusers() {
			return maxusers;
		}

		public void setMaxusers(String maxusers) {
			this.maxusers = maxusers;
		}

		public boolean isPersistent() {
			return persistent;
		}

		public void setPersistent(boolean persistent) {
			this.persistent = persistent;
		}
	}
}
