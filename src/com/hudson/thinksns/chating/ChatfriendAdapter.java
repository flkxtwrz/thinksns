package com.hudson.thinksns.chating;

import java.util.ArrayList;

import com.hudson.thinksns.R;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/** 
* ¡ƒÃÏ≈Û”—listµƒadapter 
* @author º÷ÏÕ≥¨
*/  
public class ChatfriendAdapter extends BaseAdapter
{
	ArrayList<ChatFriends> chatFriends;
	private Context mContext;
	private ImageLoader imageloder;
	private Account account;
	private ViewHolder holder;

	public ChatfriendAdapter(ArrayList<ChatFriends> friends, ImageLoader imageLoader, Context context, Account account) 
	{
		this.chatFriends = friends;
		this.imageloder = imageLoader;
		this.account = account;
		this.mContext = context;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return chatFriends.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return chatFriends.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) 
	{
		final ChatFriends friends = chatFriends.get(arg0);
		if (arg1 != null) 
		{
			holder = (ViewHolder) arg1.getTag();
		}
		else
		{
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.chat_friend_list_item, null);
			holder = new ViewHolder();
			holder.head_icon = (ImageView)arg1.findViewById(R.id.chat_friend_list_headphoto);
			holder.uname = (TextView)arg1.findViewById(R.id.chat_friend_list_name);
			
			arg1.setTag(holder);
		}
		imageloder.DisplayImage(friends.get_user_hander(),holder.head_icon);
		holder.uname.setText(friends.get_uname());
		// TODO Auto-generated method stub
		return arg1;
	}

	private class ViewHolder 
	{
		TextView uname;
		ImageView head_icon;
	}
}
