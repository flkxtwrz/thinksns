package com.hudson.thinksns.chating;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.hudson.thinksns.*;
import com.hudson.thinksns.adapter.FanListAdapter;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.develop.DateToFragment;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
/** 
* 聊天界面
* @author 王代银
*/  
public class ChatActivity extends Activity
{
	private Account account;
	private DBManager dbm;
	private ListView chat_list;
	private Button back;
	private ImageLoader imageLoader;
	private Timer mTimer;
	private TimerTask mTimeTask;
	private Handler mHandler;
	private Long timegap = 5 * 1000L;// 10秒
	ArrayList<ChatMessage> message_list;
	Button write_new;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_chat);
		init();
		initHandler();
		ChatTools chattest = new ChatTools(account);
		//new ChatTools(account).getChatDetail(3);
		message_list = new ChatTools(account).getMessageList();
		chat_list.setAdapter(new ChatListAdapter(message_list, imageLoader, this,account));
		
		chat_list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				// arg2
				dataToDetail.MESSAGEID = Integer.parseInt(message_list.get(arg2).get_list_id());
				dataToDetail.his_name = message_list.get(arg2).get_from_uname();
				Intent i = new Intent();
				i.setClass(ChatActivity.this, ChatingDetail.class);
				startActivity(i);
				finish();
			}
		});
		
		chat_list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() 
		{  
			@Override  
			public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
			{  
				menu.setHeaderTitle("确定删除？");     
				menu.add(0, 0, 0, "删除");     
			} 
		});
		
		write_new.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent();
				i.setClass(ChatActivity.this, ChooseFriend.class);
				startActivity(i);
				finish();
			}
			
		});
//		mTimer = new Timer();
//		mTimeTask = new TimerTask() 
//		{
//			@Override
//			public void run() 
//			{
//				// TODO Auto-generated method stub
//				Message msg = mHandler.obtainMessage();
//				msg.what = 0x11;
//				mHandler.sendMessage(msg);
//			}
//		};
//		mTimer.schedule(mTimeTask, 1000, timegap);// 1s 每间隔1秒刷新消息推送
	}
	
	void init()
	{
		dbm = new DBManager(this);
		account = dbm.getAccountonline();
		imageLoader = new ImageLoader(this);
		chat_list = (ListView)findViewById(R.id.menu_chat_list);
		write_new = (Button)findViewById(R.id.menu_chat_btn_pletter);
		back = (Button)findViewById(R.id.menu_chat_btn_backup);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ChatActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}
		});
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
					message_list = new ChatTools(account).getMessageList();
					chat_list.setAdapter(new ChatListAdapter(message_list, imageLoader, ChatActivity.this,account));
					break;
				default:
					break;
				}
			}

		};
	}
	
	public boolean onContextItemSelected(MenuItem item) 
	{  
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        System.out.println("点击了list里面的第"+menuInfo.position+"个项目"); 
        new ChatTools(account).deleteChat(Integer.parseInt(message_list.get(menuInfo.position).get_list_id()));
        Message msg = mHandler.obtainMessage();
		msg.what = 0x11;
		mHandler.sendMessage(msg);

        return super.onContextItemSelected(item);  
    } 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(ChatActivity.this,MainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
