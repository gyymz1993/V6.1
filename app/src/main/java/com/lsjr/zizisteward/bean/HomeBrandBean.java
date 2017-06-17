package com.lsjr.zizisteward.bean;

import java.util.List;

public class HomeBrandBean {
	private String error;
	private String msg;
	private HomePageInfo page;
	private String totalNum;

	public HomePageInfo getPage() {
		return page;
	}

	public void setPage(HomePageInfo page) {
		this.page = page;
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

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public class HomePageInfo {
		private String currPage;
		private List<HomePageDetail> page;
		private String pageSize;
		private String pageTitle;
		private String totalCount;
		private String totalPageCount;

		public String getCurrPage() {
			return currPage;
		}

		public void setCurrPage(String currPage) {
			this.currPage = currPage;
		}

		public List<HomePageDetail> getPage() {
			return page;
		}

		public void setPage(List<HomePageDetail> page) {
			this.page = page;
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
	}

	public class HomePageDetail {
		private String add_time;
		private String article_number;
		private String audit;
		private String bid;
		private String bname;
		private String cost_price;
		private String entityId;
		private String id;
		private String is_show;
		private String mname;
		private String persistent;
		private String putaway;
		private String samount;
		private String sbin;
		private String scolour;
		private String scount;
		private String shop_number;
		private String shop_repertory;
		private String shop_type;
		private String shot;
		private String simg;
		private String sinfo;
		private String sisrec;
		private String size;
		private String skeyword;
		private String smarktime;
		private String sname;
		private String snew;
		private String spic;
		private String spicfirst;
		private String spid;
		private String sprice;
		private String stime;
		private String tname;
		private String tpid;
		private String tshow;

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getAdd_time() {
			return add_time;
		}

		public void setAdd_time(String add_time) {
			this.add_time = add_time;
		}

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

		public String getBid() {
			return bid;
		}

		public void setBid(String bid) {
			this.bid = bid;
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

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
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

		public String getSbin() {
			return sbin;
		}

		public void setSbin(String sbin) {
			this.sbin = sbin;
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

		public String getShop_number() {
			return shop_number;
		}

		public void setShop_number(String shop_number) {
			this.shop_number = shop_number;
		}

		public String getShop_repertory() {
			return shop_repertory;
		}

		public void setShop_repertory(String shop_repertory) {
			this.shop_repertory = shop_repertory;
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

		public String getSmarktime() {
			return smarktime;
		}

		public void setSmarktime(String smarktime) {
			this.smarktime = smarktime;
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

		public String getStime() {
			return stime;
		}

		public void setStime(String stime) {
			this.stime = stime;
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

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

	}
}
