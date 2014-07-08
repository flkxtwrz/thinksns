package com.hudson.thinksns.chating;
/** 
* 聊天内容实体类
* @author 贾焱超
*/  
public class MessageDetail 
{
	String from_uid = "";
	String content = "";
	String from_face = "";
	String ctime = "";
	String from_uname = "";
	
	public void set_from_uid(String uid)
	{
		this.from_uid = uid;
	}
	
	public void set_content(String con)
	{
		this.content = con;
	}
	
	public void set_from_face(String face)
	{
		this.from_face = face;
	}
	
	public void set_ctime(String time)
	{
		this.ctime = time;
	}
	
	public void set_from_uname(String name)
	{
		this.from_uname = name;
	}
}
