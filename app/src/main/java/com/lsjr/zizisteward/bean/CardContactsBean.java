package com.lsjr.zizisteward.bean;

import java.util.List;

public class CardContactsBean {

	private String error;
	private String msg;
	private List<Contacts> contacts;

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

	public List<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	public static class Contacts {
		private String card_qr_code;
		private String company_name;
		private String email;
		private String id;
		private String identification_photo;
		private String is_approve;
		private String label;
		private String phone;
		private String position;
		private String remark;
		private String tcid;
		private String tshow;
		private String user_id;
		private String username;
		private boolean space;
		
		public boolean isSpace() {
			return space;
		}
		
		public void setSpace(boolean space) {
			this.space = space;
		}

		public String getCard_qr_code() {
			return card_qr_code;
		}

		public void setCard_qr_code(String card_qr_code) {
			this.card_qr_code = card_qr_code;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIdentification_photo() {
			return identification_photo;
		}

		public void setIdentification_photo(String identification_photo) {
			this.identification_photo = identification_photo;
		}

		public String getIs_approve() {
			return is_approve;
		}

		public void setIs_approve(String is_approve) {
			this.is_approve = is_approve;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getTcid() {
			return tcid;
		}

		public void setTcid(String tcid) {
			this.tcid = tcid;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
}
