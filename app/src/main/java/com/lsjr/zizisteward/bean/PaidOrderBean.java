package com.lsjr.zizisteward.bean;

import java.util.List;

public class PaidOrderBean {
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
		private String assess_order;
		private String bask_order;
		private String caddr;
		private String clocation;
		private String ctel;
		private List<PaidDelivery> delivery;
		private String dname;
		private String gid;
		private String gnum;
		private String gsname;
		private String order_price;
		private OrderTime order_time;
		private String pay_state;
		private PayTime pay_time;
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

		public String getCtel() {
			return ctel;
		}

		public void setCtel(String ctel) {
			this.ctel = ctel;
		}

		public List<PaidDelivery> getDelivery() {
			return delivery;
		}

		public void setDelivery(List<PaidDelivery> delivery) {
			this.delivery = delivery;
		}

		public String getDname() {
			return dname;
		}

		public void setDname(String dname) {
			this.dname = dname;
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

		public OrderTime getOrder_time() {
			return order_time;
		}

		public void setOrder_time(OrderTime order_time) {
			this.order_time = order_time;
		}

		public String getPay_state() {
			return pay_state;
		}

		public void setPay_state(String pay_state) {
			this.pay_state = pay_state;
		}

		public PayTime getPay_time() {
			return pay_time;
		}

		public void setPay_time(PayTime pay_time) {
			this.pay_time = pay_time;
		}

		public String getTransflow() {
			return transflow;
		}

		public void setTransflow(String transflow) {
			this.transflow = transflow;
		}
	}

	public class PaidDelivery {
		private String aid;
		private String bid;
		private String cost_price;
		private String dbname;
		private String dcolor;
		private String dnumber;
		private String dsimg;
		private String dsize;
		private String dsname;
		private String eid;
		private String ems_no;
		private String ems_price;
		private String entityId;
		private String gmid;
		private String id;
		private String kid;
		private String mname;
		private String persistent;
		private String service_name;
		private String service_named;
		private String service_price;
		private String service_type;
		private String sid;
		private String sign_name;
		private String sign_state;

		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
		}

		public String getBid() {
			return bid;
		}

		public void setBid(String bid) {
			this.bid = bid;
		}

		public String getCost_price() {
			return cost_price;
		}

		public void setCost_price(String cost_price) {
			this.cost_price = cost_price;
		}

		public String getDbname() {
			return dbname;
		}

		public void setDbname(String dbname) {
			this.dbname = dbname;
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

		public String getEid() {
			return eid;
		}

		public void setEid(String eid) {
			this.eid = eid;
		}

		public String getEms_no() {
			return ems_no;
		}

		public void setEms_no(String ems_no) {
			this.ems_no = ems_no;
		}

		public String getEms_price() {
			return ems_price;
		}

		public void setEms_price(String ems_price) {
			this.ems_price = ems_price;
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

		public String getMname() {
			return mname;
		}

		public void setMname(String mname) {
			this.mname = mname;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getService_name() {
			return service_name;
		}

		public void setService_name(String service_name) {
			this.service_name = service_name;
		}

		public String getService_named() {
			return service_named;
		}

		public void setService_named(String service_named) {
			this.service_named = service_named;
		}

		public String getService_price() {
			return service_price;
		}

		public void setService_price(String service_price) {
			this.service_price = service_price;
		}

		public String getService_type() {
			return service_type;
		}

		public void setService_type(String service_type) {
			this.service_type = service_type;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getSign_name() {
			return sign_name;
		}

		public void setSign_name(String sign_name) {
			this.sign_name = sign_name;
		}

		public String getSign_state() {
			return sign_state;
		}

		public void setSign_state(String sign_state) {
			this.sign_state = sign_state;
		}
	}

}
