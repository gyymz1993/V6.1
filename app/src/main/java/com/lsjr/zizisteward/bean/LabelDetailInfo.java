package com.lsjr.zizisteward.bean;

public class LabelDetailInfo {
    private String classify_type;
    private String description;
    private String entityId;
    private String id;
    private boolean persistent;
    private String tgrade;
    private String tname;
    private String tpath;
    private String tpid;
    private String tshow;
    private String type_icon;
    private String type_icons;
    private String type_img;
    private boolean isSelectd;

    public boolean isSelectd() {
        return isSelectd;
    }

    public void setSelectd(boolean selectd) {
        isSelectd = selectd;
    }


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

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
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
