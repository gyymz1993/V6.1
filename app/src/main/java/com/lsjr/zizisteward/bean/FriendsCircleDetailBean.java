package com.lsjr.zizisteward.bean;

import java.util.List;

public class FriendsCircleDetailBean {
	private String error;
	private String msg;
	private Share share;
	private Shop shop;
	private List<Comments> comments;

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

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public static class Comments {
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
		private Comment_Time comment_time;

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

		public Comment_Time getComment_time() {
			return comment_time;
		}

		public void setComment_time(Comment_Time comment_time) {
			this.comment_time = comment_time;
		}

		public static class Comment_Time {
			private String time;

			public String getTime() {
				return time;
			}

			public void setTime(String time) {
				this.time = time;
			}
		}
	}

	public static class Share {
		/** 朋友圈内容 */
		private String content;
		private String gnum;
		private String id;
		private String name;
		private String photo;
		private String shareImg;
		private String share_like;
		private Share_time share_time;
		private String userId;
		private String user_id;
		private String img_wh;
		/** 用户昵称 */
		private String user_name;
		private String share_time_uid;
		
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

		public String getShare_like() {
			return share_like;
		}

		public void setShare_like(String share_like) {
			this.share_like = share_like;
		}

		public Share_time getShare_time() {
			return share_time;
		}

		public void setShare_time(Share_time share_time) {
			this.share_time = share_time;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
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

	public static class Shop {
		private String shopUrl;
		private String sname;
		private String spicfirst;
		private String sprice;
		private String sid;
		
		public String getSid() {
			return sid;
		}
		
		public void setSid(String sid) {
			this.sid = sid;
		}

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
}
