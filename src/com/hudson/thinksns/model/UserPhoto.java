package com.hudson.thinksns.model;

import java.io.Serializable;

public class UserPhoto implements Serializable{
	/**
	 * 
	 * @author 贾焱超
	 * 
	 */
	private static final long serialVersionUID = 3881788620221389989L;
	private String photo_url;//图片的url地址
	private String photo_content;//发布微博图片是的文字
	private String photo_time;//发布时间
	
	public String getPhoto_url() {
		return photo_url;
	}
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	public String getPhoto_content() {
		return photo_content;
	}
	public void setPhoto_content(String photo_content) {
		this.photo_content = photo_content;
	}
	public String getPhoto_time() {
		return photo_time;
	}
	public void setPhoto_time(String photo_time) {
		this.photo_time = photo_time;
	}

	@Override
	public String toString() {
		return "UserPhoto [photo_url=" + photo_url + ", photo_content="
				+ photo_content + ", photo_time=" + photo_time + "]";
	}

}
