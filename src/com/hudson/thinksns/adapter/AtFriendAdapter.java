package com.hudson.thinksns.adapter;

import java.util.ArrayList;

import com.hudson.thinksns.R;

import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @file AtFriendAdapter.java
 * @author 石仲才
 * @description @朋友的adapter，用于填充list
 */
public class AtFriendAdapter extends BaseAdapter {
	private ArrayList<User> friends;
	private ViewHolder holder;
	private Context mContext;
	private ImageLoader imageLoader;

	public AtFriendAdapter(ArrayList<User> friends, Context context) {
		this.mContext = context;
		this.imageLoader = new ImageLoader(context);
		this.friends = friends;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friends.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
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
					R.layout.emotion_item, null);
			holder.text = (TextView) convertView
					.findViewById(R.id.emotion_text);
			holder.img = (ImageView) convertView
					.findViewById(R.id.emotion_image);
			convertView.setTag(holder);
		}
		User user = friends.get(position);
		holder.text.setText(user.getUname());
		imageLoader.DisplayImage(user.getHeadicon(), holder.img);
		return convertView;
	}

	private class ViewHolder {

		TextView text;
		ImageView img;
	}

}
