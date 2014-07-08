package com.hudson.thinksns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.adapter.FanListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.userinfo.SelfHome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @file Fans.java
 * @author 石仲才
 * @description 我的朋友的界面，主要用于查看 我的粉丝，我关注的人和互相关注的人。并可以进一步查看他们的详细情况，进行关注和 取消关注的操作
 */
public class Fans extends Activity {
	private DBManager dbm;
	private Account ac;
	private ImageLoader imageLoader;
	private Context mContext;
	private TextView title = null;
	private ListView list = null;
	private RadioGroup rg = null;
	private RadioButton b1, b2 = null;
	private Button back = null;
	private ImageView find = null;
	// 设置性别和关注否，0为男，未关注。 1为女，已关注
	private int sex_or, care_or = 0;

	private Handler mHandler;
	private final int getfans = 0x11;
	private final int getfollows = 0x22;
	private final int getfriends = 0x33;
	ArrayList<User> friends_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */

		setContentView(R.layout.fans);
		mContext = this;
		initHandler();
		dbm = new DBManager(mContext);
		ac = dbm.getAccountonline();
		imageLoader = new ImageLoader(mContext);
		title = (TextView) findViewById(R.id.fans_topbar_txt);
		list = (ListView) findViewById(R.id.fans_list);
		rg = (RadioGroup) findViewById(R.id.fans_group);
		back = (Button) findViewById(R.id.fans_back);
		find = (ImageView) findViewById(R.id.fans_find);

		b1 = (RadioButton) findViewById(R.id.fans_care);
		b2 = (RadioButton) findViewById(R.id.fans_fan);
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.fans_back:
					// TODO click things
					if (getIntent().getIntExtra("mainorhome", 0) == 1) {
						Intent i = new Intent(Fans.this, SelfHome.class);
						i.putExtra("uid", getIntent().getStringExtra("uid"));
						startActivity(i);
						Fans.this.finish();
					} else {
						Intent i = new Intent(Fans.this, MainActivity.class);
						// i.putExtra("uid", getIntent().getStringExtra("uid"));
						startActivity(i);
						Fans.this.finish();
					}
					break;
				case R.id.fans_find:
					// TODO click things

					break;
				}
			}

		};
		back.setOnClickListener(ocl);
		find.setOnClickListener(ocl);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent();
				i.setClass(Fans.this, FriendHome.class);
				i.putExtra("uid",
						String.valueOf(friends_list.get(arg2).getUid()));
				startActivity(i);
				finish();
			}
		});

		// 默认选中关注
		// init(2);
		Intent t = getIntent();
		if (t.getIntExtra("checkwhat", 0) == 1) {
			b2.setChecked(true);
			title.setText("粉丝");
		} else {
			b1.setChecked(true);
			title.setText("关注");
		}

		// inflate(sex_or, care_or);
		new GetFollowsThread().start();
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.fans_care:
					// TODO
					new GetFollowsThread().start();
					title.setText("关注");
					// listclear();
					// inflate(sex_or, care_or);
					break;
				case R.id.fans_fan:
					// TODO
					new GetFansThread().start();
					title.setText("粉丝");
					// list.removeAllViews();

					break;
				case R.id.fans_care_each:
					// TODO
					new GetFriendsThread().start();
					title.setText("互相关注");
					// list.removeAllViews();
					// list.removeAllViews();
					break;
				}
			}
		});
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case getfollows:
					list.setAdapter(new FanListAdapter(
							(ArrayList<User>) msg.obj, imageLoader, mContext,
							ac));
					break;
				case getfriends:
					list.setAdapter(new FanListAdapter(
							(ArrayList<User>) msg.obj, imageLoader, mContext,
							ac));
					break;
				case getfans:
					list.setAdapter(new FanListAdapter(
							(ArrayList<User>) msg.obj, imageLoader, mContext,
							ac));
					break;
				default:
					break;
				}
			}

		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.fans, menu);
		return true;
	}

	// 这里填充list
	private void inflate(int gender, int care) {
		// List<HashMap<String, Object>> data = new ArrayList<HashMap<String,
		// Object>>();
		// HashMap<String, Object> item = new HashMap<String, Object>();
		// item.put("state_username", "万岁寿司");
		// // 0是男，1是女
		// if (gender == 0) {
		// item.put("state_gender", R.drawable.fans_sex_boy);
		// } else {
		// item.put("state_gender", R.drawable.fans_sex_girl);
		// }
		// item.put("state_fansnum", "粉丝：23333");
		// item.put("state_intro", "我是Android");
		// // 是否关注 0 未关注 1 已关注
		// if (care == 0) {
		// item.put("state_care_orno", R.drawable.finding_addcare);
		// } else {
		// item.put("state_care_orno", R.drawable.fans_cared);
		// }
		// data.add(item);
		//
		// SimpleAdapter adapter = new SimpleAdapter(this, data,
		// R.layout.fans_blog_item, new String[] { "state_username",
		// "state_gender", "state_fansnum", "state_intro",
		// "state_care_orno" }, new int[] {
		// R.id.fans_bloger_pic_name, R.id.fans_sex,
		// R.id.fans_bloger_fans, R.id.fans_intro,
		// R.id.fans_bloger_addcare });
		// list.setAdapter(adapter);
	}

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(Fans.this, t);
		startActivity(i);
		Fans.this.finish();
	}

	/*
	 * 获取相互关注朋友的列表
	 */
	private class GetFriendsThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_friends");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			ArrayList<User> friends = new ArrayList<User>();
			try {
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
				msg.what = getfriends;
				msg.obj = friends;
				friends_list = friends;
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", result);
		}

	}

	/*
	 * 获取粉丝
	 */
	private class GetFansThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_followers");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			ArrayList<User> friends = new ArrayList<User>();
			try {
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
				msg.what = getfans;
				msg.obj = friends;
				friends_list = friends;
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", result);
		}

	}

	/*
	 * 获取关注列表
	 */
	private class GetFollowsThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_following");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5);
			ArrayList<User> friends = new ArrayList<User>();
			try {
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
				msg.what = getfollows;
				msg.obj = friends;
				friends_list = friends;
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("re", result);
		}

	}

	// private void listclear() {
	// List<HashMap<String, Object>> data = new ArrayList<HashMap<String,
	// Object>>();
	// SimpleAdapter adapter = new SimpleAdapter(this, data,
	// R.layout.fans_blog_item, new String[] {}, new int[] {
	// R.id.fans_bloger_pic_name, R.id.fans_sex,
	// R.id.fans_bloger_fans, R.id.fans_intro,
	// R.id.fans_bloger_addcare });
	// list.setAdapter(adapter);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getIntent().getIntExtra("mainorhome", 0) == 1) {
				Intent i = new Intent(Fans.this, SelfHome.class);
				i.putExtra("uid", getIntent().getStringExtra("uid"));
				startActivity(i);
				Fans.this.finish();
			} else {
				Intent i = new Intent(Fans.this, MainActivity.class);
				// i.putExtra("uid", getIntent().getStringExtra("uid"));
				startActivity(i);
				Fans.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbm.closeDB();
	}
}
