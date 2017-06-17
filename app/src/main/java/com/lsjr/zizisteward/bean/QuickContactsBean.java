package com.lsjr.zizisteward.bean;

import java.util.List;

public class QuickContactsBean {

	/** 返回码 */
	private String error;
	/** 返回的信息描述 */
	private String msg;
	/** 返回的数据集合 */
	private List<LinkMan> linkman;

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

	public List<LinkMan> getLinkman() {
		return linkman;
	}

	public void setLinkman(List<LinkMan> linkman) {
		this.linkman = linkman;
	}

	public static class LinkMan {
		/** 常用联系人孜孜账户id */
		private String common_user_id;
		/** 联系人手机 */
		private String contact_mobile;
		/** 联系人名称 */
		private String contact_name;
		/** 头像 */
		private String contact_photo;
		/** 备注 */
		private String contact_remark;
		/***/
		private String entityId;
		/** 自增id */
		private String id;
		/***/
		private boolean persistent;
		/** 关系(0其他、1家人、2朋友、3同事) */
		private String relation;
		/** 用户id */
		private String user_id;

		public String getCommon_user_id() {
			return common_user_id;
		}

		public void setCommon_user_id(String common_user_id) {
			this.common_user_id = common_user_id;
		}

		public String getContact_mobile() {
			return contact_mobile;
		}

		public void setContact_mobile(String contact_mobile) {
			this.contact_mobile = contact_mobile;
		}

		public String getContact_name() {
			return contact_name;
		}

		public void setContact_name(String contact_name) {
			this.contact_name = contact_name;
		}

		public String getContact_photo() {
			return contact_photo;
		}

		public void setContact_photo(String contact_photo) {
			this.contact_photo = contact_photo;
		}

		public String getContact_remark() {
			return contact_remark;
		}

		public void setContact_remark(String contact_remark) {
			this.contact_remark = contact_remark;
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

		public boolean getPersistent() {
			return persistent;
		}

		public void setPersistent(boolean persistent) {
			this.persistent = persistent;
		}

		public String getRelation() {
			return relation;
		}

		public void setRelation(String relation) {
			this.relation = relation;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
	}
}
