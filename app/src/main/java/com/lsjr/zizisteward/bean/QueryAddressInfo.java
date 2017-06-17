package com.lsjr.zizisteward.bean;

public class QueryAddressInfo {
	private String aid;
	private String caddr;
	private String clocation;
	private String cname;
	private String cphone;
	private String cpostcode;
	private String ctel;
	private String entityId;
	private String id;
	private String is_common;
	private String is_display;
	private String persistent;
	private String receiv_time;
	private String user_id;
	private boolean isChecked;

	public QueryAddressInfo(boolean isChecked) {
		super();
		this.isChecked = isChecked;
	}

	public QueryAddressInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getCaddr() {
		return caddr;
	}

	public void setCaddr(String caddr) {
		this.caddr = caddr;
	}

	public String getClocation() {
		return clocation;
	}

	public void setClocation(String clocation) {
		this.clocation = clocation;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCphone() {
		return cphone;
	}

	public void setCphone(String cphone) {
		this.cphone = cphone;
	}

	public String getCpostcode() {
		return cpostcode;
	}

	public void setCpostcode(String cpostcode) {
		this.cpostcode = cpostcode;
	}

	public String getCtel() {
		return ctel;
	}

	public void setCtel(String ctel) {
		this.ctel = ctel;
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

	public String getIs_common() {
		return is_common;
	}

	public void setIs_common(String is_common) {
		this.is_common = is_common;
	}

	public String getIs_display() {
		return is_display;
	}

	public void setIs_display(String is_display) {
		this.is_display = is_display;
	}

	public String getPersistent() {
		return persistent;
	}

	public void setPersistent(String persistent) {
		this.persistent = persistent;
	}

	public String getReceiv_time() {
		return receiv_time;
	}

	public void setReceiv_time(String receiv_time) {
		this.receiv_time = receiv_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
