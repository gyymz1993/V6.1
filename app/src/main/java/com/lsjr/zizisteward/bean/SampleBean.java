package com.lsjr.zizisteward.bean;

import java.util.List;

public class SampleBean {
	private String error;
	private String msg;
	private SamplePage page;
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

	public SamplePage getPage() {
		return page;
	}

	public void setPage(SamplePage page) {
		this.page = page;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public class SamplePage {
		private String currPage;
		private List<SampleDetail> page;
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

		public List<SampleDetail> getPage() {
			return page;
		}

		public void setPage(List<SampleDetail> page) {
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

	public class SampleDetail {
		private String aid;
		private String category;
		private String entityId;
		private String id;
		private String is_display;
		private int lose_time;
		private String persistent;
		private String push_content;
		private String push_link;
		private PushTime push_time;
		private String steward_name;
		private String title;
		private String user_id;

		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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

		public String getIs_display() {
			return is_display;
		}

		public void setIs_display(String is_display) {
			this.is_display = is_display;
		}

		public int getLose_time() {
			return lose_time;
		}

		public void setLose_time(int lose_time) {
			this.lose_time = lose_time;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getPush_content() {
			return push_content;
		}

		public void setPush_content(String push_content) {
			this.push_content = push_content;
		}

		public String getPush_link() {
			return push_link;
		}

		public void setPush_link(String push_link) {
			this.push_link = push_link;
		}

		public PushTime getPush_time() {
			return push_time;
		}

		public void setPush_time(PushTime push_time) {
			this.push_time = push_time;
		}

		public String getSteward_name() {
			return steward_name;
		}

		public void setSteward_name(String steward_name) {
			this.steward_name = steward_name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
	}

	public class PushTime {
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
