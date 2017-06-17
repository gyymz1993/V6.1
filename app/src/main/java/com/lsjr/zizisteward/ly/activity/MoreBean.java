package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class MoreBean {
	private String msg;
	private String error;
	private List<Shop> shop;

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

	public List<Shop> getShop() {
		return shop;
	}

	public void setShop(List<Shop> shop) {
		this.shop = shop;
	}

	public static class Shop {
		private String bname;
		private String sname;
		private String spicfirst;
		private String theme_name;
		private String sprice;
		private String cost_price;
		private String id;

		public String getBname() {
			return bname;
		}

		public void setBname(String bname) {
			this.bname = bname;
		}

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getTheme_name() {
			return theme_name;
		}

		public void setTheme_name(String theme_name) {
			this.theme_name = theme_name;
		}

		public String getSprice() {
			return sprice;
		}

		public void setSprice(String sprice) {
			this.sprice = sprice;
		}

		public String getCost_price() {
			return cost_price;
		}

		public void setCost_price(String cost_price) {
			this.cost_price = cost_price;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
