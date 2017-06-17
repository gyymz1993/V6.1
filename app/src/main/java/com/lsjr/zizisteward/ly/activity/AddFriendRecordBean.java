package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class AddFriendRecordBean {
	private String error;
	private String msg;
	private List<AddFriendRecord> addFriendRecord;

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

	public List<AddFriendRecord> getAddFriendRecord() {
		return addFriendRecord;
	}

	public void setAddFriendRecord(List<AddFriendRecord> addFriendRecord) {
		this.addFriendRecord = addFriendRecord;
	}

	public static class AddFriendRecord {
		private String by_user_id;
		private String entityId;
		private String id;
		private String is_show;
		private String persistent;
		private String photo;
		private int record_state;
		private String remark;
		private String user_id;
		private String user_name;

		public String getBy_user_id() {
			return by_user_id;
		}

		public void setBy_user_id(String by_user_id) {
			this.by_user_id = by_user_id;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
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

		public int getRecord_state() {
			return record_state;
		}
		
		public void setRecord_state(int record_state) {
			this.record_state = record_state;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
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
	}
}
