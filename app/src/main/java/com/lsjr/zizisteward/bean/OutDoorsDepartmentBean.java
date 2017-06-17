package com.lsjr.zizisteward.bean;

import java.util.List;

public class OutDoorsDepartmentBean {
	private String error;
	private String msg;
	private List<OutDoorsList> types;

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

	public List<OutDoorsList> getTypes() {
		return types;
	}

	public void setTypes(List<OutDoorsList> types) {
		this.types = types;
	}

	public class OutDoorsList {
		private String classify_type;
		private String description;
		private String entityId;
		private String id;
		private String persistent;
		private String tgrade;
		private String tname;
		private String tpath;
		private String tpid;
		private String tshow;
		private String type_icon;
		private String type_icons;
		private String type_img;

		public String getClassify_type() {
			return classify_type;
		}

		public void setClassify_type(String classify_type) {
			this.classify_type = classify_type;
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

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getTgrade() {
			return tgrade;
		}

		public void setTgrade(String tgrade) {
			this.tgrade = tgrade;
		}

		public String getTname() {
			return tname;
		}

		public void setTname(String tname) {
			this.tname = tname;
		}

		public String getTpath() {
			return tpath;
		}

		public void setTpath(String tpath) {
			this.tpath = tpath;
		}

		public String getTpid() {
			return tpid;
		}

		public void setTpid(String tpid) {
			this.tpid = tpid;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

		public String getType_icon() {
			return type_icon;
		}

		public void setType_icon(String type_icon) {
			this.type_icon = type_icon;
		}

		public String getType_icons() {
			return type_icons;
		}

		public void setType_icons(String type_icons) {
			this.type_icons = type_icons;
		}

		public String getType_img() {
			return type_img;
		}

		public void setType_img(String type_img) {
			this.type_img = type_img;
		}

	}
}
