package com.hudson.thinksns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
/**
 * @file PrivateSetting.java
 * @author 贾焱超
 * @description 隐私设置的界面
 */
public class PrivateSetting extends Activity {

	private ImageView back, cont, phone = null;
	private int con_state, pho_state = 0;
	private static final String[] WHO = { "所有人", "我关注的人" };
	private Spinner spinner = null;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.setting_privatesetting);

		back = (ImageView) findViewById(R.id.setting_private_back);
		cont = (ImageView) findViewById(R.id.setting_private_cont_onoff);
		phone = (ImageView) findViewById(R.id.setting_private_phone_onoff);
		spinner = (Spinner) findViewById(R.id.setting_private_who_comment);

		Initswitch(con_state, cont);
		Initswitch(pho_state, phone);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, WHO);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_private_back:
					Navigate(PrivateSafeSetting.class);
					break;
				case R.id.setting_private_cont_onoff:
					// TODO
					if (con_state == 0) {
						cont.setBackgroundResource(R.drawable.setting_on_btn);
						// TODO 在这里填写开启的程序

						con_state = 1;
					} else {
						cont.setBackgroundResource(R.drawable.setting_off_btn);
						// TODO 在这里填写关闭的程序

						con_state = 0;
					}
					break;
				case R.id.setting_private_phone_onoff:
					// TODO
					if (pho_state == 0) {
						phone.setBackgroundResource(R.drawable.setting_on_btn);
						// TODO 在这里填写开启的程序

						pho_state = 1;
					} else {
						phone.setBackgroundResource(R.drawable.setting_off_btn);
						// TODO 在这里填写关闭的程序

						pho_state = 0;
					}
					break;
				}
			}
		};
		back.setOnClickListener(ocl);
		cont.setOnClickListener(ocl);
		phone.setOnClickListener(ocl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.private_setting, menu);
		return true;
	}

	// 初始化开关
	private void Initswitch(int Switch, ImageView img) {
		if (Switch == 0) {
			img.setBackgroundResource(R.drawable.setting_off_btn);
		} else {
			img.setBackgroundResource(R.drawable.setting_on_btn);
		}
	}

	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(PrivateSetting.this, t);
		startActivity(i);
		PrivateSetting.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Navigate(PrivateSafeSetting.class);
		}
		return super.onKeyDown(keyCode, event);
	}

}
