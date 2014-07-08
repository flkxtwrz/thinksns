package com.hudson.thinksns.thread;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * 
 * @author 石仲才
 * 
 */
public class GetStatuesThread extends Thread{
 private Handler mHandler;
 private Account account;
 private final int getweibo=0x11;
public GetStatuesThread(Handler mHandler,Account account) {
	this.mHandler = mHandler;
	this.account=account;
}

@Override
public void run() {
	// TODO Auto-generated method stub
	MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
	MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
			"WeiboStatuses");
	MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
			"public_timeline");
	MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
			account.getOauth_token());
	MyNameValuePair NameValuePair5 = new MyNameValuePair(
			"oauth_token_secret", account.getOauth_token_secret());
//	MyNameValuePair NameValuePair6 = new MyNameValuePair(
//			"user_id", String.valueOf(ac.getUid()));

	String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
			NameValuePair1, NameValuePair2, NameValuePair3,
			NameValuePair4, NameValuePair5);
	Log.e("re", result);
	Message msg = new Message();
	msg.what = getweibo;
	msg.obj = result;
	mHandler.sendMessage(msg);
}
public ArrayList<Statuses> format( String weibostr) {
	 ArrayList<Statuses> statuses=new ArrayList<Statuses>() ;
	   JSONArray statuses_jsonarray;
	try {
		statuses_jsonarray = new JSONArray(weibostr);
		   for(int i=0;i<statuses_jsonarray.length();i++){
			   JSONObject status_jsonobject=statuses_jsonarray.getJSONObject(i);
			   int feed_id=Integer.parseInt(status_jsonobject.optString("feed_id"));
			   String publish_time=status_jsonobject.optString("publish_time");
			   String from=status_jsonobject.optString("from");
			   String feed_type=status_jsonobject.optString("type");
			   int comment_count=Integer.parseInt(status_jsonobject.optString("comment_count"));
			   int repost_count=Integer.parseInt(status_jsonobject.optString("repost_count"));
			   int digg_count=Integer.parseInt(status_jsonobject.optString("digg_count"));
			   String feed_content=status_jsonobject.optString("feed_content");
			   int uid=Integer.parseInt(status_jsonobject.optString("uid"));
			   String uname=status_jsonobject.optString("uname");
			   String headicon=status_jsonobject.optString("avatar_small");
			   Statuses status=new Statuses();
			   if(feed_type.equals("postimage")){
				   //上传图片微博
				   String bigpic_url=status_jsonobject.getJSONArray("attach").getJSONObject(0).optString("attach_url");
				   String middle_pic_url=status_jsonobject.getJSONArray("attach").getJSONObject(0).optString("attach_middle");
				   String small_pic_url=status_jsonobject.getJSONArray("attach").getJSONObject(0).optString("attach_small");
				   status.setBig_postimage_url(bigpic_url);
				   status.setMiddle_postimage_url(middle_pic_url);
				   status.setSmall_postimage_url(small_pic_url);
			   }
			   Statuses source_status=new Statuses();
			   if(status_jsonobject.optJSONObject("api_source")!=null){
				   JSONObject  source_status_jsonobject=status_jsonobject.optJSONObject("api_source");
				   int source_feed_id=Integer.parseInt(source_status_jsonobject.optString("feed_id"));
				   String source_publish_time=source_status_jsonobject.optString("publish_time");
				   String source_from=source_status_jsonobject.optString("from");
				   String source_feed_type=status_jsonobject.optString("type");
				   int source_comment_count=Integer.parseInt(source_status_jsonobject.optString("comment_count"));
				   int source_repost_count=Integer.parseInt(source_status_jsonobject.optString("repost_count"));
				   int source_digg_count=Integer.parseInt(source_status_jsonobject.optString("digg_count"));
				   String source_feed_content=source_status_jsonobject.optString("feed_content");
				   int source_uid=Integer.parseInt(source_status_jsonobject.optString("uid"));
				   String source_uname=source_status_jsonobject.optString("uname");
				   String source_headicon=source_status_jsonobject.optString("avatar_small");
				   if(source_feed_type.equals("postimage")){
					   //上传图片微博
					   String bigpic_url=source_status_jsonobject.getJSONArray("attach").getJSONObject(0).optString("attach_url");
					   String middle_pic_url=source_status_jsonobject.getJSONArray("attach").getJSONObject(0).optString("attach_middle");
					   String small_pic_url=source_status_jsonobject.getJSONArray("attach").getJSONObject(0).optString("attach_small");
					   source_status.setBig_postimage_url(bigpic_url);
					   source_status.setMiddle_postimage_url(middle_pic_url);
					   source_status.setSmall_postimage_url(small_pic_url);
				   }
				   User source_user=new User();
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
				   source_status.setUser(source_user);
				   source_status.setFeed_type(source_feed_type);
			   }
			   User user=new User();
			   user.setUid(uid);
			   user.setUname(uname);
			   user.setHeadicon(headicon);
			   status.setFeed_type(feed_type);
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
	return statuses;
	
	   
}
}
