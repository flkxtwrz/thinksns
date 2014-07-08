package com.hudson.thinksns.model;

import java.io.Serializable;

public class Statuses implements Serializable {
	/**
	 * 
	 * @author 贾焱超
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int feed_id;// 微博id
	private User user;// 微博作者
	private String publish_time;// 发布时间
	private String from;// 发表平台
	private int comment_count;// 微博评论数
	private int repost_count;// 微博评论数
	private int digg_count;// 点赞数
	private String feed_content;// 微博内容
	private String feed_type;// 微博类型
	private Statuses source_status;// 原微博
    private String big_postimage_url;
    private String middle_postimage_url;
    private String small_postimage_url;
    private int has_attach;//是否含有附件
    
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
