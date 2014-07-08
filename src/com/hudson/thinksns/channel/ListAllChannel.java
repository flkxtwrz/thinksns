package com.hudson.thinksns.channel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
/** 
* 列出频道列表
* @author 石仲才
*/  
public class ListAllChannel extends Activity
{
	private ListView channel_list;
	private DBManager dbm;
	private ImageLoader imageLoader;
	private Account account;
	ArrayList<Channel> Channels;
	private Button back;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.channel_menu);
		init();
		Channels = new ChannelTools(account).GetChannel();
		channel_list.setAdapter(new ChannelAdapter(Channels, imageLoader, this,account));
		
		channel_list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
//				String message_id = new ChatTools(account).createChat(Integer.parseInt(chatFriends.get(arg2).get_uid()));
//				dataToDetail.MESSAGEID = Integer.parseInt(message_id);
				Intent i = new Intent();
				dataToFrag.id = Channels.get(arg2).get_channel_category_id();
				//i.putExtra("c_id",Channels.get(arg2).get_channel_category_id());
				i.setClass(ListAllChannel.this, Channel_Web_Act.class);
				startActivity(i);
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent back = new Intent();
				back.setClass(ListAllChannel.this, MainActivity.class);
				startActivity(back);
				finish();
			}
		});
	}
	
	void init()
	{
		dbm = new DBManager(this);
		account = dbm.getAccountonline();
		imageLoader = new ImageLoader(this);
		channel_list = (ListView)findViewById(R.id.channel_list);
		back = (Button)findViewById(R.id.channel_btn_backup);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		Intent back = new Intent();
		back.setClass(ListAllChannel.this, MainActivity.class);
		startActivity(back);
		finish();
		return super.onKeyDown(keyCode, event);
	}
}
