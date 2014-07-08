package com.hudson.thinksns.channel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hudson.thinksns.R;
/** 
* 频道主界面
* @author 石仲才
*/  
public class Channel_Web_Act extends Activity
{
	private Button back;
	Channel_Weibo weiBo;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.channel_microblog);
		back = (Button)findViewById(R.id.channel_microblog_btn_backup);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent back = new Intent();
				back.setClass(Channel_Web_Act.this, ListAllChannel.class);
				startActivity(back);
				finish();
			}
		});
		weiBo = new Channel_Weibo();
		//channel_microblog_list
		getFragmentManager().beginTransaction().replace(R.id.channel_microblog_list, weiBo).commit();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		Intent back = new Intent();
		back.setClass(Channel_Web_Act.this, ListAllChannel.class);
		startActivity(back);
		finish();
		return super.onKeyDown(keyCode, event);
	}
}
