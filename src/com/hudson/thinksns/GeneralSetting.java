package com.hudson.thinksns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @file GeneralSetting.java
 * @author 石仲才
 * @description 主页右侧设置菜单的通用设置界面，进行一些基本的设置
 */
public class GeneralSetting extends Activity {

	private ImageView back, update = null;
	private ImageView voice, info, other, vibra, picdownload = null;
	private TextView cacheclean = null;
	// 存储设置的状态，以控制声音的设置 1为开 0为关
	private int vo_state, in_state, ot_state, vi_state = 0;
	private int picdownload_state = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.setting_generalsetting);

		back = (ImageView) findViewById(R.id.setting_voice_back);
//		voice = (ImageView) findViewById(R.id.setting_voice_voice_onoff);
//		info = (ImageView) findViewById(R.id.setting_voice_infovoice_onoff);
//		other = (ImageView) findViewById(R.id.setting_voice_others_onoff);
//		vibra = (ImageView) findViewById(R.id.setting_voice_vabiration_onoff);
		cacheclean = (TextView) findViewById(R.id.setting_sys_cacheclean);
		picdownload = (ImageView) findViewById(R.id.setting_sys_picdownload_onoff);
		update = (ImageView) findViewById(R.id.setting_sys_update);

		//Initswitch(vo_state,voice);
		//Initswitch(in_state,info);
		//Initswitch(ot_state,other);
		//Initswitch(vi_state,vibra);
		Initswitch(picdownload_state,picdownload);
		
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_voice_back:// 返回按钮
					// TODO
					Navigate(MainActivity.class);
					break;
//				case R.id.setting_voice_voice_onoff:// 声音开关
//					if (vo_state == 0) {
//						voice.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启声音的程序
//
//						vo_state = 1;
//					} else {
//						voice.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭声音的程序
//
//						vo_state = 0;
//					}
//					break;
//				case R.id.setting_voice_infovoice_onoff:// 消息通知开关
//					if (in_state == 0) {
//						info.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启声音的程序
//
//						in_state = 1;
//					} else {
//						info.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭声音的程序
//
//						in_state = 0;
//					}
//					break;
//				case R.id.setting_voice_others_onoff:// 其他声音开关
//					if (ot_state == 0) {
//						other.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启声音的程序
//
//						ot_state = 1;
//					} else {
//						other.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭声音的程序
//
//						ot_state = 0;
//					}
//					break;
//				case R.id.setting_voice_vabiration_onoff:// 振动开关
//					if (vi_state == 0) {
//						vibra.setBackgroundResource(R.drawable.setting_on_btn);
//						// TODO 在这里填写开启声音的程序
//
//						vi_state = 1;
//					} else {
//						vibra.setBackgroundResource(R.drawable.setting_off_btn);
//						// TODO 在这里填写关闭声音的程序
//
//						vi_state = 0;
//					}
//					break;
				case R.id.setting_sys_picdownload_onoff:// 2g/3g开关
					if (picdownload_state == 0) {
						picdownload
								.setBackgroundResource(R.drawable.setting_on_btn);
						// TODO 在这里填写开启2g3g的程序

						picdownload_state = 1;
					} else {
						picdownload
								.setBackgroundResource(R.drawable.setting_off_btn);
						// TODO 在这里填写关闭2g3g的程序

						picdownload_state = 0;
					}
					break;
				case R.id.setting_sys_cacheclean:// 清理缓存
					// TODO

					break;
				case R.id.setting_sys_update:// 软件更新
					// TODO

					break;
				}
			}
		};
		back.setOnClickListener(ocl);
//		voice.setOnClickListener(ocl);
//		info.setOnClickListener(ocl);
//		other.setOnClickListener(ocl);
//		vibra.setOnClickListener(ocl);
		cacheclean.setOnClickListener(ocl);
		picdownload.setOnClickListener(ocl);
		update.setOnClickListener(ocl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.voice_reminder, menu);
		return true;
	}

	//初始化开关
	private void Initswitch(int Switch, ImageView img){
		if(Switch == 0){
			img.setBackgroundResource(R.drawable.setting_off_btn);
		}else{
			img.setBackgroundResource(R.drawable.setting_on_btn);
		}		
	}
	// 界面跳转
	private void Navigate(Class<?> t) {
		Intent i = new Intent();
		i.setClass(GeneralSetting.this, t);
		startActivity(i);
		GeneralSetting.this.finish();
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
