package com.hudson.thinksns.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.Deploy;
import com.hudson.thinksns.DoComment;
import com.hudson.thinksns.FriendHome;
import com.hudson.thinksns.R;
import com.hudson.thinksns.UserInfoActivity;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.fragment.WeiboStatuesFragment;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.statusparse.WeiBoPrase;
import com.hudson.thinksns.timeutil.TimeUtil;
import com.hudson.thinksns.userinfo.SelfHome;
/**
 * @file WeiboListAdapter.java
 * @author 贾焱超
 * @description 微博列表的adapter，填充微博list
 */
public class WeiboListAdapter extends BaseAdapter {
	private ArrayList<Statuses> statuses;
	private Account account;
	private ImageLoader imageLoader;
	private ViewHolder holder;
	private Context mContext;
	private String[] platfroms = { "来自网页版", "来自手机网页版", "来自android版",
			"来自iphone版", "来自ipad版", };
	private Handler mHandler;
	private final int add_tag = 0x11;
	private final int del_tag = 0x22;
	private int m_id;
	private final int delete_weibo = 1;
	private XListView list;
	private int m_position;
	// 判断微博类型
	private String model;


	public WeiboListAdapter(ArrayList<Statuses> statuses,
			ImageLoader imageLoader, Context context, Account maccount) {

		this.statuses = statuses;
		this.imageLoader = imageLoader;
		this.mContext = context;
		this.account = maccount;
		initHandler();
	}

	public WeiboListAdapter(int position, String model,
			final WeiboStatuesFragment w, XListView list,
			final ArrayList<Statuses> statuses, ImageLoader imageLoader,
			Context context, Account maccount) {
		this.model = model;
		this.statuses = statuses;
		this.imageLoader = imageLoader;
		this.mContext = context;
		this.account = maccount;
		this.m_position = position;
		this.list = list;
		final Builder builder = new AlertDialog.Builder(context);
		this.list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, DoComment.class);
				i.putExtra("weibo", statuses.get(arg2 - 1));
				i.putExtra("m_position", m_position);
				mContext.startActivity(i);
			}
		});
		if (model.equals("user_timeline")) {
			this.list
					.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							m_id = statuses.get(arg2 - 1).getFeed_id();
							builder.setTitle("确定删除？");
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											new DeleteStatuesThread().start();
											w.onRefresh();
										}
									});

							builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									});
							builder.create().show();
							return false;
						}
					});
		}
		initHandler();
	}

	public WeiboListAdapter(int position, String model,
			final Fragment w, XListView list,
			final ArrayList<Statuses> statuses, ImageLoader imageLoader,
			Context context, Account maccount) {
		this.model = model;
		this.statuses = statuses;
		this.imageLoader = imageLoader;
		this.mContext = context;
		this.account = maccount;
		this.m_position = position;
		this.list = list;
		final Builder builder = new AlertDialog.Builder(context);
		this.list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, DoComment.class);
				i.putExtra("weibo", statuses.get(arg2 - 1));
				i.putExtra("m_position", m_position);
				mContext.startActivity(i);
			}
		});

		initHandler();
	}
	
	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				// Log.e("dianz", digg_count + "");
				switch (msg.what) {
				case add_tag:
					TextView holder = (TextView) msg.obj;
					int digg_count = Integer.parseInt(holder.getText()
							.toString());
					holder.setText(String.valueOf(digg_count + 1));
					break;
				case del_tag:
					TextView holder2 = (TextView) msg.obj;
					int digg_count2 = Integer.parseInt(holder2.getText()
							.toString());
					holder2.setText(String.valueOf(digg_count2 - 1));
					break;
				case delete_weibo:
					if (msg.obj.equals("1")) {
						Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT)
								.show();

					} else {
						Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;

				default:

				}
			}

		};
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return statuses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.weibo_item, null);
			holder = new ViewHolder();
			holder.rel = (RelativeLayout) convertView
					.findViewById(R.id.homeblog_state_resend);
			holder.text = (TextView) convertView
					.findViewById(R.id.homeblog_state_contents);
			holder.name = (TextView) convertView
					.findViewById(R.id.homeblog_state_username);
			holder.date = (TextView) convertView
					.findViewById(R.id.homeblog_state_date);
			holder.from = (TextView) convertView
					.findViewById(R.id.homeblog_state_from);
			holder.repost_linear = (LinearLayout) convertView
					.findViewById(R.id.homeblog_resend_linear);
			holder.repost = (TextView) convertView
					.findViewById(R.id.homeblog_resend_num);
			holder.digg_linear = (LinearLayout) convertView
					.findViewById(R.id.homeblog_good_linear);
			holder.digg = (TextView) convertView
					.findViewById(R.id.homeblog_digg_num);
			holder.comment_linear = (LinearLayout) convertView
					.findViewById(R.id.homeblog_comment_linear);

			holder.comments = (TextView) convertView
					.findViewById(R.id.homeblog_comment_num);
			holder.imageView_head = (ImageView) convertView
					.findViewById(R.id.homeblog_state_userpic);
			holder.imageView_content = (ImageView) convertView
					.findViewById(R.id.homeblog_state_photos);
			holder.otext = (TextView) convertView
					.findViewById(R.id.homeblog_resend_contents);
			holder.oname = (TextView) convertView
					.findViewById(R.id.homeblog_resend_username);
			holder.odate = (TextView) convertView
					.findViewById(R.id.homeblog_resend_date);
			holder.ofrom = (TextView) convertView
					.findViewById(R.id.homeblog_resend_from);
			// holder.orepost=(TextView)convertView.findViewById(R.id.resend_homeblog_resend_num);
			// holder.odigg=(TextView)
			// convertView.findViewById(R.id.resend_homeblog_digg_num);
			// holder.ocomments=(TextView)
			// convertView.findViewById(R.id.resend_homeblog_comment_num);
			holder.oimageView_head = (ImageView) convertView
					.findViewById(R.id.homeblog_resend_userpic);
			holder.oimageView_content = (ImageView) convertView
					.findViewById(R.id.homeblog_resend_photos);
			convertView.setTag(holder);
		}
		final Statuses status = statuses.get(position);
		final int userid = status.getUser().getUid();
		//表情解析
		holder.text.setText(WeiBoPrase.parseContent(status.getFeed_content(),
				mContext));
		holder.date.setText(TimeUtil.totime(Long.parseLong(status
				.getPublish_time())));
		holder.repost.setText(String.valueOf(status.getRepost_count()));
		holder.comments.setText(String.valueOf(status.getComment_count()));
		holder.digg.setText(String.valueOf(status.getDigg_count()));
		
		holder.digg_linear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AddOrDelDiggThread(String.valueOf(status.getFeed_id()),
						(TextView) v.findViewById(R.id.homeblog_digg_num))
						.start();
			}
		});
		holder.comment_linear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, Deploy.class);
				// Intent i=new Intent(mContext,DoComment.class);
				i.putExtra("title", "评论");
				i.putExtra("weibo", status);
				i.putExtra("type", "comment");
				i.putExtra("m_position", m_position);
				Log.e("comment", "jinxin pl");
				mContext.startActivity(i);

			}
		});
		holder.repost_linear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, Deploy.class);
				// Intent i=new Intent(mContext,DoRepost.class);
				i.putExtra("title", "转发");
				i.putExtra("weibo", status);
				i.putExtra("type", "repost");
				i.putExtra("m_position", m_position);
				Log.e("repost", "jinxin zf");
				mContext.startActivity(i);

			}
		});
		holder.name.setText(status.getUser().getUname());
		holder.from.setText(platfroms[Integer.parseInt(status.getFrom())]);
		imageLoader.DisplayImage(status.getUser().getHeadicon(),
				holder.imageView_head);
		holder.imageView_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uid = String.valueOf(userid);
				Intent i = new Intent(mContext, FriendHome.class);
				i.putExtra("m_position", m_position);
				i.putExtra("uid", uid);
				mContext.startActivity(i);

			}
		});
		if (status.getFeed_type().equals("repost")) {
			final int ouserid = status.getSource_status().getUser().getUid();
			holder.rel.setVisibility(View.VISIBLE);
			holder.otext.setText(WeiBoPrase.parseContent(status
					.getSource_status().getFeed_content(), mContext));
			holder.odate.setText(TimeUtil.totime(Long.parseLong(status
					.getSource_status().getPublish_time())));
			// holder.orepost.setText(String.valueOf(status.getSource_status().getRepost_count()));
			// holder.ocomments.setText(String.valueOf(status.getSource_status().getComment_count()));
			// holder.odigg.setText(String.valueOf(status.getSource_status().getDigg_count()));
			holder.oname
					.setText(status.getSource_status().getUser().getUname());
			holder.ofrom.setText(platfroms[Integer.parseInt(status
					.getSource_status().getFrom())]);
			imageLoader.DisplayImage(status.getSource_status().getUser()
					.getHeadicon(), holder.oimageView_head);
			holder.oimageView_head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String uid = String.valueOf(ouserid);
					Intent i = new Intent(mContext, FriendHome.class);
					i.putExtra("m_position", m_position);
					i.putExtra("uid", uid);
					mContext.startActivity(i);

				}
			});
			holder.rel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(mContext,DoComment.class);
					//i.putExtra("weibo", status.getSource_status());
					System.out.println(status);
					System.out.println(status.getSource_status());
					//i.putExtra("m_position", m_position);
					//mContext.startActivity(i);
				}
			});	
			if (status.getSource_status().getFeed_type().equals("postimage")) {
				holder.oimageView_content.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(status.getSource_status()
						.getSmall_postimage_url(), holder.oimageView_content);
			} else {
				holder.oimageView_content.setVisibility(View.GONE);
			}
		} else {
			holder.rel.setVisibility(View.GONE);
		}
		if (status.getFeed_type().equals("postimage")) {

			holder.imageView_content.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(status.getSmall_postimage_url(),
					holder.imageView_content);
			System.out.println("img::::"+status);
			System.out.println("img::::"+status.getSmall_postimage_url());
		} else {
			holder.imageView_content.setVisibility(View.GONE);

		}
		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent i = new Intent(mContext, DoComment.class);
		// i.putExtra("weibo", status);
		// mContext.startActivity(i);
		//
		// // TODO Auto-generated method stub
		//
		// }
		// });
		return convertView;
	}

	private class DeleteStatuesThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"WeiboStatuses");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"destroy");// "public_timeline"
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("id",
					String.valueOf(m_id));
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);

			Message msg = new Message();
			msg.what = delete_weibo;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	public class AddOrDelDiggThread extends Thread {
		private String feed_id;
		private TextView mHolder;

		public AddOrDelDiggThread(String feed_id, TextView holder) {
			this.mHolder = holder;
			this.feed_id = feed_id;
		}

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
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("feed_id",
					feed_id);

			String add_result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6);
			Message msg = mHandler.obtainMessage();
			msg.obj = mHolder;
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
				Log.e("eeee", del_result);
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

	private class ViewHolder {
		RelativeLayout rel;
		LinearLayout repost_linear, digg_linear, comment_linear;
		TextView text, name, date, repost, comments, digg, from;
		TextView otext, oname, odate, orepost, ocomments, odigg, ofrom;
		ImageView imageView_head, imageView_content;
		ImageView oimageView_head, oimageView_content;
	}
}
