package com.hudson.thinksns.chating;
/** 
* 聊天朋友的实体类
* @author 石仲才
*/  
public class ChatFriends 
{
	private String uname = "";
	private String user_hander = "";
	private String uid = "";
	
	void set_uname(String name)
	{
		this.uname = name;
	}
	
	void set_user_hander(String hander)
	{
		this.user_hander = hander;
	}
	
	void set_uid(String id)
	{
		this.uid = id;
	}
	
	String get_uname()
	{
		return uname;
	}
	
	String get_uid()
	{
		return uid;
	}
	
	String get_user_hander()
	{
		return user_hander;
	}
}
