package com.hudson.thinksns.chating;
/** 
* 聊天界面的list的adapter
* @author 王代银
*/  
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

public class ChatingAdapter extends BaseAdapter
{
	private ArrayList<MessageDetail> messages;
	private Context mContext;
	private ImageLoader imageloder;
	private Account account;
	private ViewHolder holder;
	
	public ChatingAdapter(ArrayList<MessageDetail> messages, ImageLoader imageLoader, Context context, Account account) 
	{
		this.messages = messages;
		this.imageloder = imageLoader;
		this.account = account;
		this.mContext = context;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return messages.get(arg0);
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
		final MessageDetail message = messages.get(arg0);
		if (arg1 != null) 
		{
			holder = (ViewHolder) arg1.getTag();
		}
		else
		{
			if(message.from_uname.equals(account.getUname()))
			{
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.private_letter_right, null);
				holder = new ViewHolder();
				holder.head_icon = (ImageView)arg1.findViewById(R.id.private_msg_going_userpic_r);
				holder.content = (TextView)arg1.findViewById(R.id.private_msg_going_msg_r);
			}
			else
			{
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.private_msg_chat_left, null);
				holder = new ViewHolder();
				holder.head_icon = (ImageView)arg1.findViewById(R.id.private_msg_coming_userpic);
				holder.content = (TextView)arg1.findViewById(R.id.private_msg_coming_msg);
			}
			
			arg1.setTag(holder);
		}
		imageloder.DisplayImage(message.from_face,holder.head_icon);
		holder.content.setText(message.content);
		// TODO Auto-generated method stub
		return arg1;
	}
	
	private class ViewHolder 
	{
		TextView content;
		ImageView head_icon;
	}
}
