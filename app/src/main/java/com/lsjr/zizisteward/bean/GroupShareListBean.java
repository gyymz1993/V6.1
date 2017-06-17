package com.lsjr.zizisteward.bean;

import java.util.List;

public class GroupShareListBean {

	private String error;
	private String msg;
	private List<GroupShareList> GroupShareList;

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

	public List<GroupShareList> getGroupShareList() {
		return GroupShareList;
	}

	public void setGroupShareList(List<GroupShareList> groupShareList) {
		GroupShareList = groupShareList;
	}

	public static class GroupShareList {
		private String fid;
		private String flock_title;
		private String id;
		private String img_wh;
		private String is_dispaly;
		private String photo;
		private String user_id;
		private String share_content;
		private Stime stime;
		private boolean ly;
		
		public boolean isLy() {
			return ly;
		}
		
		public void setLy(boolean ly) {
			this.ly = ly;
		}
		
		public Stime getStime() {
			return stime;
		}
		
		public void setStime(Stime stime) {
			this.stime = stime;
		}

		public String getFid() {
			return fid;
		}

		public void setFid(String fid) {
			this.fid = fid;
		}

		public String getFlock_title() {
			return flock_title;
		}

		public void setFlock_title(String flock_title) {
			this.flock_title = flock_title;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImg_wh() {
			return img_wh;
		}

		public void setImg_wh(String img_wh) {
			this.img_wh = img_wh;
		}

		public String getIs_dispaly() {
			return is_dispaly;
		}

		public void setIs_dispaly(String is_dispaly) {
			this.is_dispaly = is_dispaly;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getShare_content() {
			return share_content;
		}

		public void setShare_content(String share_content) {
			this.share_content = share_content;
		}
		
		public static class Stime {
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
