package com.hudson.thinksns.chating;
/** 
* 聊天界面的listview
* @author 石仲才
* 
*/  
import java.util.ArrayList;

import com.hudson.thinksns.R;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter
{
	private ArrayList<ChatMessage> messages;
	private Context mContext;
	private ImageLoader imageloder;
	private Account account;
	private Handler mHandler;
	private ViewHolder holder;
	
	public ChatListAdapter(ArrayList<ChatMessage> messages, ImageLoader imageLoader, Context context, Account account) 
	{

		this.messages = messages;
		this.imageloder = imageLoader;
		this.account = account;
		this.mContext = context;
		initHandler();
	}
	
	private void initHandler() 
	{
		// TODO Auto-generated method stub
		mHandler = new Handler() 
		{
			@Override
			public void handleMessage(Message msg) 
			{
				// TODO Auto-generated method stub
				
				switch (msg.what) 
				{
				case 0x11:

					break;
				case 0x22:

					break;
				default:
					break;
				}
			}

		};
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
		if (arg1 != null) 
		{
		holder = (ViewHolder) arg1.getTag();
		}
		else
		{
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.pletter_messageitem, null);
			holder = new ViewHolder();
			holder.head_icon = (ImageView)arg1.findViewById(R.id.menu_chat_message_headphoto);
			holder.message_name = (TextView)arg1.findViewById(R.id.menu_chat_message_name);
			holder.message_content = (TextView)arg1.findViewById(R.id.menu_chat_message_content);
			holder.message_time = (TextView)arg1.findViewById(R.id.menu_chat_message_time);
			
			arg1.setTag(holder);
		}
		final ChatMessage message = messages.get(arg0);
		imageloder.DisplayImage(message.get_from_face(),holder.head_icon);
		holder.message_content.setText(message.get_last_content());
		holder.message_name.setText(message.get_from_uname());
		holder.message_time.setText(message.get_message_time());
		// TODO Auto-generated method stub
		return arg1;
	}

	private class ViewHolder 
	{
		TextView message_name, message_content, message_time;
		ImageView head_icon;
	}
}
