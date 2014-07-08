package com.hudson.thinksns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hudson.thinksns.develop.BlogDetail;
/**
 * @file Finding.java
 * @author 贾焱超
 * @description 主页左侧菜单的发现页面，主要用于查找和检索好友以及热门微博
 */
public class Finding extends Activity {

	private RelativeLayout people, blog, app = null;
	private TextView hot, tp2, tp3 = null;
	private Button audio = null;
	private Intent i = null;
	private ImageView back = null;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_finding);
		mContext = this;
		i = new Intent();
		people = (RelativeLayout) findViewById(R.id.find_people);
		blog = (RelativeLayout) findViewById(R.id.find_hotblog);
		app = (RelativeLayout) findViewById(R.id.find_app);
		hot = (TextView) findViewById(R.id.find_hottopic_txt);
		tp2 = (TextView) findViewById(R.id.find_hot_topic2);
		tp3 = (TextView) findViewById(R.id.find_hot_topic3);
		audio = (Button) findViewById(R.id.finding_audio);
		back = (ImageView) findViewById(R.id.setting_inform_back);
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.find_people:
					// TODO click things
					i.putExtra("title", "找人");
					i.setClass(Finding.this, BlogDetail.class);
					startActivity(i);
					Finding.this.finish();
					break;
				case R.id.find_hotblog:
					// TODO click things
					i.putExtra("title", "找微博");
					i.setClass(Finding.this, BlogDetail.class);
					startActivity(i);
					Finding.this.finish();
					break;
				case R.id.find_app:
					// TODO click things
				//	i.putExtra("title", "应用");
					// i.setClass(Finding.this, BlogDetail.class);
					//startActivity(i);
					//Finding.this.finish();
					break;
				case R.id.find_hottopic_txt:
					// TODO click things
					Toast.makeText(mContext, "无实现API，用作后期拓展", Toast.LENGTH_SHORT).show();
					break;
				case R.id.find_hot_topic2:
					// TODO click things
					Toast.makeText(mContext, "无实现API，用作后期拓展", Toast.LENGTH_SHORT).show();
					break;
				case R.id.find_hot_topic3:
					// TODO click things
					Toast.makeText(mContext, "无实现API，用作后期拓展", Toast.LENGTH_SHORT).show();
					break;
				case R.id.finding_audio:
					// TODO click things

					break;
				case R.id.setting_inform_back:
					Intent i = new Intent(Finding.this, MainActivity.class);
					startActivity(i);
					Finding.this.finish();
					break;
				}
			}
		};
		people.setOnClickListener(ocl);
		blog.setOnClickListener(ocl);
		app.setOnClickListener(ocl);
		hot.setOnClickListener(ocl);
		tp2.setOnClickListener(ocl);
		tp3.setOnClickListener(ocl);
		audio.setOnClickListener(ocl);
		back.setOnClickListener(ocl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.finding, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Finding.this, MainActivity.class);
			startActivity(i);
			Finding.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
