package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class MedicalHomeBean {

	private String msg;
	private String error;
	/** 体检推荐 */
	private List<Amedicalr> Amedicalr;
	/** 名医推荐 */
	private List<Famousdoctor> Famousdoctor;
	/** 轮播图 */
	private List<Advertisements> advertisements;

	public List<Amedicalr> getAmedicalr() {
		return Amedicalr;
	}

	public void setAmedicalr(List<Amedicalr> amedicalr) {
		Amedicalr = amedicalr;
	}

	public List<Famousdoctor> getFamousdoctor() {
		return Famousdoctor;
	}

	public void setFamousdoctor(List<Famousdoctor> famousdoctor) {
		Famousdoctor = famousdoctor;
	}

	public List<Advertisements> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<Advertisements> advertisements) {
		this.advertisements = advertisements;
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

	public class Amedicalr {
		private String id;
		/**套餐名字*/
		private String sname;
		/**公司名字*/
		private String bname;
		/**折后价*/
		private String sprice;
		/**原价*/
		private String cost_price;
		/**图片*/
		private String spicfirst;
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getSname() {
			return sname;
		}
		public void setSname(String sname) {
			this.sname = sname;
		}
		public String getBname() {
			return bname;
		}
		public void setBname(String bname) {
			this.bname = bname;
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
		public String getSpicfirst() {
			return spicfirst;
		}
		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}
	}

	public class Famousdoctor {
		
	}

	public class Advertisements {
		private String image_filename;
		private String resolution;
		private String location;
		private String id;
		private String no;
		private String url;
		private String file_format;

		public String getImage_filename() {
			return image_filename;
		}

		public void setImage_filename(String image_filename) {
			this.image_filename = image_filename;
		}

		public String getResolution() {
			return resolution;
		}

		public void setResolution(String resolution) {
			this.resolution = resolution;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getFile_format() {
			return file_format;
		}

		public void setFile_format(String file_format) {
			this.file_format = file_format;
		}
	}
}
