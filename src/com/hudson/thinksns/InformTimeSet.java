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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
/**
 * @file InformTimeSet.java
 * @author 王代银
 * @description 通知和提醒设置中，用于设置免打扰时间的界面。
 */
public class InformTimeSet extends Activity {

	private Button back, menu = null;
	private TimePicker tp1, tp2 = null;
	private TextView tv1, tv2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_inform_time_selected);
		tp1 = (TimePicker) findViewById(R.id.setting_inform_timePicker_start);
		tp2 = (TimePicker) findViewById(R.id.setting_inform_timePicker_end);
		back = (Button) findViewById(R.id.setting_inform_times_btn_backup);
		// menu = (Button) findViewById(R.id.setting_inform_times_btn_tomenu);
		tv1 = (TextView) findViewById(R.id.setting_inform_times_txtshow1);
		tv2 = (TextView) findViewById(R.id.setting_inform_times_txtshow2);
		tv1.setText("每日" + tp1.getCurrentHour() + ":"
				+ tp1.getCurrentMinute() + "-");
		tv2.setText("次日" + tp2.getCurrentHour() + ":"
				+ tp2.getCurrentMinute());
		tp1.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				tv1.setText("每日" + tp1.getCurrentHour() + ":"
						+ tp1.getCurrentMinute() + "-");
			}
		}); 
		tp2.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				tv2.setText("次日" + tp2.getCurrentHour() + ":"
						+ tp2.getCurrentMinute());
			}
		});
		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_inform_times_btn_backup:
					String time = tv1.getText()+""+tv2.getText();
					Intent i = new Intent();
					i.putExtra("time", time);
					i.setClass(InformTimeSet.this, InformSetting.class);
					startActivity(i);
					InformTimeSet.this.finish();
					break;
				// case R.id.setting_inform_times_btn_tomenu:
				// Navigate(InformSetting.class);
				// //Navigate(MainUiActivity.class);
				// break;
				}
			}

		};
		back.setOnClickListener(ocl);
		// menu.setOnClickListener(ocl);

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
		i.setClass(InformTimeSet.this, t);
		startActivity(i);
		InformTimeSet.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Navigate(InformSetting.class);
		}
		return super.onKeyDown(keyCode, event);
	}

}
