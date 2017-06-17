package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class MedicalActivityBean {

	private List<MABean> mBeans;

	public List<MABean> getmBeans() {
		return mBeans;
	}

	public void setmBeans(List<MABean> mBeans) {
		this.mBeans = mBeans;
	}

	public static class MABean {
		private String title;
		private String name;
		private String old_price;
		private String new_price;
		private String service_name;
		
		public String getService_name() {
			return service_name;
		}
		
		public void setService_name(String service_name) {
			this.service_name = service_name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOld_price() {
			return old_price;
		}

		public void setOld_price(String old_price) {
			this.old_price = old_price;
		}

		public String getNew_price() {
			return new_price;
		}

		public void setNew_price(String new_price) {
			this.new_price = new_price;
		}
	}
}
