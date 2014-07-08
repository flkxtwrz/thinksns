package com.hudson.thinksns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.adapter.CommentToMeListAdapter;
import com.hudson.thinksns.adapter.WeiboListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.diyview.xlistview.XListView.IXListViewListener;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.CommentOfStatus;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;

import com.hudson.thinksns.statusparse.WeiBoPrase;
import com.hudson.thinksns.timeutil.TimeUtil;
import com.hudson.thinksns.userinfo.SelfHome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 *@file  DoRepost.java
 *@author 王代银
 *@description (DoComment页面的弃用版)微博的详情页面，主要用于查看某一微博的详细评论以及微博内容。 并对该微博进行更多的操作
 */
public class DoRepost extends Activity implements IXListViewListener {
	/*
	 * 微博的页面
	 */
	private RelativeLayout rel;

	private TextView text, name, date, from;
	private TextView otext, oname, odate, ofrom;
	private ImageView imageView_head, imageView_content;
	private ImageView oimageView_head, oimageView_content;
	private final String[] platfroms = { "来自网页版", "来自手机网页版", "来自android版",
			"来自iphone版", "来自ipad版", };
	private ImageLoader imageLoader;
	private LinearLayout resend, good, comment = null;
	private RelativeLayout sendbar = null;
	private ImageView add_emotion, send = null;
	private boolean or = true;
	private Statuses status;
	private Context mContext;
	private DBManager dbm;
	private Account ac;
	private Handler mHandler;
	private XListView mListView;

	private String weibostr = "";

	private final int repoststatus = 0x11;
	private final int getreposts = 0x22;
	private final int num = 5;
	private int page = 1;
	private final int LoadMore = 0x33;
	private int state = 0;

	private WeiboListAdapter mWeiboListAdapter;
	private ArrayList<Statuses> mstatuses = new ArrayList<Statuses>();
	private EditText comment_edittxt;
	InputMethodManager im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */

		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.homeblog_state_listitem);
		mContext = this;
		imageLoader = new ImageLoader(mContext);
		dbm = new DBManager(mContext);
		ac = dbm.getAccountonline();
		status = (Statuses) getIntent().getSerializableExtra("weibo");
		Log.e("weibo", status.toString());
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		mListView = (XListView) findViewById(R.id.comment_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		page = 1;
		state = 0;
		findStatusViews();
		initStatusViews();
		GetRepostStatusThread getRepoststhread = new GetRepostStatusThread();
		getRepoststhread.start();
		final RelativeLayout re = (RelativeLayout) findViewById(R.id.comment_add_emotion);
		resend = (LinearLayout) findViewById(R.id.homeblog_resend_linear);
		good = (LinearLayout) findViewById(R.id.homeblog_good_linear);
		comment = (LinearLayout) findViewById(R.id.homeblog_comment_linear);
		sendbar = (RelativeLayout) findViewById(R.id.comment_msg_send_rl);
		add_emotion = (ImageView) findViewById(R.id.comment_add_emotion_btn);
		send = (ImageView) findViewById(R.id.comment_send_btn);
		comment_edittxt = (EditText) findViewById(R.id.comment_edittxt);
		initHandler();
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.homeblog_resend_linear:
					sendbar.setVisibility(View.VISIBLE);
					break;
				case R.id.homeblog_good_linear:
					break;
				case R.id.homeblog_comment_linear:

					sendbar.setVisibility(View.VISIBLE);
					// comment.requestFocus();
					// re.requestFocus();
					break;
				case R.id.comment_add_emotion_btn:
					// sendbar.setVisibility(View.VISIBLE);
					im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (im.isActive()) {
						im.hideSoftInputFromWindow(getCurrentFocus()
								.getApplicationWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
					re.requestFocus();
					if (or == true) {
						RelativeLayout layout = (RelativeLayout) inflater
								.inflate(R.layout.private_msg_add_layout, null)
								.findViewById(R.id.private_addbtn_item);
						re.removeAllViews();
						re.addView(layout);
						or = false;

					} else {
						re.removeAllViews();
						or = true;
					}
					break;
				case R.id.comment_send_btn:

					// re.requestFocus();
					im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					im.hideSoftInputFromWindow(getCurrentFocus()
							.getApplicationWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					sendbar.setVisibility(View.GONE);

					new RepostStatusThread().start();

					// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DoComment.this.getCurrentFocus().getWindowToken(),
					// InputMethodManager.HIDE_NOT_ALWAYS);
					break;
				}
			}
		};
		resend.setOnClickListener(ocl);
		good.setOnClickListener(ocl);
		comment.setOnClickListener(ocl);
		add_emotion.setOnClickListener(ocl);
		send.setOnClickListener(ocl);

	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {

				case getreposts:
					weibostr = msg.obj.toString();
					mListView.setPullLoadEnable(true);
					if (weibostr.equals("[]")) {
						mListView.setPullLoadEnable(false);
						break;
					}
					ArrayList<Statuses> tstatuses = formatreposts();
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

				case repoststatus:
					String ss = msg.obj.toString();
					Log.e("zf", ss);
					if (!ss.equals("0")) {
						/*
						 * 转发成功
						 */
						onRefresh();
					}
					break;
				}
			}

		};
	}

	private void initStatusViews() {
		// TODO Auto-generated method stub
		text.setText(WeiBoPrase.parseContent(status.getFeed_content(), mContext));
		date.setText(TimeUtil.totime(Long.parseLong(status.getPublish_time())));

		name.setText(status.getUser().getUname());
		from.setText(platfroms[Integer.parseInt(status.getFrom())]);
		imageLoader
				.DisplayImage(status.getUser().getHeadicon(), imageView_head);
		imageView_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uid = String.valueOf(status.getUser().getUid());
				Intent i = new Intent(mContext, SelfHome.class);
				i.putExtra("uid", uid);
				mContext.startActivity(i);

			}
		});
		if (status.getFeed_type().equals("repost")) {
			final int ouserid = status.getSource_status().getUser().getUid();
			rel.setVisibility(View.VISIBLE);
			otext.setText(WeiBoPrase.parseContent(status.getSource_status()
					.getFeed_content(), mContext));
			// odate.setText(TimeUtil.totime(Long.parseLong(status.getSource_status().getPublish_time())));
			oname.setText(status.getSource_status().getUser().getUname());
			// ofrom.setText(platfroms[Integer.parseInt(status.getSource_status().getFrom())]);
			imageLoader.DisplayImage(status.getSource_status().getUser()
					.getHeadicon(), oimageView_head);
			oimageView_head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String uid = String.valueOf(ouserid);
					Intent i = new Intent(mContext, UserInfoActivity.class);
					i.putExtra("uid", uid);
					mContext.startActivity(i);

				}
			});
			if (status.getSource_status().getFeed_type().equals("postimage")) {
				oimageView_content.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(status.getSource_status()
						.getSmall_postimage_url(), oimageView_content);
			} else {
				oimageView_content.setVisibility(View.GONE);
			}
		} else {
			rel.setVisibility(View.GONE);
		}
		if (status.getFeed_type().equals("postimage")) {

			imageView_content.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(status.getSmall_postimage_url(),
					imageView_content);
		} else {
			imageView_content.setVisibility(View.GONE);

		}
	}

	private void findStatusViews() {
		// TODO Auto-generated method stub
		rel = (RelativeLayout) findViewById(R.id.homeblog_state_resend);
		text = (TextView) findViewById(R.id.homeblog_state_contents);
		name = (TextView) findViewById(R.id.homeblog_state_username);
		date = (TextView) findViewById(R.id.homeblog_state_date);
		from = (TextView) findViewById(R.id.homeblog_state_from);

		imageView_head = (ImageView) findViewById(R.id.homeblog_state_userpic);
		imageView_content = (ImageView) findViewById(R.id.homeblog_state_photos);
		otext = (TextView) findViewById(R.id.homeblog_resend_contents);
		oname = (TextView) findViewById(R.id.homeblog_resend_username);
		// odate = (TextView) findViewById(R.id.homeblog_resend_date);
		// ofrom = (TextView) findViewById(R.id.homeblog_resend_from);

		oimageView_head = (ImageView) findViewById(R.id.homeblog_resend_userpic);
		oimageView_content = (ImageView) findViewById(R.id.homeblog_resend_photos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dbm.closeDB();
		super.onDestroy();
	}

	private class GetRepostStatusThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"reposts");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("id",
					String.valueOf(status.getFeed_id()));
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
			msg.what = getreposts;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private class RepostStatusThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"repost");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("id",
					String.valueOf(status.getFeed_id()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content",
					comment_edittxt.getEditableText().toString());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7);

			Message msg = new Message();
			msg.what = repoststatus;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private ArrayList<Statuses> formatreposts() {

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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		state = 0;
		page = 1;
		mstatuses.clear();
		// getResources().getDrawable(id)
		GetRepostStatusThread getcommentsthread = new GetRepostStatusThread();
		getcommentsthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state = LoadMore;
		GetRepostStatusThread getcommentsthread = new GetRepostStatusThread();
		getcommentsthread.start();
		onLoad();
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mListView.setRefreshTime(sdf.format(new Date()));
	}
}
