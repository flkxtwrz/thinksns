package com.hudson.thinksns.chating;
/** 
* 聊天内容界面
* @author 贾焱超
*/  
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.R;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.imageloader.ImageLoader;
import com.hudson.thinksns.model.Account;

public class ChatingDetail extends Activity
{
	private Account account;
	private DBManager dbm;
	private ListView chat_list;
	private ImageLoader imageLoader;
	private TextView hand_name;
	private ImageView send_message,back;
	private EditText send_content;
	private Timer mTimer;
	private TimerTask mTimeTask;
	private Handler mHandler;
	private Long timegap = 1500L;// 1秒
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.private_msg);
		init();
		initHandler();
		ArrayList<MessageDetail> message_list = new ChatTools(account).getChatDetail(dataToDetail.MESSAGEID);
		chat_list.setAdapter(new ChatingAdapter(message_list, imageLoader, this,account));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent back = new Intent();
				back.setClass(ChatingDetail.this, ChatActivity.class);
				startActivity(back);
				finish();
			}
		});
		mTimer = new Timer();
		mTimeTask = new TimerTask() 
		{
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				Message msg = mHandler.obtainMessage();
				msg.what = 0x11;
				mHandler.sendMessage(msg);
			}
		};
		mTimer.schedule(mTimeTask, 1000, timegap);// 1s 每间隔1秒刷新消息推送
		send_message.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				if(send_content.getText().toString().equals(""))
				{
					Toast.makeText(ChatingDetail.this, "请输入", Toast.LENGTH_SHORT).show();
				}
				else
				{
					new ChatTools(account).replyChat(dataToDetail.MESSAGEID,send_content.getText().toString());
					send_content.setText("");
					ArrayList<MessageDetail> message_list = new ChatTools(account).getChatDetail(dataToDetail.MESSAGEID);
					chat_list.setAdapter(new ChatingAdapter(message_list, imageLoader, ChatingDetail.this,account));
				}
			}
			
		});
	}
	
	void init()
	{
		dbm = new DBManager(this);
		account = dbm.getAccountonline();
		imageLoader = new ImageLoader(this);
		chat_list = (ListView)findViewById(R.id.private_msg_list);
		hand_name = (TextView)findViewById(R.id.hand_name);
		hand_name.setText(dataToDetail.his_name);
		send_message = (ImageView)findViewById(R.id.private_send_btn);
		send_content = (EditText)findViewById(R.id.private_msg_edittxt);
		back = (ImageView)findViewById(R.id.private_msg_back);
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
					ArrayList<MessageDetail> message_list = new ChatTools(account).getChatDetail(dataToDetail.MESSAGEID);
					chat_list.setAdapter(new ChatingAdapter(message_list, imageLoader, ChatingDetail.this,account));
					break;
				default:
					break;
				}
			}

		};
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		Intent back = new Intent();
		back.setClass(ChatingDetail.this, ChatActivity.class);
		startActivity(back);
		finish();
		return super.onKeyDown(keyCode, event);
	}
}
