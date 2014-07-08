package com.hudson.thinksns.model;

import java.io.Serializable;
/**
 * 
 * @author 王代银
 * 
 */
public class CommentOfStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int comment_id;
	private String apptype;// 评论源的应用
	private int source_id;// 用于查找源的id
	private String Content;// 评论内容
	private String Ctime;// 评论创建时间
	private User comment_user;// 评论人的信息

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public int getSource_id() {
		return source_id;
	}

	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCtime() {
		return Ctime;
	}

	public void setCtime(String ctime) {
		Ctime = ctime;
	}

	public User getComment_user() {
		return comment_user;
	}

	public void setComment_user(User comment_user) {
		this.comment_user = comment_user;
	}

	@Override
	public String toString() {
		return "CommentOfStatus [comment_id=" + comment_id + ", apptype="
				+ apptype + ", source_id=" + source_id + ", Content=" + Content
				+ ", Ctime=" + Ctime + ", comment_user=" + comment_user + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentOfStatus other = (CommentOfStatus) obj;

		if (comment_id != other.comment_id)
			return false;

		return true;
	}

}
