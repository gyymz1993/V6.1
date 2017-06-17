package com.lsjr.zizisteward.bean;

import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

public class InviteUsersBean {

	private Friends friends;
	private String error;
	private String msg;

	public Friends getFriends() {
		return friends;
	}

	public void setFriends(Friends friends) {
		this.friends = friends;
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

	public static class Friends {
		private List<Page> page;
		private String totalCount;
		
		public String getTotalCount() {
			return totalCount;
		}
		
		public void setTotalCount(String totalCount) {
			this.totalCount = totalCount;
		}
		
		public List<Page> getPage() {
			return page;
		}
		
		public void setPage(List<Page> page) {
			this.page = page;
		}
		
		public static class Page {
			private String reality_name;
			private String name;
			private String photo;
			private String user_name;
			private String id;
			
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

			public String getReality_name() {
				return reality_name;
			}

			public void setReality_name(String reality_name) {
				this.reality_name = reality_name;
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
			
		}
	}
}
