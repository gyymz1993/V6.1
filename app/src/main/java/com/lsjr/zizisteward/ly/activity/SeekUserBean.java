package com.lsjr.zizisteward.ly.activity;

import java.util.List;

public class SeekUserBean {
	private List<SeekUser> seekUser;

	public List<SeekUser> getSeekUser() {
		return seekUser;
	}
	
	public void setSeekUser(List<SeekUser> seekUser) {
		this.seekUser = seekUser;
	}
	
	public static class SeekUser {
		private String type;
		private User user;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public static class User {
			private String credit_level_id;
			private String name;
			private String reality_name;
			private String score;
			private String user_name;
			private String id;
			private String photo;

			public String getCredit_level_id() {
				return credit_level_id;
			}

			public void setCredit_level_id(String credit_level_id) {
				this.credit_level_id = credit_level_id;
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

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
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
