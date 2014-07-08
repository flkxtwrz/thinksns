package com.hudson.thinksns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * @file InformSelectedItems.java
 * @author 石仲才
 * @description 用于设置消息的通知界面（已弃用）。
 */
public class InformSelectedItems extends Activity{
	private Button back, menu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
//		/* set it to be full screen */
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.setting_inform_selecteditem);

		back = (Button) findViewById(R.id.setting_inform_selecteditem_btn_backup);
		//menu = (Button) findViewById(R.id.setting_inform_selecteditem_btn_tomenu);

		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.setting_inform_selecteditem_btn_backup:
					//Navigate(Setting.class);
					break;
//				case R.id.setting_inform_selecteditem_btn_tomenu:
//					//Navigate(MainUiActivity.class);
//					break;
				}
			}

		};
		back.setOnClickListener(ocl);
		//menu.setOnClickListener(ocl);

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
		i.setClass(InformSelectedItems.this, t);
		startActivity(i);
		InformSelectedItems.this.finish();
	}

}
