package com.hudson.thinksns.channel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.develop.DateToFragment;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/** 
* 频道工具类
* @author 贾焱超
*/  
public class ChannelTools 
{
	ArrayList<Channel> channels;
	private Account account;
	private ArrayList<Statuses> mstatuses;
	private final int num = 5;
	private int page = 1;
	private int state = 0;
	
	public ChannelTools(Account a)
	{
		this.account = a;
	}
	
	public ArrayList<Channel> GetChannel()
	{
		GetChannelThread getChannelThread = new GetChannelThread(account.getOauth_token(),account.getOauth_token_secret());
		getChannelThread.start();
		try 
		{
			getChannelThread.join();
		}
		catch(InterruptedException e) 
		{
			e.printStackTrace();
		}
		//System.out.println("GetChannel() ^^^^^^^  OK:" + channels.get(0).get_title());
		return channels;
	}
	
	public ArrayList<Statuses> GetChannelWeb(String id)
	{
		GetChannelWebThread getChannelWeb = new GetChannelWebThread(account.getOauth_token(),account.getOauth_token_secret(),id);
		getChannelWeb.start();
		try 
		{
			getChannelWeb.join();
		}
		catch(InterruptedException e) 
		{
			e.printStackTrace();
		}
		return mstatuses;
	}
	
	private class GetChannelThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		
		GetChannelThread(String a,String b)
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
					"Channel");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_all_channel");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token); //account.getOauth_token()
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", token_secret); //account.getOauth_token_secret()
			MyNameValuePair NameValuePair6 = new MyNameValuePair(
					"format", "php");

			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			System.out.println("GetChannelThread结果" + result);
			channels = format(result);
		}
	}
	/*
	 * GetMessageThread格式
	 */
	private ArrayList<Channel> format(String fmessage) 
	{
		ArrayList<Channel> channels = new ArrayList<Channel>();

		//String pan_title = "\"title\":\"(.*?)\",\"pid\":";
		String pan_title = "'title' => '(.*?)',";
		Pattern pp_title =Pattern.compile(pan_title);
		Matcher mm_title = pp_title.matcher(fmessage);
		ArrayList<String> title = new ArrayList<String>();
		while(mm_title.find())
		{
			char[]c=mm_title.group(1).toCharArray();
			title.add(new String(c));
		}
		
		//String pan_id = "\"channel_category_id\":\"(.*?)\",\"title\"";
		String pan_id = "'channel_category_id' => '(.*?)',";
		Pattern pp_id = Pattern.compile(pan_id);
		Matcher mm_id = pp_id.matcher(fmessage);
		ArrayList<String> id = new ArrayList<String>();
		while(mm_id.find())
		{
			id.add(mm_id.group(1));
		}
		
//		String pan_ic = "'icon_url' => (.*?),";
//		Pattern pp_ic = Pattern.compile(pan_ic);
//		Matcher mm_ic = pp_ic.matcher(fmessage);
//		ArrayList<String> ic = new ArrayList<String>();
//		while(mm_ic.find())
//		{
//			ic.add(mm_id.group(1));
//		}
		
		for(int i = 0; i < title.size(); i++)
		{
			Channel message = new Channel();
			message.set_channel_category_id(id.get(i));
			message.set_title(title.get(i));
			System.out.println(title.get(i));
			System.out.println(id.get(i));
			channels.add(message);
		}
//		try {
//			JSONArray channel_jsonarray = new JSONArray("["+fmessage+"]");
//			
//			for (int i = 0; i < channel_jsonarray.length(); i++) 
//			{
//				JSONObject channel_jsonobject = channel_jsonarray
//						.getJSONObject(i);
//				String channel_category_id = channel_jsonobject.optString("channel_category_id");
//				String title = channel_jsonobject.optJSONObject(String.valueOf(i)).optString("title");
//				String pid = channel_jsonobject.optJSONObject(String.valueOf(i)).optString("pid");
//				String sort = channel_jsonobject.optJSONObject(String.valueOf(i)).optString("sort");
//				String icon_url = channel_jsonobject.optJSONObject(String.valueOf(i)).optString("icon_url");
//				
//				Channel message = new Channel();
//				message.set_channel_category_id(channel_category_id);
//				message.set_icon_url(icon_url);
//				message.set_pid(pid);
//				message.set_sort(sort);
//				//message.set_title(title);
//
//				System.out.println("GetChannelThread format : " + title);
//				channels.add(message);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return channels;
	}
	
	private class GetChannelWebThread extends Thread 
	{
		String oauth_token = "";
		String token_secret = "";
		String category_id = "";
		
		GetChannelWebThread(String a,String b,String c)
		{
			this.oauth_token = a;
			this.token_secret = b;
			this.category_id = c;
		}
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Channel");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", "get_channel_feed");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					oauth_token);
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					token_secret);
			MyNameValuePair NameValuePair7 = new MyNameValuePair("count",
					String.valueOf(num));
			MyNameValuePair NameValuePair8 = new MyNameValuePair("page",
					String.valueOf((page)));
			MyNameValuePair NameValuePair9 = new MyNameValuePair("category_id",
					category_id);

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7, NameValuePair8);

			page++;
			mstatuses = format_for_web(result);
		}

	}
	
	private ArrayList<Statuses> format_for_web(String weibostr) {

		ArrayList<Statuses> statuses = new ArrayList<Statuses>();
		JSONArray statuses_jsonarray;
		try {
			statuses_jsonarray = new JSONArray(weibostr);
			Log.e("json微博个数：", "" + statuses_jsonarray.length());
			for (int i = 0; i < statuses_jsonarray.length(); i++) {
				JSONObject status_jsonobject = statuses_jsonarray
						.getJSONObject(i);
				int feed_id = Integer.parseInt(status_jsonobject
						.optString("feed_id"));
				String publish_time = status_jsonobject
						.optString("publish_time");
				String from = status_jsonobject.optString("from");
				String feed_type = status_jsonobject.optString("type");
				int comment_count = Integer.parseInt(status_jsonobject
						.optString("comment_count", "0"));
				int repost_count = Integer.parseInt(status_jsonobject
						.optString("repost_count", "0"));
				int digg_count = Integer.parseInt(status_jsonobject.optString(
						"digg_count", "0"));
				String feed_content = status_jsonobject
						.optString("feed_content");
				int uid = Integer.parseInt(status_jsonobject.optString("uid"));
				String uname = status_jsonobject.optString("uname");
				String headicon = status_jsonobject.optString("avatar_small");
				Statuses status = new Statuses();
				status.setFeed_type(feed_type);
				if (feed_type.equals("postimage")) {
					// 上传图片微博
					String bigpic_url = status_jsonobject
							.getJSONArray("attach").getJSONObject(0)
							.optString("attach_url");
					String middle_pic_url = status_jsonobject
							.getJSONArray("attach").getJSONObject(0)
							.optString("attach_middle");
					String small_pic_url = status_jsonobject
							.getJSONArray("attach").getJSONObject(0)
							.optString("attach_small");
					status.setBig_postimage_url(bigpic_url);
					status.setMiddle_postimage_url(middle_pic_url);
					status.setSmall_postimage_url(small_pic_url);
				}
				Statuses source_status = new Statuses();
				if (status_jsonobject.optJSONObject("api_source") != null) {
					JSONObject source_status_jsonobject = status_jsonobject
							.optJSONObject("api_source");
					int source_feed_id = Integer
							.parseInt(source_status_jsonobject
									.optString("feed_id"));
					String source_publish_time = source_status_jsonobject
							.optString("publish_time");
					String source_from = source_status_jsonobject
							.optString("from");
					String source_feed_type = status_jsonobject
							.optString("type");
					int source_comment_count = Integer
							.parseInt(source_status_jsonobject.optString(
									"comment_count", "0"));
					int source_repost_count = Integer
							.parseInt(source_status_jsonobject.optString(
									"repost_count", "0"));
					int source_digg_count = Integer
							.parseInt(source_status_jsonobject.optString(
									"digg_count", "0"));
					String source_feed_content = source_status_jsonobject
							.optString("feed_content");
					int source_uid = Integer.parseInt(source_status_jsonobject
							.optString("uid", "-1"));
					String source_uname = source_status_jsonobject
							.optString("uname");
					String source_headicon = source_status_jsonobject
							.optString("avatar_small");
					if (source_feed_type.equals("postimage")) {
						// 上传图片微博
						String bigpic_url = source_status_jsonobject
								.getJSONArray("attach").getJSONObject(0)
								.optString("attach_url");
						String middle_pic_url = source_status_jsonobject
								.getJSONArray("attach").getJSONObject(0)
								.optString("attach_middle");
						String small_pic_url = source_status_jsonobject
								.getJSONArray("attach").getJSONObject(0)
								.optString("attach_small");
						source_status.setBig_postimage_url(bigpic_url);
						source_status.setMiddle_postimage_url(middle_pic_url);
						source_status.setSmall_postimage_url(small_pic_url);
					}
					User source_user = new User();
					source_user.setUid(source_uid);
					source_user.setUname(source_uname);
					source_user.setHeadicon(source_headicon);
					source_status.setFeed_id(source_feed_id);
					source_status.setPublish_time(source_publish_time);
					source_status.setFrom(source_from);
					source_status.setComment_count(source_comment_count);
					source_status.setRepost_count(source_repost_count);
					source_status.setDigg_count(source_digg_count);
					source_status.setFeed_content(source_feed_content);
					source_status.setFeed_type(source_feed_type);
					source_status.setUser(source_user);
				}
				User user = new User();
				user.setUid(uid);
				user.setUname(uname);
				user.setHeadicon(headicon);

				status.setFeed_id(feed_id);
				status.setPublish_time(publish_time);
				status.setFrom(from);
				status.setComment_count(comment_count);
				status.setRepost_count(repost_count);
				status.setDigg_count(digg_count);
				status.setFeed_content(feed_content);
				status.setUser(user);
				status.setSource_status(source_status);
				statuses.add(status);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("huode微博个数：", "" + statuses.size());
		return statuses;

	}
}
