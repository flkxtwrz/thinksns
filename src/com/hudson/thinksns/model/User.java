package com.hudson.thinksns.model;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 * @author ���ͳ�
	 * 
	 */
   private static final long serialVersionUID = 1L;
   private String uname;//�û���
   private int uid;//�û�id
   private String tag;//�û���ǩ
   private String sex;//�û��Ա�
   private String location;//�û����ڵ���
   private String headicon;//�û�ͷ��url
   private String email;//�û�����
   private String intro;//�û����
   private int following;//��ע��
   private int follower;//��˿��
   private int feedcount;//΢����
   private int checkcount;//ǩ������
   private int isFollowed;//�Ƿ��ѹ�ע 1-�ѹ�ע 0-δ��ע
public String getUname() {
	return uname;
}



public int getIsFollowed() {
	return isFollowed;
}



public String getTag() {
	return tag;
}



public void setTag(String tag) {
	this.tag = tag;
}



public void setIsFollowed(int isFollowed) {
	this.isFollowed = isFollowed;
}



public void setUname(String uname) {
	this.uname = uname;
}
public int getUid() {
	return uid;
}
public void setUid(int uid) {
	this.uid = uid;
}
public String getSex() {
	return sex;
}
public void setSex(String sex) {
	this.sex = sex;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public String getHeadicon() {
	return headicon;
}
public void setHeadicon(String headicon) {
	this.headicon = headicon;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getIntro() {
	return intro;
}
public void setIntro(String intro) {
	this.intro = intro;
}
public int getFollowing() {
	return following;
}
public void setFollowing(int following) {
	this.following = following;
}
public int getFollower() {
	return follower;
}
public void setFollower(int follower) {
	this.follower = follower;
}
public int getFeedcount() {
	return feedcount;
}
public void setFeedcount(int feedcount) {
	this.feedcount = feedcount;
}
public int getCheckcount() {
	return checkcount;
}
public void setCheckcount(int checkcount) {
	this.checkcount = checkcount;
}




@Override
public String toString() {
	return "User [uname=" + uname + ", uid=" + uid + ", tag=" + tag + ", sex="
			+ sex + ", location=" + location + ", headicon=" + headicon
			+ ", email=" + email + ", intro=" + intro + ", following="
			+ following + ", follower=" + follower + ", feedcount=" + feedcount
			+ ", checkcount=" + checkcount + ", isFollowed=" + isFollowed + "]";
}



@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	User other = (User) obj;

	if (uid != other.uid)
		return false;
	
	return true;
}

   
   
}
