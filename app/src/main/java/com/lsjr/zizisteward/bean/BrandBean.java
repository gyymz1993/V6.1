package com.lsjr.zizisteward.bean;

import java.util.List;

public class BrandBean {
	private List<BrandInfo> brands;
	private String error;
	private String msg;

	public List<BrandInfo> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandInfo> brands) {
		this.brands = brands;
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

	public class BrandInfo {
		private String bdesc;
		private String bimg;
		private String bis_show;
		private String blogo;
		private String bname;
		private String brand_time;
		private String bsort;
		private String bstory;
		private String burl;
		private String entityId;
		private String id;
		private String persistent;

		public String getBdesc() {
			return bdesc;
		}

		public void setBdesc(String bdesc) {
			this.bdesc = bdesc;
		}

		public String getBimg() {
			return bimg;
		}

		public void setBimg(String bimg) {
			this.bimg = bimg;
		}

		public String getBis_show() {
			return bis_show;
		}

		public void setBis_show(String bis_show) {
			this.bis_show = bis_show;
		}

		public String getBlogo() {
			return blogo;
		}

		public void setBlogo(String blogo) {
			this.blogo = blogo;
		}

		public String getBname() {
			return bname;
		}

		public void setBname(String bname) {
			this.bname = bname;
		}

		public String getBrand_time() {
			return brand_time;
		}

		public void setBrand_time(String brand_time) {
			this.brand_time = brand_time;
		}

		public String getBsort() {
			return bsort;
		}

		public void setBsort(String bsort) {
			this.bsort = bsort;
		}

		public String getBstory() {
			return bstory;
		}

		public void setBstory(String bstory) {
			this.bstory = bstory;
		}

		public String getBurl() {
			return burl;
		}

		public void setBurl(String burl) {
			this.burl = burl;
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

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}
	}
}
