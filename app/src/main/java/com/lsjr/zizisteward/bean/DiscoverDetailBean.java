package com.lsjr.zizisteward.bean;

import java.util.List;

public class DiscoverDetailBean {
	private List<CommentsList> comments;
	private String error;
	private String msg;
	private Share share;
	private Shop shop;

	public List<CommentsList> getComments() {
		return comments;
	}

	public void setComments(List<CommentsList> comments) {
		this.comments = comments;
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

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public class CommentsList {
		private CommentTime comment_time;
		private String content;
		private String credit_level_id;
		private String entityId;
		private String id;
		private String persistent;
		private String photo;
		private String sid;
		private String sorder;
		private String user_id;
		private String user_name;

		public CommentTime getComment_time() {
			return comment_time;
		}

		public void setComment_time(CommentTime comment_time) {
			this.comment_time = comment_time;
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

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getSorder() {
			return sorder;
		}

		public void setSorder(String sorder) {
			this.sorder = sorder;
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

	public class Share {
		private CheckTime check_time;
		private String content;
		private String credit_level_id;
		private String entityId;
		private String forbid_reason;
		private String gnum;
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

	public class Shop {
		private String shopUrl;
		private String sname;
		private String spicfirst;
		private String sprice;

		public String getShopUrl() {
			return shopUrl;
		}

		public void setShopUrl(String shopUrl) {
			this.shopUrl = shopUrl;
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

		public String getSprice() {
			return sprice;
		}

		public void setSprice(String sprice) {
			this.sprice = sprice;
		}
	}

	public class CommentTime {
		private String date;
		private String day;
		private String hours;
		private String minutes;
		private String month;
		private String nanos;
		private String seconds;
		private long time;
		private String timezoneOffset;
		private String year;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getHours() {
			return hours;
		}

		public void setHours(String hours) {
			this.hours = hours;
		}

		public String getMinutes() {
			return minutes;
		}

		public void setMinutes(String minutes) {
			this.minutes = minutes;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getNanos() {
			return nanos;
		}

		public void setNanos(String nanos) {
			this.nanos = nanos;
		}

		public String getSeconds() {
			return seconds;
		}

		public void setSeconds(String seconds) {
			this.seconds = seconds;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public String getTimezoneOffset() {
			return timezoneOffset;
		}

		public void setTimezoneOffset(String timezoneOffset) {
			this.timezoneOffset = timezoneOffset;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}
	}

}
