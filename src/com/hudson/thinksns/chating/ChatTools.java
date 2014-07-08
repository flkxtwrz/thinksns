package com.hudson.thinksns.chating;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Notify;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;

/** 
* 聊天工具类 
* @author 贾焱超
*/  
public class ChatTools
{
	int count = 20;
	int page = 1;
	String create_id;
	String reply_id;
	String delete_id;
	private Account account;
	ArrayList<ChatMessage> messageList;
	ArrayList<MessageDetail> detailList;
	ArrayList<ChatFriends> chatFriends;
	
	public ChatTools(Account a)
	{
		this.account = a;
	}
	
	/*
	 * 获取消息列表
	 * 直接调用
	 * 返回ArrayList<ChatMessage>
	 */
	public ArrayList<ChatMessage> getMessageList()
	{
		GetMessageThread getmessagethread = new GetMessageThread(account.getOauth_token(),account.getOauth_token_secret());
		getmessagethread.start();
		try 
		{
			getmessagethread.join();
		}
		catch(InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("getMessageList()运行完毕返回值ArrayList<ChatMessage>" + messageList.size());
		return messageList;
	}
	
	/*
	 * 获取可以聊天的好友列表
	 * 直接调用
	 * 返回ArrayList<ChatFriends>
	 */
	public ArrayList<ChatFriends> getChatFriend()
	{
		GetFriendThread getFriendThread = new GetFriendThread(account.getOauth_token(),account.getOauth_token_secret());
		getFriendThread.start();
		try 
		{
			getFriendThread.join();
		}
		catch(InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("getChatFriend()运行完毕返回值ArrayList<ChatFriends>" + chatFriends.size());
		return chatFriends;
	}
	/*
	 * 创造消息
	 * 直接调用输入用户id和内容
	 * 返回消息id
	 */
	public String createChat(int to_id,String contents)
	{
		CreateChatThread createChatThread = new CreateChatThread(account.getOauth_token(),account.getOauth_token_secret(),to_id,contents);
		createChatThread.start();
		try 
		{
			createChatThread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("createChat运行结果" + create_id);
		return create_id;
	}
	
	public String createChat(int to_id)
	{
		CreateChatThread createChatThread = new CreateChatThread(account.getOauth_token(),account.getOauth_token_secret(),to_id,"打个招呼>_<");
		createChatThread.start();
		try 
		{
			createChatThread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("createChat运行结果" + create_id);
		return create_id;
	}
	
	/*
	 * 回复消息
	 * 输入消息id和内容
	 * 返回消息id
	 */
	public String replyChat(int ms_id,String contents)
	{
		ReplyChatThread replyChatThread = new ReplyChatThread(account.getOauth_token(),account.getOauth_token_secret(),ms_id,contents);
		replyChatThread.start();
		try 
		{
			replyChatThread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("replyChat运行结果" + reply_id);
		return reply_id;
	}
	
	/*
	 * 删除消息
	 * 输入id
	 * 返回成功1/失败0
	 */
	public String deleteChat(int ms_id)
	{
		DeleteChatThread deleteChatThread = new DeleteChatThread(account.getOauth_token(),account.getOauth_token_secret(),ms_id);
		deleteChatThread.start();
		try 
		{
			deleteChatThread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("deleteChatThread运行结果" + delete_id);
		return delete_id;
	}
	
	/*
	 * 获取会话详情
	 */
	public ArrayList<MessageDetail> getChatDetail(int mid)
	{
		getDetailThread getdetailthread = new getDetailThread(account.getOauth_token(),account.getOauth_token_secret(),mid);
		getdetailthread.start();
		try 
		{
			getdetailthread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("detailList长度" + detailList.size());
		return detailList;
	}
	
	private class GetMessageThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		
		GetMessageThread(String a,String b)
		{
			this.oauth_token = a;
			this.token_secret = b;
		}
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Message");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_message_list");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			MyNameValuePair NameValuePair6 = new MyNameValuePair("count",
					String.valueOf((count)));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("page",
					String.valueOf((page)));

			page++;
			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6, NameValuePair7);
			System.out.println("GetMessageThread结果" + result);
			messageList = format(result);
		}
	}
	/*
	 * GetMessageThread格式
	 */
	private ArrayList<ChatMessage> format(String fmessage) 
	{
		ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

		try {
			JSONArray message_jsonarray = new JSONArray(fmessage);
			for (int i = 0; i < message_jsonarray.length(); i++) 
			{
				JSONObject message_jsonobject = message_jsonarray
						.getJSONObject(i);
				String from_uname = message_jsonobject.optString("from_uname");
				String list_id = message_jsonobject.optString("list_id");
				String from_face = message_jsonobject.optString("from_face");
				String last_message = message_jsonobject.optString("last_message");
				String last_time = message_jsonobject.optString("ctime");
				String from_uid = message_jsonobject.optString("from_uid");
				
				ChatMessage message = new ChatMessage();
				message.set_from_face(from_face);
				message.set_from_uname(from_uname);
				message.set_list_id(list_id);
				message.set_message_time(last_time);
				System.out.println("format.list_id" + list_id);
				message.set_last_message(last_message);
				//System.out.println("format.last_message" + list_id);

				messages.add(message);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}
	
	/*
	 * 创建对话
	 */
	private class CreateChatThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		int to_id;
		String content;
		
		CreateChatThread(String a,String b,int c,String d)
		{
			this.oauth_token = a;
			this.token_secret = b;
			this.to_id = c;
			this.content = d;
		}
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Message");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"create");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			MyNameValuePair NameValuePair6 = new MyNameValuePair("to_uid",
					String.valueOf((to_id)));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content",
					content);

			page++;
			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6, NameValuePair7);
			System.out.println("CreateChatThread结果" + result);
			create_id = result;
		}
	}
	
	/*
	 * 获取消息详细信息进程
	 */
	private class getDetailThread extends Thread
	{
		private String oauth_token = "";
		private String token_secret = "";
		private int message_id = 0;
		private int count = 20;
		private int page = 1;
		
		public getDetailThread(String a,String b,int c)
		{
			this.oauth_token = a;
			this.token_secret = b;
			this.message_id = c;
		}
		public void run()
		{
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Message");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_message_detail");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			MyNameValuePair NameValuePair6 = new MyNameValuePair("id",
					String.valueOf((message_id)));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("count",
					String.valueOf((count)));
			MyNameValuePair NameValuePair8 = new MyNameValuePair("page",
					String.valueOf((page)));
			
			page++;
			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6, NameValuePair7,NameValuePair8);
			System.out.println("getDetailThread结果" + result);
			detailList = format_for_detail(result);
		}
	}
	
	private ArrayList<MessageDetail> format_for_detail(String detail) 
	{
		ArrayList<MessageDetail> details = new ArrayList<MessageDetail>();

		try {
			JSONArray message_jsonarray = new JSONArray(detail);
			for (int i = 0; i < message_jsonarray.length(); i++) 
			{
				JSONObject message_jsonobject = message_jsonarray
						.getJSONObject(i);
				String from_uname = message_jsonobject.optString("from_uname");
				String from_face = message_jsonobject.optString("from_face");
				String from_uid = message_jsonobject.optString("from_uid");
				String ctime = message_jsonobject.optString("ctime");
				String content = message_jsonobject.optString("content");
		
				MessageDetail mdetail = new MessageDetail();
				mdetail.set_content(content);
				mdetail.set_ctime(ctime);
				mdetail.set_from_face(from_face);
				mdetail.set_from_uid(from_uid);
				mdetail.set_from_uname(from_uname);

				details.add(mdetail);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return details;
	}
	
	/*
	 * 回复对话进程
	 */
	private class ReplyChatThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		int ms_id;
		String content;
		
		ReplyChatThread(String a,String b,int c,String d)
		{
			this.oauth_token = a;
			this.token_secret = b;
			this.ms_id = c;
			this.content = d;
		}
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Message");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"reply");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			MyNameValuePair NameValuePair6 = new MyNameValuePair("id",
					String.valueOf((ms_id)));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content",
					content);

			//page++;
			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6, NameValuePair7);
			System.out.println("ReplyChatThread结果" + result);
			reply_id = result;
		}
	}
	/*
	 * 
	 */
	private class DeleteChatThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		int ms_id;
		
		DeleteChatThread(String a,String b,int c)
		{
			this.oauth_token = a;
			this.token_secret = b;
			this.ms_id = c;
		}
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Message");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"destroy");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			MyNameValuePair NameValuePair6 = new MyNameValuePair("list_id",
					String.valueOf((ms_id)));

			//page++;
			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			System.out.println("ReplyChatThread结果" + result);
			delete_id = result;
		}
	}
	
	/*
	 * 获取朋友列表
	 */
	private class GetFriendThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		
		GetFriendThread(String a,String b)
		{
			this.oauth_token = a;
			this.token_secret = b;
		}
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_friends");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			//MyNameValuePair NameValuePair6 = new MyNameValuePair(
			//		"count", token_secret); 

			//page++;
			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			System.out.println("GetFriendThread结果" + result);
			chatFriends = format_for_chatfriend(result);
		}
	}
	
	private ArrayList<ChatFriends> format_for_chatfriend(String friend) 
	{
		ArrayList<ChatFriends> friends = new ArrayList<ChatFriends>();

		try {
			JSONArray friends_jsonarray = new JSONArray(friend);
			for (int i = 0; i < friends_jsonarray.length(); i++) 
			{
				JSONObject friends_jsonobject = friends_jsonarray
						.getJSONObject(i);
				String uname = friends_jsonobject.optString("uname");
				String face = friends_jsonobject.optString("avatar_middle");
				String uid = friends_jsonobject.optString("uid");
		
				ChatFriends fri = new ChatFriends();
				fri.set_uid(uid);
				fri.set_uname(uname);
				fri.set_user_hander(face);

				friends.add(fri);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friends;
	}
}
