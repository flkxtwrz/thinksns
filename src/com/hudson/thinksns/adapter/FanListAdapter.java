package com.hudson.thinksns.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.thinksns.FriendHome;
import com.hudson.thinksns.R;

import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * @file FanListAdapter.java
 * @author 石仲才
 * @description 粉丝。关注。互相关注的adapter，填充list
 */
public class FanListAdapter extends BaseAdapter {

	private ArrayList<User> fans;
	private Account account;
	private ViewHolder holder;
	private Context mContext;
	private ImageLoader imageloder;
	private Handler mHandler;
	private final int create_follow_success = 0x11;
	private final int destory_follow_success = 0x12;

	public FanListAdapter(ArrayList<User> fans, ImageLoader imageLoader,
			Context context, Account account) {

		this.fans = fans;
		this.imageloder = imageLoader;
		this.account = account;
		this.mContext = context;
		initHandler();
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				ImageView iv = (ImageView) msg.obj;
				switch (msg.what) {
				case create_follow_success:
					iv.setBackgroundResource(R.drawable.fans_cared);
					iv.setContentDescription("已关注");
					break;
				case destory_follow_success:
					iv.setBackgroundResource(R.drawable.finding_addcare);
					iv.setContentDescription("未关注");
					break;
				default:
					break;
				}
			}

		};
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fans.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.fans_item, null);
			holder = new ViewHolder();
			holder.head_icon = (ImageView) convertView
					.findViewById(R.id.fans_bloger_pic);
			holder.user_btn = (ImageView) convertView
					.findViewById(R.id.fans_bloger_addcare);
			holder.username = (TextView) convertView
					.findViewById(R.id.fans_bloger_pic_name);
			holder.fanscount = (TextView) convertView
					.findViewById(R.id.fans_bloger_fans);
			holder.userintro = (TextView) convertView
					.findViewById(R.id.fans_intro);
			holder.sex_icon = (ImageView) convertView
					.findViewById(R.id.fans_sex);
			convertView.setTag(holder);
		}
		final User fan = fans.get(position);
		imageloder.DisplayImage(fan.getHeadicon(), holder.head_icon);
		holder.username.setText(fan.getUname());
		holder.userintro.setText(fan.getIntro());
		holder.fanscount.setText("粉丝数：" + String.valueOf(fan.getFollower()));
		holder.head_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uid = String.valueOf(fan.getUid());
				Intent i = new Intent(mContext, FriendHome.class);
				i.putExtra("turnpage", 1);
				i.putExtra("uid", uid);
				mContext.startActivity(i);
			}
		});
		if (fan.getSex().equals("男")) {
			holder.sex_icon.setBackgroundResource(R.drawable.fans_sex_boy);
		} else {
			holder.sex_icon.setBackgroundResource(R.drawable.fans_sex_girl);
		}
		if (fan.getIsFollowed() == 1) {
			/*
			 * 已关注
			 */
			holder.user_btn.setBackgroundResource(R.drawable.fans_cared);
			holder.user_btn.setContentDescription("已关注");
			notifyDataSetChanged();

		}
		if (fan.getIsFollowed() == 0) {
			/*
			 * 未关注
			 */
			holder.user_btn.setBackgroundResource(R.drawable.finding_addcare);
			holder.user_btn.setContentDescription("未关注");
			notifyDataSetChanged();

		}
		holder.user_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CreateOrDestoryFollowThread(String.valueOf(fan.getUid()), v)
						.start();
			}
		});
		return convertView;
	}

	private class CreateOrDestoryFollowThread extends Thread {

		private String user_id;
		private View iv;

		public CreateOrDestoryFollowThread(String user_id, View iv) {

			this.user_id = user_id;
			this.iv = iv;
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
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id",
					user_id);
			Message msg = mHandler.obtainMessage();
			msg.obj = iv;
			if (iv.getContentDescription().equals("已关注")) {
				String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
						NameValuePair1, NameValuePair2, NameValuePair3_1,
						NameValuePair4, NameValuePair5, NameValuePair6);
				if (result.equals("0")) {
					/*
					 * 取消关注失败
					 */

				} else {
					/*
					 * 成功
					 */
					msg.what = destory_follow_success;
					;
				}
			}
			if (iv.getContentDescription().equals("未关注")) {
				String result_1 = MHttpClient.get(ThinkSNSApplication.baseUrl,
						NameValuePair1, NameValuePair2, NameValuePair3,
						NameValuePair4, NameValuePair5, NameValuePair6);

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

	private class ViewHolder {
		TextView username, userintro, fanscount;
		ImageView head_icon, user_btn, sex_icon;
	}
}
