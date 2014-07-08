package com.hudson.thinksns.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.R;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.diyview.xlistview.XListView;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.CommentOfStatus;
import com.hudson.thinksns.model.Statuses;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
/**
 * @file CommentToMeListAdapter.java
 * @author 贾焱超
 * @description 对我评论的评论列表的adapter，填充list
 */
public class CommentToMeListAdapter extends BaseAdapter {
	private ArrayList<CommentOfStatus> comments;
	Statuses statuses;
	private ImageLoader imageLoader;
	private ViewHolder holder;
	private Context mContext;	
	String strcontent;
	private int m_id;
	Account account;
	private Handler mHandler;
	private final int delete_comment = 0x24;
	private XListView list;	
	public CommentToMeListAdapter(ArrayList<CommentOfStatus> comments,
			ImageLoader imageLoader, Context context) {

		
		this.comments = comments;
		this.mContext = context;
		this.imageLoader = imageLoader;
		this.mContext = context;
		
	}
//	public CommentToMeListAdapter(final Statuses statuses,Account maccount,XListView list,ArrayList<CommentOfStatus> comments,
//			ImageLoader imageLoader, Context context) {
//
//		this.statuses = statuses;
//		this.comments = comments;
//		this.mContext = context;
//		this.imageLoader = imageLoader;
//		this.mContext = context;
//		this.account = maccount;
//		this.list = list;
//		
//		final Builder builder = new AlertDialog.Builder(context);
//		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {			
//			
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				//m_id = statuses.get(arg2 - 1).getFeed_id();
//				builder.setTitle("确定删除？");
//				builder.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(
//									DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								new DeleteCommentThread().start();
//								//onRefresh();
//							}
//						});
//
//				builder.setNegativeButton("取消",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(
//									DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								dialog.dismiss();
//							}
//						});
//				builder.create().show();
//				return false;
//			}
//		});
//		initHandler();
//	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_item, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView
					.findViewById(R.id.comm_list_content);
			holder.date = (TextView) convertView
					.findViewById(R.id.comm_list_date);
			holder.name = (TextView) convertView
					.findViewById(R.id.comm_list_username);
			holder.imageView_head = (ImageView) convertView
					.findViewById(R.id.comm_list_usericon);
			convertView.setTag(holder);
		}
		CommentOfStatus comment = comments.get(position);

		holder.content.setText(comment.getContent());
		holder.date.setText(comment.getCtime());
		holder.name.setText(comment.getComment_user().getUname());
		imageLoader.DisplayImage(comment.getComment_user().getHeadicon(),
				holder.imageView_head);
		strcontent = comment.getContent();
		return convertView;
	}

	private class ViewHolder {

		TextView content, name, date;
		ImageView imageView_head;
	}
	
	private class DeleteCommentThread extends Thread {

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
					String.valueOf(statuses.getFeed_id()));
			MyNameValuePair NameValuePair7 = new MyNameValuePair("content",
					strcontent);
			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,NameValuePair7);

			Message msg = new Message();
			msg.what = delete_comment;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}
	
	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case delete_comment:
					//S//tring col = msg.obj.toString();
					if (msg.obj.equals("1")) {
						Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT)
								.show();

					} else {
						Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				}
			}
		};
	}
}