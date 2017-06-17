package com.lsjr.zizisteward.bean;

import java.util.List;

public class HomePageBean {

	public List<LunBoPicsInfo> advertisements;
	public List<ClassicPicsInfo> shop_types;
	public String error;
	public String msg;
	public List<NewsInfomation> news_informations;
	public BrandShops shops;

	public List<LunBoPicsInfo> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<LunBoPicsInfo> advertisements) {
		this.advertisements = advertisements;
	}

	public List<ClassicPicsInfo> getShop_types() {
		return shop_types;
	}

	public void setShop_types(List<ClassicPicsInfo> shop_types) {
		this.shop_types = shop_types;
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

	public List<NewsInfomation> getNews_informations() {
		return news_informations;
	}

	public void setNews_informations(List<NewsInfomation> news_informations) {
		this.news_informations = news_informations;
	}

	public BrandShops getShops() {
		return shops;
	}

	public void setShops(BrandShops shops) {
		this.shops = shops;
	}

	public class NewsInfomation {
		private String content;
		private String entityId;
		private String id;
		private String image_filename;
		private String keywords;
		private String label_id;
		private String link;
		private String persistent;
		private String scource;
		private String title;
		private String tpath;
		private String tshow;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public String getImage_filename() {
			return image_filename;
		}

		public void setImage_filename(String image_filename) {
			this.image_filename = image_filename;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public String getLabel_id() {
			return label_id;
		}

		public void setLabel_id(String label_id) {
			this.label_id = label_id;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getScource() {
			return scource;
		}

		public void setScource(String scource) {
			this.scource = scource;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTpath() {
			return tpath;
		}

		public void setTpath(String tpath) {
			this.tpath = tpath;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}
	}

	public class BrandShops {

		private int currPage;
		private List<ShopsPageDetail> page;
		private int pageSize;
		private String pageTitle;
		private int totalCount;//总条数
		private int totalPageCount;

		public int getCurrPage() {
			return currPage;
		}

		public void setCurrPage(int currPage) {
			this.currPage = currPage;
		}

		public List<ShopsPageDetail> getPage() {
			return page;
		}

		public void setPage(List<ShopsPageDetail> page) {
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

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getTotalPageCount() {
			return totalPageCount;
		}

		public void setTotalPageCount(int totalPageCount) {
			this.totalPageCount = totalPageCount;
		}
	}

	public class ShopsPageDetail {
		private String article_number;
		private String audit;
		private String bname;
		private String cost_price;
		private String entityId;
		private String id;
		private String mname;
		private String persistent;
		private String putaway;
		private String samount;
		private String sbrand;
		private String scolour;
		private String scount;
		private String sell_points;
		private String shop_type;
		private String shot;
		private String simg;
		private String sinfo;
		private String sisrec;
		private String size;
		private String skeyword;
		private String sname;
		private String snew;
		private String spic;
		private String spicfirst;
		private String spid;
		private String sprice;
		private String tname;
		private String tpid;

		public String getArticle_number() {
			return article_number;
		}

		public void setArticle_number(String article_number) {
			this.article_number = article_number;
		}

		public String getAudit() {
			return audit;
		}

		public void setAudit(String audit) {
			this.audit = audit;
		}

		public String getBname() {
			return bname;
		}

		public void setBname(String bname) {
			this.bname = bname;
		}

		public String getCost_price() {
			return cost_price;
		}

		public void setCost_price(String cost_price) {
			this.cost_price = cost_price;
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

		public String getPutaway() {
			return putaway;
		}

		public void setPutaway(String putaway) {
			this.putaway = putaway;
		}

		public String getSamount() {
			return samount;
		}

		public void setSamount(String samount) {
			this.samount = samount;
		}

		public String getSbrand() {
			return sbrand;
		}

		public void setSbrand(String sbrand) {
			this.sbrand = sbrand;
		}

		public String getScolour() {
			return scolour;
		}

		public void setScolour(String scolour) {
			this.scolour = scolour;
		}

		public String getScount() {
			return scount;
		}

		public void setScount(String scount) {
			this.scount = scount;
		}

		public String getSell_points() {
			return sell_points;
		}

		public void setSell_points(String sell_points) {
			this.sell_points = sell_points;
		}

		public String getShop_type() {
			return shop_type;
		}

		public void setShop_type(String shop_type) {
			this.shop_type = shop_type;
		}

		public String getShot() {
			return shot;
		}

		public void setShot(String shot) {
			this.shot = shot;
		}

		public String getSimg() {
			return simg;
		}

		public void setSimg(String simg) {
			this.simg = simg;
		}

		public String getSinfo() {
			return sinfo;
		}

		public void setSinfo(String sinfo) {
			this.sinfo = sinfo;
		}

		public String getSisrec() {
			return sisrec;
		}

		public void setSisrec(String sisrec) {
			this.sisrec = sisrec;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getSkeyword() {
			return skeyword;
		}

		public void setSkeyword(String skeyword) {
			this.skeyword = skeyword;
		}

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}

		public String getSnew() {
			return snew;
		}

		public void setSnew(String snew) {
			this.snew = snew;
		}

		public String getSpic() {
			return spic;
		}

		public void setSpic(String spic) {
			this.spic = spic;
		}

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getSpid() {
			return spid;
		}

		public void setSpid(String spid) {
			this.spid = spid;
		}

		public String getSprice() {
			return sprice;
		}

		public void setSprice(String sprice) {
			this.sprice = sprice;
		}

		public String getTname() {
			return tname;
		}

		public void setTname(String tname) {
			this.tname = tname;
		}

		public String getTpid() {
			return tpid;
		}

		public void setTpid(String tpid) {
			this.tpid = tpid;
		}
	}
}
