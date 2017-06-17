package com.lsjr.zizisteward.bean;

import java.util.List;

public class GrouplabelBean {

	private String error;
	private String msg;
	private List<Grouplabel> grouplabel;

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

	public List<Grouplabel> getGrouplabel() {
		return grouplabel;
	}

	public void setGrouplabel(List<Grouplabel> grouplabel) {
		this.grouplabel = grouplabel;
	}

	public static class Grouplabel {
		private String entityId;
		private String gname;
		private String icon;
		private String id;
		private String persistent;
		private String remark;
		private String tpath;

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

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
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

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getTpath() {
			return tpath;
		}

		public void setTpath(String tpath) {
			this.tpath = tpath;
		}

	}
}
