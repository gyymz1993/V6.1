package com.lsjr.zizisteward.bean;

import java.util.List;

public class CircleBean {
	private String error;
	private String msg;
	private List<Circle> circle;

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

	public List<Circle> getCircle() {
		return circle;
	}

	public void setCircle(List<Circle> circle) {
		this.circle = circle;
	}

	public static class Circle {
		private String gnum;
		private String photo;
		private String name;
		private String user_id;
		private String user_name;
		private String share_type;
		private String shareImg;
		private Share_time share_time;
		private String share_time_uid;
		private String content;
		private String share_like;
		private String share_comment;
		private String id;
		private String img_wh;
		private boolean ly;
		
		public boolean isLy() {
			return ly;
		}
		
		public void setLy(boolean ly) {
			this.ly = ly;
		}
		
		public String getShare_time_uid() {
			return share_time_uid;
		}
		
		public void setShare_time_uid(String share_time_uid) {
			this.share_time_uid = share_time_uid;
		}
		
		public String getImg_wh() {
			return img_wh;
		}
		
		public void setImg_wh(String img_wh) {
			this.img_wh = img_wh;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public Share_time getShare_time() {
			return share_time;
		}
		
		public void setShare_time(Share_time share_time) {
			this.share_time = share_time;
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
		
		public String getContent() {
			return content;
		}
		
		public void setContent(String content) {
			this.content = content;
		}
		
		public String getGnum() {
			return gnum;
		}

		public void setGnum(String gnum) {
			this.gnum = gnum;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getShare_type() {
			return share_type;
		}

		public void setShare_type(String share_type) {
			this.share_type = share_type;
		}

		public String getShareImg() {
			return shareImg;
		}

		public void setShareImg(String shareImg) {
			this.shareImg = shareImg;
		}

		public static class Share_time {
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
