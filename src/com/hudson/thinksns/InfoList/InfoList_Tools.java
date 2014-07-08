package com.hudson.thinksns.InfoList;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Notify;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * 
 * @author 石仲才
 * 
 */
public class InfoList_Tools 
{
	Account ac;
	ArrayList<Notify> infos;
	public InfoList_Tools(Account a)
	{
		this.ac = a;
	}
	
	public ArrayList<Notify> GetNotify()
	{
		GetNotifyThread getNotifyThread = new GetNotifyThread();
		getNotifyThread.start();
		try 
		{
			getNotifyThread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		return infos;
	}
	
	public void SetNotifyRead(String tt)
	{
		SetNotifyReadThread setnotifyread = new SetNotifyReadThread(tt);
		setnotifyread.start();
		try 
		{
			setnotifyread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	private class GetNotifyThread extends Thread
	{
		public void run() 
		{
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Notifytion");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_notify_by_count");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());

			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			System.out.println("GetNotifyThread结果：" + result);

			infos = format(result);
			
		}
	}
	private ArrayList<Notify> format(String jsonstr) 
	{
		ArrayList<Notify> notifys = new ArrayList<Notify>();
		try 
		{
			JSONArray notify_jsonarray = new JSONArray(jsonstr);
			for (int i = 0; i < notify_jsonarray.length(); i++) 
			{
				JSONObject notify_jsonobject = notify_jsonarray
						.getJSONObject(i);
				String type = notify_jsonobject.optString("type");
				String name = notify_jsonobject.optString("name");
				int count = Integer.parseInt(notify_jsonobject
						.optString("count"));
				Notify notify = new Notify();
				notify.setType(type);
				notify.setName(name);
				notify.setCount(count);
				notifys.add(notify);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notifys;
	}
	
	private class SetNotifyReadThread extends Thread
	{
		String type;
		SetNotifyReadThread(String t)
		{
			this.type = t;
		}
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Notifytion");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"set_notify_read");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair(
					"type", type);

			String result = MHttpClient.post(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			System.out.println("SetNotifyReadThread结果：" + result);
		}
	}
}
