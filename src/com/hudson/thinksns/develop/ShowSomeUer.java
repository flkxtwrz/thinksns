package com.hudson.thinksns.develop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hudson.thinksns.R;
import com.hudson.thinksns.adapter.FanListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * 
 * @author º÷ÏÕ≥¨
 * 
 */
@SuppressLint("NewApi")
public class ShowSomeUer extends Fragment
{

	private ListView userlist;
	private Account account;
	private DBManager dbm;
	private Handler mHandler;
	private ImageLoader imageLoader;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.userstatues, container, false);
		
		System.out.println("fragment1");
		initHandler();
		userlist = (ListView) view.findViewById(R.id.user_list_new);
		dbm = new DBManager(getActivity());
		imageLoader = new ImageLoader(getActivity());
		account = dbm.getAccountonline();
		new SearchUserByAreaThread().start();
		return view;
	}
	
	private class SearchUserByAreaThread extends Thread 
	{
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", "user_followers");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token", account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			ArrayList<User> friends = new ArrayList<User>();
			try 
			{
				JSONArray jsonArray_result = new JSONArray(result);
				for (int i = 0; i < jsonArray_result.length(); i++) {
					JSONObject json_result = jsonArray_result.getJSONObject(i);
					String uname = json_result.optString("uname");
					String headicon = json_result.optString("avatar_small");
					int follow_state = json_result
							.optJSONObject("follow_state").optInt("following");
					String uid = json_result.optString("uid");
					/*
					 * ªÒ»° intro
					 */
					MyNameValuePair NameValuePair3_1 = new MyNameValuePair(
							"act", "show");
					MyNameValuePair NameValuePair6 = new MyNameValuePair(
							"user_id", uid);
					String user_info_result = MHttpClient.get(
							ThinkSNSApplication.baseUrl, NameValuePair1,
							NameValuePair2, NameValuePair3_1, NameValuePair4,
							NameValuePair5, NameValuePair6);
					JSONObject userinfo_json = new JSONObject(user_info_result);
					String intro = userinfo_json.optString("intro");
					String sex = userinfo_json.optString("sex");
					User user = new User();
					user.setUid(Integer.parseInt(uid));
					user.setUname(uname);
					user.setHeadicon(headicon);
					user.setSex(sex);
					user.setIntro(intro);
					user.setIsFollowed(follow_state);
					Log.e("∑€Àø", user.toString());
					friends.add(user);
				}
				Message msg = mHandler.obtainMessage();
				msg.what = 0x11;
				msg.obj = friends;
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", result);
		}

	}
	
	private void initHandler() 
	{
		mHandler = new Handler() 
		{
			@Override
			public void handleMessage(Message msg) 
			{
				switch (msg.what) 
				{
				case 0x11: 
					userlist.setAdapter(new FanListAdapter((ArrayList<User>) msg.obj, imageLoader, getActivity(),account));
					//code
					break;
				}
			}
		};
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
}
