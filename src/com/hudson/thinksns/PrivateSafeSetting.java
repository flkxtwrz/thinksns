package com.hudson.thinksns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
/**
 * @file PrivateSafeSetting.java
 * @author 石仲才
 * @description 隐私安全设置的界面
 */
public class PrivateSafeSetting extends Activity {

	private Button back, menu = null;
	private RelativeLayout privateset, account, pletter, mblog, black = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.setting_privatesafe_details);

		back = (Button) findViewById(R.id.setting_safe_btn_backup);
		//menu = (Button) findViewById(R.id.setting_safe_btn_tomenu);
		privateset = (RelativeLayout) findViewById(R.id.setting_safe_privaty);
		account = (RelativeLayout) findViewById(R.id.setting_safe_accounts);
		pletter = (RelativeLayout) findViewById(R.id.setting_safe_privateletter);
		mblog = (RelativeLayout) findViewById(R.id.setting_safe_microblog);
		black = (RelativeLayout) findViewById(R.id.setting_safe_blacklist);

		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_safe_btn_backup:
					Navigate(MainActivity.class);
					break;
//				case R.id.setting_safe_btn_tomenu:
//					//Navigate(MainUiActivity.class);
//					break;
				case R.id.setting_safe_privaty:
					Navigate(PrivateSetting.class);
					break;
				case R.id.setting_safe_accounts:
					Navigate(AccountSafe.class);
					break;
				case R.id.setting_safe_privateletter:
					//在此写代码：跳转显示已屏蔽私信的人
					break;
				case R.id.setting_safe_microblog:
					//在此写代码：跳转显示已屏蔽微博的人
					break;
				case R.id.setting_safe_blacklist:
					//在此写代码：跳转显示黑名单的人
					break;
				}
			}

		};
		back.setOnClickListener(ocl);
		//menu.setOnClickListener(ocl);
		privateset.setOnClickListener(ocl);
		account.setOnClickListener(ocl);
		pletter.setOnClickListener(ocl);
		mblog.setOnClickListener(ocl);
		black.setOnClickListener(ocl);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.private_setting, menu);
		return true;
	}

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(PrivateSafeSetting.this, t);
		startActivity(i);
		PrivateSafeSetting.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Navigate(MainActivity.class);
		}
		return super.onKeyDown(keyCode, event);
	}
}
