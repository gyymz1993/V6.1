package com.lsjr.zizisteward.bean;

import java.util.List;

public class BaskListBean {
	private String error;
	private String msg;
	private BaskListPage page;
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

	public BaskListPage getPage() {
		return page;
	}

	public void setPage(BaskListPage page) {
		this.page = page;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public class BaskListPage {

		private String currPage;
		private List<BaskListDetail> page;
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

		public List<BaskListDetail> getPage() {
			return page;
		}

		public void setPage(List<BaskListDetail> page) {
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

	public class BaskListDetail {
		private CheckTime check_time;
		private String content;
		private String credit_level_id;
		private String entityId;
		private String forbid_reason;
		private String id;
		private String is_audit;
		private String is_dispaly;
		private String name;
		private String persistent;
		private String photo;
		private String reality_name;
		private String shareImg;
		private String share_comment;
		private String share_like;
		private String share_read;
		private ShareTime share_time;
		private String share_time_uid;
		private String share_type;
		private String share_typeId;
		private String spicfirst;
		private String user_id;
		private String user_name;

		public CheckTime getCheck_time() {
			return check_time;
		}

		public void setCheck_time(CheckTime check_time) {
			this.check_time = check_time;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCredit_level_id() {
			return credit_level_id;
		}

		public void setCredit_level_id(String credit_level_id) {
			this.credit_level_id = credit_level_id;
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getReality_name() {
			return reality_name;
		}

		public void setReality_name(String reality_name) {
			this.reality_name = reality_name;
		}

		public String getShareImg() {
			return shareImg;
		}

		public void setShareImg(String shareImg) {
			this.shareImg = shareImg;
		}

		public String getShare_comment() {
			return share_comment;
		}

		public void setShare_comment(String share_comment) {
			this.share_comment = share_comment;
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

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
	}

}
