package com.hudson.thinksns;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @file InformSetting.java
 * @author 贾焱超
 * @description 右侧菜单设置（通知和提醒）。
 */
public class InformSetting extends Activity {

	private ImageView back = null;
	//private TextView atme, comment, newfan = null;
	private ImageView good, secretmsg = null;
	private ImageView timebar, gainnews = null;
	private TextView timetxt = null;
	//private TextView newstxt =null;
	private int go_state, se_state, fr_state, ca_state, ho_state = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* set it to be full screen */		
		setContentView(R.layout.setting_informsetting);

		back = (ImageView) findViewById(R.id.setting_inform_back);

		//atme = (TextView) findViewById(R.id.setting_inform_atme);
		//comment = (TextView) findViewById(R.id.setting_inform_comment);
		//newfan = (TextView) findViewById(R.id.setting_inform_newfan);
		timetxt = (TextView) findViewById(R.id.setting_inform_time_txt);
		//newstxt = (TextView) findViewById(R.id.setting_inform_gainnews_txt);

		good = (ImageView) findViewById(R.id.setting_inform_good_onoff);
		secretmsg = (ImageView) findViewById(R.id.setting_inform_secretmsg_onoff);
//		friendblog = (ImageView) findViewById(R.id.setting_inform_friendblog_onoff);
//		carefocus = (ImageView) findViewById(R.id.setting_inform_carefocus_onoff);
//		hotpush = (ImageView) findViewById(R.id.setting_inform_hotpush_onoff);
		timebar = (ImageView) findViewById(R.id.setting_inform_time);
		gainnews = (ImageView) findViewById(R.id.setting_inform_gainnews);

		Initswitch(go_state, good);
		Initswitch(se_state, secretmsg);
		//Initswitch(fr_state, friendblog);
		//Initswitch(ca_state, carefocus);
		//Initswitch(ho_state, hotpush);

		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_inform_back:
					Navigate(MainActivity.class);
					break;
//				case R.id.setting_inform_atme:
//					// TODO
//					//Navigate(InformSelectedItems.class);
//					break;
//				case R.id.setting_inform_comment:
//					// TODO
//					break;
//				case R.id.setting_inform_newfan:
//					// TODO
//					break;
				case R.id.setting_inform_good_onoff:
					// TODO
					if (go_state == 0) {
						good.setBackgroundResource(R.drawable.setting_on_btn);
						// TODO 在这里填写开启的程序

						go_state = 1;
					} else {
						good.setBackgroundResource(R.drawable.setting_off_btn);
						// TODO 在这里填写关闭的程序

						go_state = 0;
					}
					break;
				case R.id.setting_inform_secretmsg_onoff:
					// TODO
					if (se_state == 0) {
						secretmsg
								.setBackgroundResource(R.drawable.setting_on_btn);
						// TODO 在这里填写开启的程序

						se_state = 1;
					} else {
						secretmsg
								.setBackgroundResource(R.drawable.setting_off_btn);
						// TODO 在这里填写关闭的程序

						se_state = 0;
					}
					break;
//				case R.id.setting_inform_friendblog_onoff:
//					// TODO
//					if (fr_state == 0) {
//						friendblog
//								.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启的程序
//
//						fr_state = 1;
//					} else {
//						friendblog
//								.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭的程序
//
//						fr_state = 0;
//					}
//					break;
//				case R.id.setting_inform_carefocus_onoff:
//					// TODO
//					if (ca_state == 0) {
//						carefocus
//								.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启的程序
//
//						ca_state = 1;
//					} else {
//						carefocus
//								.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭的程序
//
//						ca_state = 0;
//					}
//					break;
//				case R.id.setting_inform_hotpush_onoff:
//					// TODO
//					if (ho_state == 0) {
//						hotpush.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启的程序
//
//						ho_state = 1;
//					} else {
//						hotpush.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭的程序
//
//						ho_state = 0;
//					}
//					break;
				case R.id.setting_inform_time:
				case R.id.setting_inform_time_txt:
					// TODO
					Navigate(InformTimeSet.class);
					break;
				case R.id.setting_inform_gainnews:
				//case R.id.setting_inform_gainnews_txt:
					// TODO
					break;

				}
			}
		};
		back.setOnClickListener(ocl);
//		atme.setOnClickListener(ocl);
//		comment.setOnClickListener(ocl);
//		newfan.setOnClickListener(ocl);
		good.setOnClickListener(ocl);
		secretmsg.setOnClickListener(ocl);
//		friendblog.setOnClickListener(ocl);
//		carefocus.setOnClickListener(ocl);
//		hotpush.setOnClickListener(ocl);
		timebar.setOnClickListener(ocl);
		gainnews.setOnClickListener(ocl);
		timetxt.setOnClickListener(ocl);
		//newstxt.setOnClickListener(ocl);
		Intent i =getIntent();
		
		timetxt.setText(i.getStringExtra("time"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.inform_setting, menu);
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
		i.setClass(InformSetting.this, t);
		startActivity(i);
		InformSetting.this.finish();
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
