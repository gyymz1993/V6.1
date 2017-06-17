package com.lsjr.zizisteward.bean;

import java.util.List;

import com.hyphenate.chat.EMMessage;

public class GroupListBean {
	private String error;
	private String msg;
	private List<GroupList> grouplist;

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

	public List<GroupList> getGrouplist() {
		return grouplist;
	}

	public void setGrouplist(List<GroupList> grouplist) {
		this.grouplist = grouplist;
	}

	public static class GroupList {

		private Group group;
		private String size;
		private String ownerPhoto;

		public String getOwnerPhoto() {
			return ownerPhoto;
		}

		public void setOwnerPhoto(String ownerPhoto) {
			this.ownerPhoto = ownerPhoto;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public Group getGroup() {
			return group;
		}

		public void setGroup(Group group) {
			this.group = group;
		}

		public static class Group {
			private String description;
			private String entityId;
			private String gname;
			private String groupId;
			private String groupImg;
			private String groupName;
			private String id;
			private String is_open;
			private String is_owner;
			private String maxusers;
			private String member_name;
			private String owner;
			private String persistent;
			private String photo;
			private String state;
			private String who_hair;
			private String no_read_count;
			private EMMessage msg;
			private String is_add_friend;
			private boolean space;

			public boolean isSpace() {
				return space;
			}

			public void setSpace(boolean space) {
				this.space = space;
			}

			public String getIs_add_friend() {
				return is_add_friend;
			}

			public void setIs_add_friend(String is_add_friend) {
				this.is_add_friend = is_add_friend;
			}

			public EMMessage getMsg() {
				return msg;
			}

			public void setMsg(EMMessage msg) {
				this.msg = msg;
			}

			public String getNo_read_count() {
				return no_read_count;
			}

			public void setNo_read_count(String no_read_count) {
				this.no_read_count = no_read_count;
			}

			public String getWho_hair() {
				return who_hair;
			}

			public void setWho_hair(String who_hair) {
				this.who_hair = who_hair;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getEntityId() {
				return entityId;
			}

			public void setEntityId(String entityId) {
				this.entityId = entityId;
			}

			public String getGname() {
				return gname;
			}

			public void setGname(String gname) {
				this.gname = gname;
			}

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

			public String getGroupName() {
				return groupName;
			}

			public void setGroupName(String groupName) {
				this.groupName = groupName;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getIs_open() {
				return is_open;
			}

			public void setIs_open(String is_open) {
				this.is_open = is_open;
			}

			public String getIs_owner() {
				return is_owner;
			}

			public void setIs_owner(String is_owner) {
				this.is_owner = is_owner;
			}

			public String getMaxusers() {
				return maxusers;
			}

			public void setMaxusers(String maxusers) {
				this.maxusers = maxusers;
			}

			public String getMember_name() {
				return member_name;
			}

			public void setMember_name(String member_name) {
				this.member_name = member_name;
			}

			public String getOwner() {
				return owner;
			}

			public void setOwner(String owner) {
				this.owner = owner;
			}

			public String getPersistent() {
				return persistent;
			}

			public void setPersistent(String persistent) {
				this.persistent = persistent;
			}

			public String getPhoto() {
				return photo;
			}

			public void setPhoto(String photo) {
				this.photo = photo;
			}

			public String getState() {
				return state;
			}

			public void setState(String state) {
				this.state = state;
			}
		}
	}
}
