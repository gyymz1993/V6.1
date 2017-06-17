package com.lsjr.zizisteward.bean;

import java.util.List;

public class FoodHomeBean {

	private String error;
	private String msg;
	private List<Cate> cate;
	private List<Count> count;
	private List<Style> style;
	private List<Catebanner> Catebanner;
	
	public List<Catebanner> getCatebanner() {
		return Catebanner;
	}
	
	public void setCatebanner(List<Catebanner> catebanner) {
		Catebanner = catebanner;
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

	public List<Cate> getCate() {
		return cate;
	}

	public void setCate(List<Cate> cate) {
		this.cate = cate;
	}

	public List<Count> getCount() {
		return count;
	}

	public void setCount(List<Count> count) {
		this.count = count;
	}

	public List<Style> getStyle() {
		return style;
	}

	public void setStyle(List<Style> style) {
		this.style = style;
	}

	public static class Catebanner {
		private String id;
		private String spic;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSpic() {
			return spic;
		}

		public void setSpic(String spic) {
			this.spic = spic;
		}

	}

	public static class Cate {

		private String areas_name;
		private String city_name;
		private String expert;
		private String id;
		private String popularity;
		private String sname;
		private String spic;
		private String sinfo;
		private String skeyword;
		private List<Comment> comment;

		public String getSkeyword() {
			return skeyword;
		}
		
		public void setSkeyword(String skeyword) {
			this.skeyword = skeyword;
		}
		
		public String getSinfo() {
			return sinfo;
		}

		public void setSinfo(String sinfo) {
			this.sinfo = sinfo;
		}

		public String getAreas_name() {
			return areas_name;
		}

		public void setAreas_name(String areas_name) {
			this.areas_name = areas_name;
		}

		public String getCity_name() {
			return city_name;
		}

		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}

		public String getExpert() {
			return expert;
		}

		public void setExpert(String expert) {
			this.expert = expert;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPopularity() {
			return popularity;
		}

		public void setPopularity(String popularity) {
			this.popularity = popularity;
		}

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}

		public String getSpic() {
			return spic;
		}

		public void setSpic(String spic) {
			this.spic = spic;
		}

		public List<Comment> getComment() {
			return comment;
		}

		public void setComment(List<Comment> comment) {
			this.comment = comment;
		}

		public static class Comment {
			private String photo;

			public String getPhoto() {
				return photo;
			}

			public void setPhoto(String photo) {
				this.photo = photo;
			}
		}
	}

	public static class Count {

		private String ad_areas_id;
		private String areas_name;
		private String count;

		public String getAd_areas_id() {
			return ad_areas_id;
		}

		public void setAd_areas_id(String ad_areas_id) {
			this.ad_areas_id = ad_areas_id;
		}

		public String getAreas_name() {
			return areas_name;
		}

		public void setAreas_name(String areas_name) {
			this.areas_name = areas_name;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

	}

	public static class Style {

		private String classify_type;
		private String description;
		private String entityId;
		private String id;
		private String is_hobby;
		private String persistent;
		private String tgrade;
		private String tname;
		private String tpath;
		private String tpid;
		private String tshow;
		private String type_icon;
		private String type_icons;
		private String type_img;

		public String getClassify_type() {
			return classify_type;
		}

		public void setClassify_type(String classify_type) {
			this.classify_type = classify_type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public String getIs_hobby() {
			return is_hobby;
		}

		public void setIs_hobby(String is_hobby) {
			this.is_hobby = is_hobby;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getTgrade() {
			return tgrade;
		}

		public void setTgrade(String tgrade) {
			this.tgrade = tgrade;
		}

		public String getTname() {
			return tname;
		}

		public void setTname(String tname) {
			this.tname = tname;
		}

		public String getTpath() {
			return tpath;
		}

		public void setTpath(String tpath) {
			this.tpath = tpath;
		}

		public String getTpid() {
			return tpid;
		}

		public void setTpid(String tpid) {
			this.tpid = tpid;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

		public String getType_icon() {
			return type_icon;
		}

		public void setType_icon(String type_icon) {
			this.type_icon = type_icon;
		}

		public String getType_icons() {
			return type_icons;
		}

		public void setType_icons(String type_icons) {
			this.type_icons = type_icons;
		}

		public String getType_img() {
			return type_img;
		}

		public void setType_img(String type_img) {
			this.type_img = type_img;
		}

	}
}
