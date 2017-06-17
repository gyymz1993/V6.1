package com.lsjr.zizisteward.bean;

import java.util.List;

public class PaidBean {
	private String error;
	private String msg;
	private PaidPageBean shop_indent;
	private String totalNum;

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

	public PaidPageBean getShop_indent() {
		return shop_indent;
	}

	public void setShop_indent(PaidPageBean shop_indent) {
		this.shop_indent = shop_indent;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public class PaidPageBean {

		private String currPage;
		private List<PaidDetail> page;
		private int pageSize;
		private String pageTitle;
		private String totalCount;
		private String totalPageCount;

		public String getCurrPage() {
			return currPage;
		}

		public void setCurrPage(String currPage) {
			this.currPage = currPage;
		}

		public List<PaidDetail> getPage() {
			return page;
		}

		public void setPage(List<PaidDetail> page) {
			this.page = page;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public String getPageTitle() {
			return pageTitle;
		}

		public void setPageTitle(String pageTitle) {
			this.pageTitle = pageTitle;
		}

		public String getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(String totalCount) {
			this.totalCount = totalCount;
		}

		public String getTotalPageCount() {
			return totalPageCount;
		}

		public void setTotalPageCount(String totalPageCount) {
			this.totalPageCount = totalPageCount;
		}

	}

	public static class PaidDetail {

		private String assess_order;
		private String bask_order;
		private String credit_pay_money;
		private String ems_code;
		private String ems_no;
		private String entityId;
		private String favour_mode;
		private String favour_type;
		private String gnum;
		private String gpic;
		private String gsname;
		private String id;
		private String is_send;
		private String order_man;
		private String order_man_type;
		private String order_price;
		private String order_real_money;
		private String order_state;
		private String order_time;
		private String other_pay_money;
		private String parent_id;
		private String pay_state;
		private String pay_type;
		private String persistent;
		private String remarks;
		private String sample_id;
		private String service_state;
		private String service_type;
		private String stage_pay_money;
		private String stage_pay_state;
		private String transflow;
		private String tshow;
		private String uid;

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

		public String getCredit_pay_money() {
			return credit_pay_money;
		}

		public void setCredit_pay_money(String credit_pay_money) {
			this.credit_pay_money = credit_pay_money;
		}

		public String getEms_code() {
			return ems_code;
		}

		public void setEms_code(String ems_code) {
			this.ems_code = ems_code;
		}

		public String getEms_no() {
			return ems_no;
		}

		public void setEms_no(String ems_no) {
			this.ems_no = ems_no;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getFavour_mode() {
			return favour_mode;
		}

		public void setFavour_mode(String favour_mode) {
			this.favour_mode = favour_mode;
		}

		public String getFavour_type() {
			return favour_type;
		}

		public void setFavour_type(String favour_type) {
			this.favour_type = favour_type;
		}

		public String getGnum() {
			return gnum;
		}

		public void setGnum(String gnum) {
			this.gnum = gnum;
		}

		public String getGpic() {
			return gpic;
		}

		public void setGpic(String gpic) {
			this.gpic = gpic;
		}

		public String getGsname() {
			return gsname;
		}

		public void setGsname(String gsname) {
			this.gsname = gsname;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIs_send() {
			return is_send;
		}

		public void setIs_send(String is_send) {
			this.is_send = is_send;
		}

		public String getOrder_man() {
			return order_man;
		}

		public void setOrder_man(String order_man) {
			this.order_man = order_man;
		}

		public String getOrder_man_type() {
			return order_man_type;
		}

		public void setOrder_man_type(String order_man_type) {
			this.order_man_type = order_man_type;
		}

		public String getOrder_price() {
			return order_price;
		}

		public void setOrder_price(String order_price) {
			this.order_price = order_price;
		}

		public String getOrder_real_money() {
			return order_real_money;
		}

		public void setOrder_real_money(String order_real_money) {
			this.order_real_money = order_real_money;
		}

		public String getOrder_state() {
			return order_state;
		}

		public void setOrder_state(String order_state) {
			this.order_state = order_state;
		}

		public String getOrder_time() {
			return order_time;
		}

		public void setOrder_time(String order_time) {
			this.order_time = order_time;
		}

		public String getOther_pay_money() {
			return other_pay_money;
		}

		public void setOther_pay_money(String other_pay_money) {
			this.other_pay_money = other_pay_money;
		}

		public String getParent_id() {
			return parent_id;
		}

		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}

		public String getPay_state() {
			return pay_state;
		}

		public void setPay_state(String pay_state) {
			this.pay_state = pay_state;
		}

		public String getPay_type() {
			return pay_type;
		}

		public void setPay_type(String pay_type) {
			this.pay_type = pay_type;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getSample_id() {
			return sample_id;
		}

		public void setSample_id(String sample_id) {
			this.sample_id = sample_id;
		}

		public String getService_state() {
			return service_state;
		}

		public void setService_state(String service_state) {
			this.service_state = service_state;
		}

		public String getService_type() {
			return service_type;
		}

		public void setService_type(String service_type) {
			this.service_type = service_type;
		}

		public String getStage_pay_money() {
			return stage_pay_money;
		}

		public void setStage_pay_money(String stage_pay_money) {
			this.stage_pay_money = stage_pay_money;
		}

		public String getStage_pay_state() {
			return stage_pay_state;
		}

		public void setStage_pay_state(String stage_pay_state) {
			this.stage_pay_state = stage_pay_state;
		}

		public String getTransflow() {
			return transflow;
		}

		public void setTransflow(String transflow) {
			this.transflow = transflow;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

	}

}
