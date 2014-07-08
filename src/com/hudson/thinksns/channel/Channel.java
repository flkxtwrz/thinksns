package com.hudson.thinksns.channel;
/** 
* 频道实体类
* @author 王代银
*/  
public class Channel 
{
	private String title = "";
	private String channel_category_id = "";
	private String pid = "";
	private String sort = "";
	private String icon_url = "";
	
	void set_title(String ti)
	{
		this.title = ti;
	}
	
	void set_channel_category_id(String id)
	{
		this.channel_category_id = id;
	}
	
	void set_pid(String pi)
	{
		this.pid = pi;
	}
	
	void set_icon_url(String ic)
	{
		this.icon_url = ic;
	}
	
	void set_sort(String ic)
	{
		this.sort = ic;
	}
	
	String get_title()
	{
		return title;
	}
	
	String get_channel_category_id()
	{
		return channel_category_id;
	}
	
	String get_pid()
	{
		return pid;
	}
	
	String get_sort()
	{
		return sort;
	}
	
	String get_icon_url()
	{
		return icon_url;
	}
}