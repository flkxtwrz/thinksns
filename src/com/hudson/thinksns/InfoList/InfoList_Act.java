package com.hudson.thinksns.InfoList;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hudson.thinksns.AtMeActivity;
import com.hudson.thinksns.Fans;
import com.hudson.thinksns.InformMsg;
import com.hudson.thinksns.MainActivity;
import com.hudson.thinksns.R;
import com.hudson.thinksns.otherActivity;
import com.hudson.thinksns.application.ThinkSNSApplication;
import com.hudson.thinksns.chating.ChatActivity;
import com.hudson.thinksns.dbutils.DBManager;
import com.hudson.thinksns.model.Account;
import com.hudson.thinksns.model.Notify;
import com.hudson.thinksns.userinfo.SelfHome;

/**
 * 
 * @author  Ø÷Ÿ≤≈
 * 
 */
public class InfoList_Act extends Activity {
	TextView inform_system, inform_at, inform_comment, inform_fan,
			inform_pletter;
	RelativeLayout inform_sys, inform_a, inform_com, inform_fa, inform_ple;
	ArrayList<Notify> infos;
	private Account account;
	private DBManager dbm;
	private ImageView back;
	InfoList_Tools tools;
	int num_for_sys = 0, num_for_at = 0, num_for_com = 0, num_for_fan = 0,
			num_for_ple = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inform_home);
		init();
		handerArray();
		setListener();
	}

	void init() {
		dbm = new DBManager(this);
		account = dbm.getAccountonline();
		tools = new InfoList_Tools(account);
		back = (ImageView) findViewById(R.id.inform_home_back);
		inform_system = (TextView) findViewById(R.id.inform_system_msg_num);
		inform_at = (TextView) findViewById(R.id.inform_at_msg_num);
		inform_comment = (TextView) findViewById(R.id.inform_comment_msg_num);
		inform_fan = (TextView) findViewById(R.id.inform_fan_msg_num);
		inform_pletter = (TextView) findViewById(R.id.inform_pletter_msg_num);

		inform_sys = (RelativeLayout) findViewById(R.id.inform_system_msg);
		inform_a = (RelativeLayout) findViewById(R.id.inform_at_msg);
		inform_com = (RelativeLayout) findViewById(R.id.inform_comment_msg);
		inform_fa = (RelativeLayout) findViewById(R.id.inform_fan_msg);
		inform_ple = (RelativeLayout) findViewById(R.id.inform_pletter_msg);

		infos = tools.GetNotify();
		System.out.println("size:" + infos.size() + "/type"
				+ infos.get(0).getType());
	}

	void handerArray() {
		for (int i = 0; i < infos.size(); i++) {
			Notify notify = infos.get(i);
			if (notify.getType().equalsIgnoreCase("notify")) {
				num_for_sys = notify.getCount();
			} else if (notify.getType().equalsIgnoreCase("atme")) {
				num_for_at = notify.getCount();
			} else if (notify.getType().equalsIgnoreCase("comment")) {
				num_for_com = notify.getCount();
			} else if (notify.getType().equalsIgnoreCase("new_folower")) {
				num_for_fan = notify.getCount();
			} else if (notify.getType().equalsIgnoreCase("unread_message")) {
				num_for_ple = notify.getCount();
			}
		}

		inform_system.setText(num_for_sys + "");
		inform_at.setText(num_for_at + "");
		inform_comment.setText(num_for_com + "");
		inform_fan.setText(num_for_fan + "");
		inform_pletter.setText(num_for_ple + "");
	}

	void setListener() {
		inform_sys.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent resultIntent = new Intent(InfoList_Act.this,
						InformMsg.class);
				startActivity(resultIntent);
				finish();
			}
		});
		inform_a.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tools.SetNotifyRead("atme");
				Intent resultIntent = new Intent(InfoList_Act.this,
						AtMeActivity.class);
				startActivity(resultIntent);
				finish();
			}
		});
		inform_com.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tools.SetNotifyRead("comment");
				Intent resultIntent = new Intent(InfoList_Act.this,
						otherActivity.class);
				startActivity(resultIntent);
				finish();
			}
		});
		inform_fa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tools.SetNotifyRead("new_follower");
				Intent resultIntent = new Intent(InfoList_Act.this, Fans.class);
				resultIntent.putExtra("checkwhat", 1);
				startActivity(resultIntent);
				finish();
			}
		});
		inform_ple.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent resultIntent = new Intent(InfoList_Act.this,
						ChatActivity.class);
				startActivity(resultIntent);
				finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent(InfoList_Act.this,
						MainActivity.class);
				startActivity(resultIntent);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent resultIntent = new Intent(InfoList_Act.this,
					MainActivity.class);
			startActivity(resultIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
