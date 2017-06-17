package com.lsjr.zizisteward.bean;

public class CircleFoundBean {
	private String error;
	private String msg;
	private Circle circle;

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

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	public static class Circle {
		private String content;
		private String id;
		private String user_name;
		private String user_id;
		private String photo;
		private String shareImg;
		private String share_time_uid;
		private String img_wh;
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		public String getShareImg() {
			return shareImg;
		}
		public void setShareImg(String shareImg) {
			this.shareImg = shareImg;
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
	}
}
