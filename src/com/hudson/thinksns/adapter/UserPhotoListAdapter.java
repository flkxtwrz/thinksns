package com.hudson.thinksns.adapter;

import java.util.ArrayList;


import com.hudson.thinksns.R;

import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.UserPhoto;
import com.hudson.thinksns.statusparse.WeiBoPrase;
import com.hudson.thinksns.timeutil.TimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @file UserPhotoListAdapter.java
 * @author 王代银
 * @description 个人主页中我的照片的adapter，填充grid
 */
public class UserPhotoListAdapter extends BaseAdapter{
	private ArrayList<UserPhoto> photos;
	private ViewHolder holder;
	private ImageLoader imageloder;
	private Context mContext;

	public UserPhotoListAdapter(ArrayList<UserPhoto> photos,
			ImageLoader imageloder,Context context) {
		super();
		this.photos = photos;
		this.imageloder = imageloder;
		this.mContext=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photos.size();
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
		if(convertView!=null){
			holder=(ViewHolder) convertView.getTag();
		}else{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.self_photo_item, null);
			holder=new ViewHolder();
			holder.photo_content=(TextView) convertView.findViewById(R.id.photo_content);
			holder.photo_iv=(ImageView) convertView.findViewById(R.id.self_photo_image);
			holder.photo_time=(TextView) convertView.findViewById(R.id.photo_time);
			convertView.setTag(holder);
		}
		UserPhoto temp_photo=photos.get(position);
		holder.photo_content.setText(WeiBoPrase.parseContent(temp_photo.getPhoto_content(), mContext));
		holder.photo_time.setText(TimeUtil.totime(Long.parseLong(temp_photo.getPhoto_time())));
		imageloder.DisplayImage(temp_photo.getPhoto_url(), holder.photo_iv);
		return convertView;
	}
	private class ViewHolder {
	    TextView photo_content,photo_time;  
	    ImageView photo_iv;
	}
}
