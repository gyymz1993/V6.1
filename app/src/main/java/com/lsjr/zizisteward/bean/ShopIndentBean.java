package com.lsjr.zizisteward.bean;

import java.util.List;

public class ShopIndentBean {

	private String msg;
	private String error;
	private String totalNum;
	private Shop_Indent shop_indent;

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

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public Shop_Indent getShop_indent() {
		return shop_indent;
	}

	public void setShop_indent(Shop_Indent shop_indent) {
		this.shop_indent = shop_indent;
	}

	public static class Shop_Indent {
		private String currPage;
		private String pageSize;
		private String pageTitle;
		private String totalCount;
		private String totalPageCount;
		private List<Page> page;

		public String getCurrPage() {
			return currPage;
		}

		public void setCurrPage(String currPage) {
			this.currPage = currPage;
		}

		public String getPageSize() {
			return pageSize;
		}

		public void setPageSize(String pageSize) {
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

		public List<Page> getPage() {
			return page;
		}

		public void setPage(List<Page> page) {
			this.page = page;
		}

		public static class Page {
			private String assess_order;
			private String bask_order;
			private String caddr;
			private String clocation;
			private String credit_pay_money;
			private String ctel;
			private String dname;
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
//			private OrderTime order_time;
			private String other_pay_money;
			private String pay_state;
			private String pay_type;
			private String persistent;
			private String remarks;
			private String service_state;
			private String service_type;
			private String stage_pay_money;
			private String stage_pay_state;
			private String transflow;
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

			public String getCredit_pay_money() {
				return credit_pay_money;
			}

			public void setCredit_pay_money(String credit_pay_money) {
				this.credit_pay_money = credit_pay_money;
			}

			public String getCtel() {
				return ctel;
			}

			public void setCtel(String ctel) {
				this.ctel = ctel;
			}

			public String getDname() {
				return dname;
			}

			public void setDname(String dname) {
				this.dname = dname;
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
//
//			public OrderTime getOrder_time() {
//				return order_time;
//			}
//
//			public void setOrder_time(OrderTime order_time) {
//				this.order_time = order_time;
//			}

			public String getOther_pay_money() {
				return other_pay_money;
			}

			public void setOther_pay_money(String other_pay_money) {
				this.other_pay_money = other_pay_money;
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

			public String getUid() {
				return uid;
			}

			public void setUid(String uid) {
				this.uid = uid;
			}
		}
	}
}
