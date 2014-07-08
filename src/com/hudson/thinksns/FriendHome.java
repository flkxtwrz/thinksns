package com.hudson.thinksns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.adapter.WeiboListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.chating.ChatTools;
import com.hudson.thinksns.chating.ChatingDetail;
import com.hudson.thinksns.chating.ChooseFriend;
import com.hudson.thinksns.chating.dataToDetail;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.diyview.xlistview.XListView.IXListViewListener;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.userinfo.SelfHome;
import com.hudson.thinksns.userinfo.SelfHomeProfile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * @file FriendHome.java
 * @author 王代银
 * @description 他人的详细资料页面，可以查看他人的详细资料，包括他人发过的所有微博状态
 *              并可以在此页面进行关注操作和私信操作。
 */
public class FriendHome extends Activity implements IXListViewListener {

	private DBManager dbm;
	private Account ac;
	private Context mContext;
	private String uid;
	private ImageLoader imageLoader;
	private Handler mHandler;
	private Button back,private_msg, more = null;

	private XListView mListView = null;
	private final int getweibo = 0x11;
	private final int getuserinfo = 0x12;
	private final int getuserphoto = 0x13;
	// private int i = 0;
	private final int num = 5;
	private int page = 1;
	private final int LoadMore = 0x33;
	private int state = 0;
	private String weibostr = "";
	private WeiboListAdapter mWeiboListAdapter;
	private ArrayList<Statuses> mstatuses = new ArrayList<Statuses>();
	private TextView username_top, name, location, brief, user_tag, blog, fan,
			care = null;
	private final int create_follow_success = 0x14;
	private final int destory_follow_success = 0x15;
	private ImageView photo, gender, iscared = null;
	User fans = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friend_home);
		findviews();
		mContext = this;
		dbm = new DBManager(this);
		ac = dbm.getAccountonline();
		uid = getIntent().getStringExtra("uid");
		imageLoader = new ImageLoader(mContext);

		initHander();
		new GetUserInfoThread().start();
		new GetUserStatuesThread().start();
		cared();

		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.friend_back:
					// TODO click things
					if (getIntent().getIntExtra("turnpage", 0) == 1) {
						Navigate(Fans.class);
					} else {
						Navigate(MainActivity.class);
					}
					break;
				// case R.id.friend_setting:
				// // TODO click things
				// // Navigate(MainUiActivity.class);
				// break;
				// case R.id.friend_profile:
				// // TODO click things
				// //Navigate(SelfHomeProfile.class);
				// // Navigate(MainUiActivity.class);
				// break;
				case R.id.friend_care_or:
					cared();
					new CreateOrDestoryFollowThread(String.valueOf(fans
							.getUid())).start();
					cared();

					break;
				case R.id.friend_private_msg:
					String message_id = new ChatTools(ac).createChat(Integer.parseInt(uid));
					dataToDetail.MESSAGEID = Integer.parseInt(message_id);
					Intent i = new Intent();
					i.setClass(FriendHome.this, ChatingDetail.class);
					startActivity(i);
					finish();
					break;
				}
			}
		};
		back.setOnClickListener(ocl);
		iscared.setOnClickListener(ocl);
		private_msg.setOnClickListener(ocl);
		// more.setOnClickListener(ocl);
	}

	private void cared() {

		if (fans.getIsFollowed() == 1) {
			/*
			 * 已关注
			 */
			iscared.setBackgroundResource(R.drawable.fans_cared);
			//iscared.setContentDescription("已关注");
			// notifyDataSetChanged();

		}
		else{
			/*
			 * 未关注
			 */
			iscared.setBackgroundResource(R.drawable.finding_addcare);
			//iscared.setContentDescription("未关注");
			// notifyDataSetChanged();

		}
	}

	private class CreateOrDestoryFollowThread extends Thread {

		private String user_id;

		public CreateOrDestoryFollowThread(String user_id) {

			this.user_id = user_id;

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"follow_create");
			MyNameValuePair NameValuePair3_1 = new MyNameValuePair("act",
					"follow_destroy");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					user_id);
			Message msg = mHandler.obtainMessage();
			msg.obj = iscared;
			if (fans.getIsFollowed() == 1) {
				String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
						NameValuePair1, NameValuePair2, NameValuePair3_1,
						NameValuePair4, NameValuePair5, NameValuePair6);
				System.out.println("qu xiao guan zhu" + result);
				if (result.equals("0")) {
					/*
					 * 取消关注失败
					 */

				} else {
					/*
					 * 成功
					 */
					msg.what = destory_follow_success;
				}
			}
			else{
				String result_1 = MHttpClient.get(ThinkSNSApplication.baseUrl,
						NameValuePair1, NameValuePair2, NameValuePair3,
						NameValuePair4, NameValuePair5, NameValuePair6);
				System.out.println("tian jia guan zhu" + result_1);
				Log.e("result", result_1);

				if (result_1.equals("0")) {
					/*
					 * 关注失败
					 */

				} else {
					/*
					 * 成功
					 */
					msg.what = create_follow_success;
				}
			}

			mHandler.sendMessage(msg);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.friend_home, menu);
		return true;
	}

	private class GetUserStatuesThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"user_timeline");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					String.valueOf(uid));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("count",
					String.valueOf(num));
			MyNameValuePair NameValuePair8 = new MyNameValuePair("page",
					String.valueOf((page)));
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7, NameValuePair8);

			page++;
			Message msg = new Message();
			msg.what = getweibo;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private class GetUserInfoThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod", "User");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", "show");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id", uid);
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			System.out.println("GetUserInfoThread" +result);
			Log.e("re", result);
			try {
				JSONObject json_result = new JSONObject(result);
				String uname = json_result.optString("uname");
				String sex = json_result.optString("sex");
				String location = json_result.optString("location");
				String headicon = json_result.optString("avatar_small");
				String intro = json_result.optString("intro");
				String tag = json_result.optString("user_tag");
				int following = json_result.optJSONObject("count_info").optInt(
						"following_count");
				int follower = json_result.optJSONObject("count_info").optInt(
						"follower_count");
				int feedcount = json_result.optJSONObject("count_info").optInt(
						"feed_count");
				int checkcount = json_result.optJSONObject("count_info")
						.optInt("check_totalnum");
				int follow_state = json_result
						.optJSONObject("follow_state").optInt("following");
				
				System.out.println(" follow_state follow_state follow_state" +  follow_state);
				User tempuser = new User();
				tempuser.setUname(uname);
				tempuser.setUid(Integer.parseInt(uid));
				tempuser.setSex(sex);
				tempuser.setLocation(location);
				tempuser.setIntro(intro);
				tempuser.setTag(tag);
				tempuser.setHeadicon(headicon);
				tempuser.setFollowing(following);
				tempuser.setFollower(follower);
				tempuser.setFeedcount(feedcount);
				tempuser.setCheckcount(checkcount);
				tempuser.setIsFollowed(follow_state);
				Message msg = mHandler.obtainMessage();
				msg.what = getuserinfo;
				msg.obj = tempuser;
				fans = tempuser;
				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void findviews() {
		username_top = (TextView) findViewById(R.id.friend_username);
		back = (Button) findViewById(R.id.friend_back);
		private_msg = (Button)findViewById(R.id.friend_private_msg);
		// more = (Button) findViewById(R.id.friend_setting);
		name = (TextView) findViewById(R.id.friend_name);
		location = (TextView) findViewById(R.id.friend_country);
		brief = (TextView) findViewById(R.id.friend_brief);
		user_tag = (TextView) findViewById(R.id.friend_tag);
		blog = (TextView) findViewById(R.id.friend_blog_num);
		fan = (TextView) findViewById(R.id.friend_fan_num);
		care = (TextView) findViewById(R.id.friend_care_num);
		photo = (ImageView) findViewById(R.id.friend_photo);
		gender = (ImageView) findViewById(R.id.friend_gender);
		mListView = (XListView) findViewById(R.id.friend_blog_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		iscared = (ImageView) findViewById(R.id.friend_care_or);
	}

	private void initHander() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				// TODO Auto-generated method stub
				switch (msg.what) {
				case getweibo:
					weibostr = msg.obj.toString();
					mListView.setPullLoadEnable(true);
					if (weibostr.equals("[]")) {
						mListView.setPullLoadEnable(false);
						break;
					}
					ArrayList<Statuses> tstatuses = format();
					Log.e("微博个数：", "" + tstatuses.size());
					// Log.e("states", tstatuses.toString());
					if (tstatuses.size() != 0) {
						for (Statuses s : tstatuses) {
							mstatuses.add(s);
						}
					}
					Log.e("hhhh->>>>", mstatuses.size() + "");
					if (state == LoadMore) {
						mWeiboListAdapter.notifyDataSetChanged();
						break;
					}

					mWeiboListAdapter = new WeiboListAdapter(mstatuses,
							new ImageLoader(mContext), mContext, ac);

					mListView.setAdapter(mWeiboListAdapter);

					break;
				case getuserinfo:
					User user = (User) msg.obj;
					username_top.setText(user.getUname());
					name.setText(user.getUname());
					location.setText(user.getLocation());
					brief.setText("简介：" + user.getIntro());
					user_tag.setText("标签：" + user.getTag());
					blog.setText(user.getFeedcount() + "");
					fan.setText(user.getFollower() + "");
					care.setText(user.getFollowing() + "");
					imageLoader.DisplayImage(user.getHeadicon(), photo);
					if (user.getSex().equals("男")) {
						gender.setImageResource(R.drawable.fans_sex_boy);
					} else {
						gender.setImageResource(R.drawable.fans_sex_girl);
					}
					if(user.getIsFollowed() == 1)
					{
						iscared.setBackgroundResource(R.drawable.fans_cared);
					}
					else
					{
						iscared.setBackgroundResource(R.drawable.finding_addcare);
					}
					break;
				case create_follow_success:
					ImageView iv = (ImageView) msg.obj;
					System.out.println("good");
					iv.setBackgroundResource(R.drawable.fans_cared);
					iv.setContentDescription("已关注");
					break;
				case destory_follow_success:
					ImageView iv1 = (ImageView) msg.obj;
					System.out.println("false");
					iv1.setBackgroundResource(R.drawable.finding_addcare);
					iv1.setContentDescription("未关注");
					break;
				default:
					break;
				}

			}

		};
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mListView.setRefreshTime(sdf.format(new Date()));
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		state = 0;
		page = 1;
		mstatuses.clear();

		GetUserStatuesThread getstatuesthread = new GetUserStatuesThread();
		getstatuesthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state = LoadMore;
		GetUserStatuesThread getstatuesthread = new GetUserStatuesThread();
		getstatuesthread.start();
		onLoad();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbm.closeDB();
	}

	private ArrayList<Statuses> format() {

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

	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(FriendHome.this, t);
		startActivity(i);
		FriendHome.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(FriendHome.this, MainActivity.class);
			startActivity(i);
			FriendHome.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
