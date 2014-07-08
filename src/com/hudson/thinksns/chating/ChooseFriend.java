package com.hudson.thinksns.chating;

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

import com.hudson.thinksns.R;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
/** 
* 选择聊天朋友界面
* @author 王代银
*/  
public class ChooseFriend extends Activity
{
	private ListView friends_list;
	private DBManager dbm;
	private ImageLoader imageLoader;
	private Button back;
	private Account account;
	ArrayList<ChatFriends> chatFriends;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_chat_sendpletter);
		init();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent back = new Intent();
				back.setClass(ChooseFriend.this, ChatActivity.class);
				startActivity(back);
				finish();
			}
		});
		chatFriends = new ChatTools(account).getChatFriend();
		friends_list.setAdapter(new ChatfriendAdapter(chatFriends, imageLoader, this,account));
		
		friends_list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				// arg2
				//dataToDetail.MESSAGEID = Integer.parseInt(message_list.get(arg2).get_list_id());
				//dataToDetail.his_name = message_list.get(arg2).get_from_uname();
				String message_id = new ChatTools(account).createChat(Integer.parseInt(chatFriends.get(arg2).get_uid()));
				dataToDetail.MESSAGEID = Integer.parseInt(message_id);
				Intent i = new Intent();
				i.setClass(ChooseFriend.this, ChatingDetail.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	void init()
	{
		dbm = new DBManager(this);
		account = dbm.getAccountonline();
		imageLoader = new ImageLoader(this);
		friends_list = (ListView)findViewById(R.id.menu_chat_sendpletter_linkmanlist);
		back = (Button)findViewById(R.id.menu_chat_sendpletter_btn_backup);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		Intent back = new Intent();
		back.setClass(ChooseFriend.this, ChatActivity.class);
		startActivity(back);
		finish();
		return super.onKeyDown(keyCode, event);
	}
}
