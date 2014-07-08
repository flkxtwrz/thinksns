package com.hudson.thinksns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hudson.thinksns.InfoList.InfoList_Act;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.chating.ChatActivity;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.netuti.MHttpClient;
import com.hudson.thinksns.netuti.MyNameValuePair;
import com.hudson.thinksns.notify.SystemNotification;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * @file InformMsg.java
 * @author 王代银
 * @description 系统消息的界面，在这里可以看到 谁赞了我的微博的通知消息。
 */
public class InformMsg extends Activity {

	private Button back = null;
	private ListView list = null;
	private Account account;
	private DBManager dbm;
	private Context mContext;
	private Intent mIntent;
	private String uid;
	private Handler mHandler;
	private String information;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inform_system);
		mContext = this;
		init();
		mIntent = getIntent();
		uid = mIntent.getStringExtra("uid");
		listener();
		initHandler();
		new GetSystemNotifyThread().start();
	}

	private void init() {
		dbm = new DBManager(mContext);
		account = dbm.getAccountonline();
		list = (ListView) findViewById(R.id.inform_system_list);
		back = (Button) findViewById(R.id.inform_system_btn_backup);
	}

	private void listener() {
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.inform_system_btn_backup:
					Navigate(InfoList_Act.class);
					break;
				}
			}
		};
		back.setOnClickListener(ocl);
	}

	private class GetSystemNotifyThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyNameValuePair NameValuePair1 = new MyNameValuePair("app", "api");
			MyNameValuePair NameValuePair2 = new MyNameValuePair("mod",
					"Notifytion");
			MyNameValuePair NameValuePair3 = new MyNameValuePair("act",
					"get_system_notify");
			MyNameValuePair NameValuePair4 = new MyNameValuePair("oauth_token",
					account.getOauth_token());
			MyNameValuePair NameValuePair5 = new MyNameValuePair(
					"oauth_token_secret", account.getOauth_token_secret());
			MyNameValuePair NameValuePair6 = new MyNameValuePair("user_id", uid);
			MyNameValuePair NameValuePair7 = new MyNameValuePair("format", "php");

			String result = MHttpClient.get(ThinkSNSApplication.baseUrl,
					NameValuePair1, NameValuePair2, NameValuePair3,
					NameValuePair4, NameValuePair5, NameValuePair6,NameValuePair7);

			System.out.println("result:::::::" + result);
			Message msg = new Message();
			msg.what = 0x30;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}

	}

	private void initHandler() {

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0x30:
					information = msg.obj.toString();
					System.out.println("information:::::" + information);
					List<HashMap<String, Object>> tNotify = format();
					SimpleAdapter adapter = new SimpleAdapter(mContext, tNotify, R.layout.inform_system_listitem, new String[] {"data"} ,new int[] {R.id.inform_system_itemtxt});
					list.setAdapter(adapter);
					break;
				}
			}

		};
	}

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(InformMsg.this, t);
		startActivity(i);
		InformMsg.this.finish();
	}

	private List<HashMap<String, Object>> format() {

		//ArrayList<SystemNotification> msges = new ArrayList<SystemNotification>();
		List<HashMap<String, Object>> msges = new ArrayList<HashMap<String, Object>>();
		
		String pan_title = "\" >(.*?)</a> 赞";
		Pattern pp_title =Pattern.compile(pan_title);
		Matcher mm_title = pp_title.matcher(information);
		while(mm_title.find())
		{
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("data", "\""+mm_title.group(1) +"\""+ "赞"+"了你的微博");
			msges.add(item);
		}
		return msges;

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(InformMsg.this,MainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
