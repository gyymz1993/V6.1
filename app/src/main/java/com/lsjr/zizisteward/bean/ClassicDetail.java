package com.lsjr.zizisteward.bean;

public class ClassicDetail {
	private String class_types;
	private String description;
	private String entityId;
	private String id;
	private String name;
	private String parent_id;
	private String persistent;
	private String status;
	private boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public ClassicDetail() {
		super();
	}

	public ClassicDetail(boolean isChecked) {
		super();
		this.isChecked = isChecked;
	}

	public String getClass_types() {
		return class_types;
	}

	public void setClass_types(String class_types) {
		this.class_types = class_types;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getPersistent() {
		return persistent;
	}

	public void setPersistent(String persistent) {
		this.persistent = persistent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
