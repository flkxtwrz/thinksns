package com.hudson.thinksns.userinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;

import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.adapter.WeiboListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.diyview.xlistview.XListView.IXListViewListener;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/** 
* @author ¼ÖìÍ³¬
*/
public class SelfHomeCollect extends Activity implements IXListViewListener {
	private Handler mHandler;
	private WeiboListAdapter mWeiboListAdapter;
	private XListView mListView;
	private Statuses status;
	private Account ac;
	private final int num = 5;
	private int page = 1;
	private ImageLoader imageLoader;
	private Context mContext;
	private final int getweibo = 0x11;
	private DBManager dbm;
	private ArrayList<Statuses> mstatuses;
	private final int collection = 0x34;
	private String weibostr = "";
	private int state = 0;
	private final int LoadMore = 0x33;
	private Button back = null;
	// private WeiboListAdapter mWeiboListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */
		
		setContentView(R.layout.self_home_collection);
		back = (Button)findViewById(R.id.self_center_collection_btn_backup);
		mContext = this;
		dbm = new DBManager(this);
		imageLoader = new ImageLoader(mContext);
		Intent intent = getIntent();
		ac = (Account) intent.getSerializableExtra("ac");
		if (ac == null) {
			ac = dbm.getAccountonline();
		}
		ac.setState(1);
		status = (Statuses) getIntent().getSerializableExtra("weibo");
		mListView = (XListView) findViewById(R.id.self_center_collection_statelist);
		mListView.setPullLoadEnable(true);
		mstatuses = new ArrayList<Statuses>();
		state = 0;
		// mListView.setXListViewListener(this);
		initHandler();
		
		collectionThread ct = new collectionThread();
		ct.start();
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SelfHomeCollect.this,SelfHome.class);
				i.putExtra("uid", getIntent().getStringExtra("uid"));
				startActivity(i);
				SelfHomeCollect.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.self_home_collect, menu);
		return true;
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		state = 0;
		page = 1;
		mstatuses.clear();
		// getResources().getDrawable(id)
		collectionThread getcommentsthread = new collectionThread();
		getcommentsthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state = LoadMore;
		collectionThread getcommentsthread = new collectionThread();
		getcommentsthread.start();
		onLoad();
	}
	private void initHandler() {
		// mWeiboListAdapter=new WeiboListAdapter(mstatuses, new
		// ImageLoader(mContext),mContext);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case collection:
					weibostr = msg.obj.toString();
					mListView.setPullLoadEnable(true);
					if (weibostr.equals("[]")) {
						mListView.setPullLoadEnable(false);
						break;
					}
					ArrayList<Statuses> tstatuses = format();
					Log.e("Î¢²©¸öÊý£º", "" + tstatuses.size());
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

					// mWeiboListAdapter=new WeiboListAdapter(mstatuses, new
					// ImageLoader(mContext),mContext,account);
					//System.out.println(mstatuses);
					mWeiboListAdapter = new WeiboListAdapter(mstatuses,
							new ImageLoader(mContext), mContext, ac);

					mListView.setAdapter(mWeiboListAdapter);

					break;
				}
			}

		};
	}
	
	private class collectionThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", "favorite_feed");// "public_timeline"
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					String.valueOf(ac.getUid()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("count",
					String.valueOf(num));
			MyNameValuePair NameValuePair8 = new MyNameValuePair("page",
					String.valueOf((page)));
			MyNameValuePair NameValuePair9 = new MyNameValuePair("format",
					"json");

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7, NameValuePair8,NameValuePair9);
			Log.e("con", result);

			Message msg = new Message();
			msg.what = collection;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private ArrayList<Statuses> format() {

		ArrayList<Statuses> statuses = new ArrayList<Statuses>();
		JSONArray statuses_jsonarray;
		try {
			statuses_jsonarray = new JSONArray(weibostr);
			Log.e("jsonÎ¢²©¸öÊý£º", "" + statuses_jsonarray.length());
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
					// ÉÏ´«Í¼Æ¬Î¢²©
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
						// ÉÏ´«Í¼Æ¬Î¢²©
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
		Log.e("huodeÎ¢²©¸öÊý£º", "" + statuses.size());
		return statuses;

		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbm.closeDB();
		super.onDestroy();
	}
	

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mListView.setRefreshTime(sdf.format(new Date()));
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SelfHomeCollect.this,SelfHome.class);
			i.putExtra("uid", getIntent().getStringExtra("uid"));
			startActivity(i);
			SelfHomeCollect.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

