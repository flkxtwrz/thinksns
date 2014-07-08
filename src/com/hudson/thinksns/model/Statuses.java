package com.hudson.thinksns.model;

import java.io.Serializable;

public class Statuses implements Serializable {
	/**
	 * 
	 * @author ���ͳ�
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int feed_id;// ΢��id
	private User user;// ΢������
	private String publish_time;// ����ʱ��
	private String from;// ����ƽ̨
	private int comment_count;// ΢��������
	private int repost_count;// ΢��������
	private int digg_count;// ������
	private String feed_content;// ΢������
	private String feed_type;// ΢������
	private Statuses source_status;// ԭ΢��
    private String big_postimage_url;
    private String middle_postimage_url;
    private String small_postimage_url;
    private int has_attach;//�Ƿ��и���
    
	public int getHas_attach() {
		return has_attach;
	}

	public void setHas_attach(int has_attach) {
		this.has_attach = has_attach;
	}

	public int getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getRepost_count() {
		return repost_count;
	}

	public void setRepost_count(int repost_count) {
		this.repost_count = repost_count;
	}

	public int getDigg_count() {
		return digg_count;
	}

	public void setDigg_count(int digg_count) {
		this.digg_count = digg_count;
	}

	public String getFeed_content() {
		return feed_content;
	}

	public void setFeed_content(String feed_content) {
		this.feed_content = feed_content;
	}

	public String getFeed_type() {
		return feed_type;
	}

	public void setFeed_type(String feed_type) {
		this.feed_type = feed_type;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	

	public String getBig_postimage_url() {
		return big_postimage_url;
	}

	public void setBig_postimage_url(String big_postimage_url) {
		this.big_postimage_url = big_postimage_url;
	}

	public String getMiddle_postimage_url() {
		return middle_postimage_url;
	}

	public void setMiddle_postimage_url(String middle_postimage_url) {
		this.middle_postimage_url = middle_postimage_url;
	}

	public String getSmall_postimage_url() {
		return small_postimage_url;
	}

	public void setSmall_postimage_url(String small_postimage_url) {
		this.small_postimage_url = small_postimage_url;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statuses other = (Statuses) obj;
		if (feed_id != other.feed_id)
			return false;
		return true;
	}

	public Statuses getSource_status() {
		return source_status;
	}

	public void setSource_status(Statuses source_status) {
		this.source_status = source_status;
	}

	@Override
	public String toString() {
		return "Statuses [feed_id=" + feed_id + ", user=" + user
				+ ", publish_time=" + publish_time + ", from=" + from
				+ ", comment_count=" + comment_count + ", repost_count="
				+ repost_count + ", digg_count=" + digg_count
				+ ", feed_content=" + feed_content + ", feed_type=" + feed_type
				+ ", source_status=" + source_status + ", big_postimage_url="
				+ big_postimage_url + ", middle_postimage_url="
				+ middle_postimage_url + ", small_postimage_url="
				+ small_postimage_url + ", has_attach=" + has_attach + "]";
	}



}
