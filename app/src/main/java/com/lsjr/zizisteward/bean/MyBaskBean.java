package com.lsjr.zizisteward.bean;

import java.util.List;

public class MyBaskBean {
	private String error;
	private String fans;
	private String follow;
	private String msg;
	private MyBaskPage page;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getFollow() {
		return follow;
	}

	public void setFollow(String follow) {
		this.follow = follow;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public MyBaskPage getPage() {
		return page;
	}

	public void setPage(MyBaskPage page) {
		this.page = page;
	}

	public class MyBaskPage {

		private String currPage;
		private List<MyBaskDetail> page;
		private int pageSize;
		private String pageTitle;
		private int totalCount;
		private String totalPageCount;

		public String getCurrPage() {
			return currPage;
		}

		public void setCurrPage(String currPage) {
			this.currPage = currPage;
		}

		public List<MyBaskDetail> getPage() {
			return page;
		}

		public void setPage(List<MyBaskDetail> page) {
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

		public String getTotalPageCount() {
			return totalPageCount;
		}

		public void setTotalPageCount(String totalPageCount) {
			this.totalPageCount = totalPageCount;
		}
	}

	public class MyBaskDetail {
		private String check_time;
		private String content;
		private String entityId;
		private String forbid_reason;
		private String gnum;
		private String id;
		private String is_audit;
		private String is_dispaly;
		private String persistent;
		private String shareImg;
		private String share_like;
		private String share_read;
		private ShareTime share_time;
		private String share_time_uid;
		private String share_type;
		private String share_typeId;
		private String user_id;

		public String getCheck_time() {
			return check_time;
		}

		public void setCheck_time(String check_time) {
			this.check_time = check_time;
		}

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

		public String getForbid_reason() {
			return forbid_reason;
		}

		public void setForbid_reason(String forbid_reason) {
			this.forbid_reason = forbid_reason;
		}

		public String getGnum() {
			return gnum;
		}

		public void setGnum(String gnum) {
			this.gnum = gnum;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIs_audit() {
			return is_audit;
		}

		public void setIs_audit(String is_audit) {
			this.is_audit = is_audit;
		}

		public String getIs_dispaly() {
			return is_dispaly;
		}

		public void setIs_dispaly(String is_dispaly) {
			this.is_dispaly = is_dispaly;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getShareImg() {
			return shareImg;
		}

		public void setShareImg(String shareImg) {
			this.shareImg = shareImg;
		}

		public String getShare_like() {
			return share_like;
		}

		public void setShare_like(String share_like) {
			this.share_like = share_like;
		}

		public String getShare_read() {
			return share_read;
		}

		public void setShare_read(String share_read) {
			this.share_read = share_read;
		}

		public ShareTime getShare_time() {
			return share_time;
		}

		public void setShare_time(ShareTime share_time) {
			this.share_time = share_time;
		}

		public String getShare_time_uid() {
			return share_time_uid;
		}

		public void setShare_time_uid(String share_time_uid) {
			this.share_time_uid = share_time_uid;
		}

		public String getShare_type() {
			return share_type;
		}

		public void setShare_type(String share_type) {
			this.share_type = share_type;
		}

		public String getShare_typeId() {
			return share_typeId;
		}

		public void setShare_typeId(String share_typeId) {
			this.share_typeId = share_typeId;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
	}
}
