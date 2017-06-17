package com.lsjr.zizisteward.bean;

import java.util.List;

public class FriendDataBean {
	private String error;
	private String msg;
	private String name;
	private String photo;
	private String reality_name;
	private String score;
	private String user_name;
	private String credit_level_id;
	private String friendRemark;
	private List<FriendsDetail> friendsDetail;

	public String getFriendRemark() {
		return friendRemark;
	}
	
	public void setFriendRemark(String friendRemark) {
		this.friendRemark = friendRemark;
	}
	
	public String getCredit_level_id() {
		return credit_level_id;
	}
	
	public void setCredit_level_id(String credit_level_id) {
		this.credit_level_id = credit_level_id;
	}
	
	public String getReality_name() {
		return reality_name;
	}

	public void setReality_name(String reality_name) {
		this.reality_name = reality_name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getName() {
		return name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public List<FriendsDetail> getFriendsDetail() {
		return friendsDetail;
	}

	public void setFriendsDetail(List<FriendsDetail> friendsDetail) {
		this.friendsDetail = friendsDetail;
	}

	public static class FriendsDetail {
		private String photo;
		private String id;
		private String name;
		private String reality_name;
		private String shareImg;
		private String user_id;
		private String entityId;
		private String user_name;

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

	}
}
