package com.lsjr.zizisteward.bean;

import java.io.Serializable;
import java.util.List;

public class ImageBucket implements Serializable {
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public List<ImageItem> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageItem> imageList) {
		this.imageList = imageList;
	}

}
