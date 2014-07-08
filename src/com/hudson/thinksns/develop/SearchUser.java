package com.hudson.thinksns.develop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.*;
import com.hudson.thinksns.adapter.FanListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.chating.ChatTools;
import com.hudson.thinksns.chating.ChatingDetail;
import com.hudson.thinksns.chating.ChooseFriend;
import com.hudson.thinksns.chating.dataToDetail;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @author 贾焱超
 * 
 */
@SuppressLint("NewApi")
public class SearchUser extends Fragment {
	private ListView userlist;
	private Account account;
	private DBManager dbm;
	private Handler mHandler;
	private ImageLoader imageLoader;
	ArrayList<User> searched_user;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.userstatues, container, false);
		
		System.out.println("fragment1");
		initHandler();
		userlist = (ListView) view.findViewById(R.id.user_list_new);
		dbm = new DBManager(getActivity());
		imageLoader = new ImageLoader(getActivity());
		account = dbm.getAccountonline();
		new GetSearchUserThread().start();
		
		userlist.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				Intent i = new Intent();
				i.setClass(getActivity(), FriendHome.class);
				i.putExtra("uid", String.valueOf(searched_user.get(arg2).getUid()));
				startActivity(i);
				getActivity().finish();
			}
		});
		return view;
	}

	private class GetSearchUserThread extends Thread 
	{
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", "weibo_search_user");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token", account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair7 = new MyNameValuePair("key",    //记住顺序问题。
					DateToFragment.KEY_WORD);											//这里关键词
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair7);
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
					int follower_count = json_result
							.optJSONObject("count_info").optInt(
									"follower_count");
					String uid = json_result.optString("uid");
					/*
					 * 获取 intro
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
					user.setFollower(follower_count);
					Log.e("粉丝", user.toString());
					friends.add(user);
				}
				Message msg = mHandler.obtainMessage();
				msg.what = 0x11;
				msg.obj = friends;
				searched_user = friends;
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
