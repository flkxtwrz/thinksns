package com.hudson.thinksns;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * @file UserInfoActivity.java
 * @author 贾焱超
 * @description 用户个人信息的界面（已弃用）
 */
public class UserInfoActivity extends Activity {
	private DBManager dbm;
	private Account ac;
	private Context mContext;
	private String uid;
	private ImageLoader imageLoader;
	private Handler mHandler;
	private TextView username_top,name,location,brief,blog,fan,care;
	private ImageView photo,gender;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_self_center);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.top_menu);
		findviews();
		mContext = this;
		dbm = new DBManager(this);
		ac=dbm.getAccountonline();
		uid=getIntent().getStringExtra("uid");
		imageLoader = new ImageLoader(mContext);
		initHander();
		new GetUserInfoThread().start();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbm.closeDB();
	}
	private void initHander() {
		// TODO Auto-generated method stub
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				User user=(User) msg.obj;
				username_top.setText(user.getUname());
				name.setText(user.getUname());
				location.setText(user.getLocation());
				brief.setText(user.getIntro());
				blog.setText("微博\n\t"+user.getFeedcount()+"");
				fan.setText("粉丝\n\t"+user.getFollower()+"");
				care.setText("关注\n\t"+user.getFollowing()+"");
				imageLoader.DisplayImage(user.getHeadicon(), photo);
				if(user.getSex().equals("男")){
					gender.setImageResource(R.drawable.sex);
				}else{
					gender.setImageResource(R.drawable.baiyan);
				}
				
			}
			
		};
	}
	private void findviews() {
		// TODO Auto-generated method stub
		username_top=(TextView) findViewById(R.id.username);
		name=(TextView) findViewById(R.id.neckname);
		location=(TextView) findViewById(R.id.location);
		brief=(TextView) findViewById(R.id.brief);
		blog=(TextView) findViewById(R.id.blog);
		fan=(TextView) findViewById(R.id.fan);
		care=(TextView) findViewById(R.id.care);
		photo=(ImageView) findViewById(R.id.photo);
		gender=(ImageView) findViewById(R.id.gender);
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
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					uid);

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			Log.e("re", result);
			try {
				JSONObject json_result = new JSONObject(result);
				String uname = json_result.optString("uname");
				String sex=json_result.optString("sex");
				String location=json_result.optString("location");
				String headicon = json_result.optString("avatar_small");
				String intro=json_result.optString("intro");
				int following=json_result.optJSONObject("count_info").optInt("following_count");
				int follower=json_result.optJSONObject("count_info").optInt("follower_count");
			    int feedcount=json_result.optJSONObject("count_info").optInt("feed_count");
				int checkcount=json_result.optJSONObject("count_info").optInt("check_totalnum");
				User tempuser=new User();
				tempuser.setUname(uname);
				tempuser.setUid(Integer.parseInt(uid));
				tempuser.setSex(sex);
				tempuser.setLocation(location);
				tempuser.setIntro(intro);
				tempuser.setHeadicon(headicon);
				tempuser.setFollowing(following);
				tempuser.setFollower(follower);
				tempuser.setFeedcount(feedcount);
				tempuser.setCheckcount(checkcount);
				Message msg=mHandler.obtainMessage();
				msg.obj=tempuser;
				mHandler.sendMessage(msg);
				Log.e("tempuser", tempuser.toString());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
