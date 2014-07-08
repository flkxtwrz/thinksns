package com.hudson.thinksns.chating;
/** 
* 聊天内容的实体类
* @author 王代银
*/  
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatMessage 
{
	private String from_uname = "";
	private String list_id = "";
	private String from_face = "";
	private String last_message = "";
	private String message_time = "";
	private String last_from_uid = "";
	private String last_content = "";
	private String last_to_uid = "";
	
	void set_message_time(String time)
	{
		this.message_time = time;
	}
	
	void set_from_uname(String name)
	{
		this.from_uname = name;
	}
	
	void set_list_id(String id)
	{
		this.list_id = id;
	}
	
	void set_from_face(String face)
	{
		this.from_face = face;
	}
	
	void set_last_message(String message)
	{
		String pan = "\"content\":\"(.*?)\",\"to_uid";
		Pattern pp =Pattern.compile(pan);
		Matcher mm = pp.matcher(message);
		while(mm.find())
		{
			System.out.println(mm.group(1));
			this.last_content = mm.group(1);
		}
		
//		String pan1 = "\"from_uid\":(.*?),\"content";
//		Pattern pp1 =Pattern.compile(pan1);
//		Matcher mm1 = pp1.matcher(message);
//		while(mm1.find())
//		{
//			System.out.println(mm1.group(1)+"QAQ");
//			this.last_from_uid = mm1.group(1);
//		}
//		try 
//		{
//			JSONArray message_jsonarray = new JSONArray(message);
//			for (int i = 0; i < message_jsonarray.length(); i++) 
//			{
//				JSONObject message_jsonobject = message_jsonarray.getJSONObject(i);
//				this.last_from_uid = message_jsonobject.optString("from_uid");
//				this.last_content = message_jsonobject.optString("content");
//				String aaa = message_jsonobject.optString("to_uid");
//				System.out.println("ChatMessage:last_content" + last_content);
//			}
//		} 
//		catch (JSONException e) 
//		{
//			e.printStackTrace();
//		}
	}
	
	String get_last_content()
	{
		return last_content;
	}
	
	String get_from_uname()
	{
		return from_uname;
	}
	
	String get_from_face()
	{
		return from_face;
	}
	
	String get_last_from_uid()
	{
		return last_from_uid;
	}
	
	String get_message_time()
	{
		return message_time;
	}
	String get_list_id()
	{
		return list_id;
	}
}
