package com.lsjr.zizisteward.ly.activity;

import java.util.List;

import android.net.Uri;

public class SCFBean {
    private List<ImgBean> img_bean;

    public List<ImgBean> getImg_bean() {
        return img_bean;
    }

    public void setImg_bean(List<ImgBean> img_bean) {
        this.img_bean = img_bean;
    }

    public static class ImgBean {
        private String path;
        private int type;
        private Uri uri;
        private String new_path;

        public String getNew_path() {
            return new_path;
        }

        public void setNew_path(String new_path) {
            this.new_path = new_path;
        }

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
