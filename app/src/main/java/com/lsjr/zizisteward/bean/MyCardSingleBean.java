package com.lsjr.zizisteward.bean;

public class MyCardSingleBean {

	private String error;
	private String msg;
	private Card card;
	private int is_pass_certificate;
	
	public int getIs_pass_certificate() {
		return is_pass_certificate;
	}
	
	public void setIs_pass_certificate(int is_pass_certificate) {
		this.is_pass_certificate = is_pass_certificate;
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

	public Card getCard() {
		return card;
	}
	
	public void setCard(Card card) {
		this.card = card;
	}

	public static class Card {
		private String id;
		private String email;
		private String label;
		private String phone;
		private int is_approve;
		private String user_id;
		private String entityId;
		private String position;
		private String username;
		private String company_name;
		private String card_qr_code;
		private String identification_photo;
		
		public int getIs_approve() {
			return is_approve;
		}
		
		public void setIs_approve(int is_approve) {
			this.is_approve = is_approve;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getCompany_name() {
			return company_name;
		}

		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}

		public String getCard_qr_code() {
			return card_qr_code;
		}

		public void setCard_qr_code(String card_qr_code) {
			this.card_qr_code = card_qr_code;
		}

		public String getIdentification_photo() {
			return identification_photo;
		}

		public void setIdentification_photo(String identification_photo) {
			this.identification_photo = identification_photo;
		}
	}
}
