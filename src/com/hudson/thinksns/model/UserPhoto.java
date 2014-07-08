package com.hudson.thinksns.model;

import java.io.Serializable;

public class UserPhoto implements Serializable{
	/**
	 * 
	 * @author ���ͳ�
	 * 
	 */
	private static final long serialVersionUID = 3881788620221389989L;
	private String photo_url;//ͼƬ��url��ַ
	private String photo_content;//����΢��ͼƬ�ǵ�����
	private String photo_time;//����ʱ��
	
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
