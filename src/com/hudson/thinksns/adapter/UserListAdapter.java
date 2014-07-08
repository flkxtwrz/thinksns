package com.hudson.thinksns.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.thinksns.LoginActivity;
import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
/**
 * @file UserListAdapter.java
 * @author 贾焱超
 * @description 账户管理中账户的adapter，填充list
 */
public class UserListAdapter extends BaseAdapter {

	private ArrayList<Account> userlist;
	private AdapterViewHolder holder;
	private Activity act;
	private DBManager dbm;
	private UserListAdapter adapter;
	private View cv;
	private ImageLoader imageLoader;

	public UserListAdapter(ArrayList<Account> userlist, Activity mActivity,
			DBManager dbm, ImageLoader imageloader) {
		super();
		this.userlist = userlist;
		this.act = mActivity;
		this.dbm = dbm;
		this.adapter = this;
		this.imageLoader = imageloader;
	}

	private class AdapterViewHolder {
		Button butlf;
		ImageView head_icon;
		ImageView ac_on;
		TextView uname;
		Button butrt;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userlist.size() + 1;
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
		if (position == userlist.size()) {
			convertView = LayoutInflater.from(act).inflate(R.layout.addacc,
					null);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(act, LoginActivity.class);
					intent.putExtra("add", "add");
					act.startActivity(intent);

				}
			});
			convertView.setTag("addbottom");
		}

		else {
			if (convertView != null
					&& !convertView.getTag().equals("addbottom")) {
				holder = (AdapterViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(act).inflate(
						R.layout.user_item, null);
				holder = new AdapterViewHolder();
				holder.butlf = (Button) convertView.findViewById(R.id.butlf);
				holder.head_icon = (ImageView) convertView
						.findViewById(R.id.head_icon);
				holder.uname = (TextView) convertView.findViewById(R.id.uname);
				holder.butrt = (Button) convertView.findViewById(R.id.butrt);
				//holder.ac_on = (ImageView) convertView.findViewById(R.id.ac_on);
				convertView.setTag(holder);

			}
			Account u = userlist.get(position);
			holder.uname.setText(u.getUname());
			if (u.getState() != 0) {
				//holder.ac_on.setVisibility(View.VISIBLE);
				//holder.ac_on.setTag("on");
			} else {
				//holder.ac_on.setVisibility(View.GONE);
				//holder.ac_on.setTag("off");
			}
			imageLoader.DisplayImage(u.getHeadicon(), holder.head_icon);
			holder.butrt.setOnClickListener(new lvButtonListener(u.getUid()));
			holder.butlf.setOnClickListener(new lvButtonListener(u.getUid()));
			convertView.setOnClickListener(new itemclickListener(u.getUid()));
		}

		return convertView;
	}

	class itemclickListener implements OnClickListener {
		private int muid;

		itemclickListener(int uid) {
			muid = uid;

		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			// 先找出在线的
			Account ac_on = dbm.getAccountonline();
			// 选择的
			Account ac_check = dbm.findUserByUid(muid);
			if (ac_on.getUid() != 0 && ac_on.getUid() != muid) {
				ac_on.setState(0);
				ac_check.setState(1);
				dbm.update(ac_on);
				dbm.update(ac_check);
				adapter.userlist = dbm.findAllUser();
				adapter.notifyDataSetChanged();
				Intent i = new Intent();
				i.setClass(act, MainActivity.class);
				act.startActivity(i);
				act.finish();
			}
		}
	}

	class lvButtonListener implements OnClickListener {
		private int muid;

		// private int cflag=0;
		lvButtonListener(int uid) {
			muid = uid;

		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			final TranslateAnimation mShowAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.75f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(500);
			final TranslateAnimation mHiddenAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.75f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mHiddenAction.setDuration(500);
			if (cv != null && !cv.equals(v)) {
				// 两次点击的item不一样
				((View) cv.getParent()).findViewById(R.id.butlf)
						.setBackgroundResource(R.drawable.del);
				((View) cv.getParent()).findViewById(R.id.butrt).setAnimation(
						mHiddenAction);
				((View) cv.getParent()).findViewById(R.id.butrt).setVisibility(
						View.GONE);
			}
			cv = v;
			if (vid == holder.butrt.getId()) {
				// 点击删除键
				dbm.deleteByUid(muid);
				Log.e("alluser", dbm.findAllUser().toString());
				adapter.userlist = dbm.findAllUser();
				adapter.notifyDataSetChanged();
			} else if (vid == holder.butlf.getId()) {

				if (((View) v.getParent()).findViewById(R.id.butrt)
						.getVisibility() == View.GONE) {
					Log.e("cf", ((View) v.getParent()).findViewById(R.id.butlf)
							.toString());
					((View) v.getParent()).findViewById(R.id.butlf)
							.setBackgroundResource(R.drawable.del);
					((View) cv.getParent()).findViewById(R.id.butrt)
							.setAnimation(mShowAction);
					((View) v.getParent()).findViewById(R.id.butrt)
							.setVisibility(View.VISIBLE);
				} else {
					Log.e("cf", ((View) v.getParent()).findViewById(R.id.butlf)
							.toString());
					((View) v.getParent()).findViewById(R.id.butlf)
							.setBackgroundResource(R.drawable.del);
					((View) v.getParent()).findViewById(R.id.butrt)
							.setAnimation(mHiddenAction);
					((View) v.getParent()).findViewById(R.id.butrt)
							.setVisibility(View.GONE);
				}

			}

		}
	}
}
