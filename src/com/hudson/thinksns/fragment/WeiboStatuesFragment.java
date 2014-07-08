package com.hudson.thinksns.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hudson.thinksns.R;
import com.hudson.thinksns.adapter.WeiboListAdapter;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.diyview.xlistview.XListView.IXListViewListener;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * 
 * @author 王代银
 * 
 */
public class WeiboStatuesFragment extends Fragment implements
		IXListViewListener {
	// public static final String User = "UserAccount";
	private String model;
	private XListView mListView;
	private Handler mHandler;
	private String weibostr = "";
	private Context mContext;
	private WeiboListAdapter mWeiboListAdapter;
	private ArrayList<Statuses> mstatuses;
	private Account account;
	private final int getweibo = 0x11;
	private final int num = 5;
	private int page = 1;
	private final int LoadMore = 0x33;
	private int state = 0;
	private int m_position;

	public int getM_position() {
		return m_position;
	}

	public void setM_position(int m_position) {
		this.m_position = m_position;
	}

	// Builder builder;
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	// WeiboStatuesFragment.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.weibostatues, container, false);
		mListView = (XListView) v.findViewById(R.id.weibo_list);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mstatuses = new ArrayList<Statuses>();

		page = 1;
		state = 0;
		initHandler();
		GetStatuesThread getstatuesthread = new GetStatuesThread();
		getstatuesthread.start();

		return v;
	}

	private class GetStatuesThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act", model);// "public_timeline"
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					String.valueOf(account.getUid()));
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

	private void initHandler() {
		// mWeiboListAdapter=new WeiboListAdapter(mstatuses, new
		// ImageLoader(mContext),mContext);
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

					// mWeiboListAdapter=new WeiboListAdapter(mstatuses, new
					// ImageLoader(mContext),mContext,account);
					mWeiboListAdapter = new WeiboListAdapter(m_position, model,
							WeiboStatuesFragment.this, mListView, mstatuses,
							new ImageLoader(mContext), mContext, account);

					mListView.setAdapter(mWeiboListAdapter);

					break;
				}
			}

		};
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

		GetStatuesThread getstatuesthread = new GetStatuesThread();
		getstatuesthread.start();
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		state = LoadMore;
		GetStatuesThread getstatuesthread = new GetStatuesThread();
		getstatuesthread.start();
		onLoad();
	}

}
