package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class MRBean {

	private String msg;
	private String error;
	private List<Tsomr> tsomr;

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

	public List<Tsomr> getTsomr() {
		return tsomr;
	}

	public void setTsomr(List<Tsomr> tsomr) {
		this.tsomr = tsomr;
	}

	public class Tsomr {
		private String id;
		private String sid;
		private String tshow;
		private String user_id;
		private String record_type;
		private String description;
		private record_time record_time;
		private int space;
		private int ly;

		public record_time getRecord_time() {
			return record_time;
		}

		public void setRecord_time(record_time record_time) {
			this.record_time = record_time;
		}

		public int getLy() {
			return ly;
		}

		public void setLy(int ly) {
			this.ly = ly;
		}

		public int getSpace() {
			return space;
		}

		public void setSpace(int space) {
			this.space = space;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getRecord_type() {
			return record_type;
		}

		public void setRecord_type(String record_type) {
			this.record_type = record_type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public class record_time {
			private String time;

			public String getTime() {
				return time;
			}

			public void setTime(String time) {
				this.time = time;
			}
		}
	}
}
