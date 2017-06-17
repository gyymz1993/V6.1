package com.lsjr.zizisteward.bean;

import java.util.List;

public class OrderBean {
	private ContentData contentData;
	private String error;
	private String msg;

	public ContentData getContentData() {
		return contentData;
	}

	public void setContentData(ContentData contentData) {
		this.contentData = contentData;
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

	public class ContentData {

		private List<Delivery> delivery;
		private String assess_order;
		private String bask_order;
		private String gid;
		private String gnum;
		private String gsname;
		private String order_price;
		private String order_time;
		private String pay_state;
		private String pay_time;
		private String transflow;

		public String getAssess_order() {
			return assess_order;
		}

		public void setAssess_order(String assess_order) {
			this.assess_order = assess_order;
		}

		public String getBask_order() {
			return bask_order;
		}

		public void setBask_order(String bask_order) {
			this.bask_order = bask_order;
		}

		public String getGid() {
			return gid;
		}

		public void setGid(String gid) {
			this.gid = gid;
		}

		public String getGnum() {
			return gnum;
		}

		public void setGnum(String gnum) {
			this.gnum = gnum;
		}

		public String getGsname() {
			return gsname;
		}

		public void setGsname(String gsname) {
			this.gsname = gsname;
		}

		public String getOrder_price() {
			return order_price;
		}

		public void setOrder_price(String order_price) {
			this.order_price = order_price;
		}

		public String getOrder_time() {
			return order_time;
		}

		public void setOrder_time(String order_time) {
			this.order_time = order_time;
		}

		public String getPay_state() {
			return pay_state;
		}

		public void setPay_state(String pay_state) {
			this.pay_state = pay_state;
		}

		public String getPay_time() {
			return pay_time;
		}

		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
		}

		public String getTransflow() {
			return transflow;
		}

		public void setTransflow(String transflow) {
			this.transflow = transflow;
		}

		public List<Delivery> getDelivery() {
			return delivery;
		}

		public void setDelivery(List<Delivery> delivery) {
			this.delivery = delivery;
		}

	}

	public class Delivery {

		private String aid;
		private String caddr;
		private String clocation;
		private String cname;
		private String cphone;
		private String dcolor;
		private String dnumber;
		private String dsimg;
		private String dsize;
		private String dsname;
		private String entityId;
		private String gmid;
		private String id;
		private String kid;
		private String persistent;
		private String service_price;
		private String sid;

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
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

		public String getDcolor() {
			return dcolor;
		}

		public void setDcolor(String dcolor) {
			this.dcolor = dcolor;
		}

		public String getDnumber() {
			return dnumber;
		}

		public void setDnumber(String dnumber) {
			this.dnumber = dnumber;
		}

		public String getDsimg() {
			return dsimg;
		}

		public void setDsimg(String dsimg) {
			this.dsimg = dsimg;
		}

		public String getDsize() {
			return dsize;
		}

		public void setDsize(String dsize) {
			this.dsize = dsize;
		}

		public String getDsname() {
			return dsname;
		}

		public void setDsname(String dsname) {
			this.dsname = dsname;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getGmid() {
			return gmid;
		}

		public void setGmid(String gmid) {
			this.gmid = gmid;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getKid() {
			return kid;
		}

		public void setKid(String kid) {
			this.kid = kid;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getService_price() {
			return service_price;
		}

		public void setService_price(String service_price) {
			this.service_price = service_price;
		}
	}
}
