package com.hudson.thinksns.develop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hudson.thinksns.Finding;
import com.hudson.thinksns.R;

/**
 * 
 * @author 王代银
 * 
 */
public class BlogDetail extends Activity {
	private EditText getkey;
	private Button gosea;
	private String first;
	private Intent intent;
	private ImageView back;
	private TextView toptitle, detail;
	SearchUser user_lay;
	SearchWeiBo weibo_lay;
	ShowSomeUer show_user_list;
	ShowSomeWeibo show_weibo_list;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.finding_blog_detail);
		init();
	}

	@SuppressLint("NewApi")
	void init() {
		intent = getIntent();
		toptitle = (TextView) findViewById(R.id.blogdetail_title);
		detail = (TextView) findViewById(R.id.finding_people_detail_text);
		first = intent.getStringExtra("title");
		user_lay = new SearchUser();
		weibo_lay = new SearchWeiBo();
		show_user_list = new ShowSomeUer();
		show_weibo_list = new ShowSomeWeibo();

		if (first.equals("找人")) {
			toptitle.setText("找人");
			detail.setText("我的粉丝列表");
			getFragmentManager().beginTransaction()
					.replace(R.id.blogdetail_list_frame, show_user_list)
					.commit();
		} else if (first.equals("找微博")) {
			toptitle.setText("找微博");
			detail.setText("我的好友微博列表");
			getFragmentManager().beginTransaction()
					.replace(R.id.blogdetail_list_frame, show_weibo_list)
					.commit();
		}

		// mlist = (ListView)findViewById(R.id.blogdetail_list);
		getkey = (EditText) findViewById(R.id.finding_people_input_txt);
		gosea = (Button) findViewById(R.id.finding_people_audio);
		gosea.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				DateToFragment.KEY_WORD = getkey.getText().toString();
				//实现多次搜索
				getFragmentManager().beginTransaction().remove(user_lay)
						.commit();
				getFragmentManager().beginTransaction().remove(weibo_lay)
						.commit();
				user_lay = new SearchUser();
				weibo_lay = new SearchWeiBo();
				//至此
				if (first.equals("找人")) {
					getFragmentManager().beginTransaction()
							.replace(R.id.blogdetail_list_frame, user_lay)
							.commit();
				} else if (first.equals("找微博")) {
					getFragmentManager().beginTransaction()
							.replace(R.id.blogdetail_list_frame, weibo_lay)
							.commit();
				}

			}
		});
		back = (ImageView) findViewById(R.id.setting_blogdetail_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(BlogDetail.this, Finding.class);
				startActivity(i);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(BlogDetail.this, Finding.class);
			startActivity(i);
			BlogDetail.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
