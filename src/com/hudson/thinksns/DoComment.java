package com.hudson.thinksns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.adapter.CommentToMeListAdapter;
import com.hudson.thinksns.adapter.WeiboListAdapter;
import com.hudson.thinksns.adapter.WeiboListAdapter.AddOrDelDiggThread;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 *@file  DoComment.java
 *@author 贾焱超
 *@description 微博的详情页面，主要用于查看某一微博的详细评论以及微博内容。 并对该微博进行更多的操作
 */
public class DoComment extends Activity implements IXListViewListener {
	/*
	 * 微博的页面
	 */
	private RelativeLayout rel;

	private TextView text, name, date, from;
	private TextView otext, oname, odate, ofrom;
	private ImageView back, imageView_head, imageView_content;
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
	private String commentstr = "";
	private View vv;
	private final int getcomment = 0x11;
	private final int createcomment = 0x22;
	private final int repoststatus = 0x44;
	// 收藏
	private final int collection_create = 0x34;
	private final int collection_destroy = 0x35;
	// 点赞 去赞
	private final int add_tag = 0x12;
	private final int del_tag = 0x23;

	private final int num = 5;
	private int page = 1;
	private final int LoadMore = 0x33;
	private int state = 0;

	private TextView digg_num;
	// 收藏
	private LinearLayout coll;

	private CommentToMeListAdapter mCommentListAdapter;
	private ArrayList<CommentOfStatus> mComments = new ArrayList<CommentOfStatus>();
	private WeiboListAdapter mWeiboListAdapter;
	// private ArrayList<Statuses> mstatuses = new ArrayList<Statuses>();
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
		// Log.e("weibo", status.toString());
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		mListView = (XListView) findViewById(R.id.comment_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mComments = new ArrayList<CommentOfStatus>();
		page = 1;
		state = 0;
		findStatusViews();
		initStatusViews();
		GetCommentToMeThread getCommentthread = new GetCommentToMeThread();
		getCommentthread.start();
		back = (ImageView) findViewById(R.id.state_detail_back);
		final RelativeLayout re = (RelativeLayout) findViewById(R.id.comment_add_emotion);
		coll = (LinearLayout) findViewById(R.id.homeblog_collection_linear);
		resend = (LinearLayout) findViewById(R.id.homeblog_resend_linear);
		good = (LinearLayout) findViewById(R.id.homeblog_good_linear);
		comment = (LinearLayout) findViewById(R.id.homeblog_comment_linear);
		sendbar = (RelativeLayout) findViewById(R.id.comment_msg_send_rl);
		add_emotion = (ImageView) findViewById(R.id.comment_add_emotion_btn);
		send = (ImageView) findViewById(R.id.comment_send_btn);
		comment_edittxt = (EditText) findViewById(R.id.comment_edittxt);
		digg_num = (TextView) findViewById(R.id.digg_num);
		digg_num.setText(String.valueOf(status.getDigg_count()));
		initHandler();
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.state_detail_back:
					Intent o = new Intent();
					// mIntent.getIntExtra("m_position", 0);
					Log.e("xxxxxxxxxxxxxxxx",
							getIntent().getIntExtra("m_position", 0)
									+ "       ");
					o.putExtra("m_position",
							getIntent().getIntExtra("m_position", 0));
					o.setClass(DoComment.this, MainActivity.class);
					startActivity(o);
					DoComment.this.finish();
					break;
				case R.id.homeblog_resend_linear:
					Intent i = new Intent(mContext, Deploy.class);
					// Intent i=new Intent(mContext,DoRepost.class);
					i.putExtra("weibo", status);
					i.putExtra("type", "repost");
					Log.e("repost", "jinxin zf");
					i.putExtra("m_position",
							getIntent().getIntExtra("m_position", 0));
					mContext.startActivity(i);
					// sendbar.setVisibility(View.VISIBLE);
					break;
				case R.id.homeblog_good_linear:
					new AddOrDelDiggThread().start();
					break;
				case R.id.homeblog_collection_linear:
					new collectionThread().start();
					break;
				case R.id.homeblog_comment_linear:
					Intent i1 = new Intent(mContext, Deploy.class);
					// Intent i=new Intent(mContext,DoComment.class);
					i1.putExtra("weibo", status);
					i1.putExtra("type", "comment");
					Log.e("comment", "jinxin pl");

					i1.putExtra("m_position",
							getIntent().getIntExtra("m_position", 0));
					mContext.startActivity(i1);

					// sendbar.setVisibility(View.VISIBLE);

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

					new CreatCommentThread().start();

					// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(DoComment.this.getCurrentFocus().getWindowToken(),
					// InputMethodManager.HIDE_NOT_ALWAYS);

					//
					break;
				}
			}
		};
		resend.setOnClickListener(ocl);
		good.setOnClickListener(ocl);
		comment.setOnClickListener(ocl);
		add_emotion.setOnClickListener(ocl);
		send.setOnClickListener(ocl);
		coll.setOnClickListener(ocl);
		back.setOnClickListener(ocl);
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case collection_create:
					String col = msg.obj.toString();
					if (col.equals("1")) {
						Toast.makeText(mContext, "收藏成功", Toast.LENGTH_LONG)
								.show();
					}
					if (col.equals("0")) {
						Toast.makeText(mContext, "收藏失败", Toast.LENGTH_LONG)
								.show();
					}
					break;
				case collection_destroy:
					String del_col = msg.obj.toString();

					Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_LONG)
							.show();

					break;
				case add_tag:

					int digg_count = Integer.parseInt(digg_num.getText()
							.toString());
					digg_num.setText(String.valueOf(digg_count + 1));
					break;
				case del_tag:

					int digg_count2 = Integer.parseInt(digg_num.getText()
							.toString());
					digg_num.setText(String.valueOf(digg_count2 - 1));
					break;
				case getcomment:
					commentstr = msg.obj.toString();

					if (commentstr.equals("[]") || commentstr.equals("null")) {
						mListView.setPullLoadEnable(false);
						Log.e("xxx->", commentstr);
						break;
					}
					mListView.setPullLoadEnable(true);
					ArrayList<CommentOfStatus> tcomments = format();

					if (tcomments.size() != 0) {
						for (CommentOfStatus s : tcomments) {
							mComments.add(s);
						}
					}
					if (state == LoadMore) {
						mCommentListAdapter.notifyDataSetChanged();
						break;
					}
					// mCommentListAdapter = new
					// CommentToMeListAdapter(status,ac,mListView,mComments,
					// new ImageLoader(mContext), mContext);
					mCommentListAdapter = new CommentToMeListAdapter(mComments,
							new ImageLoader(mContext), mContext);
					// if(mCommentListAdapter.getCount() >= 4){
					// ViewGroup.LayoutParams params =
					// mListView.getLayoutParams();
					// params.height = 100;
					// mListView.setLayoutParams(params);
					// }
					mListView.setAdapter(mCommentListAdapter);
					// if

					break;

				case createcomment:
					String s = msg.obj.toString();
					if (s.equals("1")) {
						/*
						 * 评论成功
						 */
						onRefresh();
						// GetRepostStatusThread
					}
					break;
				case repoststatus:
					String ss = msg.obj.toString();
					Log.e("zf", ss);
					if (!ss.equals("0")) {
						/*
						 * 转发成功
						 */
						// onRefresh();
					}
					break;
				}
			}

		};
	}

	private ArrayList<CommentOfStatus> format() {
		Log.e("ssss", commentstr);
		ArrayList<CommentOfStatus> comments = new ArrayList<CommentOfStatus>();
		try {
			JSONArray comments_jsonarray = new JSONArray(commentstr);
			for (int i = 0; i < comments_jsonarray.length(); i++) {
				JSONObject comment_jsonobject = comments_jsonarray
						.getJSONObject(i);
				int comment_id = Integer.parseInt(comment_jsonobject.optString(
						"comment_id", "-1"));
				String apptype = comment_jsonobject.optString("app");
				int source_id = Integer.parseInt(comment_jsonobject.optString(
						"Row_id", "-1"));
				String content = comment_jsonobject.optString("content");
				String Ctime = comment_jsonobject.optString("ctime");
				JSONObject user_jsonobject = comment_jsonobject
						.optJSONObject("user_info");
				int uid = Integer.parseInt(user_jsonobject.optString("uid",
						"-1"));
				String uname = user_jsonobject.optString("uname");
				String headicon = user_jsonobject.optString("avatar_small");
				User user = new User();
				user.setUid(uid);
				user.setUname(uname);
				user.setHeadicon(headicon);
				CommentOfStatus comment = new CommentOfStatus();
				comment.setComment_id(comment_id);
				comment.setApptype(apptype);
				comment.setContent(content);
				comment.setCtime(Ctime);
				comment.setSource_id(source_id);
				comment.setComment_user(user);
				comments.add(comment);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return comments;
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
			vv.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(status.getSmall_postimage_url(),
					imageView_content);
		} else {
			imageView_content.setVisibility(View.GONE);
			vv.setVisibility(View.GONE);
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
		vv = (View) findViewById(R.id.fenge);
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

	private class GetCommentToMeThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"comments");
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
			Log.e("re", result);
			page++;
			Message msg = new Message();
			msg.what = getcomment;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private class CreatCommentThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"comment");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("row_id",
					String.valueOf(status.getFeed_id()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content",
					comment_edittxt.getEditableText().toString());

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7);

			Message msg = new Message();
			msg.what = createcomment;
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

	// 收藏
	private class collectionThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"favorite_create");
			MyNameValuePair NameValuePair3_1 = new MyNameValuePair("act",
					"favorite_destroy");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair(
					"source_table_name", "feed");
			MyNameValuePair NameValuePair7 = new MyNameValuePair("source_id",
					String.valueOf(status.getFeed_id()));
			MyNameValuePair NameValuePair8 = new MyNameValuePair("source_app",
					"public");
			MyNameValuePair NameValuePair9 = new MyNameValuePair("format",
					"json");

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,
					NameValuePair7, NameValuePair8, NameValuePair9);
			Log.e("con", result);
			Message msg = new Message();
			msg.obj = result;
			if (result.equals("1")) {
				/*
				 * 收藏成功
				 */
				msg.what = collection_create;
			} else if (result.equals("0")) {
				/*
				 * 已收藏故取消收藏
				 */
				String del_result = MHttpClient.get(
						ThinkSNSApplication.baseUrl, NameValuePair1,
						NameValuePair2, NameValuePair3_1, NameValuePair4,
						NameValuePair5, NameValuePair6, NameValuePair7,
						NameValuePair9);
				Log.e("eee", del_result);
				if (del_result.equals("1")) {
					/*
					 * 取消收藏成功
					 */
					msg.what = collection_destroy;
				}
			}
			mHandler.sendMessage(msg);
		}

	}

	private class AddOrDelDiggThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"add_digg");
			MyNameValuePair NameValuePair3_1 = new MyNameValuePair("act",
					"del_digg");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					ac.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", ac.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("feed_id",
					String.valueOf(status.getFeed_id()));

			String add_result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			Message msg = mHandler.obtainMessage();

			if (add_result.equals("1")) {
				/*
				 * 点赞成功
				 */
				msg.what = add_tag;
			} else if (add_result.equals("0")) {
				/*
				 * 不能加赞说明以赞故取消赞(暂时发现api没有对应取消赞的代码)
				 */
				String del_result = MHttpClient.get(
						ThinkSNSApplication.baseUrl, NameValuePair1,
						NameValuePair2, NameValuePair3_1, NameValuePair4,
						NameValuePair5, NameValuePair6);
				if (del_result.equals("1")) {
					/*
					 * 取消赞成功
					 */
					msg.what = del_tag;
				}
			}
			mHandler.sendMessage(msg);
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		state = 0;
		page = 1;
		mComments.clear();
		GetCommentToMeThread getcommentsthread = new GetCommentToMeThread();
		getcommentsthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state = LoadMore;
		GetCommentToMeThread getcommentsthread = new GetCommentToMeThread();
		getcommentsthread.start();
		onLoad();
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
			Intent i = new Intent();
			// mIntent.getIntExtra("m_position", 0);
			Log.e("xxxxxxxxxxxxxxxx", getIntent().getIntExtra("m_position", 0)
					+ "       ");
			i.putExtra("m_position", getIntent().getIntExtra("m_position", 0));
			i.setClass(DoComment.this, MainActivity.class);
			startActivity(i);
			DoComment.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
